// Number Div(Number, Number)

package script.function;

import script.*;
import java.util.*;


public class Div extends Function {
  public void setVector(List l) {
    if (l.size() != 2) throw new CompileException(getLine(), "<b><i>arg1</i> / <i>arg2</i></b> has " + l.size() + " parameter(s)");
    v = l;
  }
  public Object run(RuntimeEnvironment r) {
    Object a[] = new Object[2];
    for (int z = 0; z < 2; z++) {
      a[z] = v.get(z);
      if (a[z] instanceof Function) {
        a[z] = ((Function) a[z]).run(r);
        if (!(a[z] instanceof Number))
          throw new ExecuteException("CastingError", "<b>operator/: <i>arg" + (z + 1) + "</i> = " + v.get(z) + "</b> is not number", getLine());
      }
    }
    if (a[1].equals(new Integer(0)))
      throw new ExecuteException("ZeroDivision", "<b>operator/: <i>arg2</i> = " + v.get(1) + "</b> equal zero", getLine());
    return common.M.div((Number) a[0], (Number) a[1]);
  }

  public Object simplify(RuntimeEnvironment r) {
    Object a[] = new Object[2];
    for (int z = 0; z < 2; z++) {
      a[z] = v.get(z);
      if (a[z] instanceof Function) {
        a[z] = ((Function) a[z]).simplify(r);
        if (a[z] instanceof Throw) return a[z];
      }
      else if (a[z] instanceof Number || a[z] instanceof Function) v.set(z, a[z]);
      else throw new CompileException(getLine(), "<b>operator&: <i>arg" + (z + 1) + "</i> = " + v.get(z) + "</b> is not integer");
    }
    if (a[1].equals(new Integer(0))) {
      Throw o = new Throw("ZeroDivision", "<b>operator&: <i>arg2</i> = " + v.get(1) + "</b> equal zero");
      o.setLine(getLine());
      return o;
    }
    if (a[0] instanceof Number && a[1] instanceof Number) return common.M.div((Number) a[0], (Number) a[1]);
    return this;
  }

  public String toString() { return "(" + v.get(0) + " / " + v.get(1) + ")"; }
}