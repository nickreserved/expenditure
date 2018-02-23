package cost;

import java.awt.*;
import javax.swing.*;
import tables.*;

public class Men extends JPanel implements DataTransmitter {
	static protected JComboBox men;
	public Men() {
		final String[] header = { "Βαθμός", "Ονοματεπώνυμο", "Μονάδα" };
		men = new JComboBox(new ComboDataModel(this, null));
		setLayout(new BorderLayout());
		add(new JScrollPane(new ResizableTable(new ResizableTableModel(this, header, null, Man.class), true)));
	}

	public Object getData() { return MainFrame.data.get("Προσωπικό"); }
}