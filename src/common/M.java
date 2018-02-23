package common;

public class M {
  private M() {}

  public static double safeNumber2double(Number o) { return o == null ? 0 : o.doubleValue(); }
  static public Number add(Number a, double b) { return new Double(a.doubleValue() + b); }
  static public Number add(Number a, Number b) { return new Double(a.doubleValue() + b.doubleValue()); }
  static public Number mul(Number a, double b) { return new Double(a.doubleValue() * b); }
  static public Number mul(Number a, Number b) { return new Double(a.doubleValue() * b.doubleValue()); }
  static public Number sub(Number a, double b) { return new Double(a.doubleValue() - b); }
  static public Number sub(Number a, Number b) { return new Double(a.doubleValue() - b.doubleValue()); }
  static public Number sub(double a, Number b) { return new Double(a - b.doubleValue()); }
  static public Number div(Number a, Number b) { return new Double(a.doubleValue() / b.doubleValue()); }
  static public Number div(Number a, double b) { return new Double(a.doubleValue() / b); }
  static public Number div(double a, Number b) { return new Double(a / b.doubleValue()); }
  static public Number mod(Number a, Number b) { return new Double(a.doubleValue() % b.doubleValue()); }
  static public Number mod(Number a, double b) { return new Double(a.doubleValue() % b); }
  static public Number mod(double a, Number b) { return new Double(a % b.doubleValue()); }
  static public Number round(Number a, int b) { return new Double(round(a.doubleValue(), b)); }
  static public double round(double a, int b) {
    long div = (long) Math.pow(10, b);
    return  ( (double) Math.round(a * div) ) / div;
  }
  static public Number parseString(String a) {
    try {
      return new Double(a);
    } catch (NumberFormatException | NullPointerException e) {
      return null;
    }
  }
}