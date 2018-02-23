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
		super.put("ΦΠΑ", new Byte((byte) 19));
		super.put("Ποσότητα", new Double(1));
		super.put("ΜονάδαMέτρησης", "τεμάχια");
	}
	
	public String toString() { return super.get("Είδος").toString(); }
	
	private void recalculate() {
		Number nFpa = (Number) super.get("ΦΠΑ");
		if (nFpa == null) super.put("ΦΠΑ", nFpa = new Byte((byte) 0));
		double many = M.safeNumber2double((Number) super.get("Ποσότητα"));
		double cost = M.safeNumber2double((Number) super.get("ΤιμήΜονάδας"));
		double fpa = nFpa.doubleValue();
		if (cost != 0 && many != 0) {
			getDynamic().put("ΣυνολικήΤιμή", M.round(cost * many, 2));
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
	
	public Object put(String key, Object value) {
		if (value instanceof String) value = super.fromString(key, value.toString());
		if (value instanceof Number && ((Number) value).doubleValue() == 0 && !key.equals("ΦΠΑ")) value = null;
		if (key.equals("ΣυνολικήΤιμή")) {
			Number d = M.round(M.div((Number) value, (Number) super.get("Ποσότητα")), 4);
			super.put("ΤιμήΜονάδας", d);
		} else if (key.equals("ΣυνολικήΤιμήΜεΦΠΑ")) {
			Number d = M.round(M.div((Number) value, M.mul((Number) super.get("Ποσότητα"),
					1 + ((Number) super.get("ΦΠΑ")).doubleValue() / 100)), 4);
			super.put("ΤιμήΜονάδας", d);
		} else if (key.equals("ΤιμήMονάδαςMεΦΠΑ")) {
			Number d = M.round(M.div((Number) value, 1 + ((Number) super.get("ΦΠΑ")).doubleValue() / 100), 4);
			super.put("ΤιμήΜονάδας", d);
		} else {
			Object o = super.put(key, value);
			recalculate();
			return o;
		}
		recalculate();
		return get(key);
	}
}