// Boolean Equal(Object, Object)

package script.function;

import script.*;
import java.util.*;


public class Equal extends Function {
  public void setVector(List l) {
    if (l.size() != 2) throw new CompileException(getLine(), "<b><i>arg1</i> == <i>arg2</i></b> has " + l.size() + " parameter(s)");
    v = l;
  }
  public Object run(RuntimeEnvironment r) {
    Object a = v.get(0), b = v.get(1);
    if (a instanceof Function) a = ((Function) a).run(r);
    if (b instanceof Function) b = ((Function) b).run(r);
    return new Boolean(a.equals(b));
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
    if (a[0] instanceof Function || a[1] instanceof Function)
      return this;
    return new Boolean(a[0].equals(a[1]));
  }

  public String toString() { return "(" + v.get(0) + " == " + v.get(1) + ")"; }
}