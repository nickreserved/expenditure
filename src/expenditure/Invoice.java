package expenditure;

import static expenditure.Contractor.Type.ARMY;
import static expenditure.Contractor.Type.PRIVATE_SECTOR;
import static expenditure.Contractor.Type.PUBLIC_SERVICES;
import static expenditure.Deduction.D0;
import static expenditure.Deduction.D0_06216;
import static expenditure.Deduction.D0_13468;
import static expenditure.Deduction.D0_26216;
import static expenditure.Deduction.D0_33468;
import static expenditure.Deduction.D14;
import static expenditure.Deduction.D14_096;
import static expenditure.Deduction.D14_15816;
import static expenditure.Deduction.D14_23068;
import static expenditure.Deduction.D14_35816;
import static expenditure.Deduction.D14_43068;
import static expenditure.Deduction.D4;
import static expenditure.Deduction.D4_096;
import static expenditure.Deduction.D4_15816;
import static expenditure.Deduction.D4_23068;
import static expenditure.Deduction.D4_35816;
import static expenditure.Deduction.D4_43068;
import expenditure.Expenditure.Financing;
import static expenditure.Expenditure.Financing.ARMY_BUDGET;
import static expenditure.Expenditure.Financing.OWN_PROFITS;
import static expenditure.Expenditure.Financing.PUBLIC_INVESTMENT;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;
import util.PhpSerializer.Node;
import util.PhpSerializer.VariableFields;
import util.PhpSerializer.VariableSerializable;
import util.ResizableTableModel.TableRecord;
import static util.ResizableTableModel.getByte;
import static util.ResizableTableModel.getString;

/** Ένα τιμολόγιο της δαπάνης. */
final class Invoice implements VariableSerializable, TableRecord {
	/** Η ταυτότητα του τιμολογίου. */
	private String id;
	/** Η κατηγορία του τιμολογίου. */
	private Type type;
	/** Ο προμηθευτής / εργολάβος / δικαιούχος / ανάδοχος. */
	private Contractor contractor;
	/** Ποσοστιαίες κρατήσεις. */
	private Deduction deduction;
	/** Το ποσοστό ΦΕ του τιμολογίου. */
	private byte incomeTax;
	/** Η σύμβαση στο πλαίσιο της οποίας εκδόθηκε το τιμολόγιο. */
	private Contract contract;
	/** Λίστα με τα είδη του τιμολογίου. */
	final ArrayList<InvoiceItem> items = new ArrayList<>();

	/** Οι αξίες σε € του τιμολογίου.
	 * Η καθαρή αξία, το ΦΠΑ, το καταλογιστέο, οι κρατήσεις, το πληρωτέο, το ΦΕ και το υπόλοιπο
	 * πληρωτέο, όλα στογγυλοποιημένα στο δεύτερο δεκαδικό ψηφίο και με την αυτή σειρά.
	 * <p>Τα δεδομένα αυτά δεν αποθηκεύονται. Υπολογίζονται προκειμένου να χρησιμοποιηθούν από τον
	 * πίνακα που προβάλει τα αθροίσματα των τιμολογίων (στην καρτέλα «Τιμολόγια», ο τρίτος πίνακας
	 * κάτω-κάτω). */
	final double[] prices = new double[7];

	/** Η δαπάνη στην οποία ανήκει το τιμολόγιο. */
	final Expenditure parent;

	/** Αρχικοποίει το τιμολόγιο με τις προκαθορισμένες τιμές.
	 * @param parent Η δαπάνη στην οποία ανήκει το τιμολόγιο */
	Invoice(Expenditure parent) { this. parent = parent; type = Type.SUPPLIES; }

	/** Αρχικοποιεί ένα τιμολόγιο από έναν node δεδομένων του unserialize().
	 * @param parent Η δαπάνη στην οποία ανήκει το τιμολόγιο
	 * @param node Ο node δεδομένων
	 * @throws Exception Αν ο node δεν είναι αντικείμενο */
	Invoice(Expenditure parent, Node node) throws Exception {
		this.parent = parent;
		id               = node.getField(H[0]).getString();
		type             = Type.valueOf(node.getField(H[1]).getString());
		try { contractor = new Contractor(node.getField(H[2])); }
		catch(Exception e) {}
		try { deduction  = new Deduction(node.getField(H[3])); }
		catch(Exception e) {}
		incomeTax        = (byte) node.getField(H[4]).getInteger();
		// Ανάγνωση σύμβασης αν υπάρχει (δίνεται με index στη λίστα συμβάσεων της δαπάνης)
		Node n           = node.getField(H[5]);
		if (n.isInteger()) {
			int idx = (int) n.getInteger();
			if (idx >=0 && idx < parent.contracts.size()) contract = parent.contracts.get(idx);
		}
		// Ανάγνωση ειδών τιμολογίου
		n                = node.getField(H[6]);
		if (n.isExist()) {
			if (!n.isArray()) throw new Exception("Για τιμολόγιο, αναμένονταν λίστα ειδών");
			for (Node i : n.getArray())
				items.add(new InvoiceItem(this, i));
		}
		// Υπολογισμοί από τα αποθηκευμένα δεδομένα
		calcNet(); calcVAT(); calcDeduction(); calcMixedPayable(); calcIncomeTax();
		calcPayableMinusIncomeTax();
		recalcReport(prices);
	}

