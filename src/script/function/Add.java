// Object Add(Object, Object, ... , Object)

package script.function;

import script.*;
import java.util.*;


public class Add extends Function {
  public void setVector(List l) {
    if (l.size() < 2)
      throw new CompileException(getLine(), "<b><i>arg1</i> + <i>arg2</i> + <i>...</i> + <i>argn</i></b> has " + l.size() + " parameter(s)");
    v = l;
  }
  public Object run(RuntimeEnvironment r) {
    String ret = null;
    Number prev = null;
    for (int z = 0; z < v.size(); z++) {
      Object now = v.get(z);
      if (now instanceof Function) now = ((Function) now).run(r);
      if (now instanceof Number)
        prev = (prev == null) ? (Number) now : common.M.add((Number) prev, (Number) now);
      else {
        if (prev != null) {
          ret = (ret == null) ? prev.toString() : ret + prev;
          prev = null;
        }
        if (now != null) ret = (ret == null) ? now.toString() : ret + now;
      }
    }
    if (prev != null) {
      if (ret == null) return prev;
      else return ret + prev;
    }
    return ret;
  }

  public Object simplify(RuntimeEnvironment r) {
    Object p2 = null;
    Number p1 = null;
    Vector g = new Vector();
    for (int z = 0; z < v.size(); z++) {
      Object now = v.get(z);
      if (now instanceof Function) now = ((Function) now).simplify(r);
      if (now instanceof Throw) return now;
      else if (now instanceof Number)
        p1 = (p1 == null) ? (Number) now : common.M.add((Number) p1, (Number) now);
      else if (p2 instanceof Function) {
        g.add(p2);
        p2 = now;
        if (p1 != null) g.add(p1);
        p1 = null;
      } else  if (now instanceof Function) {
        if (p2 != null) g.add(p2);
        p2 = now;
        if (p1 != null) g.add(p1);
        p1 = null;
      } else {
        if (p1 != null) now = p1.toString() + (now == null ? "" : now);
        p1 = null;
        p2 = (p2 == null) ? now : p2 + (now == null ? "" : now.toString());
      }
    }
    v = g;
    if (p2 instanceof Function) {
      v.add(p2);
      p2 = null;
    }
    if (p1 != null) {
      if (p2 == null) v.add(p1); else p2 = p2.toString() + p1;
    }
    if (p2 != null) v.add(p2);
    if (v.size() == 1) return v.get(0);
    return this;
  }

  public String toString() {
    String s = "(";
    for (int z = 0; z < v.size(); z++) {
      if (z > 0) s += " + ";
      s += string(v.get(z));
    }
    return s + ")";
  }
}