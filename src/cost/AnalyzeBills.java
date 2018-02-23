package cost;

import java.util.*;


public class AnalyzeBills {
  static public final String[] hashKeys = { "by_provider", "by_month", "by_fe", "with_fe",
      "warrants", "by_hold", "agoras", "paroxhs" };

  static public final int BILLS_BY_PROVIDER = 1 + Bill.FE_CLEAR_COST;
  static public final int BILLS_BY_MONTH = 2 + Bill.FE_CLEAR_COST;
  static public final int BILLS_BY_FE = 3 + Bill.FE_CLEAR_COST;
  static public final int BILLS_WITH_FE = 4 + Bill.FE_CLEAR_COST;
  static public final int WARRANTS = 5 + Bill.FE_CLEAR_COST;
  static public final int BILLS_BY_HOLD = 6 + Bill.FE_CLEAR_COST;
  static public final int BILLS_AGORAS = 7 + Bill.FE_CLEAR_COST;
  static public final int BILLS_PAROXHS = 8 + Bill.FE_CLEAR_COST;

  static public Object getSumFromBills(Vector v, int val) {
    Object o = null;
    Digit d = new Digit(0, 2, false, true);
    Hold h = new Hold();
    Hashtable2 ht = new Hashtable2();
    Vector vc = new Vector();

    for (int z = 0; z < v.size(); z++) {
      Bill b = (Bill) v.elementAt(z);

      switch (val) {

	case BILLS_WITH_FE:
	  o = vc;
	  if (((Digit) b.getCell(Bill.FE_TOTAL)).doubleValue() != 0)
	    vc.add(b);
	  break;

	case BILLS_AGORAS:
	  o = vc;
	  if (b.style != Bill.PAROXH) vc.add(b);
	  break;

	case BILLS_PAROXHS:
	  o = vc;
	  if (b.style == Bill.PAROXH) vc.add(b);
	  break;

	case WARRANTS:
	  o = vc;
	  if (b.type == Bill.WARRANT) vc.add(b);
	  break;

	case Bill.FPA_LIST:
	  o = ht;
	  Hashtable fpa = (Hashtable) b.getCell(Bill.FPA_LIST);
	  Enumeration en = fpa.keys();
	  while (en.hasMoreElements()) {
	    Byte key = (Byte) en.nextElement();
	    d = (Digit) fpa.get(key);
	    if (ht.containsKey(key)) ((Digit) ht.get(key)).add(d);
	    else ht.put(key, d);
	  }
	  break;

	case Bill.HOLD_LIST:
	  o = h;
	  Hold hold = (Hold) b.getCell(Bill.HOLD_LIST);
	  h.addHold(hold);
	  break;

	case BILLS_BY_HOLD:
	  o = ht;
	  Vector l = (Vector) ht.get(b.getCell(Bill.HOLDS));
	  if (l == null) ht.put(b.getCell(Bill.HOLDS), l = new Vector());
	  l.add(b);
	  break;

	case BILLS_BY_PROVIDER:
	  o = ht;
	  l = (Vector) ht.get(b.getCell(Bill.PROVIDER));
	  if (l == null) ht.put(b.getCell(Bill.PROVIDER), l = new Vector());
	  l.add(b);
	  break;

	case BILLS_BY_FE:
	  o = ht;
	  l = (Vector) ht.get(b.getCell(Bill.FE));
	  if (l == null) ht.put(b.getCell(Bill.FE), l = new Vector());
	  l.add(b);
	  break;

	case BILLS_BY_MONTH:
	  o = ht;
	  String[] s = b.getCell(Bill.ID).toString().split("-");
	  Hashtable key = new Hashtable2();
	  key.put("year", new Short(s[2]));
	  key.put("month", StaticFunctions.months[Integer.parseInt(s[1]) - 1]);
	  l = (Vector) ht.get(key);
	  if (l == null) ht.put(key, l = new Vector());
	  l.add(b);
	  break;

	case Bill.CLEAR_COST:
	case Bill.FE_CLEAR_COST:
	case Bill.FPA_TOTAL:
	case Bill.HOLD_TOTAL:
	case Bill.KATALOGISTEO:
	case Bill.PLHRWTEO:
	case Bill.FE_TOTAL:
	case Bill.YPOLOIPO_PLHRWTEO:
	  o = d;
	  d.add((Digit) b.getCell(val));
	  break;

	default:
	  return null;
      }
    }
    return o;
  }


  static public Object hash(Vector v, String s) throws Exception {
    try {
      if (s.startsWith("bills_")) s = s.substring(6);
      for (int z = Bill.CLEAR_COST; z < hashKeys.length + BILLS_BY_PROVIDER; z++) {
	String a;
	if (z < BILLS_BY_PROVIDER) a = Bill.hashKeys[z];
	else a = hashKeys[z - BILLS_BY_PROVIDER];

	if (s.equals(a)) return getSumFromBills(v, z);
	else if (s.startsWith(a + "_")) {
	  HashTable t = new HashTable(null, null);
	  return t.getParseObject(s.substring(a.length() + 1), getSumFromBills(v, z));
	}
      }
      throw null;
    } catch (Exception e) {
      throw StaticFunctions.getException(e, "Η κλάση <b>Bills</b> δεν υποστηρίζει τη φράση <b>" + s + "</b>");
    }
  }
}