// Integer Onecomp(Integer)

package script.function;

import script.*;
import java.util.*;


public class Onecomp extends Function {
  public void setVector(List l) {
    if (l.size() != 1)
      throw new CompileException(getLine(), "<b>~<i>arg</i></b> has " + l.size() + " parameter(s)");
    v = l;
  }

  public Object run(RuntimeEnvironment r) {
    Object a = v.get(0);
    if (a instanceof Function) a = ((Function) a).run(r);
    if (integer(a)) return new Long(~((Number) a).longValue());
    throw new ExecuteException("CastingError", "<b>operator~: <i>arg</i> = " + v.get(0) + "</b> is not integer", getLine());
  }

  public Object simplify(RuntimeEnvironment r) {
    Object a = v.get(0);
    if (a instanceof Function) a = ((Function) a).simplify(r);
    if (a instanceof Throw) return a;
    if (integer(a)) return new Long(~((Number) a).longValue());
    if (a instanceof Function) { v.set(0, a); return this; }
    throw new CompileException(getLine(), "<b>operator~: <i>arg</i> = " + v.get(0) + "</b> is not integer");
  }

  public String toString() { return "(~" + v.get(0) + ")"; }
}