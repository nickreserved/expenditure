package cost;

import common.*;

public class BillItem extends HashString2Object {
	public BillItem() {
		classes.put("ΦΠΑ", Byte.class);
		classes.put("Ποσότητα", Double.class);
		classes.put("ΤιμήΜονάδας", Double.class);
		classes.put("ΣυνολικήΤιμή", Double.class);
		classes.put("ΤιμήMονάδαςMεΦΠΑ", Double.class);
		classes.put("ΣυνολικήΤιμήΜεΦΠΑ", Double.class);
		super.put("ΦΠΑ", 23);
		super.put("Ποσότητα", 1);
		super.put("ΜονάδαMέτρησης", "τεμάχια");
	}

	@Override
	public String toString() { return super.get("Είδος").toString(); }

	private void recalculate() {
		Number nFpa = (Number) super.get("ΦΠΑ");
		if (nFpa == null) super.put("ΦΠΑ", nFpa = 0);
		double many = M.safeNumber2double((Number) super.get("Ποσότητα"));
		double cost = M.safeNumber2double((Number) super.get("ΤιμήΜονάδας"));
		double fpa = nFpa.doubleValue();
		if (cost != 0 && many != 0) {
			getDynamic().put("ΣυνολικήΤιμή", M.round(cost * many, 3));
			getDynamic().put("ΣυνολικήΤιμήΜεΦΠΑ", M.round(cost * many * (1 + fpa / 100), 2));
		} else {
			getDynamic().remove("ΣυνολικήΤιμή");
			getDynamic().remove("ΣυνολικήΤιμήΜεΦΠΑ");
		}
		if (cost != 0)
			getDynamic().put("ΤιμήMονάδαςMεΦΠΑ", M.round(cost * (1 + fpa / 100), 4));
		else
			getDynamic().remove("ΤιμήMονάδαςMεΦΠΑ");
	}

	@Override
	public Object put(String key, Object value) {
		if (value instanceof String) value = super.fromString(key, value.toString());
		if (value instanceof Number && ((Number) value).doubleValue() == 0 && !key.equals("ΦΠΑ")) value = null;
		Number d;
		switch (key) {
			case "ΣυνολικήΤιμή": d = (Number) super.get("Ποσότητα"); break;
			case "ΣυνολικήΤιμήΜεΦΠΑ": d = M.mul((Number) super.get("Ποσότητα"),
						1 + ((Number) super.get("ΦΠΑ")).doubleValue() / 100);
				break;
			case "ΤιμήMονάδαςMεΦΠΑ":
				d = 1 + ((Number) super.get("ΦΠΑ")).doubleValue() / 100;
				break;
			default:
				Object o = super.put(key, value);
				recalculate();
				return o;
		}
		d = M.round(M.div((Number) value, d), 4);
		super.put("ΤιμήΜονάδας", d);
		recalculate();
		return get(key);
	}
}