package common;

import java.util.*;

public class HashObject extends Hashtable<String, Object> implements Saveable, PhpSerialize {
	public Object get(Object key) {
		return containsKey(key) ? super.get(key) : super.get("$" + key);
	}
	
	public Object put(String key, Object value) {
		if (value instanceof String) {
			value = ((String) value).trim();
			if (value.equals("")) value = null;
		}
		if (value == null) return super.remove(key);
		return super.put(key, value);
	}
	
	public String save() {
		String s = "{\r\n";
		Enumeration en = keys();
		while(en.hasMoreElements()) {
			String key = (String) en.nextElement();
			s += Functions.saveable(key, get(key));
		}
		return s + "}";
	}
	
	public boolean isEmpty() {
		if (super.isEmpty()) return true;
		Enumeration en = keys();
		while (en.hasMoreElements()) {
			String key = (String) en.nextElement();
			if (!key.startsWith("$")) return false;
		}
		return true;
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof HashObject)) return false;
		HashObject h = (HashObject) o;
		if (h.size() != size()) return false;
		Enumeration en = keys();
		while (en.hasMoreElements()) {
			String key = (String) en.nextElement();
			if (!get(key).equals(h.get(key))) return false;
		}
		return true;
	}
	
	public String serialize() {
		String ths = toString();
		String s;
		if (ths == null) s = "a:" + size() + ":{";
		else
			s = "a:" + size() + ":{";
		Enumeration en = keys();
		while(en.hasMoreElements()) {
			String key = (String) en.nextElement();
			if (key.startsWith("$")) key = key.substring(1);
			s += Functions.phpSerialize(key) + Functions.phpSerialize(get(key));
		}
		return s + "}";
	}
	
	public String toString() { return null; }
}