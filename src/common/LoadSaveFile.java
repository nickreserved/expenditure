package common;

import java.util.*;
import java.io.*;
import javax.swing.*;

public class LoadSaveFile {

  static public String loadFileToString(String file) throws IOException {
    InputStream is = ClassLoader.getSystemResourceAsStream(file);
    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    byte[] buffer = new byte[1024];
    while (true) {
      int len = is.read(buffer);
      if (len < 0) break;
      bout.write(buffer, 0, len);
    }
    return bout.toString();
  }

  static public String loadFile(String file) throws IOException {
    InputStream is = new FileInputStream(file);
    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    byte[] buffer = new byte[1024];
    while (true) {
      int len = is.read(buffer);
      if (len < 0) break;
      bout.write(buffer, 0, len);
    }
    return bout.toString();
  }

  static public void save(String file, Object sv) {
    try {
      if (sv == null ||
	  sv instanceof List && ((List) sv).size() == 0 ||
	  sv instanceof Dictionary && ((Dictionary) sv).size() == 0 ||
	  sv instanceof Map && ((Map) sv).size() == 0) return;
      String s = sv.getClass().getName() + " ";
      if (sv instanceof Saveable) s += ((Saveable) sv).save();
      else {
	String t = sv.toString();
	if (!(sv instanceof Number))
	    t = "\"" + t.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\\"", "\\\\\\\"") + "\"";
	s += t;
      }
      s += ";\r\n";

      String[] d = s.split("\r\n");
      s = "";
      int c = 0;
      for (int z = 0; z < d.length; z++) {
	if (d[z].startsWith("}") && c > 0) c--;
	s += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t".substring(0, c) + d[z] + "\r\n";
	if (d[z].endsWith("{")) c++;
      }

      saveStringFile(file, s);
    } catch (Exception e) {
      Functions.showExceptionMessage(e, "Πρόβλημα κατά την αποθήκευση του αρχείου");
    }
  }

  static public void saveStringFile(String file, String data) throws IOException {
    OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file));
    osw.write(data);
    osw.flush();
    osw.close();
  }
}