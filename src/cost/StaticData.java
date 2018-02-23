package cost;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class StaticData extends JPanel implements ActionListener, Hashing {

  protected static final String[] properties = { "Μονάδα (συντ.)", "Μονάδα (πλήρ.)", "Σχηματισμός",
      "Γραφείο (συντ.)", "Γραφείο (πληρ.)", "Ιδιότητα Αξκού", "Πόλη ή Χωρίο", "Επαρχία",
      "Διεύθυνση", "Τηλέφωνο (εξωτερικό)", "Τηλέφωνο (εσωτερικό)", "Τ.Κ.", "ΔΟΥ Στρατού", "ΑΦΜ",
      "Πρόχειρος Διαγωνισμός", "Δημόσιος Διαγωνισμός", "Δκτης", "ΕΟΥ", "Αξκος Γραφείου", "Δχστης"
  };

  protected static final String[] hashKeys = { "outfit_short", "outfit", "supervisor",
      "office_short", "office", "office_officer_speciality", "city", "province", "address",
      "phone_external", "phone_internal", "tk", "doy", "afm", "small_contest", "public_contest",
      "commander_", "eoy_", "office_officer_", "dxsths_"
  };

  public static final int OUTFIT_SHORT = 0;
  public static final int OUTFIT = 1;
  public static final int SUPERVISOR = 2;
  public static final int OFFICE_SHORT = 3;
  public static final int OFFICE = 4;
  public static final int OFFICE_OFFICER_SPECIALITY = 5;
  public static final int CITY = 6;
  public static final int PROVINCE = 7;
  public static final int ADDRESS = 8;
  public static final int PHONE_EXTERNAL = 9;
  public static final int PHONE_INTERNAL = 10;
  public static final int TK = 11;
  public static final int DOY = 12;
  public static final int AFM = 13;
  public static final int SMALL_CONTEST = 14;
  public static final int BIG_CONTEST = 15;
  public static final int COMMANDER = 16;
  public static final int EOY = 17;
  public static final int OFFICE_OFFICER = 18;
  public static final int DXSTHS = 19;

  protected static final String iniFile = "static.ini";

  JButton load = new JButton("Επανάκτηση");
  JButton save = new JButton("Αποθήκευση");

  PropertiesTableModel propertiesModel = new PropertiesTableModel(properties, null);
  JTable propertiesTable;
  JComboBox c = new JComboBox();


  public StaticData() {
    load.addActionListener(this);
    save.addActionListener(this);
    actionPerformed(null);

    Component[][] cmp = new Component[properties.length][1];
    propertiesTable = new PropertiesTable(propertiesModel, 130, cmp);
    for (int z = COMMANDER; z < properties.length; z++) cmp[z][0] = c;

    Box box_h = Box.createHorizontalBox();
    box_h.add(save);
    box_h.add(load);
    setLayout(new BorderLayout());
    add(new JScrollPane(propertiesTable), BorderLayout.CENTER);
    add(box_h, BorderLayout.SOUTH);
    setVisible(true);
  }

  public void setMen(Vector v) {
    c.removeAllItems();
    for (int z = 0; z < v.size(); z++)
      c.addItem(((Man) v.elementAt(z)).clone());
    c.addItem(null);
    c.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, 100000));
  }

  Object[][] fresh() {
    Object[][] data = new Object[properties.length][1];
    data[BIG_CONTEST][0] = new Digit(0, 2, false, true);
    data[SMALL_CONTEST][0] = new Digit(0, 2, false, true);
    if (propertiesModel.getData() == null) propertiesModel.setData(data);
    return data;
  }

  public void load(Vector v) throws Exception {
    Object[][] data = fresh();
    if (v.size() < properties.length)
      throw new Exception("Δώθηκαν λανθασμένα δεδομένα στην κλάση Static");
    for (int z = 0; z < properties.length; z++)
      if (z < SMALL_CONTEST)
        data[z][0] = v.elementAt(z);
      else if (z <= BIG_CONTEST)
        ((Digit) data[z][0]).setDigit(v.elementAt(z).toString());
      else
        data[z][0] = new Man(v.elementAt(z).toString());
    propertiesModel.setData(data);
  }

  public Vector save() {
    return StaticFunctions.propertiesarray2vector(propertiesModel.getData());
  }

  public Object hash(String s) throws Exception {
    Object[][] data = propertiesModel.getData();
    for (int z = 0; z < data.length; z++)
      if (s.startsWith(hashKeys[z])) {
       if (data[z][0] == null) throw new Exception("Δεν αρχικοποιήθηκε η φράση <b>" + s + "</b> στην κλάση <b>Static</b>");
       if (z >= COMMANDER)
         return ( (Man) data[z][0]).hash(s.substring(1 + s.lastIndexOf('_')));
       else if (s.length() == hashKeys[z].length())
         return data[z][0];
     }
    throw new Exception("<html>Η κλάση <b>Static</b> δεν υποστηρίζει τη φράση <b>" + s + "</b>");
  }


  // ================================ ActionListener ==================================== //

  public void actionPerformed(ActionEvent e) {
    try {
      if (e != null && e.getSource() == save)
        LoadSaveFile.saveFileLines(iniFile, save());
      load(LoadSaveFile.loadFileLines(iniFile));
    } catch (Exception ex) {
      fresh();
      StaticFunctions.showExceptionMessage(ex, "Σφάλμα κατά τη φόρτωση/αποθήκευση της κλάσης Static");
    }
  }
}