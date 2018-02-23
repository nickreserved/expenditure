package cost;

import common.*;

public class ContentItem extends HashObject {
	public ContentItem() { super.put("Πλήθος", 1); }
	public String toString() { return (String) get("Δικαιολογητικό"); }
}