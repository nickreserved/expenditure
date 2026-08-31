package expenditure;

import util.PhpSerializer.Node;
import util.PhpSerializer.VariableFields;
import util.PhpSerializer.VariableSerializable;
import util.PropertiesTableModel.TableRecord;
import static util.ResizableTableModel.getString;

/** Στοιχεία Μονάδας / Υπηρεσίας, τα οποία δεν αλλάζουν από δαπάνη σε δαπάνη. */
final class UnitInfo implements VariableSerializable, TableRecord {
	/** Σύντμηση ελέγχουσας αρχής. Π.χ. 12 ΚΤΣ. */
	private String checkAuthority;
	/** Αναθέτουσα Αρχή. */
	private String contractingAuthority;
	/** Πλήρες όνομα Μονάδας. Π.χ. 3ος Λόχος Μηχανικού. */
	private String unitFull;
	/** Σύντμηση ονόματος Μονάδας. Π.χ. 3ος ΛΜΧ. */
	private String unit;
	/** Έδρα Μονάδας. Π.χ. Βάλτος Ορεστιάδας. */
	private String city;
	/** Διεύθυνση Μονάδας. Π.χ. Λεωφόρος Μεσογείων 123. */
	private String address;
	/** Ταχυδρομικός κώδικας Μονάδας. */
	private String zipCode;
	/** Διεύθυνση ηλεκτρονικού ταχυδρομίου Μονάδας. */
	private String email;
	/** Κωδικός Μονάδας από το ΟΠΣ */
	private String codeOPS;
	/** Τίτλος Γραφείου χειριστή οικονομικών θεμάτων. Π.χ. 4ο Γραφείο. */
	private String office;
	/** Ιδιότητα χειριστή οικονομικών θεμάτων. Π.χ. Αξκος 4ου Γραφείου. */
	private String operatorTitle;
	/** Τηλέφωνο αρμόδιου Γραφείου ή της Μονάδας. */
	private String phone;
	/** Διοικητής. */
	private Person commander;
	/** Υποδιοικητής. */
	private Person deputy_commander;
	/** Χειριστής οικονομικών θεμάτων (Αξκος 4ου Γραφείου). */
	private Person operator;
	/** Διαχειριστής Υλικού. */
	private Person accountant;
	/** Διαταγή συγκρότησης επιτροπών. */
	private String orderCommittee;
	/** Πρόεδρος Επιτροπής Παρακολούθησης και Παραλαβής Προμηθειών. */
	private Person supplyChief;
	/** Α' Μέλος Επιτροπής Παρακολούθησης και Παραλαβής Προμηθειών. */
	private Person supplyMemberA;
	/** Β' Μέλος Επιτροπής Παρακολούθησης και Παραλαβής Προμηθειών. */
	private Person supplyMemberB;
	/** Πρόεδρος Επιτροπής Παραλαβής Υπηρεσιών. */
	private Person serviceChief;
	/** Α' Μέλος Επιτροπής Παραλαβής Υπηρεσιών. */
	private Person serviceMemberA;
	/** Β' Μέλος Επιτροπής Παραλαβής Υπηρεσιών. */
	private Person serviceMemberB;
	/** Πρόεδρος Επιτροπής ∆ιενέργειας ∆ιαγωνισµού και Αξιολόγησης Προσφορών. */
	private Person tenderChief;
	/** Α' Μέλος Επιτροπής ∆ιενέργειας ∆ιαγωνισµού και Αξιολόγησης Προσφορών. */
	private Person tenderMemberA;
	/** Β' Μέλος Επιτροπής ∆ιενέργειας ∆ιαγωνισµού και Αξιολόγησης Προσφορών. */
	private Person tenderMemberB;


