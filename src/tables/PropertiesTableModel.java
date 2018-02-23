package tables;

import java.util.*;
import javax.swing.table.*;

public class PropertiesTableModel extends AbstractTableModel {
  
  protected String[] hash;
  protected String[] hHeader;
  protected String[] vHeader;
  protected DataTransmitter transmitter;
  
	protected PropertiesTableModel() {}
	
  public PropertiesTableModel(String[] hash, DataTransmitter dt, String[] vHeader) {
    this.hash = hash;
    transmitter = dt;
    this.vHeader = vHeader;
    fireTableDataChanged();
  }
  
  public PropertiesTableModel(String[] hash, DataTransmitter dt, String[] vHeader, String[] hHeader) {
    this(hash, dt,vHeader);
    this.hHeader = hHeader;
  }
  
  public String[] getHeaders(boolean horizontal) { return horizontal ? hHeader : vHeader; }
  
  public int getColumnCount() { return hash.length / getRowCount(); }
  public int getRowCount() { return vHeader.length; }
  public String getColumnName(int col) { return col == -1 || hHeader == null ? null : hHeader[col]; }
  public boolean isCellEditable(int row, int col) { return col != -1 && hash[row] != null; }
  
  public Object getValueAt(int row, int col) {
    if (col == -1)
      if (vHeader == null || vHeader[row] == null) return hash[row];
      else return vHeader[row];
    try {
      return ((Dictionary) transmitter.getData()).get(hash[row + getColumnCount() * col]);
    } catch(Exception e) {
      return null;
    }
  }
  
  public void setValueAt(Object obj, int row, int col) {
    try {
      ((Dictionary) transmitter.getData()).put(hash[row + getColumnCount() * col], obj);
    } catch(Exception e) {}
  }
}