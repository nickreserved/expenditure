package cost;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.event.*;

public class Works extends JPanel/* implements ListSelectionListener*/ {
/*
  MainFrame main;
  protected static final String[] header = { "Εργασία", "Μονάδα μέτρησης", "Ποσότητα"};
  protected static final String[] header = { "Υλικό", "Μονάδα μέτρησης", "Ποσότητα", "Σύνολο"};

  int currentWork = -1;

  ResizableTableModel worksModel;
  ResizableTableModel materialModel = new MaterialTableModel();
  JTable worksTable;

  JComboBox cbMaterials = new JComboBox();

  public Works(MainFrame m) throws Exception {
    main = m;

    cbMaterials.setEditable(true);
    cbMaterials.setBorder(BorderFactory.createEmptyBorder(-1, 0, -2, 0));
    cbMaterials.setFont(new Font(null, 0, 12));

    worksModel = new ResizableTableModel(new Vector(), Work.header, Work.class);
    worksTable = new JTable(worksModel);
    worksTable.getSelectionModel().addListSelectionListener(this);
    worksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    TableColumnModel cm = worksTable.getColumnModel();
    cm.getColumn(Work.MEASURE).setCellEditor(new DefaultCellEditor(m.bills.cbMeasures));
    JTable materialTable = new JTable(materialModel);
    cm = materialTable.getColumnModel();
    cm.getColumn(Material.NAME).setCellEditor(new DefaultCellEditor(cbMaterials));
    cm.getColumn(Material.MEASURE).setCellEditor(new DefaultCellEditor(m.bills.cbMeasures));

    setLayout(new BorderLayout());
    JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				   new JScrollPane(worksTable),
				   new JScrollPane(materialTable));
    sp.setDividerSize(3);
    sp.setDividerLocation(75);
    add(sp, BorderLayout.CENTER);
    setVisible(true);
  }

  // set in material combo box strings for every active material
  void setMaterialCombo() {
    Vector v = (Vector) AnalyzeWorks.getSumFromWorks(worksModel.getData(), AnalyzeWorks.WORKS_ALL_MATERIALS);
    cbMaterials.removeAllItems();
    for (int z = 0; z < v.size(); z++)
      cbMaterials.addItem(v.elementAt(z).toString());
  }

  protected Vector searchMaterial(String n) {
    Vector v = new Vector();
    Vector vw = worksModel.getData();
    for (int z = 0; z < vw.size(); z++) {
      Work w = (Work) vw.elementAt(z);
      for (int y = 0; y < w.materials.size(); y++) {
	Material m = (Material) w.materials.elementAt(y);
	if (m.name.trim().equalsIgnoreCase(n.trim())) v.add(m);
      }
    }
    return v;
  }

  protected Digit addMaterial(Vector v) {
    Digit d = new Digit(0, 6, true, true);
    for (int z = 0; z < v.size(); z++) {
      Material w = (Material) v.elementAt(z);
      d.add((Digit) w.getCell(Material.MANY));
    }
    return d;
  }

  static protected void changeMaterial(Vector v, Object obj, int col) {
    for (int z = 0; z < v.size(); z++) {
      Material m = (Material) v.elementAt(z);
      m.setCell(obj, col);
    }
  }

  protected void optimizeWorksMaterials() {
    Vector v = worksModel.getData();
    for (int z = 0; z < v.size(); z++) {
      Work w = (Work) v.elementAt(z);
      Vector a = new Vector(1);
      a.add(w);
      w.materials = (Vector) AnalyzeWorks.getSumFromWorks(a, AnalyzeWorks.WORKS_ALL_MATERIALS);
    }
  }

  // ============================ ListSelectionListener ================================= //

  public void valueChanged(ListSelectionEvent e) {
    int a = worksTable.getSelectedRow();
    if (a != -1) {
      Vector v = worksModel.getData();
      if (v.size() > a) {
	currentWork = a;
	Work b = (Work) v.elementAt(a);
	optimizeWorksMaterials();
	materialModel.setData((Vector) b.getCell(Work.MATERIALS));
      } else {
	currentWork = -1;
	materialModel.setData(null);
      }
    }
  }


  // =============================== ReportTableModel ===================================== //

  protected class MaterialTableModel extends ResizableTableModel {
    public MaterialTableModel() { super(null, Material.header, Material.class); }
    public boolean isCellEditable(int row, int col) { return col != 3; }
    public Object getValueAt(int row, int col) {
      if (col != 3) return super.getValueAt(row, col);
      Object o = getValueAt(row, Material.NAME);
      if (o != null) return addMaterial(searchMaterial(o.toString()));
      else return null;
    }
    public void setValueAt(Object obj, int row, int col) {
      if (col == Material.NAME && obj != null && !obj.equals("")) {
	Object old = getValueAt(row, col);
	Object mes = null;
	Vector vm = searchMaterial(obj.toString());
	if (vm.size() > 0) mes = ((Material) vm.elementAt(0)).getCell(Material.MEASURE);
	if (old != null && !old.equals("")) {
	  if (obj != null && old.toString().equals(obj.toString())) return;
	  Vector v = searchMaterial(old.toString());
	  if (v.size() > 1) {
	    int a = JOptionPane.showConfirmDialog(null, "<html>Έχουν καταχωρηθεί πολλά υλικά με όνομα <b>" + old +
						  "</b><br>Θέλετε η αλλαγή να γίνει σε όλα;", "Μαζική αλλαγή",
						  JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
	    if (a == JOptionPane.CANCEL_OPTION) return;
	    if (a == JOptionPane.YES_OPTION) changeMaterial(v, obj, col);
	  }
	}
	super.setValueAt(obj, row, col);
	if (mes != null) super.setValueAt(mes, row, Material.MEASURE);
	setMaterialCombo();
	return;
      } else if (col == Material.MEASURE) {
	Object o = getValueAt(row, Material.NAME);
	if (o instanceof String) {
	  Vector v = searchMaterial(o.toString());
	  if (v.size() > 1) {
	    changeMaterial(v, obj, col);
	    fireTableDataChanged();
	    return;
	  }
	}
      }
      super.setValueAt(obj, row, col);
    }
  }*/
}