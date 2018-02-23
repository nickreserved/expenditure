package script;

public class Break extends Statement {
  // -128: exit
  // -127: return (see returnValue)
  // > 0:  continue
  // < 0:  break
  int blocks;
  public Break(int a) { blocks = a; }
  public Object run(RuntimeEnvironment r) {
    r.control = (byte) -blocks;
    return null;
  }
}