package common;

import java.util.*;

public class DynHashObject extends HashObject {
	private HashMap<String, Object> dyn = new HashMap<String, Object>();

	public HashMap<String, Object> getDynamic() { return dyn; }

	@Override
	public Object get(Object key) {
		Object o = super.get(key);
		return o != null ? o : dyn.get(key);
	}

	@Override
	public String serialize() {
		String s = "a:" + (size() + dyn.size()) + ":{";
		Iterator<Map.Entry<String, Object>> it = entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry<String, Object> e = it.next();
			s += Functions.phpSerialize(e.getKey()) + Functions.phpSerialize(e.getValue());
		}
		it = dyn.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry<String, Object> e = it.next();
			s += Functions.phpSerialize(e.getKey()) + Functions.phpSerialize(e.getValue());
		}
		return s + "}";
	}
}