package cost;

import java.awt.*;
import javax.swing.*;
import tables.*;

public class Providers extends JPanel implements DataTransmitter {
  static protected JComboBox providers;

	public Providers() {
		final String[] header = { "Επωνυμία", "ΑΦΜ", "ΔΟΥ", "Τηλέφωνο", "Τ.Κ.", "Πόλη", "Διεύθυνση"};
		providers = new JComboBox(new ComboDataModel(this));
    setLayout(new BorderLayout());
    add(new JScrollPane(new JTable(new ResizableTableModel(this, header, null, Provider.class))));
  }
	
	public Object getData() { return MainFrame.data.get("Προμηθευτές"); }
}