package cost;

import common.HashObject;

public class Man extends HashObject {
  @Override public String toString() { return get("Βαθμός") + " " + get("Ονοματεπώνυμο"); }
}