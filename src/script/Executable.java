package script;

public abstract class Executable {
  private int line;
  public abstract void run(RuntimeEnvironment r);
  public void setLine(int l) { line = l; }
  public int getLine() { return line; }
  public Object simplify(RuntimeEnvironment r) { return this; }

  protected static final boolean loopbreak(RuntimeEnvironment r) {
    if (r.control == 0) return false;
    if (r.control == r.EXIT || r.control == r.RETURN) return true;
    if (r.control > 0) {
      r.control--;
      return false;
    } else {
      r.control++;
      return true;
    }
  }

  protected static final boolean bool(Object o) {
    return (o == null ||
	o instanceof Number && ((Number) o).doubleValue() == 0 ||
	o instanceof String && o.toString().length() == 0 ||
	o instanceof Boolean && o.equals(Boolean.FALSE)) ?
      false : true;
  }
}