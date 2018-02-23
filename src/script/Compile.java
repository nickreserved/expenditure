package script;

import java.util.*;
import java.io.*;
import common.*;

public class Compile {
  private static Hashtable cache = new Hashtable();

  private static Vector functions = new Vector();

  private String html;
  private int pos = 0;
  private int depth = -1;
  private int loopdepth = 0;
  private boolean code = false;

  private Compile() {}

  static public Statement remove(String file) { return (Statement) cache.remove(file); }
  static public void clear() { cache.clear(); }

  static public Statement compileFile(String file) throws Exception {
    Statement v = (Statement) cache.get(file);
    if (v == null) cache.put(file, v = (Statement) compile(LoadSaveFile.loadFileToString(file)));
    return v;
  }

  static public Statement compile(String file, String html) {
    Statement v = (Statement) cache.get(file);
    if (v == null) cache.put(file, v = (Statement) compile(html));
    return v;
  }

  static public Statement compile(String html) {
    Compile c = new Compile();
    c.html = html;
    return c.getBlock();
  }

  private Statement getBlock() {
    Block eb = new Block();
    depth++;
    while (pos < html.length()) {
      Statement exe = getNextCommand();
      if (exe != null) eb.add(exe);
      if (html.charAt(pos - 1) == '}') {
	if (depth == 0) throw new CompileException(line(pos), "Βρέθηκε τέλος block } χωρίς να υπάρχει αρχή {");
	else break;
      }
    }
    if (pos >= html.length()) {
      throwRemainingBlocks();
      if (code) throw new CompileException(line(pos), "Βρεθηκε τέλος αρχείου χωρίς να βρεθεί τέλος block κώδικα <b>?&rt;</b>");
    }
    depth--;
    if (eb.size() == 0) return null;
    else if (eb.size() == 1) return (Statement) eb.get(0);
    else return eb;
  }

  private Statement getNextCommand() {
    Statement exe = null;
    int a = pos;

    if (!code) {

      // add text outside of <? ... ?> to an echo command
      pos = html.indexOf("<?", pos);
      if (pos == -1) pos = html.length(); else code = true;
      if (a != pos) {
	exe = new Echo(html.substring(a, pos));
	exe.setLine(line(a));
      }
      pos += 2;

    } else {

      // start a new code block { ... }
      if (skipRegex("\\s*\\{")) exe = getBlock();

      // end a code block { ... }
      else if (skipRegex("\\s*\\}"));

      // end a code block <? ... ?>
      else if (skipRegex("\\s*\\?\\>")) code = false;

      // command echo
      else if (skipRegex("\\s*echo\\s*(\\(|(\\s\\S))")) {
	pos--;
	Object o = evaluateExpression();
	exe = new Echo(o);
	if (!skipRegex(";"))
	  throwSemicolon(line(a), html.substring(a, pos));
      }

      // command echo (=)
      else if (html.startsWith("<?=", pos - 2)) {
	pos++;
	Object o = evaluateExpression();
	exe = new Echo(o);
	if (!skipRegex("\\?\\>"))
	  throw new CompileException(line(pos), "Το <b>echo (=)</b> δεν το ακολουθεί <b>?&rt;</b>");
	code = false;
      }

      // command throw
      else if (skipRegex("\\s*throw\\s*\\(\\s*")) {
	Object o = evaluateExpression();
	Object o2 = null;
	if (skipRegex(",\\s*")) o2 = evaluateExpression();
	if (!skipRegex("\\)\\s*;") || !(o instanceof String))
	  throwBadSyntax(line(a), html.substring(a, pos));
	exe = new Throw(o.toString(), o2);
      }

      // command while
      else if (skipRegex("\\s*while\\s*\\(\\s*")) {
	Object o = evaluateExpression();
	if (!skipRegex("\\)")) throwBadSyntax(line(a), html.substring(a, pos));
	loopdepth++;
	exe = new While(o, getNextCommand());
	loopdepth--;
      }

      // command for
      else if (skipRegex("\\s*for\\s*\\(\\s*")) {
	Function o1 = (Function) evaluateExpression();
	if (!skipRegex(";\\s*")) throwBadSyntax(line(a), html.substring(a, pos));
	Function o2 = (Function) evaluateExpression();
	if (!skipRegex(";\\s*")) throwBadSyntax(line(a), html.substring(a, pos));
	Function o3 = (Function) evaluateExpression();
	if (!skipRegex("\\)\\s*")) throwBadSyntax(line(a), html.substring(a, pos));
	loopdepth++;
	exe = new For(o1, o2, o3, getNextCommand());
	loopdepth--;
      }

/*      // command foreach
      else if (skipRegex("\\s*foreach\\s*\\(\\s*")) {
	Function o1 = (Function) evaluateExpression();
	if (!skipRegex(";\\s*")) throwBadSyntax(line(a), html.substring(a, pos));
	Function o2 = (Function) evaluateExpression();
	if (!skipRegex(";\\s*")) throwBadSyntax(line(a), html.substring(a, pos));
	Function o3 = (Function) evaluateExpression();
	if (!skipRegex("\\)\\s*")) throwBadSyntax(line(a), html.substring(a, pos));
	loopdepth++;
	exe = new For(o1, o2, o3, getNextCommand());
	loopdepth--;
      }

      // command include
      else if (skipRegex("\\s*for\\s*\\(\\s*")) {
	Function o1 = (Function) evaluateExpression();
	if (!skipRegex(";\\s*")) throwBadSyntax(line(a), html.substring(a, pos));
	Function o2 = (Function) evaluateExpression();
	if (!skipRegex(";\\s*")) throwBadSyntax(line(a), html.substring(a, pos));
	Function o3 = (Function) evaluateExpression();
	if (!skipRegex("\\)\\s*")) throwBadSyntax(line(a), html.substring(a, pos));
	loopdepth++;
	exe = new For(o1, o2, o3, getNextCommand());
	loopdepth--;
      }

      // command include_once
      else if (skipRegex("\\s*for\\s*\\(\\s*")) {
	Function o1 = (Function) evaluateExpression();
	if (!skipRegex(";\\s*")) throwBadSyntax(line(a), html.substring(a, pos));
	Function o2 = (Function) evaluateExpression();
	if (!skipRegex(";\\s*")) throwBadSyntax(line(a), html.substring(a, pos));
	Function o3 = (Function) evaluateExpression();
	if (!skipRegex("\\)\\s*")) throwBadSyntax(line(a), html.substring(a, pos));
	loopdepth++;
	exe = new For(o1, o2, o3, getNextCommand());
	loopdepth--;
      }
*/

      // command do
      else if (skipRegex("\\s*do(;|\\{|(\\s+\\S))")) {
	pos--;
	Statement e = getNextCommand();
	if (!skipRegex("\\s*while\\s*\\(\\s*"))
	  throwBadSyntax(line(a), html.substring(a, pos));
	Object o = evaluateExpression();
	if (!skipRegex("\\)\\s*;"))
	  throwBadSyntax(line(a), html.substring(a, pos));
	loopdepth++;
	exe = new Do(o, e);
	loopdepth--;
      }

      // command break
      else if (skipRegex("\\s*break((\\s*;)|(\\s+\\S))")) {
	int o = 1;
	if (html.indexOf(pos - 1) != ';') {
	  pos--;
	  Object obj = evaluateExpression();
	  if (!(obj instanceof Number))
	    throwBadSyntax(line(a), html.substring(a, pos));
	  o = ((Number) obj).intValue();
	  if (!skipRegex("\\s*;"))
	    throwSemicolon(line(a), html.substring(a, pos));
	}
	if (o > loopdepth) throwBadBreak(o, true);
	exe = new Break(-o);
      }

      // command continue
      else if (skipRegex("\\s*continue((\\s*;)|(\\s+\\S))")) {
	int o = 1;
	if (html.indexOf(pos - 1) != ';') {
	  pos--;
	  Object obj = evaluateExpression();
	  if (!(obj instanceof Number))
	    throwBadSyntax(line(a), html.substring(a, pos));
	  else o = ((Number) obj).intValue();
	  if (!skipRegex("\\s*;"))
	    throwSemicolon(line(a), html.substring(a, pos));
	}
	if (o > loopdepth) throwBadBreak(o, false);
	exe = new Break(o);
      }

      // command exit
      else if (skipRegex("\\s*exit\\s*;"))
	exe = new Break(RuntimeEnvironment.EXIT);

      // command return
      else if (skipRegex("\\s*return((\\s*;)|(\\s+\\S))")) {
	Object o = null;
	if (html.indexOf(pos - 1) != ';') {
	  pos--;
	  o = evaluateExpression();
	  if (!skipRegex(";"))
	    throwSemicolon(line(a), html.substring(a, pos));
	}
	exe = new Return(o);
      }

      // command if
      else if (skipRegex("\\s*if\\s*\\(\\s*")) {
	Object o = evaluateExpression();
	if (!skipRegex("\\)")) throwBadSyntax(line(a), html.substring(a, pos));
	Statement i = getNextCommand();
	Statement e = null;
	if (skipRegex("\\s*else(;|\\{|(\\s+\\S))")) {
	  pos--;
	  e = getNextCommand();
	}
	exe = new If(o, i, e);
      }

      if (exe != null) exe.setLine(line(a));
    }

    return exe;
  }

