package cost;

import java.util.*;
import common.*;

public class Work extends HashObject {
  public Work() {
    super.put("Ποσότητα", new Double(1));
    super.put("ΜονάδαMέτρησης", "τεμάχια");
    super.put("Υλικά", new VectorObject());
  }

  public String toString() { return get("Εργασία").toString(); }

  public boolean isEmpty() { return get("Εργασία") == null && get("Ποσότητα") == null; }

  public Object put(String key, Object value) {
    if (key.equals("Ποσότητα") && value instanceof String) {
      value = new Double(value.toString());
      if (((Number) value).doubleValue() == 0) return remove(key);
    }
    return super.put(key, value);
  }
}