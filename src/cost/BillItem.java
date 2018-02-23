package cost;

import common.*;

public class BillItem extends HashString2Object {
  public BillItem() {
    classes.put("ΦΠΑ", Byte.class);
    classes.put("Ποσότητα", Double.class);
    classes.put("ΤιμήΜονάδας", Double.class);
    classes.put("ΣυνολικήΤιμή", Double.class);
    classes.put("ΤιμήMονάδαςMεΦΠΑ", Double.class);
    classes.put("ΣυνολικήΤιμήΜεΦΠΑ", Double.class);
    super.put("ΦΠΑ", new Byte((byte) 19));
    super.put("Ποσότητα", new Double(1));
    super.put("ΜονάδαMέτρησης", "τεμάχια");
  }

  public String toString() { return super.get("Είδος").toString(); }
  public boolean equals(Object o) { return o instanceof BillItem && toString().equals(o.toString()); }

  public boolean isEmpty() {
    return super.get("Είδος") == null && super.get("ΤιμήΜονάδας") == null &&
	super.get("Ποσότητα") == null;
  }

  public Object get(Object key) {
    Object o = super.get(key);
    if (o != null) return o;
    if (key.equals("ΣυνολικήΤιμή"))
      o = M.round(M.mul((Number) super.get("ΤιμήΜονάδας"), (Number) super.get("Ποσότητα")), 2);
    else if (key.equals("ΤιμήMονάδαςMεΦΠΑ"))
      o = M.round(M.mul((Number) super.get("ΤιμήΜονάδας"),
		    (100 + ((Number) super.get("ΦΠΑ")).doubleValue()) / (double) 100), 4);
    else if (key.equals("ΣυνολικήΤιμήΜεΦΠΑ"))
      o = M.round(M.mul((Number) get("ΣυνολικήΤιμή"),
		    (100 + ((Number) super.get("ΦΠΑ")).doubleValue()) / (double) 100), 2);
    super.put("$" + key, o);
    return o;
  }

  public Object put(Object key, Object value) {
    if (value instanceof String) value = super.fromString(key, value.toString());
    if (value instanceof Number && ((Number) value).doubleValue() == 0 && !key.equals("ΦΠΑ")) value = null;
    if (key.equals("ΣυνολικήΤιμή")) {
      Number d = value == null ? null : M.round(M.div((Number) value, (Number) super.get("Ποσότητα")), 4);
      super.put("ΤιμήΜονάδας", d);
    } else if (key.equals("ΣυνολικήΤιμήΜεΦΠΑ")) {
      Number d = value == null ? null : M.round(M.div((Number) value, M.mul(
          (Number) super.get("Ποσότητα"), 1 + ((Number) super.get("ΦΠΑ")).doubleValue() / 100)), 4);
      super.put("ΤιμήΜονάδας", d);
    } else if (key.equals("ΤιμήMονάδαςMεΦΠΑ")) {
      Number d = value == null ? null : M.round(M.div((Number) value,
          1 + ((Number) super.get("ΦΠΑ")).doubleValue() / 100), 4);
      super.put("ΤιμήΜονάδας", d);
    } else
      return super.put(key, value);
    return get(key);
  }
}