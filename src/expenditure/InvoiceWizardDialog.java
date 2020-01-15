package expenditure;

import static expenditure.MainFrame.getLocationScreenCentered;
import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

final class InvoiceWizardDialog extends JDialog implements ActionListener, DocumentListener {

	@SuppressWarnings("LeakingThisInConstructor")
	public InvoiceWizardDialog(Window w) {
		super(w, "Οδηγός Τιμολογίου");

		setLayout(new BorderLayout());
		((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		Box bv = Box.createVerticalBox();

		Box bh = Box.createHorizontalBox();
		bh.add(new JLabel("Πηγή χρηματοδότησης της δαπάνης:"));
		bh.add(Box.createHorizontalStrut(5));
		bh.add(cbFinancing = new JComboBox<>(new String[] {
			"Τακτικός Προϋπολογισμός",
			"Ίδιοι πόροι",
			"Προϋπολογισμός Προγράμματος Δημοσίων Επενδύσεων"
		}));
		bv.add(bh); bv.add(Box.createVerticalStrut(5));

		bh = Box.createHorizontalBox();
		bh.add(new JLabel("Τύπος δαπάνης:"));
		bh.add(Box.createHorizontalStrut(5));
		bh.add(cbCost = new JComboBox<>(new String[] { "Λοιπές (ΓΕΣ/ΔΥΠΟΣΤΗ)", "Έργο ΜΧ (ΓΕΣ/ΔΥΠΠΕ)" }));
		bv.add(bh); bv.add(Box.createVerticalStrut(5));

		bh = Box.createHorizontalBox();
		bh.add(new JLabel("Τύπος προμηθευτή:"));
		bh.add(Box.createHorizontalStrut(5));
		bh.add(cbContractor = new JComboBox<>(new String[] {
			"Ιδιώτης (προμηθευτές, εργολάβοι, μάστορες κ.τ.λ.)",
			"Νομικά Πρόσωπα Δημοσίου Δικαίου (π.χ. ΟΣΕ)",
			"Στρατιωτικά Πρατήρια"
		}));
		bv.add(bh); bv.add(Box.createVerticalStrut(5));

		bh = Box.createHorizontalBox();
		bh.add(new JLabel("Τύπος τιμολογίου:"));
		bh.add(Box.createHorizontalStrut(5));
		bh.add(cbInvoiceType = new JComboBox<>(new String[] {
			"Προμήθεια υλικών", "Παροχή υπηρεσιών", "Προμήθεια υγρών καυσίμων", "Μισθώματα ακινήτων",
			"Λογαριασμοί ύδρευσης-αποχέτευσης ή έργα της ΔΕΗ", "Εκπόνηση μελετών"
		}));
		bv.add(bh); bv.add(Box.createVerticalStrut(5));

		bh = Box.createHorizontalBox();
		bh.add(new JLabel("Καθαρή αξία τιμολογίου:"));
		bh.add(Box.createHorizontalStrut(5));
		bh.add(tfValue = new JTextField());
		bv.add(bh); bv.add(Box.createVerticalStrut(5));

		getContentPane().add(bv, BorderLayout.PAGE_START);

		tpInfo = new JTextPane();
		tpInfo.setEditable(false);
		getContentPane().add(new JScrollPane(tpInfo), BorderLayout.CENTER);

		setSize(650, 450);
		setLocation(getLocationScreenCentered(getWidth(), getHeight()));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		cbFinancing.addActionListener(this);
		cbContractor.addActionListener(this);
		cbCost.addActionListener(this);
		cbInvoiceType.addActionListener(this);
		tfValue.getDocument().addDocumentListener(this);

		calc();
	}

	private final JComboBox cbFinancing;
	private final JComboBox cbContractor;
	private final JComboBox cbCost;
	private final JComboBox cbInvoiceType;
	private final JTextField tfValue;
	private final JTextPane tpInfo;

	@Override public void actionPerformed(ActionEvent e) { calc(); }
	@Override public void insertUpdate(DocumentEvent e) { calc(); }
	@Override public void removeUpdate(DocumentEvent e) { calc(); }
	@Override public void changedUpdate(DocumentEvent e) { calc(); }

	private void calc() {
		try {
			int financing = cbFinancing.getSelectedIndex();
			boolean construction = cbCost.getSelectedIndex() == 1;
			int contractor = cbContractor.getSelectedIndex();
			int invoiceType = cbInvoiceType.getSelectedIndex();
			double net = Math.round(Double.parseDouble(tfValue.getText()) * 100) / 100.0;

			// Σε δαπάνες προϋπολογισμού ΠΔΕ, ο εργολάβος είναι πάντα ιδιώτης
			if (financing == 2 /*Προϋπολογισμός ΠΔΕ*/ && contractor != 0 /*Ιδιώτης*/)
					cbContractor.setSelectedIndex(contractor = 0); // Ιδιώτης

			// Σε κατασκευαστικές δαπάνες, προμηθευτής είναι πάντα ιδιώτης
			if (construction && contractor != 0 /*Όχι ιδιώτης*/)
				cbContractor.setSelectedIndex(contractor = 0 /*Ιδιώτης*/);
			// Σε κατασκευαστικές δαπάνες, τιμολόγια προμήθειας υλικών, παροχής υπηρεσιών ή εκπόνησης
			// μελετών μόνο
			if (construction && invoiceType != 0 /*Προμήθεια υλικών*/ &&
					invoiceType != 1 /*Παροχή υπηρεσιών*/ && invoiceType != 5 /*Εκπόνηση μελετών*/)
				cbInvoiceType.setSelectedIndex(invoiceType = 0 /*Προμήθεια υλικών*/);

			// Σε δαπάνες που προμηθευτής είναι ο Στρατός, τα τιμολόγια είναι πάντα προμήθειας υλικών
			if (contractor == 2 /*Στρατός*/ && invoiceType != 0 /*Προμήθεια υλικών*/)
				cbInvoiceType.setSelectedIndex(invoiceType = 0); //Προμήθεια υλικών

			boolean fpa = contractor != 2 /*Όχι Στρατός*/;
			double hold = 0;
			int fe = 0;

			// Υπολογισμός κρατήσεων
			if (invoiceType == 4 /*Λογαριασμοί νερού/ΔΕΗ*/) /*hold = 0*/;
			else if (contractor == 0 /*Ιδιώτης*/) {
				if (invoiceType == 3 /*Μισθώματα ακινήτων*/)
					switch(financing) {
						case 0: /*Τακτικός Π/Υ*/ hold = 4.096; break;
						case 1: /*Ιδιοι πόροι*/ hold = 14.096; break;
						//case 2: /*Π/Υ ΠΔΕ*/ hold = 0;
					}
				else if (net >= 1000)
					if (financing == 2 /*Π/Υ ΠΔΕ*/)
						hold = invoiceType == 5 /*Εκπόνηση μελετών*/ ? 0.33468 : 0.13468;
					else { //Τακτικός Π/Υ ή Ιδιοι πόροι
						hold = invoiceType == 5 /*Εκπόνηση μελετών*/ ? 4.43068 : 4.23068;
						if (financing == 1 /*Ιδιοι πόροι*/) hold += 10;
					}
				else
					if (financing == 2 /*Π/Υ ΠΔΕ*/)
						hold = invoiceType == 5 /*Εκπόνηση μελετών*/ ? 0.26216 : 0.06216;
					else { //Τακτικός Π/Υ ή Ιδιοι πόροι
						hold = invoiceType == 5 /*Εκπόνηση μελετών*/ ? 4.35816 : 4.15816;
						if (financing == 1 /*Ιδιοι πόροι*/) hold += 10;
					}
			} else if (contractor == 1 /*ΝΠΔΔ*/ || contractor == 2 /*Στρατος*/)
				hold = financing == 1 /*Ιδιοι πόροι*/ ? 14 : 4;

			StringBuilder sb = new StringBuilder(4096);
			sb.append("Καθαρή Αξία: ").append(net).append("€");
			sb.append("\nΦΠΑ: ").append(fpa ? "Προβλέπεται και το γνωρίζει ο δικαιούχος" : "0%");
			sb.append("\nΚρατήσεις: ").append(hold).append("% της καθαρής αξίας");
			if (hold == 0) sb.append(".");
			else {
				sb.append(" (").append(Math.round(net * hold) / 100.0).append("€), που βαρύνουν ");
				if (contractor != 0 /*Ιδιώτης*/) sb.append("εμάς"); else sb.append("τον δικαιούχο");
				sb.append("\nΚαταλογιστέο: Καθαρή Αξία");
				if (fpa) sb.append(" + ΦΠΑ");
				if (contractor != 0 /*Ιδιώτης*/) sb.append(" + Κρατήσεις");
			}

			// Υπολογισμός του ΦΕ
			double valueforfe = net * (1 - hold / 100);	// Όλες οι περιπτώσεις εκτός από παροχή υπηρεσιών σε κατασκευές
			if (construction || net > 150 && contractor == 0 /*Ίδιώτης*/)
				switch (invoiceType) {
					case 0: /*Προμήθεια υλικών*/ fe = 4; break;
					case 1: /*Παροχή υπηρεσιών*/
						if (construction /*Κατασκευή έργου*/) {
							fe = 3;
							valueforfe = net;
						} else fe = 8;
						break;
					case 2: /*Προμήθεια υγρών καυσίμων*/ fe = 1; break;
					//case 3: /*Μισθώματα ακινήτων*/ fe = 0; break;
					//case 4: /*Λογαριασμοί νερού/ΔΕΗ*/ fe = 0; break;
					case 5: /*Εκπόνηση μελετών*/ fe = 4; break;
				}

			if (invoiceType == 5 /*Εκπόνηση μελετών*/)
				sb.append("\nΑν η δαπάνη αφορά εκπόνηση σχεδίων ή μελέτης έργου πολιτικού ή"
						+ " τοπογράφου μηχανικού, τότε:");

			sb.append("\nΦΕ: ").append(fe).append("% της καθαρής αξίας");
			if (fe != 3) sb.append(" μειον κρατήσεις");
			sb.append(" (").append(Math.round(valueforfe * fe) / 100.0).append("€)\n");

			if (invoiceType == 5 /*Εκπόνηση μελετών*/)
				sb.append("Αν η δαπάνη αφορά εκπόνηση σχεδίων ή μελέτης άλλου επιστημονικού τομέα ή"
						+ " αφορά επίβλεψη εφαρμογής μελέτης, τότε:\nΦΕ: 10% της καθαρής αξίας (")
						.append(Math.round(valueforfe * 10) / 100.0).append("€)\n");

			if (construction /*Κατασκευή Έργων*/ && invoiceType == 1 /*Παροχή Υπηρεσιών*/)
				sb.append("\nΟ εργολάβος πρέπει να μας υποβάλει τα παρακάτω πρωτότυπα αποδεικτικά κατάθεσης, επί της καθαρής αξίας, για:\n"
						+ "• 1% ΤΠΕΔΕ (").append(Math.round(net) / 100.0).append("€) στο λογαριασμό ______\n"
						+ "• 0.6% Π.Ο. ΕΜΔΥΔΑΣ (").append(Math.round(0.6 * net) / 100.0).append("€) στο λογαριασμό ΙΒΑΝ GR5701100800000008000587009\n"
						+ "• 0.25% ΤΜΕΔΕ (").append(Math.round(0.25 * net) / 100.0).append("€) στο λογαριασμό ______, υπέρ των Μηχανικών ΤΕ της Π.Ο.ΜΗ.ΤΕ.ΔΥ. τακτικών υπαλλήλων (µονίµων ή αορίστου χρόνου) που απασχολούνται στο Δηµόσιο, Ν.Π.Δ.Δ. και στους ΟΤΑ Α' και Β' βαθµού\n"
						+ "• Το χαρτόσημο και χαρτόσημο ΟΓΑ που αναλογεί στα παραπάνω.");

			if (contractor == 0 /*Ιδιώτης*/) {
				if (net > 1500)
					sb.append("\nΑπαιτείται Φορολογική Ενημερότητα του δικαιούχου"
							+ " για «Πληρωμή από το Δημόσιο».");
				else if (net > 1220)
					sb.append("Αν το καταλογιστέο είναι πάνω από 1500€, απαιτείται Φορολογική "
							+ "Ενημερότητα του δικαιούχου για «Πληρωμή από το Δημόσιο».");
				if (net > 2500)
					sb.append("\nΑπαιτείται Ασφαλιστική Ενημερότητα του δικαιούχου."
							+ "\nΑπαιτείται απόσπασμα ποινικού μητρώου του δικαιούχου.");
				else if (net > 2419)
					sb.append("\nΑν το καταλογιστέο είναι πάνω από 3000€, απαιτείται "
							+ "Ασφαλιστική Ενημερότητα του δικαιούχου.");
				if (net > 2500 || construction /*Κατασκευή Έργων*/)
					sb.append("\nΑπαιτείται σύναψη σύμβασης με τον δικαιούχο.");
			}

			if (net > 60000) sb.append("\nΑπαιτείται Δημόσιος Διαγωνισμός.");
			else if (net > 20000 || construction /*Κατασκευή Έργων*/)
				sb.append("\nΑπαιτείται Συνοπτικός Διαγωνισμός.");

			if (net > 20000)
				sb.append("\nΟ δικαιούχος δεν πρέπει να έχει αριθμό καταδικαστικών αποφάσεων"
						+ " εργατικής φύσεως την τελευταία διετία");

			tpInfo.setText(sb.toString());
		} catch(NumberFormatException e) {
			tpInfo.setText("Συμπληρώστε σωστά τα παραπάνω πεδία για να λάβετε πληροφορίες για το "
					+ "τιμολόγιο αλλά και τη δαπάνη.\nΟι κρατήσεις και το ΦΕ υπολογίζονται βάση της "
					+ "Φ.830/40/1173153/Σ.2320/23 Απρ 19/ΓΕΣ/ΔΟΙ.");
		}
	}
}
