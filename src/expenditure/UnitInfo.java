package expenditure;

import static expenditure.ContentItem.ΔγηΣυγκρότησηςΕπιτροπών;
import util.PhpSerializer.Node;
import util.PhpSerializer.VariableFields;
import util.PhpSerializer.VariableSerializable;
import util.PropertiesTableModel.TableRecord;
import static util.ResizableTableModel.getString;

/** Στοιχεία Μονάδας / Υπηρεσίας, τα οποία δεν αλλάζουν από δαπάνη σε δαπάνη. */
final class UnitInfo implements VariableSerializable, TableRecord {
	/** Σύντμηση ελέγχουσας αρχής. Π.χ. 12 ΚΤΣ. */
	private String checkAuthority;
	/** Σύντμηση ονόματος Σχηματισμού. Π.χ. 3η Μ/Κ ΤΑΞ. */
	private String formation;
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
	/** ΑΔΑ διαταγής συγκρότησης επιτροπών. */
	private String orderCommitteeId;
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
	/** Πρόεδρος Επιτροπής Αξιολόγησης Ενστάσεων. */
	private Person objectionChief;
	/** Α' Μέλος Επιτροπής Αξιολόγησης Ενστάσεων. */
	private Person objectionMemberA;
	/** Β' Μέλος Επιτροπής Αξιολόγησης Ενστάσεων. */
	private Person objectionMemberB;

	/** Αρχικοποίηση του αντικειμένου. */
	UnitInfo() {}
	/** Αρχικοποίηση του αντικειμένου από άλλο αντικείμενο του τύπου.
	 * Γίνεται αντιγραφή (copy constractor) προκειμένου να αποφεύγεται π.χ. να τροποποιούμε τα στοιχεία σε
	 * μια δαπάνη και να τροποποιούνται και τα στοιχεία στην αντίστοιχη καρτέλα με τα στοιχεία Μονάδας. */
	UnitInfo(UnitInfo p) {
		checkAuthority   = p.checkAuthority;
		formation        = p.formation;
		unitFull         = p.unitFull;
		unit             = p.unit;
		city             = p.city;
		address          = p.address;
		zipCode          = p.zipCode;
		office           = p.office;
		operatorTitle    = p.operatorTitle;
		phone            = p.phone;
		commander        = p.commander;
		deputy_commander = p.deputy_commander;
		operator         = p.operator;
		accountant       = p.accountant;
		orderCommittee   = p.orderCommittee;
		orderCommitteeId = p.orderCommitteeId;
		supplyChief      = p.supplyChief;
		supplyMemberA    = p.supplyMemberA;
		supplyMemberB    = p.supplyMemberB;
		serviceChief     = p.serviceChief;
		serviceMemberA   = p.serviceMemberA;
		serviceMemberB   = p.serviceMemberB;
		tenderChief      = p.tenderChief;
		tenderMemberA    = p.tenderMemberA;
		tenderMemberB    = p.tenderMemberB;
		objectionChief   = p.objectionChief;
		objectionMemberA = p.objectionMemberA;
		objectionMemberB = p.objectionMemberB;
	}

