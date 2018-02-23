package cost;

import common.*;

public class Material extends HashObject {
  public Material() {
    super.put("Ποσότητα", 1d);
    super.put("ΜονάδαMέτρησης", "τεμάχια");
  }
  public String toString() { return get("Υλικό").toString(); }
  public Object put(String key, Object value) {
    if (key.equals("Ποσότητα") && value instanceof String)
      value = new Double(value.toString());
    return super.put(key, value);
  }
}
