package expenditure;

import static expenditure.Contract.TenderType.DIRECT_ASSIGNMENT;
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
import java.awt.AWTEvent;
import java.awt.Color;
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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelEvent;
import static javax.swing.event.TableModelEvent.ALL_COLUMNS;
import static javax.swing.event.TableModelEvent.DELETE;
import static javax.swing.event.TableModelEvent.INSERT;
import static javax.swing.event.TableModelEvent.UPDATE;
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

/** Το κυρίως παράθυρο του προγράμματος.
 * Περιλαμβάνει τη main(). */
final public class MainFrame extends JFrame {
	/** Η διαδρομή του φακέλου του προγράμματος */
	static private String rootPath;
	/** Η διαδρομή του αρχείου ρυθμίσεων του προγράμματος */
	static private String iniPath;
	/** Η έκδοση του προγράμματος. */
	static final String VERSION = "22 Μαι 19";
	/** Το όνομα του αρχείου ρυθμίσεων του προγράμματος */
	static private final String INI = "expenditure.ini";
	/** Η ομάδα χαρακτήρων των ελληνικών. Χρησιμοποιείται στα εξαγόμενα αρχεία RTF. */
	static private final Charset GREEK = Charset.forName("windows-1253");
	/** Επιλογές ναι - όχι. */
	static final String[] NOYES = { "Όχι", "Ναι" };

	/** Όλα τα δεδομένα του προγράμματος. */
	static AppData data;

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
	/** Το μοντέλο δεδομένων του combobox των συμβάσεων. */
	private ComboDataModel<Contract> cdmContracts;
	/** Ο πίνακας με τα δικαιολογητικά και τους διαγωνιζόμενους. */
	private DocumentTableModel rtmDocuments;
	/** Ο πίνακας με τα στοιχεία της δαπάνης. */
	private PropertiesTableModel rtmExpenditure;
	/** Εικονίδιο δημιουργίας. */
	final static public Icon ICON_NEW = loadIcon("new");
	/** Εικονίδιο διαγραφής. */
	final static public Icon ICON_DELETE = loadIcon("close");

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

