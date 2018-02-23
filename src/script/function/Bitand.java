// Integer Bitand(Integer, Integer)

package script.function;

import script.*;
import java.util.*;


public class Bitand extends Function {
  public void setVector(List l) {
    if (l.size() != 2)
      throw new CompileException(getLine(), "<b><i>arg1</i> & <i>arg2</i></b> has " + l.size() + " parameter(s)");
    v = l;
  }

  public Object run(RuntimeEnvironment r) {
    Object a[] = new Object[2];
    for (int z = 0; z < 2; z++) {
      a[z] = v.get(z);
      if (a[z] instanceof Function) {
        a[z] = ((Function) a[z]).run(r);
        if (!integer(a[z]))
          throw new ExecuteException("CastingError", "<b>operator&: <i>arg" + (z + 1) + "</i> = " + v.get(z) + "</b> is not integer", getLine());
      }
    }
    return new Long(((Number) a[0]).longValue() & ((Number) a[1]).longValue());
  }

  public Object simplify(RuntimeEnvironment r) {
    Object a[] = new Object[2];
    for (int z = 0; z < 2; z++) {
      a[z] = v.get(z);
      if (a[z] instanceof Function) {
        a[z] = ((Function) a[z]).simplify(r);
        if (a[z] instanceof Throw) return a[z];
      }
      else if (integer(a[z]) || a[z] instanceof Function) v.set(z, a[z]);
      else throw new CompileException(getLine(), "<b>operator&: <i>arg" + (z + 1) + "</i> = " + v.get(z) + "</b> is not integer");
    }
    if (integer(a[0]) && integer(a[1]))
      return new Long(((Number) a[0]).longValue() & ((Number) a[1]).longValue());
    return this;
  }

  public String toString() { return "(" + v.get(0) + " & " + v.get(1) + ")"; }
}
