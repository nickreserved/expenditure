package expenditure;

import static expenditure.MainFrame.GREEK;
import static expenditure.MainFrame.getLocationScreenCentered;
import java.awt.BorderLayout;
import java.awt.Window;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.tree.DefaultTreeCellRenderer;
import expenditure.CpvTreeModel.TreeNode;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import static javax.swing.JOptionPane.CANCEL_OPTION;
import static javax.swing.JOptionPane.NO_OPTION;
import static javax.swing.JOptionPane.OK_CANCEL_OPTION;
import static javax.swing.JOptionPane.WARNING_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.tree.TreePath;

final class CpvDialog extends JDialog implements DocumentListener, MouseListener, KeyListener, WindowListener {

	@SuppressWarnings("LeakingThisInConstructor")
	public CpvDialog(Window w) {
		super(w, "Αναζήτηση CPV και συμπληρωματικού CPV");

		setLayout(new BorderLayout());
		((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

		JPanel left = new JPanel(new BorderLayout(0, 5));
		Box bh = Box.createHorizontalBox();
		bh.add(new JLabel("Αναζήτηση CPV:"));
		bh.add(Box.createHorizontalStrut(5));
		JTextField tf = new JTextField();
		docSearchCPV = tf.getDocument();
		bh.add(tf);
		left.add(bh, BorderLayout.PAGE_START);
		left.add(new JScrollPane(trCPV = new JTree(new CpvTreeModel(rootCPV))), BorderLayout.CENTER);

		JPanel right = new JPanel(new BorderLayout(0, 5));
		bh = Box.createHorizontalBox();
		bh.add(new JLabel("& συμπληρωματικού CPV:"));
		bh.add(Box.createHorizontalStrut(5));
		tf = new JTextField();
		docSearchSupCPV = tf.getDocument();
		bh.add(tf);
		right.add(bh, BorderLayout.PAGE_START);
		right.add(new JScrollPane(trSupCPV = new JTree(new CpvTreeModel(rootSupCPV))), BorderLayout.CENTER);

		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, false, left, right);
		split.setResizeWeight(0.5);
		split.setBorder(null);
		if (split.getUI() instanceof javax.swing.plaf.basic.BasicSplitPaneUI)
			((javax.swing.plaf.basic.BasicSplitPaneUI) split.getUI()).getDivider().setBorder(null);
		getContentPane().add(split, BorderLayout.CENTER);

		DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) trCPV.getCellRenderer();
		renderer.setLeafIcon(null);
		renderer.setClosedIcon(null);
		renderer.setOpenIcon(null);
		renderer = (DefaultTreeCellRenderer) trSupCPV.getCellRenderer();
		renderer.setLeafIcon(null);
		renderer.setClosedIcon(null);
		renderer.setOpenIcon(null);

		Box bv = Box.createVerticalBox();
		bv.add(Box.createVerticalStrut(5));
		bv.add(new JLabel("Με δεξί κλικ ή πλήκτρο INSERT στον CPV και στον συμπληρωματικό CPV, τον προσθέτετε στη παρακάτω λίστα."));
		bv.add(Box.createVerticalStrut(5));
		tfInfo = new JTextField();
		bv.add(tfInfo);
		getContentPane().add(bv, BorderLayout.PAGE_END);

		trCPV.setRootVisible(false);
		trCPV.setShowsRootHandles(true);
		trSupCPV.setRootVisible(false);
		trSupCPV.setShowsRootHandles(true);

		setSize(650, 450);
		setLocation(getLocationScreenCentered(getWidth(), getHeight()));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		docSearchCPV.addDocumentListener(this);
		docSearchSupCPV.addDocumentListener(this);
		trCPV.addMouseListener(this);
		trSupCPV.addMouseListener(this);
		trCPV.addKeyListener(this);
		trSupCPV.addKeyListener(this);
		addWindowListener(this);
	}

