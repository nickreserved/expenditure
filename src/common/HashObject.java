package common;

import java.util.TreeMap;

public class HashObject<V> extends TreeMap<String, V> implements Saveable, PhpSerialize {
	@Override
	public V put(String key, V value) {
		if (value instanceof String) {
			value = (V) ((String) value).trim();
			if (value.equals("")) value = null;
		}
		if (value == null) return super.remove(key);
		return super.put(key, value);
	}

	@Override
	public StringBuilder save(StringBuilder out) {
		out.append("{\r\n");
		entrySet().stream().forEach(i-> Saveable.save(out, i.getKey(), i.getValue()));
		return out.append("}");
	}

	@Override
	public StringBuilder serialize(StringBuilder out) {
		out.append("a:").append(size()).append(":{");
		entrySet().stream().forEach(i -> {
			PhpSerialize.serialize(out, i.getKey());
			PhpSerialize.serialize(out, i.getValue());
		});
		return out.append("}");
	}
}