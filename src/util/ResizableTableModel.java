package util;

import expenditure.MainFrame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import static java.awt.event.KeyEvent.VK_DELETE;
import static java.awt.event.KeyEvent.VK_INSERT;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import static java.awt.event.MouseEvent.BUTTON3;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.JMenuItem;
import static javax.swing.JOptionPane.WARNING_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.YES_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import static javax.swing.KeyStroke.getKeyStroke;
import javax.swing.event.TableModelEvent;
import static javax.swing.event.TableModelEvent.ALL_COLUMNS;
import static javax.swing.event.TableModelEvent.DELETE;
import static javax.swing.event.TableModelEvent.HEADER_ROW;
import static javax.swing.event.TableModelEvent.INSERT;
import static javax.swing.event.TableModelEvent.UPDATE;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import util.ResizableTableModel.TableRecord;

/** Το μοντέλο του πίνακα που αλλάζει αριθμό εγγραφών-γραμμών.
 * @param <T> Ο τύπος δεδομένων της εγγραφής-γραμμής του μοντέλου του πίνακα */
public class ResizableTableModel<T extends TableRecord> implements TableModel {
	/** Ο παροχέας εγγραφών. */
	protected final TableData<T> d;
	/** Λίστα με τις επικεφαλίδες των στηλών του πίνακα. */
	private final List<String> header;

	/** Δημιουργία του μοντέλου του πίνακα.
	 * @param data Ο παροχέας εγγραφών
	 * @param headers Οι επικεφαλίδες των στηλών του πίνακα */
	public ResizableTableModel(TableData<T> data, String[] headers) {
		this(data, Arrays.asList(headers));
	}

	/** Δημιουργία του μοντέλου του πίνακα.
	 * @param data Ο παροχέας εγγραφών
	 * @param headers Οι επικεφαλίδες των στηλών του πίνακα */
	public ResizableTableModel(TableData<T> data, List<String> headers) {
		d = data; header = headers;
	}

	/** Οι listeners για κάθε αλλαγή στα δεδομένα του πίνακα. */
	private final ArrayList<TableModelListener> listeners = new ArrayList<>();
	@Override final public void addTableModelListener(TableModelListener l) { listeners.add(l); }
	@Override final public void removeTableModelListener(TableModelListener l) { listeners.remove(l); }
	@Override public Class getColumnClass(int columnIndex) { return String.class; }
	@Override public int getColumnCount() { return header.size(); }
	@Override public int getRowCount() { return d.get() == null ? 0 : d.get().size() + 1; }
	@Override public String getColumnName(int col) { return header.get(col); }
	@Override public boolean isCellEditable(int row, int col) { return true; }
	@Override public Object getValueAt(int row, int col) {
		List<T> ar = d.get();
		return row < ar.size() ? ar.get(row).getCell(col) : null;
	}

	@Override public void setValueAt(Object obj, int row, int col) {
		List<T> ar = d.get();
		TableModelEvent e = null;
		// Table must append a row with a new object
		if (row == ar.size()) {
			ar.add(d.createNew());
			e = new TableModelEvent(this, row, row, ALL_COLUMNS, INSERT);
		}
		// set cell data to object
		TableRecord tv = ar.get(row);
		try {	// Extra security. There is no need, but exists.
			tv.setCell(col, obj);
			if (e == null) e = new TableModelEvent(this, row, row, col);	// UPDATE
		} catch (RuntimeException ex) {}
		// remove row if object is empty
		if (tv.isEmpty()) {
			d.remove(row);
			if (e == null || e.getType() == UPDATE)
				e = new TableModelEvent(this, row, row, ALL_COLUMNS, DELETE);
			else if (e.getType() == INSERT) e = null;	// INSERT και DELETE εξουδετερώνονται
		}
		if (e != null) fireTableDataChanged(e);
	}

