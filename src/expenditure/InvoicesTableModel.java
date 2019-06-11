package expenditure;

import static expenditure.MainFrame.data;
import java.util.List;
import util.ResizableHeaderTableModel;

/** Το μοντέλο του πίνακα τιμολογίων. */
final class InvoicesTableModel extends ResizableHeaderTableModel<Invoice> {
	/** Συντόμευση στην ενεργή δαπάνη. */
	private Expenditure expenditure;
	/** Συντόμευση στη λίστα τιμολογίων της ενεργής δαπάνης. */
	private List<Invoice> list;
	/** Δημιουργία του μοντέλου του πίνακα. */
	InvoicesTableModel() {
		super(new String[] {
			Invoice.H[0], Invoice.H[1], Invoice.H[2] + " ή Σύμβαση", Invoice.H[3], Invoice.H[4]
		});
		updateExpenditure();
	}
	/** Ενημερώνει το μοντέλο ότι η ενεργή δαπάνη άλλαξε. */
	private void updateExpenditure() {
		if (data.isEmpty()) { expenditure = null; list = null; }
		else { expenditure = data.getActiveExpenditure(); list = expenditure.invoices; }
	}
	@Override protected List<Invoice> get() { return list; }
	@Override protected Invoice createNew() { return new Invoice(expenditure); }
	@Override public void remove(int index) { list.get(index).recalcRemove(); list.remove(index); }
	@Override public void fireTableDataChanged() { updateExpenditure(); super.fireTableDataChanged(); }
	@Override public boolean isCellEditable(int row, int col) {
		// Αν οι αυτόματοι υπολογισμοί είναι ενεργοί, τα κελιά των κρατήσεων και ΦΕ
		// δεν επεξεργάζονται
		return col != 3 && col != 4 || !expenditure.isSmart();
	}
}