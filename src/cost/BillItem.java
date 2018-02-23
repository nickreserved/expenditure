package cost;

import common.HashString2Object;

public class BillItem extends HashString2Object {
	public BillItem() {
		classes.put("ΦΠΑ", Byte.class);
		classes.put("Ποσότητα", Double.class);
		classes.put("ΤιμήΜονάδας", Double.class);
		classes.put("ΣυνολικήΤιμή", Double.class);
		classes.put("ΤιμήMονάδαςMεΦΠΑ", Double.class);
		classes.put("ΣυνολικήΤιμήΜεΦΠΑ", Double.class);
		super.put("ΦΠΑ", (byte) 24);
		super.put("Ποσότητα", 1.0);
		super.put("ΜονάδαMέτρησης", "τεμάχια");
	}

	@Override
	public String toString() { return super.get("Είδος").toString(); }

	private void recalculate() {
		byte fpa = ((Number) super.get("ΦΠΑ")).byteValue();
		Number Many = (Number) super.get("Ποσότητα");
		double many = Many == null ? 0.0 : Many.doubleValue();
		Double Cost = (Double) super.get("ΤιμήΜονάδας");
		double cost = Cost == null ? 0.0 : Cost;
		if (cost != 0 && many != 0) {
			getDynamic().put("ΣυνολικήΤιμή", Bill.round(cost * many, 3));
			getDynamic().put("ΣυνολικήΤιμήΜεΦΠΑ", Bill.round(cost * many * (1 + fpa / 100.0), 2));
		} else {
			getDynamic().remove("ΣυνολικήΤιμή");
			getDynamic().remove("ΣυνολικήΤιμήΜεΦΠΑ");
		}
		if (cost != 0)
			getDynamic().put("ΤιμήMονάδαςMεΦΠΑ", Bill.round(cost * (1 + fpa / 100.0), 4));
		else
			getDynamic().remove("ΤιμήMονάδαςMεΦΠΑ");
	}

	@Override
	public Object put(String key, Object value) {
		if (value instanceof String) value = super.fromString(key, value.toString());
		if (value instanceof Number && ((Number) value).doubleValue() == 0 && !key.equals("ΦΠΑ")) value = null;
		double d;
		switch (key) {
			case "ΣυνολικήΤιμή": d = ((Number) super.get("Ποσότητα")).doubleValue(); break;
			case "ΣυνολικήΤιμήΜεΦΠΑ":
				d = ((Number) super.get("Ποσότητα")).doubleValue() *
						(1 + ((Number) super.get("ΦΠΑ")).byteValue() / 100.0);
				break;
			case "ΤιμήMονάδαςMεΦΠΑ":
				d = 1 + ((Number) super.get("ΦΠΑ")).byteValue() / 100.0;
				break;
			default:
				Object o = super.put(key, value);
				recalculate();
				return o;
		}
		d = Bill.round(((Double) value) / d, 4);
		super.put("ΤιμήΜονάδας", d);
		recalculate();
		return get(key);
	}
}