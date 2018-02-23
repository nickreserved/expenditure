package cost;

import java.awt.*;
import javax.swing.*;
import tables.*;

public class Holds extends JPanel implements DataTransmitter {
	static protected JComboBox holds;

	public Holds() {
		holds = new JComboBox(new ComboDataModel(this, new Hold()));
		setLayout(new BorderLayout());
		add(new JScrollPane(new ResizableTable(new ResizableTableModel(this, new String[] { "ΜΤΣ", "ΕΜΠ", "ΤΣΜΕΔΕ", "ΑΟΟΑ", "ΤΠΕΔΕ", "ΕΚΟΕΜΣ", "ΒΑΜ", "ΕΛ-ΜΤΣ", "Νοσ. Περίθαλψη", "Χαρτόσημο", "ΟΓΑ", "Σύνολο" }, null, Hold.class), false, true)));
	}

	@Override
	public Object getData() { return MainFrame.data.get("Κρατήσεις"); }
}