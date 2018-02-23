package cost;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import common.*;
import tables.*;

public class Holds extends JPanel implements TableModelListener/*, KeyListener*/ {
  static final protected JComboBox holds = new JComboBox();
  private static final String[] header = { "ÌÔÓ", "ÔÁÓ", "ÅÌĞ", "ÔÓÌÅÄÅ", "ÁÏÏÁ", "ÕĞÊ", "ÔĞÅÄÅ", "ÅÊÏÅÌÓ",
      "×áñôüóçìï", "ÏÃÁ", "Óıíïëï" };
  private static HoldTableModel htm = new HoldTableModel();

	
	/*
	public void keyReleased(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {System.out.println(new Date());}
	public void keyTyped(KeyEvent e) {}
	*/
	
	
	
	
  public Holds() {
    setLayout(new BorderLayout());
    htm.addTableModelListener(this);
/*
		JTable jt = new JTable(htm);
		jt.addKeyListener(this);
		add(new JScrollPane(jt));
*/
		

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
      Object v = MainFrame.data.get("ÊñáôŞóåéò");
      if (!(v instanceof VectorObject))
				MainFrame.data.put("ÊñáôŞóåéò", v = new VectorObject());
      return (Vector) v;
    }
  }
}