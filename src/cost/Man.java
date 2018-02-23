package cost;

public class Man implements RowTable, FileLineData, Hashing {
  protected static final String[] header = { "Βαθμός", "Όνοματεπώνυμο" };
  protected static final String[] hashKeys = { "rank", "name" };

  protected String[] data = new String[header.length];
  public static final char RANK = 0;
  public static final char NAME = 1;

  public Man() {}
  public Man(String s) throws Exception { load(s); }

  public boolean isValid() {
    return
        data[NAME] != null && data[NAME].length() != 0 &&
	data[RANK] != null && data[RANK].length() != 0;
  }

  public String toString() { return data[RANK] + " " + data[NAME]; }

  public Object clone() {
    if (!isValid()) return null;
    Man m = new Man();
    m.data = (String[]) data.clone();
    return m;
  }

  // ---------------------------- RowTable --------------------------------------------- //

  public Object getCell(int col) { return data[col]; }
  public void setCell(Object o, int col) { data[col] = o.toString(); }

  public boolean isEmpty() {
    for (int z = 0; z < data.length; z++)
      if (data[z] != null && data[z].length() != 0) return false;
    return true;
  }


  // ---------------------------- FileLineData ------------------------------------------ //

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
    for (int z = 0; z < data.length; z++)
      if (s.equals(hashKeys[z])) return data[z];
    throw new Exception("Η κλάση <b>Man</b> δεν υποστηρίζει τη φράση <b>" + s + "</b>");
  }
}