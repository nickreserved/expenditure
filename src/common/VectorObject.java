package common;

import java.util.ArrayList;

public class VectorObject<E> extends ArrayList<E> implements Saveable, PhpSerialize {

	@Override
	public StringBuilder save(StringBuilder out) {
		removeDead();
		out.append("{\r\n");
		stream().forEach(i -> Saveable.save(out, null, i));
		return out.append("}");
	}

	@Override
	public StringBuilder serialize(StringBuilder out) {
		removeDead();
		out.append("a:").append(size()).append(":{");
		for (int z = 0; z < size(); z++) {
			out.append("i:").append(z).append(";");
			PhpSerialize.serialize(out, get(z));
		}
		return out.append("}");
	}

	/** Remove null entries before save. */
	//TODO: are there null entries?
	private void removeDead() {
		int z = 0;
		while(z < size())
			if (get(z) == null) remove(z); else ++z;
	}
}