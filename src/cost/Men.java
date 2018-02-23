package cost;

import java.awt.*;
import javax.swing.*;
import tables.*;

public class Men extends JPanel implements DataTransmitter {
	static protected JComboBox men;
	public Men() {
		men = new JComboBox(new ComboDataModel(this, null));
		setLayout(new BorderLayout());
		add(new JScrollPane(new ResizableTable(new ResizableTableModel(this, new String[] { "Βαθμός", "Ονοματεπώνυμο", "Μονάδα" }, new String[] { null, null, "<html>Μονάδα <font color=gray size=2>(γενική με άρθρο)" }, Man.class), true)));
	}

	public Object getData() { return MainFrame.data.get("Προσωπικό"); }
}