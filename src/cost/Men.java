package cost;

import java.awt.BorderLayout;
import java.util.ArrayList;
import tables.ArrayTransmitter;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import tables.ComboDataModel;
import tables.ResizableTable;
import tables.ResizableTableModel;

final public class Men extends JPanel implements ArrayTransmitter<Man> {
	static protected JComboBox men;
	public Men() {
		men = new JComboBox(new ComboDataModel(this, null));
		setLayout(new BorderLayout());
		add(new JScrollPane(new ResizableTable(new ResizableTableModel(this, new String[] { "Βαθμός", "Ονοματεπώνυμο", "Μονάδα" }, new String[] { null, null, "<html>Μονάδα <font color=gray size=2>(γενική με άρθρο)" }, Man.class), true, true)));
	}

	@Override
	public ArrayList<Man> getData() { return (ArrayList<Man>) MainFrame.data.get("Προσωπικό"); }
}