package cost;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class ResizableTableModel extends AbstractTableModel {
  protected Vector data;
  protected String[] title;
  protected Class classType;


  public ResizableTableModel(Vector data, String[] title, Class classType) {
    super();
    this.data = data;
    this.title = title;
    this.classType = classType;
  }

  public Vector getData() { return data; }

  public void setData(Vector data) {
    this.data = data;
    fireTableDataChanged();
  }

  public int getColumnCount() { return title.length; }
  public int getRowCount() { return data == null ? 0 : data.size() + 1; }
  public boolean isCellEditable(int row, int col) { return true; }

  public Object getValueAt(int row, int col) {
    return (row + 1 < getRowCount()) ? ( (RowTable) data.elementAt(row)).getCell(col) : null;
  }

  public void setValueAt(Object obj, int row, int col) {
    if (row + 1 == getRowCount())
      try {
	data.add(classType.newInstance());
      } catch (Exception e) {
      }
    ((RowTable) data.elementAt(row)).setCell(obj, col);
    if (((RowTable) data.elementAt(row)).isEmpty())
      data.remove(row);
    fireTableDataChanged();
  }

  public String getColumnName(int col) {
    if (title == null) return null; else return title[col];
  }
}