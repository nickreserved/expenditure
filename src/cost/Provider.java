package cost;

import java.util.*;

public class Provider implements RowTable, FileLineData, Hashing {

  protected static final String[] header = { "Επωνυμία", "Όνομα Πατέρα ή Συζύγου", "ΑΦΜ", "ΔΟΥ",
      "Τηλέφωνο", "Τ.Κ.", "Πόλη", "Διεύθυνση"};
  protected static final String[] hashKeys = { "name", "father", "afm", "doy", "phone", "tk",
      "city", "address" };

  protected String[] data = new String[header.length];
  public static final char NAME = 0;
  public static final char FATHER = 1;
  public static final char AFM = 2;
  public static final char DOY = 3;
  public static final char PHONE = 4;
  public static final char TK = 5;
  public static final char CITY = 6;
  public static final char ADDRESS = 7;

  public Provider() {}
  public Provider(String s) throws Exception { load(s); }

  public boolean isValid() {
    return
        data[NAME] != null && data[NAME].length() != 0 &&
	data[AFM] != null && data[AFM].length() != 0 &&
	data[DOY] != null && data[DOY].length() != 0;
  }

  public String toString() { return data[NAME]; }

  public Object clone() {
    Provider p = new Provider();
    p.data = (String[]) data.clone();
    return p;
  }


  // ---------------------------- RowTable --------------------------------------------- //

  public Object getCell(int col) { return data[col]; }
  public void setCell(Object o, int col) { data[col] = o.toString(); }

  public boolean isEmpty() {
    for (int z = 0; z < data.length; z++)
      if (data[z] != null && data[z].length() != 0) return false;
    return true;
  }


  // ---------------------------- RowTable --------------------------------------------- //

  public String load(String s) throws Exception {
    String[] d = s.split("\t", header.length + 1);
    if (d.length < header.length)
      throw new Exception();
    System.arraycopy(d, 0, data, 0, header.length);
    return d.length == header.length + 1 ? d[header.length] : null;
  }

  public String save() {
    String s = "";
    for (int z = 0; z < data.length; z++) {
      if (z != 0) s += "\t";
      if (data[z] != null) s+= data[z];
    }
    return s;
  }


  // ---------------------------- Hashing ------------------------------------------ //

  public Object hash(String s) throws Exception {
    if (s.startsWith("provider_")) s = s.substring(9);
    for (int z = 0; z < hashKeys.length; z++)
      if (s.equals(hashKeys[z])) return data[z];
    throw new Exception("Η κλάση <b>Provider</b> δεν υποστηρίζει τη φράση <b>" + s + "</b>");
  }
}