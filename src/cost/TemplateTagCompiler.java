package cost;

public interface TemplateTagCompiler {
  // if return an Integer, is the new compile offset
  // any other return to String
  public Object compileTag(TemplateParser.Keyword kwd) throws Exception;
}