package util;

import java.util.List;

/** Μοντέλο δεδομένων για JComboBox. */
abstract public class ComboPlusOneDataModel extends ComboDataModel {
	@Override public int getSize() { return 1 + super.getSize(); }
	@Override public Object getElementAt(int i) {
		List al = get();
		return i >= 0 && i < al.size() ? al.get(i) : null;
	}
}