	/** Ενημερώνει όλους τους listeners ότι όλα τα δεδομένα του πίνακα άλλαξαν.
	 * @param event Το event δεδομένων */
	public void fireTableDataChanged(TableModelEvent event) {
		listeners.stream().forEach(i -> i.tableChanged(event));
	}

	/** Ενημερώνει όλους τους listeners ότι όλα τα δεδομένα του πίνακα άλλαξαν. */
	public void fireTableDataChanged() { fireTableDataChanged(new TableModelEvent(this)); }

	/** Ενημερώνει όλους τους listeners ότι η επικεφαλίδα και όλα τα δεδομένα του πίνακα άλλαξαν. */
	public void fireTableChanged() { fireTableDataChanged(new TableModelEvent(this, HEADER_ROW)); }

	/** Ταξινόμηση των δεδομένων του πίνακα ως προς μια στήλη.
	 * @param column Το index της στήλης ως προς την οποία θα ταξινομηθούν τα δεδομένα του πίνακα */
	private void sort(int column) {
		List<T> ar = d.get();
		if (ar == null) return;
		Collections.sort(ar,	// Ταξινόμηση των εγγραφών κατά τη στήλη που κάναμε κλικ, αύξουσα σειρά
			(TableRecord c, TableRecord b) -> {
				Object aa = c.getCell(column);
				Object bb = b.getCell(column);
				if (aa == null) return bb == null ? 0 : -1;
				if (bb == null) return 1;
				try { return ((Comparable) aa).compareTo(bb); }
				catch(RuntimeException ex) { return 0; }
			});
	}

	/** Προσθήκη κενής εγγραφής πάνω από δεδομένη εγγραφή.
	 * @param row Το index της εγγραφής-γραμμής πάνω από την οποία θα προστεθεί η νέα κενή εγγραφή */
	private void insertRow(int row) {
		List<T> ar = d.get();
		if (row >= 0 && row < ar.size() && !ar.get(row).isEmpty() && (row == 0 || !ar.get(row - 1).isEmpty())) {
			ar.add(row, d.createNew());
			fireTableDataChanged(new TableModelEvent(this, row, row, ALL_COLUMNS, INSERT));
		}
	}

	/** Διαγραφή εγγραφών με επιβεβαίωση.
	 * @param rows Τα indices των εγγραφών-γραμμών που θα διαγραφούν */
	private void deleteRows(int[] rows) {
		if (rows.length == 0) return;
		int length = rows.length;
		List<T> ar = d.get();
		if (rows[rows.length - 1] >= ar.size()) --length;
		if (length > 0 && YES_OPTION == showConfirmDialog(null, "Θέλετε να διαγράψω τις επιλεγμένες εγγραφές;",
				"Διαγραφή εγγραφών", YES_NO_OPTION, WARNING_MESSAGE)) {
			// Αντίστροφη διαγραφή, αλλιώς θα έχουμε index out of bounds
			for (int z = length - 1; z >= 0; --z) d.remove(rows[z]);
			fireTableDataChanged(new TableModelEvent(this, rows[0], rows[length - 1], ALL_COLUMNS, DELETE));
		}
	}

