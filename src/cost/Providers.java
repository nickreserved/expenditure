package cost;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import common.*;
import tables.*;

public class Providers extends JPanel implements DataTransmitter, TableModelListener {
  static final protected JComboBox providers = new JComboBox();
  static private final String[] header = { "Επωνυμία", "ΑΦΜ", "ΔΟΥ", "Τηλέφωνο", "Τ.Κ.", "Πόλη", "Διεύθυνση"};
  private ResizableTableModel rtm = new ResizableTableModel(this, header, header, Provider.class);

  public Providers() {
    rtm.addTableModelListener(this);
    setLayout(new BorderLayout());
    add(new JScrollPane(new JTable(rtm)));
  }

  public void tableChanged(TableModelEvent e) {
    Vector v = (Vector) getData();
    providers.removeAllItems();
    for (int z = 0; z < v.size(); z++) providers.addItem(v.get(z));
    providers.addItem(null);
  }

  public Object getData() {
    if (!(MainFrame.data instanceof HashObject)) MainFrame.data = new HashObject();
    Object v = MainFrame.data.get("Προμηθευτές");
    if (!(v instanceof VectorObject))
      MainFrame.data.put("Προμηθευτές", v = new VectorObject());
    return v;
  }

  public void updateObject() {
    rtm.fireTableDataChanged();
    tableChanged(null);
  }
}