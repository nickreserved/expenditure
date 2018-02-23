package tables;

import java.util.*;
import javax.swing.table.*;
import common.*;

public class ResizableTableModel extends AbstractTableModel {
  protected Vector data;
  protected DataTransmitter transmitter;
  protected String[] title;
  protected String[] hash;
  protected Class classType;


  public ResizableTableModel(Vector data, String[] hash, String[] title, Class classType) {
    super();
    this.data = data;
    this.title = title;
    this.hash = hash;
    this.classType = classType;
  }

  public ResizableTableModel(DataTransmitter dt, String[] hash, String[] title, Class classType) {
    super();
    transmitter = dt;
    this.title = title;
    this.hash = hash;
    this.classType = classType;
  }

  public void setData(Vector data) {
    transmitter = null;
    this.data = data;
    fireTableDataChanged();
  }

  public void setData(DataTransmitter dt) {
    this.data = null;
    transmitter = dt;
    fireTableDataChanged();
  }

  public Vector getData() { return (transmitter != null) ? (Vector) transmitter.getData() : data; }
  public int getColumnCount() { return title.length; }
  public int getRowCount() { return getData() == null ? 0 : getData().size() + 1; }
  public String getColumnName(int col) { return title == null ? null : title[col]; }
  public boolean isCellEditable(int row, int col) { return true; }

  public Object getValueAt(int row, int col) {
    try {
      return ((Dictionary) getData().get(row)).get(hash[col]);
    } catch (Exception e) {
      return null;
    }
  }

  public void setValueAt(Object obj, int row, int col) {
    try {
      if (row + 1 == getRowCount()) getData().add(classType.newInstance());
      Dictionary o = (Dictionary) getData().get(row);
      o.put(hash[col], obj);
      if (o.isEmpty()) getData().remove(row);
    } catch (Exception e) {}
    fireTableDataChanged();
  }
}