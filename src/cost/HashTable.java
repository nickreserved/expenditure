package cost;

import java.util.*;
import java.text.*;

public class HashTable implements TemplateTagCompiler {
  MainFrame m;
  Vector keywords = new Vector();
  Vector parameters = new Vector();

  public HashTable(MainFrame m, String param) {
    this.m = m;
    if (param == null) return;
    String[] d = param.split(" ");
    for (int z = 0; z < d.length; z++)
      parameters.add(d[z]);
  }

  public String openFile(String file) throws Exception {
    return TemplateParser.parse(file + ".template", this);
  }

  public Object compileTag(TemplateParser.Keyword kwd) throws Exception {
    boolean iftrue = ifTrue();

    if (kwd.pos == -1) {
      if (iftrue) return kwd.keyword; else return "";
    }

    Object s = null;
    Keyword k = new Keyword(kwd);
    if (k.keyword == "include") return openFile(k.obj1.toString());
    else if (k.keyword == "end_enum") return endEnum();
    else if (k.keyword == "else") elseIf(k);
    else if (k.keyword == "endif" || k.keyword == "end_counter") endIf(k);
    else if (kwd.keyword == "end_repeat") s = endRepeat(k);
    else if (k.keyword == "repeat" || k.keyword == "start_counter") keywords.add(k);
    else if (k.keyword == "if") startIf(k);
    else if (k.keyword == "counter") s = getCounterValue(k);
    else if (k.keyword == "enum") startEnum(k);
    else if (iftrue) {
      if (k.keyword == "set") parameter(k.obj1, true);
      else if (k.keyword == "reset") parameter(k.obj1, false);
      else if (k.keyword == "capital") s = capital(k);
      else if (k.keyword == "euro") s = euro(k);
      else if (k.keyword == "percent") s = percent(k);
      else if (k.keyword == "full_written") s = fullWritten(k);
      else if (k.keyword == "static") s = m.staticData.hash(k.obj1.toString());
      else if (k.keyword == "cost") s = m.cost.hash(k.obj1.toString());
      else if (k.keyword == "system") s = systemCall(k.obj1.toString());
      else s = getParseObject(k.keyword, null);
    }
    if (!iftrue || s == null) return ""; else return s;
  }

  protected Keyword validateCloser(String s) throws Exception {
    try {
      Keyword k = (Keyword) keywords.lastElement();
      if (!k.getCLoseKeyword(s)) throw new Exception("Το <b>" + k.keyword +
            "</b> δεν κλείνει με <b>" + s + "</b>.");
      return k;
    } catch (Exception e) {
      throw new Exception("Βρέθηκε <b>" + s + "</b> χωρίς λόγο ύπαρξης.");
    }
  }


  // ------------------------- parameter operations ---------------------------- //

  protected void parameter(Object o, boolean oper) {
    if (oper) {
      if (!parameters.contains(o)) parameters.add(o);
    } else {
      if (parameters.contains(o)) parameters.remove(o);
    }
  }


  // -------------------------------- if operations ---------------------------- //

  protected boolean ifTrue() {
    for (int z = 0; z < keywords.size(); z++) {
      Keyword k = (Keyword) keywords.elementAt(z);
      if (k.keyword == "if" && k.obj1.equals(Boolean.FALSE) ||
          k.keyword == "enum" && k.obj2 == null)
        return false;
    }
    return true;
  }

  protected void startIf(Keyword k) throws Exception {
    try {
      k.obj1 = new Boolean(parseIf(k.obj1.toString()));
      keywords.add(k);
    } catch (Exception e) {
      throw StaticFunctions.getException(e, "Το <b>if</b> δεν υποστηρίζει τη φράση <b>" + k.obj1 + "</b>");
    }
  }

  protected void elseIf(Keyword k) throws Exception {
    k = validateCloser(k.keyword);
    if (k.obj1.equals(Boolean.FALSE))
      k.obj1 = Boolean.TRUE;
    else k.obj1 = Boolean.FALSE;
  }

  protected void endIf(Keyword k) throws Exception {
    keywords.remove(validateCloser(k.keyword));
  }

  protected boolean parseIf(String s) throws Exception {
    if (s.startsWith("not_")) return parseIf(s.substring(4)) ? false : true;
    else if (s.startsWith("parameter_")) return parameters.contains(s.substring(10));

    else if (s.startsWith("null_"))
      try {
        return compileTag(new TemplateParser.Keyword(s.substring(5), 0)).equals("");
      } catch(Exception e) {
        return true;
      }

    final String[] comp = { "equal_", "greater_", "lower_" };
    final int[] com = { 0, -1, 1 };
    for (int z = 0; z < com.length; z++)
      if (s.startsWith(comp[z])) {
        int a = s.indexOf('_', comp[z].length());
        Object o = getParseObject(s.substring(a + 1), null);
        if (o instanceof Number) {
          a = new Double(s.substring(comp[z].length(), a)).compareTo(
              new Double(((Number) o).doubleValue()));
          return (a == com[z] || a * com[z] > 0) ? true : false;
        }
        break;
      }

    throw new Exception("Πρόβλημα στη φράση <b>" + s + "</b>.");
  }

