package cost;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.*;
import tables.*;

public class Providers extends JPanel implements ArrayTransmitter<Provider> {
	static protected JComboBox providers;

	public Providers() {
		providers = new JComboBox(new ComboDataModel(this, null));
		setLayout(new BorderLayout());
		ResizableTable rt = new ResizableTable(new ResizableTableModel(this, new String[]{"Επωνυμία", "Τύπος", "ΑΦΜ", "ΔΟΥ", "Τηλέφωνο", "Τ.Κ.", "Πόλη", "Διεύθυνση"}, null, Provider.class), true, true);
		add(new JScrollPane(rt));
		TableColumnModel cm = rt.getColumnModel();
		cm.getColumn(1).setCellEditor(new DefaultCellEditor(new JComboBox(new String[] { "Ιδιώτης", "Στρατός", "Δημόσιο", "Ενοικιαστής" })));
	}

	@Override
	public ArrayList<Provider> getData() { return (ArrayList<Provider>) MainFrame.data.get("Προμηθευτές"); }
}