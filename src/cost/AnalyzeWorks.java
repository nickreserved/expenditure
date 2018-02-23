package cost;

import java.util.*;

public class AnalyzeWorks {
  /*
  static public final String[] hashKeys = { "all_materials", "by_material", "with_material" };

  static public final int WORKS_ALL_MATERIALS = 0;
  static public final int WORKS_BY_MATERIAL = 1;
  static public final int WORKS_WITH_MATERIAL = 2;

  static public Object getSumFromWorks(Vector v, int val) {
    Object o = null;
    Hashtable2 h = new Hashtable2();
    Vector vc = new Vector();

    for (int z = 0; z < v.size(); z++) {
      Work w = (Work) v.elementAt(z);

      switch (val) {

	case WORKS_WITH_MATERIAL:
	  o = vc;
	  if (w.materials.size() > 0) vc.add(w);
	  break;

	case WORKS_ALL_MATERIALS:
	  o = vc;
	  for (int y = 0; y < w.materials.size(); y++) {
	    Material m = (Material) w.materials.elementAt(y);
	    for (int x = 0; x < vc.size(); x++) {
	      Material vm = (Material) vc.elementAt(x);
	      if (m.toString().equals(vm.toString())) {
		((Digit) vm.getCell(Material.MANY)).add((Digit) m.getCell(Material.MANY));
		m = null;
		break;
	      }
	    }
	    if (m != null) vc.add(m.clone());
	  }
	  break;

	case WORKS_BY_MATERIAL:
	  o = h;
	  break;

	default:
	  return null;
      }
    }
    return o;
  }


  static public Object hash(Vector v, String s) throws Exception {
/*    try {
      if (s.startsWith("works_")) s = s.substring(6);
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
      throw Functions.getException(e, "Η κλάση <b>Works</b> δεν υποστηρίζει τη φράση <b>" + s + "</b>");
    }*/
//return null;
//  }
}