	/** Ονόματα πεδίων αποθήκευσης.
	 * Αν αλλάξει οτιδήποτε, πρέπει να αναπροσαρμοστεί η κλήση MainFrame.createInvoicesPanel() */
	static final String[] H = {
		"Τιμολόγιο", "Κατηγορία", "Δικαιούχος", "Κρατήσεις", "ΦΕ", "Σύμβαση", "Είδη"
	};

	/** Επιστρέφει τον τύπο του τιμολογίου. */
	Type getType() { return type; }
	/** Επιστρέφει τον δικαιούχο του τιμολογίου. */
	Contractor getContractor() { return contract == null ? contractor : contract.getContractor(); }
	/** Επιστρέφει τις κρατήσεις του τιμολογίου. */
	Deduction getDeduction() { return deduction; }
	/** Η σύμβαση στο πλαίσιο της οποίας εκδόθηκε το τιμολόγιο. */
	Contract getContract() { return contract; }

	@Override public void serialize(VariableFields fields) {
		if (id != null)         fields.add (H[0], id);
								fields.add (H[1], type.toString());	// Ο τύπος του τιμολογίου δεν είναι ποτέ null
		if (contractor != null) fields.add (H[2], contractor);
								fields.add (H[3], deduction);	// Οι κρατήσεις και null, εξάγονται
								fields.add (H[4], incomeTax);	// Το ΦΕ και μηδενικό, εξάγεται
		if (contract != null)   fields.add (H[5], parent.contracts.indexOf(contract));	// Αναφορά στη λίστα συμβάσεων
		if (!items.isEmpty())   fields.addV(H[6], items);
	}

	@Override public Object getCell(int index) {
		switch(index) {
			case 0: return id;
			case 1: return type;
			case 2: return contract == null ? contractor : contract;
			case 3: return deduction;
			case 4: return incomeTax;
			default: return contract;
		}
	}

	@Override public void setCell(int index, Object value) {
		switch(index) {
			case 0: id = getString(value); break;
			case 1:		// Θέτει τον τύπο του τιμολογίου
				if (value != type)
					if (parent.isSmart()) {
						Type t = (Type) value;
						boolean hasVat1 = hasVAT(type, contractor);
						boolean hasVat2 = hasVAT(t, contractor);
						type = t;
						if (hasVat1 != hasVat2) setVAT(hasVat2);
						recalcFromInvoicesGroupNet(calcInvoicesGroupNet());
					} else type = (Type) value;
				break;
			case 2:		// Θέτει τον δικαιούχο του τιμολογίου ή τη σύμβαση
				if (value != contract && value != contractor) {
					// Κρατάει τις προηγούμενες τιμές γιατί θα χρειαστούν στους υπολογισμούς
					Contract oldContract = contract;
					Contractor oldContractor = getContractor();
					// Αλλάζει σύμβαση ή δικαιούχο
					if (contract != null) sub(contract.prices, prices);
					if (value instanceof Contract) {
						contractor = null;
						contract = (Contract) value;
						add(contract.prices, prices);
					} else {
						contract = null;
						contractor = (Contractor) value;
					}
					if (parent.isSmart()) {
						// Επανυπολογίζει τα τιμολόγια της παλιάς σύμβασης ή δικαιούχου
						recalcInvoicesGroupFromNet(parent, oldContract, oldContractor);
						// Θέτει το ΦΠΑ αν έχει αλλάξει
						Contractor c = getContractor();
						Contractor.Type ct1 = oldContractor != null ? oldContractor.getType() : null;
						Contractor.Type ct2 =             c != null ?             c.getType() : null;
						if (ct1 != ct2) {
							boolean hasVat1 = hasVAT(type, oldContractor);
							boolean hasVat2 = hasVAT(type, c);
							if (hasVat1 != hasVat2) setVAT(hasVat2);
						}
						// Επανυπολογίζει τα τιμολόγια της νέας σύμβασης ή δικαιούχου
						recalcInvoicesGroupFromNet();
					}
				}
				break;
			case 3:		// Θέτει τις κρατήσεις
				if (!parent.isSmart() && value != deduction) {
					deduction = (Deduction) value;
					double[] d = Arrays.copyOf(prices, prices.length);
					calcDeduction(); calcMixedPayable(); calcIncomeTax(); calcPayableMinusIncomeTax();
					recalcReportDiff(d);
				}
				break;
			case 4: {	// Θέτει το ποσοστό ΦΕ
				byte b = getByte(value);
				if (!parent.isSmart() && b != incomeTax) {
					incomeTax = b;
					double[] d = Arrays.copyOf(prices, prices.length);
					calcIncomeTax(); calcPayableMinusIncomeTax();
					recalcReportDiff(d);
				}
				break;
			}
		}
	}

