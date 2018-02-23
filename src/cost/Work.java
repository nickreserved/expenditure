package cost;

import common.*;

public class Work extends HashObject {
  public Work() {
    super.put("Ποσότητα", 1d);
    super.put("ΜονάδαMέτρησης", "τεμάχια");
    super.put("Υλικά", new VectorObject());
  }
  public String toString() { return get("Εργασία").toString(); }
  public Object put(String key, Object value) {
    if (key.equals("Ποσότητα") && value instanceof String)
      value = new Double(value.toString());
    return super.put(key, value);
  }
}