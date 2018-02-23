package cost;

import java.util.*;
import javax.swing.*;
import java.text.*;

public class HashTable implements TemplateTagCompiler {
  MainFrame m;
  Vector keywords = new Vector();
  Hashtable parameters = new Hashtable2();

  public HashTable(MainFrame m, Hashtable param) {
    this.m = m;
    if (param != null) parameters = param;
  }

  public String openFile(String file) throws Exception {
    return TemplateParser.parse(file + ".html", this);
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
    else if (k.keyword == "enum") startEnum(k);
    else if (iftrue) {
      if (k.keyword == "set") parameter(k.obj1.toString(), true);
      else if (k.keyword == "reset") parameter(k.obj1.toString(), false);
      else if (k.keyword == "request") paramRequest(k.obj1.toString(), k.obj2.toString());
      else s = getParseObject(k.keyword, null);
    }
    return iftrue ? s : null;
  }

  protected Keyword validateCloser(String s) throws Exception {
    try {
      Keyword k = (Keyword) keywords.lastElement();
      if (!k.isCorrectCLosingKeyword(s)) throw new Exception("Το <b>" + k.keyword +
            "</b> δεν κλείνει με <b>" + s + "</b>.");
      return k;
    } catch (Exception e) {
      throw new Exception("Βρέθηκε <b>" + s + "</b> χωρίς λόγο ύπαρξης.");
    }
  }


  // ------------------------- parameter operations ---------------------------- //

