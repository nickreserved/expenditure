package cost;

import common.*;

public class Hold extends DynHashObject {

	public Hold() { getDynamic().put("Σύνολο", 0.0); }

	@Override
	public String toString() { return getDynamic().get("Σύνολο").toString(); }

	@Override
	public Object put(String key, Object value) {
		if (key.equals("Σύνολο")) return getDynamic().get("Σύνολο");
		double d;
		try { d = Functions.round(Double.parseDouble(value.toString()), 4); }
		catch (NumberFormatException | NullPointerException e) { d = 0.0; }
		Double o = (Double) super.put(key, d == 0 ? null : d);
		Double t = (Double) getDynamic().get("Σύνολο");
		if (o == null) o = 0.0;
		getDynamic().put("Σύνολο", Functions.round(t + d - o, 4));
		return o;
	}
}