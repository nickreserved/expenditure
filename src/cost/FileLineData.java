package cost;

public interface FileLineData {
  public String load(String s) throws Exception;
  public String save();
  public boolean isValid();
}