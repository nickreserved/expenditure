// Boolean Not(Object)

package script.function;

import script.*;
import java.util.*;


public class Not extends Function {
  public void setVector(List l) {
    if (l.size() != 1)
      throw new CompileException(getLine(), "<b>!<i>arg</i></b> has " + l.size() + "parameter(s)");
    v = l;
  }

  public Object run(RuntimeEnvironment r) {
    Object a = v.get(0);
    if (a instanceof Function) a = ((Function) a).run(r);
    return new Boolean(bool(a));
  }

  public Object simplify(RuntimeEnvironment r) {
    Object a = v.get(0);
    if (a instanceof Function) v.set(0, a = ((Function) a).simplify(r));
    if (a instanceof Throw) return a;
    else if (a instanceof Function) return this;
    else return new Boolean(!bool(a));
  }

  public String toString() { return "(!" + v.get(0) + ")"; }
}
