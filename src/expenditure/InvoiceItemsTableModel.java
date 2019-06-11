package expenditure;

import java.util.List;
import util.ResizableHeaderTableModel;

/** Το μοντέλο του πίνακα υλικών εργασιών. */
final class InvoiceItemsTableModel extends ResizableHeaderTableModel<InvoiceItem> {
	/** Συντόμευση στο ενεργό τιμολόγιο. */
	private Invoice invoice;
	/** Συντόμευση στη λίστα ειδών του ενεργού τιμολογίου. */
	private List<InvoiceItem> list;

	/** Δημιουργία του μοντέλου του πίνακα. */
	InvoiceItemsTableModel() { super(InvoiceItem.H); }

	@Override protected List<InvoiceItem> get() { return list; }
	@Override protected InvoiceItem createNew() { return new InvoiceItem(invoice); }
	@Override public void remove(int index) { list.remove(index).parent.recalcFromItems(); }

	/** Ενημερώνεται για την επιλογή του τρέχοντος επιλεγμένου τιμολογίου στον πίνακα τιμολογίων.
	 * Ο πίνακας τιμολογίων μας ενημερώνει, μέσω αυτής της κλήσης, ότι επιλέχθηκε άλλο τιμολόγιο από
	 * το χρήστη. Συνέπεια είναι όλος ο πίνακας ειδών να ανανεωθεί με τα είδη του νέου τιμολογίου.
	 * @param i Το τρέχον επιλεγμένο τιμολόγιο στον πίνακα τιμολογίων */
	void setSelectedInvoice(Invoice i) {
		invoice = i;
		list = i == null ? null : i.items;
		fireTableDataChanged();
	}
}