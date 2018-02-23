package script.function;

import script.*;
import java.util.*;


public class Less extends Function {
  public void setVector(List l) {
    if (l.size() != 2) throw new CompileException(getLine(), "function <b>Less</b> must take 2 parameters");
    v = l;
  }
  public Object run(RuntimeEnvironment r) {
    Object a = v.get(0), b = v.get(1);
    if (a instanceof Function) a = ((Function) a).run(r);
    if (b instanceof Function) b = ((Function) b).run(r);
    if (a instanceof Comparable && b instanceof Comparable)
      return new Boolean (((Comparable) a).compareTo(b) < 0 ? true : false);
    throw new ExecuteException("CastingError", "<b>Less:</b> Cannot compare <b>" + a + "</b> & <b>" + b + "</b>", getLine());
  }

  public Object simplify(RuntimeEnvironment r) {
    Object a = v.get(0), b = v.get(1);
    if (a instanceof Function) a = ((Function) a).simplify(r);
    if (b instanceof Function) b = ((Function) b).simplify(r);
    if (b instanceof Throw) return b;
    else if (b instanceof Comparable || b instanceof Function) v.set(1, b);
    else throw new CompileException(getLine(), "<b>Less</b> has a non-comparable parameter (<b>" + b + "</b>)");
    if (a instanceof Throw) return a;
    else if (a instanceof Comparable || a instanceof Function) v.set(0, a);
    else throw new CompileException(getLine(), "<b>Less</b> has a non-comparable parameter (<b>" + a + "</b>)");
    if (a instanceof Comparable && b instanceof Comparable)
      return new Boolean (((Comparable) a).compareTo(b) < 0 ? true : false);
    return this;
  }

  public String toString() { return "(" + v.get(0) + " < " + v.get(1) + ")"; }
}