package cost;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import tables.*;
import common.*;
import java.awt.event.*;

public class Contents extends JPanel implements DataTransmitter, ItemListener {
	private ResizableTableModel rtm;
	public Contents() {
		final String[] header = {"Δικαιολογητικό", "Πλήθος"};
		JTable tbl = new ResizableTable(rtm = new ResizableTableModel(this, header, header, ContentItem.class), true);
		final Byte[] many = {(byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5};
		JComboBox cbMany = new JComboBox(many);
		JComboBox cbContents = new JComboBox((Vector) load("all", ""));
		cbContents.setEditable(true);
		TableColumnModel cm = tbl.getColumnModel();
		cm.getColumn(0).setCellEditor(new DefaultCellEditor(cbContents));
		cm.getColumn(1).setCellEditor(new DefaultCellEditor(cbMany));
		setLayout(new BorderLayout());
		add(new JScrollPane(tbl));
		this.setFocusCycleRoot(true);
	}
	
	private Object load(String a, String b) {
		try {
			String[] args = {a, b};
			PhpScriptRunner php = new PhpScriptRunner(MainFrame.rootPath + "php/", "engine/contents.php", args);
			php.exec(null, php, null, false);
			return TreeFileLoader.load(php.getStdout());
		} catch(Exception e) {
			Functions.showExceptionMessage(this, e, "Πρόβλημα", "Πρόβλημα κατά την εκτέλεση ενός php script");
			return new VectorObject();
		}
	}

	public Object getData() {
		Cost c = (Cost) MainFrame.costs.get();
		return c == null ? null : c.get("ΦύλλοΚαταχώρησης");
	}
	
	public void paint(Graphics g) {
		rtm.fireTableRowsDeleted(10000, 10000);
		super.paint(g);
	}
	
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == e.DESELECTED) return;
		Cost c = (Cost) MainFrame.costs.get();
		String b = (String) c.get("ΤύποςΔιαγωνισμού");
		String a = (String) c.get("ΤύποςΔαπάνης");
		if (a != null && b != null && JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog(this, "<html>Αλλάξατε τον τύπο δαπάνης.<br>Να προσαρμόσω το φύλλο καταχώρησης;", "Αλλαγή Φύλλου Καταχώρησης", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE)) return;
		if (e.getItem().toString().indexOf("Διαγωνισμό") != -1) b = e.getItem().toString();
		else a = e.getItem().toString();
		c.put("ΦύλλοΚαταχώρησης", a != null && b != null ? load(a, b) : null);
	}
}