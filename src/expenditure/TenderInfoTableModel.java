package expenditure;

import static expenditure.MainFrame.data;
import util.PropertiesTableModel;

/** Το μοντέλο του πίνακα με τους διαγωνισμούς.
 * Μοντέλο πίνακα 2 στηλών, με την πρώτη στήλη να είναι οι επικεφαλίδες και τη δεύτερη οι τιμές. */
public class TenderInfoTableModel extends PropertiesTableModel {
	/** Ο διαγωνισμός που εμφανίζεται στον πίνακα. */
	private Tender tender;
	/** Αρχικοποίηση του μοντέλου του πίνακα. */
	TenderInfoTableModel() {
		super(new String[] {
			"<html><b>Διαγωνισμός", "<html>Τίτλος Διαγωνισμού <font size=2><i>(αιτιατική)",
			Tender.H[1], Tender.H[2], Tender.H[3], Tender.H[4], Tender.H[5], Tender.H[6],
			Tender.H[7], Tender.H[8], u(Tender.H[9]), Tender.H[10], Tender.H[11],
			"<html><b>Κατακύρωση", u(Tender.H[12]), Tender.H[13], Tender.H[14], Tender.H[15]
		}, 1);
	}
	@Override public boolean isCellEditable(int row, int col) {
		return col != 0 && tender != null && row != 0 && row != 13
				&& (!data.expenditure.isSmart() || row != 2);
	}
	@Override public TableRecord get(int index) { return tender; }
	/** Θέτει το διαγωνισμό που εμφανίζεται στον πίνακα.
	 * @param tender Ο διαγωνισμός που εμφανίζεται στον πίνακα */
	void setSelectedTender(Tender tender) { this.tender = tender; fireTableDataChanged(); }
	/** Επιστρέφει μια επικεφαλίδα πίνακα με το βοηθητικό κείμενο ότι χωρίζεται με &.
	 * @param a Ένα κείμενο
	 * @return Το δοσμένο κείμενο προσαρμοσμένο για επικεφαλίδα πίνακα, αφού έχει προστεθεί στο
	 * τέλος του ότι "χωρίζονται με &", δηλαδή το κείμενο μπορεί να έχει πολλά στοιχεία, τα οποία
	 * για να διαχωριστούν απαιτούν το & */
	static String u(String a) { return "<html>" + a + " <font size=2><i>(χωρίζονται με &)"; }
}