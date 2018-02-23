package script;

public class If extends Statement {
  private Object func;
  private Statement ifBlock;
  private Statement elseBlock;

  public If(Object func, Statement ifBlock) {
    this.func = func;
    this.ifBlock = ifBlock;
    this.elseBlock = elseBlock;
  }

  public If(Object func, Statement ifBlock, Statement elseBlock) {
    this(func, ifBlock);
    this.elseBlock = elseBlock;
  }

  public Object run(RuntimeEnvironment r) {
    if (bool(func instanceof Function ? ((Function) func).run(r) : func)) {
      if (ifBlock != null) ifBlock.run(r);
    } else {
      if (elseBlock != null) elseBlock.run(r);
    }
    return null;
  }

  public String toString() {
    String s = "if (" + func.toString() + ")" +
	(ifBlock == null ? ";" : (" " + ifBlock.toString()));
    if (elseBlock != null) s += " else " + elseBlock.toString();
    return s;
  }

  public Object simplify(RuntimeEnvironment r) {
    if (ifBlock != null) ifBlock = (Statement) ifBlock.simplify(r);
    if (elseBlock != null) elseBlock = (Statement) elseBlock.simplify(r);
    if (func instanceof Function) {
      func = ((Function) func).simplify(r);
      if (func instanceof Function) return this;
    }
    if (func == null ||
	func instanceof Number && ((Number) func).doubleValue() == 0 ||
	func instanceof String && func.toString().length() == 0 ||
	func instanceof Boolean && func.equals(Boolean.FALSE)) {
      return elseBlock;
    } else return ifBlock;
  }
}