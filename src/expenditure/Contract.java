package expenditure;

import static expenditure.Invoice.add;
import static expenditure.Invoice.sub;
import expenditure.Tender.Competitor;
import util.PhpSerializer.Node;
import util.PhpSerializer.VariableFields;
import util.PhpSerializer.VariableSerializable;
import util.ResizableTableModel.TableRecord;
import static util.ResizableTableModel.getString;

/** Μια σύμβαση. */
final class Contract implements VariableSerializable, TableRecord {
	/** Η ταυτότητα της σύμβασης. */
	private String name;
	/** Το θέμα της σύμβασης. */
	private String title;
	/** Πρόσκληση υποβολής προσφορών. */
	private String callForOffers;
	/** Η απόφαση απευθείας ανάθεσης. */
	private String orderDirectAssignment;
	/** Ο διαγωνισμός από τον οποίο προέκυψε η σύμβαση. */
	private Tender tender;
	/** Ο δικαιούχος με τον οποίο συνυπογράφεται η σύμβαση. */
	private Contractor contractor;
	/** Προσκεκλημένοι οικονομικοί φορείς.
	 * Αν έχουμε σύμβαση με απευθείας ανάθεση, μη κοινοποιήσιμη στο διαδίκτυο, τότε η Πρόσκληση
	 * Υποβολής Προσφορών καλεί συγκεκριμένους οικονομικούς φορείς. Ένας από αυτούς είναι ο
	 * δικαιούχος με τον οποίο θα υπογραφεί η σύμβαση. Οι υπόλοιποι, το πολύ δύο, είναι οι
	 * συγκεκριμένοι. */
	private Contractor guest1, guest2;

	/** Οι αξίες σε € των τιμολογίων που ανήκουν στην σύμβαση.
	 * Η καθαρή αξία, το ΦΠΑ, το καταλογιστέο, οι κρατήσεις, το πληρωτέο, το ΦΕ και το υπόλοιπο
	 * πληρωτέο, όλα στογγυλοποιημένα στο δεύτερο δεκαδικό ψηφίο και με την αυτή σειρά.
	 * <p>Τα δεδομένα αυτά δεν αποθηκεύονται. Υπολογίζονται προκειμένου να χρησιμοποιηθούν από τον
	 * πίνακα που προβάλει τα αθροίσματα των τιμολογίων (στην καρτέλα «Τιμολόγια», ο τρίτος πίνακας
	 * κάτω-κάτω). */
	final double[] prices = new double[7];

	/** Η δαπάνη στην οποία ανήκει η σύμβαση. */
	final Expenditure parent;

	/** Αρχικοποίηση του αντικειμένου με τις προκαθορισμένες τιμές.
	 * @param parent Η δαπάνη στην οποία ανήκει η σύμβαση */
	Contract(Expenditure parent) { this.parent = parent; }

	/** Αρχικοποιεί μια σύμβαση από έναν node δεδομένων του unserialize().
	 * @param parent Η δαπάνη στην οποία ανήκει η σύμβαση
	 * @param n Ο node δεδομένων
	 * @throws Exception Αν ο node δεν είναι αντικείμενο */
	Contract(Expenditure parent, Node node) throws Exception {
		this.parent = parent;
		if (!node.isObject()) throw new Exception("Για σύμβαση, αναμένονταν αντικείμενο");
		name                  = node.getField(H[0]).getString();
		title                 = node.getField(H[1]).getString();
		callForOffers         = node.getField(H[3]).getString();
		orderDirectAssignment = node.getField(H[4]).getString();
		guest1                = Contractor.create(node.getField(H[5]));
		guest2                = Contractor.create(node.getField(H[6]));
		Node n                = node.getField(H[7]);
		if (n.isInteger() && n.getInteger() < parent.tenders.size())
			tender            = parent.tenders.get((int) n.getInteger());
		n                     = node.getField(H[2]);
		// Σε διαγωνισμό, ο νικητής αποθηκεύεται ως index των διαγωνιζόμενων
		if (tender == null) contractor = Contractor.create(n);
		else if (n.isInteger() && n.getInteger() < tender.competitors.size())
			contractor        = tender.competitors.get((int) n.getInteger()).getContractor();
	}

	/** Αρχικοποιείται μια σύμβαση από το σύστημα αυτόματων υπολογισμών.
	 * @param parent Η δαπάνη στην οποία ανήκει η σύμβαση
	 * @param contractor Ο ανάδοχος της σύμβασης */
	Contract(Expenditure parent, Contractor contractor) {
		this.parent = parent; this.contractor = contractor;
		name = contractor != null ? contractor.toString() : "ανώνυμη";
	}

	/** Κόστος πάνω από το οποίο έχουμε συνοπτικό διαγωνισμό. */
	static final private int TENDER_PRICE1 = 30000;
	static final private int TENDER_PRICE2 = 60000;

