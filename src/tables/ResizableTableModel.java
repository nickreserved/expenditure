package tables;

import java.util.*;
import javax.swing.table.*;

public class ResizableTableModel<T> extends AbstractTableModel {
  protected ArrayList<T> data;
  protected ArrayTransmitter<T> transmitter;
  protected String[] title;
  protected String[] hash;
  protected Class<T> classType;
	protected int old = -1;

  public ResizableTableModel(ArrayList<T> data, String[] hash, String[] title, Class<T> classType) {
    this.data = data;
    this.title = title;
    this.hash = hash;
    this.classType = classType;
  }

  public ResizableTableModel(ArrayTransmitter<T> dt, String[] hash, String[] title, Class<T> classType) {
    transmitter = dt;
    this.title = title;
    this.hash = hash;
    this.classType = classType;
  }

  public String[] getHash() { return hash; }
  public void setHash(String[] hash) { this.hash = hash; }

  public void setData(ArrayList<T> data) {
    transmitter = null;
    this.data = data;
		fireTableDataChanged();
  }

  public void setData(ArrayTransmitter<T> dt) {
    this.data = null;
    transmitter = dt;
		fireTableDataChanged();
  }

  public ArrayList<T> getData() { return (transmitter != null) ? transmitter.getData() : data; }
	@Override
  public int getColumnCount() { return hash.length; }
	@Override
  public int getRowCount() {
		int a = getData() == null ? 0 : getData().size() + 1;
		if (a != old) { old = a; fireTableDataChanged(); }
		return a;
	}
	@Override
  public String getColumnName(int col) { return title != null && title[col] != null ? title[col] : hash[col]; }
	@Override
  public boolean isCellEditable(int row, int col) { return true; }

	@Override
  public Object getValueAt(int row, int col) {
    try {
      return ((HashMap) getData().get(row)).get(hash[col]);
    } catch (Exception e) {
      return null;
    }
  }

	@Override
	public void setValueAt(Object obj, int row, int col) {
	try {
		if (row >= getData().size()) getData().add(classType.newInstance());
		else if (getData().get(row) == null) getData().set(row, classType.newInstance());
		HashMap o = (HashMap) getData().get(row);
		o.put(hash[col], obj);
		if (o.isEmpty()) getData().remove(row);
	} catch (Exception e) {}
	fireTableDataChanged();
	}
}