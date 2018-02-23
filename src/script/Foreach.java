package script;

public class Foreach extends Statement {
  private Function array;
  private Function key;
  private Function value;
  private Statement block;

  public Foreach(Function a, Function v, Statement e) {
    array = a;
    value = v;
    block = e;
  }
  public Foreach(Function a, Function k, Function v, Statement e) {
    this(a, v, e);
    key = k;
  }

  public Object run(RuntimeEnvironment r) {
    Object o = array.run(r);
//    if (!(
    return null;
  }
}