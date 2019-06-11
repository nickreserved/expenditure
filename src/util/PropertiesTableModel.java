package util;

import static java.awt.Color.WHITE;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventObject;
import static javax.swing.BorderFactory.createMatteBorder;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelEvent;
import static javax.swing.event.TableModelEvent.HEADER_ROW;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

/** Μοντέλο πίνακα 2 στηλών, με την πρώτη στήλη να είναι οι επικεφαλίδες και τη δεύτερη οι τιμές. */
abstract public class PropertiesTableModel implements TableModel {
	/** Οι επικεφαλίδες των γραμμών. */
	private final String[] vHeader;
	/** Οι επικεφαλίδες των στηλών. Αν είναι null δεν υπάρχουν επικεφαλίδες. */
	private final String[] hHeader;

	/** Αρχικοποίηση του μοντέλου του πίνακα.
	 * @param vHeader Οι επικεφαλίδες των γραμμών
	 * @param hHeader Οι επικεφαλίδες των στηλών, χωρίς επικεφαλίδα για τη στήλη των επικεφαλίδων
	 * γραμμών. */
	public PropertiesTableModel(String[] vHeader, String[] hHeader) {
		this.vHeader = vHeader; this.hHeader = hHeader;
	}

	/** Αρχικοποίηση του μοντέλου του πίνακα.
	 * Ο πίνακας δεν έχει επικεφαλίδες στηλών.
	 * @param vHeader Οι επικεφαλίδες των γραμμών
	 * @param columns Ο αριθμός των στηλών του πίνακα, χωρίς τη στήλη επικεφαλίδων. */
	public PropertiesTableModel(String[] vHeader, int columns) {
		this(vHeader, new String[columns]);
	}

	@Override public int getColumnCount() { return hHeader.length + 1; }
	@Override public int getRowCount() { return vHeader.length; }
	@Override public String getColumnName(int col) { return col != 0 ? hHeader[col - 1] : null; }
	@Override public boolean isCellEditable(int row, int col) { return col != 0; }

	@Override public Object getValueAt(int row, int col) {
		if (col == 0) {
			if (vHeader != null) return vHeader[row];
		} else
			try { return get(col - 1).getCell(row); }	// try-catch: Επιπλέον ασφάλεια
			catch(RuntimeException e) {}
		return null;
	}

	@Override public void setValueAt(Object obj, int row, int col) {
		try {	// try-catch: Επιπλέον ασφάλεια
			get(col).setCell(row, obj);
			TableModelEvent e = new TableModelEvent(this, row, row, col);
			listeners.forEach(i -> i.tableChanged(e));
		}
		catch(RuntimeException ex) {}
	}

	@Override public Class<?> getColumnClass(int columnIndex) { return Object.class; }

	/** Οι listeners για κάθε αλλαγή στα δεδομένα του πίνακα. */
	private final ArrayList<TableModelListener> listeners = new ArrayList<>();
	@Override public void addTableModelListener(TableModelListener l) { listeners.add(l); }
	@Override public void removeTableModelListener(TableModelListener l) { listeners.remove(l); }

	/** Ενημερώνει όλους τους listeners ότι όλα τα δεδομένα του πίνακα άλλαξαν.
	 * @param event Το event δεδομένων */
	public void fireTableDataChanged(TableModelEvent event) {
		listeners.stream().forEach(i -> i.tableChanged(event));
	}

	/** Ενημερώνει όλους τους listeners ότι όλα τα δεδομένα του πίνακα άλλαξαν. */
	public void fireTableDataChanged() { fireTableDataChanged(new TableModelEvent(this)); }

	/** Ενημερώνει όλους τους listeners ότι η επικεφαλίδα και όλα τα δεδομένα του πίνακα άλλαξαν. */
	public void fireTableChanged() { fireTableDataChanged(new TableModelEvent(this, HEADER_ROW)); }


