package cost;

import common.*;

public class Work extends HashObject {
  public Work() {
    super.put("��������", 1d);
    super.put("������M�������", "�������");
    super.put("�����", new VectorObject<>());
  }
	@Override
  public String toString() { return get("�������").toString(); }
	@Override
  public Object put(String key, Object value) {
    if (key.equals("��������") && value instanceof String)
      value = new Double(value.toString());
    return super.put(key, value);
  }
}