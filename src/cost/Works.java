package cost;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import tables.*;

public class Works extends JPanel implements DataTransmitter, ListSelectionListener {
	private ResizableTableModel matModel;
	private JTable tblWorks;
	private String cost;
	
	public Works() {
		final String[] commonHeader = { null, null, "Μονάδα μέτρησης"};
		
		ResizableTableModel workModel = new ResizableTableModel(this, new String[] { "Εργασία", "Ποσότητα", "ΜονάδαMέτρησης" }, commonHeader, Work.class);
		tblWorks = new ResizableTable(workModel, true);
		tblWorks.getSelectionModel().addListSelectionListener(this);
		tblWorks.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblWorks.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(Bills.cbMeasures));
		
		matModel = new ResizableTableModel((Vector) null, new String[] { "Υλικό", "Ποσότητα", "ΜονάδαMέτρησης"}, commonHeader, Material.class);
		JTable tblMaterial = new ResizableTable(matModel, false);
		tblMaterial.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(Bills.cbMeasures));
		
		setLayout(new BorderLayout());
		JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(tblWorks), new JScrollPane(tblMaterial));
		sp.setDividerSize(3);
		sp.setDividerLocation(75);
		add(sp);
	}
	
	public Object getData() {
		Cost c = (Cost) MainFrame.costs.get();
		return c == null ? null : c.get("Εργασίες");
	}
	
	public void valueChanged(ListSelectionEvent e) {
		int a = tblWorks.getSelectionModel().getLeadSelectionIndex();
		Vector v = (Vector) getData();
		matModel.setData(v == null || a < 0 || a >= v.size() || ((Map) v.get(a)) == null ? null : (Vector) ((Map) v.get(a)).get("Υλικά"));
	}
		
	public void paint(Graphics g) {
		if (cost != MainFrame.costs.getPos()) {
			cost = MainFrame.costs.getPos();
			valueChanged(null);
			((ResizableTableModel) tblWorks.getModel()).fireTableRowsDeleted(10000, 10000);
		}
		super.paint(g);
	}
}