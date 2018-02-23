package common;

import java.util.*;

public class VectorObject extends Vector implements Saveable {
  public String save() {
    String s = "{\r\n";
    for (int z = 0; z < size(); z++) {
      Object value = get(z);
      s += value.getClass().getName() + " ";
      if (value instanceof Saveable)
	s += ((Saveable) value).save();
      else {
	String t = value.toString();
	if (!(value instanceof Number))
	  t = "\"" + t.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\\"", "\\\\\\\"") + "\"";
	s += t;
      }
      s += ";\r\n";
    }
    return s + "}";
  }
}