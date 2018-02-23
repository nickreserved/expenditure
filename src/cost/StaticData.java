package cost;

import java.awt.*;
import java.util.*;
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
		Object o = MainFrame.currentCost == null ? null : MainFrame.costs.get(MainFrame.currentCost);
		if (o instanceof Dictionary) return o;
		if (!(MainFrame.data instanceof HashObject)) MainFrame.data = new HashObject();
		o = MainFrame.data.get("ΑμετάβληταΣτοιχείαΔαπάνης");
		if (!(o instanceof HashObject))
			MainFrame.data.put("ΑμετάβληταΣτοιχείαΔαπάνης", o = new HashObject());
		return o;
	}
}