package cost;

import java.util.Vector;

public class Vector2 extends Vector {
  protected int it = 0;
  public Vector2() { super(); }
  public Vector2(Vector v) { super.addAll(v); }
  public Object element() { return this.elementAt(it); }
  public void inc() { it++; }
  public void dec() { it--; }
  public void setIterator(int i) { it = i; }
  public int getIterator() { return it; }
}