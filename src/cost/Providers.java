package cost;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Providers extends JPanel implements ActionListener {
  protected static final String iniFile = "providers.ini";

  JButton load = new JButton("Επανάκτηση");
  JButton save = new JButton("Αποθήκευση");

  ResizableTableModel providersModel;
  MainFrame main;

  public Providers(MainFrame m) throws Exception {
    main = m;
    load.addActionListener(this);
    save.addActionListener(this);

    Box box_h = Box.createHorizontalBox();
    box_h.add(save);
    box_h.add(load);
    setLayout(new BorderLayout());
    providersModel = new ResizableTableModel(null, Provider.header, Provider.class);
    actionPerformed(null);
    add(new JScrollPane(new JTable(providersModel)), BorderLayout.CENTER);
    add(box_h, BorderLayout.SOUTH);
    setVisible(true);
  }


  // ================================ ActionListener ==================================== //

  public void actionPerformed(ActionEvent e) {
    try {
      if (e != null && e.getSource() == save)
        LoadSaveFile.saveFileLines(iniFile, providersModel.getData());
      providersModel.setData(
          LoadSaveFile.loadFileLineObjects(LoadSaveFile.loadFileLines(iniFile), Provider.class));
      main.bills.setProviders(providersModel.getData());
    } catch(Exception ex) {
      StaticFunctions.showExceptionMessage(ex, "Σφάλμα κατά τη φόρτωση/αποθήκευση των Προμηθευτών");
    }
  }
}