	@Override public boolean isEmpty() { return false; }	// Μόνο με DELETE διαγράφεται τιμολόγιο

	/** Τροποποιεί το ΦΠΑ των ειδών του τιμολογίου με βάση τα δεδομένα της δαπάνης.
	 * Επίσης, ενημερώνει τις αξίες του τιμολογίου που τροποποιήθηκαν συνέπεια της αλλαγής ΦΠΑ.
	 * @param nonZero Το ΦΠΑ των ειδών του τιμολογίου δεν είναι μηδέν */
	private void setVAT(boolean nonZero) {
		if (0 != items.stream().filter(nonZero ? i -> i.setVATnot0() : i -> i.setVAT0()).count()) {
			double[] d = Arrays.copyOf(prices, prices.length);
			calcVAT(); calcMixedPayable(); calcPayableMinusIncomeTax();
			recalcReportDiff(d);
		}
	}

	/** Τροποποιεί τις ποσοστιαίες κρατήσεις του τιμολογίου με βάση τα δεδομένα της δαπάνης.
	 * @param net Η καθαρή αξία όλων των τιμολογίων του ίδιου δικαιούχου
	 * @return Οι κρατήσεις άλλαξαν */
	private boolean setDeductionPercent(double net) {
		Deduction d = calcDeduction(type, getContractor(), net, parent.getFinancing());
		if (deduction != null && !deduction.equals(d) || deduction == null && d != null) {
			deduction = d;
			return true;
		} else return false;
	}

	/** Τροποποιεί το ποσοστό ΦΕ του τιμολογίου με βάση τα δεδομένα της δαπάνης.
	 * @param net Η καθαρή αξία όλων των τιμολογίων του ίδιου δικαιούχου
	 * @return Το ΦΕ άλλαξε */
	private boolean setIncomeTaxPercent(double net) {
		byte b = calcIncomeTax(type, getContractor(), net, parent.isConstruction());
		if (b != incomeTax) {
			incomeTax = b;
			return true;
		} else return false;
	}

	/** Υπολογισμός της καθαρής αξίας του τιμολογίου. */
	private void calcNet() {
		prices[0] = Math.round(items.stream().mapToDouble(i -> i.getNetPrice()).sum() * 100) / 100.0;
	}
	/** Υπολογισμός του ΦΠΑ του τιμολογίου. */
	private void calcVAT() {
		prices[1] = Math.round(items.stream().mapToDouble(i -> i.getVATPrice()).sum() * 100) / 100.0;
	}
	/** Υπολογισμός των κρατήσεων του τιμολογίου.
	 * Θα πρέπει να έχει υπολογιστεί η καθαρή αξία. */
	private void calcDeduction() {
		prices[3] = deduction == null ? 0 : Math.round(deduction.sum() * prices[0]) / 100.0;
	}
	/** Υπολογισμός του καταλογιστέου και του πληρωτέου του τιμολογίου.
	 * Θα πρέπει να έχει υπολογιστεί η καθαρή αξία, το ΦΠΑ και οι κρατήσεις. */
	private void calcMixedPayable() {
		// Καταλογιστέο
		double d = prices[0] + prices[1];
		Contractor c = getContractor();
		boolean wePayDeductions = c != null && (c.getType() == PUBLIC_SERVICES || c.getType() == ARMY);
		if (wePayDeductions) d += prices[3];
		prices[2] = Math.round(d * 100) / 100.0;
		// Πληρωτέο
		prices[4] = Math.round((prices[2] - prices[3]) * 100) / 100.0;
	}
	/** Υπολογισμός του ΦΕ του τιμολογίου.
	 * Θα πρέπει να έχει υπολογιστεί η καθαρή αξία και οι κρατήσεις. */
	private void calcIncomeTax() {
		// Υπολογισμός καθαρής αξίας για υπολογισμό ΦΕ
		double priceNetIncomeTax = prices[0];
		if (incomeTax != 3) priceNetIncomeTax -= prices[3];
		// Υπολογισμός ΦΕ
		prices[5] = Math.round(priceNetIncomeTax * incomeTax) / 100.0;
	}
	/** Υπολογισμός του πληρωτέου του τιμολογίου.
	 * Θα πρέπει να έχει υπολογιστεί το πληρωτέο και το ΦΕ. */
	private void calcPayableMinusIncomeTax() {
		prices[6] = Math.round((prices[4] - prices[5]) * 100) / 100.0;
	}

