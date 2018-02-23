package cost;

import java.util.*;
import java.text.*;
import javax.swing.*;
import java.util.regex.*;

public class StaticFunctions {
  private StaticFunctions() {}



  static final public String[] months = { "Ιαν", "Φεβ", "Μαρ", "Απρ", "Μαι", "Ιουν", "Ιουλ", "Αυγ",
      "Σεπ", "Οκτ", "Νοε", "Δεκ" };

  static final public String[] days = { "Κυρ", "Δευ", "Τρι", "Τετ", "Πεμ", "Παρ", "Σαβ" };



  static public Vector propertiesarray2vector (Object[][] s) {
    Vector v = new Vector();
    for (int z = 0; z < s.length; z++) v.add(s[z][0]);
    return v;
  }





  static public Object safeObject2String(Object o) { return o == null ? "" : o; }







  static public String letterCounter(int n) {
    if (n < 1 || n > 89) return "";
    String[] m = { "", "α", "β", "γ", "δ", "ε", "στ", "ζ", "η", "θ" };
    String[] d = { "", "ι", "κ", "λ", "μ", "ν", "ξ", "ο", "π" };
    return d[n / 10] + m[n % 10];
  }





  static public String getEuroFullWritten(Digit d) throws ParseException {
    String[] s = d.toString().split(",", 2);
    String b = null, a = numToText(s[0]);
    if (s.length == 2) b = numToText(s[1].substring(0, 2));
    if (b != null && !b.equals("μηδέν")) b += " λεπτά"; else b = null;
    if (a.equals("μηδέν")) {
      if (b == null) return "μηδέν ευρώ"; else return b;
    } else {
      return a + " ευρώ" + (b == null ? "" : " και " + b);
    }
  }


  static public String numToText(String s) throws ParseException {
    String[][] other = { { "χίλια", "χιλιάδες" }, { "εκατομμύριο", "εκατομμύρια" } };
    String[] other_1 = { "δισ", "τρισ", "τετράκις", "πεντάκις", "εξάκις", "επτάκις", "οκτάκις" };

    String o = "", t;
    if (s.length() == 0) return "μηδέν";
    int a, c = 0;

    while ((a = s.length()) != 0) {
      if (a > 2) {
	t = s.substring(a - 3);
	s = s.substring(0, a - 3);
      } else {
	t = s;
	s = "";
      }
      t = numToText100(t, c == 1) + " ";
      if (c == 0);
      else if (c > 2) {
	t += other_1[c - 3] + (c < 5 ? "" : " ");
	c = 2;
      }
      if (c > 0 && c <= 2) {
	int j = 1;
	if (t.startsWith("ένα")) {
	    if (c == 1) t = other[c - 1][0]; else t += other[c - 1][0];
	} else if (t.trim().length() != 0)
	  t += other[c - 1][1];
      }
      o = t + " " + o;
      c++;
    }
    o = o.replaceAll("\\s+", " ").trim();
    return o.length() == 0 ? "μηδέν" : o;
  }


  static private String numToText100(String s, boolean female) throws ParseException {
    String[] ekatodades = { "εκατό", "εκατόν", "διακόσια", "τριακόσια", "τετρακόσια", "πεντακόσια", "εξακόσια", "εφτακόσια", "οκτακόσια", "εννιακόσια" };
    String[] dekades = { "είκοσι", "τριάντα", "σαράντα", "πενήντα", "εξήντα", "εβδομήντα", "ογδόντα", "εννενήντα" };
    String[] monades = { "ένα", "δύο", "τρία", "τέσσερα", "πέντε", "έξι", "επτά", "οκτώ", "εννέα", "δέκα", "έντεκα", "δώδεκα", "δεκατρία", "δεκατέσσερα", "δεκαπέντε", "δεκαέξι", "δεκαεπτά", "δεκαοκτώ", "δεκαεννιά" };
    String[] monades_2 = { "ένα", "δύο", "τρείς", "τέσσερεις", "πέντε", "έξι", "επτά", "οκτώ", "εννέα", "δέκα", "έντεκα", "δώδεκα", "δεκατρείς", "δεκατέσσερεις", "δεκαπέντε", "δεκαέξι", "δεκαεπτά", "δεκαοκτώ", "δεκαεννιά" };

    String o = "";
    int a = 0;
    if (s.length() > 3) throw new ParseException(null, 0);
    if (s.length() == 3) {
      a = s.charAt(0) - "0".charAt(0);
      if (a > 0 && a < 10) {
	if (s.substring(1).equals("00") && a == 1) a = 0;
	o = ekatodades[a];
      }
      s = s.substring(1);
    }
    if (s.length() == 2) {
      a = s.charAt(0) - "0".charAt(0);
      if (a > 1 && a < 10) o += " " + dekades[a - 2];
      s = s.substring(1);
    }
    a = s.charAt(0) - "0".charAt(0) + (a == 1 ? 10 : 0);
    if (a != 0) {
      if (!female) o += " " + monades[a - 1];
      else o += " " + monades_2[a - 1];
    }
    return o.trim();
  }





