package cost;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import tables.*;
import common.*;

public class Bills extends JPanel implements ListSelectionListener, DataTransmitter, TableModelListener {
	static protected JComboBox cbMeasures = new JComboBox(new String[] { "τεμάχια", "lt", "Kgr", "ton", "mm", "cm", "<html>cm<sup>2", "<html>cm<sup>3", "m", "<html>m<sup>2", "<html>m<sup>3", "ρολά", "πόδια", "λίβρες", "ζεύγη", "στρέμματα", "Km", "<html>Km<sup>2" });
	private ResizableTableModel billModel;
	private PropertiesTable tblSum;
	private JTable tblBills;
	private String cost;
	
	public Bills() {
		ResizableTableModel billsModel = new ResizableTableModel(this, new String[] { "Τιμολόγιο", "Τύπος", "Κατηγορία", "Προμηθευτής", "ΑνάλυσηΚρατήσεωνΣεΠοσοστά", "ΠοσοστόΦΕ" }, new String[] { null, null, null, null, "Κρατήσεις", "ΦΕ" }, Bill.class);
		billsModel.addTableModelListener(this);
		tblBills = new ResizableTable(billsModel, false, false);
		tblBills.getSelectionModel().addListSelectionListener(this);
		tblBills.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		TableColumnModel cm = tblBills.getColumnModel();
		cm.getColumn(1).setCellEditor(new DefaultCellEditor(new JComboBox(new String[] { "Τιμολόγιο", "ΣΠ/ΚΨΜ", "Δημόσιο", "Απόδειξη ενοικιασης" })));
		cm.getColumn(2).setCellEditor(new DefaultCellEditor(new JComboBox(new String[] { "Προμήθεια υλικών", "Παροχή υπηρεσιών", "Αγορά υγρών καυσίμων", "Τεχνικών έργων" })));
		cm.getColumn(3).setCellEditor(new DefaultCellEditor(Providers.providers));
		cm.getColumn(4).setCellEditor(new DefaultCellEditor(Holds.holds));
		cm.getColumn(5).setCellEditor(new DefaultCellEditor(new JComboBox(new Byte[] { 4, 8, 0, 1, 3 })));
		
		billModel = new ResizableTableModel((Vector) null, new String[] { "Είδος", "Ποσότητα", "ΤιμήΜονάδας", "ΣυνολικήΤιμή", "ΦΠΑ", "ΤιμήMονάδαςMεΦΠΑ", "ΣυνολικήΤιμήΜεΦΠΑ" ,"ΜονάδαMέτρησης"}, new String[] { null, null, "Τιμή μονάδας", "Συνολική τιμή", null, "Τιμή μονάδας με ΦΠΑ", "Συνολική τιμή με ΦΠΑ" ,"Μονάδα μέτρησης"}, BillItem.class);
		JTable billTable = new ResizableTable(billModel, true, true);
		cm = billTable.getColumnModel();
		JComboBox fpa = new JComboBox(new Byte[] { 19, 9, 0 });
		fpa.setEditable(true);
		cm.getColumn(4).setCellEditor(new DefaultCellEditor(fpa));
		cbMeasures.setEditable(true);
		cm.getColumn(7).setCellEditor(new DefaultCellEditor(cbMeasures));
		billModel.addTableModelListener(this);
		
		setLayout(new BorderLayout());
		JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(tblBills), new JScrollPane(billTable));
		sp.setDividerSize(3);
		sp.setDividerLocation(75);
		add(sp, BorderLayout.CENTER);
		add(PropertiesTable.getBoxed(tblSum = new PropertiesTable(new ReportTableModel(), null)), BorderLayout.SOUTH);
	}
	
	public Object getData() {
		Cost c = (Cost) MainFrame.costs.get();
		return c == null ? null : c.get("Τιμολόγια");
	}
	
	public void tableChanged(TableModelEvent e) {
		Vector v = (Vector) getData();
		int cb = tblBills.getSelectionModel().getLeadSelectionIndex();
		if (v == null || cb == -1 || cb >= v.size()) return;
		((Bill) v.get(cb)).recalculate();
		repaint();
	}
	
	public void valueChanged(ListSelectionEvent e) {
		int a = tblBills.getSelectionModel().getLeadSelectionIndex();
		Vector v = (Vector) getData();
		billModel.setData(a < 0 || a >= v.size() ? null : (Vector) ((Map) v.get(a)).get("Είδη"));
	}
	
	public void paint(Graphics g) {
		if (cost != MainFrame.costs.getPos()) {
			cost = MainFrame.costs.getPos();
			valueChanged(null);
			((ResizableTableModel) tblBills.getModel()).fireTableRowsDeleted(10000, 10000);
		}
		super.paint(g);
	}
	
	
	private class ReportTableModel extends PropertiesTableModel {
		public ReportTableModel() {
			super(new String[] { "ΚαθαρήΑξία", "ΚατηγορίεςΦΠΑ", "Καταλογιστέο", "ΑνάλυσηΚρατήσεωνΣεΕυρώ", "Πληρωτέο", "ΦΕΣεΕυρώ", "ΥπόλοιποΠληρωτέο", null, null, null, null, null, null, null }, null, new String[] { "Καθαρή Αξία", "ΦΠΑ", null, "Κρατήσεις", null, "ΦΕ", "Υπόλοιπο" }, new String[] { "Τρέχον Τιμολόγιο", "Όλα τα Τιμολόγια" });
		}
		public boolean isCellEditable(int row, int col) { return false; }
		public Object getValueAt(int row, int col) {
			try {
				Object t, o = null;
				VectorObject bv = (VectorObject) ((ResizableTableModel) tblBills.getModel()).getData();
				int cb = tblBills.getSelectionModel().getLeadSelectionIndex();
				switch (col) {
					case -1: return super.getValueAt(row, col);
					case 0:
						if (cb == -1 || cb >= bv.size()) return null;
						o = ((Bill) bv.get(cb)).get(getHash()[row]);
						if ((row == 1 || row == 3) && o != null)
							o = ((Map) o).get("Σύνολο");
						break;
					case 1:
						o = 0d;
						for (int z = 0; z < bv.size(); z++) {
							t = ((Bill) bv.get(z)).get(getHash()[row]);
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