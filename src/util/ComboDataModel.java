package util;

import java.util.ArrayList;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/** Μοντέλο δεδομένων για JComboBox.
 * @param <T> Ο τύπος των επιλογών */
abstract public class ComboDataModel<T> implements ComboBoxModel {
	/** Το τρέχον επιλεγμένο αντικείμενο στο JComboBox. */
	private T idx;

	/** Ο παροχέας των αντικειμένων για combo box. */
	/** Επιστρέφει μια λίστα με αντικείμενα.
	 * @return Η λίστα με τα αντικείμενα. */
	abstract protected List<T> get();

	@Override public T getSelectedItem() { return idx; }
	@Override public void setSelectedItem(Object o) {
		idx = (T) o;
		ListDataEvent e = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, getSize());
		listeners.forEach(i -> i.contentsChanged(e));
	}

	@Override public int getSize() { return get().size(); }
	@Override public T getElementAt(int i) { return get().get(i); }

	/** Λίστα με listeners για κάθε αλλαγή στο JComboBox. */
	private final ArrayList<ListDataListener> listeners = new ArrayList<>();
	@Override public void removeListDataListener(ListDataListener l) { listeners.remove(l); }
	@Override public void addListDataListener(ListDataListener l) { listeners.add(l); }
}