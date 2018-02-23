package common;

import java.io.FileOutputStream;
import java.io.IOException;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.util.List;
import java.util.Map;

/** Object can be saved to a file. */
public interface Saveable {
	/** Save object as string.
	 * @param out String buffer where object will be saved
	 * @return out */
	StringBuilder save(StringBuilder out);
	
	/** Save a pair of key-value as string.
	 * @param out String buffer where pair will be saved
	 * @param key The key, which can be null
	 * @param value The value, which cannot be null
	 * @return out */
	static StringBuilder save(StringBuilder out, String key, Object value) {
		// If value is empty do not store anything
		if (value instanceof List && ((List) value).isEmpty() ||
				value instanceof Map && ((Map) value).isEmpty())
			return out;
		// Store value type and key name if exists
		out.append(value.getClass().getName()).append(" ");
		if (key != null) {
			if (key.matches(".*[\\{;\\s].*"))
				out.append("\"").append(key.replaceAll("\\\\", "\\\\\\\\")
						.replaceAll("\\\"", "\\\\\\\"")).append("\"");
			else out.append(key);
			out.append(" = ");
		}
		// Store value
		if (value instanceof Saveable)
			return ((Saveable) value).save(out).append(";\r\n");
		else {
			if (value instanceof Number) out.append(value);
			else out.append("\"").append(value.toString().replaceAll("\\\\", "\\\\\\\\")
					.replaceAll("\\\"", "\\\\\\\"")).append("\"");
			return out.append(";\r\n");
		}
	}
	
	/** Save an object to a file.
	 * @param file Filename of output file
	 * @param sv Object to save, which cannot be null */
	static void save(String file, Object sv) throws IOException {
		String[] d = save(new StringBuilder(131072), null, sv).toString().split("\r\n");
		StringBuilder sb = new StringBuilder(131072);
		int c = 0;
		for (String d1 : d) {
			if (d1.startsWith("}") && c > 0) c--;
			sb.append("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t".substring(0, c))
					.append(d1).append("\r\n");
			if (d1.endsWith("{")) c++;
		}
		// save to file
		try (FileOutputStream f = new FileOutputStream(file)) {
			f.write(sb.toString().getBytes(UTF_8));
			f.close();
		}	
	}
}