	/** Δημιουργία πίνακα με δυνατότητα προσθήκης νέων εγγραφών-γραμμών.
	 * @param model Το μοντέλο διαχείρισης δεδομένων του πίνακα
	 * @param insert Η δυνατότητα προσθήκης γραμμών μεταξύ δύο άλλων, είναι ενεργή
	 * @param sort Η δυνατότητα ταξινόμησης σε μια στήλη, είναι ενεργή
	 * @return Ο πίνακας */
	static public JTable createTable(ResizableTableModel model, boolean insert, boolean sort) {
		// Προσοχή! Στις παρακάτω lambda functions δε χρησιμοποιούμε το model γιατί μπορεί στο μεταξύ
		// να τροποποιηθεί με την JTable.setModel()
		JTable t = new JTable(model);

		// Popup menu για εισαγωγή και διαγραφή γραμμής
		JPopupMenu popupMenu = new JPopupMenu();
		if (insert)
			popupMenu.add(createMenuItem("Εισαγωγή κενής γραμμής", "new", VK_INSERT, (ActionEvent e)
					-> ((ResizableTableModel) t.getModel()).insertRow(t.getSelectedRow())));
		popupMenu.add(createMenuItem("Διαγραφή επιλεγμένων γραμμών", "close", VK_DELETE, (ActionEvent e)
				-> ((ResizableTableModel) t.getModel()).deleteRows(t.getSelectedRows())));
		t.setComponentPopupMenu(popupMenu);

		t.addMouseListener(new MouseListener() {
			@Override public void mousePressed(MouseEvent e) {
				// Στο δεξί κλικ εμφανίζεται το popup menu και πρέπει να είναι
				// επιλεγμένη μια γραμμή οπωσδήποτε.
				if (e.getButton() == BUTTON3 && t.getSelectedRow() == -1) {
					Point point = e.getPoint();
					int currentRow = t.rowAtPoint(point);
					t.setRowSelectionInterval(currentRow, currentRow);
				}
			}
			@Override public void mouseReleased(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) {}
			@Override public void mouseClicked(MouseEvent e) {}
		});

		t.addKeyListener(new KeyListener() {
			@Override public void keyPressed(KeyEvent e) {
				// Αν υπάρχουν 2 menu items, τότε είναι το insert και το delete. Αλλιώς είναι μόνο το delete
				if (e.getKeyCode() == KeyEvent.VK_INSERT && t.getComponentPopupMenu().getComponentCount() == 2) {
					e.consume(); ((ResizableTableModel) t.getModel()).insertRow(t.getSelectedRow());
				} else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
					e.consume(); ((ResizableTableModel) t.getModel()).deleteRows(t.getSelectedRows());
				}
			}
			@Override public void keyReleased(KeyEvent e) {}
			@Override public void keyTyped(KeyEvent e) {}
		});