	/// Τα δύο χειριστήρια δέντρων για το CPV και τον συμπληρωματικό CPV.
	private final JTree trCPV, trSupCPV;
	/// Το κείμενο των {@code JTextField} που χρησιμοποιείται για αναζήτηση CPV και συμπληρωματικού CPV.
	private final Document docSearchCPV, docSearchSupCPV;
	/// Το χειριστήριο στο οποίο εμφανίζονται οι αντιγραμένοι CPV.
	private final JTextField tfInfo;

	@Override public void insertUpdate(DocumentEvent e) { search(e); }
	@Override public void removeUpdate(DocumentEvent e) { search(e); }
	@Override public void changedUpdate(DocumentEvent e) { search(e); }

	/** Πυροδοτείται από οποιαδήποτε αλλαγή στο κείμενο αναζήτησης CPV και συμπληρωματικού CPV.
	 * Εκτελεί την αναζήτηση του αντίστοιχου CpvTreeModel που φιλτράρει το δέντρο.
	 * @param e Το συμβάν. */
	private void search(DocumentEvent e) {
		Document doc = e.getDocument();
		String find;
		try { find = doc.getText(0, doc.getLength()); }
		catch(BadLocationException ex) { find = ""; }
		((CpvTreeModel) (doc == docSearchCPV ? trCPV.getModel() : trSupCPV.getModel()))
				.search(find);
	}

	/// Η πλήρης ιεραρχία CPV. Φορτώνεται με την @link #load().
	private static TreeNode rootCPV = null;