	/** Αρχικοποίηση του αντικειμένου. */
	UnitInfo() {}
	/** Αρχικοποίηση του αντικειμένου από άλλο αντικείμενο του τύπου.
	 * Γίνεται αντιγραφή (copy constractor) προκειμένου να αποφεύγεται π.χ. να τροποποιούμε τα στοιχεία σε
	 * μια δαπάνη και να τροποποιούνται και τα στοιχεία στην αντίστοιχη καρτέλα με τα στοιχεία Μονάδας. */
	UnitInfo(UnitInfo p) {
		checkAuthority       = p.checkAuthority;
		contractingAuthority = p.contractingAuthority;
		unitFull             = p.unitFull;
		unit                 = p.unit;
		city                 = p.city;
		address              = p.address;
		zipCode              = p.zipCode;
		email                = p.email;
		codeOPS              = p.codeOPS;
		office               = p.office;
		operatorTitle        = p.operatorTitle;
		phone                = p.phone;
		commander            = p.commander;
		deputy_commander     = p.deputy_commander;
		operator             = p.operator;
		accountant           = p.accountant;
		orderCommittee       = p.orderCommittee;
		supplyChief          = p.supplyChief;
		supplyMemberA        = p.supplyMemberA;
		supplyMemberB        = p.supplyMemberB;
		serviceChief         = p.serviceChief;
		serviceMemberA       = p.serviceMemberA;
		serviceMemberB       = p.serviceMemberB;
		tenderChief          = p.tenderChief;
		tenderMemberA        = p.tenderMemberA;
		tenderMemberB        = p.tenderMemberB;
	}

	/** Αρχικοποιεί τα στοιχεία της Μονάδας από έναν node δεδομένων του unserialize().
	 * @param node Ο node δεδομένων
	 * @throws Exception Αν ο node δεν είναι αντικείμενο */
	UnitInfo(Node node) throws Exception {
		if (!node.isObject()) throw new Exception("Για στοιχεία Μονάδας, αναμένονταν αντικείμενο");
		checkAuthority       = node.getField(H[0]).getString();
		contractingAuthority = node.getField(H[1]).getString();
		unitFull             = node.getField(H[2]).getString();
		unit                 = node.getField(H[3]).getString();
		city                 = node.getField(H[4]).getString();
		address              = node.getField(H[5]).getString();
		zipCode              = node.getField(H[6]).getString();
		email                = node.getField(H[7]).getString();
		codeOPS              = node.getField(H[8]).getString();
		office               = node.getField(H[9]).getString();
		operatorTitle        = node.getField(H[10]).getString();
		phone                = node.getField(H[11]).getString();
		commander            = Person.create(node.getField(H[12]));
		deputy_commander     = Person.create(node.getField(H[13]));
		operator             = Person.create(node.getField(H[14]));
		accountant           = Person.create(node.getField(H[15]));
		orderCommittee       = node.getField(H[16]).getString();
		supplyChief          = Person.create(node.getField(H[17]));
		supplyMemberA        = Person.create(node.getField(H[18]));
		supplyMemberB        = Person.create(node.getField(H[19]));
		serviceChief         = Person.create(node.getField(H[20]));
		serviceMemberA       = Person.create(node.getField(H[21]));
		serviceMemberB       = Person.create(node.getField(H[22]));
		tenderChief          = Person.create(node.getField(H[23]));
		tenderMemberA        = Person.create(node.getField(H[24]));
		tenderMemberB        = Person.create(node.getField(H[25]));
	}

	/** Ονόματα πεδίων αποθήκευσης. */
	static final String[] H = {
		"Ελέγχουσα Αρχή", "Αναθέτουσα Αρχή", "Μονάδα Πλήρες", "Μονάδα", "Έδρα", "Διεύθυνση", "Τ.Κ.",
		"email", "Κωδικός ΟΠΣ",
		"Γραφείο", "Ιδιότητα Αξκου", "Τηλέφωνο", "Δκτης", "ΕΟΥ", "Αξκος Γραφείου", "Δχστης",
		"Δγη Συγκρότησης Επιτροπών",
		"Πρόεδρος Παραλαβής Προμηθειών", "Α Μέλος Παραλαβής Προμηθειών", "Β Μέλος Παραλαβής Προμηθειών",
		"Πρόεδρος Παραλαβής Υπηρεσιών", "Α Μέλος Παραλαβής Υπηρεσιών", "Β Μέλος Παραλαβής Υπηρεσιών",
		"Πρόεδρος Διαγωνισμών", "Α Μέλος Διαγωνισμών", "Β Μέλος Διαγωνισμών"
	};

	/** Επιστρέφει array με όλα τα πρόσωπα που εμπλέκονται στα αμετάβλητα στοιχεία.
	 * Χρησιμοποιείται στην εισαγωγή προσώπων στο πρόγραμμα, από αμετάβλητα στοιχεία μιας δαπάνης.
	 * @returns Το array με τα πρόσωπα */
	Person[] getPersonnel() {
		return new Person[] {
			commander, deputy_commander, operator, accountant,
			supplyChief, supplyMemberA, supplyMemberB,
			serviceChief, serviceMemberA, serviceMemberB,
			tenderChief, tenderMemberA, tenderMemberB
		};
	}

