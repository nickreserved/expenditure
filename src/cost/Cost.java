package cost;

import common.HashString2Object;
import common.VectorObject;

final public class Cost extends HashString2Object {
	public Cost() {
		classes.put("Ποσό", Double.class);
		put("Τιμολόγια", new VectorObject());
		put("Εργασίες", new VectorObject());
		put("ΕΦ", "11-200");
	}
}
