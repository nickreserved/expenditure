package cost;

import java.util.*;

public class Bill implements RowTable, FileLineData, Hashing {
  protected static final String[] header = {
      "Τιμολόγιο", "Τύπος", "Κατηγορία", "Προμηθευτής", "Κρατήσεις", "ΦΕ"};
  protected static final String[] hashKeys = {
      "name", "type", "style", "provider", "hold_percent", "fe_percent", "items", "clear_cost",
      "fpa_euro", "fpa_list", "hold_euro_total", "hold_euro", "katalogisteo", "plhrwteo", "fe_euro",
      "ypoloipo_plhrwteo", "clear_fe_cost" };
  protected static final Byte[] feList = { new Byte((byte) 4), new Byte((byte) 8),
      new Byte((byte) 1), new Byte((byte) 0), new Byte((byte) 3) };
  protected static final String[] types = {
      "Τιμολόγιο", "Διατακτική", "Απόδειξη Δημόσιας Υπηρεσίας" };
  protected static final String[] categories = {
      "Προμήθεια αγαθών", "Παροχή υπηρεσιών", "Αγορά υγρών καυσίμων" };

  protected String id;
  protected int type;
  protected Provider provider;
  protected Hold hold;
  protected int style = 0;
  protected Byte fe = feList[0];
  protected Vector items = new Vector();

  public static final int ID = 0;
  public static final int TYPE = 1;
  public static final int CATEGORY = 2;
  public static final int PROVIDER = 3;
  public static final int HOLDS = 4;
  public static final int FE = 5;
  public static final int ITEMS = 6;

  public static final int CLEAR_COST = 7;
  public static final int FPA_TOTAL = 8;
  public static final int FPA_LIST = 9;
  public static final int HOLD_TOTAL = 10;
  public static final int HOLD_LIST = 11;
  public static final int KATALOGISTEO = 12;
  public static final int PLHRWTEO = 13;
  public static final int FE_TOTAL = 14;
  public static final int YPOLOIPO_PLHRWTEO = 15;
  public static final int FE_CLEAR_COST = 16;

  public static final int BILL = 0;
  public static final int WARRANT = 1;
  public static final int PUBLIC_SERVICE = 2;

  public static final int PROMH8EIA = 0;
  public static final int PAROXH = 1;
  public static final int KAYSIMA = 2;

  public Bill() {}
  public Bill(String s) throws Exception { load(s); }

  public String toString() { return id; }

  protected static boolean isValidBill(String bill) {
    return bill.matches("\\d+/\\d+-\\d+-\\d+");
  }


  // ---------------------------- RowTable --------------------------------------------- //

  public boolean isEmpty() {
    if (id == null || id.length() == 0) return true;
    else return false;
  }