  // ------------------------------------ modifiers ------------------------------------- //

  protected String capital(Keyword k) throws Exception {
    k.keyword = k.obj1.toString();
    return StaticFunctions.toUppercase(compileTag(k).toString());
  }

  protected String fullWritten(Keyword k) throws Exception {
    k.keyword = k.obj1.toString();
    return StaticFunctions.getEuroFullWritten((Digit) compileTag(k));
  }

  protected String euro(Keyword k) throws Exception {
    k.keyword = k.obj1.toString();
    String s = compileTag(k).toString();
    if (s.length() != 0) s += " &#8364;";
    return s;
  }

  protected String percent(Keyword k) throws Exception {
    k.keyword = k.obj1.toString();
    String s = compileTag(k).toString();
    if (s.length() != 0) s += '%';
    return s;
  }

  // ---------------------------------- repeat operations ------------------------------ //

  protected Integer endRepeat(Keyword k) throws Exception {
    Keyword kw1 = validateCloser(k.keyword);
    Keyword kwd = (Keyword) keywords.lastElement();
    int c = 1 + ((Integer) kwd.obj2).intValue();
    if (c == ((Integer) kwd.obj1).intValue()) {
      keywords.remove(kw1);
      return null;
    } else {
      k.obj2 = new Integer(c);
      return new Integer(k.pos);
    }
  }

  // --------------------------------- counter operations ------------------------------ //

  protected String getCounterValue(Keyword kwd) throws Exception {
    for (int z = 0; z < keywords.size(); z++) {
      Keyword k = (Keyword) keywords.elementAt(z);
      if (k.keyword == "start_counter" && k.obj1.equals(kwd.obj1)) {
        int n = ((Integer) k.obj2).intValue();
        k.obj2 = new Integer(n + 1);
        if (kwd.obj1.toString().startsWith("letter_"))
          return StaticFunctions.letterCounter(n);
        else
          return Integer.toString(n);
      }
    }
    throw new Exception("Χρησιμοποιείται ο counter <b>" + kwd.obj1 +
                        "</b> που δεν έχει αρχικοποιηθεί.");
  }


  // ------------------------------------ Enum operations ------------------------------ //

  protected void startEnum(Keyword k) throws Exception {
    String key = k.obj1.toString();
    if (!ifTrue())
      k.obj2 = null;
    else
      try {
        k.obj2 = getParseObject(key, null);
        if (k.obj2 instanceof Hashtable) {
          Hashtable h = (Hashtable) k.obj2;
          k.obj2 = h.size() == 0 ? null : h.clone();
        } else if (k.obj2 instanceof Vector) {
          Vector h = (Vector) k.obj2;
          k.obj2 = h.size() == 0 ? null : h.clone();
        }
      } catch (Exception e) {
        throw StaticFunctions.getException(e, "Το <b>enum</b> δεν υποστηρίζει τη φράση <b>" + k.obj1 + "</b>");
      }
    String[] d = key.split("_", 2);
    if (findEnum(d[0]) != null) {
      key = d[1];
      if (key.startsWith("value_")) key = key.substring(6);
      else if (key.startsWith("key_")) key = key.substring(4);
      k.obj1 = key;
    }
    keywords.add(k);
  }

  protected Object endEnum() throws Exception {
    Keyword k = validateCloser("end_enum");
    Object o = k.obj2;
    if (o == null) o = "";
    else if (o instanceof Vector) {
      Vector v = (Vector) o;
      if (v.size() > 0) v.remove(0);
      if (v.size() == 0) o = ""; else o = new Integer(k.pos);
    } else if (o instanceof Hashtable) {
      Hashtable v = (Hashtable) o;
      if (v.size() > 0) v.remove(v.keys().nextElement());
      if (v.size() == 0) o = ""; else o = new Integer(k.pos);
    }
    if (!(o instanceof Integer)) keywords.remove(k);
    return o;
  }

