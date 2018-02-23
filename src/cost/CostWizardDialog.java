package cost;

import common.Functions;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class CostWizardDialog extends JDialog implements ActionListener, DocumentListener {

	public CostWizardDialog(Window w) {
		super(w, "Οδηγός Τιμολογίου");
		
		setLayout(new BorderLayout());
		((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		Box bv = Box.createVerticalBox();
		
		Box bh = Box.createHorizontalBox();
		bh.add(new JLabel("Πηγή χρηματοδότησης της δαπάνης:"));
		bh.add(Box.createHorizontalStrut(5));
		bh.add(cbMoney = new JComboBox(new String[] { "Ίδιοι πόροι", "Δημόσιο",
				"Ενιαίο Πρόγραμμα Προμηθειών (Υπουργείο Ανάπτυξης)" }));
		bv.add(bh); bv.add(Box.createVerticalStrut(5));

		bh = Box.createHorizontalBox();
		bh.add(new JLabel("Τύπος δαπάνης:"));
		bh.add(Box.createHorizontalStrut(5));
		bh.add(cbCost = new JComboBox(new String[] { "Κατασκευή, επισκευή, συντήρηση έργων ΜΧ", "Λοιπές" }));
		bv.add(bh); bv.add(Box.createVerticalStrut(5));

		bh = Box.createHorizontalBox();
		bh.add(new JLabel("Τύπος προμηθευτή:"));
		bh.add(Box.createHorizontalStrut(5));
		bh.add(cbProvider = new JComboBox(new String[] { "Ιδιώτης (Εταιρίες, Καταστήματα, Εργολάβοι, Ιδιώτες)",
				"Στρατός (Λέσχες, Πρατήρια, κ.τ.λ.)", "Δημόσιο (ΔΕΗ, ΟΣΕ, κ.τ.λ.)", "Ενοικιαστής (Ενοικίαση διαμερίσματος)" }));
		bv.add(bh); bv.add(Box.createVerticalStrut(5));

		bh = Box.createHorizontalBox();
		bh.add(new JLabel("Τύπος τιμολογίου:"));
		bh.add(Box.createHorizontalStrut(5));
		bh.add(cbBill = new JComboBox(new String[] { "Προμήθεια υλικών", "Παροχή υπηρεσιών", "Αγορά υγρών καυσίμων" }));
		bv.add(bh); bv.add(Box.createVerticalStrut(5));
		
		bh = Box.createHorizontalBox();
		bh.add(new JLabel("Καθαρή αξία τιμολογίου:"));
		bh.add(Box.createHorizontalStrut(5));
		bh.add(tfValue = new JTextField());
		bv.add(bh); bv.add(Box.createVerticalStrut(5));

		bh = Box.createHorizontalBox();
		bh.add(new JLabel("<html>" + lblBills));
		bh.add(Box.createHorizontalStrut(5));
		bh.add(tfValueProvider = new JTextField());
		bv.add(bh); bv.add(Box.createVerticalStrut(5));

		getContentPane().add(bv, BorderLayout.PAGE_START);

		tpInfo = new JTextPane();
		tpInfo.setEditable(false);
		tpInfo.setContentType("text/html");
		getContentPane().add(new JScrollPane(tpInfo), BorderLayout.CENTER);

		setSize(650, 450);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		
		cbMoney.addActionListener(this);
		cbProvider.addActionListener(this);
		cbCost.addActionListener(this);
		cbBill.addActionListener(this);
		tfValue.getDocument().addDocumentListener(this);
		tfValueProvider.getDocument().addDocumentListener(this);
		
		calc();
	}

	private final String lblBills = "Καθαρή αξία <b>όλων</b> των τιμολογίων του <b>ίδιου</b> προμηθευτή:";
	private final JComboBox cbMoney;
	private final JComboBox cbProvider;
	private final JComboBox cbCost;
	private final JComboBox cbBill;
	private final JTextField tfValue;
	private final JTextField tfValueProvider;
	private final JTextPane tpInfo;

	@Override public void actionPerformed(ActionEvent e) { calc(); }
	@Override public void insertUpdate(DocumentEvent e) { calc(); }
	@Override public void removeUpdate(DocumentEvent e) { calc(); }
	@Override public void changedUpdate(DocumentEvent e) { calc(); }
	
	private void calc() {
		try {
			int idxMoney = cbMoney.getSelectedIndex();
			int idxCost = cbCost.getSelectedIndex();
			int idxProvider = cbProvider.getSelectedIndex();
			int idxBill = cbBill.getSelectedIndex();
			
			double value = Functions.round(Double.parseDouble(tfValue.getText()), 2);
			double valueprovider = 0;
			boolean fpa = true;
			double hold = 0;
			boolean agreement = false;
			int fe = 0;			
			String text = "<html><style>ul {margin-top: -15px; margin-bottom: 0}</style>";
			
			try {
				valueprovider = Functions.round(Double.parseDouble(tfValueProvider.getText()), 2);
			} catch (NumberFormatException e) {}
			if (valueprovider < value) valueprovider = value;
			text += "<b>Καθαρή Αξία:</b> " + value + " <br>" + lblBills + " " + valueprovider + " ";
			
			switch(idxMoney) {
				case 0 /*Ίδιοι Πόροι*/:
				case 1 /*Δημόσιο*/:
					switch(idxProvider) {
						case 0 /*Ιδιώτης*/:
							if (valueprovider < 2500) hold = 4.096; else { hold = 4.1996; agreement = true; }
							break;
						case 1 /*Στρατός*/:
						case 2 /*Δημόσιο*/:
							cbCost.setSelectedIndex(idxCost = 1);	// Λοιπές δαπάνες
							if (idxProvider == 1 /*Στρατός*/ && idxBill == 1 /*Παροχή Υπηρεσιών*/)
								cbBill.setSelectedIndex(idxBill = 0); //Προμήθεια υλικών
							hold = 4;
							break;
						default /*Ενοικιαστής*/:
							cbCost.setSelectedIndex(idxCost = 1);	// Λοιπές δαπάνες
							cbBill.setSelectedIndex(idxBill = 1); // Παροχή υπηρεσιών
					}
					if (idxMoney == 0 && hold != 0) hold = Functions.round(hold + 10, 4);
					break;
				case 2 /*Υπ.Αν.*/:
					cbCost.setSelectedIndex(idxCost = 1);
					cbProvider.setSelectedIndex(idxProvider = 0);
					if (idxBill == 1) cbBill.setSelectedIndex(idxBill = 0);
					hold = 4.302;
			}

			if (idxProvider == 1 /*Στρατός*/ || idxProvider == 3 /*Ενοικιαστής*/) fpa = false;
			text += "<br><b>ΦΠΑ:</b> " + (fpa ? "Προβλέπεται και το γνωρίζει ο προμηθευτής" : "0%");

			text += "<br><b>Κρατήσεις:</b> " + hold + "% της καθαρής αξίας (" +
					Math.round(value * hold) / 100 + " ), που βαρύνουν " +
					(idxProvider != 0 /*Ιδιώτης*/ ? "εμάς" : "τον προμηθευτή") +
					"<br><b>Καταλογιστέο:</b> Καθαρή Αξία" + (fpa ? " + ΦΠΑ" : "") +
					(idxProvider != 0 /*Ιδιώτης*/ ? " + Κρατήσεις" : "");			
			
			{
			double valueforfe = value;
			if (idxProvider == 0 /*Ιδιώτης*/ && valueprovider > 150 /*ΚαθαρήΑξια του προμηθευτή*/) {
				final int[] a = { 4, 8, 1 };
				fe = a[idxBill];	// Είδος τιμολογίου
				if (idxBill == 1 /*Παροχή Υπηρεσιών*/ && idxCost == 0 /*Κατασκευή Έργων*/) fe = 3;
				else valueforfe = value - Math.round(hold * value) / 100;
			}
			text += "<br><b>ΦΕ:</b> " + fe + "% της καθαρής αξίας" +
					(fe == 3 ? "" : " μειον κρατήσεις") + " (" + Math.round(valueforfe * fe) / 100 + " )<br>";
			}
	
			if (idxCost == 0 /*Κατασκευή Έργων*/ && idxBill == 1 /*Παροχή Υπηρεσιών*/)
				text += "<br>Ο εργολάβος πρέπει να μας υποβάλει αποδεικτικά κατάθεσης για 1% <b>ΤΠΕΔΕ</b> της καθαρής αξίας (" + Functions.round(0.01 * value, 2) +
						" ).<br>Εργολάβος ασφαλισμένος στο <b>ΤΣΜΕΔΕ</b> είναι υποχρεωμένος να μας υποβάλλει αποδεικτικά κατάθεσης για <b>ΤΣΜΕΔΕ</b>:" +
						"<ul><li><b>Προσωπική Εισφορά:</b><ul><li>Αν το έργο είναι <b>Γενική Εργολαβία</b>: 2% του 10% της καθαρής αξίας (" +
						Functions.round(0.002 * value, 2) + " )<li>Αν το έργο είναι <b>Τμηματικές Εργολαβίες</b>: 2% του 25% της καθαρής αξίας (" +
						Functions.round(0.005 * value, 2) + " )</ul><li><b>Υπέρ Τρίτων (Ν2166/93):</b> 0.6% της καθαρής αξίας (" +
						Functions.round(0.006 * value, 2) + " )</ul>";
			
			if (idxProvider == 0 /*Ιδιώτης*/) {
				if (valueprovider > 1500) text += "<br>Απαιτείται Φορολογική Ενημερότητα του προμηθευτή για «Πληρωμή από το Δημόσιο».";
				else if (valueprovider > 1220) text += "<br>Αν το καταλογιστέο είναι πάνω από 1500, απαιτείται Φορολογική Ενημερότητα του προμηθευτή για «Πληρωμή από το Δημόσιο».";
				if (valueprovider > 3000) text += "<br>Απαιτείται Ασφαλιστική Ενημερότητα του προμηθευτή.";
				else if (valueprovider > 2440) text += "<br>Αν το καταλογιστέο είναι πάνω από 3000, απαιτείται Ασφαλιστική Ενημερότητα του προμηθευτή.";
			}
			if (valueprovider > 60000) { text += "<br>Απαιτείται Δημόσιος Διαγωνισμός."; agreement = true; }
			else if (valueprovider > 15000 || idxCost == 0 /*Κατασκευή Έργων*/)
				{ text += "<br>Απαιτείται Πρόχειρος Διαγωνισμός."; agreement = true; }
			if (agreement) text += "<br>Απαιτείται σύναψη σύμβασης με τον προμηθευτή.";
			
			tpInfo.setText(text);
		} catch(NumberFormatException e) {
			tpInfo.setText("Συμπληρώστε σωστά τα παραπάνω πεδία για να λάβετε πληροφορίες για το τιμολόγιο αλλά και τη δαπάνη.<br>Οι κρατήσεις και το ΦΕ υπολογίζονται βάση της Φ.830/131/864670/Σ.7834/24 Οκτ 14/ΓΕΣ/ΔΟΙ/3α.");
		}
	}
}
