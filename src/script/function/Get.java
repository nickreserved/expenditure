package script.function;

import script.*;
import java.util.*;


public class Get extends Function {
  public void setVector(List l) {
    if (l.size() != 0)
      throw new CompileException(getLine(), "function <b>Get</b> must take at least 1 parameter");
    v = l;
  }

  public Object run(RuntimeEnvironment r) { return r.get(v, getLine(), false); }

  // elegxos gia ypar3h yparxoyswn timwn alliws aplo simplify
  public Object simplify(RuntimeEnvironment r) {
/*    Object b = r.get(v.get(0));
    if (v.size() == 1) return b;
    for (int z = 1; z < v.size(); z++) {
      Object a = v.get(z);
      if (a instanceof Function) a = ((Function) a).run(r);
      if (b instanceof Map) b = ((Map) b).get(a);
      else if (b instanceof List && a instanceof Number)
        b = ((List) b).get(((Number) a).intValue());
      else throw new ExecuteException("CastingError", "<b>" + a + "</b> is not a member of <b>"
                                      + b + "</b>. <b>Get</b> error.", getLine());
    }
    return b;
//-------------------------------------------------
    Object a = v.get(0);
    if (a instanceof Function) {
      v.set(0, a = ((Function) a).simplify(r));
      if (a instanceof Throw) return a;
    }
    if (v.size() == 2) {
      v.set(1, a = ((Function) v.get(1)).simplify(r));
      if (a instanceof Throw) return a;
    }*/
throw new CompileException(-1 , "Get Simplify not yet implemended");
    //return this;
  }

  public String toString() {
    boolean gt = false;
    String r = "";
    for (int z = 0; z < v.size(); z++) {
      Object a = v.get(z);
      if (a instanceof String && a.toString().matches("[_\\p{Alpha}\\p{InGreek}][_\\p{Alnum}\\p{InGreek}]*"))
        r += z == 0 ? a : "." + a;
      else {
        if (!(a instanceof Number || a instanceof Function))
          a = '"' + a.toString().replaceAll("\\\\", "\\\\\\\\").replaceAll("\\\"", "\\\\\\\"") + '"';
        if (z != 0) r += "[" + a + "]";
        else r = "get(" + a + ")";
      }
    }
    return r;
  }
}
