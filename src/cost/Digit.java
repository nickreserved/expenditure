package cost;

import java.text.*;

public class Digit extends Number implements FromString {
  static public final int NONE = -1000;

  protected double n = 0;
  protected int d = NONE;
  protected boolean optionaldecimal = true;
  protected boolean emptyzero = true;

  public Digit() {}
  public Digit(String s) { setDigit(s); }
  public Digit(String s, int digit) { setDigit(s, digit); }
  public Digit(String s, int digit, boolean optionalDecimal, boolean emptyIfZero) {
    setDigit(s, digit);
    setFlags(optionalDecimal, emptyIfZero);
  }
  public Digit(double s) { setDigit(s); }
  public Digit(double s, int digit) { setDigit(s, digit); }
  public Digit(double s, int digit, boolean optionalDecimal, boolean emptyIfZero) {
    setDigit(s, digit);
    setFlags(optionalDecimal, emptyIfZero);
  }

  public void setDigit(String s) {
    n = parseDigit(s);
    round(d);
  }
  public void setDigit(String s, int digit) {
    d = digit;
    n = parseDigit(s, d);
  }
  public void setDigit(double num) { n = round(num, d); }
  public void setDigit(double num, int digit) {
    n = round(num, digit);
    d = digit;
  }

  public double doubleValue() { return n; }
  public float floatValue() { return (float) n; }
  public int intValue() { return (int) n; }
  public long longValue() { return (long) n; }

  public Object clone() { return new Digit(n, d, optionaldecimal, emptyzero); }

  public void setFlags(boolean optionalDecimal, boolean emptyIfZero) {
    optionaldecimal = optionalDecimal;
    emptyzero = emptyIfZero;
  }

  public void round(int digit) {
    d = digit;
    n = round(n, d);
  }

  public static double parseDigit(String s) {
    try {
      DecimalFormat nf = new DecimalFormat();
      return nf.parse(s).doubleValue();
    } catch (Exception e) {
      return 0;
    }
  }

  public static double parseDigit(String s, int digit) {
    return round(parseDigit(s), digit);
  }

  static public double round(double num, int digit) {
    if (digit == NONE) return num;
    long div = (long) Math.pow(10, digit);
    return  ( (double) Math.round(num * div) ) / div;
  }

  public String toString() {
    if (n == 0 && emptyzero) return "";
    String s = "";
    String a = optionaldecimal ? "#" : "0";
    for (int z = 0; z < d; z++)
      s += z == 0 ? "." + a : a;
    if (d == NONE) s = ".#####################";
    return new DecimalFormat("#0" + s).format(n);
  }

  public void fromString(String s) { setDigit(s); }

  public double add(double num) { return n = round(n + num, d); }
  public double add(Digit num) { return add(num.doubleValue()); }
  public static Digit add(Digit num1, double num2) {
    return new Digit(num1.n + num2, num1.d, num1.optionaldecimal, num1.emptyzero);
  }
  public static Digit add(Digit num1, Digit num2) { return add(num1, num2.doubleValue()); }

  public double sub(double num) { return n = round(n - num, d); }
  public double sub(Digit num) { return sub(num.doubleValue()); }

  public double mul(double num) { return n = round(n * num, d); }
  public double mul(Digit num) { return mul(num.doubleValue()); }
  public static Digit mul(Digit num1, double num2) {
    return new Digit(num1.n * num2, num1.d, num1.optionaldecimal, num1.emptyzero);
  }
  public static Digit mul(Digit num1, Digit num2) { return mul(num1, num2.doubleValue()); }

  public double div(double num) { return n = round(n / num, d); }
  public double div(Digit num) { return div(num.doubleValue()); }
}