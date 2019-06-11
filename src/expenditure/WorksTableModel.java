package expenditure;

import static expenditure.MainFrame.data;
import java.util.Arrays;
import java.util.List;
import util.ResizableHeaderTableModel;

/** Το μοντέλο του πίνακα εργασιών. */
final class WorksTableModel extends ResizableHeaderTableModel<Work> {
	/** Συντόμευση στη λίστα εργασιών της ενεργής δαπάνης. */
	private List<Work> list;
	/** Δημιουργία του μοντέλου του πίνακα. */
	WorksTableModel() { super(Arrays.copyOf(Work.H, 3)); updateExpenditure(); }
	/** Ενημερώνει το μοντέλο ότι η ενεργή δαπάνη άλλαξε. */
	private void updateExpenditure() {
		list = data.isEmpty() ? null : data.getActiveExpenditure().works;
	}
	@Override protected List<Work> get() { return list; }
	@Override protected Work createNew() { return new Work(); }
	@Override public void fireTableDataChanged() { updateExpenditure(); super.fireTableDataChanged(); }
}