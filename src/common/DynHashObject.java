package common;

import java.util.TreeMap;
import java.util.stream.Stream;

public class DynHashObject<V> extends HashObject<V> {
	private final TreeMap<String, V> dyn = new TreeMap<>();

	public TreeMap<String, V> getDynamic() { return dyn; }

	@Override
	public V get(Object key) {
		V o = super.get(key);
		return o != null ? o : dyn.get(key);
	}

	@Override
	public StringBuilder serialize(StringBuilder out) {
		out.append("a:").append(size() + dyn.size()).append(":{");
		Stream.concat(entrySet().stream(), dyn.entrySet().stream())
				.forEach(e -> {
					PhpSerialize.serialize(out, e.getKey());
					PhpSerialize.serialize(out, e.getValue());
				});
		return out.append("}");
	}
}