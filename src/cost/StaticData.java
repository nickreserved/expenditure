package cost;

import java.awt.*;
import javax.swing.*;
import tables.*;

public class StaticData extends JPanel implements DataTransmitter {
	static final protected String[] hash = { "ΣύντμησηΜονάδας", "Μονάδα", "ΓραφείοΣχηματισμού",
			"Γραφείο", "ΙδιότηταΑξκου", "Πόλη", "Διεύθυνση", "ΕξωτερικόΤηλέφωνο",
			"ΕσωτερικόΤηλέφωνο", "ΤΚ", "Δκτης", "ΕΟΥ", "ΑξκοςΓραφείου", "Δχστης"
	};
	public StaticData() {
		Component[] cmp = new Component[hash.length];
		for (int z = hash.length - 4; z < hash.length; z++) cmp[z] = Men.men;
		setLayout(new BorderLayout());
		add(PropertiesTable.getScrolled(
				new PropertiesTableModel(
				hash, this,
				new String[] { "<html>Μονάδα <font color=gray size=2>(σύντμηση)", "<html>Μονάδα <font color=gray size=2>(πλήρης)",
						"Γραφείο Σχηματισμού", null, "Ιδιότητα Αξκού",	"Πόλη ή Χωρίο", null, "<html>Τηλέφωνο <font color=gray size=2>(εξωτερικό)",
						"<html>Τηλέφωνο <font color=gray size=2>(εσωτερικό)", "Τ.Κ.",	null,	null, "Αξκος Γραφείου", null }
		), cmp, 130));
	}
	
	@Override
	public Object getData() {	return MainFrame.data.get("ΑμετάβληταΣτοιχείαΔαπάνης");	}
}