	/** Επιστρέφει ένα πίνακα με την πρώτη στήλη ως επικεφαλίδες.
	 * Θεωρώντας ότι ο πίνακας εμφανίζει αντικείμενα, κάθε αντικείμενο καταλαμβάνει μια στήλη στον
	 * πίνακα. Η πρώτη στήλη (επικεφαλίδες) είναι οι τίτλοι των πεδίων του αντικειμένου. Υπάρχει
	 * πάντως και γραμμή επικεφαλίδων, όπως στους κλασικούς πίνακες.
	 * <p>Το σύνηθες, είναι ο πίνακας να εμφανίζει μόνο ένα αντικείμενο, με τη λογική κλειδί-τιμή
	 * (δηλαδή 2 στήλες).
	 * @param model Το μοντέλο δεδομένων του πίνακα
	 * @param cmp Ένα component για κάθε γραμμή του πίνακα, που επεξεργάζεται το πεδίο στη
	 * συγκεκριμένη γραμμή, για όλα τα αντικείμενα. Αν κάποιο component στο array δωθεί null,
	 * αντικαθίσταται από ένα JTextField που είναι ο default επεξεργαστής κελιών. Αν η παράμετρος
	 * είναι null, τότε όλα τα πεδία επεξεργάζονται από JTextField. Αν τα components του array δεν
	 * επαρκούν για κάθε γραμμή του πίνακα, οι γραμμές που δεν έχουν component χρησιμοποιούν το
	 * τελευταίο του array.
	 * @return Ο πίνακας */
	static public JTable createTable(TableModel model, Component[] cmp) {
		JTable t = createTable(model);
		// Αφαίρεση της ΓΡΑΜΜΗΣ των επικεφαλίδων αν δεν απαιτείται
		if (model.getColumnName(1) == null) t.setTableHeader(null);
		// Αν απαιτούνται ειδικά components στην επεξεργασία των στοιχείων του πίνακα (π.χ. ComboBox)
		if (cmp != null) {
			TableCellEditor edit = new RichTableCellEditor(cmp);
			for (int z = 1; z < model.getColumnCount(); z++)
				t.getColumnModel().getColumn(z).setCellEditor(edit);
		}
		return t;
	}

	/** Επιστρέφει ένα πίνακα με την πρώτη στήλη ως επικεφαλίδες.
	 * Θεωρώντας ότι ο πίνακας εμφανίζει αντικείμενα, κάθε αντικείμενο καταλαμβάνει μια στήλη στον
	 * πίνακα. Η πρώτη στήλη (επικεφαλίδες) είναι οι τίτλοι των πεδίων του αντικειμένου. Υπάρχει
	 * πάντως και γραμμή επικεφαλίδων, όπως στους κλασικούς πίνακες.
	 * <p>Το σύνηθες, είναι ο πίνακας να εμφανίζει μόνο ένα αντικείμενο, με τη λογική κλειδί-τιμή
	 * (δηλαδή 2 στήλες).
	 * <p>Η κλήση αυτή είναι η πιο γενικευμένη από την ομάδα createTable().
	 * @param model Το μοντέλο δεδομένων του πίνακα
	 * @return Ο πίνακας */
	static public JTable createTable(TableModel model) {
		JTable t = new JTable(model);
		t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JTableHeader header = t.getTableHeader();
		header.setReorderingAllowed(false);
		// Η πρώτη ΣΤΗΛΗ με τις επικεφαλίδες, θα εμφανίζεται όπως και η επικεφαλίδα του πίνακα
		t.getColumnModel().getColumn(0).setCellRenderer((JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) -> {
			JLabel c = new JLabel(value != null ? value.toString() : null);
			c.setBackground(UIManager.getColor("TableHeader.background"));
			c.setOpaque(true);
			c.setForeground(UIManager.getColor("TableHeader.foreground"));
			return c;
		});
		return t;
	}


	/** Μια στήλη ενός πίνακα που αντιστοιχεί σε ένα αντικείμενο.
	 * Μια λίστα από αντικείμενα παρουσιάζεται σε ένα πίνακα, όπου κάθε στήλη του πίνακα είναι ένα
	 * αντικείμενο και κάθε γραμμή το αντίστοιχο πεδίο των αντικειμένων. Το TableRecord είναι ένα από τα
	 * αντικείμενα της λίστας, δηλαδή μια στήλη του πίνακα. */
	public static interface TableRecord {

		/** Επιστρέφει το πεδίο του αντικειμένου που αντιστοιχεί σε συγκεκριμένη στήλη του πίνακα.
		 * @param index Το index της στήλης του πίνακα (ή γραμμής σύμφωνα με το ενναλλακτικό μοντέλο)
		 * @return Η τιμή του αντίστοιχου πεδίου του αντικειμένου */
		Object getCell(int index);

		/** Θέτει το πεδίο του αντικειμένου που αντιστοιχεί σε συγκεκριμένη στήλη του πίνακα.
		 * @param index Το index της στήλης του πίνακα (ή γραμμής σύμφωνα με το ενναλλακτικό μοντέλο)
		 * @param value Η τιμή για το αντίστοιχο πεδίο του αντικειμένου */
		void setCell(int index, Object value);
	}


	/** Ο παροχέας μιας εγγραφής για πίνακα-στήλη.
	 * @param index Το index του αντικειμένου
	 * @return Η εγγραφή */
	public abstract TableRecord get(int index);


