package common;

import java.util.*;
import javax.swing.*;
import java.util.regex.*;

public class Functions {
	private Functions() {}
	
	
	public static final String phpSerialize(Object o) {
		if (o == null) return "N;";
		else if (o instanceof PhpSerialize) return ((PhpSerialize) o).serialize();
		else if (o instanceof Double || o instanceof Float) return "d:" + o + ";";
		else if (o instanceof Number) return "i:" + o + ";";
		else if (o instanceof Boolean) return "b:" + (o.equals(Boolean.TRUE) ? 1 : 0) + ";";
		else return "s:" + o.toString().length() + ":\"" + o + "\";";
	}
	
	public static final String saveable(String key, Object value) {
		if (value instanceof List && ((List) value).isEmpty() ||
				value instanceof Dictionary && ((Dictionary) value).isEmpty() ||
				value instanceof Map && ((Map) value).isEmpty())
			return "";
		
		String s = value.getClass().getName() + " ";
		if (key != null)
			s += (key.matches(".*[\\{;\\s].*") ? "\"" + key.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\\"", "\\\\\\\"") + "\"" : key) + " = ";
		
		if (value instanceof Saveable)
			return s + ((Saveable) value).save() + ";\r\n";
		else {
			String t = value.toString();
			if (!(value instanceof Number))
				t = "\"" + t.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\\"", "\\\\\\\"") + "\"";
			return s + t + ";\r\n";
		}
	}
	
	
	// return end of a regex which starts here or -1
	final static public int findEndOfRegex(String s, int start, String regex) {
		Matcher m = Pattern.compile(regex).matcher(s);
		return m.find(start) && m.start() == start ? m.end() : -1;
	}

	
	// if string starts with ', return index of closest ', else
	// find where a string close with " and returns offset.
	// it parses \" as character " and not as string closer.
	// it check 'start' character as string starter (char " or ')
	final static public int getStringClosePosition(String s, int start) {
		if (s.charAt(start) == '\'') return s.indexOf("'", start + 1);
		else if (s.charAt(start) != '"') return -1;
		while ((start = s.indexOf('"', ++start)) != -1) {
			int c = start;
			while (c != 0 && s.charAt(--c) == '\\');
			c = (start - c) % 2;
			if (c == 1) break;
		}
		return start;
	}
	
	
	public static void showExceptionMessage(java.awt.Component c, Exception e, String title, String info) {
		if (info == null) info = ""; else info += "<br>";
		if (e != null) {
			info += "Σφάλμα: <b>" + e.getClass().getName() + "</b>";
			String s = e.getLocalizedMessage();
			if (s != null && s.length() > 7) info += "<br>Λόγος: <b>" + s + "</b>";
		}
		JOptionPane.showMessageDialog(null, "<html>" + info, title, JOptionPane.ERROR_MESSAGE);
	}
}