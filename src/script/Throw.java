package script;

public class Throw extends Statement {
  private String e;
  private Object re;

  public Throw(String exception) { e = exception; }
  public Throw(String exception, Object reason) {
    this(exception);
    re = reason;
  }

  public Object run(RuntimeEnvironment r) {
    Object n = re;
    if (n instanceof Function) n = ((Function) re).run(r);
    throw new ExecuteException(e, n.toString(), getLine());
  }

  public Object simplify(RuntimeEnvironment r) {
    if (re instanceof Function) re = ((Function) re).simplify(r);
    return this;
  }
}