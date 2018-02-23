package cost;

import common.HashObject;

public class ContentItem extends HashObject {
	public ContentItem() { super.put("Πλήθος", (byte) 1); }
	@Override public String toString() { return (String) get("Δικαιολογητικό"); }
}