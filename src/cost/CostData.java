package cost;

import common.HashObject;
import java.awt.BorderLayout;
import java.awt.event.ItemListener;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import tables.DataTransmitter;
import tables.PropertiesTable;
import tables.PropertiesTableModel;

final public class CostData extends JPanel implements DataTransmitter {
	final static protected String[] COST = { "Κατασκευή Έργων", "Προμήθεια - Συντήρηση - Επισκευή" };
	final static protected String[] CONTEST = { "Δημόσιος Διαγωνισμός", "Πρόχειρος Διαγωνισμός", "Χωρίς Διαγωνισμό" };

	public CostData(ItemListener il) {
		final String[] hash = { null, "ΤύποςΔαπάνης", "ΤύποςΔιαγωνισμού", "ΔγηΔιάθεσης", "ΔγηΑνάθεσης",
				"Διαβιβαστικό", "Ποσό", "ΕΦ", "ΚΑ", "Τίτλος", "ΗμερομηνίαΥποβολής", "ΑξκοςΈργου",
				"ΠρόεδροςΑγοράςΔιάθεσης", "ΜέλοςΑγοράςΔιάθεσηςΑ", "ΜέλοςΑγοράςΔιάθεσηςΒ",
				"ΠρόεδροςΠοιοτικήςΠοσοτικήςΠαραλαβής", "ΜέλοςΠοιοτικήςΠοσοτικήςΠαραλαβήςΑ", "ΜέλοςΠοιοτικήςΠοσοτικήςΠαραλαβήςΒ",
				null, null, "Έργο", "ΠεριοχήΈργου", "Εργολάβος",
				"ΗμερομηνίαΑφανώνΕργασιών", "ΗμερομηνίαΟριστικήςΕπιμέτρησης", "ΗμερομηνίαΠροσωρινήςΟριστικήςΠαραλαβής", "ΗμερομηνίαΔιοικητικήςΠαράδοσης",
				"ΠρόεδροςΑφανώνΕργασιών", "ΜέλοςΑφανώνΕργασιών",
				"ΠρόεδροςΠροσωρινήςΟριστικήςΠαραλαβής", "ΜέλοςΠροσωρινήςΟριστικήςΠαραλαβήςΑ", "ΜέλοςΠροσωρινήςΟριστικήςΠαραλαβήςΒ",
				null, null, "ΔγηΔιακήρυξης", "ΔγηΚατακύρωσης", "Εφημερίδες", "ΗμερομηνίεςΔημοσίευσης", "ΘέμαΔιαγωνισμού",
				"ΏραΔιαγωνισμού", "ΤόποςΔιαγωνισμού", "Νικητής", "ΠρόεδροςΔιαγωνισμού", "ΜέλοςΔιαγωνισμούΑ", "ΜέλοςΔιαγωνισμούΒ",
				null, null, "ΣύντμησηΜονάδας", "Μονάδα", "ΕλέγχουσαΑρχή", "Γραφείο", "ΙδιότηταΑξκου", "Πόλη",
				"Διεύθυνση", "Τηλέφωνο", "ΤΚ", "Δκτης", "ΕΟΥ", "ΑξκοςΓραφείου", "Δχστης"
		};
		JComboBox[] cmp = new JComboBox[hash.length];
		cmp[1] = new JComboBox(COST);
		cmp[2] = new JComboBox(CONTEST);
		cmp[1].addItemListener(il);
		cmp[2].addItemListener(il);
		cmp[22] = Providers.providers;
		cmp[41] = Providers.providers;
		for (int z = 11; z < 11 + 7; z++) cmp[z] = Men.men;
		for (int z = 27; z < 27 + 5; z++) cmp[z] = Men.men;
		for (int z = 42; z < 42 + 3; z++) cmp[z] = Men.men;
		for (int z = hash.length - 4; z < hash.length; z++) cmp[z] = Men.men;
		setLayout(new BorderLayout());
		add(PropertiesTable.getScrolled(
				new PropertiesTableModel(hash, this,
				new String[] { "<html><b>ΚΟΙΝΕΣ ΔΑΠΑΝΕΣ", "Τύπος Δαπάνης", "Διαγωνισμός",
					"Δγη διάθεσης", "Δγη συγκρότησης επιτροπών", null, null, null, null,
					"<html>Τίτλος <font color=gray size=2>(αιτιατική)", "Ημερομηνία υποβολής",
					"Αξκος Έργου",
					"Πρόεδρος Αγοράς Διάθεσης", "Α' Μέλος Αγοράς Διάθεσης", "Β' Μέλος Αγοράς Διάθεσης",
					"Πρόεδρος Ποιοτ. Ποσοτ. Παραλαβης", "Α' Μέλος Ποιοτ. Ποσοτ. Παραλαβης", "Β' Μέλος Ποιοτ. Ποσοτ. Παραλαβης",
					null, "<html><b>ΔΑΠΑΝΕΣ ΕΡΓΩΝ", "<html>Έργο <font color=gray size=2>(αιτιατική)",
					"<html>Περιοχή Έργου <font color=gray size=2>(αιτιατική με άρθρο)",
					"Εργολάβος ή Εργατοτεχνίτης", "Ημερομηνία Ελέγχ. Αφαν. Εργασιών",
					"Ημερομηνία Οριστ. Επιμέτρησης", "Ημερομηνία Προσ. Οριστ. Παραλαβής",
					"Ημερομηνία Δοικητ. Παράδοσης",
					"Πρόεδρος Αφανών Εργασιών", "Μέλος Αφανών Εργασιών",
					"Πρόεδρος Προσ. Οριστ. Παραλαβής", "Α' Μέλος Προσ. Οριστ. Παραλαβής", "Β' Μέλος Προσ. Οριστ. Παραλαβής",
					null, "<html><b>ΔΙΑΓΩΝΙΣΜΟΙ", "Δγη Διακύρηξης Διαγωνισμού",
					"Δγη Κατακύρωσης Διαγωνισμού",
					"<html>Εφημερίδες <font color=gray size=2>(χωρίζουν με '<b>,</b>')",
					"<html>Ημερομηνίες Δημοσίευσης <font color=gray size=2>(χωρίζουν με '<b>,</b>')",
					"<html>Θέμα Διαγωνισμού <font color=gray size=2>(αιτιατική)", "Ώρα/Ημερομηνία",
					"<html>Τόπος που θα διεξαχθεί <font color=gray size=2>(αιτιατική με άρθρο)", null,
					"Πρόεδρος Διαγωνισμού", "Α' Μέλος Διαγωνισμού", "Β' Μέλος Διαγωνισμού", null,
					"<html><b>ΑΜΕΤΑΒΛΗΤΑ ΣΤΟΙΧΕΙΑ", "<html>Μονάδα <font color=gray size=2>(σύντμηση)",
					"<html>Μονάδα <font color=gray size=2>(πλήρης)", "Ελέγχουσα Αρχή", null,
					"Ιδιότητα Αξκού", "Πόλη ή Χωρίο", null, "Τηλέφωνο", "Τ.Κ.", null, null,
					"Αξκος Γραφείου", null
				}
		), cmp, 230));
	}
	
	@Override public HashObject getData() { return (Cost) MainFrame.costs.get(); }
}