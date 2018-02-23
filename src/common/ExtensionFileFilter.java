package common;

import java.io.*;
import java.util.*;

public class ExtensionFileFilter extends javax.swing.filechooser.FileFilter {
  String s = "";
  String t = "";

  public ExtensionFileFilter() {}
  public ExtensionFileFilter(String s, String title) { setExtensions(s, title); }

  public void setExtensions(String s, String title) {
    this.s = s.toLowerCase();
    t = title;
  }
  public String getDescription() { return t; }

  public boolean accept(File f) {
    if (f.isDirectory()) return true;
    String file = f.getName().toLowerCase();
    if (s.length() == 0) return true;
    String[] d = s.split(":");
    for (int z = 0; z < d.length; z++)
      if (file.endsWith(d[z])) return true;
    return false;
  }
}