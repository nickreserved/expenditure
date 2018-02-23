package script.function;

import script.*;
import java.util.*;


public class Set extends Function {
  public void setVector(List l) {
    if (l.size() != 2)
      throw new CompileException(getLine(), "<b><i>arg1</i> = <i>arg2</i></b> has " + l.size() + " parameter(s)");
    if (l.get(0) instanceof Get) v = l;
    else throw new CompileException(getLine(), "<b><i>arg1</i> = <i>arg2</i></b> has <b><i>arg1</i> instanceof " + v.get(0).getClass().getName() + "</b>");
  }

  public Object run(RuntimeEnvironment r) {
    List g = ((Get) v.get(0)).getVector();
    Object b = r.get(g, getLine(), true);
    Object a = g.get(g.size() - 1);
    if (a instanceof Function) a = ((Function) a).run(r);
    Object w = v.get(v.size() - 1);
    if (w instanceof Function) w = ((Function) w).run(r);
    if (b instanceof Map) ((Map) b).put(a, w);
    else if (b instanceof List) ((List) b).set(((Number) a).intValue(), w);
    return w;
  }

  // elegxos gia ypar3h yparxoyswn timwn alliws aplo simplify
  public Object simplify(RuntimeEnvironment r) {
    throw new CompileException(-1 , "Get Simplify not yet implemended");
    //return this;
  }

  public String toString() { return "(" + v.get(0) + " = " + v.get(1) + ")"; }
  public boolean isStatement() { return true; }
}