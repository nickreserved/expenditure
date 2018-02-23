package cost;

import common.*;

public class Provider extends HashObject {
	public Provider() { super.put("Τύπος", "Ιδιώτης"); } // backward compatibility
	@Override
  public String toString() { return get("Επωνυμία").toString(); }
}