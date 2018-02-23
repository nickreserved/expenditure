package common;

import java.util.*;

public class VectorObject extends Vector implements Saveable, PhpSerialize {
  public String save() {
		removeDead();
    String s = "{\r\n";
    for (int z = 0; z < size(); z++)
      s += Functions.saveable(null, get(z));
    return s + "}";
  }
  
  public String serialize() {
		removeDead();
    String s = "a:" + size() + ":{";
    for (int z = 0; z < size(); z++)
      s += "i:" + z + ";" + Functions.phpSerialize(get(z));
    return s + "}";
  }
	
	private void removeDead() {
    for (int z = 0; z < size(); z++)
      if (get(z) == null) remove(z);
	}
}