	/** Αρχικοποιεί τα στοιχεία της Μονάδας από έναν node δεδομένων του unserialize().
	 * @param node Ο node δεδομένων
	 * @throws Exception Αν ο node δεν είναι αντικείμενο */
	UnitInfo(Node node) throws Exception {
		if (!node.isObject()) throw new Exception("Για στοιχεία Μονάδας, αναμένονταν αντικείμενο");
		checkAuthority   = node.getField(H[0]).getString();
		formation        = node.getField(H[1]).getString();
		unitFull         = node.getField(H[2]).getString();
		unit             = node.getField(H[3]).getString();
		city             = node.getField(H[4]).getString();
		address          = node.getField(H[5]).getString();
		zipCode          = node.getField(H[6]).getString();
		office           = node.getField(H[7]).getString();
		operatorTitle    = node.getField(H[8]).getString();
		phone            = node.getField(H[9]).getString();
		commander        = getPerson(node.getField(H[10]));
		deputy_commander = getPerson(node.getField(H[11]));
		operator         = getPerson(node.getField(H[12]));
		accountant       = getPerson(node.getField(H[13]));
		orderCommittee   = node.getField(H[14]).getString();
		orderCommitteeId = node.getField(H[15]).getString();
		supplyChief      = getPerson(node.getField(H[16]));
		supplyMemberA    = getPerson(node.getField(H[17]));
		supplyMemberB    = getPerson(node.getField(H[18]));
		serviceChief     = getPerson(node.getField(H[19]));
		serviceMemberA   = getPerson(node.getField(H[20]));
		serviceMemberB   = getPerson(node.getField(H[21]));
		tenderChief      = getPerson(node.getField(H[22]));
		tenderMemberA    = getPerson(node.getField(H[23]));
		tenderMemberB    = getPerson(node.getField(H[24]));
		objectionChief   = getPerson(node.getField(H[25]));
		objectionMemberA = getPerson(node.getField(H[26]));
		objectionMemberB = getPerson(node.getField(H[27]));
	}

	/** Αρχικοποιεί ένα πρόσωπο από έναν node δεδομένων του unserialize().
	 * @param node Ο node δεδομένων
	 * @returns Το πρόσωπο, ή null αν προέκυψε σφάλμα */
	static Person getPerson(Node node) {
		try { return new Person(node); }
		catch(Exception e) { return null; }
	}

	/** Ονόματα πεδίων αποθήκευσης. */
	static final String[] H = {
		"Ελέγχουσα Αρχή", "Σχηματισμός", "Μονάδα Πλήρες", "Μονάδα", "Έδρα", "Διεύθυνση", "Τ.Κ.",
		"Γραφείο", "Ιδιότητα Αξκου", "Τηλέφωνο", "Δκτης", "ΕΟΥ", "Αξκος Γραφείου", "Δχστης",
		ΔγηΣυγκρότησηςΕπιτροπών.toString(), "ΑΔΑ Δγης Συγκρότησης Επιτροπών",
		"Πρόεδρος Παραλαβής Προμηθειών", "Α Μέλος Παραλαβής Προμηθειών", "Β Μέλος Παραλαβής Προμηθειών",
		"Πρόεδρος Παραλαβής Υπηρεσιών", "Α Μέλος Παραλαβής Υπηρεσιών", "Β Μέλος Παραλαβής Υπηρεσιών",
		"Πρόεδρος Διαγωνισμών", "Α Μέλος Διαγωνισμών", "Β Μέλος Διαγωνισμών",
		"Πρόεδρος Ενστάσεων", "Α Μέλος Ενστάσεων", "Β Μέλος Ενστάσεων",
	};

	/** Επιστρέφει array με όλα τα πρόσωπα που εμπλέκονται στα αμετάβλητα στοιχεία.
	 * Χρησιμοποιείται στην εισαγωγή προσώπων στο πρόγραμμα, από αμετάβλητα στοιχεία μιας δαπάνης.
	 * @returns Το array με τα πρόσωπα */
	Person[] getPersonnel() {
		return new Person[] {
			commander, deputy_commander, operator, accountant,
			supplyChief, supplyMemberA, supplyMemberB,
			serviceChief, serviceMemberA, serviceMemberB,
			tenderChief, tenderMemberA, tenderMemberB,
			objectionChief, objectionMemberA, objectionMemberB
		};
	}

