package cost;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import tables.*;
import common.*;

public class Bills extends JPanel implements ListSelectionListener, DataTransmitter,
    TableModelListener {
  private static final String[] itemHeader = { "Είδος", "Ποσότητα", "Τιμή μονάδας", "Συνολική τιμή",
      "ΦΠΑ", "Τιμή μονάδας με ΦΠΑ", "Συνολική τιμή με ΦΠΑ" ,"Μονάδα μέτρησης"};
  private static final String[] itemHash = { "Είδος", "Ποσότητα", "ΤιμήΜονάδας", "ΣυνολικήΤιμή",
      "ΦΠΑ", "ΤιμήMονάδαςMεΦΠΑ", "ΣυνολικήΤιμήΜεΦΠΑ" ,"ΜονάδαMέτρησης"};

  protected static final String[] measures = { "τεμάχια", "lt", "Kgr", "cm", "cm^2", "cm^3", "m", "m^2",
      "m^3", "ρολά", "πόδια", "λίβρες", "ζεύγη", "στρέμματα", "Km", "Km^2" };
  protected static final Number[] fpaList = { new Byte((byte) 36), new Byte((byte) 19), new Byte((byte) 9), new Byte((byte) 0) };

  private static final String[] billHeader = { "Τιμολόγιο", "Τύπος", "Κατηγορία", "Προμηθευτής",
      "Κρατήσεις", "ΦΕ" };
  private static final String[] billHash = { "Τιμολόγιο", "Τύπος", "Κατηγορία", "Προμηθευτής",
      "ΑνάλυσηΚρατήσεωνΣεΠοσοστά", "ΠοσοστόΦΕ" };

  protected static final Number[] feList = { new Byte((byte) 8), new Byte((byte) 4), new Byte((byte) 3), new Byte((byte) 1), new Byte((byte) 0) };
  private static final String[] billTypes = { "Τιμολόγιο", "ΣΠ/ΚΨΜ", "Δημόσιο" };
  private static final String[] categories = { "Προμήθεια υλικών", "Παροχή υπηρεσιών", "Αγορά υγρών καυσίμων" };

  private static int currentBill = -1;
  
  private static ResizableTableModel billsModel;
  private static ResizableTableModel billModel;
  private static PropertiesTableModel propertiesModel;
  private static JTable billsTable;
  
  protected JComboBox cbMeasures = new JComboBox(measures);
  
	public Bills() {
		billsModel = new ResizableTableModel(this, billHash, billHeader, Bill.class);
		billsTable = new JTable(billsModel);
		billsTable.getSelectionModel().addListSelectionListener(this);
		billsModel.addTableModelListener(this);
		billsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		TableColumnModel cm = billsTable.getColumnModel();
		cm.getColumn(1).setCellEditor(new DefaultCellEditor(new JComboBox(billTypes)));
		cm.getColumn(2).setCellEditor(new DefaultCellEditor(new JComboBox(categories)));
		cm.getColumn(3).setCellEditor(new DefaultCellEditor(Providers.providers));
		cm.getColumn(4).setCellEditor(new DefaultCellEditor(Holds.holds));
		cm.getColumn(5).setCellEditor(new DefaultCellEditor(new JComboBox(feList)));
		
		billModel = new ResizableTableModel((Vector) null, itemHash, itemHeader, BillItem.class);
		JTable billTable = new JTable(billModel);
		cm = billTable.getColumnModel();
		JComboBox fpa = new JComboBox(fpaList);
		fpa.setEditable(true);
		cm.getColumn(4).setCellEditor(new DefaultCellEditor(fpa));
		cm.getColumn(7).setCellEditor(new DefaultCellEditor(cbMeasures));
		billModel.addTableModelListener(this);
		cbMeasures.setEditable(true);

		setLayout(new BorderLayout());
		JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				new JScrollPane(billsTable),
				new JScrollPane(billTable));
		sp.setDividerSize(3);
		sp.setDividerLocation(75);
		add(sp, BorderLayout.CENTER);
		add(PropertiesTable.getBoxed(new PropertiesTable(propertiesModel = new ReportTableModel(), null)), BorderLayout.SOUTH);
	}
	
	public Object getData() {
		if (MainFrame.currentCost == null) return null;
		Object o = MainFrame.costs.get(MainFrame.currentCost);
		if (!(o instanceof Dictionary)) return null;
		Object a = ((Dictionary) o).get("Τιμολόγια");
		if (!(a instanceof VectorObject)) {
			((Dictionary) o).put("Τιμολόγια", a = new VectorObject());
			billsModel.fireTableDataChanged();
		}
		return a;
	}
	
	public void tableChanged(TableModelEvent e) {
		Vector v = (Vector) getData();
		if (v == null || currentBill == -1 || currentBill >= v.size()) return;
		((Bill) v.get(currentBill)).recalculate();
		repaint();
	}
	
	public void valueChanged(ListSelectionEvent e) {
		int a = billsTable.getSelectedRow();
		Vector v = (Vector) getData();
		if (a != -1) {
			if (v.size() > a) {
				currentBill = a;
				billModel.setData((Vector) ((Bill) v.get(a)).get("Είδη"));
			} else {
				currentBill = -1;
				billModel.setData((Vector) null);
			}
		}
		propertiesModel.fireTableDataChanged();
	}
	
	public void updateObject() {
		currentBill = -1;
		billModel.setData((Vector) null);
		billsModel.fireTableDataChanged();
		propertiesModel.fireTableDataChanged();
	}
	
	
	// =============================== ReportTableModel ===================================== //
	
	protected static class ReportTableModel extends PropertiesTableModel {
		final static String[] hHdr = { "Τρέχον Τιμολόγιο", "Όλα τα Τιμολόγια" };
		final static String[] hash = { "ΚαθαρήΑξία", "ΚατηγορίεςΦΠΑ", "Καταλογιστέο", "ΑνάλυσηΚρατήσεωνΣεΕυρώ", "Πληρωτέο",
				"ΦΕΣεΕυρώ", "ΥπόλοιποΠληρωτέο", null, null, null, null, null, null, null };
		final static String[] vHdr = { "Καθαρή Αξία", "ΦΠΑ", null, "Κρατήσεις", null, "ΦΕ", "Υπόλοιπο" };

		public ReportTableModel() { super(hash, null, vHdr, hHdr); }
		public boolean isCellEditable(int row, int col) { return false; }
		public Object getValueAt(int row, int col) {
			try {
				Object t, o = null;
				VectorObject bv = (VectorObject) billsModel.getData();
				switch (col) {
					case -1: return super.getValueAt(row, col);
					case 0:
						if (currentBill == -1 || currentBill >= bv.size()) return null;
						o = ((Bill) bv.get(currentBill)).get(hash[row]);
						if ((row == 1 || row == 3) && o != null)
							o = ((Map) o).get("Σύνολο");
						break;
					case 1:
						o = new Double(0);
						for (int z = 0; z < bv.size(); z++) {
							t = ((Bill) bv.get(z)).get(hash[row]);
							if ((row == 1 || row == 3) && t != null)
								t = ((Map) t).get("Σύνολο");
							o = M.round(M.add((Number) o, (Number) t), 2);
						}
				}
				if (!(o instanceof Number) || ((Number) o).doubleValue() != 0) return o;
			} catch(Exception e) {}
			return null;
		}
	}
}