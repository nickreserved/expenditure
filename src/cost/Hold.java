package cost;

import java.util.*;

public class Hold implements RowTable, FileLineData, Hashing {

  static protected final String[] header = { "ΜΤΣ", "ΤΑΣ", "ΕΜΠ", "ΤΣΜΕΔΕ", "ΑΟΟΑ", "ΥΠΚ", "ΤΠΕΔΕ",
      "ΕΚΟΕΜΣ", "Χαρτόσημο", "ΟΓΑ Χαρτοσήμου", "Σύνολο Κρατήσεων" };
  protected static final String[] hashKeys = { "mts", "tas", "emp", "tsmede", "aooa", "ypk",
      "tpede", "ekoems", "stamp", "oga", "total" };

  protected Digit[] data = new Digit[header.length - 1];
  public static final int MTS = 0;
  public static final int TAS = 1;
  public static final int EMP = 2;
  public static final int TSMEDE = 3;
  public static final int AOOA = 4;
  public static final int YPK = 5;
  public static final int TPEDE = 6;
  public static final int EKOEMS = 7;
  public static final int STAMP = 8;
  public static final int OGA = 9;
  public static final int TOTAL = 10;

  public Hold() {}
  public Hold(String s) throws Exception { load(s); }

  public String toString() { return getCell(TOTAL).toString(); }

  public Object clone() {
    Hold p = new Hold();
    p.data = (Digit[]) data.clone();
    return p;
  }

  public Digit getTotalHold() {
    Digit a = new Digit(0, 3, false, true);
    for (int z = 0; z < data.length; z++)
      a.add((Digit) getCell(z));
    return a;
  }

  public Hold getHoldForCost(double cost) {
    Hold h = new Hold();
    Digit total = new Digit(getTotalHold().doubleValue() * cost / 100, 2, false, true);
    for (int z = 0; z <= OGA; z++) {
      Digit t = new Digit(((Digit) getCell(z)).doubleValue() * cost / 100, 2, false, true);
      if (z < data.length) h.data[z] = t;
      total.sub(t);
    }
    h.data[MTS].add(total);
    return h;
  }

  public void addHold(Hold a) {
    for (int z = 0; z < data.length; z++)
      if (data[z] == null) data[z] = (Digit) a.data[z].clone();
      else data[z].add(a.data[z]);
  }

  // ---------------------------- RowTable --------------------------------------------- //

  public Object getCell(int col) {
    if (col == TOTAL) return getTotalHold();
    else return data[col] == null ? new Digit(0, 3, false, true) : data[col];
  }
  public void setCell(Object o, int col) {
    data[col] = new Digit(o.toString(), 3, false, true);
  }
  public boolean isEmpty() { return getTotalHold().doubleValue() == 0; }
  public boolean isValid() { return true; }


  // ---------------------------- RowTable --------------------------------------------- //

  public String load(String s) throws Exception {
    String[] d = s.split("\t", data.length + 1);
    if (d.length < data.length) throw new Exception();
    for(int z = 0; z < data.length; z++)
      setCell(d[z], z);
    return d.length == data.length + 1 ? d[data.length] : null;
  }

  public String save() {
    String s = "";
    for (int z = 0; z < data.length; z++) {
      if (z != 0) s += "\t";
      s += data[z];
    }
    return s;
  }


  // ---------------------------- Hashing ------------------------------------------ //

  public Object hash(String s) throws Exception {
    if (s.startsWith("hold_")) s = s.substring(5);
    for (int z = 0; z < header.length; z++)
      if (s.equals(hashKeys[z])) return getCell(z);
    throw new Exception("Η κλάση <b>Hold</b> δεν υποστηρίζει τη φράση <b>" + s + "</b>");
  }
}