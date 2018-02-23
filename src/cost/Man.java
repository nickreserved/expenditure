package cost;

import java.util.*;
import common.*;


public class Man extends HashObject {
  public String toString() { return get("Βαθμός") + " " + get("Ονοματεπώνυμο"); }
  public boolean equals(Object o) { return o instanceof Man && toString().equals(o.toString()); }
}