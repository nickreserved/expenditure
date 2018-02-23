package cost;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import javax.swing.table.*;

public class Holds extends JPanel implements ActionListener {
  protected static final String iniFile = "holds.ini";

  JButton load = new JButton("Επανάκτηση");
  JButton save = new JButton("Αποθήκευση");

  ResizableTableModel holdModel;
  MainFrame main;

  public Holds(MainFrame m) throws Exception {
    main = m;
    load.addActionListener(this);
    save.addActionListener(this);

    Box box_h = Box.createHorizontalBox();
    box_h.add(save);
    box_h.add(load);
    setLayout(new BorderLayout());
    holdModel = new HoldTableModel(null, Provider.header, Hold.class);
    actionPerformed(null);
    add(new JScrollPane(new JTable(holdModel)), BorderLayout.CENTER);
    add(box_h, BorderLayout.SOUTH);
    setVisible(true);
  }


  // ================================ ActionListener ==================================== //

  public void actionPerformed(ActionEvent e) {
    try {
      if (e != null && e.getSource() == save)
        LoadSaveFile.saveFileLines(iniFile, holdModel.getData());
      holdModel.setData(
          LoadSaveFile.loadFileLineObjects(LoadSaveFile.loadFileLines(iniFile), Hold.class));
      main.bills.setHolds(holdModel.getData());
    } catch(Exception ex) {
      StaticFunctions.showExceptionMessage(ex, "Σφάλμα κατά τη φόρτωση/αποθήκευση των Κρατήσεων");
    }
  }


  // =============================== HoldTableModel ===================================== //

  class HoldTableModel extends ResizableTableModel {
    public HoldTableModel(Vector data, String[] title, Class cls) {
      super(data, Hold.header, cls);
    }
    public boolean isCellEditable(int row, int col) {
      if (col < Hold.STAMP) return true; else return false;
    }
  }
}