  protected void paramRequest(String s, String message) throws Exception {
    String[] d = s.split("_", 3);
    if (parameters.containsKey(d[0])) return;
    final String title = "Ερώτηση κατά την μεταγλώτιση του προτύπου";
    boolean isDigit = !d[1].equals("boolean");
    message = "<html>" + message + "</html>";
    if (isDigit) {
      parameters.put(d[0],
          new Digit(StaticFunctions.safeObject2String(JOptionPane.showInputDialog(null, message, title,
          JOptionPane.QUESTION_MESSAGE, null, null, getParseObject(d[2], null))).toString(), 2, true, false));
    } else {
      if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, message, title,
          JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE))
        parameters.put(d[0], Boolean.TRUE);
      else
        parameters.put(d[0], Boolean.FALSE);
    }
  }

  protected void parameter(String o, boolean add) throws Exception {
    if (add) {
      try {
        String[] s = o.split("_", 3);
        if (s[1].equals("boolean"))
          parameters.put(s[0], s[2].equals("true") ? Boolean.TRUE : Boolean.FALSE);
        else if (s[1].equals("digit"))
          parameters.put(s[0], new Digit(s[2], 2, false, false));
        else {
          s = o.split("_", 2);
          parameters.put(s[0], getParseObject(s[1], null));
        }
      } catch(Exception e) {
        throw StaticFunctions.getException(e, "Δεν μπορεί να τεθεί η παράμετρος <b>" + o + "</b>.");
      }
    } else {
      if (parameters.containsKey(o)) parameters.remove(o);
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
    if (ifTrue())
      try {
        k.obj1 = new Boolean(parseIf(k.obj1.toString()));
      } catch (Exception e) {
        throw StaticFunctions.getException(e, "Το <b>if</b> δεν υποστηρίζει τη φράση <b>" + k.obj1 + "</b>");
      }
    keywords.add(k);
  }

  protected void elseIf(Keyword k) throws Exception {
    k = validateCloser(k.keyword);
    if (Boolean.FALSE.equals(k.obj1))
      k.obj1 = Boolean.TRUE;
    else k.obj1 = Boolean.FALSE;
  }

  protected void endIf(Keyword k) throws Exception {
    keywords.remove(validateCloser(k.keyword));
  }

  protected boolean parseIf(String s) throws Exception {
    if (s.startsWith("not_")) return parseIf(s.substring(4)) ? false : true;
    else if (s.startsWith("null_"))
      try {
        return getParseObject(s.substring(5), null) == null;
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

    Object o = getParseObject(s, null);
    return o != Boolean.FALSE && o != null;
  }


  // ------------------------------------ modifiers ------------------------------------- //

  protected String euro(String s) { return s != null && s.length() != 0 ? s += " &#8364;" : s; }
  protected String percent(String s) { return s != null && s.length() != 0 ? s += "%" : s; }


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

  protected String getCounterValue(String kwd) throws Exception {
    for (int z = 0; z < keywords.size(); z++) {
      Keyword k = (Keyword) keywords.elementAt(z);
      if (k.keyword == "start_counter" && k.obj1.equals(kwd)) {
        int n = ((Integer) k.obj2).intValue();
        k.obj2 = new Integer(n + 1);
        if (kwd.startsWith("letter_"))
          return StaticFunctions.letterCounter(n);
        else
          return Integer.toString(n);
      }
    }
    throw new Exception("Χρησιμοποιείται ο counter <b>" + kwd + "</b> που δεν έχει αρχικοποιηθεί.");
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
        throw StaticFunctions.getException(e, "Το <b>enum</b> δεν υποστηρίζει τη φράση <b>" + key + "</b>");
      }
    String[] d = key.split("_", 2);
    if (findEnum(d[0]) != null) {
      if (d[1].startsWith("value_") || d[1].startsWith("key_")) d = d[1].split("_", 2);
      key = d[1];
    }
    k.obj1 = getEnumAbbreviation(key);
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
    try {
      if (k.startsWith("counter_")) return getCounterValue(k.substring(8));
      else if (k.startsWith("parameter_")) return parameters.get(k.substring(10));
      else if (k.startsWith("genikh_")) return StaticFunctions.multipleInflections(StaticFunctions.safeObject2String(getParseObject(k.substring(7), null)).toString(), (byte) 1);
      else if (k.startsWith("aitiatikh_")) return StaticFunctions.multipleInflections(StaticFunctions.safeObject2String(getParseObject(k.substring(10), null)).toString(), (byte) 2);
      else if (k.startsWith("klitikh_")) return StaticFunctions.multipleInflections(StaticFunctions.safeObject2String(getParseObject(k.substring(8), null)).toString(), (byte) 3);
      else if (k.startsWith("capital_")) return StaticFunctions.toUppercase(StaticFunctions.safeObject2String(getParseObject(k.substring(8), null)).toString());
      else if (k.startsWith("euro_")) return euro(getParseObject(k.substring(5), null).toString());
      else if (k.startsWith("percent_")) return percent(getParseObject(k.substring(8), null).toString());
      else if (k.startsWith("full_written_")) return StaticFunctions.getEuroFullWritten((Digit) getParseObject(k.substring(13), null));
      else if (k.startsWith("static_")) return m.staticData.hash(k.substring(7));
      else if (k.startsWith("cost_")) return m.cost.hash(k.substring(5));
      else if (k.startsWith("system_")) return systemCall(k.substring(7));
      else if (k.startsWith("sizeof_")) {
        Object obj = getParseObject(k.substring(7), o);
        if (obj instanceof Vector) return new Short((short) ((Vector) obj).size());
        else if (obj instanceof Hashtable) return new Short((short) ((Hashtable) obj).size());
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
    } catch (Exception e) {
      throw StaticFunctions.getException(e, "Πρόβλημα στη φράση <b>" + k + "</b>.");
    }
  }

  protected Keyword findEnum(String s) {
    for (int z = keywords.size() - 1; z >= 0; z--) {
      Keyword k = (Keyword) keywords.elementAt(z);
      if (k.keyword.equals("enum") && k.obj1.toString().equals(s)) return k;
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
    protected static final String[] names = { "if", "enum", "include", "set", "reset" };

    public Object obj1 = null;
    public Object obj2 = null;

    public Keyword(TemplateParser.Keyword kwd) {
      super(kwd.keyword.intern(), kwd.pos);
      breakKeyword();
    }

    protected void breakKeyword() {
      if (keyword.startsWith("request_")) {
        String[] d = keyword.substring(8).split("\"", 2);
        obj1 = d[0].substring(0, d[0].length() - 1);
        obj2 = d[1].substring(0, d[1].length() - 1);
        keyword = "request";
      } else if (keyword.startsWith("repeat_")) {
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

    public boolean isCorrectCLosingKeyword(String s) {
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