  protected Object getParseObject(String k, Object o) throws Exception {
    if (k.startsWith("sizeof_")) {
      Object obj = getParseObject(k.substring(7), o);
      if (obj instanceof Vector) return new Short((short) ((Vector) obj).size());
      else if (obj instanceof Hashtable) return new Short((short) ((Hashtable) obj).size());
      else return new Short((short) 0);
    } else if (k.equals("bills") && o == null) return m.bills.billsModel.getData();
    else if (k.startsWith("bills_")) {
      if (o == null) o = m.bills.billsModel.getData();
      if (o instanceof Vector) return AnalyzeBills.hash((Vector) o, k);
    } else if (o instanceof Bill && k.equals("items")) return ((Bill) o).getCell(Bill.ITEMS);
    else if (o instanceof Hashing) return ((Hashing) o).hash(k);
    else if (o instanceof Hashtable) {
      Hashtable h = (Hashtable) o;
      Object key = h.keys().nextElement();
      if (k.equals("key")) return key;
      else if (k.equals("value")) return h.get(key);
      else if (k.startsWith("key_")) return getParseObject(k.substring(4), key);
      else if (k.startsWith("value_")) return getParseObject(k.substring(6), h.get(key));
      else if (h.containsKey(k)) return h.get(k);
    } else if (o instanceof Vector) {
      if (k.equals("bills")) return (Vector) o;
      return getParseObject(k, ( (Vector) o).elementAt(0));
    } else if (o == null) {
      String[] kwd = k.split("_", 2);
      Keyword kw = findEnum(kwd[0]);
      if (kwd.length == 2 && kw != null) {
         if(kw.obj2 != null) return getParseObject(kwd[1], kw.obj2);
         else return null;
      }
    }
    throw new Exception("Πρόβλημα στη φράση <b>" + k + "</b>.");
  }

  protected Keyword findEnum(String s) {
    for (int z = keywords.size() - 1; z >= 0; z--) {
      Keyword k = (Keyword) keywords.elementAt(z);
      if (k.keyword.equals("enum") && getEnumAbbreviation(k.obj1.toString()).equals(s)) return k;
    }
    return null;
  }

  static protected String getEnumAbbreviation(String s) {
    int a = -1;
    String r = "";
    do
      r += s.charAt(++a);
    while ( (a = s.indexOf('_', a)) != -1);
    return r;
  }

  // --------------------------------------- System Class --------------------------- //

  protected static String systemCall(String s) throws Exception {
    Calendar c = new GregorianCalendar();
    if (s.startsWith("system_")) s = s.substring(7);
    SimpleDateFormat df = new SimpleDateFormat("HH:mm");

    if (s.equals("hour"))
      return df.format(c.getTime());
    else if (s.equals("year"))
      return String.valueOf(c.get(Calendar.YEAR));
    else if (s.equals("month"))
      return StaticFunctions.months[c.get(Calendar.MONTH)];
    else if (s.equals("day"))
      return StaticFunctions.days[c.get(Calendar.YEAR)];
    else if (s.equals("date"))
      df.applyPattern("dd " + systemCall("month") + " yyyy");
    else if (s.equals("full_date"))
      df.applyPattern(systemCall("day") + ", dd " + systemCall("month") + " yyyy");
    else if (s.equals("full_date_time"))
      df.applyPattern(systemCall("day") + ", dd HH:mm" + systemCall("month") + " yyyy");
    else throw new Exception("<html>Η κλάση <b>System</b> δεν υποστηρίζει τη φράση <b>" + s + "</b>");
    return df.format(c.getTime());
  }


  // =============================== Advanced Keyword Class ========================= //

  static public class Keyword extends TemplateParser.Keyword {
    static String[] names = { "if", "enum", "include", "counter", "capital", "euro", "percent",
        "full_written", "static", "cost", "system", "set", "reset" };

    public Object obj1 = null;
    public Object obj2 = null;

    public Keyword(TemplateParser.Keyword kwd) {
      super(kwd.keyword.intern(), kwd.pos);
      breakKeyword();
    }

    protected void breakKeyword() {
      if (keyword.startsWith("repeat_")) {
        obj1 = new Integer(keyword.substring(7));
        keyword = "repeat";
      } else if (keyword.startsWith("start_counter_")) {
        obj1 = keyword.substring(14);
        obj2 = new Integer(1);
        keyword = "start_counter";
      } else
        for (int z = 0; z < names.length; z++)
          if (keyword.startsWith(names[z] + "_")) {
            obj1 = keyword.substring(names[z].length() + 1);
            keyword = names[z];
            return;
          }
    }

    public boolean getCLoseKeyword(String s) {
      if (
          (keyword == "if" && (s.equals("endif") || s.equals("else"))) ||
          (keyword == "start_counter" && s.equals("end_counter")) ||
          (keyword == "repeat" && s.equals("end_repeat")) ||
          (keyword == "enum" && s.equals("end_enum"))
          ) return true;
      else return false;
    }
  }
}