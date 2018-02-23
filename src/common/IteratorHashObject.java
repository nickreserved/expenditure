package common;

public class IteratorHashObject extends HashObject {
	private static final String KEY_S = "*";
	private String k;

	public Object get() { return k == null ? null : get(k); }
	public String getPos() {return k; }
	public void setPos(String key) { if (containsKey(key)) k = key; }
	public void add(String key, Object val) { super.put(k = key, val); }

	@Override
	public Object put(String key, Object val) {
		if (k == null) k = key;
		return key.equals(KEY_S) ? k = val.toString() : super.put(key, val);
	}

	public Object remove() {
		if (k == null) return null;
		Object o = super.remove(k);
		k = (String) keySet().stream().findAny().orElse(null);
		return o;
	}

	@Override
	public StringBuilder save(StringBuilder out) {
		if (k != null) super.put(KEY_S, k);
		super.save(out);
		super.remove(KEY_S);
		return out;
	}
}