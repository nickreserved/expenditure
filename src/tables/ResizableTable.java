package tables;

import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;

public class ResizableTable extends JTable implements KeyListener {
	private final boolean insert;
	public ResizableTable(ResizableTableModel rtm, boolean ins) {
		super(rtm);
		addKeyListener(this);
		insert = ins;
	}
	public void keyReleased(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == e.VK_INSERT && insert) {
			e.consume();
			ResizableTableModel rtm = (ResizableTableModel) getModel();
			Vector v = rtm.getData();
			int a = getSelectedRow();
			if (a >= 0 && a < v.size() && v.get(a) != null && (a == 0 || v.get(a - 1) != null)) {
				v.insertElementAt(null, a);
				rtm.fireTableDataChanged();
			}
		} else if (e.getKeyCode() == e.VK_DELETE) {
			e.consume();
			ResizableTableModel rtm = (ResizableTableModel) getModel();
			Vector v = rtm.getData();
			int[] a = getSelectedRows();
			boolean chk = false;
			boolean yes = false;
			for (int z = a.length - 1; z >= 0; z--)
				if (a[z] >= 0 && a[z] < v.size()) {
					if (v.get(a[z]) != null && !chk) {
						chk = true;
						if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(this,
								"Υπάρχουν εγγραφές που δεν είναι άδειες. Θέλετε να τις διαγράψω;",
								"Διαγραφή εγγραφών", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE))
							yes = true;
					}
					if (v.get(a[z]) == null || yes) v.remove(a[z]);
				}
			rtm.fireTableDataChanged();
		}
	}
	public void keyTyped(KeyEvent e) {}
}
