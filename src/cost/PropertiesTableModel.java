package cost;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class PropertiesTableModel extends AbstractTableModel {

  protected String[] properties;
  protected Object[][] data;
  protected String[] header;

  public PropertiesTableModel(String[] properties, Object[][] data) {
    super();
    this.properties = properties;
    setData(data);
  }

  public PropertiesTableModel(String[] properties, Object[][] data, String[] header) {
    this(properties, data);
    this.header = header;
  }

  public void setProperties(String[] properties) {
    this.properties = properties;
    fireTableDataChanged();
  }

  public String[] getHeaders() { return header; }
  public Object[][] getData() { return data; }
  public void setData(Object[][] data) {
    this.data = data;
    fireTableDataChanged();
  }

  public int getColumnCount() {
    if (header != null) return header.length;
    else if (data == null || data[0] == null) return 0;
    else return data[0].length + 1;
  }

  public int getRowCount() {
    if (properties == null) return 0; else return properties.length;
  }

  public boolean isCellEditable(int row, int col) {
    if (col == 0) return false; else return true;
  }

  public Object getValueAt(int row, int col) {
    if (col == 0)
     if (properties != null && row < properties.length) return properties[row];
     else return null;
    else return data[row][col - 1];
  }

  public void setValueAt(Object obj, int row, int col) {
    if (col > 0) {
      if (obj instanceof String && data[row][col - 1] instanceof FromString)
        try{
          ((FromString) data[row][col - 1]).fromString(obj.toString());
        } catch (Exception e) {
          data[row][col - 1] = null;
        }
      else
        data[row][col - 1] = obj;
    }
  }

  public String getColumnName(int col) {
    if (header == null) return null; else return header[col];
  }
}