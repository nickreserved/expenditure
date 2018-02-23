package cost;

import static cost.Bill.round;
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

final public class CostWizardDialog extends JDialog implements ActionListener, DocumentListener {

	@SuppressWarnings("LeakingThisInConstructor")
	public CostWizardDialog(Window w) {
		super(w, "Οδηγός Τιμολογίου");
		
		setLayout(new BorderLayout());
		((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		Box bv = Box.createVerticalBox();
		
		Box bh = Box.createHorizontalBox();
		bh.add(new JLabel("Πηγή χρηματοδότησης της δαπάνης:"));
		bh.add(Box.createHorizontalStrut(5));
		bh.add(cbFinancing = new JComboBox(new String[] {
			"Τακτικός Προϋπολογισμός",
			"Ίδιοι πόροι",
			"Προϋπολογισμός Προγράμματος Δημοσίων Επενδύσεων"
		}));
		bv.add(bh); bv.add(Box.createVerticalStrut(5));

		bh = Box.createHorizontalBox();
		bh.add(new JLabel("Τύπος δαπάνης:"));
		bh.add(Box.createHorizontalStrut(5));
		bh.add(cbCost = new JComboBox(new String[] { "Λοιπές (ΓΕΣ/ΔΥΠΟΣΤΗ)", "Έργο ΜΧ (ΓΕΣ/ΔΥΠΠΕ)" }));
		bv.add(bh); bv.add(Box.createVerticalStrut(5));

		bh = Box.createHorizontalBox();
		bh.add(new JLabel("Τύπος προμηθευτή:"));
		bh.add(Box.createHorizontalStrut(5));
		bh.add(cbContractor = new JComboBox(new String[] {
			"Ιδιώτης (προμηθευτές, εργολάβοι, μάστορες κ.τ.λ.)",
			"Νομικά Πρόσωπα Δημοσίου Δικαίου (π.χ. ΟΣΕ)",
			"Στρατιωτικά Πρατήρια",
			"Λογαριασμοί ύδρευσης-αποχέτευσης ή έργα της ΔΕΗ"
		}));
		bv.add(bh); bv.add(Box.createVerticalStrut(5));

		bh = Box.createHorizontalBox();
		bh.add(new JLabel("Τύπος τιμολογίου:"));
		bh.add(Box.createHorizontalStrut(5));
		bh.add(cbInvoiceType = new JComboBox(new String[] {
			"Προμήθεια υλικών", "Παροχή υπηρεσιών", "Αγορά υγρών καυσίμων"
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
		tpInfo.setContentType("text/html");
		getContentPane().add(new JScrollPane(tpInfo), BorderLayout.CENTER);

		setSize(650, 450);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		
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
			double net = round(Double.parseDouble(tfValue.getText()), 2);
			
			// Σε κατασκευαστικές δαπάνες, προμηθευτής είναι πάντα ιδιώτης
			if (construction && contractor != 0 /*Όχι ιδιώτης*/) {
				construction = false;
				cbCost.setSelectedIndex(0 /*Όχι έργο ΜΧ*/);
			}
			// Σε δαπάνες που προμηθευτής είναι ο Στρατός, τα τιμολόγια είναι πάντα προμήθειας υλικών
			if (contractor == 2 /*Στρατός*/ && invoiceType != 0 /*Προμήθεια υλικών*/)
				cbInvoiceType.setSelectedIndex(invoiceType = 0); //Προμήθεια υλικών
			// Σε δαπάνες ρεύματος-νερού, τα τιμολόγια είναι πάντα παροχής υπηρεσιών
			if (contractor == 3 /*Ύδρευση-Ρεύμα*/ && invoiceType != 1 /*Παροχή υπηρεσιών*/)
				cbInvoiceType.setSelectedIndex(invoiceType = 1); // Παροχή υπηρεσιών
			// Σε δαπάνες προϋπολογισμού ΠΔΕ, ο εργολάβος είναι πάντα ιδιώτης
			if (financing == 2 /*Προϋπολογισμός ΠΔΕ*/ && contractor != 0 /*Ιδιώτης*/)
					cbContractor.setSelectedIndex(contractor = 0); // Ιδιώτης

			boolean fpa = contractor != 2 /*Όχι Στρατός*/;
			double hold = 0, valueforfe = net;
			int fe = 0;
			
			// Υπολογισμός κρατήσεων
			if (contractor == 0 /*Ιδιώτης*/) {
				if (net > 2500)
					switch(financing) {
						case 0: /*Τακτικός Π/Υ*/ hold = 4.22032; break;
						case 1: /*Ιδιοι πόροι*/ hold = 14.22032; break;
						case 2: /*Π/Υ ΠΔΕ*/ hold = 0.12432; break;
					}
				else
					switch(financing) {
						case 0: /*Τακτικός Π/Υ*/ hold = 4.096; break;
						case 1: /*Ιδιοι πόροι*/ hold = 14.096; break;
						//case 2: /*Π/Υ ΠΔΕ*/ hold = 0; break;
					}
			} else if (contractor == 1 /*ΝΠΔΔ*/ || contractor == 2 /*Στρατος*/)
				switch (financing) {
					case 0: /*Τακτικός Π/Υ */ hold = 4; break;
					case 1: /*Ιδιοι πόροι */ hold = 14; break;
					//case 2: /* Π/Υ ΠΔΕ */ hold = 0; break;
				}
			// else if (contractor == 3 /*Ύδρευση-Ρεύμα*/) hold = 0;
			
			StringBuilder sb = new StringBuilder(4096);
			sb.append("<html><style>ul {margin-top: -15px; margin-bottom: 0}</style><b>Καθαρή Αξία:</b> ");
			sb.append(net).append("€");

			sb.append("<br><b>ΦΠΑ:</b> ");
			if (fpa) sb.append("Προβλέπεται και το γνωρίζει ο προμηθευτής");
			else sb.append("0%");

			sb.append("<br><b>Κρατήσεις:</b> ").append(hold).append("% της καθαρής αξίας");
			if (hold == 0) sb.append(".");
			else {
				sb.append(" (").append(Math.round(net * hold) / 100.0).append("€), που βαρύνουν ");
				if (contractor != 0 /*Ιδιώτης*/) sb.append("εμάς"); else sb.append("τον προμηθευτή");
				sb.append("<br><b>Καταλογιστέο:</b> Καθαρή Αξία");
				if (fpa) sb.append(" + ΦΠΑ");
				if (contractor != 0 /*Ιδιώτης*/) sb.append(" + Κρατήσεις");
			}

			// Υπολογισμός του ΦΕ
			if (net > 150 && contractor == 0 /*Ίδιώτης*/)
				switch (invoiceType) {
					case 0: /*Προμήθεια υλικών*/ fe = 4; break;
					case 1: /*Παροχή υπηρεσιών*/
						if (construction /*Κατασκευή έργου*/) {
							fe = 3;
							valueforfe -= Math.round(hold * net) / 100.0;
						} else fe = 8;
						break;
					case 2: /*Προμήθεια υγρών καυσίμων*/ fe = 1; break;
				}

			sb.append("<br><b>ΦΕ:</b> ").append(fe).append("% της καθαρής αξίας");
			if (fe != 3) sb.append(" μειον κρατήσεις");
			sb.append(" (").append(Math.round(valueforfe * fe) / 100.0).append("€)<br>");
	
			if (construction /*Κατασκευή Έργων*/ && invoiceType == 1 /*Παροχή Υπηρεσιών*/)
				sb.append("<br>Ο εργολάβος πρέπει να μας υποβάλει τα πρωτότυπα αποδεικτικά κατάθεσης"
						+ "για 1% <b>ΤΠΕΔΕ</b> (")
						.append(round(0.01 * net, 2))
						.append("€) στο λογαριασμό ______, 0.5% <b>ΕΜΠ</b> (")
						.append(round(0.005 * net, 2))
						.append("€) στο λογαριασμό ______, 0.6% <b>Π.Ο. ΕΜΔΥΔΑΣ</b> (")
						.append(round(0.006 * net, 2))
						.append("€) στο λογαριασμό ΙΒΑΝ GR5701100800000008000587009,  επί της καθαρής"
								+ " αξίας, καθώς και το χαρτόσημο και χαρτόσημο ΟΓΑ που αναλογεί σε αυτά.");
			
			if (contractor == 0 /*Ιδιώτης*/) {
				if (net > 1500)
					sb.append("<br>Απαιτείται Φορολογική Ενημερότητα του προμηθευτή"
							+ " για «Πληρωμή από το Δημόσιο».");
				else if (net > 1220)
					sb.append("<br>Αν το καταλογιστέο είναι πάνω από 1500€, απαιτείται Φορολογική "
							+ "Ενημερότητα του προμηθευτή για «Πληρωμή από το Δημόσιο».");
				if (net > 2500)
					sb.append("<br>Απαιτείται Ασφαλιστική Ενημερότητα του προμηθευτή."
							+ "<br>Απαιτείται απόσπασμα ποινικού μητρώου."
							+ "<br>Απαιτείται σύναψη σύμβασης με τον προμηθευτή.");
			}
			
			if (net > 60000) sb.append("<br>Απαιτείται Δημόσιος Διαγωνισμός.");
			else if (net > 20000) sb.append("<br>Απαιτείται Πρόχειρος Διαγωνισμός.");

			if (net > 20000)
				sb.append("<br>Ο εργολάβος δεν πρέπει να έχει αριθμό καταδικαστικών αποφάσεων"
						+ " εργατικής φύσεως την τελευταία διετία");
			
			tpInfo.setText(sb.toString());
		} catch(NumberFormatException e) {
			tpInfo.setText("Συμπληρώστε σωστά τα παραπάνω πεδία για να λάβετε πληροφορίες για το τιμολόγιο αλλά και τη δαπάνη.<br>Οι κρατήσεις και το ΦΕ υπολογίζονται βάση της Φ.830/60/918814/Σ.5965/2 Σεπ 16/ΓΕΣ/ΔΟΙ/3α και ΚΥΑ 1191/14 Μαι 17 (ΦΕΚ Β' 969).");
		}
	}
}