	/** Επιστρέφει το πλήρες όνομα της Μονάδας.
	 * @returns Το πλήρες όνομα της Μονάδας */
	String getUnitName() { return unitFull; }


	@Override public void serialize(VariableFields fields) {
		if (checkAuthority != null)       fields.add(H[ 0], checkAuthority);
		if (contractingAuthority != null) fields.add(H[ 1], contractingAuthority);
		if (unitFull != null)             fields.add(H[ 2], unitFull);
		if (unit != null)                 fields.add(H[ 3], unit);
		if (city != null)                 fields.add(H[ 4], city);
		if (address != null)              fields.add(H[ 5], address);
		if (zipCode != null)              fields.add(H[ 6], zipCode);
		if (email != null)                fields.add(H[ 7], email);
		if (codeOPS != null)              fields.add(H[ 8], codeOPS);
		if (office != null)               fields.add(H[ 9], office);
		if (operatorTitle != null)        fields.add(H[10], operatorTitle);
		if (phone != null)                fields.add(H[11], phone);
		if (commander != null)            fields.add(H[12], commander);
		if (deputy_commander != null)     fields.add(H[13], deputy_commander);
		if (operator != null)             fields.add(H[14], operator);
		if (accountant != null)           fields.add(H[15], accountant);
		if (orderCommittee != null)       fields.add(H[16], orderCommittee);
		if (supplyChief != null)          fields.add(H[17], supplyChief);
		if (supplyMemberA != null)        fields.add(H[18], supplyMemberA);
		if (supplyMemberB != null)        fields.add(H[19], supplyMemberB);
		if (serviceChief != null)         fields.add(H[20], serviceChief);
		if (serviceMemberA != null)       fields.add(H[21], serviceMemberA);
		if (serviceMemberB != null)       fields.add(H[22], serviceMemberB);
		if (tenderChief != null)          fields.add(H[23], tenderChief);
		if (tenderMemberA != null)        fields.add(H[24], tenderMemberA);
		if (tenderMemberB != null)        fields.add(H[25], tenderMemberB);
	}

	@Override public Object getCell(int index) {
		switch(index) {
			case 0: return null;	// Επικεφαλίδα
			case 1: return checkAuthority;
			case 2: return contractingAuthority;
			case 3: return unitFull;
			case 4: return unit;
			case 5: return city;
			case 6: return address;
			case 7: return zipCode;
			case 8: return email;
			case 9: return codeOPS;
			case 10: return office;
			case 11: return operatorTitle;
			case 12: return phone;
			case 13: return commander;
			case 14: return deputy_commander;
			case 15: return operator;
			case 16: return accountant;
			case 17: return null;	// Επικεφαλίδα
			case 18: return orderCommittee;
			case 19: return supplyChief;
			case 20: return supplyMemberA;
			case 21: return supplyMemberB;
			case 22: return serviceChief;
			case 23: return serviceMemberA;
			case 24: return serviceMemberB;
			case 25: return tenderChief;
			case 26: return tenderMemberA;
			default: return tenderMemberB;
		}
	}

	@Override public void setCell(int index, Object value) {
		switch(index) {
			//case 0: break;
			case 1: checkAuthority       = getString(value); break;
			case 2: contractingAuthority = getString(value); break;
			case 3: unitFull             = getString(value); break;
			case 4: unit                 = getString(value); break;
			case 5: city                 = getString(value); break;
			case 6: address              = getString(value); break;
			case 7: zipCode              = getString(value); break;
			case 8: email                = getString(value); break;
			case 9: codeOPS              = getString(value); break;
			case 10: office              = getString(value); break;
			case 11: operatorTitle       = getString(value); break;
			case 12: phone               = getString(value); break;
			case 13: commander           = (Person) value; break;
			case 14: deputy_commander    = (Person) value; break;
			case 15: operator            = (Person) value; break;
			case 16: accountant          = (Person) value; break;
			//case 17: break;
			case 18: orderCommittee      = getString(value); break;
			case 19: supplyChief         = (Person) value; break;
			case 20: supplyMemberA       = (Person) value; break;
			case 21: supplyMemberB       = (Person) value; break;
			case 22: serviceChief        = (Person) value; break;
			case 23: serviceMemberA      = (Person) value; break;
			case 24: serviceMemberB      = (Person) value; break;
			case 25: tenderChief         = (Person) value; break;
			case 26: tenderMemberA       = (Person) value; break;
			case 27: tenderMemberB       = (Person) value; break;
		}
	}
}
