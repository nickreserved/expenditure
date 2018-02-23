package cost;

import java.util.*;
import common.*;

public class ContentItem extends HashObject {
	public ContentItem() { super.put("Πλήθος", 1); }
	public String toString() { return (String) get("Δικαιολογητικό"); }
	public boolean equals(Object o) { return o instanceof String ? o.toString().equals(toString()) : super.equals(o); }
	public Object put(String key, Object value) {
		if (!key.equals("Δικαιολογητικό") || !(value instanceof Map)) return super.put(key, value);
		Object many = get("Πλήθος");
		clear();
		super.put("Πλήθος", many);
		super.putAll((Map) value);
		return get("Δικαιολογητικό");
	}
}