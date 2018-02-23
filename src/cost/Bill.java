package cost;

import java.util.*;
import common.*;

public class Bill extends HashObject {
  public Bill() {
    put("ΠοσοστόΦΕ", new Byte((byte) 4));
    put("Τύπος", "Τιμολόγιο");
    put("Κατηγορία", "Προμήθεια υλικών");
    put("Είδη", new VectorObject());
  }

  public boolean isEmpty() { return super.get("Τιμολόγιο") == null; }
  public String toString() { return super.get("Τιμολόγιο").toString(); }
  public boolean equals(Object o) { return o instanceof Bill && toString().equals(o.toString()); }

  public Object put(Object key, Object value) {
    if (key.equals("Τιμολόγιο") && !isValidBill(value.toString()) && !value.toString().equals(""))
      return null;
    return super.put(key, value);
  }

  public Object get(Object key) {
    Object o = super.get(key);
    if (o != null) return o;

    Vector items = (Vector) get("Είδη");
    Number d = new Double(0);

    if (key.equals("ΚαθαρήΑξία")) {
      for (int z = 0; z < items.size(); z++)
	d = M.add(d, (Number) ((BillItem) items.get(z)).get("ΣυνολικήΤιμή"));
      o = M.round(d, 2);
    } else if (key.equals("ΚατηγορίεςΦΠΑ")) {
      Hashtable h = new Hashtable();
      setFpa();
      for (int z = 0; z < items.size(); z++) {
	BillItem bi = (BillItem) items.get(z);
	Number fpa = (Number) bi.get("ΦΠΑ");
	d = (Number) h.get(fpa);
	Number va = (Number) bi.get("ΣυνολικήΤιμή");
	if (fpa.doubleValue() != 0) {
	  if (d == null) h.put(fpa, va); else h.put(fpa, M.add(d, va));
	}
      }
      d = new Double(0);
      Enumeration en = h.keys();
      while (en.hasMoreElements()) {
	Number fpa = (Number) en.nextElement();
	Number f = (Number) h.get(fpa);
	h.put(fpa, f = M.round(M.mul(f, fpa.doubleValue() / 100), 2));
	d = M.add(d, f);
      }
      h.put("Σύνολο", M.round(d, 2));
      o = h;

    } else if (key.equals("ΑνάλυσηΚρατήσεωνΣεΕυρώ")) {
      Hashtable h = new Hashtable(), hold = (Hashtable) super.get("ΑνάλυσηΚρατήσεωνΣεΠοσοστά");
      TreeMap tm = new TreeMap();
      Number ka = (Number) get("ΚαθαρήΑξία");
      Number sum = M.round(M.mul(ka, M.div((Number) hold.get("Σύνολο"), 100)), 2);
      h.put("Σύνολο", sum);
      Enumeration en = hold.keys();
      while (en.hasMoreElements()) {
	Object k = en.nextElement();
	if (!k.equals("$Σύνολο")) {
	  d = M.mul(ka, M.div((Number) hold.get(k), 100));
	  double diff = d.doubleValue();
	  sum = M.sub(sum, d = M.round(d, 2));
	  h.put(k, d);
	  diff = 1000 * (diff - d.doubleValue()) + Math.random() / 100;
	  tm.put(new Double(diff), k);
	}
      }
      int z = (int) (100 * M.round(sum, 4).doubleValue());
      if (z > 0) {
	for (; z > 0; z--) {
	  Object last = tm.lastKey();
          Object k = tm.get(last);
          h.put(k, M.round(M.add((Number) h.get(k), 0.01), 2));
	  tm.remove(last);
	}
      } else {
	for (z =- z; z > 0; z--) {
	  Object first = tm.firstKey();
          Object k = tm.get(first);
          h.put(k, M.round(M.sub((Number) h.get(k), 0.01), 2));
	  tm.remove(first);
	}
      }
      o = h;

    } else if (key.equals("Καταλογιστέο"))
      o = M.round(M.add((Number) ((Hashtable) get("ΣΠ/ΚΨΜ".equals(super.get("Τύπος")) ?
                                                  "ΑνάλυσηΚρατήσεωνΣεΕυρώ" : "ΚατηγορίεςΦΠΑ")).get("Σύνολο"),
                        (Number) get("ΚαθαρήΑξία")), 2);
    else if (key.equals("Πληρωτέο"))
      o = M.round(M.sub((Number) get("Καταλογιστέο"), (Number)
                        ((Hashtable) get("ΑνάλυσηΚρατήσεωνΣεΕυρώ")).get("Σύνολο")), 2);

    else if (key.equals("ΦΕΣεΕυρώ")) {
      setFe();
      o = M.round(M.mul((Number) get("ΚαθαρήΑξίαΜείονΚρατήσεις"), ((Number)
          get("ΠοσοστόΦΕ")).doubleValue() / 100), 2);

    } else if (key.equals("ΚαθαρήΑξίαΜείονΚρατήσεις"))
      o = M.round(M.sub((Number) get("ΚαθαρήΑξία"), (Number)
                        ((Hashtable) get("ΑνάλυσηΚρατήσεωνΣεΕυρώ")).get("Σύνολο")), 2);

    else if (key.equals("ΥπόλοιποΠληρωτέο"))
      o = M.round(M.sub((Number) get("Πληρωτέο"), (Number) get("ΦΕΣεΕυρώ")), 2);

    if (o != null) super.put("$" + key, o);
    return o;
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

  protected static boolean isValidBill(String bill) {
    return bill.matches("\\d+/\\d+-\\d+-\\d+");
  }
}