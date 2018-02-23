package cost;

import java.io.*;
import common.*;

public class TemplateParser {

  private TemplateParser() {}

  static public String parse(String file, TemplateTagCompiler c) throws Exception {
    String html = LoadSaveFile.loadFileToString("templates/" + file);
    String s = "";
    int st = 0, en;
    for (;;) {
      en = html.indexOf("<?", st);
      s += Functions.safeObject2String(c.compileTag(new Keyword(
	  html.substring(st, en == -1 ? html.length() : en), -1)));
      if (en == -1) return s;
      if ((st = html.indexOf("?>", en += 2)) == -1) st = html.length();
      Object o = Functions.safeObject2String(c.compileTag(new Keyword(html.substring(en, st), st += 2)));
      if (o instanceof Integer) st = ((Integer) o).intValue();
      else s += o;
    }
  }


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