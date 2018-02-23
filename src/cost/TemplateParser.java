package cost;

import java.io.*;

public class TemplateParser {

  private TemplateParser() {}

  static public String parse(String file, TemplateTagCompiler c) throws Exception {
    String html = LoadSaveFile.loadFileToString("templates/" + file);
    if (html == null) return "";

    String s = "";
    int bst, st = 0, en = 0;
    int line = 0, col = 0;

    for (;;)
    {
      // find first '%' & second '%'
      if ((st = html.indexOf("%", bst = st)) == -1) st = bst;
      if ((en = html.indexOf("%", st + 1)) == -1) {
        s += StaticFunctions.safeObject2String(c.compileTag(new Keyword(html.substring(bst), -1)));
        return s;
      }

      // add text which is not tag
      s += StaticFunctions.safeObject2String(c.compileTag(new Keyword(html.substring(bst, st), -1)));

      // check if it is a tag with forbidden chars
      String tag = html.substring(st + 1, en);
      st = en + 1;
      if (hasForbiddenChars(tag)) {
        if (tag.length() != 0) tag = "%" + tag;
        st = en;
        s += StaticFunctions.safeObject2String(c.compileTag(new Keyword(tag, -1)));
      } else {
        Object o = StaticFunctions.safeObject2String(c.compileTag(new Keyword(tag, en + 1)));
        if (o instanceof Integer) st = ((Integer) o).intValue();
        else s += o;
      }
    }
  }

//  protected static boolean hasForbiddenChars(String s) { return !s.matches("[0-9a-zA-Z_.]+"); }
  protected static boolean hasForbiddenChars(String s) { return !s.matches("[\\w.]+(\"[\\s\\S]+\")?"); }


  // =================== this is a class for passing keywords ================== //

  static public class Keyword {
    public String keyword;
    public int pos;

    public Keyword(String keyword, int pos) {
      this.keyword = keyword;
      this.pos = pos;
    }
  }
}