  static public String toUppercase(String s) {
    String t = "";
    boolean num = false;
    for (int z = 0; z < s.length(); z++) {
      char c = s.charAt(z);
      if (" \r\n\t".indexOf(c) != -1) num = false;
      else if (num);
      else if ("0123456789".indexOf(c) != -1) num = true;
      else
	c = ("" + c).toUpperCase().replaceAll("Ά", "Α").replaceAll("Έ", "Ε")
	    .replaceAll("Ή", "Η").replaceAll("Ί", "Ι").replaceAll("Ό", "Ο")
	    .replaceAll("Ύ", "Υ").replaceAll("Ώ", "Ω").charAt(0);
      t += c;
    }
    return t;
  }






  static public int findInArray(Object[] a, Object b) {
    if (b == null) return -1;
    for (int z = 0; z < a.length; z++)
      if (b.equals(a[z])) return z;
    return -1;
  }








  static public Exception getException(Exception e, String s) {
    String a = e == null ? null : e.getMessage();
    if (a == null || a.length() < 5) a = null;
    String b = e == null ? null : e.getClass().getName();
    if (b != null && b.equals("java.lang.Exception")) b = null;
    if ((a != null || b != null) && s != null) s = "<br>" + s;
    if (s == null) s = "";
    if (a != null) s = a + s;
    if (a != null && b != null) s = ": " + s;
    if (b != null) s = "<b>" + b + "</b>" + s;
    return new Exception(s);
  }








  public static void showExceptionMessage(Exception e, String s) {
    JOptionPane.showMessageDialog(null,
				  "<html>Διαπράχθηκαν τα παρακάτω Στρατιωτικά Εγκλήματα:<br>" +
				  StaticFunctions.getException(e, null).getMessage(), s,
				  JOptionPane.ERROR_MESSAGE);
  }







  public static String multipleInflections(String a, byte what) {
    String out = "";
    int s, prev = 0;
    Matcher m = Pattern.compile("(\\p{InGreek}{3,}+)").matcher(a);
    while(m.find()) {
      out += a.substring(prev, s = m.start());
      out += getInflection(m.group(), what);
      prev = m.end();
    }
    out += a.substring(prev, a.length());
    return out;
  }



  public static String getInflection(String onomastikh, byte what) {
    if (onomastikh.endsWith("ος") && what == 1) return onomastikh.replace('ς', 'υ');
    else if (onomastikh.endsWith("ός") && what == 1) return onomastikh.replace('ς', 'ύ');
    else if (what >= 2 && what <= 3 && (
	onomastikh.endsWith("ος") || onomastikh.endsWith("ός")) ||
	what >= 1 && what <= 3 && (
	onomastikh.endsWith("ης") || onomastikh.endsWith("ής") ||
	onomastikh.endsWith("ας") || onomastikh.endsWith("άς") ||
	onomastikh.endsWith("ες") || onomastikh.endsWith("ές") ||
	onomastikh.endsWith("υς") || onomastikh.endsWith("ύς"))) {
      return onomastikh.replaceAll("ς", "");
    } else if (what == 1 && (
	onomastikh.endsWith("η") || onomastikh.endsWith("ή") ||
	onomastikh.endsWith("α") || onomastikh.endsWith("ά") ||
	onomastikh.endsWith("ω") || onomastikh.endsWith("ώ")))
      return onomastikh + "ς";
    return onomastikh;
  }





}