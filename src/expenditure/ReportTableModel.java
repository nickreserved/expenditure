package expenditure;

import static expenditure.Expenditure.a;
import static expenditure.MainFrame.data;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/** Δεδομένα πίνακα με τα στατιστικά των τιμολογίων της δαπάνης.
 * Περιλαμβάνει 4 στήλες:
 * <ul><li>Η πρώτη έχει τις επικεφαλίδες των ποσών (π.χ. Καθαρή Αξία).
 * <li>Η δεύτερη έχει τα ποσά του επιλεγμένου τιμολογίου.
 * <li>Η τρίτη έχει αθροιστικά τα ποσά των τιμολογίων που ανήκουν στην ίδια σύμβαση με το επιλεγμένο
 * τιμολόγιο.
 * <li>Η τέταρτη έχει αθροιστικά τα ποσά όλων των τιμολογίων.</ul> */
class ReportTableModel implements TableModel {
	/** Οι επικεφαλίδες των γραμμών. */
	static final String[] VERTICAL_HEADER = {
		"Καθαρή Αξία", "ΦΠΑ", "Καταλογιστέο", "Κρατήσεις", "Πληρωτέο", "ΦΕ", "Υπόλοιπο Πληρωτέο"
	};
	/** Οι επικεφαλίδες των στηλών. */
	private static final String[] HORIZONTAL_HEADER = {
		null, "Τρέχον τιμολόγιο", "Τιμολόγια σύμβασης", "Όλα τα τιμολόγια"
	};

	/** Το τρέχον τιμολόγιο. */
	private Invoice inv;

	/** Ενημερώνεται το μοντέλο ότι το ενεργό τιμολόγιο άλλαξε.
	 * @param i Το νέο τιμολόγιο */
	void changedInvoiceSelection(Invoice i) { inv = i; }

	@Override public void addTableModelListener(TableModelListener l) {}
	@Override public void removeTableModelListener(TableModelListener l) {}
	@Override public int getRowCount() { return VERTICAL_HEADER.length; }
	@Override public int getColumnCount() { return HORIZONTAL_HEADER.length; }
	@Override public String getColumnName(int columnIndex) { return HORIZONTAL_HEADER[columnIndex]; }
	@Override public Class<?> getColumnClass(int columnIndex) { return String.class; }
	@Override public boolean isCellEditable(int row, int col) { return false; }
	@Override public void setValueAt(Object aValue, int rowIndex, int columnIndex) {}
	@Override public Object getValueAt(int row, int col) {
		if (col == 0) return VERTICAL_HEADER[row];
		if (col == 3) return a(data.getActiveExpenditure().prices[row]);
		if (inv == null) return null;
		if (col == 1) return a(inv.prices[row]);
		if (inv.getContract() == null) return null;
		return a(inv.getContract().prices[row]);
	}
}
