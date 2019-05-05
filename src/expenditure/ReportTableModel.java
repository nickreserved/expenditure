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

	/** Η τρέχουσα δαπάνη.
	 * Αν δεν είναι η τρέχουσα, όλα τα προσωρινά αποθηκευμένα δεδομένα (που τηρούνται για μεγαλύτερη
	 * ταχύτητα) πρέπει να επανυπολογιστούν. */
	private Expenditure expenditure;
	/** Αναφορά στις τιμές του ενεργού τιμολογίου. Αν δεν υπάρχει ενεργό τιμολόγιο είναι όλα 0. */
	private double[] invoice;
	/** Αναφορά στις τιμές της σύμβασης του ενεργού τιμολογίου.
	 * Αν δεν υπάρχει ενεργό τιμολόγιο ή δεν έχει σύμβαση, είναι όλα 0. */
	private double[] contract;

	/** Ενημερώνεται το μοντέλο ότι το ενεργό τιμολόγιο άλλαξε.
	 * Επειδή κάποια δεδομένα αποθηκεύονται προσωρινά για μεγαλύτερη ταχύτητα, η αλλαγή του ενεργού
	 * τιμολογίου οδηγεί σε ανανέωση αυτών των δεδομένων. */
	void changedInvoiceSelection(int row) {
		if (row >= 0 && row < expenditure.invoices.size()) {
			Invoice i = expenditure.invoices.get(row);
			invoice = i.prices;
			Contract c = i.getContract();
			contract = c != null ? c.prices : new double[7];
		} else contract = invoice = new double[7];
	}

	/** Ελέγχει τη δαπάνη που είναι ανοικτή στο πρόγραμμα.
	 * Αν διαφέρει από την προσωρινά αποθηκευμένη στο μοντέλο (για λόγους ταχύτητας) τότε διαγράφει
	 * όλα τα προσωρινά αποθηκευμένα δεδομένα. */
	private void recheck() {
		Expenditure e = data.getActiveExpenditure();
		if (e != expenditure) {
			expenditure = e;
			invoice = contract = new double[7];
		}
	}

	@Override public void addTableModelListener(TableModelListener l) {}
	@Override public void removeTableModelListener(TableModelListener l) {}
	@Override public int getRowCount() { return VERTICAL_HEADER.length; }
	@Override public int getColumnCount() { return HORIZONTAL_HEADER.length; }
	@Override public String getColumnName(int columnIndex) { return HORIZONTAL_HEADER[columnIndex]; }
	@Override public Class<?> getColumnClass(int columnIndex) { return String.class; }
	@Override public boolean isCellEditable(int row, int col) { return false; }
	@Override public void setValueAt(Object aValue, int rowIndex, int columnIndex) {}
	@Override public Object getValueAt(int row, int col) {
		switch(col) {
			case 0: return VERTICAL_HEADER[row];
			case 1: recheck(); return a(invoice[row]);
			case 2: recheck(); return a(contract[row]);
			default: recheck(); return a(expenditure.prices[row]);
		}
	}
}
