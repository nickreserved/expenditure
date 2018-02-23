package script.function;

import script.*;
import java.util.*;


public class Incprev extends Function {
  public void setVector(List l) {
    if (l.size() != 1 || !(l.get(0) instanceof Get))
      throw new CompileException(getLine(), "function <b>Incprev</b> must take a <b>Get</b> parameter");
    v = l;
  }

  public Object run(RuntimeEnvironment r) {
    List g = ((Get) v.get(0)).getVector();
    Object b = r.get(g, getLine(), true);
    Object a = g.get(g.size() - 1);
    Object w;
    if (a instanceof Function) a = ((Function) a).run(r);
    if (b instanceof Map) {
      w = ((Map) b).get(a);
      if (w instanceof Number) {
        w = new Double(((Number) w).doubleValue() + 1);
        ((Map) b).put(a, w);
        return w;
      }
    } else if (b instanceof List && a instanceof Number) {
      w = ((List) b).get(((Number) a).intValue());
      if (w instanceof Number) {
        w = new Double(((Number) w).doubleValue() + 1);
        ((List) b).set(((Number) a).intValue(), w);
        return w;
      }
    }
    throw new ExecuteException("CastingError", "Cannot apply <b>Incprev</b> to <b>" + v.get(0) + "</b> because not a Number.", getLine());
  }

  // elegxos gia ypar3h yparxoyswn timwn alliws aplo simplify
  public Object simplify(RuntimeEnvironment r) {
    throw new CompileException(-1 , "Get Simplify not yet implemended");
    //return this;
  }

  public String toString() { return "++" + v.get(0); }
  public boolean isStatement() { return true; }
}