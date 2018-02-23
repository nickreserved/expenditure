package script.function;

import script.*;
import java.util.*;


public class Notequal extends Function {
  public void setVector(List l) {
    if (l.size() != 2) throw new CompileException(getLine(), "function <b>Notequal</b> must take 2 parameters");
    v = l;
  }
  public Object run(RuntimeEnvironment r) {
    Object a = v.get(0), b = v.get(1);
    if (a instanceof Function) a = ((Function) a).run(r);
    if (b instanceof Function) b = ((Function) b).run(r);
    return new Boolean(!a.equals(b));
  }

  public Object simplify(RuntimeEnvironment r) {
    Object a = v.get(0), b = v.get(1);
    if (a instanceof Function) a = ((Function) a).simplify(r);
    if (b instanceof Function) b = ((Function) b).simplify(r);
    if (b instanceof Throw) return b; else v.set(1, b);
    if (a instanceof Throw) return a; else v.set(0, a);
    if (!(a instanceof Function || b instanceof Function))
      return new Boolean(!a.equals(b));
    return this;
  }

  public String toString() { return "(" + v.get(0) + " != " + v.get(1) + ")"; }
}