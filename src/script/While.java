package script;

public class While extends Statement {
  private Object func;
  private Statement block;

  public While(Object o, Statement e) {
    func = o;
    block = e;
  }

  public Object run(RuntimeEnvironment r) {
    while (bool(func instanceof Function ? ((Function) func).run(r) : func)) {
      if (block != null) {
	block.run(r);
	if (loopbreak(r)) break;
      }
    }
    return null;
  }

  public String toString() {
    String s = "while (" + func.toString() + ")";
    if (block == null) return s + ";";
    else return s + block.toString();
  }

  // an to block teleiwnei me Throw, Return, !continue ginetai to While se If
  // an to block teleiwnei se break afaireitai to break apo to block
  public Object simplify(RuntimeEnvironment r) {
    if (block != null) block = (Statement) block.simplify(r);
    if (func instanceof Function) {
      func = ((Function) func).simplify(r);
      if (func instanceof Function) return this;
    }
    return bool(func) ? new For(null, null, null, block) : null;
  }
}