package script.function;

import script.*;
import java.util.*;


public class Neg extends Function {
  public void setVector(List l) {
    if (l.size() != 1) throw new CompileException(getLine(), "function <b>Neg</b> must take 1 parameter");
    v = l;
  }
  public Object run(RuntimeEnvironment r) {
    Object a = v.get(0);
    if (a instanceof Function) a = ((Function) a).run(r);
    if (a instanceof Number)
      return new Double(-((Number) a).doubleValue());
    throw new ExecuteException("CastingError", "<b>Neg</b> has a non-number parameter", getLine());
  }

  public Object simplify(RuntimeEnvironment r) {
    Object a = v.get(0);
    if (a instanceof Function) a = ((Function) a).simplify(r);
    if (a instanceof Throw) return a;
    else if (a instanceof Number) return new Double(-((Number) a).doubleValue());
    else if (a instanceof Function) v.set(0, a);
    else throw new CompileException(getLine(), "<b>Neg</b> has a non-number parameter (<b>" + a + "</b>)");
    return this;
  }

  public String toString() { return "(-" + v.get(0) + ")"; }
}