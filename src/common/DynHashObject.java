package common;

import java.util.*;

public class DynHashObject extends HashObject {
	private Hashtable<String, Object> dyn = new Hashtable<String, Object>();
	
	public Hashtable<String, Object> getDynamic() { return dyn; }
	
	public Object get(Object key) {
		Object o = super.get(key);
		return o != null ? o : dyn.get(key);
	}
	
	public String serialize() {
		String s = "a:" + (size() + dyn.size()) + ":{";
		Hashtable h = this;
		for (int z = 0; z < 2; z++, h = dyn) {
			Enumeration en = h.keys();
			while(en.hasMoreElements()) {
				String key = (String) en.nextElement();
				s += Functions.phpSerialize(key) + Functions.phpSerialize(h.get(key));
			}
		}
		return s + "}";
	}
}