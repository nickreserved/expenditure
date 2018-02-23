package common; 

import java.util.*;

public class IteratorHashObject extends HashObject {
	private static final String keyS = "*";
	private String k;
	
	public Object get() { return k == null ? null : get(k); }
	public String getPos() {return k; }
	public void setPos(String key) { if (containsKey(key)) k = key; }
	public void add(String key, Object val) { super.put(k = key, val); }

	public Object put(String key, Object val) {
		if (k == null) k = key;
		return key.equals(keyS) ? k = val.toString() : super.put(key, val);
	} 
	
	public Object remove() {
		if (k == null) return null;
		Object o = super.remove(k);
		Enumeration en = keys();
		k = en.hasMoreElements() ? en.nextElement().toString() : null;
		return o;
	}
	
	public String save() {
		if (k != null) super.put(keyS, k);
		String s = super.save();
		super.remove(keyS);
		return s;
	}
}