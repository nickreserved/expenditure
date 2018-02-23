package tables;

import javax.swing.table.*;

public class PropertiesTableModel extends AbstractTableModel {

  private String[] hash;
  private String[] hHeader;
  private String[] vHeader;
  private DataTransmitter transmitter;

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

  public String[] getHash() { return hash; }
	public String[] getHeaders(boolean horizontal) { return horizontal ? hHeader : vHeader; }

	@Override
  public int getColumnCount() { return hash.length / getRowCount(); }
	@Override
  public int getRowCount() { return vHeader.length; }
	@Override
  public String getColumnName(int col) { return col == -1 || hHeader == null ? null : hHeader[col]; }
	@Override
  public boolean isCellEditable(int row, int col) { return col != -1 && hash[row] != null; }

	@Override
  public Object getValueAt(int row, int col) {
    if (col == -1)
      if (vHeader == null || vHeader[row] == null) return hash[row];
      else return vHeader[row];
    try {
      return transmitter.getData().get(hash[row + getColumnCount() * col]);
    } catch(Exception e) {
      return null;
    }
  }

	@Override
  public void setValueAt(Object obj, int row, int col) {
    try {
      transmitter.getData().put(hash[row + getColumnCount() * col], obj);
    } catch(Exception e) {}
  }
}