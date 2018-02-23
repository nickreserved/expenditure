package cost;

import common.HashObject;
import common.VectorObject;

public class Work extends HashObject {
  public Work() {
    super.put("Ποσότητα", 1d);
    super.put("ΜονάδαMέτρησης", "τεμάχια");
    super.put("Υλικά", new VectorObject<>());
  }
	@Override
  public String toString() { return get("Εργασία").toString(); }
	@Override
  public Object put(String key, Object value) {
    if (key.equals("Ποσότητα") && value instanceof String)
      value = Double.parseDouble(value.toString());
    return super.put(key, value);
  }
}