	/// Η πλήρης ιεραρχία συμπληρωματικών CPV. Φορτώνεται με την @link #load().
	private static TreeNode rootSupCPV = new TreeNode(null, "Συμπληρωματικοί CPV",
			new TreeNode[] {
				new TreeNode("A", "Υλικά",
						new TreeNode[] {
							new TreeNode("AA", "Μέταλλα και κράματα"),
							new TreeNode("AB", "Αμέταλλα")
						}
				),
				new TreeNode("B", "Μορφή, σχήμα, συσκευασία και ετοιμασία",
						new TreeNode[] {
							new TreeNode("BA", "Μορφή"),
							new TreeNode("BB", "Σχήμα"),
							new TreeNode("BC", "Συσκευασία και ετοιμασία")
						}
				),
				new TreeNode("C", "Υλικά/προϊόντα με ειδικές ιδιότητες και τρόπος λειτουργίας",
						new TreeNode[] {
							new TreeNode("CA", "Υλικά/προϊόντα με ειδικές ιδιότητες"),
							new TreeNode("CB", "Τρόπος λειτουργίας")
						}
				),
				new TreeNode("D", "Γενικά, διοίκηση",
						new TreeNode[] {
							new TreeNode("DA", "Γενικά και διοικητικά χαρακτηριστικά")
						}
				),
				new TreeNode("E", "Χρήστες/δικαιούχοι",
						new TreeNode[] {
							new TreeNode("EA", "Χρήστες ή δικαιούχοι")
						}
				),
				new TreeNode("F", "Καθορισμένη χρήση",
						new TreeNode[] {
							new TreeNode("FA", "Εκπαιδευτική χρήση"),
							new TreeNode("FB", "Χρήση στην ασφάλεια"),
							new TreeNode("FC", "Χρήση αποβλήτων"),
							new TreeNode("FD", "Χρήση κατά εποχές"),
							new TreeNode("FE", "Ταχυδρομική χρήση"),
							new TreeNode("FF", "Για καθαρισμό"),
							new TreeNode("FG", "Άλλη χρήση")
						}
				),
				new TreeNode("G", "Κλίμακα και διαστάσεις",
						new TreeNode[] {
							new TreeNode("GA", "Ένδειξη διαστάσεων και ισχύος"),
							new TreeNode("GB", "Συχνότητα"),
							new TreeNode("GC", "Άλλη ένδειξη")
						}
				),
				new TreeNode("H", "Λοιπά χαρακτηριστικά για τρόφιμα, ποτά και γεύματα",
						new TreeNode[] {
							new TreeNode("HA", "Χαρακτηριστικά για τρόφιμα ποτά και γεύματα")
						}
				),
				new TreeNode("I", "Λοιπά χαρακτηριστικά για κατασκευές/εργασίες",
						new TreeNode[] {
							new TreeNode("IA", "Χαρακτηριστικά για κατασκευές/εργασίες")
						}
				),
				new TreeNode("J", "Λοιπά χαρακτηριστικά για υπολογιστές, πληροφορική τεχνολογία ή επικοινωνιες",
						new TreeNode[] {
							new TreeNode("JA", "Χαρακτηριστικά για υπολογιστές, πληροφορική τεχνολογία ή επικοινωνιες")
						}
				),
				new TreeNode("K", "Λοιπά χαρακτηριστικά για διανομή ενέργειας και νερού",
						new TreeNode[] {
							new TreeNode("KA", "Χαρακτηριστικά για διανομή ενέργειας και νερού")
						}
				),
				new TreeNode("L", "Λοιπά ιατρικά και εργαστηριακά χαρακτηριστικά",
						new TreeNode[] {
							new TreeNode("LA", "Ιατρικά και εργαστηριακά χαρακτηριστικά")
						}
				),
				new TreeNode("M", "Λοιπά χαρακτηριστικά για μεταφορές",
						new TreeNode[] {
							new TreeNode("MA", "Χαρακτηριστικά για καθορισμένο τύπο οχήματος"),
							new TreeNode("MB", "Χαρακτηριστικά του οχήματος"),
							new TreeNode("MD", "Χαρακτηριστικά ειδικών μεταφορών"),
							new TreeNode("ME", "Χαρακτηριστικά για τη μεταφορά ειδικών αγαθών"),
							new TreeNode("MF", "Με χρήση οχήματος")
						}
				),
				new TreeNode("P", "Υπηρεσίες ενοικίασης",
						new TreeNode[] {
							new TreeNode("PA", "Υπηρεσίες ενοικίασης ή εκμίσθωσης"),
							new TreeNode("PB", "Υπηρεσίες πληρώματος, οδηγού ή χειριστή")
						}
				),
				new TreeNode("Q", "Λοιπά χαρακτηριστικά για υπηρεσίες διαφήμισης και παροχής νομικών συμβούλων",
						new TreeNode[] {
							new TreeNode("QA", "Υπηρεσίες διαφήμισης"),
							new TreeNode("QB", "Υπηρεσίες παροχής νομικών συμβούλων")
						}
				),
				new TreeNode("R", "Λοιπά χαρακτηριστικά για υπηρεσίες έρευνας",
						new TreeNode[] {
							new TreeNode("RA", "Ιατρική έρευνα"),
							new TreeNode("RB", "Υπηρεσίες οικονομικής έρευνας"),
							new TreeNode("RC", "Τεχνολογική έρευνα"),
							new TreeNode("RD", "Ερευνητικά πεδία")
						}
				),
				new TreeNode("S", "Λοιπά χαρακτηριστικά για χρηματοοικονομικές υπηρεσίες",
						new TreeNode[] {
							new TreeNode("SA", "Υπηρεσίες τραπεζικής"),
							new TreeNode("SB", "Υπηρεσίες ασφαλειών"),
							new TreeNode("SC", "Υπηρεσίες παροχής συντάξεων")
						}
				),
				new TreeNode("T", "Λοιπά χαρακτηριστικά για υπηρεσίες εκτύπωσης",
						new TreeNode[] {
							new TreeNode("TA", "Υπηρεσίες εκτύπωσης")
						}
				),
				new TreeNode("U", "Λοιπά χαρακτηριστικά για υπηρεσίες λιανεμπορίου",
						new TreeNode[] {
							new TreeNode("UA", "Υπηρεσίες λιανεμπορίου τροφίμων"),
							new TreeNode("UB", "Υπηρεσίες λιανεμπορίου που δεν αφορούν τρόφιμα")
						}
				),
			}
	);


	/// Οι λίστες CPV και συμπληρωματικών CPV έχουν φορτωθεί από το PHP script.
	static boolean isLoaded() { return rootCPV != null; }

