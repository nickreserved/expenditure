package common;

public interface Hash {
  public Object get(Object key);
  public void removeTemporary();
}