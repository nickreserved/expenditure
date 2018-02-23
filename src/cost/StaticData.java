package cost;

import common.HashObject;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JPanel;
import tables.DataTransmitter;
import tables.PropertiesTable;
import tables.PropertiesTableModel;

final public class StaticData extends JPanel implements DataTransmitter {
	static final protected String[] HASH = {
		"ΣύντμησηΜονάδας", "Μονάδα", "ΕλέγχουσαΑρχή", "Γραφείο", "ΙδιότηταΑξκου", "Πόλη",
		"Διεύθυνση", "Τηλέφωνο", "ΤΚ", "Δκτης", "ΕΟΥ", "ΑξκοςΓραφείου", "Δχστης"
	};
	public StaticData() {
		Component[] cmp = new Component[HASH.length];
		for (int z = HASH.length - 4; z < HASH.length; z++) cmp[z] = Men.men;
		setLayout(new BorderLayout());
		add(PropertiesTable.getScrolled(new PropertiesTableModel(
				HASH, this,
				new String[] {
					"<html>Μονάδα <font color=gray size=2>(σύντμηση)",
					"<html>Μονάδα <font color=gray size=2>(πλήρης)", "Ελέγχουσα Αρχή", null,
					"Ιδιότητα Αξκού", "Πόλη ή Χωρίο", null, "Τηλέφωνο", "Τ.Κ.",	null, null,
					"Αξκος Γραφείου", null
				}
		), cmp, 130));
	}
	
	@Override
	public HashObject getData() {	return (HashObject) MainFrame.data.get("ΑμετάβληταΣτοιχείαΔαπάνης");	}
}