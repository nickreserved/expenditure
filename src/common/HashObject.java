package common;

import java.util.*;

public class HashObject extends Hashtable implements Saveable, Hash {
  public Object get(Object key) {
    return containsKey(key) ? super.get(key) : super.get("$" + key);
  }

  public Object put(Object key, Object value) {
    if (value instanceof String) {
      value = ((String) value).trim();
      if (value.equals("")) value = null;
    }
    if (!key.toString().startsWith("$")) removeTemporary();
    if (value == null) return super.remove(key);
    return super.put(key, value);
  }

  public void removeTemporary() {
    Enumeration en = keys();
    while (en.hasMoreElements()) {
      String key = en.nextElement().toString();
      if (key.startsWith("$")) super.remove(key);
    }
  }

  public String save() {
    String s = "{\r\n";
    Enumeration en = keys();
    while(en.hasMoreElements()) {
      String key = en.nextElement().toString();
      Object value = get(key);
      if (value != null && !key.startsWith("$") &&
	  !(value instanceof List && ((List) value).size() == 0) &&
	  !(value instanceof Dictionary && ((Dictionary) value).size() == 0) &&
	  !(value instanceof Map && ((Map) value).size() == 0)) {
	s += value.getClass().getName() + " " + key + " = ";
	if (value instanceof Saveable)
	  s += ((Saveable) value).save();
	else {
	  String t = value.toString();
	  if (!(value instanceof Number))
	    t = "\"" + t.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\\"", "\\\\\\\"") + "\"";
	  s += t;
	}
	s += ";\r\n";
      }
    }
    return s + "}";
  }

  public boolean isEmpty() {
    if (super.isEmpty()) return true;
    Enumeration en = keys();
    while (en.hasMoreElements()) {
      Object key = en.nextElement();
      if (!key.toString().startsWith("$") && get(key) != null) return false;
    }
    return true;
  }

  public Object remove(Object key) {
    removeTemporary();
    return super.remove(key);
  }

  public boolean equals(Object o) {
    if (!(o instanceof HashObject)) return false;
    HashObject h = (HashObject) o;
    if (h.size() != size()) return false;
    Enumeration en = keys();
    while (en.hasMoreElements()) {
      Object key = en.nextElement();
      if (!get(key).equals(h.get(key))) return false;
    }
    return true;
  }
}