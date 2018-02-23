package tables;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class PropertiesTable extends JTable {
	TableCellEditor edit;
	JTable vert;
	
	public PropertiesTable(PropertiesTableModel tcr, Component[] cmp) {
		super();
		
		if (cmp != null) edit = new RichTableCellEditor(cmp);
		
		TableColumnModel tcm = new DefaultTableColumnModel();
		TableColumn cr = new TableColumn(-1);
		cr.setHeaderValue(" ");
		tcm.addColumn(cr);
		vert = new JTable(tcr, tcm);
		vert.setBackground(getTableHeader().getBackground());
		vert.setForeground(getTableHeader().getForeground());
		
		setModel(tcr);
		setSelectionModel(vert.getSelectionModel());
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getTableHeader().setReorderingAllowed(false);
		if (tcr.getHeaders(true) == null) setTableHeader(null);
		for (int z = 0; z < this.getColumnCount(); z++)
			if (cmp != null) getColumnModel().getColumn(z).setCellEditor(edit);
	}
	
	public JTable getVerticalHeader() { return vert; }
	
	public static JScrollPane getScrolled(PropertiesTableModel tcr, Component[] cmp, int w) {
		return getScrolled(new PropertiesTable(tcr, cmp), w);
	}
	
	public static JScrollPane getScrolled(PropertiesTable jt, int w) {
		JViewport jv = new JViewport();
		jv.setView(jt.getVerticalHeader());
		jv.setPreferredSize(new Dimension(w, 100000));
		JScrollPane jsp = new JScrollPane(jt);
		jsp.setRowHeader(jv);
		return jsp;
	}
	
	public static Box getBoxed(PropertiesTable jt) {
		JTable jh = jt.getVerticalHeader();
		JPanel bv1 = new JPanel(new BorderLayout());
		bv1.add(jh, BorderLayout.SOUTH);
		bv1.setMaximumSize(new Dimension(10000, 10000));
		Box bv2 = Box.createVerticalBox();
		bv2.add(jt.getTableHeader());
		bv2.add(jt);
		Box bv3 = Box.createHorizontalBox();
		bv3.add(bv1);
		bv3.add(bv2);
		return bv3;
	}
}