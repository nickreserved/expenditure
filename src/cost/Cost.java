package cost;

import common.*;

public class Cost extends HashString2Object {
	public Cost() {
		classes.put("Ποσό", Double.class);
		put("Τιμολόγια", new VectorObject());
	}
}
