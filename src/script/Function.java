package script;

import java.util.*;

public abstract class Function extends Statement {
  protected List v;
  public final List getVector() { return v; }
  public void setVector(List l) { v = l; }
  public boolean isStatement() { return false; }
}