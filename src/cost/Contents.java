package cost;

import common.PhpScriptRunner;
import common.TreeFileLoader;
import common.VectorObject;
import static cost.MainFrame.showExceptionMessage;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import tables.ArrayTransmitter;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;
import tables.ResizableTable;
import tables.ResizableTableModel;

final public class Contents extends JPanel implements ArrayTransmitter<ContentItem>, ItemListener {
	private final ResizableTableModel rtm;
	public Contents() {
		JTable tbl = new ResizableTable(rtm = new ResizableTableModel(this, new String[] {"Δικαιολογητικό", "Πλήθος"}, null, ContentItem.class), true, false);
		JComboBox cbMany = new JComboBox(new Byte[] {1, 2, 3, 4, 5});
		JComboBox cbContents = new JComboBox(((ArrayList) load(-1)).toArray());
		cbContents.setEditable(true);
		TableColumnModel cm = tbl.getColumnModel();
		cm.getColumn(0).setCellEditor(new DefaultCellEditor(cbContents));
		cm.getColumn(1).setCellEditor(new DefaultCellEditor(cbMany));
		setLayout(new BorderLayout());
		add(new JScrollPane(tbl));
		this.setFocusCycleRoot(true);
	}

	private Object load(int a) {
		try {
			PhpScriptRunner php = new PhpScriptRunner(MainFrame.rootPath + "php/", "engine/contents.php", new String[] { Integer.toString(a) });
			return TreeFileLoader.load(php.exec(null, PhpScriptRunner.STDOUT).stdout);
		} catch(Exception e) {
			showExceptionMessage(this, e, "Πρόβλημα", "Πρόβλημα κατά την εκτέλεση ενός php script");
			return new VectorObject();
		}
	}

	@Override
	public ArrayList<ContentItem> getData() {
		Cost c = (Cost) MainFrame.costs.get();
		return c == null ? null : (ArrayList<ContentItem>) c.get("ΦύλλοΚαταχώρησης");
	}

	@Override
	public void paint(Graphics g) {
		rtm.fireTableRowsDeleted(10000, 10000);
		super.paint(g);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.DESELECTED) return;
		Cost c = (Cost) MainFrame.costs.get();
		String b = (String) c.get("ΤύποςΔιαγωνισμού");
		String a = (String) c.get("ΤύποςΔαπάνης");
		if (a != null && b != null && JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog(this, "<html>Αλλάξατε τον τύπο δαπάνης.<br>Να προσαρμόσω το φύλλο καταχώρησης;", "Αλλαγή Φύλλου Καταχώρησης", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE)) return;
		if (e.getItem().toString().contains("Διαγωνισμό")) b = e.getItem().toString();
		else a = e.getItem().toString();
		c.put("ΦύλλοΚαταχώρησης", a != null && b != null ?
			load(1 - Arrays.binarySearch(CostData.COST, a) + 2 * (2 - Arrays.binarySearch(CostData.CONTEST, b))) : null);
	}
}