package expenditure;

import java.util.List;
import util.ResizableTableModel;

/** Το μοντέλο πίνακα με τα δικαιολογητικά που καταθέτουν οι διαγωνιζόμενοι. */
class DocumentTableModel extends ResizableTableModel {
	/** Ο αριθμός στηλών προ των απορρίψεων. */
	static final private int N = 1;
	/** Η τρέχουσα σύμβαση. */
	private Contract contract;
	@Override public int getColumnCount() { return contract == null ? N : N + contract.competitors.size(); }
	@Override public int getRowCount() { return contract == null ? 0 : contract.documents.size() + 1; }
	@Override public String getColumnName(int col) {
		return col < N ? Contract.Document.H[col] : contract.competitors.get(col - N).toString();
	}
	@Override protected List<Contract.Document> get() { return contract == null ? null : contract.documents; }
	@Override protected Contract.Document createNew() { return new Contract.Document(); }
	/** Θέτει την τρέχουσα σύμβαση.
	 * @param contract Η τρέχουσα σύμβαση */
	void setSelectedContract(Contract contract) {  this.contract = contract; fireTableChanged(); }
}
