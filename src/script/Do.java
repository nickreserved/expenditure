package script;

public class Do extends Statement {
  private Object func;
  private Statement block;

  public Do(Object o, Statement e) {
    func = o;
    block = e;
  }

  public Object run(RuntimeEnvironment r) {
    do {
      if (block != null) {
	block.run(r);
	if (loopbreak(r)) break;
      }
    } while (bool(func instanceof Function ? ((Function) func).run(r) : func));
    return null;
  }

  public String toString() {
    return "do " + (block == null ? ";" : block.toString()) + "while (" + func.toString() + ");";
  }

  // an to block teleiwnei me Throw, Return, !continue ginetai to Do sketo block
  // an to block teleiwnei se break afaireitai to break apo to block
  public Object simplify(RuntimeEnvironment r) {
    if (block != null) block = (Statement) block.simplify(r);
    if (func instanceof Function) {
      func = ((Function) func).simplify(r);
      if (func instanceof Function) return this;
    }
    return bool(func) ? new For(null, null, null, block) : block;
  }
}