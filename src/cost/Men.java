package cost;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Men extends JPanel implements ActionListener {
  protected static final String iniFile = "men.ini";

  JButton load = new JButton("Επανάκτηση");
  JButton save = new JButton("Αποθήκευση");

  ResizableTableModel menModel;
  MainFrame main;

  public Men(MainFrame m) throws Exception {
    main = m;
    load.addActionListener(this);
    save.addActionListener(this);

    Box box_h = Box.createHorizontalBox();
    box_h.add(save);
    box_h.add(load);
    setLayout(new BorderLayout());
    menModel = new ResizableTableModel(null, Man.header, Man.class);
    actionPerformed(null);
    add(new JScrollPane(new JTable(menModel)), BorderLayout.CENTER);
    add(box_h, BorderLayout.SOUTH);
    setVisible(true);
  }


  // ================================ ActionListener ==================================== //

  public void actionPerformed(ActionEvent e) {
    try {
      if (e != null && e.getSource() == save)
        LoadSaveFile.saveFileLines(iniFile, menModel.getData());
      menModel.setData(
          LoadSaveFile.loadFileLineObjects(LoadSaveFile.loadFileLines(iniFile), Man.class));
      main.staticData.setMen(menModel.getData());
    } catch(Exception ex) {
      StaticFunctions.showExceptionMessage(ex, "Σφάλμα κατά τη φόρτωση/αποθήκευση του Προσωπικού");
    }
  }
}