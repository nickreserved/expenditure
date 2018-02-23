package cost;

public class OrderId implements FromString, Hashing {
  protected static final String[] hashKeys = { "complete", "fakelos", "ypofakelos", "prwtokollo",
      "sxedio", "date", "from" };

  String order;

  public OrderId() {}
  public OrderId(String s) { fromString(s); }

  public String toString() { return order; }

  // ---------------------------- FromString --------------------------------------------- //

  public void fromString(String s) {
    if (s.matches("Φ\\.\\d{3}/\\d+/\\d+/Σ\\.\\d+/\\d{2} \\p{InGreek}{3,4} \\d{4}/.+")) order = s;
  }

  // ---------------------------- Hashing --------------------------------------------- //

  public Object hash(String s) throws Exception {
    if (order == null)
      throw new Exception("Δεν αρχικοποιήθηκε η κλάση <b>OrderId</b>");
    if (s.startsWith("order_")) s = s.substring(6);
    if (s.equals(hashKeys[0])) return order;
    String[] d = order.split("/", 6);
    for (int z = 1; z < hashKeys.length; z++)
      if (s.equals(hashKeys[z])) return d[z - 1];
    throw new Exception("Η κλάση <b>BillItem</b> δεν υποστηρίζει τη φράση <b>" + s + "</b>");
  }
}