	/** Καθορίζει το είδος του διαγωνισμού ανάλογα με τη δοσμένη καθαρή αξία.
	 * @param price Η συνολική καθαρή αξία όλων των τιμολογίων του ανάδοχου */
	void calcTenderType(double price) {
		if (tender != null && parent.isSmart() && price <= TENDER_PRICE1) {
			sub(tender.prices, prices);
			tender = null;
		}
	}

	/** Επικεφαλίδες του αντίστοιχου πίνακα, αλλά και ονόματα πεδίων αποθήκευσης. */
	static final String[] H = {
		"Σύμβαση", "Τίτλος", "Ανάδοχος", "Πρόσκληση Υποβολής Προσφορών",
		"Απόφαση Απευθείας Ανάθεσης", "Προσκεκλημένος 1", "Προσκεκλημένος 2", "Διαγωνισμός"
	};
	
	/** Επιστρέφει array με όλους τους δικαιούχους που εμπλέκονται στα στοιχεία σύμβασης.
	 * Χρησιμοποιείται στην εισαγωγή δικαιούχων στο πρόγραμμα, από τα στοιχεία μιας δαπάνης.
	 * @returns Το array με τους δικαιούχους */
	Contractor[] getContractors() { return new Contractor[] { contractor, guest1, guest2 }; }
	
	/** Επιστρέφει τον ανάδοχο της σύμβασης. */
	Contractor getContractor() { return contractor; }
	/** Επιστρέφει το διαγωνισμό. */
	Tender getTender() { return tender; }

	@Override public String toString() { return name == null ? "Σύμβαση ανώνυμη" : "Σύμβαση " + name; }

	@Override public void serialize(VariableFields fields) {
		if (name != null)                  fields.add(H[0], name);
		if (title != null)                 fields.add(H[1], title);
		if (callForOffers != null)         fields.add(H[3], callForOffers);
		if (orderDirectAssignment != null) fields.add(H[4], orderDirectAssignment);
		if (guest1 != null)                fields.add(H[5], guest1);
		if (guest2 != null)                fields.add(H[6], guest2);
		if (tender != null) {
			// Αν έχουμε διαγωνισμό, αποθηκεύεται με το index του
			int idx = parent.tenders.indexOf(tender);
			if (idx != -1)             fields.add(H[7], idx);
			// Αν έχουμε διαγωνισμό ο νικητής αποθηκεύεται με το index των διαγωνιζόμενων
			if (contractor != null)
				for (int z = 0; z < tender.competitors.size(); ++z)
					if (contractor.equals(tender.competitors.get(z).getContractor())) {
						fields.add(H[2], z); break;
					}
		} else if (contractor != null) fields.add(H[2], contractor);
	}

	@Override public Object getCell(int index) {
		switch(index) {
			case 0: return name;
			case 1: return title;
			case 2: return contractor;
			case 3: return null;
			case 4: return callForOffers;
			case 5: return orderDirectAssignment;
			case 6: return guest1;
			case 7: return guest2;
			case 8: return null;
			default: return tender;
		}
	}

	@Override public void setCell(int index, Object value) {
		switch(index) {
			case 0: name                  = getString(value); break;
			case 1: title                 = getString(value); break;
			case 2:
				contractor = (Contractor) value;
				if (tender != null) addCompetitorIfNotExist();
				break;
			case 4: callForOffers         = getString(value); break;
			case 5: orderDirectAssignment = getString(value); break;
			case 6: guest1                = (Contractor) value; break;
			case 7: guest2                = (Contractor) value; break;
			case 9:
				if (value != tender) {
					// Σε αυτόματους υπολογισμούς, αν θέσουμε αντικανονικό διαγωνισμό δε γίνεται δεκτός
					if (parent.isSmart())
						if (value == null && prices[0] > TENDER_PRICE2
								|| value != null && prices[0] <= TENDER_PRICE1)
							break;
					if (tender != null) sub(tender.prices, prices);
					tender = (Tender) value;
					if (tender != null) {
						add(tender.prices, prices);
						addCompetitorIfNotExist();
					}
					parent.calcContents();
				}
				break;
		}
	}

	/** Η σύμβαση έχει όλα της τα πεδία κενά. */
	@Override public boolean isEmpty() {
		return name == null && title == null && contractor == null && callForOffers == null
				&& orderDirectAssignment == null && guest1 == null && guest2 == null
				&& tender == null;
	}

	/** Προσθέτει τον ανάδοχο της σύμβασης, σαν διαγωνιζόμενο στο διαγωνισμό, αν δεν είναι ήδη. */
	private void addCompetitorIfNotExist() {
		if (contractor != null && tender.competitors.stream().noneMatch(i -> contractor.equals(i.getContractor())))
			tender.competitors.add(new Competitor(tender, contractor));
	}
}