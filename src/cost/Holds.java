package cost;

import java.util.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

import common.*;
import tables.*;

public class Holds extends JPanel implements TableModelListener {
  static final protected JComboBox holds = new JComboBox();
  private static final String[] header = { "ΜΤΣ", "ΤΑΣ", "ΕΜΠ", "ΤΣΜΕΔΕ", "ΑΟΟΑ", "ΥΠΚ", "ΤΠΕΔΕ", "ΕΚΟΕΜΣ",
      "Χαρτόσημο", "ΟΓΑ", "Σύνολο" };
  private static HoldTableModel htm = new HoldTableModel();

  public Holds() {
    setLayout(new BorderLayout());
    htm.addTableModelListener(this);
    add(new JScrollPane(new JTable(htm)));
  }

  public void tableChanged(TableModelEvent e) {
    Vector v = htm.getData();
    holds.removeAllItems();
    for (int z = 0; z < v.size(); z++) holds.addItem(v.get(z));
    holds.addItem(new Hold());
  }

  public void updateObject() {
    htm.fireTableDataChanged();
    tableChanged(null);
  }


  // =============================== HoldTableModel ===================================== //

  protected static class HoldTableModel extends ResizableTableModel {
    public HoldTableModel() { super((Vector) null, header, header, Hold.class); }
    public boolean isCellEditable(int row, int col) { return col < header.length - 1 ? true : false; }
    public Vector getData() {
      if (!(MainFrame.data instanceof HashObject)) MainFrame.data = new HashObject();
      Object v = MainFrame.data.get("Κρατήσεις");
      if (!(v instanceof VectorObject))
	MainFrame.data.put("Κρατήσεις", v = new VectorObject());
      return (Vector) v;
    }
  }
}