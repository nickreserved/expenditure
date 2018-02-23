package script.function;

import script.*;
import java.util.*;


public class Mul extends Function {
  public void setVector(List l) {
    if (l.size() < 2) throw new CompileException(getLine(), "function <b>Mul</b> must take at least 2 parameters");
    v = l;
  }
  public Object run(RuntimeEnvironment r) {
    Number p = null;
    for (int z = 0; z < v.size(); z++) {
      Object now = v.get(z);
      if (now instanceof Function) now = ((Function) now).run(r);
      if (!(now instanceof Number)) throw new ExecuteException("CastingError", "<b>Mul</b> has a non-number parameter (<b>" + now + "</b>)", getLine());
      p = z == 0 ? (Number) now : common.M.mul(p, (Number) now);
    }
    return p;
  }

  public Object simplify(RuntimeEnvironment r) {
    Number p = null;
    Vector g = new Vector();
    for (int z = 0; z < v.size(); z++) {
      Object now = v.get(z);
      if (now instanceof Function) now = ((Function) now).simplify(r);
      if (now instanceof Number) p = p == null ? (Number) now : common.M.mul(p, (Number) now);
      else if (now instanceof Function) g.add(now);
      else if (now instanceof Throw) return now;
      else throw new CompileException(getLine(), "<b>Mul</b> has a non-number parameter (<b>" + now + "</b>)");
    }
    if (p != null) g.add(p);
    v = g;
    if (v.size() == 1) return v.get(0);
    return this;
  }

  public String toString() {
    String s = "(";
    for (int z = 0; z < v.size(); z++) {
      if (z > 0) s += " * ";
      s += v.get(z);
    }
    return s + ")";
  }
}
