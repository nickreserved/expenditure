package cost;

public class BillItem implements RowTable, FileLineData, Hashing {
  protected static final String[] header = { "Είδος", "Ποσότητα", "Τιμή μονάδας", "Συνολική τιμή",
      "ΦΠΑ", "Τιμή μονάδας με ΦΠΑ", "Συνολική τιμή με ΦΠΑ" ,"Μονάδα μέτρησης"};
  public static final String[] measures = {
      "τεμάχια", "lt", "Kgr", "cm", "cm^2", "cm^3", "m", "m^2", "m^3", "ρολά", "πόδια", "λίβρες", "ζεύγη", "στρέμματα", "Km", "Km^2" };
  protected static final Byte[] fpaList = { new Byte((byte) 19), new Byte((byte) 9), new Byte((byte) 0) };
  protected static final String[] hashKeys = { "name", "many", "cost", "total_cost", "fpa",
      "cost_with_fpa", "total_cost_with_fpa", "measure" };

  protected String name;
  protected int measure = 0;
  protected Digit many = new Digit(1, 6, true, true);
  protected Digit cost = new Digit(0, 4, true, true);
  protected Byte fpa = fpaList[0];

  public static final int NAME = 0;
  public static final int MANY = 1;
  public static final int COST = 2;
  public static final int TOTAL_COST = 3;
  public static final int FPA = 4;
  public static final int COST_WITH_FPA = 5;
  public static final int TOTAL_COST_WITH_FPA = 6;
  public static final int MEASURE = 7;

  public BillItem() {}
  public BillItem(String s) throws Exception { load(s); }


  // ---------------------------- RowTable --------------------------------------------- //

  public boolean isEmpty() {
    return (name == null || name.length() == 0) && many.doubleValue() * cost.doubleValue() == 0;
  }

  public Object getCell(int col) {
    switch (col) {
      case NAME: return name;
      case MANY: return many;
      case COST: return cost;
      case TOTAL_COST:
        Digit d = Digit.mul(cost, many);
        d.round(2);
        return d;
      case FPA: return fpa;
      case COST_WITH_FPA: return Digit.mul(cost, (100 + fpa.intValue()) / (double) 100);
      case TOTAL_COST_WITH_FPA: return Digit.mul( (Digit) getCell(TOTAL_COST), (100 + fpa.intValue()) / (double) 100);
      case MEASURE: return measures[measure];
      default: return null;
    }
  }

  public void setCell(Object o, int col) {
    try {
      switch (col) {
        case NAME:
          name = o.toString();
          break;
        case MANY:
          many.setDigit(o.toString());
          break;
        case COST:
          cost.setDigit(o.toString());
          break;
        case TOTAL_COST:
          if (many.doubleValue() == 0) return;
          cost.setDigit(Digit.parseDigit(o.toString()) / many.doubleValue());
          break;
        case FPA:
          fpa = (Byte) o;
          break;
        case COST_WITH_FPA:
          cost.setDigit(Digit.parseDigit(o.toString()) / (100 + fpa.intValue()) * 100);
          break;
        case TOTAL_COST_WITH_FPA:
          cost.setDigit(Digit.parseDigit(o.toString()) / many.doubleValue()
                        / (100 + fpa.intValue()) * 100);
          break;
        case MEASURE:
          measure = StaticFunctions.findInArray(measures, o);
      }
    } catch (Exception e) {}
  }


  // ---------------------------- FileLineData --------------------------------------------- //

  public boolean isValid() {
    return name == null || name.length() * many.doubleValue() * cost.doubleValue() == 0;
  }

  public String load(String s) throws Exception {
    String[] d = s.split("\t", 6);
    if (d.length < 5) throw new Exception();
    name = d[0];
    setCell(d[1], MEASURE);
    setCell(d[2], MANY);
    setCell(d[3], COST);
    setCell(new Byte(d[4]), FPA);
    return d.length == 6 ? d[5] : null;
  }

  public String save() {
    return name + "\t" + getCell(MEASURE) + "\t" + getCell(MANY) + "\t" + getCell(COST)
	 + "\t" + getCell(FPA);
  }

  // ---------------------------- Hashing --------------------------------------------- //

  public Object hash(String s) throws Exception {
    if (s.startsWith("item_")) s = s.substring(5);
    for (int z = 0; z < hashKeys.length; z++)
      if (s.equals(hashKeys[z]))
        return getCell(z);
    throw new Exception("Η κλάση <b>BillItem</b> δεν υποστηρίζει τη φράση <b>" + s + "</b>");
  }
}