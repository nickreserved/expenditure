package cost;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.DefaultCellEditor;
import javax.swing.JScrollPane;
import tables.ArrayTransmitter;
import tables.ResizableTableModel;
import javax.swing.JTable;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import tables.ResizableTable;

final public class Works extends JPanel implements ArrayTransmitter<Work>, ListSelectionListener {
	private final ResizableTableModel matModel;
	private final JTable tblWorks;
	private String cost;

	@SuppressWarnings("LeakingThisInConstructor")
	public Works() {
		final String[] commonHeader = { null, null, "Μονάδα μέτρησης"};

		ResizableTableModel workModel = new ResizableTableModel(this, new String[] { "Εργασία", "Ποσότητα", "ΜονάδαMέτρησης" }, commonHeader, Work.class);
		tblWorks = new ResizableTable(workModel, true, false);
		tblWorks.getSelectionModel().addListSelectionListener(this);
		tblWorks.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblWorks.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(Bills.cbMeasures));

		matModel = new ResizableTableModel((ArrayList) null, new String[] { "Υλικό", "Ποσότητα", "ΜονάδαMέτρησης"}, commonHeader, Material.class);
		JTable tblMaterial = new ResizableTable(matModel, false, true);
		tblMaterial.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(Bills.cbMeasures));

		setLayout(new BorderLayout());
		JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(tblWorks), new JScrollPane(tblMaterial));
		sp.setDividerSize(3);
		sp.setDividerLocation(75);
		add(sp);
	}

	// Throws if there is no current work selected
	public void addMaterialToCurrentWork(ArrayList<Material> mat) {
		((ArrayList<Material>) matModel.getData()).addAll(mat);
	}

  @Override
	public ArrayList<Work> getData() {
		Cost c = (Cost) MainFrame.costs.get();
		return c == null ? null : (ArrayList<Work>) c.get("Εργασίες");
	}

  @Override
	public void valueChanged(ListSelectionEvent e) {
		int a = tblWorks.getSelectionModel().getLeadSelectionIndex();
		ArrayList<Work> v = getData();
		matModel.setData(v == null || a < 0 || a >= v.size() || v.get(a) == null ? null : (ArrayList) ((Map) v.get(a)).get("Υλικά"));
	}

  @Override
	public void paint(Graphics g) {
		if (cost != MainFrame.costs.getPos()) {
			cost = MainFrame.costs.getPos();
			valueChanged(null);
			((ResizableTableModel) tblWorks.getModel()).fireTableRowsDeleted(10000, 10000);
		}
		super.paint(g);
	}
}