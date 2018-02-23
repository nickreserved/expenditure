package cost;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import tables.*;

public class Holds extends JPanel implements DataTransmitter, ActionListener {

	static protected JComboBox holds;
	protected ResizableTable table;

	public Holds() {
		holds = new JComboBox(new ComboDataModel(this, new Hold()));

		ArrayList<String> heads = new ArrayList<>(Arrays.asList(new String[]
			{"Σύνολο", "ΜΤΣ", "Χαρτόσημο", "ΟΓΑ", "ΕΑΑΔΗΣΥ"} ));
		ArrayList<Hold> lst = (ArrayList<Hold>) getData();
		for (HashMap z : lst) {
			Set<String> hold = z.keySet();
			for (String head : hold)
				if (!heads.contains(head)) heads.add(head);
		}
		String[] hash = new String[heads.size()];
		hash = heads.toArray(hash);

		setLayout(new BorderLayout());
		add(new JScrollPane(table = new ResizableTable(new ResizableTableModel(this, hash, null, Hold.class), false, true)));

		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem m = new JMenuItem("Προσθήκη στήλης");
		m.addActionListener(this);
		popupMenu.add(m);
		table.getTableHeader().setComponentPopupMenu(popupMenu);
	}

	@Override
	public final Object getData() { return MainFrame.data.get("Κρατήσεις"); }

	@Override
	public void actionPerformed(ActionEvent e) {
		String ac = e.getActionCommand();
		ResizableTableModel rtm = (ResizableTableModel) table.getModel();
		String head = JOptionPane.showInputDialog(getParent(),
						"Δώστε της επικεφαλίδα της νέας στήλης.", "Εισαγωγή στήλης", JOptionPane.QUESTION_MESSAGE);
		if (head != null && !head.equals("")) {
			String[] heads = rtm.getHash();
			if (Arrays.asList(heads).contains(head))
				JOptionPane.showMessageDialog(getParent(),
								"Υπάρχει ήδη στήλη με αυτό το όνομα.", "Εισαγωγή στήλης", JOptionPane.ERROR_MESSAGE);
			else {
				String nheads[] = Arrays.copyOf(heads, heads.length + 1);
				nheads[heads.length] = head;
				rtm.setHash(nheads);
				table.addColumn(new TableColumn(heads.length));
			}
		}
	}
}