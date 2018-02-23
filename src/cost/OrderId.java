package cost;

import java.util.*;
import common.*;

public class OrderId extends Dictionary {
  private static final String[] hashKeys = { "Φάκελος", "Υποφάκελος", "Πρωτόκολλο", "Σχέδιο",
      "Ημερομηνία", "Εκδότης" };
  private String order;

  public OrderId(String s) {
    if (s.matches("Φ\\.?\\d+/\\d+/\\d+/Σ\\.?\\d+/\\d{2} \\p{InGreek}{3,4} \\d{4}/.+")) order = s;
  }

  public String toString() { return (order == null) ? "" : order; }
//  public String toString() { return order; }
  public int size() { return order == null ? 0 : 6; }
  public boolean isEmpty() { return order == null; }
  public Enumeration elements() { return null; }
  public Enumeration keys() { return null; }
  public Object put(Object key, Object value) { return null; }
  public Object remove(Object key) { return null; }

  public Object get(Object key) {
    if (order == null) return null;
    String[] d = order.split("/", 6);
    if (d.length != 6) return null;
    for (int z = 0; z < 6; z++)
      if (key.equals(hashKeys[z])) return d[z];
    return null;
  }
}