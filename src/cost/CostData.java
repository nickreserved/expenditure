package cost;

import java.awt.*;
import javax.swing.*;
import tables.*;
import common.*;

public class CostData extends JPanel implements DataTransmitter {
	public CostData() {
		final String[] hdr = { "Τύπος Δαπάνης", "Διαγωνισμός", "Δγη διάθεσης", "Δγη συγκρότησης επιτροπών",
				null, null, null, null, null, "Ημερομηνία υποβολής", "Αξκος Έργου",
				"Πρόεδρος Αγοράς Διάθεσης", "Α' Μέλος Αγοράς Διάθεσης", "Β' Μέλος Αγοράς Διάθεσης",
				"Πρόεδρος Ποιοτ. Ποσοτ. Παραλαβης", "Α' Μέλος Ποιοτ. Ποσοτ. Παραλαβης", "Β' Μέλος Ποιοτ. Ποσοτ. Παραλαβης",
		};
		final String[] hash = { "ΤύποςΔαπάνης", "ΤύποςΔιαγωνισμού", "ΔγηΔιάθεσης", "ΔγηΑνάθεσης",
				"Διαβιβαστικό", "Ποσό", "ΕΦ", "ΚΑ", "Τίτλος", "ΗμερομηνίαΥποβολής", "ΑξκοςΈργου",
				"ΠρόεδροςΑγοράςΔιάθεσης", "ΜέλοςΑγοράςΔιάθεσηςΑ", "ΜέλοςΑγοράςΔιάθεσηςΒ",
				"ΠρόεδροςΠοιοτικήςΠοσοτικήςΠαραλαβής", "ΜέλοςΠοιοτικήςΠοσοτικήςΠαραλαβήςΑ", "ΜέλοςΠοιοτικήςΠοσοτικήςΠαραλαβήςΒ"
		};
		final String[] cosT = { "Προμήθεια - Συντήρηση - Επισκευή", "Κατασκευή Έργων με Υλικά Δημοσίου ή Εμπορίου και Οπλίτες ή Εργατοτεχνίτες", "Κατασκευή Έργων από Εργοληπτική Επιχείρηση" };
		final String[] contesT = { "Χωρίς Διαγωνισμό", "Πρόχειρος Διαγωνισμός", "Δημόσιος Διαγωνισμός" };
		Component[] cmp = new Component[hash.length];
		cmp[0] = new JComboBox(cosT);
		cmp[1] = new JComboBox(contesT);
		for (int z = hash.length - 7; z < hash.length; z++) cmp[z] = Men.men;
		setLayout(new BorderLayout());
		add(PropertiesTable.getScrolled(new PropertiesTableModel(hash, this, hdr), cmp, 210));
	}
	
	public Object getData() { return MainFrame.costs.get(); }
}