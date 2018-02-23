package common;

import java.lang.reflect.*;
import java.util.*;

public class TreeFileLoader {
	private int pos = 0;
	private String html;
	private String key;
	private Object value;
	private TreeFileLoader() {}

	static public Object loadResource(String file) throws Exception {
		return load(LoadSaveFile.loadResource(file));
	}
	
	static public Object loadFile(String file) throws Exception {
		return load(LoadSaveFile.loadFile(file));
	}

	static public Object load(String html) throws Exception {
		TreeFileLoader c = new TreeFileLoader();
		c.html = html;
		return c.load();
	}

	private Object load() throws Exception {
		Object v = value = null;
		String k = key = null;
		skipRegex("\\s*");
		if (skipRegex("\\}\\s*;\\s*")) return value;

		int a = pos;
		skipRegex("\\S+");
		String s = html.substring(a, pos);
		Constructor[] cs = Class.forName(s).getConstructors();
		Constructor c0 = null, c1 = null;
		for (Constructor c : cs) {
			Class[] cl = c.getParameterTypes();
			if (cl.length == 0) c0 = c;
			else if (cl.length == 1 && cl[0].equals(String.class)) {
				c1 = c;
			}
		}

		if (c1 == null) v = c0.newInstance(new Object[0]);

		skipRegex("\\s*");
		if (html.startsWith("\"", pos)) {
			a = Functions.getStringClosePosition(html, pos);
			if (a == -1) throw new Exception();
			s = html.substring(++pos, a).replaceAll("\\\\\\\\", "\\\\").replaceAll("\\\\\\\"", "\\\"");
			int b = pos; pos = a + 1;
			if (skipRegex("\\s*=\\s*")) k = s; else pos = b - 1;
		} else {
			a = Functions.findEndOfRegex(html, pos, "[^\\\"\\{;\\s]+\\s*=\\s*");
			if (a != -1) {
				int b = pos;
				skipRegex("[^=\\s]+");
				k = html.substring(b, pos);
				pos = a;
			}
		}
		if (html.startsWith("{", pos) && (v instanceof List || v instanceof Map)) {
			pos++;
			while(load() != null)
				if (v instanceof List) ((List) v).add(value);
				else ((Map) v).put(key, value);
		} else {
			if (html.startsWith("\"", pos)) {
				a = Functions.getStringClosePosition(html, pos);
				if (a == -1) throw new Exception();
				s = html.substring(++pos, a).replaceAll("\\\\\\\\", "\\\\").replaceAll("\\\\\\\"", "\\\"");
				pos = a;
				skipRegex("\\\"\\s*;\\s*");
			} else {
				a = pos;
				skipRegex("[^\\s;]+");
				s = html.substring(a, pos);
				skipRegex("\\s*;\\s*");
			}
			if (c1 != null) v = c1.newInstance(new Object[] {s});
		}
		value = v; key = k;
		return v;
	}

	private boolean skipRegex(String regex) {
		int a = Functions.findEndOfRegex(html, pos, regex);
		if (a == -1) return false;
		pos = a;
		return true;
	}
}