	/** Το τιμολόγιο επίκειται να διαγραφεί.
	 * Αφαιρούνται οι αξίες του τιμολογίου από τις αξίες της σύμβασης και της δαπάνης. */
	void recalcRemove() {
		neg(prices); recalcReport(prices);
		if (parent.isSmart()) {
			Contract cact = contract;		// Κρατά σύμβαση και δικαιούχο και μετά τα μηδενίζει...
			Contractor cr = contractor;		// ...για να μην επηρρεάσουν την ομάδα τιμολογίων
			contract = null; contractor = null;
			// Επανυπολογίζει τα υπόλοιπα τιμολόγια της κοινής ομάδας τιμολογίων
			recalcInvoicesGroupFromNet(parent, cact, cr);
		}
	}

	/** Ενημερώνει το τιμολόγιο ότι κάποιο από τα είδη του τροποποιήθηκε.
	 * Δύναται να επηρεάζει κρατήσεις και ΦΕ. Επανυπολογίζει τις τιμές του τιμολογίου (Καθαρή αξία
	 * κτλ). */
	void recalcFromItems() {
		double[] d = Arrays.copyOf(prices, prices.length);
		calcNet(); calcVAT();
		if (prices[0] != d[0] || prices[1] != d[1]) {	// Υπάρχουν αλλαγές
			calcDeduction(); calcMixedPayable(); calcIncomeTax(); calcPayableMinusIncomeTax();
			recalcReportDiff(d);
			if (parent.isSmart()) recalcInvoicesGroupFromNet();
		}
	}

	/** Ενημερώνει το τιμολόγιο ότι ενεργοποιήθηκε ο αυτοματισμός.
	 * Δύναται να επηρεάζει ΦΠΑ, κρατήσεις και ΦΕ. Επανυπολογίζει τις τιμές του τιμολογίου. Ο
	 * αυτοματισμός είναι ενεργός. */
	void recalcFromSmart() {
		setVAT(hasVAT(type, getContractor()));
		recalcInvoicesGroupFromNet();
	}

	/** Ενημερώνει το τιμολόγιο ότι τροποποιήθηκε η χρηματοδότηση δαπάνης.
	 * Δύναται να επηρεάζει τις κρατήσεις. Επανυπολογίζει τις τιμές του τιμολογίου. Ο αυτοματισμός
	 * είναι ενεργός. */
	void recalcFromFinancing() {
		double[] d = Arrays.copyOf(prices, prices.length);
		if (setDeductionPercent(calcInvoicesGroupNet())) {
			calcDeduction(); calcMixedPayable(); calcIncomeTax(); calcPayableMinusIncomeTax();
			recalcReportDiff(d);
		}
	}

	/** Ενημερώνει το τιμολόγιο ότι τροποποιήθηκε ο τύπος δαπάνης.
	 * Δύναται να επηρεάζει το ΦΕ. Επανυπολογίζει τις τιμές του τιμολογίου. Ο αυτοματισμός είναι
	 * ενεργός. */
	void recalcFromConstruction() {
		double[] d = Arrays.copyOf(prices, prices.length);
		if (setIncomeTaxPercent(calcInvoicesGroupNet())) {
			calcIncomeTax(); calcPayableMinusIncomeTax();
			recalcReportDiff(d);
		}
	}

	/** Επανυπολογίζει την ομάδα τιμολογίων στην οποία ανήκει το τρέχον τιμολόγιο.
	 * Ομάδα τιμολογίων είναι όλα τα τιμολόγια που ανήκουν στη δοσμένη σύμβαση, ή αν αυτή είναι null,
	 * όλα τα τιμολόγια του δοσμένου δικαιούχου ή αν κι αυτός είναι null, το τρέχον τιμολόγιο μόνο.
	 * <p>Δύναται να επηρεαστούν κρατήσεις και ΦΕ αυτών των τιμολογίων και να τεθεί ο δικαιούχος και
	 * η σύμβασή τους.
	 * <p>Ο αυτοματισμός είναι ενεργός. */
	private void recalcInvoicesGroupFromNet() {
		if (contract != null) recalcContractInvoicesFromNet(parent, contract);
		else if (contractor != null) recalcContractorInvoicesFromNet(parent, contractor);
		else recalcFromInvoicesGroupNet(prices[0]);
	}

