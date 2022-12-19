package expenditure;

import static expenditure.Deduction.D14;
import static expenditure.Deduction.D14_096;
import static expenditure.Deduction.D4;
import expenditure.Expenditure.Financing;
import java.awt.AWTEvent;
import static java.awt.Color.WHITE;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import static java.lang.ClassLoader.getSystemResource;
import static java.lang.ClassLoader.getSystemResourceAsStream;
import java.net.ServerSocket;
import java.net.Socket;
import static java.net.URLDecoder.decode;
import java.nio.charset.Charset;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;
import static java.util.concurrent.TimeUnit.MINUTES;
import java.util.stream.Stream;
import static javax.swing.BorderFactory.createLineBorder;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import static javax.swing.JOptionPane.CLOSED_OPTION;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.NO_OPTION;
import static javax.swing.JOptionPane.OK_CANCEL_OPTION;
import static javax.swing.JOptionPane.OK_OPTION;
import static javax.swing.JOptionPane.PLAIN_MESSAGE;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;
import static javax.swing.JOptionPane.WARNING_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.YES_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;
import static javax.swing.JOptionPane.showInputDialog;
import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.JOptionPane.showOptionDialog;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager.LookAndFeelInfo;
import static javax.swing.UIManager.getInstalledLookAndFeels;
import static javax.swing.UIManager.getSystemLookAndFeelClassName;
import static javax.swing.UIManager.setLookAndFeel;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.TableModelEvent;
import static javax.swing.event.TableModelEvent.ALL_COLUMNS;
import static javax.swing.event.TableModelEvent.DELETE;
import static javax.swing.event.TableModelEvent.INSERT;
import static javax.swing.event.TableModelEvent.UPDATE;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;
import util.ComboDataModel;
import util.ComboPlusOneDataModel;
import util.ExtensionFileFilter;
import util.PhpScriptRunner;
import util.PhpScriptRunner.StdInStream;
import util.PhpSerializer;
import util.PhpSerializer.FormatException;
import util.PhpSerializer.VariableSerializable;
import util.PropertiesTableModel;
import util.PropertiesTableModel.TableRecord;
import static util.PropertiesTableModel.createTable;
import util.ResizableHeaderTableModel;
import util.ResizablePropertiesTableModel;
import util.ResizableTableModel;
import static util.ResizableTableModel.createTable;
import static expenditure.Deduction.D4_1996;
import static expenditure.Deduction.D0_1036;
import static expenditure.Deduction.D14_1996;
import static expenditure.Deduction.D4_296;
import static expenditure.Deduction.D4_3996;
import static expenditure.Deduction.D0_2;
import static expenditure.Deduction.D0_3036;
import static expenditure.Deduction.D14_296;
import static expenditure.Deduction.D14_3996;
import static expenditure.Deduction.D4_096;

/** Το κυρίως παράθυρο του προγράμματος.
 * Περιλαμβάνει τη main(). */
final public class MainFrame extends JFrame {

	// ============================================================ ΣΤΑΘΕΡΕΣ ===

	/** Η διαδρομή του φακέλου του προγράμματος */
	static private final String ROOT_PATH = getRootPath();
	/** Η διαδρομή του αρχείου ρυθμίσεων του προγράμματος */
	static private String iniPath;
	/** Η έκδοση του προγράμματος. */
	static private final String VERSION = "20 Δεκ 22";
	/** Το όνομα του αρχείου ρυθμίσεων του προγράμματος */
	static private final String INI = "expenditure.ini";
	/** Η ομάδα χαρακτήρων των ελληνικών. Χρησιμοποιείται στα εξαγόμενα αρχεία RTF. */
	static private final Charset GREEK = Charset.forName("windows-1253");
	/** Επιλογές ναι - όχι. */
	static final String[] NOYES = { "Όχι", "Ναι" };
	/** Η πόρτα του server για επικοινωνία μεταξύ των εφαρμογών. */
	static private final int SERVER_PORT = 6666;

	// ==================================== ΟΛΑ ΤΑ ΔΕΔΟΜΕΝΑ ΤΟΥ ΠΡΟΓΡΑΜΜΑΤΟΣ ===

	/** Όλα τα δεδομένα του προγράμματος. */
	static AppData data;

	// ====================================== ΓΡΑΦΙΚΑ ΣΤΟΙΧΕΙΑ ΤΟΥ ΠΑΡΑΘΥΡΟΥ ===

	/** Το παράθυρο του προγράμματος.
	 * Κατά την εκτέλεση της main() είναι null. Μετά ορίζεται. */
	static MainFrame window;
	/** Ο πίνακας τιμολογίων. */
	private JTable tblInvoices;
	/** Το μοντέλο δεδομένων του πίνακα με τα αθροίσματα τιμολογίων. */
	private ReportTableModel rtmReport;
	/** Το μοντέλο δεδομένων του πίνακα εργασιών. */
	private WorksTableModel rtmWorks;
	/** Το μοντέλο δεδομένων του πίνακα των δικαιούχων. */
	private ResizableHeaderTableModel<Contractor> rtmContractors;
	/** Το μοντέλο δεδομένων του πίνακα κρατήσεων. */
	private ResizableTableModel<Deduction> rtmDeductions;
	/** Το μοντέλο δεδομένων του πίνακα προσωπικού. */
	private ResizableHeaderTableModel rtmPersonnel;
	/** Το μοντέλο δεδομένων του πίνακα με το φύλλο καταχώρησης της δαπάνης. */
	private ContentsTableModel rtmContents;
	/** Το μοντέλο δεδομένων του πίνακα των ταυτοτήτων των συμβάσεων. */
	private ResizableHeaderTableModel<Contract> rtmContracts;
	/** Το μοντέλο δεδομένων του combobox των διαγωνισμών. */
	private ComboDataModel<Tender> cdmTenders;
	/** Το μοντέλο δεδομένων του πίνακα διαγωνιζόμενων. */
	private CompetitorTableModel rtmCompetitors;
	/** Το μοντέλο δεδομένων του πίνακα με τα στοιχεία της δαπάνης. */
	private PropertiesTableModel rtmExpenditure;
	/** Εικονίδιο δημιουργίας. */
	final static public Icon ICON_NEW = loadIcon("new");
	/** Εικονίδιο διαγραφής. */
	final static public Icon ICON_DELETE = loadIcon("close");

	// ============================================= ΕΝΑΡΞΗ ΤΟΥ ΠΡΟΓΡΑΜΜΑΤΟΣ ===

	/** Το entry point του προγράμματος.
	 * @param args Οι παράμετροι του προγράμματος. Το πρόγραμμα δέχεται σαν παραμέτρους μόνο ονόματα
	 * αρχείων δαπανών για άνοιγμα. */
	public static void main(String[] args) {
		if (!serverStart(args)) return;				// Απλό return γιατί ο server δεν άνοιξε
		if (!initPHP()) { serverKill(); return; }
		iniLoad();									// Οι ρυθμίσεις του προγράμματος
		importNewDeductions();		// Εισάγει τυχόν νέες κρατήσεις κατά την πρώτη εκτέλεση νέας έκδοσης
		expendituresOpen(args);		// Ανοίγει δαπάνες που έχουν δωθεί ως παράμετροι
		setSkin();					// Ορίζει το κέλυφος του παραθύρου (L&F)
		window = new MainFrame();	// Δημιουργία του παραθύρου του προγράμματος
		// Όχι μέσα στη MainFrame() γιατί θα υπάρξουν αναφορές στο window πριν αυτό τεθεί
		window.setVisible(true);	// Εμφάνιση του παραθύρου του προγράμματος
		iniAutoSave();				// Αποθήκευση αλλαγών κάθε 5 λεπτά
	}

	/** Αρχικοποιεί το παράθυρο του προγράμματος. */
	public MainFrame() {
		super("Στρατιωτικές Δαπάνες");
		setIconImage(loadIcon("app").getImage());
		getContentPane().add(createPanels());
		updatePanels();
		setJMenuBar(createMenuMain());
		updateMenus();
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(640, 450);
		setLocation(getLocationScreenCentered(getWidth(), getHeight()));
	}

	// ============================================================== PANELS ===

