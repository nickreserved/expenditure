package script;

public class Return extends Break {
  Object re;
  public Return(Object ret) {
    super(RuntimeEnvironment.RETURN);
    re = ret;
  }
  public Object run(RuntimeEnvironment r) {
    super.run(r);
    return re;
  }
  public Object simplify(RuntimeEnvironment r) {
    if (re instanceof Function) re = ((Function) re).simplify(r);
    return this;
  }
}