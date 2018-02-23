package script;

import java.util.*;

public class Block extends Statement {
  private Vector block = new Vector();

  public void add(Statement o) { block.add(o); }
  public int size() { return block.size(); }
  //public void addBlock(Block eb) {
  //  for (int
  //}
  // mhpws xreiazetai to replace? to addBlock?
  public void remove(int a) { block.remove(a); }
  public Statement get(int a) { return (Statement) block.get(a); }

  public Object run(RuntimeEnvironment r) {
    for (int z = 0; z < block.size() && r.control == 0; z++)
      ((Statement) block.get(z)).run(r);
    return null;
  }

  public String toString() {
    String a = "{\r\n";
    for (int z = 0; z < block.size(); z++) a += block.get(z).toString() + "\r\n";
    return a + "}\r\n";
  }

  public Object simplify(RuntimeEnvironment r) {
    int c = 0;
    for (int z = 0; z < block.size(); z++) {
      Object o;
      block.set(c, o = get(z).simplify(r));
      if (o instanceof Statement) { // && o changes something) { // dhladh ekxwrhsh se metablhth
	c++;
	if (o instanceof Break || o instanceof Throw) break;
      }
    }
    if (c < block.size()) block.setSize(c);
    if (block.size() == 0) return null;
    return this;
  }
}