package cost;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.table.TableColumn;
import tables.ArrayTransmitter;
import tables.ComboDataModel;
import tables.ResizableTable;
import tables.ResizableTableModel;

final public class Holds extends JPanel implements ArrayTransmitter<Hold>, ActionListener {

	static protected JComboBox holds;
	protected ResizableTable table;

	@SuppressWarnings("LeakingThisInConstructor")
	public Holds() {
		holds = new JComboBox(new ComboDataModel(this, new Hold()));

		ArrayList<String> heads = new ArrayList<>(Arrays.asList(new String[]
			{"Σύνολο", "ΜΤΣ", "Χαρτόσημο", "ΟΓΑ", "ΕΑΑΔΗΣΥ"} ));
		((ArrayList<Hold>) getData()).forEach(i -> 
				i.keySet().stream().filter(j -> !heads.contains(j.toString()))
						.forEach(j -> heads.add(j.toString()))
		);
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
	public ArrayList<Hold> getData() { return (ArrayList<Hold>) MainFrame.data.get("Κρατήσεις"); }

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