		if (sort)
			t.getTableHeader().addMouseListener(new MouseAdapter() {
				// Κάνοντας κλικ σε μια στήλη στην επικεφαλίδα του πίνακα, ταξινομείται η συγκεκριμένη στήλη
				@Override public void mouseClicked(MouseEvent e) {
					Point point = e.getPoint();
					int a = t.getTableHeader().columnAtPoint(point);
					a = t.convertColumnIndexToModel(a);
					((ResizableTableModel) t.getModel()).sort(a);
				}
			});
		return t;
	}

	/** Δημιουργεί μια επιλογή μενού.
	 * @param name Το όνομα της επιλογής μενού, όπως θα εμφανίζεται στη γραμμή μενού του προγράμματος
	 * @param icon Ένα εικονίδιο για την επιλογή μενού ή null
	 * @param key Ένα πλήκρο ως συντόμευση της επιλογής μενού
	 * @param action Ένας listener που θα εκτελείται όταν επιλεγεί η επιλογή από το μενου
	 * @return Η επιλογή του μενού */
	static private JMenuItem createMenuItem(String name, String icon, int key, ActionListener action) {
		JMenuItem i = MainFrame.createMenuItem(name, icon, action);
		i.setAccelerator(getKeyStroke(key, 0));
		return i;
	}

	/** Επιστρέφει ένα String από ένα Object.
	 * Αν το object δεν είναι String ή έχει μόνο χαρακτήρες διαστήματος, επιστρέφει null.
	 * @param o Το Object
	 * @return To String ή null */
	static public String getString(Object o) {
		if (o instanceof String) {
			String s = ((String) o).trim();	// JDK11: s.isBlank()
			if (!s.isEmpty()) return s;
		}
		return null;
	}

	/** Επιστρέφει ένα double από ένα Object.
	 * Αν το Object δεν είναι String αριθμός ή Number, επιστρέφει 0.
	 * @param o Το Object
	 * @return Ο double */
	static public double getDouble(Object o) {
		if (o instanceof Number) return ((Number) o).doubleValue();
		else if (o instanceof String)
			try { return Double.parseDouble((String) o); } catch(NumberFormatException ex) {}
		return 0;
	}

	/** Επιστρέφει ένα long από ένα Object.
	 * Αν το Object δεν είναι String αριθμός ή Number, επιστρέφει 0.
	 * @param o Το Object
	 * @return Ο long */
	static public long getLong(Object o) {
		if (o instanceof Number) return ((Number) o).longValue();
		else if (o instanceof String)
			try { return Long.parseLong((String) o); } catch(NumberFormatException ex) {}
		return 0;
	}

	/** Επιστρέφει ένα byte από ένα Object.
	 * Αν το Object δεν είναι String αριθμός ή Number, επιστρέφει 0.
	 * @param o Το Object
	 * @return Το byte */
	static public byte getByte(Object o) {
		if (o instanceof Number) return ((Number) o).byteValue();
		else if (o instanceof String)
			try { return Byte.parseByte((String) o); } catch(NumberFormatException ex) {}
		return 0;
	}


	/** Μια γραμμή ενός πίνακα που αντιστοιχεί σε ένα αντικείμενο.
	 * Μια λίστα από αντικείμενα παρουσιάζεται σε ένα πίνακα, όπου κάθε γραμμή του πίνακα είναι ένα
	 * αντικείμενο και κάθε στήλη το αντίστοιχο πεδίο των αντικειμένων. Το TableRecord είναι ένα από
	 * τα αντικείμενα της λίστας, δηλαδή μια γραμμή του πίνακα. */
	public static interface TableRecord {
		/** Επιστρέφει το πεδίο του αντικειμένου που αντιστοιχεί σε συγκεκριμένη στήλη του πίνακα.
		 * @param index Το index της στήλης του πίνακα (ή γραμμής σύμφωνα με το ενναλλακτικό μοντέλο)
		 * @return Η τιμή του αντίστοιχου πεδίου του αντικειμένου */
		Object getCell(int index);

		/** Θέτει το πεδίο του αντικειμένου που αντιστοιχεί σε συγκεκριμένη στήλη του πίνακα.
		 * @param index Το index της στήλης του πίνακα (ή γραμμής σύμφωνα με το ενναλλακτικό μοντέλο)
		 * @param value Η τιμή για το αντίστοιχο πεδίο του αντικειμένου */
		void setCell(int index, Object value);

		/** Το αντικείμενο μπορεί να θεωρηθεί κενό.
		 * @return Αν κάποια σημαντικά πεδία του αντικειμένου ειναι κενά, τότε μπορεί όλο το
		 * αντικείμενο να θεωρηθεί κενό. Αυτό είναι χρήσιμο όταν π.χ. σβήσουμε όλα τα σημαντικά
		 * πεδία ενός αντικειμένου σε ένα πίνακα, τότε η γραμμή του πίνακα διαγράφεται αυτόματα. */
		boolean isEmpty();
	}

	/** Ο παροχέας εγγραφών για πίνακα.
	 * Κληρονομεί από το ArrayData προκειμένου να χρησιμοποιείται ο ίδιος παροχέας τόσο σε πίνακα όσο
	 * και σε ComboBox.
	 * @param <T> Ο τύπος δεδομένων της εγγραφής-γραμμής του μοντέλου του πίνακα */
	public interface TableData<T extends TableRecord> {
		/** Επιστρέφει μια λίστα με εγγραφές.
		 * @return Η λίστα με τις εγγραφές. */
		List<T> get();
		/** Επιστρέφει μια νέα κενή εγγραφή.
		 * @return Η νέα κενή εγγραφή. */
		default T createNew() { return null; }
		/** Διαγράφει ένα στοιχείο από τη λίστα.
		 * Η χρησιμότητά της είναι να εκτελεί επιπρόσθετο κώδικα όταν ένα στοιχείο διαγράφεται.
		 * @param index Ο δείκτης του στοιχείου που θα διαγραφεί, στη λίστα */
		default void remove(int index) { get().remove(index); }
	}
}