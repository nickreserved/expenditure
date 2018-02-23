package tables;

import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

public class ComboDataModel implements ComboBoxModel {
	private final ArrayList<ListDataListener> v = new ArrayList<ListDataListener>();
	private Object idx;
	private final Object last;
	private final boolean hasLast;
	private DataTransmitter dtr;
	public ComboDataModel(DataTransmitter dt) { dtr = dt; hasLast = false; last = null; }
	public ComboDataModel(DataTransmitter dt, Object o) { dtr = dt; hasLast = true; last = o; }
	@Override
	public Object getSelectedItem() { return idx; }
	@Override
	public void setSelectedItem(Object o) {
		idx = o;
		for(int z = 0; z < v.size(); z++)
			v.get(z).contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, getSize()));
	}
	@Override
	public int getSize() { return (hasLast ? 1 : 0) + ((ArrayList) dtr.getData()).size(); }
	@Override
	public void removeListDataListener(ListDataListener l) { v.remove(l); }
	@Override
	public void addListDataListener(ListDataListener l) { v.add(l); }
	@Override
	public Object getElementAt(int i) {
		int a = ((ArrayList) dtr.getData()).size();
		if (i == a && hasLast) return last;
		if (i >= 0 && i < a) return ((ArrayList) dtr.getData()).get(i);
		return null;
	}
}