	/** Δημιουργεί τις καρτέλες του παραθύρου της εφαρμογής.
	 * @return Οι καρτέλες τις εφαρμογής σε ένα panel */
	private JTabbedPane createPanels() {
		// ======================== ΚΟΙΝΟΧΡΗΣΤΑ COMBOBOXES ========================
		// Η τελευταία εγγραφή τους είναι κενή (null), για την περίπτωση που δεν θέλουμε καμία από
		// τις υπαρκτές επιλογές του ComboBox (θέλουμε να αφήσουμε το πεδίο κενό).
		// Χρησιμοποιούνται στα TableModel και βρίσκονται εδώ γιατί κάθε φορά που αλλάζει το κέλυφος
		// πρέπει να επαναδημιουργούνται.

		// Επιλογέας προσώπων που περιέχει λίστα όλου του προσωπικού.
		JComboBox cbPersonnel = new JComboBox(new ComboPlusOneDataModel() {
			@Override protected List get() { return data.personnel; }
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
		cbUnits    .setBorder(border);
		cbPersonnel.setBorder(border);
		cbBoolean  .setBorder(border);

		// ======================== ΚΑΡΤΕΛΑ «ΕΡΓΑΣΙΕΣ» ========================
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

		// ======================== ΚΑΡΤΕΛΑ «ΣΤΟΙΧΕΙΑ ΜΟΝΑΔΑΣ» ========================
		// Οι επικεφαλίδες του πίνακα στοιχείων Μονάδας
		String[] unitHeader = {
			"<html><b>Στοιχεία Μονάδας",
			"<html>Ελέγχουσα Αρχή <font color=gray size=2>(σύντμηση)",
			"<html>Σχηματισμός <font color=gray size=2>(σύντμηση)",
			"<html>Μονάδα <font color=gray size=2>(πλήρης)",
			"<html>Μονάδα <font color=gray size=2>(σύντμηση)",
			UnitInfo.H[4], UnitInfo.H[5], UnitInfo.H[6], UnitInfo.H[7], UnitInfo.H[8], UnitInfo.H[9],
			UnitInfo.H[10], UnitInfo.H[11], UnitInfo.H[12], UnitInfo.H[13],
			"<html><b>Στοιχεία Επιτροπών Δαπάνων",
			UnitInfo.H[14], UnitInfo.H[15], UnitInfo.H[16], UnitInfo.H[17], UnitInfo.H[18],
			UnitInfo.H[19], UnitInfo.H[20], UnitInfo.H[21], UnitInfo.H[22], UnitInfo.H[23],
			UnitInfo.H[24], UnitInfo.H[25], UnitInfo.H[26], UnitInfo.H[27]
		};
		// Οι επεξεργαστές για τα πεδία του πίνακα με τα στοιχεία της Μονάδας.
		// Πρέπει να είναι τύπου Component γιατί τα null στοιχεία, αντικαθίστανται με JTextField.
		Component[] unitEditors = {
			null, null, null, null, null, null, null, null, null, null, null, cbPersonnel, cbPersonnel,
			cbPersonnel, cbPersonnel,
			null, null, null, cbPersonnel
		};

		// Το tabbed panel πρέπει να είναι πρώτο στοιχείο της φορμας και οι Εργασίες το 4ο στοιχείο
		// του tabbed panel.
		// Μετά από κάθε προσθηκη, διαγραφή, αλλαγή σειράς να ελέγχω το MainFrame.updatePanels().
		JTabbedPane tabs = new JTabbedPane();
		tabs.addTab("Στοιχεία Δαπάνης", createPanelExpenditure(unitHeader, cbBoolean, unitEditors));
		tabs.addTab("Τιμολόγια", createPanelInvoices(cbUnits, rtmMaterials));
		tabs.addTab("Συμβάσεις", createPanelContracts(cbBoolean));
		tabs.addTab("Φύλλο καταχώρησης", createPanelContents());
		tabs.addTab("Εργασίες", spWorks);
		tabs.addTab("Στοιχεία Μονάδας", createPanelUnit(unitHeader, unitEditors));
		tabs.addTab("Δικαιούχοι", createPanelContractors(border));
		tabs.addTab("Ανάλυση Κρατήσεων", createPanelDeductions());
		tabs.addTab("Προσωπικό Μονάδας", createPanelPersonnel());

		Color c = new Color(176, 208, 176);
		tabs.setBackgroundAt(0, c);
		tabs.setBackgroundAt(1, c);
		tabs.setBackgroundAt(2, c);
		tabs.setBackgroundAt(3, c);
		tabs.setBackgroundAt(4, c);
		c = new Color(224, 224, 176);
		tabs.setBackgroundAt(5, c);
		tabs.setBackgroundAt(6, c);
		tabs.setBackgroundAt(7, c);
		tabs.setBackgroundAt(8, c);

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
				return super.isCellEditable(row, col) && row != 0 && row != 15;
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
		InvoicesTableModel rtmInvoices = new InvoicesTableModel();
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
			// Εύρεση του επιλεγμένου τιμολογίου
			int selection = window.tblInvoices.getSelectedRow();
			List<Invoice> list = ((InvoicesTableModel) window.tblInvoices.getModel()).get();
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
			if (data.getActiveExpenditure().isSmart()) {	// έχουμε αυτόματο υπολογισμό
				int to = data.getActiveExpenditure().invoices.size() - 1;
				tblInvoices.tableChanged(new TableModelEvent(src, 0, to, 2));
				tblInvoices.tableChanged(new TableModelEvent(src, 0, to, 3));
				tblInvoices.tableChanged(new TableModelEvent(src, 0, to, 4));
				if (col != 4) {
					window.rtmContents.fireTableDataChanged();
					window.rtmDocuments.fireTableChanged();
				}
			}
			// Ο πίνακας με τα κόστη ανανεώνεται για να εμφανίζει τα νέα κόστη
			window.rtmReport.fireTableDataChanged(new TableModelEvent(src));
		});

		// Όταν τροποποιούμε ένα τιμολόγιο στον αντίστοιχο πίνακα
		rtmInvoices.addTableModelListener(e -> {
			ResizableTableModel src = (ResizableTableModel) e.getSource();
			int col = e.getColumn();
			// Εισήχθη ένα τιμολόγιο: Τίποτα
			if (e.getType() == INSERT);
			// Διαγράφηκε ένα τιμολόγιο: Ο πίνακας με τα κόστη και το φύλλο καταχώρησης ανανεώνεται
			if (e.getType() == DELETE) {
				window.rtmReport.fireTableDataChanged(new TableModelEvent(src, 0, 6, 3));	// Στήλη δαπάνης
				window.rtmContents.fireTableDataChanged();
			}
			else if (data.getActiveExpenditure().isSmart()) {	// έχουμε αυτόματο υπολογισμό
				// Αν άλλαξε ο τύπος του τιμολογίου ή ο τύπος του δικαιούχου
				if (col == 1 || col == 2) {
					// Ενδέχεται να άλλαξαν κρατήσεις και ΦΕ σε όλα τα τιμολόγια του ίδιου δικαιούχου
					int to = data.getActiveExpenditure().invoices.size();
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
					window.rtmReport.fireTableDataChanged(new TableModelEvent(src, 1, 6, 3));	// Στήλη δαπάνης
				}
			// Αν άλλαξε η σύμβαση του τιμολογίου, ο πίνακας με τα κόστη και το φύλλο καταχώρησης ανανεώνεται
			} else if (col == 2) {
				window.rtmReport.fireTableDataChanged(new TableModelEvent(rtmItems, 0, 6, 2));	// Στήλη σύμβασης
				window.rtmContents.fireTableDataChanged();
			}
			// Αν άλλαξαν κρατήσεις ή ΦΕ, ο πίνακας με τα κόστη ανανεώνεται
			else if (col == 3 || col == 4) {
				window.rtmReport.fireTableDataChanged(new TableModelEvent(src, 2, 6, 1));	// Στήλη τιμολογίου
				window.rtmReport.fireTableDataChanged(new TableModelEvent(src, 2, 6, 2));	// Στήλη σύμβασης
				window.rtmReport.fireTableDataChanged(new TableModelEvent(src, 2, 6, 3));	// Στήλη δαπάνης
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
	 * @param cbBoolean Ο επιλογέας Ναι/Όχι
	 * @param unitEditors Οι επεξεργαστές του πίνακα της καρτέλας «Στοιχεία Μονάδας»
	 * @return Το panel με τα στοιχεία της δαπάνης */
	private JScrollPane createPanelExpenditure(String[] unitHeader, JComboBox cbBoolean,
			Component[] unitEditors) {
		// Επικεφαλίδα του πίνακα στοιχείων δαπάνης
		String[] expHeader = Stream.concat(Stream.of(new String[] {
			"<html><b>Στοιχεία Δαπάνης",
			Expenditure.H[0], Expenditure.H[1],
			"<html>" + Expenditure.H[2] + " <font color=gray size=2>(Πίστωση ΓΕΣ/Γ2)",
			"Ειδικός Φορέας (ΕΦ)", "Αναλυτικός Λογαριασμός Εσόδων/Εξόδων (ΑΛΕ)", Expenditure.H[5],
			"<html>" + Expenditure.H[6] + " <font color=gray size=2>(αιτιατική)", Expenditure.H[7],
			Expenditure.H[8], Expenditure.H[9], "<html><b>Αυτοματισμοί", Expenditure.H[10]
		}), Stream.of(unitHeader)).toArray(String[]::new);
		// Οι επεξεργαστές για τα πεδία του πίνακα με τα στοιχεία της δαπάνης.
		// Πρέπει να είναι τύπου Component γιατί τα null στοιχεία, αντικαθίστανται με JTextField.
		Component[] expEditors = Stream.concat(Stream.of(new Component[] {
			null, null, cbBoolean, cbBoolean, null, null, new JComboBox(Financing.values()), null,
			null, null, null, null, cbBoolean
		}), Stream.of(unitEditors)).toArray(Component[]::new);
		// Ρύθμιση του πίνακα με τα στοιχεία δαπάνης
		rtmExpenditure = new PropertiesTableModel(expHeader, 1) {
			@Override public TableRecord get(int index) { return data.getActiveExpenditure(); }
			@Override public boolean isCellEditable(int row, int col) {
				return super.isCellEditable(row, col) && row != 0 && row != 3 && row != 11
						&& row - 13 != 0 && row - 13 != 15;
			}
		};
		// Ανανέωση πινάκων που τροποποιούνται λόγω αυτοματισμών
		rtmExpenditure.addTableModelListener(e -> {
			switch(e.getFirstRow()) {
				case 12: window.rtmDocuments.fireTableChanged();	// no break
				case 3: window.rtmContents.fireTableDataChanged(); break;
			}
		});
		return new JScrollPane(createTable(rtmExpenditure, expEditors));
	}

	/** Δημιουργεί ένα panel με τους πίνακες των συμβάσεων.
	 * @param cbBoolean Ο επιλογέας Ναι/Όχι
	 * @return Το panel με τους πίνακες συμβάσεων */
	private Box createPanelContracts(JComboBox cbBoolean) {
		// Επιλογέας σύμβασης
		cdmContracts = new ComboDataModel<Contract>() {
			@Override protected List get() {
				return data.isEmpty() ? new ArrayList() : data.getActiveExpenditure().contracts;
			}
		};
		JComboBox cbContracts = new JComboBox(cdmContracts);
		// Πλήκτρο νέας σύμβασης
		JButton btnNew = new JButton("Νέα σύμβαση", ICON_NEW);
		btnNew.addActionListener(e -> {
			// Νέα σύμβαση: Επιλέγεται η πρώτη κενή, αλλιώς δημιουργείται μια.
			Expenditure expenditure = data.getActiveExpenditure();
			if (expenditure.isSmart())
				showMessageDialog(window, "Όταν οι αυτόματοι υπολογισμοί είναι ενεργοί, οι συμβάσεις "
						+ "δημιουργούνται αυτόματα,\nόταν τα τιμολόγια ξεπεράσουν το προβλεπόμενο ποσό.",
						"Αποτυχία δημιουργίας σύμβασης", ERROR_MESSAGE);
			else {
				List<Contract> contracts = expenditure.contracts;
				Contract contract = contracts.stream().filter(i -> i.isEmpty()).findAny().orElse(null);
				if (contract == null) contracts.add(contract = new Contract(expenditure));
				window.cdmContracts.setSelectedItem(contract);
			}
		});
		// Πλήκτρο διαγραφής σύμβασης
		JButton btnDelete = new JButton("Διαγραφή τρέχουσας σύμβασης", ICON_DELETE);
		btnDelete.addActionListener(e -> {
			Contract contract = window.cdmContracts.getSelectedItem();
			if (contract != null) {
				Expenditure expenditure = data.getActiveExpenditure();
				if (expenditure.invoices.stream().anyMatch(i -> i.getContract() == contract))
					showMessageDialog(window, "Η σύμβαση δε μπορεί να διαγραφεί όσο χρησιμοποιείται από τιμολόγια.",
							"Αποτυχία διαγραφής της σύμβασης", ERROR_MESSAGE);
				else if (contract.isEmpty() || OK_OPTION == showConfirmDialog(window,
						"Είστε σίγουροι ότι θέλετε να διαγράψετε την τρέχουσα σύμβαση;",
						"Διαγραφή τρέχουσας σύμβασης", OK_CANCEL_OPTION, WARNING_MESSAGE)) {
					List<Contract> contracts = expenditure.contracts;
					contracts.remove(contract);
					expenditure.calcContents();
					window.rtmContents.fireTableDataChanged();
					window.cdmContracts.setSelectedItem(contracts.isEmpty() ? null : contracts.get(0));
				}
			}
		});
		cbContracts.setMaximumSize(new Dimension(300, 25));	// Hack
		// Δημιουργία οριζόντιας μπάρας επιλογέα σύμβασης
		Box hBox = Box.createHorizontalBox();
		hBox.add(new JLabel("Τρέχουσα σύμβαση:"));
		hBox.add(cbContracts);
		hBox.add(btnNew);
		hBox.add(btnDelete);

		// Πίνακας στοιχείων σύμβασης
		// Επιλογέας δικαιούχων / εργολάβων / προμηθευτών / ανάδοχων.
		JComboBox cbContractors = new JComboBox(new ComboDataModel() {
			@Override protected List get() { return data.contractors; }
		});
		cbContractors.setBorder(cbBoolean.getBorder());
		// Οι τύποι διαγωνισμού
		JComboBox cbTenderTypes = new JComboBox(Contract.TenderType.values());
		cbTenderTypes.setBorder(cbBoolean.getBorder());
		// Ο πίνακας με τα στοιχεία της σύμβασης
		ContractInfoTableModel ptmInfo = new ContractInfoTableModel();
		Component[] components = new Component[] {  null, null, cbTenderTypes, cbContractors, null };
		JTable tblInfo = PropertiesTableModel.createTable(ptmInfo, components);

		// Ο πίνακας με τα δικαιολογητικά και τους διαγωνιζόμενους
		rtmDocuments = new DocumentTableModel();
		JTable tblDocuments = createTable(rtmDocuments, true, false);
		// Hack: Λόγω των διαγωνιζόμενων, ο πίνακας ξανασχεδιάζεται εξολοκλήρου, με αποτέλεσμα,
		// όλες οι στήλες να διαγράφονται και να επαναδημιουργούνται και να χάνεται ο cell editor.
		// Εδώ, κάθε φορά που μια νέα στήλη προστίθεται, τροποποιούμε το cell editor αν χρειάζεται.
		tblDocuments.getColumnModel().addColumnModelListener(new TableColumnModelListener() {
			@Override public void columnAdded(TableColumnModelEvent e) {
				if (e.getToIndex() == 1)
					((TableColumnModel) e.getSource()).getColumn(1)
							.setCellEditor(new DefaultCellEditor(cbBoolean));
			}
			@Override public void columnRemoved(TableColumnModelEvent e) {}
			@Override public void columnMoved(TableColumnModelEvent e) {}
			@Override public void columnMarginChanged(ChangeEvent e) {}
			@Override public void columnSelectionChanged(ListSelectionEvent e) {}
		});
		// Προσθήκη popup menu στην επικεφαλίδα του πίνακα, για προσθαφαίρεση διαγωνιζόμενων
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(createMenuItem("Εισαγωγή στήλης διαγωνιζόμενου", ICON_NEW, e -> {
			Contract contract = window.cdmContracts.getSelectedItem();
			if (contract == null) return;
			Contractor contractor = (Contractor) showInputDialog(window, "Επιλέξτε διαγωνιζόμενο",
					"Εισαγωγή διαγωνιζόμενου", QUESTION_MESSAGE, null, data.contractors.toArray(), null);
			if (contractor != null) {
				if (contract.competitors.contains(contractor))
					showMessageDialog(window, "Υπάρχει ήδη στήλη με αυτόν το διαγωνιζόμενο.",
							"Εισαγωγή στήλης", ERROR_MESSAGE);
				else {
					contract.competitors.add(contractor);
					// Hack: Με το δεξί κλίκ στην επικεφαλίδα του πίνακα, προκειμένου να εμφανιστεί
					// το μενού για προσθήκη νέας επιμέρους κράτησης, το JTable θεωρεί ότι η στήλη
					// βρίσκεται σε κατάσταση drag & drop και κρατάει μια αναφορά στη συγκεκριμένη
					// στήλη. Όμως εμείς επειδή με τη μεθεπόμενη εντολή λέμε να ξανασχεδιάσει
					// εξολοκλήρου τον πίνακα, όλες οι στήλες διαγράφονται και επαναδημιουργούνται
					// με αποτέλεσμα, όταν μετά το gui renderer thread θα ψάξει να βρει τη στήλη θα
					// μας δώσει exception
					tblDocuments.getTableHeader().setDraggedColumn(null);
					((ResizableTableModel) tblDocuments.getModel()).fireTableChanged();
				}
			}
		}));
		popupMenu.add(createMenuItem("Διαγραφή στήλης διαγωνιζόμενου", "close", e -> {
			Contract contract = window.cdmContracts.getSelectedItem();
			if (contract == null || contract.competitors.isEmpty()) return;
			Contractor contractor = (Contractor) showInputDialog(window, "Επιλέξτε διαγωνιζόμενο",
					"Διαγραφή διαγωνιζόμενου", QUESTION_MESSAGE, null, contract.competitors.toArray(), null);
			if (contractor != null)
				if (contract.getTenderType() != DIRECT_ASSIGNMENT
						&& contractor.equals(contract.getContractor()))
					showMessageDialog(window, "Δεν μπορείτε να διαγράψετε τον νικητή του διαγωνισμού",
							"Διαγραφή διαγωνιζόμενου", ERROR_MESSAGE);
				else {
					contract.competitors.remove(contractor);
					// Hack: Με το δεξί κλίκ στην επικεφαλίδα του πίνακα, προκειμένου να εμφανιστεί
					// το μενού για προσθήκη νέας επιμέρους κράτησης, το JTable θεωρεί ότι η στήλη
					// βρίσκεται σε κατάσταση drag & drop και κρατάει μια αναφορά στη συγκεκριμένη
					// στήλη. Όμως εμείς επειδή με τη μεθεπόμενη εντολή λέμε να ξανασχεδιάσει
					// εξολοκλήρου τον πίνακα, όλες οι στήλες διαγράφονται και επαναδημιουργούνται
					// με αποτέλεσμα, όταν μετά το gui renderer thread θα ψάξει να βρει τη στήλη θα
					// μας δώσει exception
					tblDocuments.getTableHeader().setDraggedColumn(null);
					((ResizableTableModel) tblDocuments.getModel()).fireTableChanged();
				}
		}));
		tblDocuments.getTableHeader().setComponentPopupMenu(popupMenu);

		// Όταν τροποποιούνται διάφορα στοιχεία της σύμβασης
		ptmInfo.addTableModelListener(e -> {
			if (e.getFirstRow() != e.getLastRow()) return; // Μόνο για ανανέωση ενός κελιού
			switch(e.getFirstRow()) {
				case 0:	// Όνομα σύμβασης
					cbContracts.repaint(); break;
				case 2:	// Τύπος Διαγωνισμού
					window.rtmContents.fireTableDataChanged();	// όχι break!
				case 3:	// Ανάδοχος διαγωνισμού
					window.rtmDocuments.fireTableChanged();
					break;
			}
		});

		// Όταν επιλέγουμε άλλη σύμβαση στον επιλογέα συμβάσεων
		cdmContracts.addListDataListener(new ListDataListener() {
			@Override public void intervalAdded(ListDataEvent e) { contentsChanged(e); }
			@Override public void intervalRemoved(ListDataEvent e) { contentsChanged(e); }
			@Override public void contentsChanged(ListDataEvent e) {
				Contract contract = ((ComboDataModel<Contract>) e.getSource()).getSelectedItem();
				ptmInfo.setSelectedContract(contract);
				rtmDocuments.setSelectedContract(contract);
			}
		});

		// Η τελική διαμόρφωση της καρτέλας
		Box vBoxI = Box.createVerticalBox();
		vBoxI.add(tblInfo);
		vBoxI.add(Box.createVerticalStrut(20));
		Box hBoxO = Box.createHorizontalBox();
		hBoxO.add(new JLabel("<html>Δικαιολογητικά που πρέπει να υποβάλλουν οι διαγωνιζόμενοι.<br>"
				+ "Προσθαφαιρούμε διαγωνιζόμενους με δεξί κλίκ στην επικεφαλίδα του πίνακα.<br>"
				+ "Στο κελί γράφουμε το λόγο απορριψης του δικαιολογητικού"
				+ " ή το αφήνουμε κενό αν κάνουμε δεκτό το δικαιολογητικό.<br>"
				+ "Αν ο διαγωνιζόμενος δεν υπέβαλε το δικαιολογητικό βάζουμε -,"
				+ " ενώ αν δεν όφειλε να το υποβάλει βάζουμε +.<br>"
				+ "Αν ένα δικαιολογητικό τελειώνει με ' ή', το επόμενο αποτελεί ενναλλακτικό του."));
		vBoxI.add(hBoxO);
		vBoxI.add(tblDocuments.getTableHeader());
		vBoxI.add(tblDocuments);
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
		// Οι επικεφαλίδες του πίνακα προσωπικού
		String[] headers = { Person.H[0], Person.H[1], "<html>Μονάδα <font color=gray size=2>(γενική με άρθρο)" };
		// Το μοντέλο του πίνακα προσωπικού
		rtmPersonnel = new ResizableHeaderTableModel<Person>(headers) {
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

	/** Που πρέπει να τοποθετηθεί ένα παράθυρο για να είναι κεντραρισμένο στην οθόνη.
	 * @param width Το πλάτος του παραθύρου
	 * @param height Το ύψος του παραθύρου
	 * @return Η θέση του παραθύρου */
	static Point getLocationScreenCentered(int width, int height) {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		return new Point((screen.width - width) / 2, (screen.height - height) / 2);
	}

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
	/** Φορτώνει ένα εικονίδιο.
	 * Χρησιμοποιείται για να φορτώνει εικονίδια για τα μενού. Τα εικονίδια είναι 16 x 16 PNG.
	 * @param name Ένα φιλικό όνομα, το οποίο παραπέμπει σε αρχείο εικόνας, εντός του πακέτου του
	 * προγράμματος. Αν είναι null, επιστρέφει ένα εικονίδιο 16 x 16, 100% διαφανές.
	 * @return Το εικονίδιο */
	static private ImageIcon loadIcon(String name) {
		//if (name == null) return new MenuBlankIcon(); else
		return new ImageIcon(getSystemResource("expenditure/" + name + ".png"));
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
				/*createMenu("ΦΕ", new JMenuItem[] {
					createMenuItem("Εφορία", (ActionEvent e) -> exportReport("ΦΕ για την Εφορία.php", null)),
					createMenuItem("Προμηθευτής", (ActionEvent e) -> exportReport("ΦΕ για τον Προμηθευτή.php", null))
				}),*/
				createMenu("Αλληλογραφία", new JMenuItem[] {
					createMenuItem("Συγκρότηση Επιτροπών", e ->
							showDraftDialogExport("Δγη Συγκρότησης Επιτροπών.php",
									data.isEmpty() ? data.unitInfo : data.getActiveExpenditure())),
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
//					null,
//					createMenuItem("Πρακτικό", (ActionEvent e) ->
//							exportReport("Πρακτικό Διαγωνισμού.php", null)),
//					createMenuItem("Εισηγητική Έκθεση", (ActionEvent e) ->
//							exportReport("Εισηγητική Έκθεση Διαγωνισμού.php", null)),
//					createMenuItem("Κατακύρωση", (ActionEvent e) ->
//						showDraftDialogExport("Δγη Κατακύρωσης Διαγωνισμού.php"))
				}),
				createMenu("Υπεύθυνες Δηλώσεις", new JMenuItem[] {
					createMenuItem("Γνωστοποίηση τραπεζικού λογαριασμού", e -> statement("statement_IBAN")),
					createMenuItem("Μη χρησιμοποίηση στρατιωτικού ως αντιπρόσωπου", e ->
							statement("statement_representative")),
					null,
					createMenuItem("Κενή", e -> saveScriptOutput(
							"<?php require('header.php'); require_once('statement.php'); statement(null); ?>\n\n}")),
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
		doc.getItem(2).setEnabled(has);				// μενού Εξαγωγή/Αλληλογραφία/Διαβιβαστικό Δαπάνης
		export.getItem(2).setEnabled(has);			// μενού Εξαγωγή/Σύμβαση
		export.getItem(3).setEnabled(has);			// μενού Εξαγωγή/Διαγωνισμοί
		export.getItem(5).setEnabled(has);			// μενού Εξαγωγή/Διάφορα
		JMenu expenditures = getJMenuBar().getMenu(4);		// μενού Δαπάνες
		expenditures.setEnabled(has);
		// Ξαναδημιουργείται το μενού με όλες τις ανοικτές δαπάνες σαν επιλογές του
		if (has) {
			expenditures.removeAll();
			ButtonGroup btg = new ButtonGroup();
			for (int z = 0; z < data.expenditures.size(); ++z) {
				boolean active = z == data.activeExpenditure;
				String f = data.expenditures.get(z).file.getName();
				if (f.endsWith(".δαπάνη")) f = f.substring(0, f.length() - 7);
				JRadioButtonMenuItem jmi = new JRadioButtonMenuItem(f, active);
				int zz = z;	// Σε lambda functions η Java θέλει οι τοπικές μεταβλητές να είναι σταθερές
				ActionListener al = e -> {
					data.activeExpenditure = zz;
					window.updatePanels();					// με το window.* γλιτώνουμε το capture
				};
				jmi.addActionListener(al);
				btg.add(jmi); expenditures.add(jmi);
			}
		}
	}

	/** Εμφάνιση διαλόγου με πληροφορίες για το πρόγραμμα και τον προγραμματιστή. */
	private void about() {
		showMessageDialog(this,
				"<html><center><b><font size=4>Στρατιωτικές Δαπάνες</font><br>" +
				"<font size=3>Έκδοση " + VERSION + "</font></b></center><br>" +
				"Προγραμματισμός: <b>Γκέσος Παύλος (ΣΣΕ 2002)</b><br>" +
				"Άδεια χρήσης: <b>BSD</b><br>" +
				"Σελίδα: <b>http://ha-expenditure.sourceforge.net/</b><br><br>" +
				"<center>Το πρόγραμμα είναι 15 ετών!</center>",
				getTitle(), PLAIN_MESSAGE);
	}

	/** Άνοιγμα της τεκμηρίωσης του προγράμματος, στο πρόγραμμα πλοήγησης. */
	private void help() {
		try { Desktop.getDesktop().open(new File(rootPath + "help/index.html")); }
		catch(RuntimeException | IOException ex) {
			showExceptionMessage(this, ex, "Πρόβλημα στην εκκίνηση του browser", null);
		}
	}

	/** Δημιουργεί μια νέα δαπάνη και την κάνει τρέχουσα στο παράθυρο του προγράμματος. */
	private void expenditureNew() {
		File s = null;
		int z = 0;
		do
			s = new File(rootPath + "Νέα Δαπάνη - " + z++ + ".δαπάνη");
		while(data.isExpenditureExist(s));
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
			Expenditure expenditure = data.getActiveExpenditure();
			File file = expenditure.file;		// Αποθήκευση: υπάρχει τουλάχιστον μια δαπάνη
			JFileChooser fc = new JFileChooser(file);
			fc.setSelectedFile(file);
			fc.setFileFilter(new ExtensionFileFilter("δαπάνη", "Αρχείο Δαπάνης"));
			if(fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
			File f = appendExpenditureExt(fc.getSelectedFile());
			// Διαδικασίες αν το αρχείο αποθήκευσης είναι διαφορετικό από το ήδη υπάρχον
			boolean otherFile = !file.equals(f);
			if (otherFile) {
				if (data.isExpenditureExist(f)) {	// Αν το νέο όνομα αρχείου ανήκει σε άλλη ανοικτή δαπάνη
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
			PhpSerializer.serialize(expenditure.save(), new FileOutputStream(f), UTF_8);
			// Αν το αρχείο αποθήκευσης είναι διαφορετικό από το ήδη υπάρχον, αλλάζει το όνομα της
			// δαπάνης στο μενού Δαπάνες
			if (otherFile) {
				expenditure.file = f;
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
			JFileChooser fc = new JFileChooser(data.isEmpty() ? null : data.getActiveExpenditure().file);
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
			if (data.isExpenditureExist(file)) {
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
		if ((flags & IMPORT_PERSONNEL) != 0)
			MainFrame.importData(expenditure.unitInfo);
		if ((flags & IMPORT_DEDUCTIONS) != 0)
			importFiltered(expenditure.invoices.stream().map(i -> i.getDeduction()), data.deductions);
		if ((flags & IMPORT_CONTRACTORS) != 0)
			importFiltered(expenditure.invoices.stream().map(i -> i.getContractor()), data.contractors);
		if ((flags & IMPORT_UNIT_DATA) != 0) data.unitInfo = expenditure.unitInfo;
	}

	/** Από τα αμετάβλητα δεδομένα, εισάγει προσωπικό στα δεδομένα του προγράμματος.
	 * Χρησιμοποιείται για την εισαγωγή προσωπικού στην αντίστοιχη καρτέλα του προγράμματος. Αν π.χ.
	 * έχει καταστραφεί το αρχείο ρυθμίσεων και έχουμε κρατημένα μερικά αρχεία δαπανών, μπορούμε να
	 * επαναφέρουμε αρκετά πράγματα.
	 * @param p Τα αμετάβλητα δεδομένα */
	static private void importData(UnitInfo p) {
		Stream.of(p.getPersonnel())
				.filter(i -> i != null && !data.personnel.contains(i))
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
			data.expenditures.remove(data.activeExpenditure);
			if (data.activeExpenditure == data.expenditures.size()) --data.activeExpenditure;
			updatePanels(); updateMenus();
		}
	}

	/** Εξάγει μια υπεύθυνη δήλωση για δικαιούχους.
	 * Αν υπάρχει ανοικτή δαπάνη, εξάγει Υπεύθυνη Δήλωση για όλους τους δικαιούχους των τιμολογίων
	 * της δαπάνης. Αν δεν υπάρχει ανοικτή δαπάνη, εμφανίζει ένα παράθυρο επιλογής ενός δικαιούχου
	 * και εξάγει μια Υπεύθυνη Δήλωση για το δικαιούχο που θα επιλεγεί.
	 * @param function Το αρχείο PHP που θα εκτελεστεί προκειμένου να εξαχθεί η Υπεύθυνη Δήλωση */
	static private void statement(String function) {
		VariableSerializable o;
		boolean many;
		if (data.isEmpty()) {
			o = (Contractor) showInputDialog(window, "Επιλέξτε το δικαιούχο για τον οποίο θα βγει η Υπεύθυνη Δήλωση",
					"Εξαγωγή Υπεύθυνης Δήλωσης", QUESTION_MESSAGE, null,
					data.contractors.toArray(new Contractor[data.contractors.size()]), null);
			if (o == null) return;
			many = false;
		} else { o = data.getActiveExpenditure(); many = true;  }
		TreeMap<String, String> env = new TreeMap<>();
		env.put("export", function);
		env.put("many", Boolean.toString(many));
		exportReport("export.php", o, env);
	}


	/** Ενεργοποιεί και απενεργοποιεί τις καρτέλες δαπανών στο παράθυρο του προγράμματος.
	 * Αν δεν υπάρχει καμία ανοικτή δαπάνη απενεργοποιεί τις καρτέλες δαπανών. */
	private void updatePanels() {
		boolean has = !data.isEmpty();
		boolean con = has && data.getActiveExpenditure().isConstruction();
		JTabbedPane j = (JTabbedPane) getContentPane().getComponent(0);
		j.setEnabledAt(0, has);	// Καρτέλα "Δαπάνη"
		j.setEnabledAt(1, has);	// Καρτέλα "Τιμολόγια"
		j.setEnabledAt(2, has);	// Καρτέλα "Συμβάσεις"
		j.setEnabledAt(3, has);	// Καρτέλα "Φύλλο Καταχώρησης"
		j.setEnabledAt(4, con);	// Καρτέλα "Εργασίες"
		// Ανανέωση πινάκων οι οποίοι ενδεχομένως να έχουν αλλάξει μέγεθος
		if (has) {
			List<Contract> contracts = data.getActiveExpenditure().contracts;
			cdmContracts.setSelectedItem(contracts.isEmpty() ? null : contracts.get(0));
			((InvoicesTableModel) tblInvoices.getModel()).fireTableDataChanged();
			rtmWorks.fireTableDataChanged();
			rtmContents.fireTableDataChanged();
			rtmReport.fireTableDataChanged(new TableModelEvent(rtmReport, 0, 6, 3));
			rtmExpenditure.fireTableDataChanged(new TableModelEvent(
					rtmExpenditure, 0, rtmExpenditure.getRowCount() - 1, 1));
		} else if (j.getSelectedIndex() < 5) j.setSelectedIndex(5);
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

	/** Αποθηκεύει τις ρυθμίσεις του προγράμματος (συμπεριλαμβάνονται οι ανοικτές δαπάνες).
	 * @throws IOException */
	static private void iniSave() throws IOException {
		PhpSerializer.serialize(data, new FileOutputStream(iniPath), UTF_8);
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

	/** Αρχικοποίηση της μηχανής PHP.
	 * @return true Αν αρχικοποιήθηκε με επιτυχία */
	static private boolean initPHP() {
		// Η διαδρομή προς το εκτελέσιμο php. Σε linux είναι απλά 'php' ενώ σε Windows είναι η
		// διαδρομή προς το php που εγκαθιστά ο installer του προγράμματος.
		String php = System.getProperty("os.name").contains("Windows") ? rootPath + "php5/php.exe" : "php";
		try { PhpScriptRunner.init(php); return true; }
		catch (ExecutionException e) {
			showExceptionMessage(null, e, "Πρόβλημα του PHP cli",
				"Πρόβλημα κατά την αρχικοποίηση του <b>PHP cli</b>.<br>Το πρόγραμμα θα τερματίσει.");
			return false;
		}
	}

	/** Σώζει αυτόματα, κάθε 5 λεπτά, τις ρυθμίσεις του προγράμματος.
	 * Συμπεριλαμβάνονται όσες δαπάνες είναι ανοικτές. */
	static private void iniAutoSave() {
		newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
				try { iniSave(); } catch(IOException ex) {}
			}, 5, 5, MINUTES);

	}

	/** Αποθηκεύει το φάκελο εκτέλεσης του προγράμματος στη μεταβλητή rootPath. */
	static private void initRootPath() {
		try {	//JDK11: Αντικατάσταση του "UTF-8" με UTF_8 και αφαίρεση των try-catch
			rootPath = decode(getSystemResource(INI).getPath()
					.replaceAll("(expenditure\\.jar!/)?expenditure\\.ini$|^(file\\:)?/", ""), "UTF-8");
		} catch(IOException e) {}
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
		iniPath = rootPath + INI;
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
				D4, D4_096, D4_15816, D4_35816, D4_23068, D4_43068, D0_06216, D0_26216, D0_13468,
				D0_33468, D14, D14_096, D14_15816, D14_35816, D14_23068, D14_43068
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

	/** Επιλογή αν θέλουμε μια διαταγή να εξαχθεί ως σχέδιο ή ως ακριβές αντίγραφο.
	 * Εμφανίζει ένα διάλογο όπου ο χρήστης επιλέγει εξαγωγή μιας διαταγής σαν σχέδιο ή σαν ακριβές
	 * αντίγραφο.
	 * @param filename Το όνομα αρχείου PHP που εξάγει τη Δγη */
	private void showDraftDialogExport(String filename) {
		showDraftDialogExport(filename, data.getActiveExpenditure());
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
	static private void exportReport(String function) { exportReport(data.getActiveExpenditure(), function); }

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
		exportReport(fname, data.getActiveExpenditure(), env);
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
		File file = data.isEmpty() ? null : data.getActiveExpenditure().file;
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
		PhpScriptRunner php = new PhpScriptRunner(rootPath + "php/", fname, null);
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
		PhpScriptRunner php = new PhpScriptRunner(rootPath + "php/", null, null);
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

	/** Το entry point του προγράμματος.
	 * @param args Οι παράμετροι του προγράμματος. Το πρόγραμμα δέχεται σαν παραμέτρους μόνο ονόματα
	 * αρχείων δαπανών για άνοιγμα. */
	public static void main(String[] args) {
		if (!serverStart(args)) return;				// Απλό return γιατί ο server δεν άνοιξε
		initRootPath();
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

	/** Εξασφαλίζει ότι μόνο μια παρουσία (instance) του προγράμματος εκτελείται.
	 * Ανοίγει και διατηρεί ανοικτή, μια συγκεκριμένη πόρτα δικτύου (την 666) για να
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
				serverSend(arg.getBytes(UTF_8));
			return false;
		}
	}
	/** Τερματίζει το server.
	 * Αυτό επιτυγχάνεται στέλνοντας το byte 0 στο server. */
	static private void serverKill() { serverSend(new byte[] { 0 }); }
	/** Εγκαθιστά έναν server στο socket localhost:666.
	 * throws IOException Αν το socket έχει δεσμευτεί από άλλο instance του προγράμματος. */
	static private void serverStart() throws IOException {
		// Το socket που μένει ανοικτό. Η πόρτα 666 στον τοπικό υπολογιστή.
		ServerSocket ss = new ServerSocket(666);
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
	/** Στέλνει δεδομένα στο socket με πόρτα 666 του τρέχοντα υπολογιστή.
	 * Χρησιμοποιείται για να αποστείλει τη δαπάνη που πρέπει να ανοίξει το τρέχον instance, αλλά
	 * δεν θα την ανοίξει επειδή υπάρχει ήδη άλλο ανοικτό instance που θα την ανοίξει εκείνο.
	 * @param a Τα δεδομένα */
	static private void serverSend(byte[] a) {
		try { new Socket("127.0.0.1", 666).getOutputStream().write(a); }
		catch(IOException e) {}
	}
}