	/** Λειτουργικότητα επεξεργασίας κελιών του πίνακα με μοντέλο PropertiesTableModel.
	 * Υποστηρίζει δυνατότητα διαφορετικού component επεξεργασίας για κάθε γραμμή. Ωστόσο αυτή τη στιγμή
	 * μόνο 2 τύποι υποστηρίζονται: JComboBox και JTextField. Θα μπορούσαν να υποστηριχθούν πάρα πολλοί,
	 * όπως π.χ. JCheckBox ή JSlider, αλλά δεν τους χρειάζεται στην παρούσα φάση το πρόγραμμα. */
	static private class RichTableCellEditor implements TableCellEditor {
		/** Λίστα με τα components επεξεργασίας κελιών, ένα για κάθε γραμμή του πίνακα. */
		private final Component[] handlers;
		/** Το ενεργό component επεξεργασίας κελιών. */
		private Component current;
		/** Ένας ActionListener που τερματίζει την επεξεργασία του κελιού. */
		private final ActionListener actionListener = e -> stopCellEditing();

		/** Αρχικοποίηση της λειτουργικότητας.
		 * @param components Λίστα με τα components επεξεργασίας κελιών, ένα για κάθε γραμμή του πίνακα.
		 * Αν κάποιο είναι null, αντικαθίσταται με το default component επεξεργασίας κελιών που είναι το
		 * JTextField. Σε όλα τα components ορίζεται ένας ActionListener. Επειδή μόνο JTextField και
		 * JComponent επιτρέπονται, το λογικό είναι ότι η λίστα έχει μόνο JComponent ή null. */
		public RichTableCellEditor(Component[] components) {
			// Για κάθε null τιμή του components, θα εμφανίζεται ένα text field
			JTextField text = new JTextField();	// Δεν χρειάζεται listener
			text.setBorder(createMatteBorder(1,1,0,0,WHITE));
			// Τα null του components αντικαθίστανται από το default text field
			// Στα μη null, δεν προστίθεται listener γιατί μπορεί να είναι πολλά ίδια μεταξύ τους
			// και να προστεθεί στο ίδιο component ο ίδιος listener πολλές φορές. Αντί αυτού
			// προστίθεται listener όταν ξεκινάει η επεξεργασία και αφαιρείται όταν τελειώνει
			handlers = components;
			for (int z = 0; z < handlers.length; ++z)
				if (handlers[z] == null) handlers[z] = text;
		}

		@Override public Object getCellEditorValue() {
			if (current instanceof JTextField) return ((JTextField) current).getText();
			if (current instanceof JComboBox) return ((JComboBox) current).getSelectedItem();
			if (current instanceof JCheckBox) return ((JCheckBox) current).isSelected();
			return null;
		}

		@Override public boolean isCellEditable(EventObject anEvent) {
			if (anEvent instanceof MouseEvent)	// Απαιτείται διπλό κλίκ για να ξεκινήσει η επεξεργασία
				return ((MouseEvent) anEvent).getClickCount() >= 2;
			return true;
		}

		@Override public boolean shouldSelectCell(EventObject anEvent) { return true; }

		@Override public boolean stopCellEditing() {
			if (current instanceof JComboBox) ((JComboBox) current).removeActionListener(actionListener);
			if (!listeners.isEmpty()) {
				ChangeEvent e = new ChangeEvent(this);
				// Επειδή το editingStopped() προσθαφαιρεί listeners πρέπει να κάνουμε αντιγραφή τους
				// listeners πριν τους ενημερώσουμε, ειδάλλως έχουμε ConcurrentModificationException
				CellEditorListener[] ar = listeners.toArray(new CellEditorListener[listeners.size()]);
				for (CellEditorListener l : ar) l.editingStopped(e);
			}
			return true;
		}

		@Override public Component getTableCellEditorComponent(JTable table, Object value,
				boolean isSelected, int row, int column) {
			current = row < handlers.length ? handlers[row] : handlers[handlers.length - 1];
			if (current instanceof JTextField)
				((JTextField) current).setText(value != null ? value.toString() : null);
			else if (current instanceof JComboBox) {
				((JComboBox) current).setSelectedItem(value);
				((JComboBox) current).addActionListener(actionListener);
			} else if (current instanceof JCheckBox)
				((JCheckBox) current).setSelected(value instanceof Boolean && (Boolean) value);
			return current;
		}

		// Είναι αστείο, αλλά λόγω ενός bug της Java δεν καλείται ποτέ
		// https://bugs.java.com/bugdatabase/view_bug.do?bug_id=6788481
		@Override public void cancelCellEditing() {
			ChangeEvent e = new ChangeEvent(this);
			listeners.forEach(i -> i.editingCanceled(e));
		}

		/** Οι listeners για τον τερματισμό ή ακύρωση της επεξεργασίας. */
		final private ArrayList<CellEditorListener> listeners = new ArrayList<>();
		@Override public void addCellEditorListener(CellEditorListener l) { listeners.add(l); }
		@Override public void removeCellEditorListener(CellEditorListener l) { listeners.remove(l); }
	}
}