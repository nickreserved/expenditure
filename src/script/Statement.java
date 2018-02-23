package script;

public abstract class Statement {
  private int line;
  public abstract Object run(RuntimeEnvironment r);
  public void setLine(int l) { line = l; }
  public int getLine() { return line; }
  public Object simplify(RuntimeEnvironment r) { return this; }
  public boolean isStatement() { return true; }

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

  protected static final boolean integer(Object o) {
    return o instanceof Byte || o instanceof Short || o instanceof Integer || o instanceof Long;
  }

  protected static final String string(Object o) {
    return (o instanceof Number || o instanceof Function) ? o.toString() :
          '"' + o.toString().replaceAll("\\\\", "\\\\\\\\").replaceAll("\\\"", "\\\\\\\"") + '"';
  }
}