package cost;

import java.util.*;
import common.*;

public class Material extends HashObject {
  public Material() {
    super.put("Ποσότητα", new Double(1));
    super.put("ΜονάδαMέτρησης", "τεμάχια");
  }

  public String toString() { return get("Υλικό").toString(); }

  public boolean isEmpty() { return get("Υλικό") == null && get("Ποσότητα") == null; }

  public Object put(Object key, Object value) {
    if (key.equals("Ποσότητα") && value instanceof String) {
      value = new Double(value.toString());
      if (((Number) value).doubleValue() == 0) return remove(key);
    }
    return super.put(key, value);
  }
}
