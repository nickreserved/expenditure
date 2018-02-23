package cost;

import javax.swing.*;
import java.util.*;

public class ExportReport {
  private ExportReport() {}

  static public void exportReport(String file, MainFrame m, String param) {
    try {
      HashTable ht = new HashTable(m, param);
      Vector v = new Vector();
      v.add(LoadSaveFile.loadFileToString("templates/header.template") +
            ht.openFile(file));
      JFileChooser fc = new JFileChooser();
      fc.setFileFilter(new ExtensionFileFilter("html", "Αρχείο Εξόδου"));
      int returnVal = fc.showSaveDialog(m);
      if(returnVal != JFileChooser.APPROVE_OPTION) return;
      String f = fc.getSelectedFile().getPath();
      if (!f.endsWith(".html")) f += ".html";
      LoadSaveFile.saveFileLines(f, v);
    } catch (Exception e) {
      StaticFunctions.showExceptionMessage(e, "Εξαγωγή Δαπάνης");
    }
  }
}