  public Object getCell(int col) {
    switch (col) {
      case ID: return id;
      case TYPE: return types[type];
      case CATEGORY: return categories[style];
      case PROVIDER: return provider;
      case HOLDS: return hold;
      case FE: return fe;
      case ITEMS: return items;

      case CLEAR_COST:
        Digit a = new Digit(0, 2, false, true);
        for (int z = 0; z < items.size(); z++)
          a.add((Digit) ((BillItem) items.elementAt(z)).getCell(BillItem.TOTAL_COST));
        return a;

      case FPA_LIST:
        Hashtable h = new Hashtable();
        if (type == WARRANT) setFpa();
        else
          for (int z = 0; z < items.size(); z++) {
            BillItem o = (BillItem) items.elementAt(z);
            Byte fpa = (Byte) o.getCell(BillItem.FPA);
            Object obj = h.get(fpa);
            double va = ((Digit) o.getCell(BillItem.TOTAL_COST)).doubleValue();
            if (fpa.byteValue() != 0) {
              if (obj == null) h.put(fpa, new Digit(va, 2, false, true));
              else ((Digit) obj).add(va);
            }
          }
        Enumeration en = h.keys();
        while (en.hasMoreElements()) {
          Byte fpa = (Byte) en.nextElement();
          ((Digit) h.get(fpa)).mul(fpa.doubleValue() / 100);
        }
        return h;

      case FPA_TOTAL:
        h = (Hashtable) getCell(FPA_LIST);
        en = h.keys();
        Digit d = new Digit(0, 2, false, true);
        while (en.hasMoreElements())
          d.add((Digit) h.get(en.nextElement()));
        return d;

      case HOLD_LIST:
        if (hold == null) return null;
        return hold.getHoldForCost(((Digit) getCell(CLEAR_COST)).doubleValue());
      case HOLD_TOTAL:
        Hold hld = (Hold) getCell(HOLD_LIST);
        if (hld == null) return new Digit(0, 2, false, true);
        d = hld.getTotalHold();
        d.round(2);
        return d;
      case KATALOGISTEO:
        double clear = ((Digit) getCell(CLEAR_COST)).doubleValue();
        double diff;
        if (type == WARRANT) diff = ((Digit) getCell(HOLD_TOTAL)).doubleValue();
        else diff = ((Digit) getCell(FPA_TOTAL)).doubleValue();
        return new Digit(clear + diff, 2, false, true);
      case PLHRWTEO:
        if (type == WARRANT) return getCell(CLEAR_COST);
        return new Digit(
            ((Digit) getCell(KATALOGISTEO)).doubleValue() -
            ((Digit) getCell(HOLD_TOTAL)).doubleValue(), 2, false, true);
      case FE_CLEAR_COST:
        return new Digit(((Digit) getCell(CLEAR_COST)).doubleValue() -
                         ((Digit) getCell(HOLD_TOTAL)).doubleValue(), 2, false, true);
      case FE_TOTAL:
        setFe();
        return new Digit(((Digit) getCell(FE_CLEAR_COST)).doubleValue() *
                         fe.intValue() / 100, 2, false, true);
      case YPOLOIPO_PLHRWTEO:
        if (type == WARRANT) return getCell(CLEAR_COST);
        return new Digit(
            ((Digit) getCell(PLHRWTEO)).doubleValue() -
            ((Digit) getCell(FE_TOTAL)).doubleValue(), 2, false, true);

      default: return null;
    }
  }
  public void setCell(Object o, int col) {
    switch (col) {
      case ID:
	if (isValidBill(o.toString()) || o.toString().length() == 0) id = o.toString();
	break;
      case TYPE:
	type = StaticFunctions.findInArray(types, o);
	setFe();
        setFpa();
	break;
      case CATEGORY:
        style = StaticFunctions.findInArray(categories, o);
        setFe();
        break;
      case PROVIDER:
	provider = (Provider) o;
	break;
      case HOLDS:
	hold = (Hold) o;
	break;
      case FE:
	if (o instanceof Byte) fe = (Byte) o;
    }
  }

  protected void setFe() {
    if (type != BILL) fe = new Byte((byte) 0);
    else if (fe.byteValue() != 0)
      if (style == PAROXH) fe = new Byte((byte) 8);
      else if (style == PROMH8EIA) fe = new Byte((byte) 4);
      else fe = new Byte((byte) 1);
  }

  protected void setFpa() {
    for (int z = 0; z < items.size(); z++) {
      BillItem bi = (BillItem) items.elementAt(z);
      if (type == WARRANT)
        bi.setCell(new Byte((byte) 0), BillItem.FPA);
      else if (((Byte) bi.getCell(BillItem.FPA)).byteValue() == 0)
        bi.setCell(new Byte((byte) 18), BillItem.FPA);
    }
  }


  // ---------------------------- FileLineData --------------------------------------------- //

  public boolean isValid() {
    if (!isEmpty() && provider != null && hold != null && items.size() != 0)
      return true;
    else return false;
  }

  public String load(String s) throws Exception {
    String[] d = s.split("\t", 5);
    if (d.length < 5 || !isValidBill(id = d[0]))
      throw new Exception();
    setCell(d[1], TYPE);
    setCell(d[2], CATEGORY);
    fe = new Byte(d[3]);
    provider = new Provider();
    s = provider.load(d[4]);
    hold = new Hold();
    s = hold.load(s);
    while (s != null) {
      BillItem i = new BillItem();
      s = i.load(s);
      items.add(i);
    }
    return null;
  }

  public String save() {
    String s = getCell(ID) + "\t" + getCell(TYPE) + "\t" + getCell(CATEGORY) +
        "\t" + getCell(FE) + "\t" + provider.save() + "\t" + hold.save();
    for (int z = 0; z < items.size(); z++)
      s += "\t" + ((BillItem) items.elementAt(z)).save();
    return s;
  }

  // ---------------------------- Hashing --------------------------------------------- //

  public Object hash(String s) throws Exception {
    if (s.startsWith("bill_")) s = s.substring(5);
    for (int z = 0; z <= FE_CLEAR_COST; z++)
      if ((z == HOLDS || z == HOLD_LIST || z == PROVIDER) && s.startsWith(hashKeys[z]))
        return ((Hashing) getCell(z)).hash(s.substring(1 + hashKeys[z].length()));
      else if (s.equals(hashKeys[z]))
        return getCell(z);
    throw new Exception("Η κλάση <b>Bill</b> δεν υποστηρίζει τη φράση <b>" + s + "</b>");
  }
}