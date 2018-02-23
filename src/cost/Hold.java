package cost;

import java.util.*;

import common.*;

public class Hold extends HashObject {
  public String toString() { return get("Σύνολο").toString(); }

  public Object put(Object key, Object value) {
    Double d = new Double(0);
    try {
      d = new Double(value.toString());
    } catch(Exception e) {}
    return super.put(key, d.doubleValue() == 0 ? null : d);
  }

  public Object get(Object key) {
    Object o = super.get(key);
    if (o != null) return o;
    Double d = null;
    if (key.equals("Σύνολο")) {
      d = new Double(0);
      Enumeration en = this.elements();
      while (en.hasMoreElements())
	d = new Double(d.doubleValue() + ((Number) en.nextElement()).doubleValue());
      super.put("$" + key, d);
    }
    return d;
  }
}