	/** Επανυπολογίζει την ομάδα τιμολογίων.
	 * Ομάδα τιμολογίων είναι όλα τα τιμολόγια που ανήκουν στη δοσμένη σύμβαση, ή αν αυτή είναι null,
	 * όλα τα τιμολόγια του δοσμένου δικαιούχου.
	 * <p>Δύναται να επηρεαστούν κρατήσεις και ΦΕ αυτών των τιμολογίων και να τεθεί ο δικαιούχος και
	 * η σύμβασή τους.
	 * <p>Ο αυτοματισμός είναι ενεργός.
	 * @param expenditure Η δαπάνη στην οποία αναφερόμαστε
	 * @param contract Η σύμβαση της ομάδας τιμολογίων ή null αν μας ενδιαφέρει ο δικαιούχος
	 * @param contractor Ο δικαιούχος της ομάδας τιμολογίων, ο οποίος λαμβάνεται υπόψη μόνο αν η
	 * σύμβαση είναι null */
	static private void recalcInvoicesGroupFromNet(Expenditure expenditure, Contract contract,
			Contractor contractor) {
		if (contract != null) recalcContractInvoicesFromNet(expenditure, contract);
		else if (contractor != null) recalcContractorInvoicesFromNet(expenditure, contractor);
	}

	/** Επανυπολογίζει την ομάδα τιμολογίων της ίδιας σύμβασης.
	 * Δύναται να επηρεαστούν κρατήσεις και ΦΕ αυτών των τιμολογίων και να τεθεί ο δικαιούχος και
	 * η σύμβασή τους.
	 * <p>Ο αυτοματισμός είναι ενεργός.
	 * @param expenditure Η δαπάνη στην οποία αναφερόμαστε
	 * @param contract Η σύμβαση του τιμολογίου. Δεν πρέπει να είναι null. */
	static private void recalcContractInvoicesFromNet(Expenditure expenditure, Contract contract) {
		Contractor contractor = contract.getContractor();
		Predicate<Invoice> pred = contractor == null
				? i -> contract == i.contract
				: i -> contract == i.contract || contractor.equals(i.getContractor());
		double net = calcPredicateNet(expenditure, pred);
		// Έλεγχος αν απαιτείται η σύμβαση
		Contract cact = contract;
		if (net <= CONTRACT_PRICE) cact = null;
		else cact.setTenderType(net);
		// Πιθανός επανυπολογισμός για όλα τα τιμολόγια της σύμβασης
		recalcInvoicesGroupFromNet(expenditure, pred, net, cact, contractor);
	}

	/** Επανυπολογίζει την ομάδα τιμολογίων του ίδιου δικαιούχου.
	 * Δύναται να επηρεαστούν κρατήσεις και ΦΕ αυτών των τιμολογίων και να τεθεί ο δικαιούχος και
	 * η σύμβασή τους.
	 * <p>Ο αυτοματισμός είναι ενεργός.
	 * @param expenditure Η δαπάνη στην οποία αναφερόμαστε
	 * @param contractor Ο δικαιούχος του οποίου τα τιμολόγια θα επιστρέψουμε την καθαρή αξία. Δεν
	 * πρέπει να είναι null. */
	static private void recalcContractorInvoicesFromNet(Expenditure expenditure, Contractor contractor) {
		Predicate<Invoice> pred = i -> contractor.equals(i.getContractor());
		double net = calcPredicateNet(expenditure, pred);
		// Εύρεση ή δημιουργία ή αφαίρεση σύμβασης
		Contract contract = null;
		if (net > CONTRACT_PRICE) {
			contract = expenditure.contracts.stream()
					.filter(i -> contractor.equals(i.getContractor()))
					.findFirst().orElse(null);
			if (contract == null) {
				contract = new Contract(contractor);
				expenditure.contracts.add(contract);
			}
			contract.setTenderType(net);
		}
		// Πιθανός επανυπολογισμός για όλα τα τιμολόγια της σύμβασης
		recalcInvoicesGroupFromNet(expenditure, pred, net, contract, contractor);
	}

	/** Επανυπολογίζει τις αξίες όλων των τιμολογίων του ίδιου δικαιούχου.
	 * Δύναται να επηρεάζει κρατήσεις και ΦΕ αυτών των τιμολογίων και να θέσει δικαιούχο και σύμβαση.
	 * <p>Ο αυτοματισμός είναι ενεργός.
	 * @param expenditure Η δαπάνη στην οποία αναφερόμαστε
	 * @param pred Επιλογέας τιμολογίων, συνήθως του ίδιου δικαιούχου ή της ίδιας σύμβασης
	 * @param net Το άθροισμα της καθαρής αξίας όλων των τιμολογίων που ικανοποιούν το predicate
	 * @param contract Η σύμβαση στην οποία θα ανήκουν τα τιμολόγια από εδώ και πέρα, ή null
	 * @param contractor Ο δικαιούχος στον οποίο θα ανήκουν τα τιμολόγια από εδώ και πέρα, ή null αν
	 * έχει οριστεί η σύμβαση ή δεν υπάρχει δικαιούχος (ακόμα) */
	static private void recalcInvoicesGroupFromNet(Expenditure expenditure, Predicate<Invoice> pred,
			double net, Contract contract, Contractor contractor) {
		expenditure.invoices.stream().filter(pred)
				.forEach(i -> {
					if (i.contract != contract) {
						if (i.contract != null) sub(i.contract.prices, i.prices);
						if (contract != null) {
							add(contract.prices, i.prices);
							i.contractor = null;
						} else i.contractor = contractor;
						i.contract = contract;
					}
					i.recalcFromInvoicesGroupNet(net);
				});
	}

