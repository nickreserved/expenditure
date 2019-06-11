package expenditure;

import static expenditure.MainFrame.data;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/** Μοντέλο δεδομένων για JComboBox, που περιέχει όλες τις συμβάσεις και τους δικαιούχους. */
final class ContractsContractorsDataModel implements ComboBoxModel {
	/** Το τρέχον επιλεγμένο αντικείμενο στο JComboBox. */
	private Object idx;

	@Override public Object getSelectedItem() { return idx; }
	@Override public void setSelectedItem(Object o) {
		idx = o;
		ListDataEvent e = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, getSize());
		listeners.forEach(i -> i.contentsChanged(e));
	}

	@Override public int getSize() {
		int r = data.contractors.size();
		if (!data.isEmpty()) r += data.getActiveExpenditure().contracts.size();
		return r;
	}
	@Override public Object getElementAt(int i) {
		if (i >= 0) {
			if (!data.isEmpty()) {
				List<Contract> lst = data.getActiveExpenditure().contracts;
				if (i < lst.size()) return lst.get(i);
				i -= lst.size();
			}
			if (i < data.contractors.size()) return data.contractors.get(i);
		}
		return null;
	}

	/** Λίστα με listeners για κάθε αλλαγή στο JComboBox. */
	private final ArrayList<ListDataListener> listeners = new ArrayList<>();
	@Override public void removeListDataListener(ListDataListener l) { listeners.remove(l); }
	@Override public void addListDataListener(ListDataListener l) { listeners.add(l); }
}