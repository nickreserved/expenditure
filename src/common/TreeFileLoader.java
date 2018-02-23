package common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.nio.charset.Charset;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TreeFileLoader {
	public static final Charset GREEK = Charset.forName("windows-1253");

	private int pos = 0;
	private String html;
	private String key;
	private Object value;
	private TreeFileLoader() {}

	static public Object loadResource(String file) throws Exception {
		return load(loadFile(ClassLoader.getSystemResourceAsStream(file)));
	}
	
	static public Object loadFile(String file) throws Exception {
		return load(loadFile(new FileInputStream(file)));
	}
	
	public static String loadFile(InputStream is) throws IOException {
		// Backward compatibility for older saves with charset windows-1253.
		// Replace after year 2020 with: return new String(is.readAllBytes(), UTF_8);
		byte[] b = is.readAllBytes();
		int count = 0;
		// Dumb guessing, if file is in windows-1253 or utf8 encoding
		for (byte i : b) {
			if (i == (byte) 206) {
				++count; // utf text with greek letters has many characters 206
			}
		}
		return new String(b, count > b.length / 20 ? UTF_8 : GREEK);
	}

	static public Object load(String html) throws Exception {
		TreeFileLoader c = new TreeFileLoader();
		c.html = html;
		return c.load();
	}

	@SuppressWarnings("null")
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
			a = getStringClosePosition(html, pos);
			if (a == -1) throw new Exception();
			s = html.substring(++pos, a).replaceAll("\\\\\\\\", "\\\\").replaceAll("\\\\\\\"", "\\\"");
			int b = pos; pos = a + 1;
			if (skipRegex("\\s*=\\s*")) k = s; else pos = b - 1;
		} else {
			a = findEndOfRegex(html, pos, "[^\\\"\\{;\\s]+\\s*=\\s*");
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
				a = getStringClosePosition(html, pos);
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
		int a = findEndOfRegex(html, pos, regex);
		if (a == -1) return false;
		pos = a;
		return true;
	}
	
	// return end of a regex which starts here or -1
	static private int findEndOfRegex(String s, int start, String regex) {
		Matcher m = Pattern.compile(regex).matcher(s);
		return m.find(start) && m.start() == start ? m.end() : -1;
	}

	// if string starts with ', return index of closest ', else
	// find where a string close with " and returns offset.
	// it parses \" as character " and not as string closer.
	// it check 'start' character as string starter (char " or ')
	static private int getStringClosePosition(String s, int start) {
		if (s.charAt(start) == '\'') return s.indexOf("'", start + 1);
		else if (s.charAt(start) != '"') return -1;
		while ((start = s.indexOf('"', ++start)) != -1) {
			int c = start;
			while (c != 0 && s.charAt(--c) == '\\') {}
			c = (start - c) % 2;
			if (c == 1) break;
		}
		return start;
	}
}