	/** Το επίπεδο του CPV.
	 * <ol start="0">
	 * <li>Τομέας (Block),             2 πρώτα, 31000000-6 Ηλεκτρολογικός εξοπλισμός & μηχανές
	 * <li>Ομάδα (Group),              3 πρώτα, 31600000-2 Εξοπλισμός ηλεκτρικός
	 * <li>Τάξη (Class),               4 πρώτα, 31680000-5 Ηλεκτρολογικό υλικό & εξαρτήματα
	 * <li>Κατηγορία (Category),       5 πρώτα, 31681000-2 Ηλεκτρικά υλικά
	 * <li>Υποκατηγορία (Subcategory), 8,       31681410-0 Ηλεκτρικά υλικά συντήρησης / καλώδια
	 * </ol>
	 * @param cpv O κωδικός CPV.
	 * @return Το επίπεδο 0-4. */
	private static int getCpvLevel(String cpv) {
		if (cpv.substring(2, 8).equals("000000")) return 0;
		if (cpv.substring(3, 8).equals("00000")) return 1;
		if (cpv.substring(4, 8).equals("0000")) return 2;
		if (cpv.substring(5, 8).equals("000")) return 3;
		return 4;
	}

	/** Φορτώνει από PHP script την ιεραρχία όλων των CPV και όλων των συμπληρωματικών CPV.
	 * Μετά τη φόρτωση οι δύο ιεραρχίες αποθηκεύονται στις μεταβλητές @link #rootCPV και @link
	 * #rootSupCPV. */
	static void load() {
		String[] lines;
		try {
			String script = "<?php require('cpv_functions.php'); export_cpv_lists(); ?>";
			byte[] a = MainFrame.exportScriptOutput(script, null, true);
			lines = new String(a, GREEK).split("\\R");
		} catch (Exception e) {
			MainFrame.showError(e.getMessage());
			return;
		}

		int i = 0;
		for (; i < lines.length; ++i)
			if (lines[i].isEmpty()) break;

		TreeNode[] cpv = new TreeNode[i];
		for (i = 0; i < cpv.length; ++i)
			cpv[i] = new TreeNode(lines[i].split("\t", 2));

		TreeNode[] scpv = new TreeNode[lines.length - cpv.length - 1];
		++i; // Η κενή γραμμή
		for (int j = 0; j < scpv.length; ++j, ++i)
			scpv[j] = new TreeNode(lines[i].split("\t", 2));

		rootCPV = new TreeNode(null, "CPV",
				makeHierarchyCPV(new EnumerateCPV(cpv), 0).toArray(new TreeNode[0]));

		// Φόρτωση των συμπληρωματικών CPV στην ήδη υπάρχουσα ιεραρχία.
		ArrayList<TreeNode> list = new ArrayList<>();
		int index1 = 0, index2 = 0;
		String bag = rootSupCPV.children[index1].children[index2].cpv;
		for (i = 0; i < scpv.length; ++i) {
			String bag2 = scpv[i].cpv.substring(0, 2);
			if (!bag.equals(bag2)) {
				rootSupCPV.children[index1].children[index2].children = list.toArray(new TreeNode[0]);
				list.clear();
				for (;;) {
					++index2;
					if (rootSupCPV.children[index1].children.length == index2) {
						index2 = 0; ++index1;
						// Δεν ελέγχουμε αν το index1 ξεπεράσει τα όρια του array γιατί δεν είναι
						// φυσιολογική συμπεριφορά και θα θέλαμε να συμβεί exception.
					}
					bag = rootSupCPV.children[index1].children[index2].cpv;
					// Από τον παρακάτω έλεγχο χρειάζεται μόνο το break. Ειδάλλως κάτι κακό έχει συμβεί.
					if (bag.equals(bag2)) break;
				}
			}
			list.add(scpv[i]);
		}
		// και το τελευταίο για να κλείσουμε
		rootSupCPV.children[index1].children[index2].children = list.toArray(new TreeNode[0]);
	}

	@Override
	public void windowOpened(WindowEvent e) {}

