package cost;

import java.awt.*;
import javax.swing.*;
import common.*;
import tables.*;

public class StaticData extends JPanel implements DataTransmitter {
	public StaticData() {
		final String[] hdr = { "Μονάδα (συντ.)", "Μονάδα (πλήρ.)", "Γραφείο Σχηματισμού",
				null, "Ιδιότητα Αξκού",	"Πόλη ή Χωρίο", null, "Τηλέφωνο (εξωτερικό)",
				"Τηλέφωνο (εσωτερικό)", "Τ.Κ.",	null, null, "Αξκος Γραφείου"
		};
		final String[] hash = { "ΣύντμησηΜονάδας", "Μονάδα", "ΓραφείοΣχηματισμού",
				"Γραφείο", "ΙδιότηταΑξκου", "Πόλη", "Διεύθυνση", "ΕξωτερικόΤηλέφωνο",
				"ΕσωτερικόΤηλέφωνο", "ΤΚ", "Δκτης", "ΕΟΥ", "ΑξκοςΓραφείου"
		};
		Component[] cmp = new Component[hash.length];
		for (int z = hash.length - 3; z < hash.length; z++) cmp[z] = Men.men;
		setLayout(new BorderLayout());
		add(PropertiesTable.getScrolled(new PropertiesTableModel(hash, this, hdr), cmp, 130));
	}
	
	public Object getData() {
		Object c = MainFrame.costs.get();
		return c == null ? MainFrame.data.get("ΑμετάβληταΣτοιχείαΔαπάνης") : c;
	}
}