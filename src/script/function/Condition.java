// Object Condition(Object, Object, Object)

package script.function;

import script.*;
import java.util.*;


public class Condition extends Function {
  public void setVector(List l) {
    if (l.size() != 3)
      throw new CompileException(getLine(), "<b><i>arg1</i> ? <i>arg2</i> : <i>arg3</i></b> has " + l.size() + " parameter(s)");
    v = l;
  }
  public Object run(RuntimeEnvironment r) {
    int z = bool(((Function) v.get(0)).run(r)) ? 1 : 2;
    Object a = v.get(z);
    if (a instanceof Function) a = ((Function) a).run(r);
    return a;
  }

  public Object simplify(RuntimeEnvironment r) {
    Object a[] = new Object[3];
    for (int z = 0; z < 3; z++) {
      a[z] = v.get(z);
      if (a[z] instanceof Function) {
        a[z] = ((Function) a[z]).simplify(r);
        if (a[z] instanceof Throw) {
          if (z == 0) return a[0];
          // else: if members are Throw we have a problem!...
        } else v.set(z, a[z]);
      }
    }
    if (a[0] instanceof Function) return this;
    return bool(a[0]) ? a[1] : a[2];
  }

  public String toString() { return "(" + v.get(0) + " ? " + v.get(1) + " : " + v.get(2) + ")"; }
}
