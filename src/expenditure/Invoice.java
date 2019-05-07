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
import static expenditure.Expenditure.FINANCING;
import static expenditure.Invoice.Type.ENGINEERING_STUDY;
import static expenditure.Invoice.Type.PROPERTY_RENTAL;
import static expenditure.Invoice.Type.STUDY_SUPERVISION;
import static expenditure.Invoice.Type.WATER_ELECTRICITY;
import java.util.ArrayList;
import java.util.Arrays;
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
		try { type   = Type.valueOf(node.getField(H[1]).getString()); }
		catch(RuntimeException e) { type = Type.SUPPLIES; }
		try { contractor = new Contractor(node.getField(H[2])); }
		catch(Exception e) { contractor = null; }
		try { deduction  = new Deduction(node.getField(H[3])); }
		catch(Exception e) { deduction = null; }
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

	/** Κατηγορίες του τιμολογίου, από τις οποίες προκύπτει το ΦΕ και άλλα δικαιολογητικά. */
	enum Type {
		/** Προμήθεια υλικών. */
		SUPPLIES,
		/** Παροχή υπηρεσιών. */
		SERVICES,
		/** Προμήθεια υγρών καυσίμων. */
		LIQUID_FUEL,
		/** Ενοικίαση Ακινήτου. */
		PROPERTY_RENTAL,
		/** Λογαριασμοί ύδρευσης - αποχέτευσης ή έργα της ΔΕΗ. */
		WATER_ELECTRICITY,
		/** Εκπόνηση μελέτης μηχανικού. */
		ENGINEERING_STUDY,
		/** Επίβλεψη εφαρμογής μελέτης ή εκπόνηση μελέτης εκτός μηχανικού. */
		STUDY_SUPERVISION;
		/** Ο τύπος του δικαιούχου με κείμενο. */
		static final private String[] TYPE = {
			"Προμήθεια υλικών", "Παροχή υπηρεσιών", "Προμήθεια υγρών καυσίμων",
			"Μίσθωση ακινήτων", "Λογαριασμοί ύδρευσης - αποχέτευσης ή έργα της ΔΕΗ",
			"Εκπόνηση μελέτης μηχανικού", "Επίβλεψη εφαρμογής μελέτης ή εκπόνηση μελέτης εκτός μηχανικού"
		};
		@Override public String toString() { return TYPE[ordinal()]; }
	}

	/** Επιστρέφει τον τύπο του τιμολογίου. */
	Type getType() { return type; }
	/** Επιστρέφει τον δικαιούχο του τιμολογίου. */
	Contractor getContractor() { return contractor; }
	/** Επιστρέφει τις κρατήσεις του τιμολογίου. */
	Deduction getDeduction() { return deduction; }
	/** Η σύμβαση στο πλαίσιο της οποίας εκδόθηκε το τιμολόγιο. */
	Contract getContract() { return contract; }

	@Override public String toString() { return id; }

	@Override public void serialize(VariableFields fields) {
		if (id != null)         fields.add (H[0], id);
								fields.add (H[1], type.name());	// Ο τύπος του τιμολογίου δεν είναι ποτέ null
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
			case 2: return contractor;
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
						recalcVATDeductionIncomeTax(hasVat1, hasVat2);
					} else type = (Type) value;
				break;
			case 2:		// Θέτει τον δικαιούχο του τιμολογίου
				if (value != contractor) {
					Contractor c = (Contractor) value;
					if (parent.isSmart()) {
						Contractor.Type ct1 = contractor != null ? contractor.getType() : null;
						Contractor.Type ct2 =          c != null ?          c.getType() : null;
						if (ct1 != ct2) {
							boolean hasVat1 = hasVAT(type, contractor);
							boolean hasVat2 = hasVAT(type, c);
							Contractor cc = contractor;	// κρατάει τον παλιό δικαιούχο
							contractor = c;				// αλλάζει το δικαιούχο
							recalcContractorInvoicesFromNet(parent, cc);	// ενημερώνει τιμολόγια παλιου δικαιούχου
							recalcVATDeductionIncomeTax(hasVat1, hasVat2);	// ενημερώνει τιμολόγια νέου δικαιούχου
							break;
						}
					}
					contractor = c;
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
			case 5:		// Θέτει τη σύμβαση
				if (value != contract) {
					if (contract != null) sub(contract.prices, prices);
					Contract c = (Contract) value;
					if (c != null) add(c.prices, prices);
					contract = c;
				}
				break;
		}
	}

	@Override public boolean isEmpty() { return false; }	// Μόνο με DELETE διαγράφεται τιμολόγιο

	/** Επανυπολογίζει ΦΠΑ ειδών, κρατήσεις και ΦΕ τιμολογίου.
	 * Αποτελεί κοινό τμήμα των κλήσεων setType() και setContractor().
	 * @param hasVat1 Πριν αλλάξει η τιμή (τύπος τιμολογίου ή δικαιούχου) τα είδη του τιμολογίου
	 * είχαν ΦΠΑ
	 * @param hasVat1 Αφού άλλαγε η τιμή τα είδη του τιμολογίου έχουν ΦΠΑ */
	private void recalcVATDeductionIncomeTax(boolean hasVat1, boolean hasVat2) {
		if (hasVat1 != hasVat2) {
			double vatOld = prices[1];
			setVAT(hasVat2);
			recalcReportDiff(vatOld, 1);	// Ενημέρωση σύμβασης και δαπάνης για αλλαγή ΦΠΑ
		}
		recalcContractorInvoicesFromNet();
	}

	/** Τροποποιεί το ΦΠΑ των ειδών του τιμολογίου με βάση τα δεδομένα της δαπάνης.
	 * @param nonZero Το ΦΠΑ των ειδών του τιμολογίου δεν είναι μηδέν */
	private void setVAT(boolean nonZero) {
		if (nonZero) items.forEach(i -> i.setVATnot0());
		else         items.forEach(i -> i.setVAT0());
		calcVAT();
	}

	/** Τροποποιεί τις ποσοστιαίες κρατήσεις του τιμολογίου με βάση τα δεδομένα της δαπάνης.
	 * @param net Η καθαρή αξία όλων των τιμολογίων του ίδιου δικαιούχου
	 * @return Οι κρατήσεις άλλαξαν */
	private boolean setDeductionPercent(double net) {
		Deduction d = calcDeduction(type, contractor, net, parent.getFinancing());
		if (deduction != null && !deduction.equals(d) || deduction == null && d != null) {
			deduction = d;
			return true;
		} else return false;
	}

	/** Τροποποιεί το ποσοστό ΦΕ του τιμολογίου με βάση τα δεδομένα της δαπάνης.
	 * @param net Η καθαρή αξία όλων των τιμολογίων του ίδιου δικαιούχου
	 * @return Το ΦΕ άλλαξε */
	private boolean setIncomeTaxPercent(double net) {
		byte b = calcIncomeTax(type, contractor, net, parent.isConstruction());
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
		boolean wePayDeductions = contractor != null && (contractor.getType() == PUBLIC_SERVICES
				|| contractor.getType() == ARMY);
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
			// Ο μηδενισμός έχει σκοπό να μην ξαναυπολογιστεί η καθαρή αξία στην καθαρή αξία όλων
			// των τιμολογίων του ίδιου δικαιούχου παρακάτω
			prices[0] = 0; prices[1] = 0;
			recalcContractorInvoicesFromNet();
		}
	}

	/** Ενημερώνει το τιμολόγιο ότι κάποιο από τα είδη του τροποποιήθηκε.
	 * Δύναται να επηρεάζει κρατήσεις και ΦΕ. Επανυπολογίζει τις τιμές του τιμολογίου (Καθαρή αξία
	 * κτλ). */
	void recalcFromItems() {
		double[] d = Arrays.copyOf(prices, prices.length);
		calcNet(); calcVAT();
		if (prices[0] != d[0] || prices[1] != d[1]) {	// Υπάρχουν αλλαγές
			if (parent.isSmart()) recalcContractorInvoicesFromNet();
			else {
				calcDeduction(); calcMixedPayable(); calcIncomeTax(); calcPayableMinusIncomeTax();
				recalcReportDiff(d);
			}
		}
	}

	/** Ενημερώνει το τιμολόγιο ότι ενεργοποιήθηκε ο αυτοματισμός.
	 * Δύναται να επηρεάζει κρατήσεις και ΦΕ. Επανυπολογίζει τις τιμές του τιμολογίου. Ο
	 * αυτοματισμός είναι ενεργός. */
	void recalcFromSmart() {
		double vatOld = prices[1];
		setVAT(hasVAT(type, contractor));
		recalcReportDiff(vatOld, 1);	// Ενημέρωση σύμβασης και δαπάνης για αλλαγή ΦΠΑ
		recalcContractorInvoicesFromNet();
	}

	/** Ενημερώνει το τιμολόγιο ότι τροποποιήθηκε η χρηματοδότηση δαπάνης.
	 * Δύναται να επηρεάζει τις κρατήσεις. Επανυπολογίζει τις τιμές του τιμολογίου. Ο αυτοματισμός
	 * είναι ενεργός. */
	void recalcFromFinancing() {
		double[] d = Arrays.copyOf(prices, prices.length);
		if (setDeductionPercent(calcContractorNet())) {
			calcDeduction(); calcMixedPayable(); calcIncomeTax(); calcPayableMinusIncomeTax();
			recalcReportDiff(d);
		}
	}

	/** Ενημερώνει το τιμολόγιο ότι τροποποιήθηκε ο τύπος δαπάνης.
	 * Δύναται να επηρεάζει το ΦΕ. Επανυπολογίζει τις τιμές του τιμολογίου. Ο αυτοματισμός είναι
	 * ενεργός. */
	void recalcFromConstruction() {
		double[] d = Arrays.copyOf(prices, prices.length);
		if (setIncomeTaxPercent(calcContractorNet())) {
			calcIncomeTax(); calcPayableMinusIncomeTax();
			recalcReportDiff(d);
		}
	}

	/** Ενημερώνει όλα τα τιμολόγια του ίδιου δικαιούχου ότι η συνολική καθαρή αξία τους άλλαξε.
	 * Δύναται να επηρεάζει κρατήσεις και ΦΕ αυτών των τιμολογίων. Επανυπολογίζει τις τιμές αυτών
	 * των τιμολογίων. Ο αυτοματισμός είναι ενεργός. */
	private void recalcContractorInvoicesFromNet() {
		if (contractor != null) recalcContractorInvoicesFromNet(parent, contractor);
		else recalcFromContractorNet(prices[0]);
	}

	/** Ενημερώνει όλα τα τιμολόγια του ίδιου δικαιούχου ότι η συνολική καθαρή αξία τους άλλαξε.
	 * Δύναται να επηρεάζει κρατήσεις και ΦΕ αυτών των τιμολογίων. Επανυπολογίζει τις τιμές αυτών
	 * των τιμολογίων. Ο αυτοματισμός είναι ενεργός.
	 * @param expenditure Η δαπάνη στην οποία αναφερόμαστε
	 * @param contractor Ο δικαιούχος του οποίου τα τιμολόγια θα επιστρέψουμε την καθαρή αξία. Δεν
	 * μπορεί να είναι null. */
	static private void recalcContractorInvoicesFromNet(Expenditure expenditure, Contractor contractor) {
		double net = calcContractorNet(expenditure, contractor);
		expenditure.invoices.stream()
				.filter(i -> contractor.equals(i.contractor))
				.forEach(i -> i.recalcFromContractorNet(net));
	}

	/** Ενημερώνει το τιμολόγιο ότι η καθαρή αξία όλων των τιμολογίων του ίδιου δικαιούχου άλλαξε.
	 * Δύναται να επηρεάζει κρατήσεις και ΦΕ. Επανυπολογίζει τις τιμές του τιμολογίου. Ο
	 * αυτοματισμός είναι ενεργός.
	 * @param net Η καθαρή αξία όλων των τιμολογίων του ίδιου δικαιούχου */
	private void recalcFromContractorNet(double net) {
		double[] d = Arrays.copyOf(prices, prices.length);
		boolean deduct = setDeductionPercent(net);
		if (deduct) { calcDeduction(); calcMixedPayable(); }
		if (setIncomeTaxPercent(net) || deduct) {	// <-- αλλαγή διάταξης απαγορεύται!
			calcIncomeTax(); calcPayableMinusIncomeTax();
			recalcReportDiff(d);
		}
	}

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

	/** Επιστρέφει το άθροισμα των καθαρών αξιών όλων των τιμολογίων του ίδιου δικαιούχου.
	 * @return Το άθροισμα των καθαρών αξιών όλων των τιμολογίων του ίδιου δικαιούχου */
	private double calcContractorNet() {
		return contractor == null ? prices[0] : calcContractorNet(parent, contractor);
	}

	/** Επιστρέφει το άθροισμα των καθαρών αξιών όλων των τιμολογίων του ίδιου δικαιούχου.
	 * @param expenditure Η δαπάνη στην οποία αναφερόμαστε
	 * @param contractor Ο δικαιούχος του οποίου τα τιμολόγια θα επιστρέψουμε την καθαρή αξία. Δεν
	 * μπορεί να είναι null.
	 * @return Το άθροισμα των καθαρών αξιών όλων των τιμολογίων του ίδιου δικαιούχου */
	static private double calcContractorNet(Expenditure expenditure, Contractor contractor) {
		return expenditure.invoices.stream()
				.filter(i -> contractor.equals(i.contractor))
				.mapToDouble(i -> i.prices[0]).sum();
	}

	/** Υπολογίζει ποιο πρέπει να είναι το ποσοστό ΦΕ με βάση τα υπόλοιπα στοιχεία της δαπάνης.
	 * @param type Ο τύπος του τιμολογίου
	 * @param contractor Ο δικαιούχος
	 * @param net Το άθροισμα καθαρών αξιών όλων των τιμολογίων του ίδιου δικαιούχου
	 * @param construction Η δαπάνη είναι δαπάνη έργου
	 * @return Το ποσοστό ΦΕ ή 0 αν κάποια παράμετρος είναι null */
	static private byte calcIncomeTax(Type type, Contractor contractor, double net, boolean construction) {
		if (type != null && contractor != null && contractor.getType() == PRIVATE_SECTOR &&
				(construction || net > 150))
			switch(type) {
				case SUPPLIES: return 4;
				case SERVICES:
					if (!construction) return 8;
					else if (net > 300) return 3;
					else return 0;
				case LIQUID_FUEL: return 1;
			}
		return 0;
	}

	/** Υπολογίζει ποιο πρέπει να είναι το ποσοστό κρατήσεων με βάση τα υπόλοιπα στοιχεία της δαπάνης.
	 * @param type Ο τύπος του τιμολογίου
	 * @param contractor Ο δικαιούχος
	 * @param net Το άθροισμα καθαρών αξιών όλων των τιμολογίων του ίδιου δικαιούχου
	 * @param financing Ο τύπος χρηματοδότησης της δαπάνης
	 * @return Οι κρατήσεις του τιμολογίου ή null αν κάποια παράμετρος είναι null */
	static private Deduction calcDeduction(Type type, Contractor contractor, double net,
			String financing) {
		Deduction deduction = null;
		if (type == null || contractor == null || contractor.getType() == null || financing == null);
		else if (type == WATER_ELECTRICITY) deduction = D0;
		else if (contractor.getType() == PRIVATE_SECTOR) {
			if (type == PROPERTY_RENTAL) {
					 if (FINANCING[0].equals(financing)) deduction = D4_096;
				else if (FINANCING[1].equals(financing)) deduction = D14_096;
				else if (FINANCING[2].equals(financing)) deduction = D0;
			} else if (net > 2500) {
				if (type == ENGINEERING_STUDY || type == STUDY_SUPERVISION) {
						 if (FINANCING[0].equals(financing)) deduction = D4_43068;
					else if (FINANCING[1].equals(financing)) deduction = D14_43068;
					else if (FINANCING[2].equals(financing)) deduction = D0_33468;
				} else {// if (type != ENGINEERING_STUDY && type != STUDY_SUPERVISION)
						 if (FINANCING[0].equals(financing)) deduction = D4_23068;
					else if (FINANCING[1].equals(financing)) deduction = D14_23068;
					else if (FINANCING[2].equals(financing)) deduction = D0_13468;
				}
			} else // if (net <= 2500)
				if (type == ENGINEERING_STUDY || type == STUDY_SUPERVISION) {
						 if (FINANCING[0].equals(financing)) deduction = D4_35816;
					else if (FINANCING[1].equals(financing)) deduction = D14_35816;
					else if (FINANCING[2].equals(financing)) deduction = D0_26216;
				} else {
						 if (FINANCING[0].equals(financing)) deduction = D4_15816;
					else if (FINANCING[1].equals(financing)) deduction = D14_15816;
					else if (FINANCING[2].equals(financing)) deduction = D0_06216;
				}
		} else // if (contractor.getType() == ARMY || contractor.getType() == PUBLIC_SERVICES)
			deduction = FINANCING[1].equals(financing) ? D14: D4;
		return deduction;
	}

	/** Ελέγχει πρέπει να υπάρχει ΦΠΑ με βάση τα υπόλοιπα στοιχεία της δαπάνης.
	 * @param type Ο τύπος του τιμολογίου
	 * @param contractor Ο δικαιούχος
	 * @return Πρέπει να υπάρχει ΦΠΑ (ή κάποια παράμετρος είναι null) */
	static boolean hasVAT(Invoice.Type type, Contractor contractor) {
		return type == null || contractor == null || contractor.getType() == null ||
				contractor.getType() != ARMY && type != PROPERTY_RENTAL;
	}
}