	@Override
	public void windowClosing(WindowEvent e) {
		// Λαμβάνουμε από το πρόχειρο συστήματος το κείμενο για να δούμε αν έχουν ήδη αντιγραφεί σε
		// αυτό οι CPV και είναι όμοιοι.
		String text = null;
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		try { text = (String) clipboard.getData(DataFlavor.stringFlavor); }
		catch (UnsupportedFlavorException | IOException ex) {}
		String info = tfInfo.getText().trim();
		if (info.length() == 0 || info.equals(text)) return;
		// Αν δεν έχουν αντιγραφεί δίνουμε τη δυνατότητα αυτόματης αντιγραφής πριν κλείσει το παράθυρο
		if (NO_OPTION == showConfirmDialog(this,
				"Δεν έχετε αντιγράψει τους CPV στο πρόχειρο του συστήματος και θα χαθούν.\nΝα τους αντιγράψω;",
				"Αντιγραφή στο πρόχειρο των CPV",YES_NO_OPTION, WARNING_MESSAGE))
			return;
		clipboard.setContents(new StringSelection(info), null);
	}

	@Override
	public void windowClosed(WindowEvent e) {}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowDeactivated(WindowEvent e) {}

	/** Κλάση που περιλαμβάνει array και index σε αυτό, με δυνατότητα τροποποίησης του index.
	 * Χρησιμοποιείται για να μπει σαν παράμετρος σε συνάρτηση και να ενημερώσει το index με την
	 * επιστροφή. Το αρχικό index είναι 0. */
	private static class EnumerateCPV {
		/** Ο constructor.
		 * @param nodes Το array. */
		EnumerateCPV(TreeNode[] nodes) { array = nodes; }
		/// Το array.
		TreeNode[] array;
		/// O δείκτης θέσης του array.
		int index = 0;
	}

	private static ArrayList<TreeNode> makeHierarchyCPV(EnumerateCPV flatten, int level) {
		ArrayList<TreeNode> list = new ArrayList<>();
		while (flatten.index < flatten.array.length) {
			int nextLevel = getCpvLevel(flatten.array[flatten.index].cpv);
			if (level == nextLevel) {
				list.add(flatten.array[flatten.index]);
				++flatten.index;
			} else if (level < nextLevel)
				list.get(list.size() - 1).children = makeHierarchyCPV(flatten, nextLevel)
																.toArray(new TreeNode[0]);
			else if (level > nextLevel) break;
		}
		return list;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() != MouseEvent.BUTTON3) return;
		JTree tree = (JTree) e.getComponent();
		addCPV(tree, tree.getPathForLocation(e.getX(), e.getY()));
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}


	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() != KeyEvent.VK_INSERT) return;
		JTree tree = (JTree) e.getComponent();
		addCPV(tree, tree.getSelectionPath());
	}


	private void addCPV(JTree tree, TreePath path) {
		if (path == null) return;
		TreeNode node = (TreeNode) path.getLastPathComponent();
		if (tree == trCPV) {
			if (getCpvLevel(node.cpv) < 2)
				if (CANCEL_OPTION == showConfirmDialog(this,
					"Η επιλογή ενός γενικού CPV ίσως να οδηγήσει σε δικαστική προσβολή του διαγωνισμού.\nΘέλετε να συνεχίσω;",
					"Πολύ γενικός CPV", OK_CANCEL_OPTION, WARNING_MESSAGE)) return;
			String now = tfInfo.getText().trim();
			if (!now.isEmpty()) {
				if (now.endsWith(",")) now += " "; // Δεν πολυχρειάζεται
				else now += ", ";
			}
			now += node.cpv;
			tfInfo.setText(now);
		} else {	// tree == trSupCPV
			if (node.cpv.length() != 6) return;
			String now = tfInfo.getText().trim();
			if (now.isEmpty()) {
				showMessageDialog(this,
						"Ένας συμπληρωματικός CPV ακολουθεί έναν CPV.\nΔεν υπάρχει μόνος του.",
						"Συμπληρωματικός CPV δίχως CPV", WARNING_MESSAGE);
				return;
			} else if (now.endsWith(",")) // Δεν πολυχρειάζεται
				now = now.substring(0, now.length() - 1).trim(); // Δεν πολυχρειάζεται
			now += " " + node.cpv;
			tfInfo.setText(now);
		}
	}
}
