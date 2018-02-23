package common;

import java.util.*;

public class HashObject extends Hashtable<String, Object> implements Saveable, PhpSerialize {
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
	
	public String serialize() {
		String s = "a:" + size() + ":{";
		Enumeration en = keys();
		while(en.hasMoreElements()) {
			String key = (String) en.nextElement();
			s += Functions.phpSerialize(key) + Functions.phpSerialize(get(key));
		}
		return s + "}";
	}
}