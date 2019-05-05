package util;

import java.util.ArrayList;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/** Μοντέλο δεδομένων για JComboBox. */
public class ComboDataModel implements ComboBoxModel {
	/** Το τρέχον επιλεγμένο αντικείμενο στο JComboBox. */
	private Object idx;
	/** Επιπλεόν ένα αντικείμενο στο τέλος, που είναι null. */
	private final boolean hasLast;
	/** Ο παροχέας των αντικειμένων. */
	private final ComboData d;

	/** Αρχικοποίηση του μοντέλου.
	 * @param dt Ο παροχέας των αντικειμένων
	 * @param lastNull Επιπλεόν ένα αντικείμενο στο τέλος, που είναι null */
	public ComboDataModel(ComboData dt, boolean lastNull) { d = dt; hasLast = lastNull; }

	@Override public Object getSelectedItem() { return idx; }
	@Override public void setSelectedItem(Object o) {
		idx = o;
		ListDataEvent e = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, getSize());
		listeners.forEach(i -> i.contentsChanged(e));
	}

	@Override public int getSize() { return (hasLast ? 1 : 0) + d.get().size(); }
	@Override public Object getElementAt(int i) {
		List al = d.get();
		if (i >= 0 && i < al.size()) return al.get(i);
		return null;
	}

	/** Λίστα με listeners για κάθε αλλαγή στο JComboBox. */
	private final ArrayList<ListDataListener> listeners = new ArrayList<>();
	@Override public void removeListDataListener(ListDataListener l) { listeners.remove(l); }
	@Override public void addListDataListener(ListDataListener l) { listeners.add(l); }


	/** Ο παροχέας των αντικειμένων για combo box. */
	public interface ComboData {
		/** Επιστρέφει μια λίστα με αντικείμενα.
		 * @return Η λίστα με τα αντικείμενα. */
		List get();
	}
}
