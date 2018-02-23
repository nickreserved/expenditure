package cost;

import java.util.*;
import common.*;

public class Bill extends DynHashObject {
	public Bill() {
		put("ΠοσοστόΦΕ", new Byte((byte) 4));
		put("Τύπος", "Τιμολόγιο");
		put("Κατηγορία", "Προμήθεια υλικών");
		put("Είδη", new VectorObject());
	}
	
	public String toString() { return super.get("Τιμολόγιο").toString(); }
	
	public Object put(String key, Object value) {
		Object o = super.put(key, value);
		recalculate();
		return o;
	}
	
	protected final void recalculate() {
		try {
			Vector items = (Vector) get("Είδη");
			Number kak, ka, kat, pl, fe, tfpa, thold, d = new Double(0);
			
			
			for (int z = 0; z < items.size(); z++)
				d = M.add(d, (Number) ((BillItem) items.get(z)).getDynamic().get("ΣυνολικήΤιμή"));
			getDynamic().put("ΚαθαρήΑξία", ka = M.round(d, 2));
			
			
			Dictionary h = new HashObject();
			setFpa();
			for (int z = 0; z < items.size(); z++) {
				BillItem bi = (BillItem) items.get(z);
				Number fpa = (Number) bi.get("ΦΠΑ");
				d = (Number) h.get(fpa);
				Number va = (Number) bi.getDynamic().get("ΣυνολικήΤιμή");
				if (fpa.doubleValue() != 0) {
					if (d == null) h.put(fpa.toString(), va); else h.put(fpa.toString(), M.add(d, va));
				}
			}
			d = new Double(0);
			Enumeration en = h.keys();
			while (en.hasMoreElements()) {
				Number fpa = new Byte(en.nextElement().toString());
				Number f = M.round(M.mul((Number) h.get(fpa.toString()), fpa.doubleValue() / 100), 2);
				h.put(fpa.toString(), f);
				d = M.round(M.add(d, f), 2);
			}
			h.put("Σύνολο", tfpa = d);
			getDynamic().put("ΚατηγορίεςΦΠΑ", h);
			
			
			Hold hold = (Hold) super.get("ΑνάλυσηΚρατήσεωνΣεΠοσοστά");
			h = new HashObject();
			TreeMap tm = new TreeMap();
			Number sum = M.round(M.mul(ka, M.div((Number) hold.getDynamic().get("Σύνολο"), 100)), 2);
			h.put("Σύνολο", thold = sum);
			en = hold.keys();
			while (en.hasMoreElements()) {
				String k = en.nextElement().toString();
				d = M.mul(ka, M.div((Number) hold.get(k), 100));
				double diff = d.doubleValue();
				sum = M.sub(sum, d = M.round(d, 2));
				h.put(k, d);
				diff = 1000 * (diff - d.doubleValue()) + Math.random() / 100;
				tm.put(new Double(diff), k);
			}
			int z = (int) (100 * M.round(sum, 4).doubleValue());
			if (z > 0) {
				for (; z > 0; z--) {
					Object last = tm.lastKey();
					Object k = tm.get(last);
					h.put(k, M.add((Number) h.get(k), 0.01));
					tm.remove(last);
				}
			} else {
				for (z =- z; z > 0; z--) {
					Object first = tm.firstKey();
					Object k = tm.get(first);
					h.put(k, M.sub((Number) h.get(k), 0.01));
					tm.remove(first);
				}
			}
			getDynamic().put("ΑνάλυσηΚρατήσεωνΣεΕυρώ", h);
			
			
			getDynamic().put("Καταλογιστέο", kat = M.round(M.add("ΣΠ/ΚΨΜ".equals(super.get("Τύπος")) ? thold : tfpa, ka), 2));
			
			getDynamic().put("Πληρωτέο", pl = M.round(M.sub(kat, thold), 2));
			
			getDynamic().put("ΚαθαρήΑξίαΜείονΚρατήσεις", kak = M.round(M.sub(ka, thold), 2));
			
			setFe();
			getDynamic().put("ΦΕΣεΕυρώ", fe = M.round(M.mul(kak, ((Number) get("ΠοσοστόΦΕ")).doubleValue() / 100), 2));
			
			getDynamic().put("ΥπόλοιποΠληρωτέο", M.round(M.sub(pl, fe), 2));
		} catch(Exception e) {}
	}
	
	protected void setFe() {
		if (!super.get("Τύπος").equals("Τιμολόγιο")) super.put("ΠοσοστόΦΕ", new Byte((byte) 0));
		else if (((Number) super.get("ΠοσοστόΦΕ")).doubleValue() != 0)
			if (super.get("Κατηγορία").equals("Παροχή υπηρεσιών")) super.put("ΠοσοστόΦΕ", new Byte((byte) 8));
			else if (super.get("Κατηγορία").equals("Προμήθεια υλικών")) super.put("ΠοσοστόΦΕ", new Byte((byte) 4));
			else super.put("ΠοσοστόΦΕ", new Byte((byte) 1));
	}
	
	protected void setFpa() {
		List items = (List) get("Είδη");
		for (int z = 0; z < items.size(); z++) {
			BillItem bi = (BillItem) items.get(z);
			if (super.get("Τύπος").equals("ΣΠ/ΚΨΜ"))
				bi.put("ΦΠΑ", new Byte((byte) 0));
			else if (((Number) bi.get("ΦΠΑ")).doubleValue() == 0)
				bi.put("ΦΠΑ", new Byte((byte) 19));
		}
	}
}