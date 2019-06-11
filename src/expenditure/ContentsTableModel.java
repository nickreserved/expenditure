package expenditure;

import static expenditure.MainFrame.data;
import java.util.List;
import util.ResizableHeaderTableModel;

/** Το μοντέλο του πίνακα φύλλου καταχώρησης. */
final class ContentsTableModel extends ResizableHeaderTableModel<ContentItem> {
	/** Συντόμευση στη λίστα του φύλλου καταχώρησης της ενεργής δαπάνης. */
	private List<ContentItem> list;
	/** Δημιουργία του μοντέλου του πίνακα. */
	ContentsTableModel() {
		super(new String[] { ContentItem.H[0], ContentItem.H[1] + " ή " + ContentItem.H[3] });
		updateExpenditure();
	}
	/** Ενημερώνει το μοντέλο ότι η ενεργή δαπάνη άλλαξε. */
	private void updateExpenditure() {
		list = data.isEmpty() ? null : data.getActiveExpenditure().contents;
	}
	@Override protected List<ContentItem> get() { return list; }
	@Override protected ContentItem createNew() { return new ContentItem(); }
	@Override protected void remove(int index) {
		if (list.get(index).isUserDefined()) list.remove(index);
	}
	@Override public boolean isCellEditable(int row, int column) {
		if (row == list.size()) return true;
		ContentItem ci = list.get(row);
		return ci.isUserDefined() || column == 1 && ci.hasChoice();
	}
	@Override public void fireTableDataChanged() { updateExpenditure(); super.fireTableDataChanged(); }
}