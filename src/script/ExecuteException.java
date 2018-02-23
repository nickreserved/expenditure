package script;

public class ExecuteException extends CompileException {
  String ex;
  public ExecuteException(String exception, String why, int line) {
    super(line, why);
    ex = exception;
  }
  public String toString() {
    return super.toString().replaceFirst("μεταγλώττισης", "εκτέλεσης");
  }
}