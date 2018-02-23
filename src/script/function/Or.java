// Boolean Or(Object, Object)

package script.function;

import script.*;
import java.util.*;


public class Or extends Function {
  public void setVector(List l) {
    if (l.size() != 2)
      throw new CompileException(getLine(), "<b><i>arg1</i> || <i>arg2</i></b> has " + l.size() + " parameter(s)");
    v = l;
  }
  public Object run(RuntimeEnvironment r) {
    Object a[] = new Object[2];
    for (int z = 0; z < 2; z++) {
      a[z] = v.get(z);
      if (a[z] instanceof Function) a[z] = ((Function) a[z]).run(r);
    }
    return new Boolean(bool(a[0]) || bool(a[1]));
  }

  public Object simplify(RuntimeEnvironment r) {
    Object a[] = new Object[2];
    for (int z = 0; z < 2; z++) {
      a[z] = v.get(z);
      if (a[z] instanceof Function) {
        a[z] = ((Function) a[z]).simplify(r);
        if (a[z] instanceof Throw) return a[z]; else v.set(z, a[z]);
      }
    }
    if (a[0] instanceof Function) {
      if (a[1] instanceof Function) return this;
      if (bool(a[1])) return Boolean.TRUE;
      return a[0];
    } else {
      if (bool(a[0])) return Boolean.TRUE;
      if (a[1] instanceof Function) return a[1];
      return new Boolean(bool(a[1]));
    }
  }

  public String toString() { return "(" + v.get(0) + " || " + v.get(1) + ")"; }
}
