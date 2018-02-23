package common;

import java.util.*;

public class HashObject extends HashMap<String, Object> implements Saveable, PhpSerialize {
	@Override
	public Object put(String key, Object value) {
		if (value instanceof String) {
			value = ((String) value).trim();
			if (value.equals("")) value = null;
		}
		if (value == null) return super.remove(key);
		return super.put(key, value);
	}

	@Override
	public String save() {
		String s = "{\r\n";
		Iterator<Map.Entry<String, Object>> it = entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry<String, Object> e = it.next();
			s += Functions.saveable(e.getKey(), e.getValue());
		}
		return s + "}";
	}

	@Override
	public String serialize() {
		String s = "a:" + size() + ":{";
		Iterator<Map.Entry<String, Object>> it = entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry<String, Object> e = it.next();
			s += Functions.phpSerialize(e.getKey()) + Functions.phpSerialize(e.getValue());
		}
		return s + "}";
	}
}