	@Override public void serialize(VariableFields fields) {
		if (checkAuthority != null)   fields.add (H[0],  checkAuthority);
		if (formation != null)        fields.add (H[1],  formation);
		if (unitFull != null)         fields.add (H[2],  unitFull);
		if (unit != null)             fields.add (H[3],  unit);
		if (city != null)             fields.add (H[4],  city);
		if (address != null)          fields.add (H[5],  address);
		if (zipCode != null)          fields.add (H[6],  zipCode);
		if (office != null)           fields.add (H[7],  office);
		if (operatorTitle != null)    fields.add (H[8],  operatorTitle);
		if (phone != null)            fields.add (H[9],  phone);
		if (commander != null)        fields.add (H[10], commander);
		if (deputy_commander != null) fields.add (H[11], deputy_commander);
		if (operator != null)         fields.add (H[12], operator);
		if (accountant != null)       fields.add (H[13], accountant);
		if (orderCommittee != null)   fields.add (H[14], orderCommittee);
		if (orderCommitteeId != null) fields.add (H[15], orderCommitteeId);
		if (supplyChief != null)      fields.add (H[16], supplyChief);
		if (supplyMemberA != null)    fields.add (H[17], supplyMemberA);
		if (supplyMemberB != null)    fields.add (H[18], supplyMemberB);
		if (serviceChief != null)     fields.add (H[19], serviceChief);
		if (serviceMemberA != null)   fields.add (H[20], serviceMemberA);
		if (serviceMemberB != null)   fields.add (H[21], serviceMemberB);
		if (tenderChief != null)      fields.add (H[22], tenderChief);
		if (tenderMemberA != null)    fields.add (H[23], tenderMemberA);
		if (tenderMemberB != null)    fields.add (H[24], tenderMemberB);
		if (objectionChief != null)   fields.add (H[25], objectionChief);
		if (objectionMemberA != null) fields.add (H[26], objectionMemberA);
		if (objectionMemberB != null) fields.add (H[27], objectionMemberB);
	}

	@Override public Object getCell(int index) {
		switch(index) {
			case 0: return null;	// Επικεφαλίδα
			case 1: return checkAuthority;
			case 2: return formation;
			case 3: return unitFull;
			case 4: return unit;
			case 5: return city;
			case 6: return address;
			case 7: return zipCode;
			case 8: return office;
			case 9: return operatorTitle;
			case 10: return phone;
			case 11: return commander;
			case 12: return deputy_commander;
			case 13: return operator;
			case 14: return accountant;
			case 15: return null;	// Επικεφαλίδα
			case 16: return orderCommittee;
			case 17: return orderCommitteeId;
			case 18: return supplyChief;
			case 19: return supplyMemberA;
			case 20: return supplyMemberB;
			case 21: return serviceChief;
			case 22: return serviceMemberA;
			case 23: return serviceMemberB;
			case 24: return tenderChief;
			case 25: return tenderMemberA;
			case 26: return tenderMemberB;
			case 27: return objectionChief;
			case 28: return objectionMemberA;
			default: return objectionMemberB;
		}
	}

	@Override public void setCell(int index, Object value) {
		switch(index) {
			//case 0: break;
			case 1: checkAuthority    = getString(value); break;
			case 2: formation         = getString(value); break;
			case 3: unitFull          = getString(value); break;
			case 4: unit              = getString(value); break;
			case 5: city              = getString(value); break;
			case 6: address           = getString(value); break;
			case 7: zipCode           = getString(value); break;
			case 8: office            = getString(value); break;
			case 9: operatorTitle     = getString(value); break;
			case 10: phone            = getString(value); break;
			case 11: commander        = (Person) value; break;
			case 12: deputy_commander = (Person) value; break;
			case 13: operator         = (Person) value; break;
			case 14: accountant       = (Person) value; break;
			//case 15: break;
			case 16: orderCommittee   = getString(value); break;
			case 17: orderCommitteeId = getString(value); break;
			case 18: supplyChief      = (Person) value; break;
			case 19: supplyMemberA    = (Person) value; break;
			case 20: supplyMemberB    = (Person) value; break;
			case 21: serviceChief     = (Person) value; break;
			case 22: serviceMemberA   = (Person) value; break;
			case 23: serviceMemberB   = (Person) value; break;
			case 24: tenderChief      = (Person) value; break;
			case 25: tenderMemberA    = (Person) value; break;
			case 26: tenderMemberB    = (Person) value; break;
			case 27: objectionChief   = (Person) value; break;
			case 28: objectionMemberA = (Person) value; break;
			case 29: objectionMemberB = (Person) value; break;
		}
	}
}
