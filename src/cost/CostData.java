package cost;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class CostData extends JPanel implements ActionListener, Hashing {
  protected static final String[] properties = { "Δγη διάθεσης", "Δγη ανάθεσης", "Ποσό", "ΕΦ",
      "ΚΑ", "Τίτλος", "Ημερομηνία υποβολής", "Πηγή χρημάτων",
      "Αξκος Έργου", "Πρόεδρος Αφανών Εργασιών",
      "Πρόεδρος Αγοράς Διάθεσης", "Α' Μέλος Αγοράς Διάθεσης", "Β' Μέλος Αγοράς Διάθεσης",
      "Πρόεδρος Ποιοτ. Ποσοτ. Παραλαβης", "Α' Μέλος Ποιοτ. Ποσοτ. Παραλαβης", "Β' Μέλος Ποιοτ. Ποσοτ. Παραλαβης",
      "Πρόεδρος Προσ. Οριστ. Παραλαβής", "Α' Μέλος Προσ. Οριστ. Παραλαβής", "Β' Μέλος Προσ. Οριστ. Παραλαβής",
      "Πρόεδρος Ζυγ. Υλικών Εμπορίου", "Μέλος Ζυγ. Υλικών Εμπορίου"
  };

  protected static final String[] hashKeys = {
      "approval_order", "my_order", "money", "ef", "ka", "title", "expire_date", "money_source",
      "work_officer", "proedros_afanwn",
      "proedros_agoras", "melos1_agoras", "melos2_agoras",
      "proedros_poiotikhs", "melos1_poiotikhs", "melos2_poiotikhs",
      "proedros_paralabhs", "melos1_paralabhs", "melos2_paralabhs",
      "proedros_zygisews", "melos_zygisews"
  };

  public static final int APROVAL_ORDER = 0;
  public static final int MY_ORDER = 1;
  public static final int MONEY = 2;
  public static final int EF = 3;
  public static final int KA = 4;
  public static final int TITLE = 5;
  public static final int EXPIRE_DATE = 6;
  public static final int MONEY_SOURCE = 7;
  public static final int WORK_OFFICER = 8;
  public static final int AFANWN_ERGASIWN_1 = 9;
  public static final int AGORA_DIA8ESH_1 = 10;
  public static final int AGORA_DIA8ESH_2 = 11;
  public static final int AGORA_DIA8ESH_3 = 12;
  public static final int POIOTIKH_POSOTIKH_1 = 13;
  public static final int POIOTIKH_POSOTIKH_2 = 14;
  public static final int POIOTIKH_POSOTIKH_3 = 15;
  public static final int PROSORINH_ORISTIKH_1 = 16;
  public static final int PROSORINH_ORISTIKH_2 = 17;
  public static final int PROSORINH_ORISTIKH_3 = 18;
  public static final int ZYGISH_EMPORIOY_1 = 19;
  public static final int ZYGISH_EMPORIOY_2 = 20;


  MainFrame main;

  String costFile;

  PropertiesTableModel propertiesModel = new PropertiesTableModel(properties, null);
  Component[][] cmp = new Component[properties.length][1];
  JTable propertiesTable;

  public CostData(MainFrame f) {
    main = f;

    fresh();

    for (int z = WORK_OFFICER; z <= ZYGISH_EMPORIOY_2; z++) cmp[z][0] = main.staticData.c;

    propertiesTable = new PropertiesTable(propertiesModel, 210, cmp);

    setLayout(new BorderLayout());
    add(new JScrollPane(propertiesTable), BorderLayout.CENTER);
    setVisible(true);
  }

  public void fresh() {
    if (propertiesModel.getData() != null) {
      int a = JOptionPane.showConfirmDialog(null,
                                            "Η τρέχουσα δαπάνη θα χαθεί.\r\nΝα συνεχίσω;",
                                            "Νέα Δαπάνη",
                                            JOptionPane.YES_NO_OPTION,
                                            JOptionPane.ERROR_MESSAGE);
      if (a == JOptionPane.NO_OPTION) return;
    }
    Object[][] data = new Object[properties.length][1];
    data[APROVAL_ORDER][0] = new OrderId();
    data[MY_ORDER][0] = new OrderId();
    data[MONEY][0] = new Digit(0, 2, false, true);
    propertiesModel.setData(data);
    main.bills.billsModel.setData(new Vector());
    main.bills.currentBill = -1;
    main.bills.billModel.setData(null);
    costFile = "Νέα Δαπάνη.cost";
  }

  public void load() {
    int a = JOptionPane.showConfirmDialog(null,
                                          "Η τρέχουσα δαπάνη θα χαθεί.\r\nΝα συνεχίσω;",
                                          "’νοιγμα Δαπάνης",
                                          JOptionPane.YES_NO_OPTION,
                                          JOptionPane.WARNING_MESSAGE);
    if (a == JOptionPane.NO_OPTION) return;
    JFileChooser fc = new JFileChooser(costFile);
    fc.setSelectedFile(new File(costFile));
    fc.setFileFilter(new ExtensionFileFilter("cost", "Αρχείο Δαπάνης"));
    int returnVal = fc.showOpenDialog(this);
    if(returnVal != JFileChooser.APPROVE_OPTION) return;
    String s = fc.getSelectedFile().getPath();
    loadFile(s);
    costFile = s;
  }

  public void loadFile(String file) {
    try {
      Vector v = LoadSaveFile.loadFileLines(file);
      costFile = file;
      main.staticData.load(v);
      load(v, main.staticData.properties.length);
      main.bills.billsModel.setData(
          LoadSaveFile.loadFileLineObjects(v, properties.length + main.staticData.properties.length,
                                           Bill.class)
          );
      main.bills.currentBill = -1;
      main.bills.billModel.setData(null);
    } catch (Exception e) {
      StaticFunctions.showExceptionMessage(e, "Σφάλμα κατά τη φόρτωση της Δαπάνης");
    }
  }

  public void load(Vector v, int start) {
    Object[][] data = new Object[properties.length][1];
    if (v.size() + 1 >= properties.length + start) {
      for (int z = 0; z < properties.length; z++)
        if (z <= MY_ORDER)
          data[z][0] = new OrderId(v.elementAt(z + start).toString());
        else if (z == MONEY)
          data[z][0] = new Digit(v.elementAt(z + start).toString(), 2, false, true);
        else if (z < WORK_OFFICER)
          data[z][0] = v.elementAt(z + start);
        else
          try {
            data[z][0] = new Man(v.elementAt(z + start).toString());
          } catch (Exception e) {
          }
      propertiesModel.setData(data);
    }
  }

  public boolean save() {
    JFileChooser fc = new JFileChooser(costFile);
    fc.setSelectedFile(new File(costFile));
    fc.setFileFilter(new ExtensionFileFilter("cost", "Αρχείο Δαπάνης"));
    int returnVal = fc.showSaveDialog(this);
    if(returnVal != JFileChooser.APPROVE_OPTION) return false;
    costFile = fc.getSelectedFile().getPath();
    if (!costFile.endsWith(".cost")) costFile += ".cost";

    Vector v = StaticFunctions.propertiesarray2vector(main.staticData.propertiesModel.getData());
    Vector v1 = StaticFunctions.propertiesarray2vector(propertiesModel.getData());
    for (int z = 0; z < v1.size(); z++)
      v.add(v1.elementAt(z));
    v1 = main.bills.billsModel.getData();
    for (int z = 0; z < v1.size(); z++)
      v.add(v1.elementAt(z));
    try {
      LoadSaveFile.saveFileLines(costFile, v);
      return true;
    } catch (Exception e) {
      StaticFunctions.showExceptionMessage(e, "Σφάλμα κατά τη αποθήκευση της Δαπάνης");
      return false;
    }
  }

  // ================================ Hashing ========================================== //

  public Object hash(String s) throws Exception {
    Object[][] data = propertiesModel.getData();
    for (int z = 0; z < data.length; z++)
      if (s.startsWith(hashKeys[z])) {
        if (data[z][0] == null) throw new Exception("Δεν αρχικοποιήθηκε η φράση <b>cost_" + s + "</b>");
        else if (s.length() == hashKeys[z].length()) return data[z][0];
        else if (z >= WORK_OFFICER || z <= MY_ORDER) return ( (Hashing) data[z][0]).hash(s.substring(1 + s.lastIndexOf('_')));

      }
    throw new Exception("Η κλάση <b>Cost</b> δεν υποστηρίζει τη φράση <b>" + s + "</b>");
  }

  // ================================ ActionListener ==================================== //

  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == main.openCost) load();
    else if (e.getSource() == main.saveCost) save();
    else if (e.getSource() == main.newCost) fresh();
    else if (e.getSource() == main.exit) main.dispatchEvent(new WindowEvent(main, WindowEvent.WINDOW_CLOSING));
      // bills array must have elements ######################### NOT FORGET!!! ##########
    else if (e.getSource() == main.exportBill)
      ExportReport.exportReport("plhrhs_apodei3h", main, null);
    else if (e.getSource() == main.exportCost)
      ExportReport.exportReport("cost", main, null);
    else if (e.getSource() == main.exportEpitropes)
      ExportReport.exportReport("my_order", main, null);
    else if (e.getSource() == main.exportDiabibastiko)
      ExportReport.exportReport("diabibastiko", main, null);
    else if (e.getSource() == main.exportProvider)
      ExportReport.exportReport("fe_provider", main, null);
    else if (e.getSource() == main.exportEforia)
      ExportReport.exportReport("fe_doy", main, null);
    else if (e.getSource() == main.exportReport)
      ExportReport.exportReport("report", main, null);
    else if (e.getSource() == main.exportHolds)
      ExportReport.exportReport("hold", main, null);
  }
}