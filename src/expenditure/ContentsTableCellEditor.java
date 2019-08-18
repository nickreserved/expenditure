package expenditure;

import static expenditure.ContentItem.ONLY_LISTED;
import static expenditure.MainFrame.NOYES;
import static java.awt.Color.WHITE;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import static javax.swing.BorderFactory.createLineBorder;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;

/** Ο επεξεργαστής της στήλης του πλήθους του φύλλου καταχώρησης. */
final class ContentsTableCellEditor implements TableCellEditor {
	/** Το ενεργό component επεξεργασίας κελιών. */
	private JComponent c;
	/** Ο παροχέας εγγραφών. */
	final private ContentsTableModel model;
	/** Λίστα με τις επιλογές για προκαθορισμένα δικαιολογητικά. Όχι, Ναι, Μόνο Καταχώρηση. */
	static final private String[] NOYESLIST = new String[] { NOYES[0], NOYES[1], ONLY_LISTED };
	/** Λίστα με τις επιλογές για προκαθορισμένα δικαιολογητικά. Ναι, Μόνο Καταχώρηση. */
	static final private String[] YESLIST = new String[] { NOYES[1], ONLY_LISTED };

	/** Αρχικοποίηση του επεξεργαστή κελιών του πίνακα.
	 * Αφορά μόνο τη δεύτερη στήλη του πίνακα του φύλλου καταχώρησης.
	 * @param model Ο παροχέας εγγραφών του πίνακα του φύλλου καταχώρησης */
	ContentsTableCellEditor(ContentsTableModel model) { this.model = model; }

	@Override public Object getCellEditorValue() {
		return c instanceof JComboBox ? ((JComboBox) c).getSelectedItem() : ((JTextField) c).getText();
	}

	@Override public boolean isCellEditable(EventObject anEvent) {
		if (anEvent instanceof MouseEvent)	// Απαιτείται διπλό κλίκ για να ξεκινήσει η επεξεργασία
			return ((MouseEvent) anEvent).getClickCount() >= 2;
		return true;
	}

	@Override public boolean shouldSelectCell(EventObject anEvent) { return true; }

	@Override public boolean stopCellEditing() {
		ChangeEvent e = new ChangeEvent(this);
		// Επειδή το editingStopped() προσθαφαιρεί listeners πρέπει να χρησιμοποιήσουμε κλασικό for,
		// ειδάλλως έχουμε ConcurrentModificationException
		for (int z = 0; z < listeners.size(); ++z)
			listeners.get(z).editingStopped(e);
		return true;
	}

	@Override public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		boolean combo = false;
		List<ContentItem> lst = model.get();
		if (row < lst.size()) {
			ContentItem i = lst.get(row);
			if (!i.isUserDefined()) {
				Object[] list;
				if (i.hasChoiceNoYes()) list = NOYES;
				else if (i.hasChoiceNoYesList()) list = NOYESLIST;
				else list = YESLIST;
				c = new JComboBox(new DefaultComboBoxModel(list));
				((JComboBox) c).setSelectedItem(value);
				((JComboBox) c).addActionListener(e -> stopCellEditing());
				combo = true;
			}
		}
		if (!combo) {
			c = new JTextField((String) value);
			((JTextField) c).addActionListener(e -> stopCellEditing());
		}
		c.setBorder(createLineBorder(WHITE, 0));
		return c;
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
