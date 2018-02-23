package common;

public class M {
  private M() {}

  static public final Number add(Number a, double b) { return new Double(a.doubleValue() + b); }
  static public final Number add(Number a, Number b) { return new Double(a.doubleValue() + b.doubleValue()); }
  static public final Number mul(Number a, double b) { return new Double(a.doubleValue() * b); }
  static public final Number mul(Number a, Number b) { return new Double(a.doubleValue() * b.doubleValue()); }
  static public final Number sub(Number a, double b) { return new Double(a.doubleValue() - b); }
  static public final Number sub(Number a, Number b) { return new Double(a.doubleValue() - b.doubleValue()); }
  static public final Number sub(double a, Number b) { return new Double(a - b.doubleValue()); }
  static public final Number div(Number a, Number b) { return new Double(a.doubleValue() / b.doubleValue()); }
  static public final Number div(Number a, double b) { return new Double(a.doubleValue() / b); }
  static public final Number div(double a, Number b) { return new Double(a / b.doubleValue()); }
  static public final Number mod(Number a, Number b) { return new Double(a.doubleValue() % b.doubleValue()); }
  static public final Number mod(Number a, double b) { return new Double(a.doubleValue() % b); }
  static public final Number mod(double a, Number b) { return new Double(a % b.doubleValue()); }
  static public final Number round(Number a, int b) { return new Double(round(a.doubleValue(), b)); }
  static public final double round(double a, int b) {
    long div = (long) Math.pow(10, b);
    return  ( (double) Math.round(a * div) ) / div;
  }
  static public final Number parseString(String a) {
    try {
      return new Double(a);
    } catch (Exception e) {
      return null;
    }
  }
}