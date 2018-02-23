package cost;

import javax.swing.*;
import java.util.*;
import common.*;

public class ExportReport {
  private ExportReport() {}

  static public void exportReport(String file, Hashtable param) {
    try {
      HashTable ht = new HashTable(param);
      String s = LoadSaveFile.loadFileToString("templates/header.html") + ht.openFile(file);
      JFileChooser fc = new JFileChooser();
      fc.setFileFilter(new ExtensionFileFilter("html", "Αρχείο Εξόδου"));
      int returnVal = fc.showSaveDialog(null);
      if(returnVal != JFileChooser.APPROVE_OPTION) return;
      file = fc.getSelectedFile().getPath();
      if (!file.endsWith(".html")) file += ".html";
      LoadSaveFile.saveStringFile(file, s);
    } catch (Exception e) {
      Functions.showExceptionMessage(e, "Εξαγωγή Δαπάνης");
    }
  }
}