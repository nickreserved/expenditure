package tables;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;

public class ResizableTable extends JTable
		implements KeyListener, MouseListener, Comparator<Map> {
	private final boolean insert;
	private String sort;
	public ResizableTable(ResizableTableModel rtm, boolean ins, boolean sort) {
		super(rtm);
		addKeyListener(this);
		insert = ins;
		if (sort) getTableHeader().addMouseListener(this);
	}
	@Override
	public void keyReleased(KeyEvent e) {}
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_INSERT && insert) {
			e.consume();
			ResizableTableModel rtm = (ResizableTableModel) getModel();
			ArrayList v = rtm.getData();
			int a = getSelectedRow();
			if (a >= 0 && a < v.size() && v.get(a) != null && (a == 0 || v.get(a - 1) != null)) {
				v.add(a, null);
				rtm.fireTableDataChanged();
			}
		} else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
			e.consume();
			ResizableTableModel rtm = (ResizableTableModel) getModel();
			ArrayList v = rtm.getData();
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
	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {
		int a = convertColumnIndexToModel(getTableHeader().columnAtPoint(e.getPoint()));
		sort = ((ResizableTableModel) getModel()).hash[a];
		Collections.sort(((ResizableTableModel) getModel()).getData(), this);
	}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public int compare(Map a, Map b) {
		Object aa = a.get(sort);
		Object bb = b.get(sort);
		if (aa instanceof Comparable)
			return bb != null && bb.getClass().equals(aa.getClass()) ?
				((Comparable) aa).compareTo((Comparable) bb) : 1;
		else
			return bb instanceof Comparable ? -1 : 0;
	}
}