  // evaluates an expression. skip any white-space. stop on ,;)] and ?> (gia to = echo)
  private Object evaluateExpression() {
    return null;
  }



  private int line(int p) { return Functions.getCurrentLine(html, "\n", p); }

  private boolean skipRegex(String regex) {
    int a = Functions.findEndOfRegex(html, pos, regex);
    if (a == -1) return false;
    pos = a;
    return true;
  }

  private void throwRemainingBlocks() {
    if (depth > 0) throw new CompileException(line(pos), "Βρεθηκε τέλος κώδικα χωρις να κλείσουν <b>" + depth + " { ... }</b> block(s)");
  }

  private static void throwNoParameter(int p, String o) {
    throw new CompileException(p, "Βρέθηκε <b>" + o + "</b> δίχως παράμετρο");
  }

  private void throwBadBreak(int b, boolean o) {
    throw new CompileException(line(pos), "Βρέθηκε εντολή <b>" + (o ? "break " : "continue ") +
			       b + "</b> ενώ οι υπάρχοντες βρόχοι είναι μόνο " + loopdepth);
  }

  private static void throwBadSyntax(int p, String o) {
    throw new CompileException(p, "Η εντολή <b>" + o + "</b> δεν έχει σωστή σύνταξη");
  }

  private static void throwSemicolon(int p, String o) {
    throw new CompileException(p, "Η εντολή <b>" + o + "</b> θα έπρεπε να ακολουθείται από <b>;</b>");
  }

  public static void findAllFunctions() {
    String[] cls = new File(ClassLoader.getSystemResource("script/function").getPath()).list();
    for (int z = 0; z < cls.length; z++)
      if (cls[z].endsWith(".class")) {
        String s = cls[z];
        s = "script.function." + s.substring(0, 1).toUpperCase() + s.substring(1, s.length() - 6).toLowerCase();
        functions.add(s);
      }
  }
}