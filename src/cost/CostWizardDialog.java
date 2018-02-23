package cost;

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

		setSize(600, 450);
		setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
		
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

	@Override	public void actionPerformed(ActionEvent e) { calc(); }
	@Override public void insertUpdate(DocumentEvent e) { calc(); }
	@Override public void removeUpdate(DocumentEvent e) { calc(); }
	@Override	public void changedUpdate(DocumentEvent e) { calc(); }
	
	private void calc() {
		try {
			int[] cb = new int[4];
			cb[0] = cbMoney.getSelectedIndex();
			cb[1] = cbCost.getSelectedIndex();
			cb[2] = cbProvider.getSelectedIndex();
			cb[3] = cbBill.getSelectedIndex();
			
			double value = common.M.round(Double.parseDouble(tfValue.getText()), 2);
			double valueprovider = 0;
			boolean fpa = true;
			double hold = 0;
			boolean agreement = false;
			int fe = 0;			
			String text = "<html><style>ul {margin-top: -15px; margin-bottom: 0}</style>";
			
			try {
				valueprovider = common.M.round(Double.parseDouble(tfValueProvider.getText()), 2);
			} catch (NumberFormatException e) {}
			if (valueprovider < value) valueprovider = value;
			text += "<b>Καθαρή Αξία:</b> " + value + " <br>" + lblBills + " " + valueprovider + " ";
			
			if (cb[2] == 1 /*Στρατός*/ || cb[2] == 3 /*Ενοικιαστής*/) fpa = false;
			text += "<br><b>ΦΠΑ:</b> " + (fpa ? "Προβλέπεται και το γνωρίζει ο προμηθευτής" : "0%");
			
			switch(cb[0]) {
				case 0 /*Ίδιοι Πόροι*/:
				case 1 /*Δημόσιο*/:
					switch(cb[2]) {
						case 0 /*Ιδιώτης*/:
							if (cb[1] == 0 /*Κατασκευή Έργων*/ && cb[3] == 1 /*Παροχή Υπηρεσιών*/)
								if (valueprovider < 2500) hold = 5.12; else { hold = 5.2236; agreement = true; }
							else
								if (valueprovider < 2500) hold = 4.096; else { hold = 4.1996; agreement = true; }
							break;
						case 1 /*Στρατός*/:
							cbCost.setSelectedIndex(1);	// Λοιπές δαπάνες
							if (cb[3] == 1 /*Παροχή Υπηρεσιών*/) cbBill.setSelectedIndex(0); //Προμήθεια υλικών
							hold = 4;
							break;
						case 2 /*Δημόσιο*/:
							cbCost.setSelectedIndex(1);	// Λοιπές δαπάνες
							if (valueprovider < 2500) hold = 4.096; else { hold = 4.1996; agreement = true; }
							break;
						default /*Ενοικιαστής*/:
							cbCost.setSelectedIndex(1);	// Λοιπές δαπάνες
							cbBill.setSelectedIndex(1); // Παροχή υπηρεσιών
					}
					if (cb[0] == 0 && hold != 0) hold = common.M.round(hold + 10, 4);
					break;
				case 3 /*Υπ.Αν.*/:
					cbCost.setSelectedIndex(1);
					cbProvider.setSelectedIndex(0);
					if (cb[3] == 1) cbBill.setSelectedIndex(0);
					hold = 4.6092;
			}
			text += "<br><b>Κρατήσεις:</b> " + hold + "% της καθαρής αξίας (" +
					Math.round(value * hold) / 100 + " ), που βαρύνουν " +
					(cb[2] != 0 /*Ιδιώτης*/ ? "εμάς" : "τον προμηθευτή") +
					"<br><b>Καταλογιστέο:</b> Καθαρή Αξία" + (fpa ? " + ΦΠΑ" : "") +
					(cb[2] != 0 /*Ιδιώτης*/ ? " + Κρατήσεις" : "");			
			
			{
			double valueforfe = value;
			if (cb[2] == 0 /*Ιδιώτης*/ && valueprovider > 150 /*ΚαθαρήΑξια του προμηθευτή*/) {
				final int[] a = { 4, 8, 1 };
				fe = a[cb[3]];	// Είδος τιμολογίου
				if (cb[3] == 1 /*Παροχή Υπηρεσιών*/ && cb[1] == 0 /*Κατασκευή Έργων*/) fe = 3;
				else valueforfe = value - Math.round(hold * value) / 100;
			}
			text += "<br><b>ΦΕ:</b> " + fe + "% της καθαρής αξίας" +
					(fe == 3 ? "" : " μειον κρατήσεις") + " (" + Math.round(valueforfe * fe) / 100 + " )<br>";
			}
	
			if (cb[1] == 0 /*Κατασκευή Έργων*/ && cb[3] == 1 /*Παροχή Υπηρεσιών*/) {
				double a = common.M.round(0.005 * value, 2);
				text += "<br>Ο εργολάβος πρέπει να μας υποβάλει αποδεικτικά κατάθεσης για:<ul><li>0.5% <b>ΕΜΠ</b> της καθαρής αξίας (" +
						a + " )<li>0.5% <b>ΤΣΜΕΔΕ</b> της καθαρής αξίας (" + a + " )<li>0.5% <b>ΤΠΕΔΕ</b> της καθαρής αξίας (" + a +
						" )</ul>Εργολάβος ασφαλισμένος στο <b>ΤΣΜΕΔΕ</b> είναι υποχρεωμένος να μας υποβάλλει αποδεικτικά κατάθεσης για <b>ΤΣΜΕΔΕ</b>:" +
						"<ul><li><b>Προσωπική Εισφορά:</b><ul><li>Αν το έργο είναι <b>Γενική Εργολαβία</b>: 2% του 10% της καθαρής αξίας (" +
						common.M.round(0.002 * value, 2) + " )<li>Αν το έργο είναι <b>Τμηματικές Εργολαβίες</b>: 2% του 25% της καθαρής αξίας (" +
						a + " )</ul><li><b>Υπέρ Τρίτων (Ν2166/93):</b> 0.6% της καθαρής αξίας (" +
						common.M.round(0.006 * value, 2) + " )</ul>";
			}
			
			if (cb[2] == 0 /*Ιδιώτης*/) {
				if (valueprovider > 1220) text += "<br>Αν το καταλογιστέο είναι πάνω από 1500, απαιτείται Φορολογική Ενημερότητα του προμηθευτή για «Πληρωμή από το Δημόσιο».";
				if (valueprovider > 2440) text += "<br>Αν το καταλογιστέο είναι πάνω από 3000, απαιτείται Ασφαλιστική Ενημερότητα του προμηθευτή.";
			}
			if (valueprovider > 60000) { text += "<br>Απαιτείται Δημόσιος Διαγωνισμός."; agreement = true; }
			else if (valueprovider > 15000 || cb[1] == 0 /*Κατασκευή Έργων*/)
				{ text += "<br>Απαιτείται Πρόχειρος Διαγωνισμός."; agreement = true; }
			if (agreement) text += "<br>Απαιτείται σύναψη σύμβασης με τον προμηθευτή.";
			
			tpInfo.setText(text);
		} catch(NumberFormatException e) {
			tpInfo.setText("Συμπληρώστε σωστά τα παραπάνω πεδία για να λάβετε πληροφορίες για το τιμολόγιο αλλά και τη δαπάνη.");
		}
	}
}
