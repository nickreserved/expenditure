package cost;

public interface RowTable {
  public Object getCell(int col);
  public void setCell(Object o, int col);
  public boolean isEmpty();
}