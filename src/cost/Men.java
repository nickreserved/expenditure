package cost;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import common.*;
import tables.*;

public class Men extends JPanel implements DataTransmitter, TableModelListener {
  static final protected JComboBox men = new JComboBox();
  static private final String[] header = { "Βαθμός", "Ονοματεπώνυμο", "Μονάδα" };
  private ResizableTableModel rtm = new ResizableTableModel(this, header, header, Man.class);

  public Men() {
    rtm.addTableModelListener(this);
    setLayout(new BorderLayout());
    add(new JScrollPane(new JTable(rtm)));
  }

  public Object getData() {
    if (!(MainFrame.data instanceof HashObject)) MainFrame.data = new HashObject();
    Object v = MainFrame.data.get("Προσωπικό");
    if (!(v instanceof VectorObject))
      MainFrame.data.put("Προσωπικό", v = new VectorObject());
    return v;
  }

  public void tableChanged(TableModelEvent e) {
    Vector v = (Vector) getData();
    men.removeAllItems();
    for (int z = 0; z < v.size(); z++) men.addItem(v.get(z));
    men.addItem(null);
  }

  public void updateObject() {
    rtm.fireTableDataChanged();
    tableChanged(null);
  }
}