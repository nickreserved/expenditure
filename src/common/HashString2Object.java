package common;

import java.lang.reflect.*;
import java.util.*;

public class HashString2Object extends DynHashObject {
	protected HashMap<String, Class> classes = new HashMap<>();

	@Override
	public Object put(String key, Object value) {
		if (value instanceof String)
			value = fromString(key, value.toString());
		return super.put(key, value);
	}

	protected Object fromString(String key, String value) {
		try {
			Class o = classes.get(key);
			if (o == null) return value;
			Constructor[] cs = o.getConstructors();
			for (Constructor c : cs) {
				Class[] cl = c.getParameterTypes();
				if (cl.length == 1 && cl[0].equals(String.class))
					return c.newInstance(new Object[] {value});
			}
		} catch(Exception e) {}
		return null;
	}
}