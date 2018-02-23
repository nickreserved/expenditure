package common;

/** Serialize object for unserializing as PHP object. */
public interface PhpSerialize {
	/** Serialize object as string.
	 * @param out String buffer where object will be serialized
	 * @return out */
	StringBuilder serialize(StringBuilder out);
	
	static StringBuilder serialize(StringBuilder out, Object o) {
		if (o == null) return out.append("N;");
		else if (o instanceof PhpSerialize) return ((PhpSerialize) o).serialize(out);
		else if (o instanceof Double || o instanceof Float) return out.append("d:").append(o).append(";");
		else if (o instanceof Number) return out.append("i:").append(o).append(";");
		else if (o instanceof Boolean) return out.append("b:").append(o.equals(Boolean.TRUE) ? 1 : 0).append(";");
		else return out.append("s:").append(o.toString().length()).append(":\"").append(o).append("\";");
	}
}