	/** Ενημερώνει το τιμολόγιο ότι η καθαρή αξία όλων των τιμολογίων του ίδιου δικαιούχου άλλαξε.
	 * Δύναται να επηρεάζει κρατήσεις και ΦΕ. Επανυπολογίζει τις τιμές του τιμολογίου. Ο
	 * αυτοματισμός είναι ενεργός.
	 * @param net Η καθαρή αξία όλων των τιμολογίων του ίδιου δικαιούχου ή της ίδιας σύμβασης */
	private void recalcFromInvoicesGroupNet(double net) {
		double[] d = Arrays.copyOf(prices, prices.length);
		boolean deduct = setDeductionPercent(net);
		if (deduct) { calcDeduction(); calcMixedPayable(); }
		if (setIncomeTaxPercent(net) || deduct) {	// <-- αλλαγή διάταξης απαγορεύται!
			calcIncomeTax(); calcPayableMinusIncomeTax();
			recalcReportDiff(d);
		}
	}

	/** Κόστος πάνω από το οποίο έχουμε σύμβαση. */
	static final private int CONTRACT_PRICE = 2500;

	/** Ενημερώνει ένα στοιχείο του πίνακα με τα αθροίσματα τιμών τιμολογίων από τη διαφορά παλιάς και νέας τιμής του τιμολογίου.
	 * Η στήλη με το άθροισμα των τιμολογίων της τρέχουσας σύμβασης και η στήλη με το άθροισμα όλων
	 * των τιμολογίων ενημερώνεται.
	 * @param d Μια τιμή του τιμολογίου πριν από κάποια αλλαγή που θα αφαιρεθεί από την τρέχουσα τιμή
	 * του τιμολογίου και θα προστεθεί στην υπάρχουσα τιμή των αντίστοιχων στηλών
	 * @param index Ο δείκτης της τιμής του τιμολογίου, με τιμές 0 ως 6 για καθαρή αξία, ΦΠΑ,
	 * καταλογιστέο, κρατήσεις, πληρωτέο, ΦΕ, υπόλοιπο πληρωτέο */
	private void recalcReportDiff(double d, int index) {
		d = Math.round((prices[index] - d) * 100) / 100.0;
		if (contract != null)
			contract.prices[index] = Math.round((contract.prices[index] + d) * 100) / 100.0;
		parent.prices[index] = Math.round((parent.prices[index] + d) * 100) / 100.0;
	}

	/** Ενημερώνει τον πίνακα με τα αθροίσματα τιμών τιμολογίων από τις διαφορές παλιών και νέων τιμών του τιμολογίου.
	 * Η στήλη με το άθροισμα των τιμολογίων της τρέχουσας σύμβασης και η στήλη με το άθροισμα όλων
	 * των τιμολογίων ενημερώνεται.
	 * @param d Array με τις 7 τιμές του τιμολογίου πριν από κάποια αλλαγή που θα αφαιρεθούν από τις
	 * τρέχουσες 7 τιμές του τιμολογίου και θα προστεθούν στις υπάρχουσες τιμές των αντίστοιχων στηλών */
	private void recalcReportDiff(double[] d) { sub(d, prices); neg(d); recalcReport(d); }

	/** Ενημερώνει τον πίνακα με τα αθροίσματα τιμών τιμολογίων.
	 * Η στήλη με το άθροισμα των τιμολογίων της τρέχουσας σύμβασης και η στήλη με το άθροισμα όλων
	 * των τιμολογίων ενημερώνεται.
	 * @param d Array με τις διαφορές των 7 τιμών του τιμολογίου που θα προστεθούν στις υπάρχουσες
	 * τιμές των αντίστοιχων στηλών */
	private void recalcReport(double[] d) {
		if (contract != null) add(contract.prices, d);
		add(parent.prices, d);
	}

	/** Προσθέτει το διάνυσμα a στο διάνυσμα to.
	 * @param to Ο προσθεταίος πριν την κλήση και το αποτέλεσμα της πρόσθεσης μετά την κλήση
	 * @param a Ο 2ος προσθεταίος */
	static private void add(double[] to, double[] a) {
		for (int z = 0; z < to.length; ++z)
			to[z] = Math.round((to[z] + a[z]) * 100) / 100.0;
	}

	/** Μετατρέπει το διάνυσμα a στον αντίθετό του.
	 * @param a Ένα διάνυσμα πριν την κλήση και το αντίθετό του μετά την κλήση */
	static private void neg(double[] a) {
		for (int z = 0; z < a.length; ++z)
			a[z] = -a[z];
	}

