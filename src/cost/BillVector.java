package cost;

import java.util.*;
import common.*;

public class BillVector extends VectorObject implements Hash {
  private Hashtable ho = new Hashtable();

  public void removeTemporary() { ho.clear(); }

  public Object get(Object key) {
    Object value = ho.get(key);
    if (value != null) return value;

    if (key.equals("ΚατάΠρομηθευτή")) {
      Hashtable h = new Hashtable();
      for (int z = 0; z < size(); z++) {
	Bill b = (Bill) get(z);
	Object k = b.get("Προμηθευτής");
	BillVector bv = (BillVector) h.get(k);
	if (bv == null) h.put(k, bv = new BillVector());
	bv.add(b);
      }
      value = h;

    } else if (key.equals("ΚατάΜήνα")) {
      Hashtable h = new Hashtable();
      for (int z = 0; z < size(); z++) {
	Bill b = (Bill) get(z);
	String[] s = b.get("Τιμολόγιο").toString().split("-");
	Hashtable k = new HashObject();
	k.put("year", new Short(s[2]));
	k.put("month", Functions.months[Integer.parseInt(s[1]) - 1]);
	BillVector bv = (BillVector) h.get(k);
	if (bv == null) h.put(k, bv = new BillVector());
	bv.add(b);
      }
      value = h;

    } else if (key.equals("ΚατηγορίεςΦΠΑ")) {
      Hashtable h = new Hashtable();
      for (int z = 0; z < size(); z++) {
	Bill b = (Bill) get(z);
	Hashtable fpa = (Hashtable) b.get("ΚατηγορίεςΦΠΑ");
	Enumeration en = fpa.keys();
	while (en.hasMoreElements()) {
	  Object k = en.nextElement();
	  Number d = (Number) fpa.get(k);
	  if (h.containsKey(k)) h.put(k, M.round(M.add(d, (Number) h.get(k)), 2));
	  else h.put(k, d);
	}
      }
      value = h;

    } else if (key.equals("ΑνάλυσηΚρατήσεωνΣεΕυρώ")) {
      Hashtable h = new Hashtable();
      for (int z = 0; z < size(); z++) {
	Bill b = (Bill) get(z);
	Hashtable hold = (Hashtable) b.get("ΑνάλυσηΚρατήσεωνΣεΕυρώ");
	Enumeration en = hold.keys();
	while (en.hasMoreElements()) {
	  Object k = en.nextElement();
	  Number d = (Number) hold.get(k);
          if (h.containsKey(k)) h.put(k, M.round(M.add(d, (Number) h.get(k)), 2));
          else h.put(k, d);
	}
      }
      value = h;

    } else if (key.equals("ΚατάΚρατήσεις")) {
      Hashtable h = new Hashtable();
      for (int z = 0; z < size(); z++) {
	Bill b = (Bill) get(z);
	Object k = b.get("ΑνάλυσηΚρατήσεωνΣεΠοσοστά");
	BillVector bv = (BillVector) h.get(k);
	if (bv == null) h.put(k, bv = new BillVector());
	bv.add(b);
      }
      value = h;

    } else if (key.equals("ΚατάΦΕ")) {
      Hashtable h = new Hashtable();
      for (int z = 0; z < size(); z++) {
	Bill b = (Bill) get(z);
	Object k = b.get("ΠοσοστόΦΕ");
	BillVector bv = (BillVector) h.get(k);
	if (bv == null) h.put(k, bv = new BillVector());
	bv.add(b);
      }
      value = h;

    } else if (key.equals("ΜεΦΕ")) {
      BillVector bv = new BillVector();
      for (int z = 0; z < size(); z++) {
	Bill b = (Bill) get(z);
	if (((Number) b.get("ΦΕΣεΕυρώ")).doubleValue() != 0) bv.add(b);
      }
      value = bv;

    } else if (key.equals("ΑγοράςΥλικών")) {
      BillVector bv = new BillVector();
      for (int z = 0; z < size(); z++) {
	Bill b = (Bill) get(z);
	if (!b.get("Κατηγορία").equals("Παροχή υπηρεσιών")) bv.add(b);
      }
      value = bv;

    } else if (key.equals("ΠαροχήςΥπηρεσιών")) {
      BillVector bv = new BillVector();
      for (int z = 0; z < size(); z++) {
	Bill b = (Bill) get(z);
	if (b.get("Κατηγορία").equals("Παροχή υπηρεσιών")) bv.add(b);
      }
      value = bv;

    } else if (key.equals("ΣΠ/ΚΨΜ")) {
      BillVector bv = new BillVector();
      for (int z = 0; z < size(); z++) {
	Bill b = (Bill) get(z);
	if (b.get("Τύπος").equals(key)) bv.add(b);
      }
      value = bv;

    } else if (key.equals("ΚαθαρήΑξία") || key.equals("ΚαθαρήΑξίαΜείονΚρατήσεις") ||
	       key.equals("Καταλογιστέο") || key.equals("Πληρωτέο") ||
	       key.equals("ΥπόλοιποΠληρωτέο") || key.equals("ΦΕΣεΕυρώ")) {
      Number d = new Double(0);
      for (int z = 0; z < size(); z++)
	d = M.round(M.add(d, (Number) ((Bill) get(z)).get(key)), 2);
      value = d;

    }
    if (value != null) ho.put(key, value);
    return value;
  }
}