package tables;

import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

public class ComboDataModel implements ComboBoxModel {
	private final Vector<ListDataListener> v = new Vector();
	private Object idx;
	private final Object last;
	private final boolean hasLast;
	private DataTransmitter dtr;
	public ComboDataModel(DataTransmitter dt) { dtr = dt; hasLast = false; last = null; }
	public ComboDataModel(DataTransmitter dt, Object o) { dtr = dt; hasLast = true; last = o; }
	public Object getSelectedItem() { return idx; }
	public void setSelectedItem(Object o) {
		idx = o;
		for(int z = 0; z < v.size(); z++)
			v.get(z).contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, getSize()));
	}
	public int getSize() { return (hasLast ? 1 : 0) + ((Vector) dtr.getData()).size(); }
	public void removeListDataListener(ListDataListener l) { v.remove(l); }
	public void addListDataListener(ListDataListener l) { v.add(l); }
	public Object getElementAt(int i) {
		int a = ((Vector) dtr.getData()).size();
		if (i == a && hasLast) return last;
		if (i >= 0 && i < a) return ((Vector) dtr.getData()).get(i);
		return null;
	}
}
