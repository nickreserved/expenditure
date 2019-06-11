package expenditure;

import static expenditure.MainFrame.data;
import util.PropertiesTableModel;

/** Μοντέλο πίνακα 2 στηλών, με την πρώτη στήλη να είναι οι επικεφαλίδες και τη δεύτερη οι τιμές. */
public class ContractInfoTableModel extends PropertiesTableModel {
	/** Η σύμβαση που εμφανίζεται στον πίνακα. */
	private Contract contract;
	/** Αρχικοποίηση του μοντέλου του πίνακα. */
	ContractInfoTableModel() {
		super(new String[] {
			Contract.H[0], "<html>Τίτλος Σύμβασης <font color=gray size=2>(αιτιατική)",
			Contract.H[2], Contract.H[3],
			"<html><b>Διαγωνισμοί", Contract.H[4], Contract.H[5],
			"<html>" + Contract.H[6] + " <font color=gray size=2>(αιτιατική με άρθρο)", Contract.H[7],
			Contract.H[8], Contract.H[9]
		}, 1);
	}
	@Override public boolean isCellEditable(int row, int col) {
		return col != 0 && contract != null && row != 4 &&
				(!data.getActiveExpenditure().isSmart() || row != 2 && row != 3);
	}
	@Override public TableRecord get(int index) { return contract; }
	/** Θέτει τη σύμβαση που εμφανίζεται στον πίνακα.
	 * @param contract Η σύμβαση που εμφανίζεται στον πίνακα */
	void setSelectedContract(Contract contract) { this.contract = contract; fireTableDataChanged(); }
}