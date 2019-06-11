package util;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.TableModelEvent;
import static javax.swing.event.TableModelEvent.ALL_COLUMNS;
import static javax.swing.event.TableModelEvent.DELETE;
import static javax.swing.event.TableModelEvent.INSERT;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/** Μοντέλο πίνακα 2 στηλών, με την πρώτη στήλη να είναι οι επικεφαλίδες και τη δεύτερη οι τιμές.
 * Λειτουργεί σε συνδυασμό με εναν επιλογέα εγγραφών, ο οποίος καθορίζει το index της εγγραφής που
 * προβάλεται. Ο επιλογέας εγγραφών μπορεί να είναι μια λίστα, ένα combobox, ένας άλλος πίνακας κτλ.
 * @param <T> Ο τύπος των εγγραφών τα πεδία των οποίων προβάλονται στις γραμμές του πίνακα */
abstract public class ResizablePropertiesTableModel<T extends ResizableTableModel.TableRecord>
		implements TableModel {
	/** Οι επικεφαλίδες των γραμμών. */
	private final String[] vHeader;
	/** Ο δείκτης στην τρέχουσα εγγραφή ή -1 αν δεν υπάρχει επιλεγμένη. */
	private int index = -1;
	/** Συντόμευση στην τρέχουσα ενεργή εγγραφή. */
	private T record;

	/** Αρχικοποίηση του μοντέλου του πίνακα.
	 * @param vHeader Οι επικεφαλίδες των γραμμών */
	public ResizablePropertiesTableModel(String[] vHeader) { this.vHeader = vHeader; }

	/** Επιστρέφει μια λίστα με εγγραφές.
	 * Κάθε εγγραφή, είναι μια γραμμή στον πίνακα.
	 * @return Η λίστα με τις εγγραφές. */
	abstract protected List<T> get();
	/** Επιστρέφει μια νέα κενή εγγραφή.
	 * @return Η νέα κενή εγγραφή. */
	abstract protected T createNew();
	/** Διαγράφει ένα στοιχείο από τη λίστα.
	 * Η χρησιμότητά της είναι να εκτελεί επιπρόσθετο κώδικα όταν ένα στοιχείο διαγράφεται.
	 * @param index Ο δείκτης του στοιχείου που θα διαγραφεί, στη λίστα */
	protected void remove(int index) { get().remove(index); }

	@Override public int getColumnCount() { return 2; }
	@Override public int getRowCount() { return vHeader.length; }
	@Override public String getColumnName(int col) { return null; }
	@Override public boolean isCellEditable(int row, int col) { return col != 0 && index != -1; }

	@Override public Object getValueAt(int row, int col) {
		if (col == 0) return vHeader[row];
		if (record != null)
			try {	// try-catch: Επιπλέον ασφάλεια
				return record.getCell(row);
			} catch(RuntimeException e) {}
		return null;
	}

	@Override public void setValueAt(Object obj, int row, int col) {
		if (index == -1) return;		// Καμία εγγραφή δεν είναι επιλεγμένη
		// Συμβάν για τον τρέχοντα πίνακα αλλά και για τον επιλογέα εγγραφών
		TableModelEvent selector = null, cur = new TableModelEvent(this, row, row, col);
		if (record == null) {
			record = createNew();
			get().add(record);
			selector = new TableModelEvent(this, index, index, ALL_COLUMNS, INSERT);
			// Όλες οι γραμμές επειδή κάποια πεδία του φρεσκοδημιουργηθέντος μπορεί να μην είναι
			// null κατά την αρχικοποίηση, οπότε πρέπει να εμφανιστούν
			cur = new TableModelEvent(this, 0, vHeader.length - 1, col);
		}
		try {	// try-catch: Επιπλέον ασφάλεια
			record.setCell(row, obj);
		} catch(RuntimeException ex) {}
		if (record.isEmpty()) {
			remove(index);
			if (selector != null) selector = null;
			else selector = new TableModelEvent(this, index, index, ALL_COLUMNS, DELETE);
		}
		if (selector == null) selector = new TableModelEvent(this, index);
		TableModelEvent cur2 = cur, selector2 = selector;	// hack
		listeners.forEach(i -> i.tableChanged(cur2));
		clisteners.forEach(i -> i.tableChanged(selector2));
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
	public void fireTableDataChanged() {
		fireTableDataChanged(new TableModelEvent(this, 0, vHeader.length - 1, 1));
	}

	/** Οι listeners για την ενημέρωση του επιλογέα εγγραφών. */
	private final ArrayList<TableModelListener> clisteners = new ArrayList<>();
	/** Πρόσθεση listener για την ενημέρωση του επιλογέα εγγραφών.
	 * @param l Ο listener */
	public void addSelectorTableModelListener(TableModelListener l) { clisteners.add(l); }
	/** Αφαίρεση listener για την ενημέρωση του επιλογέα εγγραφών.
	 * @param l Ο listener */
	public void removeSelectorTableModelListener(TableModelListener l) { clisteners.remove(l); }

	/** Θέτει το δείκτη της τρέχουσας εγγραφής του επιλογέα.
	 * @param index Ο δείκτης της τρέχουσας εγγραφής στον επιλογέα */
	public void setIndex(int index) {
		this.index = index;
		if (index != -1) {
			List<T> list = get();
			record = index < list.size() ? list.get(index) : null;
		}
		fireTableDataChanged();
	}
}