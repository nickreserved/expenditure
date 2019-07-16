package expenditure;

import static expenditure.Expenditure.a;
import static expenditure.MainFrame.data;
import java.util.ArrayList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/** Δεδομένα πίνακα με τα στατιστικά των τιμολογίων της δαπάνης.
 * Περιλαμβάνει 4 στήλες:
 * <ul><li>Η πρώτη έχει τις επικεφαλίδες των ποσών (π.χ. Καθαρή Αξία).
 * <li>Η δεύτερη έχει τα ποσά του επιλεγμένου τιμολογίου.
 * <li>Η τρίτη έχει αθροιστικά τα ποσά των τιμολογίων που ανήκουν στην ίδια σύμβαση με το επιλεγμένο
 * τιμολόγιο.
 * <li>Η τέταρτη έχει αθροιστικά τα ποσά όλων των τιμολογίων.</ul> */
final class ReportTableModel implements TableModel {
	/** Οι επικεφαλίδες των γραμμών. */
	static final String[] VERTICAL_HEADER = {
		"Καθαρή Αξία", "ΦΠΑ", "Καταλογιστέο", "Κρατήσεις", "Πληρωτέο", "ΦΕ", "Υπόλοιπο Πληρωτέο"
	};
	/** Οι επικεφαλίδες των στηλών. */
	private static final String[] HORIZONTAL_HEADER = {
		null, "Τρέχον τιμολόγιο", "Τιμολόγια σύμβασης", "Τιμολόγια διαγωνισμού", "Όλα τα τιμολόγια"
	};

	/** Το τρέχον τιμολόγιο. */
	private Invoice invoice;

	/** Ενημερώνεται το μοντέλο ότι το ενεργό τιμολόγιο άλλαξε.
	 * @param i Το νέο τιμολόγιο */
	void setSelectedInvoice(Invoice i) {
		invoice = i;
		fireTableDataChanged(new TableModelEvent(this, 0, 6, 1));
		fireTableDataChanged(new TableModelEvent(this, 0, 6, 2));
		fireTableDataChanged(new TableModelEvent(this, 0, 6, 3));
	}

	/** Οι listeners για κάθε αλλαγή στα δεδομένα του πίνακα. */
	private final ArrayList<TableModelListener> listeners = new ArrayList<>();
	@Override final public void addTableModelListener(TableModelListener l) { listeners.add(l); }
	@Override final public void removeTableModelListener(TableModelListener l) { listeners.remove(l); }
	@Override public int getRowCount() { return VERTICAL_HEADER.length; }
	@Override public int getColumnCount() { return HORIZONTAL_HEADER.length; }
	@Override public String getColumnName(int columnIndex) { return HORIZONTAL_HEADER[columnIndex]; }
	@Override public Class<?> getColumnClass(int columnIndex) { return String.class; }
	@Override public boolean isCellEditable(int row, int col) { return false; }
	@Override public void setValueAt(Object aValue, int rowIndex, int columnIndex) {}
	@Override public Object getValueAt(int row, int col) {
		if (col == 0) return VERTICAL_HEADER[row];
		if (col == 4) return a(data.expenditure.prices[row]);
		if (invoice == null) return null;
		if (col == 1) return a(invoice.prices[row]);
		Contract contract = invoice.getContract();
		if (contract == null) return null;
		if (col == 2) return a(contract.prices[row]);
		Tender tender = contract.getTender();
		if (tender == null) return null;
		return a(tender.prices[row]);
	}

	/** Ενημερώνει όλους τους listeners ότι όλα τα δεδομένα του πίνακα άλλαξαν.
	 * @param event Το event δεδομένων */
	public void fireTableDataChanged(TableModelEvent event) {
		listeners.stream().forEach(i -> i.tableChanged(event));
	}
	/** Ενημερώνει όλους τους listeners ότι όλα τα δεδομένα του πίνακα άλλαξαν. */
	public void fireTableDataChanged() { fireTableDataChanged(new TableModelEvent(this)); }
}