	/** Δημιουργεί τις καρτέλες του παραθύρου της εφαρμογής.
	 * @return Οι καρτέλες τις εφαρμογής σε ένα panel */
	private JTabbedPane createPanels() {
		// ------------------------------------------ ΚΟΙΝΟΧΡΗΣΤΑ COMBOBOXES ---
		// Βρίσκονται εδώ γιατί κάθε φορά που αλλάζει το κέλυφος πρέπει να επαναδημιουργούνται.
		// Επιλογέας προσώπων που περιέχει λίστα όλου του προσωπικού.
		JComboBox cbPersonnel = new JComboBox(new ComboPlusOneDataModel() {
			@Override protected List get() { return data.personnel; }
		});
		// Επιλογέας δικαιούχων / εργολάβων / προμηθευτών / ανάδοχων.
		JComboBox cbContractors = new JComboBox(new ComboPlusOneDataModel() {
			@Override protected List get() { return data.contractors; }
		});
		// Επιλογέας true / false.
		JComboBox cbBoolean = new JComboBox(NOYES);
		// Επιλογέας διαθέσιμων μονάδων μέτρησης, με δυνατότητα προσθήκης από το πληκτρολόγιο.
		JComboBox cbUnits = new JComboBox(new String[] {
			"τεμάχια", "Kgr", "ton", "lt", "mm", "cm", "cm²", "cm³", "m", "m²", "m³", "ρολά", "πόδια",
			"λίβρες", "ζεύγη", "στρέμματα", "Km", "Km²", "τονοχιλιόμετρα", "ώρες", "ημέρες", "μήνες"
		});
		cbUnits.setEditable(true);
		// Περίγραμμα για όλα
		Border border = createLineBorder(WHITE, 0);
		cbUnits      .setBorder(border);
		cbPersonnel  .setBorder(border);
		cbContractors.setBorder(border);
		cbBoolean    .setBorder(border);

		// ---------------------------------------------- ΚΑΡΤΕΛΑ «ΕΡΓΑΣΙΕΣ» ---
		// Πίνακας εργασιών
		rtmWorks = new WorksTableModel();
		JTable tblWorks = createTable(rtmWorks, true, false);
		tblWorks.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblWorks.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(cbUnits));
		// Πίνακας υλικών
		MaterialsTableModel rtmMaterials = new MaterialsTableModel(tblWorks);
		JTable tblMaterial = createTable(rtmMaterials, true, true);
		tblMaterial.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(cbUnits));
		// Δημιουργία του panel
		JSplitPane spWorks = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				new JScrollPane(tblWorks),
				new JScrollPane(tblMaterial));
		spWorks.setDividerSize(3);
		spWorks.setDividerLocation(75);

		// -------------------------------------- ΚΑΡΤΕΛΑ «ΣΤΟΙΧΕΙΑ ΜΟΝΑΔΑΣ» ---
		// Οι επικεφαλίδες του πίνακα στοιχείων Μονάδας
		String[] unitHeader = {
			"<html><b>Στοιχεία Μονάδας",
			"<html>Ελέγχουσα Αρχή <font size=2><i>(σύντμηση)",
			"<html>Σχηματισμός <font size=2><i>(σύντμηση)",
			"<html>Μονάδα <font size=2><i>(πλήρης)",
			"<html>Μονάδα <font size=2><i>(σύντμηση)",
			UnitInfo.H[4], UnitInfo.H[5], UnitInfo.H[6], UnitInfo.H[7], UnitInfo.H[8], UnitInfo.H[9],
			UnitInfo.H[10], UnitInfo.H[11], UnitInfo.H[12], UnitInfo.H[13],
			"<html><b>Στοιχεία Επιτροπών Δαπάνων",
			UnitInfo.H[14], UnitInfo.H[15], UnitInfo.H[16], UnitInfo.H[17], UnitInfo.H[18],
			UnitInfo.H[19], UnitInfo.H[20], UnitInfo.H[21], UnitInfo.H[22], UnitInfo.H[23]
		};
		// Οι επεξεργαστές για τα πεδία του πίνακα με τα στοιχεία της Μονάδας.
		// Πρέπει να είναι τύπου Component γιατί τα null στοιχεία, αντικαθίστανται με JTextField.
		Component[] unitEditors = {
			null, null, null, null, null, null, null, null, null, null, null, cbPersonnel, cbPersonnel,
			cbPersonnel, cbPersonnel,
			null, null, cbPersonnel
		};

		// Το tabbed panel πρέπει να είναι πρώτο στοιχείο της φορμας και οι Εργασίες το 4ο στοιχείο
		// του tabbed panel.
		// Μετά από κάθε προσθηκη, διαγραφή, αλλαγή σειράς να ελέγχω το MainFrame.updatePanels().
		JTabbedPane tabs = new JTabbedPane();
		tabs.addTab("Στοιχεία Δαπάνης", createPanelExpenditure(unitHeader, unitEditors, cbBoolean, cbPersonnel));
		tabs.addTab("Τιμολόγια", createPanelInvoices(cbUnits, rtmMaterials));
		tabs.addTab("Συμβάσεις", createPanelContracts(cbContractors));
		tabs.addTab("Διαγωνισμοί", createPanelTenders(cbBoolean, cbContractors));
		tabs.addTab("Φύλλο καταχώρησης", createPanelContents());
		tabs.addTab("Εργασίες", spWorks);
		tabs.addTab("Στοιχεία Μονάδας", createPanelUnit(unitHeader, unitEditors));
		tabs.addTab("Δικαιούχοι", createPanelContractors(border));
		tabs.addTab("Ανάλυση Κρατήσεων", createPanelDeductions());
		tabs.addTab("Προσωπικό Μονάδας", createPanelPersonnel());
		return tabs;
	}

	/** Δημιουργεί ένα panel με τα στοιχεία της Μονάδας.
	 * @param unitHeader Οι επικεφαλίδες γραμμών του πίνακα
	 * @param unitEditors Λίστα με τους χειριστές που επεξεργάζονται τα κελιά. Όσα είναι null
	 * θεωρούνται ως text editors.
	 * @return Το panel με τα στοιχεία της Μονάδας */
	private JScrollPane createPanelUnit(String[] unitHeader, Component[] unitEditors) {
		// Ρύθμιση του πίνακα με τα στοιχεία Μονάδας
		PropertiesTableModel ptm = new PropertiesTableModel(unitHeader, 1) {
			@Override public TableRecord get(int index) { return data.unitInfo; }
			@Override public boolean isCellEditable(int row, int col) {
				return super.isCellEditable(row, col) && row != 0 && row != 15 && row != 29;
			}
		};
		return new JScrollPane(createTable(ptm, unitEditors));
	}

	/** Δημιουργεί ένα panel με τα στοιχεία των τιμολογίων.
	 * @param cbUnit Ο επιλογέας με τις μονάδες μέτρησης
	 * @param rtmMaterials Το μοντέλο δεδομένων του πίνακα υλικών της καρτέλας «Εργασίες»
	 * @return Το panel με τα στοιχεία των τιμολογίων */
	private Box createPanelInvoices(JComboBox cbUnits, MaterialsTableModel rtmMaterials) {
		// Επιλογέας κρατήσεων.
		JComboBox cbDeductions = new JComboBox(new ComboPlusOneDataModel() {
			@Override protected List get() { return data.deductions; }
		});
		cbDeductions.setBorder(cbUnits.getBorder());
		// Επιλογέας δικαιούχων / εργολάβων / προμηθευτών / ανάδοχων και συμβάσεων.
		JComboBox cbContracts = new JComboBox(new ContractsContractorsDataModel());
		cbContracts.setBorder(cbUnits.getBorder());
		// Ρύθμιση του πίνακα τιμολογίων
		// Το μοντέλο του πίνακα τιμολογίων.
		String[] headers = { Invoice.H[0], Invoice.H[1], Invoice.H[2] + " ή Σύμβαση", Invoice.H[3], Invoice.H[4] };
		ResizableTableModel<Invoice> rtmInvoices = new ResizableHeaderTableModel<Invoice>(headers) {
			@Override protected List<Invoice> get() { return data.expenditure == null ? null : data.expenditure.invoices; }
			@Override protected Invoice createNew() { return new Invoice(data.expenditure); }
			@Override public void remove(int index) {
				data.expenditure.invoices.get(index).recalcRemove();
				data.expenditure.invoices.remove(index);
			}
			@Override public boolean isCellEditable(int row, int col) {
				// Αν οι αυτόματοι υπολογισμοί είναι ενεργοί, τα κελιά των κρατήσεων και ΦΕ
				// δεν επεξεργάζονται
				return col != 3 && col != 4 || !data.expenditure.isSmart();
			}
		};
		JComboBox invoiceTypes = new JComboBox(Invoice.Type.values());
		JComboBox incomeTax    = new JComboBox(new Byte[] { 4, 8, 0, 1, 3, 20 });
		invoiceTypes.setBorder(cbUnits.getBorder());
		incomeTax   .setBorder(cbUnits.getBorder());
		tblInvoices = createTable(rtmInvoices, false, false);
		tblInvoices.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		TableColumnModel cm = tblInvoices.getColumnModel();
		cm.getColumn(1).setCellEditor(new DefaultCellEditor(invoiceTypes));
		cm.getColumn(2).setCellEditor(new DefaultCellEditor(cbContracts));
		cm.getColumn(3).setCellEditor(new DefaultCellEditor(cbDeductions));
		cm.getColumn(4).setCellEditor(new DefaultCellEditor(incomeTax));

		// Επιλογέας ΦΠΑ
		JComboBox vat = new JComboBox(new Byte[] { 24, 13, 6, 0 });
		vat.setBorder(cbUnits.getBorder());
		vat.setEditable(true);
		// Ρύθμιση του πίνακα ειδών τιμολογίου
		InvoiceItemsTableModel rtmItems = new InvoiceItemsTableModel();
		JTable tblItems = createTable(rtmItems, true, true);
		cm = tblItems.getColumnModel();
		cm.getColumn(4).setCellEditor(new DefaultCellEditor(vat));
		cm.getColumn(7).setCellEditor(new DefaultCellEditor(cbUnits));
		// Προσθήκη επιπλέον επιλογής στο popup menu για μεταφορά υλικών στις εργασίες
		JPopupMenu popupMenu = tblItems.getComponentPopupMenu();
		popupMenu.addSeparator();
		popupMenu.add(createMenuItem("Αντιγραφή επιλεγμένων γραμμών στην τρέχουσα εργασία", "import", e -> {
			// Αντί να αποκτήσουμε πρόσβαση μέσω data, το οποίο είναι επίπονο γιατί πρέπει να
			// εντοπίσουμε τρέχουσα εργασία και τρέχον τιμολόγιο, χρησιμοποιούμε τα δεδομένα
			// των αντίστοιχων μοντέλων πινάκων
			List<Material> materials = rtmMaterials.get();
			List<InvoiceItem> items = rtmItems.get();
			if (materials != null) {
				int rows[] = tblItems.getSelectedRows();
				int from = materials.size();
				for (int row : rows)
					if (row < items.size())
						materials.add(new Material(items.get(row)));
				rtmMaterials.fireTableDataChanged(new TableModelEvent(rtmMaterials, from,
						materials.size() - 1, ALL_COLUMNS, INSERT));
			} else showMessageDialog(this, "Δεν υπάρχει επιλεγμένη εργασία για να προστεθούν υλικά.\n" +
					"Επιλέξτε πρώτα μια εργασία στην καρτέλα «Εργασίες» και μετά προσθέστε υλικά, από τα τιμολόγια, σε αυτή.",
					"Αντιγραφή ειδών στην τρέχουσα εργασία", ERROR_MESSAGE);
		}));

		// Ρύθμιση του πίνακα με τα κόστη των τιμολογίων
		rtmReport = new ReportTableModel();
		JTable tblReport = PropertiesTableModel.createTable(rtmReport);

		// Όταν επιλέγουμε άλλο τιμολόγιο στον πίνακα τιμολογίων
		tblInvoices.getSelectionModel().addListSelectionListener(e -> {
			if (e.getValueIsAdjusting()) return;
			// Αν υπάρχει κελί υπό επεξεργασία στον πίνακα με τα είδη του τιμολογίου,
			// η επεξεργασία πρέπει να σταματήσει
			TableCellEditor tce = tblItems.getCellEditor();
			if (tce != null) tce.stopCellEditing();
			// Εύρεση του επιλεγμένου τιμολογίου
			int selection = window.tblInvoices.getSelectedRow();
			List<Invoice> list = data.expenditure.invoices;
			Invoice i = list != null && selection >= 0 && selection < list.size()
					? list.get(selection) : null;
			// Ανανέωση δεδομένων των πινάκων ειδών και αθροισμάτων
			rtmItems.setSelectedInvoice(i);
			window.rtmReport.setSelectedInvoice(i);
		});

		// Όταν τροποποιούμε ένα είδος τιμολογίου στον αντίστοιχο πίνακα
		rtmItems.addTableModelListener(e -> {
			int col = e.getColumn();
			// Κελιά που δεν αλλάζουν αθροίσματα. Σίγουρα δεν είναι INSERT / DELETE
			if (col == 0 || col == 7) return;
			ResizableTableModel src = (ResizableTableModel) e.getSource();
			// Αν τροποποιηθεί κάποιο αριθμητικό κελί του πίνακα, επηρεάζεται όλη η εγγραφή
			if (e.getType() == UPDATE && col > 0 && col < 7)
				tblItems.tableChanged(new TableModelEvent(src, e.getFirstRow(), e.getLastRow(), ALL_COLUMNS));
			// Όταν έχουμε αυτόματο υπολογισμό, ο πίνακας τιμολογίων ανανεώνει τις στήλες σύμβασης,
			// κρατήσεων και ΦΕ (όλα τα τιμολόγια, λόγω πιθανού ίδιου δικαιούχου - όχι μόνο το τρέχον)
			if (data.expenditure.isSmart()) {	// έχουμε αυτόματο υπολογισμό
				int to = data.expenditure.invoices.size() - 1;
				tblInvoices.tableChanged(new TableModelEvent(src, 0, to, 2));
				tblInvoices.tableChanged(new TableModelEvent(src, 0, to, 3));
				tblInvoices.tableChanged(new TableModelEvent(src, 0, to, 4));
				if (col != 4) {
					window.rtmContents.fireTableDataChanged();
					window.rtmContracts.fireTableDataChanged();
				}
			}
			// Ο πίνακας με τα κόστη ανανεώνεται για να εμφανίζει τα νέα κόστη
			window.rtmReport.fireTableDataChanged(new TableModelEvent(src, 0, 6));
		});

		// Όταν τροποποιούμε ένα τιμολόγιο στον αντίστοιχο πίνακα
		rtmInvoices.addTableModelListener(e -> {
			ResizableTableModel src = (ResizableTableModel) e.getSource();
			int col = e.getColumn();
			// Εισήχθη ένα τιμολόγιο: Τίποτα
			if (e.getType() == INSERT);
			// Διαγράφηκε ένα τιμολόγιο: Ο πίνακας με τα κόστη και το φύλλο καταχώρησης ανανεώνεται
			else if (e.getType() == DELETE) {
				window.rtmReport.fireTableDataChanged(new TableModelEvent(src, 0, 6, 4));	// Στήλη δαπάνης
				window.rtmContents.fireTableDataChanged();
			}
			else if (data.expenditure.isSmart()) {	// έχουμε αυτόματο υπολογισμό
				// Αν άλλαξε ο τύπος του τιμολογίου ή ο δικαιούχος
				if (col == 1 || col == 2) {
					// Ενδέχεται να άλλαξαν κρατήσεις και ΦΕ σε όλα τα τιμολόγια του ίδιου δικαιούχου
					int to = data.expenditure.invoices.size();
					window.tblInvoices.tableChanged(new TableModelEvent(src, 0, to, 3));
					window.tblInvoices.tableChanged(new TableModelEvent(src, 0, to, 4));
					// Ο πίνακας με τα είδη τιμολογίου ανανεώνεται γιατί μπορεί να άλλαξε το ΦΠΑ
					int rows = rtmItems.getRowCount() - 1;
					tblItems.tableChanged(new TableModelEvent(src, 0, rows, 4));	// Στήλη ΦΠΑ
					tblItems.tableChanged(new TableModelEvent(src, 0, rows, 5));	// Στήλη καταλογιστέας αξίας ενός
					tblItems.tableChanged(new TableModelEvent(src, 0, rows, 6));	// Στήλη καταλογιστέας αξίας όλων
					// Ο πίνακας με τα κόστη ανανεώνεται γιατί μπορεί να άλλαξαν κρατήσεις και ΦΕ
					window.rtmReport.fireTableDataChanged(new TableModelEvent(src, 1, 6, 1));	// Στήλη τιμολογίου
					window.rtmReport.fireTableDataChanged(new TableModelEvent(src, 1, 6, 2));	// Στήλη σύμβασης
					window.rtmReport.fireTableDataChanged(new TableModelEvent(src, 1, 6, 3));	// Στήλη διαγωνισμού
					window.rtmReport.fireTableDataChanged(new TableModelEvent(src, 1, 6, 4));	// Στήλη δαπάνης
					// Μπορεί να εισήχθε σύμβαση (αν άλλαξε δικαιούχος)
					if (col == 2) window.rtmContracts.fireTableDataChanged();
				}
			// Αν άλλαξε η σύμβαση του τιμολογίου, ο πίνακας με τα κόστη και το φύλλο καταχώρησης ανανεώνεται
			} else if (col == 2) {
				window.rtmReport.fireTableDataChanged(new TableModelEvent(rtmItems, 0, 6, 2));	// Στήλη σύμβασης
				window.rtmReport.fireTableDataChanged(new TableModelEvent(rtmItems, 0, 6, 3));	// Στήλη διαγωνισμού
				window.rtmContents.fireTableDataChanged();
			}
			// Αν άλλαξαν κρατήσεις ή ΦΕ, ο πίνακας με τα κόστη ανανεώνεται
			else if (col == 3 || col == 4) {
				window.rtmReport.fireTableDataChanged(new TableModelEvent(src, 2, 6, 1));	// Στήλη τιμολογίου
				window.rtmReport.fireTableDataChanged(new TableModelEvent(src, 2, 6, 2));	// Στήλη σύμβασης
				window.rtmReport.fireTableDataChanged(new TableModelEvent(src, 2, 6, 3));	// Στήλη διαγωνισμού
				window.rtmReport.fireTableDataChanged(new TableModelEvent(src, 2, 6, 4));	// Στήλη δαπάνης
			}
		});

		// Δημιουργία του πανελ
		JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				new JScrollPane(tblInvoices),
				new JScrollPane(tblItems));
		sp.setDividerSize(3);
		sp.setDividerLocation(75);
		// Hack: Αν αφαιρεθεί το pnl η στοίχιση του sp χαλάει. Γιατί; Μην πειράζεις ότι δεν καταλαβαίνεις!
		Box pnl = Box.createHorizontalBox();
		pnl.add(sp);
		Box pnlInvoices = Box.createVerticalBox();
		pnlInvoices.add(pnl);
		// Αν ο JTable δεν είναι μέσα σε JScrollPane, ο header του πρέπει να προστεθεί manual
		pnlInvoices.add(tblReport.getTableHeader());
		pnlInvoices.add(tblReport);
		return pnlInvoices;
	}

	/** Δημιουργεί ένα panel με τα στοιχεία της δαπάνης.
	 * @param unitHeader Οι επικεφαλίδες του πίνακα της καρτέλας «Στοιχεία Μονάδας»
	 * @param unitEditors Οι επεξεργαστές του πίνακα της καρτέλας «Στοιχεία Μονάδας»
	 * @param cbBoolean Ο επιλογέας Ναι/Όχι
	 * @param cbBoolean Ο επιλογέας προσωπικού
	 * @return Το panel με τα στοιχεία της δαπάνης */
	private JScrollPane createPanelExpenditure(String[] unitHeader, Component[] unitEditors,
			JComboBox cbBoolean, JComboBox cbPersonnel) {
		// Επικεφαλίδα του πίνακα στοιχείων δαπάνης
		String[] expHeader = Stream.concat(Stream.of(new String[] {
			"<html><b>Στοιχεία Δαπάνης", Expenditure.H[0],
			"<html>" + Expenditure.H[1] + " <font size=2><i>(Πίστωση ΓΕΣ/Γ2)",
			"Ειδικός Φορέας (ΕΦ)", "Αναλυτικός Λογαριασμός Εσόδων/Εξόδων (ΑΛΕ)", Expenditure.H[4],
			"<html>" + Expenditure.H[5] + " <font size=2><i>(αιτιατική)", Expenditure.H[6],
			"<html><b>Αυτοματισμοί", Expenditure.H[7],
			"<html><b>Στοιχεία Επιτροπών Έργων", Expenditure.H[8], Expenditure.H[9],
			Expenditure.H[10], Expenditure.H[11], Expenditure.H[12], Expenditure.H[13]
		}), Stream.of(unitHeader)).toArray(String[]::new);
		// Οι επεξεργαστές για τα πεδία του πίνακα με τα στοιχεία της δαπάνης.
		// Πρέπει να είναι τύπου Component γιατί τα null στοιχεία, αντικαθίστανται με JTextField.
		Component[] expEditors = Stream.concat(Stream.of(new Component[] {
			null, null, cbBoolean, null, null, new JComboBox(Financing.values()), null, null,
			null, cbBoolean,
			null, cbPersonnel, cbPersonnel, cbPersonnel, cbPersonnel, cbPersonnel, cbPersonnel
		}), Stream.of(unitEditors)).toArray(Component[]::new);
		// Ρύθμιση του πίνακα με τα στοιχεία δαπάνης
		rtmExpenditure = new PropertiesTableModel(expHeader, 1) {
			@Override public TableRecord get(int index) { return data.expenditure; }
			@Override public boolean isCellEditable(int row, int col) {
				return super.isCellEditable(row, col) && row != 0 && row != 8 && row != 10
						&& row - 17 != 0 && row - 17 != 15;
			}
		};
		// Ανανέωση πινάκων που τροποποιούνται λόγω αυτοματισμών
		rtmExpenditure.addTableModelListener(e -> {
			switch(e.getFirstRow()) {
				case 9: window.rtmContracts.fireTableDataChanged();		// Αυτοματισμοί - no break
				case 2: window.rtmContents.fireTableDataChanged(); break;	// Έργο
			}
		});
		return new JScrollPane(createTable(rtmExpenditure, expEditors));
	}

	/** Δημιουργεί το panel με τον πίνακα των συμβάσεων.
	 * @param cbContractors Επιλογέας οικονομικών φορέων.
	 * @return Το panel με τον πίνακα συμβάσεων */
	private JSplitPane createPanelContracts(JComboBox cbContractors) {
		// Ο πάροχος δεδομένων για τον πίνακα με τις ταυτότητες των συμβάσεων
		rtmContracts = new ResizableHeaderTableModel<Contract>(Arrays.copyOf(Contract.H, 1)) {
			@Override protected List<Contract> get() { return data.expenditure == null ? null : data.expenditure.contracts; }
			@Override protected Contract createNew() { return new Contract(data.expenditure); }
			@Override public boolean isCellEditable(int row, int col) { return false; }
			@Override protected void remove(int index) {
				// Διαγράφονται μόνο οι συμβάσεις που δε χρησιμοποιούνται από κανένα τιμολόγιο
				List<Contract> list = get();
				Contract contract = list.get(index);
				if (data.expenditure.invoices.stream().noneMatch(i -> i.getContract() == contract))
					list.remove(index);
				else showMessageDialog(window, "Η σύμβαση δε μπορεί να διαγραφεί όσο χρησιμοποιείται από τιμολόγια.",
							"Αποτυχία διαγραφής της σύμβασης", ERROR_MESSAGE);
			}
		};
		// Ο πίνακας με τις ταυτότητες των συμβάσεων
		JTable tblContracts = createTable(rtmContracts, true, true );
		// Επιλογέας διαγωνισμού
		JComboBox cbTenders = new JComboBox(new ComboPlusOneDataModel() {
			@Override protected List get() {
				return data.isEmpty() ? new ArrayList() : data.expenditure.tenders;
			}
		});
		cbTenders.setBorder(cbContractors.getBorder());
		// Ο πίνακας με τα στοιχεία της σύμβασης
		String[] h = {
			Contract.H[0], "<html>Τίτλος Σύμβασης <font size=2><i>(αιτιατική)", Contract.H[2],
			"<html><b>Απευθείας Ανάθεση", Contract.H[3], Contract.H[4], Contract.H[5], Contract.H[6],
			"<html><b>Διαγωνισμός", Contract.H[7]
		};
		ResizablePropertiesTableModel<Contract> ptmContractInfo = new ResizablePropertiesTableModel<Contract>(h) {
			@Override protected List<Contract> get() { return data.expenditure == null ? null : data.expenditure.contracts; }
			@Override protected Contract createNew() { return new Contract(data.expenditure); }
			@Override public boolean isCellEditable(int row, int col) {
				return super.isCellEditable(row, col)
						&& row != 3 && row != 8 && (!data.expenditure.isSmart() || row != 2);
			}
		};
		// Αλλαγή στο διαγωνισμό της σύμβασης τροποποιεί τον πίνακα αθροισμάτων τιμολογίων
		ptmContractInfo.addTableModelListener(l -> {
			switch(l.getFirstRow()) {
				case 9: window.rtmReport.fireTableDataChanged(new TableModelEvent(window.rtmReport, 0, 6, 3));	// no break
				case 2: window.rtmCompetitors.fireTableDataChanged(); break;
			}
		});
		JTable tblInfo = PropertiesTableModel.createTable(ptmContractInfo, new Component[] {
			null, null, cbContractors,
			null, null, null, cbContractors, cbContractors,
			null, cbTenders
		});
		// Αλλαγές στη λίστα με τις ταυτότητες των συμβάσεων από τον πίνακα στοιχείων της σύμβασης
		ptmContractInfo.addSelectorTableModelListener(tblContracts);
		// Όταν επιλέγουμε άλλη σύμβαση στον πίνακα με τις ταυτότητες των συμβάσεων
		tblContracts.getSelectionModel().addListSelectionListener(e -> {
			if (e.getValueIsAdjusting()) return;
			// Αν υπάρχει κελί υπό επεξεργασία στον πίνακα με τα στοιχεία της σύμβασης,
			// η επεξεργασία πρέπει να σταματήσει
			TableCellEditor tce = tblInfo.getCellEditor();
			if (tce != null) tce.stopCellEditing();
			// Ο πίνακας με τα στοιχεία της σύμβασης,
			// ενημερώνεται με τα στοιχεία της επιλεγμένης σύμβασης
			ptmContractInfo.setIndex(((ListSelectionModel) e.getSource()).getMinSelectionIndex());
		});

		// Η τελική διαμόρφωση της καρτέλας
		JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				new JScrollPane(tblContracts),
				new JScrollPane(tblInfo));
		sp.setDividerSize(3);
		sp.setDividerLocation(100);
		return sp;
	}

	/** Δημιουργεί ένα panel με τους πίνακες των διαγωνισμών.
	 * @param cbBoolean Ο επιλογέας Ναι/Όχι
	 * @param cbContractors Επιλογέας οικονομικών φορέων.
	 * @return Το panel με τους πίνακες διαγωνισμών */
	private Box createPanelTenders(JComboBox cbBoolean, JComboBox cbContractors) {
		// Επιλογέας διαγωνισμού
		cdmTenders = new ComboDataModel<Tender>() {
			@Override protected List get() {
				return data.isEmpty() ? new ArrayList() : data.expenditure.tenders;
			}
		};
		JComboBox cbTenders = new JComboBox(cdmTenders);
		// Πλήκτρο νέου διαγωνισμού
		JButton btnNew = new JButton("Νέος διαγωνισμός", ICON_NEW);
		btnNew.addActionListener(e -> {
			Tender tender = new Tender(data.expenditure);
			data.expenditure.tenders.add(tender);
			window.cdmTenders.setSelectedItem(tender);
		});
		// Πλήκτρο διαγραφής διαγωνισμού
		JButton btnDelete = new JButton("Διαγραφή διαγωνισμού", ICON_DELETE);
		btnDelete.addActionListener(e -> {
			Tender tender = window.cdmTenders.getSelectedItem();
			if (tender != null) {
				if (data.expenditure.contracts.stream().anyMatch(i -> i.getTender() == tender))
					showMessageDialog(window, "Ο διαγωνισμός δε μπορεί να διαγραφεί όσο χρησιμοποιείται από συμβάσεις.",
							"Αποτυχία διαγραφής του διαγωνισμού", ERROR_MESSAGE);
				else if (OK_OPTION == showConfirmDialog(window,
						"Είστε σίγουροι ότι θέλετε να διαγράψετε τον τρέχοντα διαγωνισμό;",
						"Διαγραφή τρέχοντος διαγωνισμού", OK_CANCEL_OPTION, WARNING_MESSAGE)) {
					List<Tender> tenders = data.expenditure.tenders;
					tenders.remove(tender);
					data.expenditure.calcContents();
					window.rtmContents.fireTableDataChanged();
					window.cdmTenders.setSelectedItem(tenders.isEmpty() ? null : tenders.get(0));
				}
			}
		});
		cbTenders.setMaximumSize(new Dimension(300, 25));	// Hack
		// Δημιουργία οριζόντιας μπάρας επιλογέα διαγωνισμού
		Box hBox = Box.createHorizontalBox();
		hBox.add(new JLabel("Τρέχον διαγωνισμός:"));
		hBox.add(cbTenders);
		hBox.add(btnNew);
		hBox.add(btnDelete);

		// Ο πίνακας με τα στοιχεία του διαγωνισμού
		TenderInfoTableModel ptmInfo = new TenderInfoTableModel();
		Component[] cmp = {
			null, null, null, null, null, null, null, cbBoolean, null, null, null,
			null, null, null, cbBoolean, null
		};
		JTable tblInfo = PropertiesTableModel.createTable(ptmInfo, cmp);
		// Όταν τροποποιούνται διάφορα στοιχεία του διαγωνισμού
		ptmInfo.addTableModelListener(e -> {
			switch(e.getFirstRow()) {
				case 1: window.rtmContents.fireTableDataChanged(); break;	// Τύπος Διαγωνισμού
				case 2: cbTenders.repaint(); break;							// Όνομα διακήρυξης
			}
		});

		// Ο πίνακας με τους διαγωνιζόμενους
		rtmCompetitors = new CompetitorTableModel();
		JTable tblCompetitors = createTable(rtmCompetitors, true, false);
		tblCompetitors.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(cbContractors));

		// Όταν επιλέγουμε άλλο διαγωνισμό στον επιλογέα διαγωνισμών
		cdmTenders.addListDataListener(new ListDataListener() {
			@Override public void intervalAdded(ListDataEvent e) { contentsChanged(e); }
			@Override public void intervalRemoved(ListDataEvent e) { contentsChanged(e); }
			@Override public void contentsChanged(ListDataEvent e) {
				Tender tender = ((ComboDataModel<Tender>) e.getSource()).getSelectedItem();
				ptmInfo.setSelectedTender(tender);
				rtmCompetitors.setSelectedTender(tender);	// Οχι window. γιατί τρέχει πιο πριν!
			}
		});

		// Η τελική διαμόρφωση της καρτέλας
		Box vBoxI = Box.createVerticalBox();
		vBoxI.add(tblInfo);
		vBoxI.add(Box.createVerticalStrut(3));
		vBoxI.add(tblCompetitors.getTableHeader());
		vBoxI.add(tblCompetitors);
		Box vBox = Box.createVerticalBox();
		vBox.add(hBox);
		vBox.add(new JScrollPane(vBoxI));
		return vBox;
	}

	/** Δημιουργεί ένα panel με τον πίνακα των δικαιούχων.
	 * @param border Το περίγραμμα των επιλογέων που επεξεργάζονται τα πεδία στον πίνακα
	 * @return Το panel με τους πίνακες δικαιούχων */
	private JSplitPane createPanelContractors(Border border) {
		// Ο πάροχος δεδομένων για τον πίνακα με τις επωνυμίες των δικαιούχων
		rtmContractors = new ResizableHeaderTableModel<Contractor>(Arrays.copyOf(Contractor.H, 1)) {
			@Override protected List<Contractor> get() { return data.contractors; }
			@Override protected Contractor createNew() { return new Contractor(); }
			@Override public boolean isCellEditable(int row, int col) { return false; }
		};
		// Ο πίνακας με τις επωνυμίες των δικαιούχων
		JTable tblContractors = createTable(rtmContractors, true, true );

		// Ο επιλογέας τύπου του δικαιούχου
		JComboBox type = new JComboBox(Contractor.Type.values());
		type.setBorder(border);
		// Ο πίνακας με τα στοιχεία του δικαιούχου
		String[] h = {
			Contractor.H[0], Contractor.H[1], Contractor.H[2], Contractor.H[3], "Έδρα, Διεύθυνση, Τ.Κ.",
			"<html><b>Στοιχεία Υπεύθυνης Δήλωσης", Contractor.H[5], PersonInfo.H[0], PersonInfo.H[1],
			PersonInfo.H[2], PersonInfo.H[3], PersonInfo.H[4], PersonInfo.H[5], PersonInfo.H[6],
			PersonInfo.H[7] + " (Έδρα, Διεύθυνση, ΤΚ)", PersonInfo.H[8]
		};
		ResizablePropertiesTableModel<Contractor> ptmInfo = new ResizablePropertiesTableModel<Contractor>(h) {
			@Override protected List get() { return data.contractors; }
			@Override protected Contractor createNew() { return new Contractor(); }
			@Override public boolean isCellEditable(int row, int col) {
				return super.isCellEditable(row, col) && row != 5;
			}
		};
		JTable tblInfo = PropertiesTableModel.createTable(ptmInfo, new Component[] { null, type, null });

		// Αλλαγές στη λίστα δικαιούχων από τον πίνακα στοιχείων δικαιούχου
		ptmInfo.addSelectorTableModelListener(tblContractors);
		// Όταν επιλέγουμε άλλο δικαιούχο στον πίνακα δικαιούχων
		tblContractors.getSelectionModel().addListSelectionListener(e -> {
			if (e.getValueIsAdjusting()) return;
			// Αν υπάρχει κελί υπό επεξεργασία στον πίνακα με τα στοιχεία του δικαιούχου,
			// η επεξεργασία πρέπει να σταματήσει
			TableCellEditor tce = tblInfo.getCellEditor();
			if (tce != null) tce.stopCellEditing();
			// Ο πίνακας με τα στοιχεία του δικαιούχου,
			// ενημερώνεται με τα στοιχεία του επιλεγμένου δικαιούχου
			ptmInfo.setIndex(((ListSelectionModel) e.getSource()).getMinSelectionIndex());
		});

		// Η τελική διαμόρφωση της καρτέλας
		JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				new JScrollPane(tblContractors),
				new JScrollPane(tblInfo));
		sp.setDividerSize(3);
		sp.setDividerLocation(200);
		return sp;
	}

	/** Δημιουργεί ένα panel με τον πίνακα των κρατήσεων. */
	private JScrollPane createPanelDeductions() {
		// Το μοντέλο του πίνακα κρατήσεων
		rtmDeductions = new ResizableTableModel<Deduction>() {
			@Override protected List<Deduction> get() { return data.deductions; }
			@Override protected Deduction createNew() { return new Deduction(); }
			@Override public int getColumnCount() { return Deduction.TABLE_HEADER.size(); }
			@Override public String getColumnName(int index) { return Deduction.TABLE_HEADER.get(index); }
			// Η πρώτη στήλη του πίνακα, που έχει το σύνολο, δεν πρέπει να είναι επεξεργάσιμη
			@Override public boolean isCellEditable(int row, int col) { return col != 0; }
			@Override public void fireTableDataChanged(TableModelEvent e) {
				super.fireTableDataChanged(e);
				// Κάθε UPDATE σε μεμονωμένο κελί, πλην πρώτης στήλης, ανανεώνει το σύνολο κρατήσεων στο
				// αντίστοιχο κελί της πρώτης στήλης
				if (e.getType() == UPDATE && e.getColumn() > 0 && e.getFirstRow() == e.getLastRow())
					super.fireTableDataChanged(new TableModelEvent(this, e.getFirstRow(), e.getLastRow(), 0));
			}
		};
		JTable t = createTable(rtmDeductions, false, true);
		// Προσθήκη popup menu στην επικεφαλίδα του πίνακα, για προσθήκη νέας επιμέρους κράτησης
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(createMenuItem("Προσθήκη στήλης", e -> {
			String head = showInputDialog(window,			// με το window.* γλιτώνουμε το capture
					"Δώστε της επικεφαλίδα της νέας στήλης.", "Εισαγωγή στήλης", QUESTION_MESSAGE);
			if (head != null && !head.isEmpty()) {
				if (Deduction.TABLE_HEADER.contains(head))
					showMessageDialog(window,				// με το window.* γλιτώνουμε το capture
							"Υπάρχει ήδη στήλη με αυτό το όνομα.", "Εισαγωγή στήλης", ERROR_MESSAGE);
				else {
					Deduction.TABLE_HEADER.add(head);
					// Hack: Με το δεξί κλίκ στην επικεφαλίδα του πίνακα, προκειμένου να εμφανιστεί
					// το μενού για προσθήκη νέας επιμέρους κράτησης, το JTable θεωρεί ότι η στήλη
					// βρίσκεται σε κατάσταση drag & drop και κρατάει μια αναφορά στη συγκεκριμένη
					// στήλη. Όμως εμείς επειδή με τη μεθεπόμενη εντολή λέμε να ξανασχεδιάσει
					// εξολοκλήρου τον πίνακα, όλες οι στήλες διαγράφονται και επαναδημιουργούνται
					// με αποτέλεσμα, όταν μετά το gui renderer thread θα ψάξει να βρει τη στήλη θα
					// μας δώσει exception
					t.getTableHeader().setDraggedColumn(null);
					window.rtmDeductions.fireTableChanged();
				}
			}
		}));
		t.getTableHeader().setComponentPopupMenu(popupMenu);
		return new JScrollPane(t);
	}

	/** Δημιουργεί το panel με τον πίνακα του προσωπικού της Μονάδας / Υπηρεσίας. */
	private JScrollPane createPanelPersonnel() {
		// Το μοντέλο του πίνακα προσωπικού
		rtmPersonnel = new ResizableHeaderTableModel<Person>(Person.H) {
			@Override protected List<Person> get() { return data.personnel; }
			@Override protected Person createNew() { return new Person(); }
		};
		return new JScrollPane(createTable(rtmPersonnel, true, true));
	}

	/** Δημιουργεί το panel με τον πίνακα του φύλλου καταχώρησης της δαπάνης. */
	private JScrollPane createPanelContents() {
		rtmContents = new ContentsTableModel();
		JTable t = createTable(rtmContents, true, false);
		t.getColumnModel().getColumn(1).setCellEditor(new ContentsTableCellEditor(rtmContents));
		return new JScrollPane(t);
	}

	/** Ενεργοποιεί και απενεργοποιεί τις καρτέλες δαπανών στο παράθυρο του προγράμματος.
	 * Αν δεν υπάρχει καμία ανοικτή δαπάνη απενεργοποιεί τις καρτέλες δαπανών. */
	void updatePanels() {
		boolean has = !data.isEmpty();
		boolean con = has && data.expenditure.isConstruction();
		JTabbedPane j = (JTabbedPane) getContentPane().getComponent(0);
		j.setEnabledAt(0, has);	// Καρτέλα "Δαπάνη"
		j.setEnabledAt(1, has);	// Καρτέλα "Τιμολόγια"
		j.setEnabledAt(2, has);	// Καρτέλα "Συμβάσεις"
		j.setEnabledAt(3, has);	// Καρτέλα "Διαγωνισμοί"
		j.setEnabledAt(4, has);	// Καρτέλα "Φύλλο Καταχώρησης"
		j.setEnabledAt(5, con);	// Καρτέλα "Εργασίες"
		// Ανανέωση πινάκων οι οποίοι ενδεχομένως να έχουν αλλάξει μέγεθος
		if (has) {
			List<Tender> tenders = data.expenditure.tenders;
			cdmTenders.setSelectedItem(tenders.isEmpty() ? null : tenders.get(0));
			((ResizableTableModel) tblInvoices.getModel()).fireTableDataChanged();
			rtmWorks.fireTableDataChanged();
			rtmContents.fireTableDataChanged();
			rtmContracts.fireTableDataChanged();
			rtmReport.fireTableDataChanged(new TableModelEvent(rtmReport, 0, 6, 3));
			rtmExpenditure.fireTableDataChanged(new TableModelEvent(
					rtmExpenditure, 0, rtmExpenditure.getRowCount() - 1, 1));
		} else if (j.getSelectedIndex() < 6) j.setSelectedIndex(6);
	}

	// ================================================ ΓΡΑΦΙΚΕΣ ΛΕΙΤΟΥΡΓΙΕΣ ===

	/** Που πρέπει να τοποθετηθεί ένα παράθυρο για να είναι κεντραρισμένο στην οθόνη.
	 * @param width Το πλάτος του παραθύρου
	 * @param height Το ύψος του παραθύρου
	 * @return Η θέση του παραθύρου */
	static Point getLocationScreenCentered(int width, int height) {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		return new Point((screen.width - width) / 2, (screen.height - height) / 2);
	}

	/** Φορτώνει ένα εικονίδιο.
	 * Χρησιμοποιείται για να φορτώνει εικονίδια για τα μενού. Τα εικονίδια είναι 16 x 16 PNG.
	 * @param name Ένα φιλικό όνομα, το οποίο παραπέμπει σε αρχείο εικόνας, εντός του πακέτου του
	 * προγράμματος. Αν είναι null, επιστρέφει ένα εικονίδιο 16 x 16, 100% διαφανές.
	 * @return Το εικονίδιο */
	static private ImageIcon loadIcon(String name) {
		//if (name == null) return new MenuBlankIcon(); else
		return new ImageIcon(getSystemResource("expenditure/" + name + ".png"));
	}

	/** Θέτει το κέλυφος του προγράμματος (Look & Feel).
	 * Επιλέγει το κέλυφος από τις ρυθμίσεις. Αν αποτύχει επιλέγει το κέλυφος από το λειτουργικό
	 * σύστημα. Αν αποτύχει, το αφήνει στο JVM. */
	static private void setSkin() {
		try { setLookAndFeel(data.skin); }
		catch(RuntimeException | ReflectiveOperationException | UnsupportedLookAndFeelException e) {
			try { setLookAndFeel(getSystemLookAndFeelClassName()); }
			catch(ReflectiveOperationException | UnsupportedLookAndFeelException ex) {}
		}
	}

	// =============================================================== ΜΕΝΟΥ ===

	/** Δημιουργεί ένα μενού.
	 * @param name Το όνομα του μενού, όπως θα εμφανίζεται στη γραμμή μενού του προγράμματος
	 * @param children Οι επιλογές του μενου
	 * @return Το μενού */
	static private JMenu createMenu(String name, JMenuItem[] children) {
		JMenu m = new JMenu(name);
		for (JMenuItem i : children)
			if (i == null) m.addSeparator();
			else m.add(i);
		return m;
	}

	/** Δημιουργεί μια επιλογή μενού.
	 * @param name Το όνομα της επιλογής μενού, όπως θα εμφανίζεται στη γραμμή μενού του προγράμματος
	 * @param action Ένας listener που θα εκτελείται όταν επιλεγεί η επιλογή από το μενου
	 * @return Η επιλογή του μενού */
	static private JMenuItem createMenuItem(String name, ActionListener action) {
		JMenuItem i = new JMenuItem(name);
		i.addActionListener(action);
		return i;
	}

	/** Δημιουργεί μια επιλογή μενού.
	 * @param name Το όνομα της επιλογής μενού, όπως θα εμφανίζεται στη γραμμή μενού του προγράμματος
	 * @param icon Ένα εικονίδιο για την επιλογή μενού ή null
	 * @param action Ένας listener που θα εκτελείται όταν επιλεγεί η επιλογή από το μενου
	 * @return Η επιλογή του μενού */
	static public JMenuItem createMenuItem(String name, String icon, ActionListener action) {
		JMenuItem i = new JMenuItem(name, loadIcon(icon));
		i.addActionListener(action);
		return i;
	}

	/** Δημιουργεί μια επιλογή μενού.
	 * @param name Το όνομα της επιλογής μενού, όπως θα εμφανίζεται στη γραμμή μενού του προγράμματος
	 * @param icon Ένα εικονίδιο για την επιλογή μενού ή null
	 * @param action Ένας listener που θα εκτελείται όταν επιλεγεί η επιλογή από το μενου
	 * @return Η επιλογή του μενού */
	static public JMenuItem createMenuItem(String name, Icon icon, ActionListener action) {
		JMenuItem i = new JMenuItem(name, icon);
		i.addActionListener(action);
		return i;
	}

	/** Δημιουργεί μια επιλογή μενού On/Off.
	 * @param name Το όνομα της επιλογής μενού, όπως θα εμφανίζεται στη γραμμή μενού του προγράμματος
	 * @param icon Ένα εικονίδιο για την επιλογή μενού ή null
	 * @param check Η κατάσταση τηςεπιλογής θα είναι ενεργή
	 * @param action Ένας listener που θα εκτελείται όταν επιλεγεί η επιλογή από το μενου
	 * @return Η επιλογή του μενού */
	static private JCheckBoxMenuItem createMenuItem(String name, String icon, boolean check,
			ActionListener action) {
		JCheckBoxMenuItem i = new JCheckBoxMenuItem(name, loadIcon(icon), check);
		i.addActionListener(action);
		return i;
	}

	/** Δημιουργεί ένα μενού, με επιλογές όλα τα διαθέσιμα κελύφη για το παράθυρο.
	 * @param name Το όνομα της επιλογής μενού, όπως θα εμφανίζεται στη γραμμή μενού του προγράμματος
	 * @param icon Ένα εικονίδιο για την επιλογή μενού ή null
	 * @return Το μενού */
	private JMenu createMenuWithSkins(String name, String icon) {
		JMenu skins = new JMenu(name);
		skins.setIcon(loadIcon(icon));
		LookAndFeelInfo[] lafs = getInstalledLookAndFeels();
		String s = data.skin;
		if (s == null) s = getSystemLookAndFeelClassName();
		ButtonGroup btg = new ButtonGroup();
		for (LookAndFeelInfo laf : lafs) {
			String className = laf.getClassName();
			JRadioButtonMenuItem i = new JRadioButtonMenuItem(laf.getName(), className.equals(s));
			i.addActionListener(e -> {
				data.skin = className;
				setSkin(); dispose();
				window = new MainFrame();
				// Όχι μέσα στη MainFrame γιατί θα υπάρξουν αναφορές στο window πριν αυτό τεθεί
				window.setVisible(true);

			});
			btg.add(i);
			skins.add(i);
		}
		return skins;
	}

	/** Δημιουργεί μια γραμμή μενού.
	 * @param children Οι επιλογές της γραμμής μενου
	 * @return Η γραμμή μενού */
	static private JMenuBar createMenuBar(JMenuItem[] children) {
		JMenuBar m = new JMenuBar();
		for (JMenuItem i : children) m.add(i);
		return m;
	}

	/** Δημιουργεί τη γραμμή μενού του παραθύρου του προγράμματος.
	 * Προσοχή! Μετά από οποιαδήποτε αλλαγή στις επιλογές της γραμμής μενού, πρέπει να ελεγχθεί και
	 * η κλήση updateMenus() όπου γίνεται αναφορά σε κάποιες από τις επιλογές με index (οπότε αλλαγή
	 * σειράς στις επιλογές, προσθήκες ή διαγραφές, μπορεί να της δημιουργήσουν πρόβλημα).
	 * @return Η γραμμή μενού */
	private JMenuBar createMenuMain() {
		return createMenuBar(new JMenuItem[] {
			createMenu("Αρχείο", new JMenuItem[] {
				createMenuItem("Νέα Δαπάνη", ICON_NEW, e -> expenditureNew()),
				createMenuItem("Άνοιγμα Δαπάνης...", "open", e -> expenditureOpen()),
				createMenuItem("Αποθήκευση Δαπάνης...", "save", e -> expenditureSaveCurrent()),
				createMenuItem("Εισαγωγή στοιχείων...", "import", e -> importData()),
				createMenuItem("Κλείσιμο Δαπάνης", "close", e -> expenditureCloseActive()),
				null,
				createMenuItem("Έξοδος", "exit", e ->
						dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)))
			}),
			createMenu("Εξαγωγή", new JMenuItem[] {
				createMenuItem("Δαπάνη", e -> {
					TreeMap<String, String> env = new TreeMap<>();
					if (data.onlyOnce) env.put("one", "true");
					exportReport("Δαπάνη.php", env);
				}),
				createMenu("Αλληλογραφία", new JMenuItem[] {
					createMenuItem("Συγκρότηση Επιτροπών", e ->
							showDraftDialogExport("Δγη Συγκρότησης Επιτροπών.php",
									data.isEmpty() ? data.unitInfo : data.expenditure)),
					createMenuItem("Πρόσκληση Υποβολής Προσφορών", e ->
							showDraftDialogExport("Πρόσκληση Υποβολής Προσφορών.php")),
					createMenuItem("Απόφαση Απευθείας Ανάθεσης", e ->
							showDraftDialogExport("Απόφαση Απευθείας Ανάθεσης.php")),
					createMenuItem("Διαβιβαστικό Δαπάνης", e ->
							showDraftDialogExport("Διαβιβαστικό Δαπάνης.php")),
					/*createMenuItem("Εκθεση Απατούμενης Δαπάνης", (ActionEvent e) ->
							showDraftDialogExport("Έκθεση Απαιτούμενης Δαπάνης.php"))*/
				}),
				createMenuItem("Σύμβαση", e -> exportReport("export_contracts")),
				createMenu("Διαγωνισμοί", new JMenuItem[] {
					createMenuItem("Διακήρυξη", e ->
						showDraftDialogExport("Διακήρυξη Διαγωνισμού.php")),
					createMenu("Πρακτικά Αποσφράγισης", new JMenuItem[] {
						createMenuItem("Τεχνικών Προσφορών", e ->
							exportReport(data.expenditure, "technical_offer_unseal_reports")),
						createMenuItem("Τεχνικών & Οικονομικών Προσφορών", e ->
							exportReport(data.expenditure, "offer_unseal_reports")),
					}),
					createMenuItem("Ανάδειξη Προσωρινού Αναδόχου", e ->
						showDraftDialogExport("Απόφαση Ανάδειξης Προσωρινού Αναδόχου.php")),
					createMenuItem("Πρακτικό Ελέγχου Δικαιολογητικών Κατακύρωσης", e ->
						exportReport("Πρακτικό Ελέγχου Δικαιολογητικών Κατακύρωσης.php", null)),
					createMenuItem("Κατακύρωση", e ->
						showDraftDialogExport("Κατακύρωση Διαγωνισμού.php"))
				}),
				createMenu("Υπεύθυνες Δηλώσεις", new JMenuItem[] {
					createMenuItem("Γνωστοποίηση τραπεζικού λογαριασμού", e -> statement("statement_IBAN")),
					createMenuItem("Μη χρησιμοποίηση στρατιωτικού ως αντιπρόσωπου", e ->
							statement("statement_representative")),
					createMenuItem("Μη συνδρομή λόγων αποκλεισμού", e ->
							statement("statement_disqualification")),
					null,
					createMenuItem("Κενή", e -> saveScriptOutput(
							"<?php require_once('statement.php'); init(1); statement(null); ?>\n\n}")),
				}),
				createMenu("ΦΕ", new JMenuItem[] {
					createMenuItem("Εφορία", e -> exportReport("ΦΕ για την Εφορία.php", null)),
					createMenuItem("Δικαιούχος", e -> exportReport("ΦΕ για τον Δικαιούχο.php", null))
				}),
				createMenu("Διάφορα", new JMenuItem[] {
					createMenuItem("Κατάσταση Πληρωμής", e ->
							exportReport("Κατάσταση Πληρωμής.php", null)),
				/*	createMenuItem("Πρόχειρη Λίστα Τιμολογίων", (ActionEvent e) ->
							exportReport("Πρόχειρη Λίστα Τιμολογίων.php", null)),
					createMenuItem("Απόδειξη για Προκαταβολή",
							(ActionEvent e) -> exportReport("Απόδειξη για Προκαταβολή.php", null))*/
				})
			}),
			createMenu("Εργαλεία", new JMenuItem[] {
				createMenuItem("Οδηγός Τιμολογίου", "wizard",
						e -> new InvoiceWizardDialog(this).setVisible(true)),
				createMenuItem("Πληροφορίες IBAN", "bank", e -> iban()),
				createMenuItem("Υπεύθυνη Δήλωση", "statement",
						e -> new StatementDialog(this).setVisible(true))
			}),
			createMenu("Ρυθμίσεις", new JMenuItem[] {
				createMenuWithSkins("Κέλυφος", "skins"),
				createMenuItem("Ένα Αντίγραφο", "only_one", data.onlyOnce,
						e -> data.onlyOnce = !data.onlyOnce),
			}),
			createMenu("Δαπάνες", new JMenuItem[] {}),
			createMenu("Βοήθεια", new JMenuItem[] {
				createMenuItem("Εγχειρίδιο", "help", e -> help()),
				createMenuItem("Περί...", "about", e -> about())
			}),
		});
	}

	/** Ενημερώνει τη γραμμή μενού του προγράμματος.
	 * Αν δεν υπάρχουν ανοικτές δαπάνες, απενεργοποιεί ορισμένα μενού. Επιπλέον ενημερώνει το μενού
	 * Δαπάνες, για το ποιές δαπάνες είναι ανοικτές. */
	private void updateMenus() {
		boolean has = !data.isEmpty();				// Το πρόγραμμα έχει ανοικτές δαπάνες
		JMenu file = getJMenuBar().getMenu(0);		// μενού Αρχείο
		file.getItem(2).setEnabled(has);			// μενού Αποθήκευση Δαπάνης
		file.getItem(4).setEnabled(has);			// μενού Κλείσιμο Δαπάνης
		JMenu export = getJMenuBar().getMenu(1);	// μενού Εξαγωγή
		export.getItem(0).setEnabled(has);			// μενού Εξαγωγή/Δαπάνη
		JMenu doc = (JMenu) export.getItem(1);		// μενού Εξαγωγή/Αλληλογραφία
		doc.getItem(1).setEnabled(has);				// μενού Εξαγωγή/Αλληλογραφία/Απόφαση Απευθείας Ανάθεσης
		doc.getItem(2).setEnabled(has);				// μενού Εξαγωγή/Αλληλογραφία/Απόφαση Απευθείας Ανάθεσης
		doc.getItem(3).setEnabled(has);				// μενού Εξαγωγή/Αλληλογραφία/Διαβιβαστικό Δαπάνης
		export.getItem(2).setEnabled(has);			// μενού Εξαγωγή/Σύμβαση
		export.getItem(3).setEnabled(has);			// μενού Εξαγωγή/Διαγωνισμοί
		JMenu statement = (JMenu) export.getItem(4);// μενού Εξαγωγή/Υπεύθυνες Δηλώσεις
		statement.getItem(2).setEnabled(has);		// μενού Εξαγωγή/Υπεύθυνες Δηλώσεις/Μη συνδρομή λόγων αποκλεισμού
		export.getItem(5).setEnabled(has);			// μενού Εξαγωγή/ΦΕ
		export.getItem(6).setEnabled(has);			// μενού Εξαγωγή/Διάφορα
		JMenu expenditures = getJMenuBar().getMenu(4);	// μενού Δαπάνες
		expenditures.setEnabled(has);
		// Ξαναδημιουργείται το μενού με όλες τις ανοικτές δαπάνες σαν επιλογές του
		if (has) {
			expenditures.removeAll();
			ButtonGroup btg = new ButtonGroup();
			data.expenditures.forEach(expenditure -> {
				boolean active = expenditure == data.expenditure;
				String f = expenditure.file.getName();
				if (f.endsWith(".δαπάνη")) f = f.substring(0, f.length() - 7);
				JRadioButtonMenuItem jmi = new JRadioButtonMenuItem(f, active);
				jmi.addActionListener(e -> {
					data.expenditure = expenditure;
					window.updatePanels();					// με το window.* γλιτώνουμε το capture
				});
				btg.add(jmi); expenditures.add(jmi);
			});
		}
	}

	// ============================================ ΕΝΕΡΓΕΙΕΣ ΕΠΙΛΟΓΩΝ ΜΕΝΟΥ ===

	/** Εμφάνιση διαλόγου με πληροφορίες για το πρόγραμμα και τον προγραμματιστή. */
	private void about() {
		showMessageDialog(this,
				"<html><center><b><font size=4>Στρατιωτικές Δαπάνες</font><br>" +
				"<font size=3>Έκδοση " + VERSION + "</font></b></center><br>" +
				"Προγραμματισμός: <b>Γκέσος Παύλος (ΣΣΕ 2002)</b><br>" +
				"Άδεια χρήσης: <b>BSD</b><br>" +
				"Σελίδα λήψης: <b>http://ha-expenditure.sourceforge.net/</b><br>" +
				"Σελίδα υποστήριξης: <b>https://www.facebook.com/haexpenditure/</b><br>" +
				"Ηλεκτρονικό ταχυδρομείο: <b>gessos.paul@gmail.com</b><br><br>" +
				"Στείλτε τυχόν σφάλματα ή παραλείψεις του προγράμματος<br>προκειμένου να διορθώνονται.<br><br>" +
				"Η πρώτη έκδοση του προγράμματος ήταν το 2004!",
				getTitle(), PLAIN_MESSAGE);
	}

	/** Άνοιγμα της τεκμηρίωσης του προγράμματος, στο πρόγραμμα πλοήγησης. */
	private void help() {
		try { Desktop.getDesktop().open(new File(ROOT_PATH + "help/index.html")); }
		catch(RuntimeException | IOException ex) {
			showExceptionMessage(this, ex, "Πρόβλημα στην εκκίνηση του browser", null);
		}
	}

	/** Δημιουργεί μια νέα δαπάνη και την κάνει τρέχουσα στο παράθυρο του προγράμματος. */
	private void expenditureNew() {
		File s = null;
		int z = 0;
		do
			s = new File(ROOT_PATH + "Νέα Δαπάνη - " + z++ + ".δαπάνη");
		while(data.isExpenditureFileExist(s));
		data.addActiveExpenditure(new Expenditure(s, new UnitInfo(data.unitInfo)));
		updateMenus(); updatePanels();
	}

	/** Προσθέτει προέκταση 'δαπάνη' σε ένα αρχείο, αν δεν έχει.
	 * @param f Το αρχείο
	 * @return Το f, αν έχει προέκταση .δαπάνη, ειδάλλως ένα νέο αρχείο ίδιο με το f αλλά με
	 * προέκταση .δαπάνη.
	 * @throws IOException */
	static private File appendExpenditureExt(File f) throws IOException {
		return appendExt(f, ".δαπάνη");
	}

	/** Προσθέτει προέκταση σε ένα αρχείο, αν δεν έχει.
	 * @param f Το αρχείο
	 * @param ext Η επέκταση του ονόματος αρχείου μαζί με την τελεία (".ini")
	 * @return Το f, αν έχει την σωστή προέκταση, ειδάλλως ένα νέο αρχείο ίδιο με το f αλλά με τη
	 * σωστή προέκταση.
	 * @throws IOException */
	static File appendExt(File f, String ext) throws IOException {
		String fname = f.getCanonicalPath();
		return fname.endsWith(ext) ? f : new File(fname + ext);
	}

	/** Ανοίγει το διάλογο "Αποθήκευση ως" και αποθηκεύει την τρέχουσα δαπάνη. */
	private void expenditureSaveCurrent() {
		try {
			// Διάλογος Αποθήκευση ως...
			File file = data.expenditure.file;	// Αποθήκευση: υπάρχει τουλάχιστον μια δαπάνη
			JFileChooser fc = new JFileChooser(file);
			fc.setSelectedFile(file);
			fc.setFileFilter(new ExtensionFileFilter("δαπάνη", "Αρχείο Δαπάνης"));
			if(fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
			File f = appendExpenditureExt(fc.getSelectedFile());
			// Διαδικασίες αν το αρχείο αποθήκευσης είναι διαφορετικό από το ήδη υπάρχον
			boolean otherFile = !file.equals(f);
			if (otherFile) {
				if (data.isExpenditureFileExist(f)) {	// Αν το νέο όνομα αρχείου ανήκει σε άλλη ανοικτή δαπάνη
					showMessageDialog(this, "Το όνομα αυτό ανοίκει σε άλλη ανοικτή δαπάνη.\n"
							+ "Παρακαλώ δώστε άλλο όνομα.", "Αποθήκευση Δαπάνης", ERROR_MESSAGE);
					return;
				} else if (f.exists()) {	// Αν το νέο όνομα αρχείου υπάρχει στο μέσο αποθήκευσης
					if (NO_OPTION == showConfirmDialog(this,
							"Το αρχείο αυτό υπάρχει και θα χαθεί.\nΘελετε να συνεχίσω;",
							"Αποθήκευση Δαπάνης", YES_NO_OPTION, WARNING_MESSAGE))
						return;
				}
			}
			// Αποθήκευση του αρχείου δαπάνης
			PhpSerializer.serialize(data.expenditure.save(), new FileOutputStream(f), UTF_8);
			// Αν το αρχείο αποθήκευσης είναι διαφορετικό από το ήδη υπάρχον, αλλάζει το όνομα της
			// δαπάνης στο μενού Δαπάνες
			if (otherFile) {
				data.expenditure.file = f;
				updateMenus();
			}
		} catch(HeadlessException | IOException e) {
			showExceptionMessage(this, e, "Αποθήκευση Δαπάνης", "Πρόβλημα κατά την αποθήκευση της δαπάνης");
		}
	}

	/** Ανοίγει μια δαπάνη με το διάλογο "Άνοιγμα" και την κάνει τρέχουσα.
	 * Ενημερώνει το παράθυρο του προγράμματος. */
	private void expenditureOpen() {
		try {
			// Ο διάλογος Άνοιγμα... θα ανοίξει στο φάκελο που είναι αποθηκευμένη η τρέχουσα δαπάνη
			JFileChooser fc = new JFileChooser(data.isEmpty() ? null : data.expenditure.file);
			fc.setFileFilter(new ExtensionFileFilter("δαπάνη", "Αρχείο Δαπάνης"));
			int returnVal = fc.showOpenDialog(this);
			if(returnVal != JFileChooser.APPROVE_OPTION) return;
			expenditureOpen(this, appendExpenditureExt(fc.getSelectedFile()));
		} catch (IOException ex) {}
	}

	/** Ανοίγει μια δαπάνη και την κάνει τρέχουσα.
	 * Ενημερώνει το παράθυρο του προγράμματος.
	 * @param frame Το παράθυρο του προγράμματος, προκειμένου να ενημερωθεί με την νέα τρέχουσα δαπάνη
	 * @param file Το αρχείο της δαπάνης */
	static private void expenditureOpen(MainFrame frame, File file) {
		try {
			if (data.isExpenditureFileExist(file)) {
				showMessageDialog(frame, "Το όνομα αυτό ανοίκει σε ανοικτή δαπάνη.\n"
						+ "Για να ανοίξετε αυτή τη δαπάνη θα πρέπει να κλείσετε την ομόνυμη ανοικτή.",
						"Άνοιγμα Δαπάνης", ERROR_MESSAGE);
				return;
			}
			Expenditure expenditure = new Expenditure(file,
					PhpSerializer.unserialize(new FileInputStream(file), UTF_8));
			data.addActiveExpenditure(expenditure);
			if (frame != null) {
				frame.updateMenus();
				frame.updatePanels();
			}
		} catch (FormatException e) {
			showExceptionMessage(frame, e, "Άνοιγμα αρχείου",
				"Το αρχείο που προσπαθείτε να ανοίξετε δεν είναι σωστό αρχείο δαπάνης<br><b>"
				+ file.getName() +
				"</b><br>Μήπως προσπαθείτε να ανοίξετε παλιό αρχείο δαπάνης που πλέον δεν υποστηρίζεται;");
		} catch (Exception e) {
			showExceptionMessage(frame, e, "Άνοιγμα αρχείου",
					"Πρόβλημα κατά το άνοιγμα της δαπάνης<br><b>" + file.getName() + "</b>");
		}
	}

	/** Εισαγωγή του προσωπικού. */
	static private final int IMPORT_PERSONNEL= 1;
	/** Εισαγωγή των κρατήσεων. */
	static private final int IMPORT_DEDUCTIONS = 2;
	/** Εισαγωγή των δικαιούχων. */
	static private final int IMPORT_CONTRACTORS = 4;
	/** Εισαγωγή των αμετάβλητων στοιχείων. */
	static private final int IMPORT_UNIT_DATA = 8;

	/** Από ένα αρχείο ρυθμίσεων ένα αρχείο δαπάνης, εισάγει τα κοινά δεδομένα του προγράμματος.
	 * Χρησιμοποιείται για την εισαγωγή προσωπικού, κρατήσεων, δικαιούχων και των αμετάβλητων
	 * δεδομένων, στις αντίστοιχες καρτέλες του προγράμματος. Αν π.χ. έχει καταστραφεί το αρχείο
	 * ρυθμίσεων και έχουμε κρατημένο ένα άλλο, μπορούμε να επαναφέρουμε αρκετά πράγματα.
	 * <p>Ανοίγει ένα διάλογο στο χρήστη προκειμένου να επιλέξει ένα ή περισσότερα αρχεία ρυθμίσεων
	 * ή δαπανών. Στη συνέχεια ανοίγει δεύτερο διάλογο που ο χρήστης επιλέγει τι στοιχεία θα αντλήσει
	 * από τα επιλεγμένα αρχεία (π.χ. μόνο το Προσωπικό). Έπειτα ενημερώνει τα αμετάβλητα στοιχεία
	 * του προγράμματος από τα αρχεία επιλογής. */
	private void importData() {
		JFileChooser fc = new JFileChooser(iniPath);
		fc.setMultiSelectionEnabled(true);
		fc.setFileFilter(new ExtensionFileFilter(new String[] { "ini", "δαπάνη" },
				"Αρχείο Δαπάνης και Ρυθμίσεων"));
		int returnVal = fc.showOpenDialog(this);
		if(returnVal != JFileChooser.APPROVE_OPTION) return;
		File[] files = fc.getSelectedFiles();
		final String choices[] = new String[] {
			"Δικαιούχοι, Κρατήσεις, Προσωπικό",
			"Δικαιούχοι, Προσωπικό",
			"Αμετάβλητα στοιχεία",
			"Δικαιούχοι",
			"Κρατήσεις",
			"Προσωπικό"
		};
		final int fchoices[] = new int[] {
			IMPORT_CONTRACTORS | IMPORT_DEDUCTIONS | IMPORT_PERSONNEL,
			IMPORT_CONTRACTORS | IMPORT_PERSONNEL,
			IMPORT_UNIT_DATA,
			IMPORT_CONTRACTORS,
			IMPORT_DEDUCTIONS,
			IMPORT_PERSONNEL
		};
		Object a = showInputDialog(this,
				"Επιλέξτε τι θα εξάγετε από τα επιλεχθέντα αρχεία δαπανών και ρυθμίσεων\n"
				+ "για εισαγωγή στα δεδομένα του προγράμματος", "Εισαγωγή στοιχείων από αρχεία δαπανών και ρυθμίσεων",
				QUESTION_MESSAGE, null, choices, choices[0]);
		if (a == null) return;
		int flags = fchoices[Arrays.asList(choices).indexOf(a)];
		for (File file : files) {
			importData(file, flags);
			// Μόνο μια φορά εισάγει τα αμετάβλητα δεδομένα
			flags &= IMPORT_CONTRACTORS | IMPORT_DEDUCTIONS | IMPORT_PERSONNEL;
		}
		// Ενημέρωση πινάκων γιατί μπορεί να άλλαξε ο αριθμός γραμμών τους
		rtmContractors.fireTableDataChanged();
		rtmDeductions.fireTableDataChanged();
		rtmPersonnel.fireTableDataChanged();
	}

	/** Από ένα αρχείο ρυθμίσεων ένα αρχείο δαπάνης, εισάγει τα κοινά δεδομένα του προγράμματος.
	 * Χρησιμοποιείται για την εισαγωγή προσωπικού, κρατήσεων, δικαιούχων και των αμετάβλητων
	 * δεδομένων, στις αντίστοιχες καρτέλες του προγράμματος. Αν π.χ. έχει καταστραφεί το αρχείο
	 * ρυθμίσεων και έχουμε κρατημένο ένα άλλο, μπορούμε να επαναφέρουμε αρκετά πράγματα.
	 * @param file Το αρχείο ρυθμίσεων ή δαπάνης
	 * @param flags Το τι θα εισάγει. Διάζευξη των IMPORT_*. */
	private void importData(File file, int flags) {
		String fname = file.getName();
		try {
			InputStream is = new FileInputStream(file);
			if (fname.endsWith(".ini"))
				importData(iniLoad(is), flags);
			else importData(new Expenditure(file, PhpSerializer.unserialize(is, UTF_8)), flags);
		} catch(Exception e) {
			showExceptionMessage(this, e, "Άνοιγμα αρχείου",
					"Πρόβλημα κατά το άνοιγμα του αρχείου δαπάνης ή ρυθμίσεων<br><b>" + fname + "</b>");
		}
	}

	/** Από ένα αρχείο ρυθμίσεων, εισάγει τα κοινά δεδομένα του προγράμματος.
	 * Χρησιμοποιείται για την εισαγωγή προσωπικού, κρατήσεων, δικαιούχων και των αμετάβλητων
	 * δεδομένων, στις αντίστοιχες καρτέλες του προγράμματος. Αν π.χ. έχει καταστραφεί το αρχείο
	 * ρυθμίσεων και έχουμε κρατημένο ένα άλλο, μπορούμε να επαναφέρουμε αρκετά πράγματα.
	 * @param d Το αρχείο ρυθμίσεων
	 * @param flags Το τι θα εισάγει. Διάζευξη των IMPORT_*. */
	static private void importData(AppData d, int flags) {
		if ((flags & IMPORT_PERSONNEL) != 0) importFiltered(d.personnel, data.personnel);
		if ((flags & IMPORT_DEDUCTIONS) != 0) importFiltered(d.deductions, data.deductions);
		if ((flags & IMPORT_CONTRACTORS) != 0) importFiltered(d.contractors, data.contractors);
		if ((flags & IMPORT_UNIT_DATA) != 0) data.unitInfo = d.unitInfo;
	}

	/** Από μια δαπάνη, εισάγει τα κοινά δεδομένα του προγράμματος.
	 * Χρησιμοποιείται για την εισαγωγή προσωπικού, κρατήσεων, δικαιούχων και των αμετάβλητων
	 * δεδομένων, στις αντίστοιχες καρτέλες του προγράμματος. Αν π.χ. έχει καταστραφεί το αρχείο
	 * ρυθμίσεων και έχουμε κρατημένα μερικά αρχεία δαπανών, μπορούμε να επαναφέρουμε αρκετά πράγματα.
	 * @param expenditure Η δαπάνη
	 * @param flags Το τι θα εισάγει. Διάζευξη των IMPORT_*. */
	static private void importData(Expenditure expenditure, int flags) {
		if ((flags & IMPORT_PERSONNEL) != 0) {
			MainFrame.importData(expenditure.unitInfo.getPersonnel());
			MainFrame.importData(expenditure.getPersonnel());
		}
		if ((flags & IMPORT_DEDUCTIONS) != 0)
			importFiltered(expenditure.invoices.stream().map(i -> i.getDeduction()), data.deductions);
		if ((flags & IMPORT_CONTRACTORS) != 0) {
			importFiltered(expenditure.invoices.stream().map(i -> i.getContractor()), data.contractors);
			importFiltered(expenditure.contracts.stream().flatMap(i -> Stream.of(i.getContractors())),
																					data.contractors);			
			importFiltered(expenditure.tenders.stream().flatMap(i -> i.competitors.stream())
													.map(j -> j.getContractor()), data.contractors);
		}
		if ((flags & IMPORT_UNIT_DATA) != 0) data.unitInfo = expenditure.unitInfo;
	}

	/** Από τα αμετάβλητα δεδομένα, εισάγει προσωπικό στα δεδομένα του προγράμματος.
	 * Χρησιμοποιείται για την εισαγωγή προσωπικού στην αντίστοιχη καρτέλα του προγράμματος. Αν π.χ.
	 * έχει καταστραφεί το αρχείο ρυθμίσεων και έχουμε κρατημένα μερικά αρχεία δαπανών, μπορούμε να
	 * επαναφέρουμε αρκετά πράγματα.
	 * @param p Τα αμετάβλητα δεδομένα */
	static private void importData(Person[] p) {
		Stream.of(p).filter(i -> i != null && !data.personnel.contains(i))
					.forEach(i -> data.personnel.add(i));
	}

	/** Εισάγει τα στοιχεία της λίστας in στη λίστα out, αν δεν είναι null και δεν υπάρχουν ήδη στην out. */
	static private void importFiltered(List in, List out) { importFiltered(in.stream(), out); }

	/** Εισάγει τα στοιχεία του stream στη λίστα, αν δεν είναι null και δεν υπάρχουν ήδη στην λίστα. */
	static private void importFiltered(Stream stream, List out) {
		stream.filter(i -> i != null && !out.contains(i)).forEach(i -> out.add(i));
	}

	/** Κλείσιμο της τρέχουσας δαπάνης. */
	private void expenditureCloseActive() {
		if (YES_OPTION == showConfirmDialog(this,
				"<html>Να κλείσω την τρέχουσα δαπάνη;", "Κλείσιμο Δαπάνης",
				YES_NO_OPTION, WARNING_MESSAGE)) {
			data.expenditures.remove(data.expenditure);
			data.expenditure = data.expenditures.isEmpty() ? null : data.expenditures.get(0);
			updatePanels(); updateMenus();
		}
	}

	@Override protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			try { iniSave(); }
			catch(IOException ex) {
				if (NO_OPTION == showConfirmDialog(this,
						"<html>Αποτυχία κατά την αποθήκευση του <b>" + INI + "</b>.<br>Να κλείσω τo πρόγραμμα;",
						"Τερματισμός", YES_NO_OPTION, WARNING_MESSAGE))
					return;
			}
			serverKill();
		}
		super.processWindowEvent(e);
	}

	// =================================== ΑΡΧΕΙΟ ΡΥΘΜΙΣΕΩΝ ΚΑΙ ΑΡΧΙΚΟΠΟΙΗΣΗ ===

	/** Αποθηκεύει τις ρυθμίσεις του προγράμματος (συμπεριλαμβάνονται οι ανοικτές δαπάνες).
	 * @throws IOException */
	static private void iniSave() throws IOException {
		PhpSerializer.serialize(data, new FileOutputStream(iniPath), UTF_8);
	}

	/** Φορτώνει το default αρχείο ρυθμίσεων του προγράμματος.
	 * Δεν πρόκειται για μια καθημερινή λειτουργία. Συμβαίνει μόνο όταν:
	 * <ul><li>Κάτι καταστροφικό έχει συμβεί. Π.χ. ο χρήστης διέγραψε το κανονικό αρχείο ρυθμίσεων.
	 * <li>Το πρόγραμμα εκτελείται για πρώτη φορά σε αυτόν τον υπολογιστή.</ul>
	 * <p>Αν συμβεί σφάλμα (σχεδόν αδύνατο), ένα μύνημα εμφανίζεται που προειδοποιεί το χρήστη, και
	 * οι ρυθμίσεις είναι κενές.
	 * @return Τα δεδομένα του προγράμματος (τα οποία είναι κενά αν συμβεί σφάλμα) */
	static private AppData iniLoadDefault() {
		try { return iniLoad(getSystemResourceAsStream(INI)); }
		catch(Exception e) {
			showExceptionMessage(null, e, "Πρόβλημα",
				"Πρόβλημα κατά τη φόρτωση του default <b>" + INI + "</b>.");
			return new AppData();
		}
	}

	/** Φορτώνει το αρχείο ρυθμίσεων του προγράμματος.
	 * @param is Ένα stream που περιέχει το αρχείο ρυθμίσεων του προγράμματος
	 * @return Τα δεδομένα του προγράμματος */
	static private AppData iniLoad(InputStream is) throws Exception {
		return new AppData(PhpSerializer.unserialize(is, UTF_8));
	}

	/** Φορτώνει το default αρχείο ρυθμίσεων του προγράμματος.
	 * Δεν πρόκειται για μια καθημερινή λειτουργία. Συμβαίνει μόνο όταν κάτι πολύ καταστροφικό έχει
	 * συμβεί. Π.χ. Το κανονικό αρχείο ρυθμίσεων δεν περιέχει σωστά δεδομένα.
	 * <p>Η καταστροφή που απαγορεύει τη φόρτωση του κανονικου αρχείου ρυθμίσεων, εμφανίζεται σε
	 * ένα παράθυρο σφάλματος.
	 * @return Τα δεδομένα του προγράμματος */
	static private AppData iniLoadDefaultWarn(Exception e) {
		showExceptionMessage(null, e, "Πρόβλημα",
				"Πρόβλημα κατά τη φόρτωση του <b>" + INI + "</b><br>"
				+ "Θα φορτώσω τη default έκδοσή του.");
		return iniLoadDefault();
	}

	/** Σώζει αυτόματα, κάθε 5 λεπτά, τις ρυθμίσεις του προγράμματος.
	 * Συμπεριλαμβάνονται όσες δαπάνες είναι ανοικτές. */
	static private void iniAutoSave() {
		newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
				try { iniSave(); } catch(IOException ex) {}
			}, 5, 5, MINUTES);

	}

	/** Επιστρέφει το φάκελο εκτέλεσης του προγράμματος.
	 * @return Ο φάκελος εκτέλεσης του προγράμματος */
	static private String getRootPath() {
		try {	//JDK11: Αντικατάσταση του "UTF-8" με UTF_8 και αφαίρεση των try-catch
			return decode(getSystemResource(INI).getPath()
					.replaceAll("(expenditure\\.jar!/)?expenditure\\.ini$|^(file\\:)?/", ""), "UTF-8");
		} catch(IOException e) { return null; }
	}

	/** Φορτώνει τις ρυθμίσεις του προγράμματος.
	 * Υψηλού επιπέδου κλήση που κάνει τις παρακάτω προσπάθειες:
	 * <ol><li>Φορτώνει το αρχείο ρυθμίσεων από το φάκελο του προγράμματος. Αυτή είναι η φορητή
	 * (portable) χρήση του προγράμματος. Αν δε βρει το αρχείο πάμε στο βήμα 2. Αν το αρχείο βρεθεί
	 * αλλά αποτύχει η φόρτωση (π.χ. χαλασμένο) πάμε στο βήμα 3.
	 * <li>Φορτώνει το αρχείο ρυθμίσεων από το φάκελο του χρήστη. Αυτή είναι η κανονική χρήση του
	 * προγράμματος. Αν δε βρει το αρχείο ή αποτύχει η φόρτωση πάμε στο βήμα 3.
	 * <li>Φορτώνει το προκαθορισμένο αρχείο ρυθμίσεων που βρίσκεται μέσα στο JAR του προγράμματος.
	 * Αυτό σημαίνει ότι το αρχείο ρυθμίσεων είτε στην portable είτε στην κανονική θέση, θα
	 * αντικατασταθούν από το προκαθορισμένο. Αν υπάρξει οποιοδήποτε πρόβλημα, πάμε στο βήμα 4.
	 * <li>Το πρόγραμμα ξεκινάει με όλα τα δεδομένα κενά.</ol> */
	static private void iniLoad() {
		// Πρώτα ψάχνει το αρχείο ρυθμίσεων στο φάκελο εγκατάστασης (φορητή λειτουργία)
		iniPath = ROOT_PATH + INI;
		try { data = iniLoad(new FileInputStream(iniPath)); }
		catch(FileNotFoundException ex) {
			// Αν αποτύχει, ψάχνει το αρχείο ρυθμίσεων στο φάκελο του χρήστη (κανονική λειτουργία)
			iniPath = System.getProperty("user.home") + "/" + INI;
			try { data = iniLoad(new FileInputStream(iniPath)); }
			catch(FileNotFoundException e) { data = iniLoadDefault(); }
			catch(Exception e) { data = iniLoadDefaultWarn(e); }
		} catch(Exception ex) { data = iniLoadDefaultWarn(ex); }
	}

	/** Εισάγει τις κρατήσεις στα δεδομένα του προγράμματος.
	 * Όταν εγκαθιστούμε νέα έκδοση του προγράμματος, την πρώτη φορά που θα εκτελεστεί, θα εισάγει
	 * τυχόν νέες κρατήσεις που έχουν θεσμοθετηθεί. */
	static private void importNewDeductions() {
		if (!VERSION.equals(data.version)) {
			Deduction[] deductions = {
				D4, D4_096, D4_296, D4_1996, D4_3996, D0_2, D0_1036,
				D0_3036, D14, D14_096, D14_296, D14_1996, D14_3996
			};
			importFiltered(Stream.of(deductions), data.deductions);
			data.version = VERSION;	// ανανέωση της έκδοσης στις ρυθμίσεις στην τρέχουσα
		}
	}

	/** Φορτώνει δαπάνες στο πρόγραμμα.
	 * @param filenames Τα ονόματα αρχείου των δαπανών για φόρτωση */
	static private void expendituresOpen(String[] filenames) {
		for (String s : filenames)
			expenditureOpen(null, new File(s));
	}

	// ====================================================== ΕΞΑΓΩΓΗ ΣΕ PHP ===

	/** Αρχικοποίηση της μηχανής PHP.
	 * @return true Αν αρχικοποιήθηκε με επιτυχία */
	static private boolean initPHP() {
		// Η διαδρομή προς το εκτελέσιμο php. Σε linux είναι απλά 'php' ενώ σε Windows είναι η
		// διαδρομή προς το php που εγκαθιστά ο installer του προγράμματος.
		String php = System.getProperty("os.name").contains("Windows") ? ROOT_PATH + "php5/php.exe" : "php";
		try { PhpScriptRunner.init(php); return true; }
		catch (ExecutionException e) {
			showExceptionMessage(null, e, "Πρόβλημα του PHP cli",
				"Πρόβλημα κατά την αρχικοποίηση του <b>PHP cli</b>.<br>Το πρόγραμμα θα τερματίσει.");
			return false;
		}
	}

	/** Εμφανίζει πληροφορίες για έναν λογαριασμό IBAN που εισάγει ο χρήστης.
	 * Αρχικά ζητάει από το χρήστη να δώσει έναν λογαριασμό IBAN. Στη συνέχεια εμφανίζει ένα διάλογο
	 * με πληροφορίες για το λογαριασμό αυτό, όπως π.χ. αν είναι έγκυρος, σε ποια τράπεζα ανήκει κτλ. */
	private void iban() {
		String iban = showInputDialog(this, "Δώστε έναν IBAN", "Πληροφορίες ΙΒΑΝ", QUESTION_MESSAGE);
		if (iban == null) return;
		iban = iban.replaceAll("[^A-Z0-9]", "");
		try {
			String script = "<?php require('functions.php'); iban_gui('" + iban + "'); ?>";
			byte[] a = exportScriptOutput(script, null, true);
			showMessageDialog(this, new String(a, GREEK), "Πληροφορίες ΙΒΑΝ", INFORMATION_MESSAGE);
		} catch (Exception e) { showError(e.getMessage()); }
	}

	/** Εξάγει μια υπεύθυνη δήλωση για δικαιούχους.
	 * Αν υπάρχει ανοικτή δαπάνη, εξάγει Υπεύθυνη Δήλωση για όλους τους δικαιούχους των τιμολογίων
	 * της δαπάνης. Αν δεν υπάρχει ανοικτή δαπάνη, εμφανίζει ένα παράθυρο επιλογής ενός δικαιούχου
	 * και εξάγει μια Υπεύθυνη Δήλωση για το δικαιούχο που θα επιλεγεί.
	 * @param function Το αρχείο PHP που θα εκτελεστεί προκειμένου να εξαχθεί η Υπεύθυνη Δήλωση */
	static private void statement(String function) {
		VariableSerializable o;
		TreeMap<String, String> env = new TreeMap<>();
		if (data.isEmpty()) {
			o = (Contractor) showInputDialog(window, "Επιλέξτε το δικαιούχο για τον οποίο θα βγει η Υπεύθυνη Δήλωση",
					"Εξαγωγή Υπεύθυνης Δήλωσης", QUESTION_MESSAGE, null,
					data.contractors.toArray(new Contractor[0]), null);
			if (o == null) return;
			env.put("unit", data.unitInfo.getUnitName());
		} else o = data.expenditure;
		env.put("export", function);
		exportReport("export.php", o, env);
	}

	/** Επιλογή αν θέλουμε μια διαταγή να εξαχθεί ως σχέδιο ή ως ακριβές αντίγραφο.
	 * Εμφανίζει ένα διάλογο όπου ο χρήστης επιλέγει εξαγωγή μιας διαταγής σαν σχέδιο ή σαν ακριβές
	 * αντίγραφο.
	 * @param filename Το όνομα αρχείου PHP που εξάγει τη Δγη */
	private void showDraftDialogExport(String filename) {
		showDraftDialogExport(filename, data.expenditure);
	}

	/** Επιλογή αν θέλουμε μια διαταγή να εξαχθεί ως σχέδιο ή ως ακριβές αντίγραφο.
	 * Εμφανίζει ένα διάλογο όπου ο χρήστης επιλέγει εξαγωγή μιας διαταγής σαν σχέδιο ή σαν ακριβές
	 * αντίγραφο.
	 * @param filename Το όνομα αρχείου PHP που εξάγει τη Δγη
	 * @param obj Το αντικείμενο που θα εξαχθεί στο stdin του PHP */
	private void showDraftDialogExport(String filename, VariableSerializable obj) {
		TreeMap<String, String> env = new TreeMap();
		final String[] a = { "Ακριβές Αντίγραφο", "Σχέδιο" };
		int b = showOptionDialog(this, "Επιλέξτε σαν τι θα βγεί η διαταγή.",
				"Επιλογή", OK_CANCEL_OPTION, QUESTION_MESSAGE, null, a, a[0]);
		switch(b) {
			case CLOSED_OPTION: return;
			case 1: env.put("draft", "true");	// χωρίς break
			default: exportReport(filename, obj, env);
		}
	}

	/** Εξάγει ένα PHP πρότυπο, από αυτά που περιέχονται στο αρχείο export.php.
	 * Εκτελεί το export.php script και εξάγει ένα αρχείο κειμένου RTF. Ζητά από το χρήστη με διάλογο,
	 * που να το αποθηκεύσει ή εμφανίζει τυχόν λάθη που προέκυψαν κατά τη διαδικασία.
	 * @param function Το όνομα που αφορά το κείμενο που θα εξαχθεί. Συνήθως το όνομα της συνάρτησης
	 * που θα κληθεί για να δημιουργήσει το κείμενο. */
	static private void exportReport(String function) { exportReport(data.expenditure, function); }

	/** Εξάγει ένα PHP πρότυπο, από αυτά που περιέχονται στο αρχείο export.php.
	 * Εκτελεί το export.php script και εξάγει ένα αρχείο κειμένου RTF. Ζητά από το χρήστη με διάλογο,
	 * που να το αποθηκεύσει ή εμφανίζει τυχόν λάθη που προέκυψαν κατά τη διαδικασία.
	 * @param obj Το αντικείμενο που θα γίνει serialize και θα εισαχθεί στο script από το stdin
	 * @param function Το όνομα που αφορά το κείμενο που θα εξαχθεί. Συνήθως το όνομα της συνάρτησης
	 * που θα κληθεί για να δημιουργήσει το κείμενο. */
	static void exportReport(VariableSerializable obj, String function) {
		TreeMap<String, String> env = new TreeMap<>();
		env.put("export", function);
		exportReport("export.php", obj, env);
	}

	/** Εξάγει ένα PHP πρότυπο.
	 * Εκτελεί ένα PHP script και εξάγει ένα αρχείο κειμένου RTF. Ζητά από το χρήστη με διάλογο, που
	 * να το αποθηκεύσει ή εμφανίζει τυχόν λάθη που προέκυψαν κατά τη διαδικασία.
	 * @param fname Το όνομα αρχείου του PHP προτύπου
	 * @param env Οι μεταβλητές περιβάλλοντος για το PHP script που θα δημιουργήσει το εξαγόμενο
	 * αρχείο ή null */
	static private void exportReport(String fname, Map<String, String> env) {
		exportReport(fname, data.expenditure, env);
	}

	/** Εξάγει ένα PHP πρότυπο.
	 * Εκτελεί ένα PHP script και εξάγει ένα αρχείο κειμένου RTF. Ζητά από το χρήστη με διάλογο, που
	 * να το αποθηκεύσει ή εμφανίζει τυχόν λάθη που προέκυψαν κατά τη διαδικασία.
	 * @param fname Το όνομα αρχείου του PHP προτύπου
	 * @param obj Το αντικείμενο που θα γίνει serialize και θα εισαχθεί στο script από το stdin
	 * @param env Οι μεταβλητές περιβάλλοντος για το PHP script που θα δημιουργήσει το εξαγόμενο
	 * αρχείο ή null */
	static private void exportReport(String fname, VariableSerializable obj, Map<String, String> env) {
		// Εκτέλεση του PHP script με την αντίστοιχη είσοδο και έξοδο
		StdInStream sin = os -> {
			// Το try-catch απαιτείται γιατί αν το script έχει ήδη τερματίσει, επειδή δε θέλει
			// να επεξεργαστεί το stdin, έχει κλείσει το stdin στο οποίο εμείς εδώ γράφουμε.
			try {
				PhpSerializer.serialize(obj, os, GREEK);
				os.close();
			} catch(IOException ex) {}
		};
		exportReport(fname, sin, env);
	}

	/** Εμφανίζει ένα διάλογο για την αποθήκευση του αρχείου RTF.
	 * @param out Τα δεδομένα για εξαγωγή στο αρχείο */
	static private void exportPromptRTF(byte[] out) throws IOException {
		// Διάλογος αποθήκευσης του αρχείου δαπάνης
		File file = data.isEmpty() ? null : data.expenditure.file;
		JFileChooser fc = new JFileChooser(file);
		fc.setFileFilter(new ExtensionFileFilter("rtf", "Rich Text"));
		int returnVal = fc.showSaveDialog(window);
		if(returnVal != JFileChooser.APPROVE_OPTION) return;
		file = appendExt(fc.getSelectedFile(), ".rtf");
		try (FileOutputStream f = new FileOutputStream(file)) {
			f.write(out);
			f.close();
		}
		try { Desktop.getDesktop().open(file); }
		catch (RuntimeException | IOException ex) {}
	}

	/** Εξάγει ένα PHP πρότυπο.
	 * Εκτελεί ένα PHP script και εξάγει ένα αρχείο κειμένου RTF. Ζητά από το χρήστη με διάλογο, που
	 * να το αποθηκεύσει ή εμφανίζει τυχόν λάθη που προέκυψαν κατά τη διαδικασία.
	 * @param fname Το όνομα αρχείου του PHP προτύπου
	 * @param sin Ο χειριστής του stdin του PHP script ή null
	 * @param env Οι μεταβλητές περιβάλλοντος για το PHP script που θα δημιουργήσει το εξαγόμενο
	 * αρχείο. Το null επιτρέπεται. */
	static private void exportReport(String fname, StdInStream sin, Map<String, String> env) {
		// Αρχικοποίηση των ρυθμίσεων εκτέλεσης του PHP script
		PhpScriptRunner php = new PhpScriptRunner(ROOT_PATH + "php/", fname, null);
		if (env != null) php.getEnvironment().putAll(env);
		try {
			PhpScriptRunner.StdOut out = new PhpScriptRunner.StdOut(), err = new PhpScriptRunner.StdOut();
			int errCode = php.exec(sin, out, err);
			// Έλεγχος σφαλμάτων εκτέλεσης του PHP script
			byte[] a = err.join();
			String error = a != null ? new String(a, GREEK) : "";
			if (errCode != 0) error += "<html><font color=red><b>Το php script τερμάτισε με σοβαρό σφάλμα";
			if (!error.isEmpty()) throw new Exception(error);	// δε χρειάζεται να περιμένουμε το stdout
			// Διάλογος αποθήκευσης του αρχείου δαπάνης
			exportPromptRTF(out.join());
		} catch (Exception e) { showError(e.getMessage()); }
	}

	/** Επιστρέφει την έξοδο ενός PHP script.
	 * @param script Το PHP script
	 * @param env Οι μεταβλητές περιβάλλοντος για το PHP script που θα δημιουργήσει το εξαγόμενο
	 * αρχείο. Το null επιτρέπεται.
	 * @param redirectError Ανακατευθύνει την έξοδο του stderr στο stdout
	 * @return Η έξοδος του PHP script στο stdout */
	static private byte[] exportScriptOutput(String script, Map<String, String> env,
			boolean redirectError) throws Exception {
		StdInStream sin = os -> {
			os.write(script.getBytes(GREEK));
			os.close();
		};
		// Αρχικοποίηση των ρυθμίσεων εκτέλεσης του PHP script
		PhpScriptRunner php = new PhpScriptRunner(ROOT_PATH + "php/", null, null);
		if (env != null) php.getEnvironment().putAll(env);
		int errCode;
		String error = "";
		PhpScriptRunner.StdOut out = new PhpScriptRunner.StdOut();
		if (redirectError) errCode = php.exec(sin, out);
		else {
			PhpScriptRunner.StdOut err = new PhpScriptRunner.StdOut();
			errCode = php.exec(sin, out, err);
			// Έλεγχος σφαλμάτων εκτέλεσης του PHP script
			byte[] a = err.join();
			error = a != null ? new String(a, GREEK) : "";
		}
		if (errCode != 0) error += "<html><font color=red><b>Το php script τερμάτισε με σοβαρό σφάλμα";
		if (!error.isEmpty()) throw new Exception(error);	// δε χρειάζεται να περιμένουμε το stdout
		return out.join();
	}

	/** Αποθηκεύει την έξοδο ενός PHP script.
	 * Εκτελεί ένα PHP script και εξάγει ένα αρχείο κειμένου RTF. Ζητά από το χρήστη με διάλογο, που
	 * να το αποθηκεύσει ή εμφανίζει τυχόν λάθη που προέκυψαν κατά τη διαδικασία.
	 * @param script Το PHP script */
	static private void saveScriptOutput(String script) {
		try { exportPromptRTF(exportScriptOutput(script, null, false)); }
		catch (Exception e) { showError(e.getMessage()); }
	}

	/** Εμφανίζει ένα διάλογο με τα σφάλματα της εξαγωγής αρχείου από το PHP script.
	 * @param err Το κείμενο του σφάλματος αποτελούμενο από αριθμό γραμμών. */
	static private void showError(String err) {
		JDialog dlg = new JDialog(window, "Εμφάνιση σφαλμάτων εκτέλεσης του PHP Script", true);
		JList<String> list = new JList<>(err.split("\n"));
		JScrollPane scroll = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		dlg.add(scroll);
		dlg.pack();
		dlg.setLocation(getLocationScreenCentered(dlg.getWidth(), dlg.getHeight()));
		dlg.setVisible(true);
	}

	// ============================================================= ΔΙΑΦΟΡΑ ===

	/** Εμφάνιση παραθύρου σφάλματος.
	 * @param c Το πατρικό παράθυρο (ή στοιχείο αυτού). Μπορεί να είναι null.
	 * @param e Η εξαίρεση (exception) που συνέβη. Μπορεί να είναι null.
	 * @param title Ο τίτλος του παραθύρου σφάλματος
	 * @param info Πληροφορίες για το σφάλμα. Μπορεί να είναι null. */
	static void showExceptionMessage(Component c, Exception e, String title, String info) {
		if (info == null) info = ""; else info += "<br>";
		if (e != null) {
			info += "Σφάλμα: <b>" + e.getClass().getName() + "</b>";
			String s = e.getLocalizedMessage();
			if (s != null && s.length() > 7) info += "<br>Λόγος: <b>" + s;
		}
		showMessageDialog(c, "<html>" + info, title, ERROR_MESSAGE);
	}

	/** Διαβάζει όλα τα bytes από ένα input stream.
	 * Υπάρχει στη Java 9+ αλλά όχι στην Java 8. Οπότε την υλοποιούμε.
	 * @param is Το stream απ' όπου θα διαβαστούν τα δεδομένα.
	 * @return Όλα τα δεδομένα του stream σε ένα byte array
	 * @throws IOException */
	static public byte[] readAllBytes(InputStream is) throws IOException {
		final int BUFFER_SIZE = 65536;				// Μέγιστο μέγεθος ενός chunk
		ArrayList<byte[]> bufs = new ArrayList<>();	// Κρατάει όλα τα chunks
		int n = BUFFER_SIZE;	// Πόσα bytes διαβάστηκαν από το stream < BUFFER_SIZE
		byte[] buf = null;		// Το chunk
		for(;;) {
			if (n == BUFFER_SIZE) {
				buf = new byte[BUFFER_SIZE];
				bufs.add(buf);
				n = 0;
			}
			int ret = is.read(buf, n, BUFFER_SIZE - n);
			if (ret == -1) {
				is.close();
				byte[] r = new byte[(bufs.size() - 1) * BUFFER_SIZE + n];	// Buffer εξόδου
				for (int z = 0; z < bufs.size() - 1; ++z) {
					System.arraycopy(bufs.get(z), 0, r, z * BUFFER_SIZE, BUFFER_SIZE);
				}
				System.arraycopy(bufs.get(bufs.size() - 1), 0, r, r.length - n, n);
				return r;
			} else n += ret;
		}
	}

	// ============================================================== SERVER ===

	/** Εξασφαλίζει ότι μόνο μια παρουσία (instance) του προγράμματος εκτελείται.
	 * Ανοίγει και διατηρεί ανοικτή, μια συγκεκριμένη πόρτα δικτύου (την SERVER_PORT) για να
	 * ακούει. Αν αποτύχει να την ανοίξει, θεωρεί ότι κάποιο άλλο instance του προγράμματος τρέχει
	 * ήδη και έχει προλάβει να ανοίξει αυτό, την συγκεκριμένη πόρτα. Στην περίπτωση αυτή, αν έχει
	 * κληθεί με παράμετρο μια δαπάνη, για να την ανοίξει, τότε στέλνει στο instance που ακούει την
	 * πόρτα, μέσω δικτυακού πρωτοκόλλου, το όνομα της δαπάνης που πρέπει να ανοιχτεί και τερματίζει.
	 * Το instance που ακούει στην πόρτα, λαμβάνει το αρχείο της δαπάνης και το ανοίγει.
	 * Τα αρχεία δαπανών δεν ανοίγουν, αν ο server είναι το τρέχον πρόγραμμα (δεν υπάρχει άλλο
	 * ήδη ανοικτό πρόγραμμα δαπανών).
	 * @param filenames Τα ονόματα αρχείων των δαπανών για άνοιγμα στο πρόγραμμα-server
	 * @return true Αν δεν υπάρχει άλλο πρόγραμμα δαπανών ανοικτό */
	static private boolean serverStart(String[] filenames) {
		try { serverStart(); return true; }
		catch(IOException e) {
			for (String arg : filenames)
			{System.out.println(arg);
				serverSend(arg.getBytes(UTF_8));}
			return false;
		}
	}

	/** Τερματίζει το server.
	 * Αυτό επιτυγχάνεται στέλνοντας το byte 0 στο server. */
	static private void serverKill() { serverSend(new byte[] { 0 }); }

	/** Εγκαθιστά έναν server στο socket localhost:SERVER_PORT.
	 * throws IOException Αν το socket έχει δεσμευτεί από άλλο instance του προγράμματος. */
	static private void serverStart() throws IOException {
		// Το socket που μένει ανοικτό. Η πόρτα SERVER_PORT στον τοπικό υπολογιστή.
		ServerSocket ss = new ServerSocket(SERVER_PORT);
		Runnable server = () -> {
			for(;;)
				try {
					byte[] buf = readAllBytes(ss.accept().getInputStream());
					if (buf.length == 1 && buf[0] == 0) return;			// Τερματισμός του server
					expenditureOpen(window, new File(new String(buf, UTF_8)));	// Ανοιγμα δαπάνης
				} catch(IOException e) {}
		};
		new Thread(server).start();
	}

	/** Στέλνει δεδομένα στο socket με πόρτα SERVER_PORT του τρέχοντα υπολογιστή.
	 * Χρησιμοποιείται για να αποστείλει τη δαπάνη που πρέπει να ανοίξει το τρέχον instance, αλλά
	 * δεν θα την ανοίξει επειδή υπάρχει ήδη άλλο ανοικτό instance που θα την ανοίξει εκείνο.
	 * @param a Τα δεδομένα */
	static private void serverSend(byte[] a) {
		try {
			try (
				Socket s = new Socket("127.0.0.1", SERVER_PORT);
				OutputStream os = s.getOutputStream();
			) { // Αν δεν γίνει flush και close, stream και socket, τότε συμβαίνει connection reset στο server
				os.write(a);
				os.flush();
				os.close();
			}
		}
		catch(IOException e) {}
	}
}