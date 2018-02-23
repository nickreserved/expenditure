package cost;

import common.DynHashObject;
import common.HashObject;
import common.VectorObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

public class Bill extends DynHashObject {
	public Bill() {
		super.put("ΠοσοστόΦΕ", (byte) 4);
		super.put("Κατηγορία", "Προμήθεια υλικών");
		super.put("Είδη", new VectorObject());
	}

	@Override public String toString() { return get("Τιμολόγιο").toString(); }

	@Override
	public Object put(String key, Object value) {
		Object o = super.put(key, value);
		recalculate();
		return o;
	}

	protected final void recalculate() {
		boolean check = MainFrame.data == null ? false :
				!Boolean.TRUE.equals(((HashObject) MainFrame.data.get("Ρυθμίσεις")).get("ΤιμολόγιοΧειροκίνητα"));
		try {
			ArrayList<BillItem> items = (ArrayList) get("Είδη");
			Double d = 0.0;
			double ka, pl, tfpa, thold;

			for (BillItem bi : items)
				d = d + (Double) bi.getDynamic().get("ΣυνολικήΤιμή");
			getDynamic().put("ΚαθαρήΑξία", ka = round(d, 2));

			HashObject h = new HashObject();
			if (check) setFpa();
			for (BillItem bi : items) {
				Byte fpa = ((Number) bi.get("ΦΠΑ")).byteValue();
				d = (Double) h.get(fpa.toString());
				double va = (double) bi.getDynamic().get("ΣυνολικήΤιμή");
				if (fpa != 0) {
					if (d == null) h.put(fpa.toString(), va); else h.put(fpa.toString(), d + va);
				}
			}
			d = 0d;
			Iterator<String> it = h.keySet().iterator();
			while (it.hasNext()) {
				Byte fpa = Byte.parseByte(it.next());
				double f = round((Double) h.get(fpa.toString()) * fpa / 100.0, 2);
				h.put(fpa.toString(), f);
				d = round(d + f, 2);
			}
			h.put("Σύνολο", tfpa = d);
			getDynamic().put("ΚατηγορίεςΦΠΑ", h);

			Hold hold = (Hold) get("ΑνάλυσηΚρατήσεωνΣεΠοσοστά");
			h = new HashObject();
			TreeMap<Double, String> tm = new TreeMap<>();
			double sum = round(ka * (double) hold.getDynamic().get("Σύνολο") / 100, 2);
			h.put("Σύνολο", thold = sum);
			Iterator en = hold.keySet().iterator();
			while (en.hasNext()) {
				String k = en.next().toString();
				double diff = d = ka * (double) hold.get(k) / 100;
				sum = sum - (d = round(d, 2));
				h.put(k, d);
				diff = 1000 * (diff - d) + Math.random() / 100;
				tm.put(diff, k);
			}
			int z = (int) (100 * round(sum, 4));
			if (z > 0) {
				for (; z > 0; z--) {
					Double last = tm.lastKey();
					String k = tm.get(last);
					h.put(k, (double) h.get(k) + 0.01);
					tm.remove(last);
				}
			} else {
				for (z =- z; z > 0; z--) {
					Double first = tm.firstKey();
					String k = tm.get(first);
					h.put(k, (double) h.get(k) - 0.01);
					tm.remove(first);
				}
			}
			getDynamic().put("ΑνάλυσηΚρατήσεωνΣεΕυρώ", h);

			String type = (String) ((Provider) get("Προμηθευτής")).get("Τύπος");
			double kat = "Δημόσιο".equals(type) || "Στρατός".equals(type) ? thold + tfpa : tfpa;
			getDynamic().put("Καταλογιστέο", kat = round(kat + ka, 2));

			getDynamic().put("Πληρωτέο", pl = round(kat - thold, 2));

			if (check) setFe();
			byte fe = ((Number) get("ΠοσοστόΦΕ")).byteValue();
			double fee, kak = fe == 3 ? ka : round(ka - thold, 2);
			getDynamic().put("ΚαθαρήΑξίαΓιαΦΕ", kak);
			getDynamic().put("ΦΕΣεΕυρώ", fee = round(kak * fe / 100.0, 2));

			getDynamic().put("ΥπόλοιποΠληρωτέο", round(pl - fee, 2));
		} catch(NullPointerException | NumberFormatException e) {}
	}

	protected void setFe() {
		byte a = ((Number) get("ΠοσοστόΦΕ")).byteValue();
		if (a == 0) return;
		String c = (String) get("Κατηγορία");
		if (!"Ιδιώτης".equals(((Provider) get("Προμηθευτής")).get("Τύπος"))) super.put("ΠοσοστόΦΕ", (byte) 0);
		else if (c.equals("Παροχή υπηρεσιών")) {
			Cost co = null;
			if (MainFrame.costs != null) co = (Cost) MainFrame.costs.get();
			if (co != null && CostData.COST[0].equals((String) co.get("ΤύποςΔαπάνης"))) { if (a != 3) super.put("ΠοσοστόΦΕ", (byte) 3); }
			else if (a != 8) super.put("ΠοσοστόΦΕ", (byte) 8);
		} else if (c.equals("Προμήθεια υλικών")) { if (a != 4) super.put("ΠοσοστόΦΕ", (byte) 4); }
		else if (c.equals("Αγορά υγρών καυσίμων")) { if (a != 1) super.put("ΠοσοστόΦΕ", (byte) 1); }
	}

	protected void setFpa() {
		List<BillItem> items = (List) get("Είδη");
		String a = (String) ((Provider) get("Προμηθευτής")).get("Τύπος");
		if ("Στρατός".equals(a) || "Ενοικιαστής".equals(a))
			items.forEach(i -> i.put("ΦΠΑ", (byte) 0));
	}

	public static double round(double a, int b) {
		long div = (long) Math.pow(10, b);
		return ((double) Math.round(a * div)) / div;
	}
}