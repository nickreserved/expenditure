package cost;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import tables.*;
import common.*;

public class Bills extends JPanel implements ListSelectionListener, DataTransmitter, TableModelListener {
	private ResizableTableModel billModel;
	private PropertiesTable tblSum;
	private JTable tblBills;
	private String cost;
	
	public Bills() {
		final String[] itemHeader = { null, null, "Τιμή μονάδας", "Συνολική τιμή", null, "Τιμή μονάδας με ΦΠΑ", "Συνολική τιμή με ΦΠΑ" ,"Μονάδα μέτρησης"};
		final String[] itemHash = { "Είδος", "Ποσότητα", "ΤιμήΜονάδας", "ΣυνολικήΤιμή", "ΦΠΑ", "ΤιμήMονάδαςMεΦΠΑ", "ΣυνολικήΤιμήΜεΦΠΑ" ,"ΜονάδαMέτρησης"};
		
		final String[] measures = { "τεμάχια", "lt", "Kgr", "cm", "cm^2", "cm^3", "m", "m^2", "m^3", "ρολά", "πόδια", "λίβρες", "ζεύγη", "στρέμματα", "Km", "Km^2" };
		final Number[] fpaList = { new Byte((byte) 36), new Byte((byte) 19), new Byte((byte) 9), new Byte((byte) 0) };
		
		final String[] billHeader = { null, null, null, null, "Κρατήσεις", "ΦΕ" };
		final String[] billHash = { "Τιμολόγιο", "Τύπος", "Κατηγορία", "Προμηθευτής", "ΑνάλυσηΚρατήσεωνΣεΠοσοστά", "ΠοσοστόΦΕ" };
		
		final Number[] feList = { new Byte((byte) 8), new Byte((byte) 4), new Byte((byte) 3), new Byte((byte) 1), new Byte((byte) 0) };
		final String[] billTypes = { "Τιμολόγιο", "ΣΠ/ΚΨΜ", "Δημόσιο" };
		final String[] categories = { "Προμήθεια υλικών", "Παροχή υπηρεσιών", "Αγορά υγρών καυσίμων" };
		
		ResizableTableModel billsModel = new ResizableTableModel(this, billHash, billHeader, Bill.class);
		billsModel.addTableModelListener(this);
		tblBills = new ResizableTable(billsModel, false);
		tblBills.getSelectionModel().addListSelectionListener(this);
		tblBills.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		TableColumnModel cm = tblBills.getColumnModel();
		cm.getColumn(1).setCellEditor(new DefaultCellEditor(new JComboBox(billTypes)));
		cm.getColumn(2).setCellEditor(new DefaultCellEditor(new JComboBox(categories)));
		cm.getColumn(3).setCellEditor(new DefaultCellEditor(Providers.providers));
		cm.getColumn(4).setCellEditor(new DefaultCellEditor(Holds.holds));
		cm.getColumn(5).setCellEditor(new DefaultCellEditor(new JComboBox(feList)));
		
		billModel = new ResizableTableModel((Vector) null, itemHash, itemHeader, BillItem.class);
		JTable billTable = new ResizableTable(billModel, true);
		cm = billTable.getColumnModel();
		JComboBox fpa = new JComboBox(fpaList);
		fpa.setEditable(true);
		cm.getColumn(4).setCellEditor(new DefaultCellEditor(fpa));
		JComboBox cbMeasures = new JComboBox(measures);
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
			final String[] hHdr = { "Τρέχον Τιμολόγιο", "Όλα τα Τιμολόγια" };
			final String[] hsh = { "ΚαθαρήΑξία", "ΚατηγορίεςΦΠΑ", "Καταλογιστέο", "ΑνάλυσηΚρατήσεωνΣεΕυρώ", "Πληρωτέο", "ΦΕΣεΕυρώ", "ΥπόλοιποΠληρωτέο", null, null, null, null, null, null, null };
			final String[] vHdr = { "Καθαρή Αξία", "ΦΠΑ", null, "Κρατήσεις", null, "ΦΕ", "Υπόλοιπο" };
			hHeader = hHdr; hash = hsh; vHeader = vHdr;
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
						o = ((Bill) bv.get(cb)).get(hash[row]);
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