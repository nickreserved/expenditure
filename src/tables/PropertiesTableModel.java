package tables;

import java.util.*;
import javax.swing.table.*;

public class PropertiesTableModel extends AbstractTableModel {

  protected String[][] hash;
  protected String[] header;
  protected Dictionary map;
  protected DataTransmitter transmitter;

  public PropertiesTableModel(String[][] hash, Dictionary map, String[] header) {
    super();
    this.hash = hash;
    setData(map);
    this.header = header;
  }

  public PropertiesTableModel(String[][] hash, DataTransmitter dt, String[] header) {
    super();
    this.hash = hash;
    setData(dt);
    this.header = header;
  }

  public Dictionary getData() {
    return transmitter != null ? (Dictionary) transmitter.getData() : map;
  }

  public String[] getHeaders() { return header; }

  public void setData(Dictionary map) {
    this.map = map;
    fireTableDataChanged();
  }

  public void setData(DataTransmitter dt) {
    transmitter = dt;
    map = null;
    fireTableDataChanged();
  }

  public int getColumnCount() {
    if (header != null) return header.length;
    else if (hash == null || hash[0] == null) return 0;
    else return hash[0].length;
  }

  public int getRowCount() {
    return hash == null ? 0 : hash.length;
  }
  public String getColumnName(int col) { return header == null ? null : header[col]; }

  public boolean isCellEditable(int row, int col) {
    try {
      if (!hash[row][col].startsWith(":")) return true;
    } catch(Exception e) {}
    return false;
  }

  public Object getValueAt(int row, int col) {
    try {
      String s = hash[row][col];
      if (s.startsWith(":")) return s;
      else return getData().get(s);
    } catch(Exception e) {
      return null;
    }
  }

  public void setValueAt(Object obj, int row, int col) {
    try { getData().put(hash[row][col], obj); }
    catch(Exception e) {}
  }
}