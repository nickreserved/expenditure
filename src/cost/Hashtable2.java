package cost;

import java.util.*;

public class Hashtable2 extends Hashtable {
  public Object getExt(Object key) {
    Object o = super.get(key);
    if (o != null) return o;
    Enumeration en = super.keys();
    while (en.hasMoreElements()) {
      o = en.nextElement();
      if (o.toString().equals(key.toString()))
        return super.get(o);
    }
    return null;
  }
}