	/** Αφαιρεί το διάνυσμα a από το διάνυσμα to.
	 * @param to Ο αφαιρεταίος πριν την κλήση και το αποτέλεσμα της αφαίρεσης μετά την κλήση
	 * @param a Ο αφαιρέτης */
	static private void sub(double[] to, double[] a) {
		for (int z = 0; z < to.length; ++z)
			to[z] = Math.round((to[z] - a[z]) * 100) / 100.0;
	}

	/** Επιστρέφει το άθροισμα των καθαρών αξιών όλων των τιμολογίων της ίδιας σύμβασης ή δικαιούχου.
	 * Αν το τιμολόγιο ανήκει σε σύμβαση, επιστρέφει την καθαρή αξία όλων των τιμολογίων στην ίδια
	 * σύμβαση. Αν δεν ανήκει σε σύμβαση αλλά έχει οριστεί ο δικαιούχος του, επιστρέφει την καθαρή
	 * αξία όλων των τιμολογίων του ίδιου δικαιούχου. Αν δεν έχει οριστεί ούτε δικαιούχος, τότε
	 * επιστρέφει μόνο την καθαρή αξία του τιμολογίου.
	 * @return Το άθροισμα των καθαρών αξιών όλων των τιμολογίων του ίδιου δικαιούχου ή σύμβασης */
	private double calcInvoicesGroupNet() {
		if (contract != null) return calcPredicateNet(parent, i -> contract == i.contract);
		else if (contractor != null) return calcPredicateNet(parent, i -> contractor.equals(i.contractor));
		else return prices[0];
	}

	/** Επιστρέφει το άθροισμα των καθαρών αξιών όλων των τιμολογίων που ικανοποιούν την προϋπόθεση.
	 * @param expenditure Η δαπάνη στην οποία αναφερόμαστε
	 * @param pred Η προϋπόθεση
	 * @return Το άθροισμα των καθαρών αξιών όλων των τιμολογίων που ικανοποιούν την προϋπόθεση */
	static private double calcPredicateNet(Expenditure expenditure, Predicate<Invoice> pred) {
		return expenditure.invoices.stream().filter(pred).mapToDouble(i -> i.prices[0]).sum();
	}

	/** Υπολογίζει ποιο πρέπει να είναι το ποσοστό ΦΕ με βάση τα υπόλοιπα στοιχεία της δαπάνης.
	 * @param type Ο τύπος του τιμολογίου
	 * @param contractor Ο δικαιούχος
	 * @param net Το άθροισμα καθαρών αξιών όλων των τιμολογίων του ίδιου δικαιούχου
	 * @param construction Η δαπάνη είναι δαπάνη έργου
	 * @return Το ποσοστό ΦΕ ή 0 αν κάποια παράμετρος είναι null */
	static private byte calcIncomeTax(Type type, Contractor contractor, double net, boolean construction) {
		if (type == null || contractor == null || contractor.getType() != PRIVATE_SECTOR ||
				!construction && net <= 150) return 0;
		else if (type == Type.SUPPLIES) return 4;
		else if (type == Type.SERVICES)
			if (!construction) return 8;
			else if (net > 300) return 3;
			else return 0;
		else if (type == Type.LIQUID_FUEL) return 1;
		else if (type == Type.ENGINEERING_STUDY || type == Type.STUDY_SUPERVISION) return 20;
		return 0;
	}

	/** Υπολογίζει ποιο πρέπει να είναι το ποσοστό κρατήσεων με βάση τα υπόλοιπα στοιχεία της δαπάνης.
	 * @param type Ο τύπος του τιμολογίου
	 * @param contractor Ο δικαιούχος
	 * @param net Το άθροισμα καθαρών αξιών όλων των τιμολογίων του ίδιου δικαιούχου
	 * @param financing Ο τύπος χρηματοδότησης της δαπάνης
	 * @return Οι κρατήσεις του τιμολογίου ή null αν κάποια παράμετρος είναι null */
	static private Deduction calcDeduction(Type type, Contractor contractor, double net,
			Financing financing) {
		Deduction deduction = null;
		if (type == null || contractor == null || contractor.getType() == null || financing == null);
		else if (type == Type.WATER_ELECTRICITY) deduction = D0;
		else if (contractor.getType() == PRIVATE_SECTOR) {
			if (type == Type.PROPERTY_RENTAL) {
					 if (financing == ARMY_BUDGET) deduction = D4_096;
				else if (financing == OWN_PROFITS) deduction = D14_096;
				else if (financing == PUBLIC_INVESTMENT) deduction = D0;
			} else if (net > 2500) {
				if (type == Type.ENGINEERING_STUDY || type == Type.STUDY_SUPERVISION) {
						 if (financing == ARMY_BUDGET) deduction = D4_43068;
					else if (financing == OWN_PROFITS) deduction = D14_43068;
					else if (financing == PUBLIC_INVESTMENT) deduction = D0_33468;
				} else {// if (type != ENGINEERING_STUDY && type != STUDY_SUPERVISION)
						 if (financing == ARMY_BUDGET) deduction = D4_23068;
					else if (financing == OWN_PROFITS) deduction = D14_23068;
					else if (financing == PUBLIC_INVESTMENT) deduction = D0_13468;
				}
			} else // if (net <= 2500)
				if (type == Type.ENGINEERING_STUDY || type == Type.STUDY_SUPERVISION) {
						 if (financing == ARMY_BUDGET) deduction = D4_35816;
					else if (financing == OWN_PROFITS) deduction = D14_35816;
					else if (financing == PUBLIC_INVESTMENT) deduction = D0_26216;
				} else {
						 if (financing == ARMY_BUDGET) deduction = D4_15816;
					else if (financing == OWN_PROFITS) deduction = D14_15816;
					else if (financing == PUBLIC_INVESTMENT) deduction = D0_06216;
				}
		} else // if (contractor.getType() == ARMY || contractor.getType() == PUBLIC_SERVICES)
			deduction = financing == OWN_PROFITS ? D14: D4;
		return deduction;
	}

