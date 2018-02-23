package script;

public class For extends Statement {
  private Function o1, o2, o3;
  private Statement block;

  public For(Function o1, Function o2, Function o3, Statement e) {
    this.o1 = o1;
    this.o2 = o2;
    this.o3 = o3;
    block = e;
  }

  public Object run(RuntimeEnvironment r) {
    if (o1 != null) o1.run(r);
    for (; bool(o2 == null ? Boolean.TRUE : o2.run(r));) {
      if (block != null) {
	block.run(r);
	if (loopbreak(r)) break;
      }
      if (o3 != null) o3.run(r);
    }
    return null;
  }
}