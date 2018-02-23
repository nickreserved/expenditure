package script;

import java.io.*;
import java.util.*;

public class RuntimeEnvironment {
  public OutputStream out;
  public Hashtable stc;
  public Vector lcl;
  //public Hashtable functions;

  // every Block checks 'control' after each command execution.
  // if it is 0, proceed with next command else break execution.
  // 0:    proceed next command
  // > 0:  continue
  // < 0:  break
  static public final byte EXIT = -128;
  static public final byte RETURN = -127;
  public byte control = 0;

  public Object get(List v, int line, boolean array) {
    Object b = null, a = null;
    for (int z = 0; z < v.size(); z++) {
      b = a;
      a = v.get(z);
      if (a instanceof Function) a = ((Function) a).run(this);
      if (z == 0) {
        if (stc.containsKey(a)) { b = stc; a = stc.get(a); }
        else { b = lcl.lastElement(); a = ((Map) b).get(a); }
      } else if (b instanceof Map) a = ((Map) b).get(a);
      else if (b instanceof List && a instanceof Number)
        a = ((List) b).get(((Number) a).intValue());
      else throw new ExecuteException("CastingError", "<b>" + a + "</b> is not a member of <b>" + b + "</b>.", line);
    }
    return array ? b : a;
  }
}