	/** Ελέγχει πρέπει να υπάρχει ΦΠΑ με βάση τα υπόλοιπα στοιχεία της δαπάνης.
	 * @param type Ο τύπος του τιμολογίου
	 * @param contractor Ο δικαιούχος
	 * @return Πρέπει να υπάρχει ΦΠΑ (ή κάποια παράμετρος είναι null) */
	static boolean hasVAT(Type type, Contractor contractor) {
		return type == null || contractor == null || contractor.getType() == null ||
				contractor.getType() != ARMY && type != Type.PROPERTY_RENTAL;
	}


	/** Κατηγορίες του τιμολογίου, από τις οποίες προκύπτει το ΦΕ και άλλα δικαιολογητικά. */
	static final class Type {
		/** Ιδιωτική αρχικοποίηση του enum. */
		private Type(String s) { a = s; }
		/** Η κατηγορία του τιμολογίου με κείμενο. */
		final private String a;
		@Override public String toString() { return a; }
		/** Λαμβάνει την κατηγορία του τιμολογίου από το κείμενο περιγραφής του.
		 * Αν το κείμενο είναι εσφαλμένο ή null επιστρέφει SUPPLIES.
		 * @param s Η κατηγορία του τιμολογίου σε κείμενο
		 * @return Η κατηγορία του τιμολογίου */
		static private Type valueOf(String s) {
			if (SERVICES.a.equals(s)) return SERVICES;
			if (LIQUID_FUEL.a.equals(s)) return LIQUID_FUEL;
			if (PROPERTY_RENTAL.a.equals(s)) return PROPERTY_RENTAL;
			if (WATER_ELECTRICITY.a.equals(s)) return WATER_ELECTRICITY;
			if (ENGINEERING_STUDY.a.equals(s)) return ENGINEERING_STUDY;
			if (STUDY_SUPERVISION.a.equals(s)) return STUDY_SUPERVISION;
			return SUPPLIES;
		}
		/** Προμήθεια υλικών. */
		static final private Type SUPPLIES = new Type("Προμήθεια υλικών");
		/** Παροχή υπηρεσιών. */
		static final private Type SERVICES = new Type("Παροχή υπηρεσιών");
		/** Προμήθεια υγρών καυσίμων. */
		static final private Type LIQUID_FUEL = new Type("Προμήθεια υγρών καυσίμων");
		/** Ενοικίαση Ακινήτου. */
		static final private Type PROPERTY_RENTAL = new Type("Μίσθωση ακινήτων");
		/** Λογαριασμοί ύδρευσης - αποχέτευσης ή έργα της ΔΕΗ. */
		static final private Type WATER_ELECTRICITY = new Type("Λογαριασμοί ύδρευσης - αποχέτευσης ή έργα της ΔΕΗ");
		/** Εκπόνηση μελέτης μηχανικού. */
		static final private Type ENGINEERING_STUDY = new Type("Εκπόνηση μελέτης μηχανικού");
		/** Επίβλεψη εφαρμογής μελέτης ή εκπόνηση μελέτης εκτός μηχανικού. */
		static final private Type STUDY_SUPERVISION = new Type("Επίβλεψη εφαρμογής μελέτης ή εκπόνηση μελέτης εκτός μηχανικού");
		/** Επιστρέφει λίστα με όλες τις κατηγορίες τιμολογίου.
		 * @return Λίστα με όλες τις κατηγορίες τιμολογίου */
		static Type[] values() {
			return new Type[] {
				SUPPLIES, SERVICES, LIQUID_FUEL, PROPERTY_RENTAL, WATER_ELECTRICITY,
				ENGINEERING_STUDY, STUDY_SUPERVISION
			};
		}
	}
}