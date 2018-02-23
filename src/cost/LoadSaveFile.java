package cost;

import java.util.*;
import java.io.*;
import javax.swing.*;

public class LoadSaveFile {

  static public String loadFileToString(String file) throws Exception {
    try {
      InputStream is = ClassLoader.getSystemResourceAsStream(file);
      ByteArrayOutputStream bout = new ByteArrayOutputStream();
      byte[] buffer = new byte[1024];
      while (true) {
        int len = is.read(buffer);
        if (len < 0) break;
        bout.write(buffer, 0, len);
      }
      return bout.toString();
    } catch (Exception e) {
      throw new Exception("Πρόβλημα κατά το άνοιγμα του resource <b>" + file + "</b>");
    }
  }

  static public Vector loadFileLines(String file) throws Exception {
    Vector v = new Vector();
    BufferedReader f = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
    String s;
    while ((s = f.readLine()) != null)
      v.add(s);
    return v;
  }

  static public Vector loadFileLineObjects(Vector file, Class cls) {
    return loadFileLineObjects(file, 0, cls);
  }

  static public Vector loadFileLineObjects(Vector file, int start, Class cls) {
    Vector v = new Vector();
    try {
      for (int z = start; z < file.size(); z++) {
        FileLineData a = (FileLineData) cls.newInstance();
        a.load(file.elementAt(z).toString());
        if (a.isValid()) v.add(a);
      }
    } catch (Exception e) {
      JOptionPane.showMessageDialog(null, "<html>Τα δεδομένα είναι εσφαλμένα.<br>" +
                                    "Το μήνυμα λάθους είναι το παρακάτω:<br><b>" +
				    e.getMessage() + "</b></html>", "Πρόβλημα!",
				    JOptionPane.ERROR_MESSAGE);
    }
    return v;
  }

  static public void saveFileLines(String file, Vector lines) throws Exception {
    BufferedWriter f = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
    for (int z = 0; z < lines.size(); z++) {
      if (z != 0) f.newLine();
      Object o = lines.elementAt(z);
      if (o == null) f.write("");
      else if (o instanceof FileLineData) {
        FileLineData a = (FileLineData) o;
        if (a.isValid()) f.write(a.save());
      } else
        f.write(o.toString());
    }
    f.flush();
    f.close();
  }
}