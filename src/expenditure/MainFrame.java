package expenditure;

import static expenditure.Deduction.D0_06216;
import static expenditure.Deduction.D0_12432;
import static expenditure.Deduction.D0_26216;
import static expenditure.Deduction.D0_32432;
import static expenditure.Deduction.D14;
import static expenditure.Deduction.D14_096;
import static expenditure.Deduction.D14_15816;
import static expenditure.Deduction.D14_22032;
import static expenditure.Deduction.D14_35816;
import static expenditure.Deduction.D14_42032;
import static expenditure.Deduction.D4;
import static expenditure.Deduction.D4_096;
import static expenditure.Deduction.D4_15816;
import static expenditure.Deduction.D4_22032;
import static expenditure.Deduction.D4_35816;
import static expenditure.Deduction.D4_42032;
import static expenditure.Expenditure.FINANCING;
import java.awt.AWTEvent;
import java.awt.Color;
import static java.awt.Color.WHITE;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
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
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import static javax.swing.JOptionPane.CLOSED_OPTION;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.NO_OPTION;
import static javax.swing.JOptionPane.OK_CANCEL_OPTION;
import static javax.swing.JOptionPane.PLAIN_MESSAGE;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;
import static javax.swing.JOptionPane.WARNING_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.YES_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;
import static javax.swing.JOptionPane.showInputDialog;
import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.JOptionPane.showOptionDialog;
import javax.swing.JPanel;
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableModelEvent;
import static javax.swing.event.TableModelEvent.ALL_COLUMNS;
import static javax.swing.event.TableModelEvent.DELETE;
import static javax.swing.event.TableModelEvent.INSERT;
import javax.swing.table.TableColumnModel;
import util.ComboDataModel;
import util.ExtensionFileFilter;
import util.PhpScriptRunner;
import util.PhpScriptRunner.StdInStream;
import util.PhpSerializer;
import util.PhpSerializer.FormatException;
import util.PropertiesTableModel;
import static util.PropertiesTableModel.createTable;
import util.ResizableTableModel;
import util.ResizableTableModel.TableData;
import static util.ResizableTableModel.createTable;

/** Το κυρίως παράθυρο του προγράμματος.
 * Περιλαμβάνει τη main(). */
final public class MainFrame extends JFrame {
	/** Η διαδρομή του φακέλου του προγράμματος */
	static private String rootPath;
	/** Η διαδρομή του αρχείου ρυθμίσεων του προγράμματος */
	static private String iniPath;
	/** Η έκδοση του προγράμματος. */
	static final String VERSION = "20 Απρ 19";
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
	/** Ο πίνακας των τιμολογίων. */
	private JTable tblInvoices;
	/** Ο πίνακας των εργασιών. */
	private JTable tblWorks;
	/** Ο πίνακας των δικαιούχων. */
	private JTable tblContractors;
	/** Ο πίνακας των κρατήσεων. */
	private JTable tblDeductions;
	/** Ο πίνακας του προσωπικού. */
	private JTable tblPersonnel;
	/** Ο πίνακας με το φύλλο καταχώρησης της δαπάνης. */
	private JTable tblContents;

	/** Αρχικοποιεί το παράθυρο του προγράμματος. */
	public MainFrame() {
		super("Στρατιωτικές Δαπάνες");
		setIconImage(loadIcon("app").getImage());
		getContentPane().add(createPanels());
		updatePanels();
		setJMenuBar(createMainMenu());
		updateMenus();
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(790, 480);
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
		JComboBox cbPersonnel = new JComboBox(new ComboDataModel(() -> data.personnel, true));
		// Επιλογέας δικαιούχων / εργολάβων / προμηθευτών / ανάδοχων.
		JComboBox cbContractors = new JComboBox(new ComboDataModel(() -> data.contractors, false));
		// Επιλογέας κρατήσεων.
		JComboBox cbDeductions = new JComboBox(new ComboDataModel(() -> data.deductions, true));
		// Επιλογέας διαθέσιμων μονάδων μέτρησης, με δυνατότητα προσθήκης από το πληκτρολόγιο.
		JComboBox cbUnits = new JComboBox(new String[] {
			"τεμάχια", "Kgr", "ton", "lt", "mm", "cm", "cm²", "cm³", "m", "m²", "m³", "ρολά", "πόδια",
			"λίβρες", "ζεύγη", "στρέμματα", "Km", "Km²", "τονοχιλιόμετρα", "ώρες", "ημέρες", "μήνες"
		});
		cbUnits.setEditable(true);
		// Επιλογέας true / false.
		JComboBox cbBoolean = new JComboBox(NOYES);
		// Περίγραμμα για όλα
		Border border = createLineBorder(WHITE, 0);
		cbPersonnel  .setBorder(border);
		cbContractors.setBorder(border);
		cbDeductions .setBorder(border);
		cbUnits      .setBorder(border);
		cbBoolean    .setBorder(border);

		// ======================== ΚΑΡΤΕΛΑ «ΕΡΓΑΣΙΕΣ» ========================
		// Ρυθμίζεται ο πίνακας εργασιών
		TableData td = new TableData() {
			@Override public List<Work> get() {
				return data.isEmpty() ? null : data.getActiveExpenditure().works;
			}
			@Override public Work createNew() { return new Work(); }
		};
		ResizableTableModel rtmWorks = new ResizableTableModel(td, Arrays.copyOf(Work.H, 3));
		tblWorks = createTable(rtmWorks, true, false);
		tblWorks.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblWorks.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(cbUnits));

		// Ρυθμίζεται ο πίνακας υλικών των εργασιών
		TableData tdMaterials = new TableData() {
			@Override public List<Material> get() {
				int a = window.tblWorks.getSelectedRow();	// με το window.* γλιτώνουμε το capture
				if (a == -1 || data.isEmpty()) return null;
				List<Work> w = data.getActiveExpenditure().works;
				return a < w.size() ? w.get(a).materials : null;
			}
			@Override public Material createNew() { return new Material(); }
		};
		ResizableTableModel rtmMaterials = new ResizableTableModel(tdMaterials, Material.H);
		JTable tblMaterial = createTable(rtmMaterials, true, true);
		tblMaterial.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(cbUnits));
		// Όταν επιλέγουμε άλλη εργασία στον πίνακα εργασιών, ο πίνακας υλικών προσαρμόζεται σε αυτή
		tblWorks.getSelectionModel().addListSelectionListener((ListSelectionEvent e) ->
				((ResizableTableModel) tblMaterial.getModel()).fireTableDataChanged());
		// Δημιουργία του panel
		JSplitPane spWorks = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				new JScrollPane(tblWorks),
				new JScrollPane(tblMaterial));
		spWorks.setDividerSize(3);
		spWorks.setDividerLocation(75);

		// ======================== ΚΑΡΤΕΛΑ «ΤΙΜΟΛΟΓΙΑ» ========================
		// Ρύθμιση του πίνακα τιμολογίων
		td = new TableData() {
			@Override public List<Invoice> get() {
				return data.isEmpty() ? null : data.getActiveExpenditure().invoices;
			}
			@Override public Invoice createNew() { return new Invoice(data.getActiveExpenditure()); }
			@Override public void remove(int index) {
				List<Invoice> list = get();
				list.get(index).recalcRemove();
				list.remove(index);
			}
		};
		ResizableTableModel rtmInvoices = new ResizableTableModel(td, Arrays.copyOf(Invoice.H, 6)) {
			@Override public boolean isCellEditable(int row, int col) {
				// Αν οι αυτόματοι υπολογισμοί είναι ενεργοί, τα κελιά των κρατήσεων και ΦΕ
				// δεν επεξεργάζονται
				return (col != 3 && col != 4 || !data.getActiveExpenditure().isSmart())
						&& col != 5;	// Προσωρινός αποκλεισμός της σύμβασης
			}
		};
		JComboBox invoiceTypes = new JComboBox(Invoice.Type.values());
		JComboBox incomeTax    = new JComboBox(new Byte[] { 4, 8, 0, 1, 3, 20 });
		invoiceTypes.setBorder(border);
		incomeTax   .setBorder(border);
		tblInvoices = createTable(rtmInvoices, false, false);
		tblInvoices.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		TableColumnModel cm = tblInvoices.getColumnModel();
		cm.getColumn(1).setCellEditor(new DefaultCellEditor(invoiceTypes));
		cm.getColumn(2).setCellEditor(new DefaultCellEditor(cbContractors));
		cm.getColumn(3).setCellEditor(new DefaultCellEditor(cbDeductions));
		cm.getColumn(4).setCellEditor(new DefaultCellEditor(incomeTax));

		// Ρύθμιση του πίνακα ειδών τιμολογίου
		TableData tdItems = new TableData<InvoiceItem>() {
			@Override public List<InvoiceItem> get() {
				int a = window.tblInvoices.getSelectedRow();	// με το window.* γλιτώνουμε το capture
				if (a == -1 || data.isEmpty()) return null;
				List<Invoice> w = data.getActiveExpenditure().invoices;
				return a < w.size() ? w.get(a).items : null;
			}
			@Override public InvoiceItem createNew() {
				return new InvoiceItem(data.getActiveExpenditure().invoices.get(
						window.tblInvoices.getSelectedRow()));	// με το window.* γλιτώνουμε το capture
			}
			@Override public void remove(int index) {
				List<InvoiceItem> list = get();
				InvoiceItem ii = list.get(index);
				list.remove(index);
				ii.parent.recalcFromItems();
			}
		};
		ResizableTableModel rtmItems = new ResizableTableModel(tdItems, InvoiceItem.H);
		JTable tblItems = createTable(rtmItems, true, true);
		cm = tblItems.getColumnModel();
		JComboBox vat = new JComboBox(new Byte[] { 24, 13, 6, 0 });
		vat.setBorder(border);
		vat.setEditable(true);
		cm.getColumn(4).setCellEditor(new DefaultCellEditor(vat));
		cm.getColumn(7).setCellEditor(new DefaultCellEditor(cbUnits));

		// Προσθήκη επιπλέον επιλογής στο popup menu για μεταφορά υλικών στις εργασίες
		JPopupMenu popupMenu = tblItems.getComponentPopupMenu();
		popupMenu.addSeparator();
		popupMenu.add(createMenuItem("Αντιγραφή επιλεγμένων γραμμών στην τρέχουσα εργασία", "import",
				(ActionEvent e) -> {
					// Αντί να αποκτήσουμε πρόσβαση μέσω data, το οποίο είναι επίπονο γιατί πρέπει να
					// εντοπίσουμε τρέχουσα εργασία και τρέχον τιμολόγιο, χρησιμοποιούμε τα δεδομένα των
					// αντίστοιχων μοντέλων πινάκων
					List<Material> materials = tdMaterials.get();
					List<InvoiceItem> items = tdItems.get();
					if (materials != null && items != null) {
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

		// Το μοντέλο δεδομένων του πίνακα που εμφανίζει τα κόστη των τιμολογίων, και ο πίνακας
		ReportTableModel rtmReport = new ReportTableModel();
		JTable tblReport = PropertiesTableModel.createTable(rtmReport);
		// Όταν επιλέγουμε άλλο τιμολόγιο στον πίνακα τιμολογίων
		tblInvoices.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
			ResizableTableModel src = new ResizableTableModel(null, (List) null);	// Hack: dumb != null
			// Ο πίνακας με τα είδη τιμολογίου ανανεώνεται για να εμφανίζει τα είδη του νέου τιμολογίου
			tblItems.tableChanged(new TableModelEvent(rtmInvoices));
			// Ο πίνακας με τα κόστη ανανεώνεται για να εμφανίζει τα νέα κόστη
			((ReportTableModel) tblReport.getModel())
					.changedInvoiceSelection(window.tblInvoices.getSelectedRow());	// με το window.* γλιτώνουμε το capture
			tblReport.tableChanged(new TableModelEvent(src, 0, 6, 1));	// Στήλη τιμολογίου
			tblReport.tableChanged(new TableModelEvent(src, 0, 6, 2));	// Στήλη σύμβασης
		});
		// Όταν τροποποιούμε ένα είδος τιμολογίου στον αντίστοιχο πίνακα
		rtmItems.addTableModelListener((TableModelEvent e) -> {
			ResizableTableModel src = (ResizableTableModel) e.getSource();
			int col = e.getColumn();
			// Αλλαγή σε ένα κελί του πίνακα ειδών επηρεάζει και τα υπόλοιπα της γραμμής
			if (col != ALL_COLUMNS) {
				if (col == 0 || col == 6) return;	// Σίγουρα δεν είναι INSERT / DELETE
				tblItems.tableChanged(new TableModelEvent(src, e.getFirstRow(), e.getLastRow(), ALL_COLUMNS));
			}
			// Όταν έχουμε αυτόματο υπολογισμό, ο πίνακας τιμολογίων ανανεώνει τις στήλες κρατήσεων
			// και ΦΕ (όλα τα τιμολόγια, λόγω πιθανού ίδιου δικαιούχου - όχι μόνο το τρέχον)
			if (data.getActiveExpenditure().isSmart()) {	// έχουμε αυτόματο υπολογισμό
				int to = data.getActiveExpenditure().invoices.size();
				window.tblInvoices.tableChanged(new TableModelEvent(src, 0, to, 3));
				window.tblInvoices.tableChanged(new TableModelEvent(src, 0, to, 4));
			}
			// Ο πίνακας με τα κόστη ανανεώνεται για να εμφανίζει τα νέα κόστη
			tblReport.tableChanged(new TableModelEvent(src));
		});
		// Όταν τροποποιούμε ένα τιμολόγιο στον αντίστοιχο πίνακα
		rtmInvoices.addTableModelListener((TableModelEvent e) -> {
			ResizableTableModel src = (ResizableTableModel) e.getSource();
			int col = e.getColumn();
			// Διαγράφηκε ένα τιμολόγιο: Ο πίνακας με τα κόστη ανανεώνεται
			if (e.getType() == DELETE) tblReport.tableChanged(new TableModelEvent(src, 0, 6, 3));	// Στήλη δαπάνης
			// Αν άλλαξε η σύμβαση του τιμολογίου, ο πίνακας με τα κόστη ανανεώνεται
			else if (col == 5) tblReport.tableChanged(new TableModelEvent(rtmItems, 0, 6, 2));	// Στήλη σύμβασης
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
					tblReport.tableChanged(new TableModelEvent(src, 1, 6, 1));	// Στήλη τιμολογίου
					tblReport.tableChanged(new TableModelEvent(src, 1, 6, 2));	// Στήλη σύμβασης
					tblReport.tableChanged(new TableModelEvent(src, 1, 6, 3));	// Στήλη δαπάνης
				}
			// Αν άλλαξαν κρατήσεις ή ΦΕ, ο πίνακας με τα κόστη ανανεώνεται
			} else if (col == 3 || col == 4) {
				tblReport.tableChanged(new TableModelEvent(src, 2, 6, 1));	// Στήλη τιμολογίου
				tblReport.tableChanged(new TableModelEvent(src, 2, 6, 2));	// Στήλη σύμβασης
				tblReport.tableChanged(new TableModelEvent(src, 2, 6, 3));	// Στήλη δαπάνης
			}
		});

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
		// Ποιά πεδία του πίνακα στοιχείων Μονάδας είναι επεξεργάσιμα
		boolean[] unitEditable = {
			false, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
			false, true, true, true, true, true, true, true, true, true, true, true, true, true, true
		};
		// Οι επεξεργαστές για τα πεδία του πίνακα με τα στοιχεία της Μονάδας.
		// Πρέπει να είναι τύπου Component γιατί τα null στοιχεία, αντικαθίστανται με JTextField.
		Component[] unitEditors = {
			null, null, null, null, null, null, null, null, null, null, null, cbPersonnel, cbPersonnel,
			cbPersonnel, cbPersonnel,
			null, null, null, cbPersonnel, cbPersonnel, cbPersonnel, cbPersonnel, cbPersonnel,
			cbPersonnel, cbPersonnel, cbPersonnel, cbPersonnel, cbPersonnel, cbPersonnel, cbPersonnel
		};
		// Ρύθμιση του πίνακα με τα στοιχεία Μονάδας
		PropertiesTableModel ptm = new PropertiesTableModel((int index) -> data.unitInfo,
				unitHeader, 1, unitEditable);
		JScrollPane spUnit = new JScrollPane(createTable(ptm, unitEditors));

		// ======================== ΚΑΡΤΕΛΑ «ΣΤΟΙΧΕΙΑ ΔΑΠΑΝΗΣ» ========================
		// Επικεφαλίδα του πίνακα στοιχείων δαπάνης
		String[] expHeader = Stream.concat(Stream.of(new String[] {
			"<html><b>Στοιχεία Δαπάνης",
			Expenditure.H[0], Expenditure.H[1], Expenditure.H[2], "Ειδικός Φορέας (ΕΦ)",
			"Αναλυτικός Λογαριασμός Εσόδων/Εξόδων (ΑΛΕ)", Expenditure.H[5],
			"<html>Τίτλος <font color=gray size=2>(αιτιατική)", Expenditure.H[7], Expenditure.H[8],
			Expenditure.H[9],
			"<html><b>Αυτοματισμοί", Expenditure.H[10]
		}), Stream.of(unitHeader)).toArray(String[]::new);
		// Ποιά πεδία του πίνακα στοιχείων δαπάνης είναι επεξεργάσιμα
		boolean[] a = { false, true, true, false, true, true, true, true, true, true, true, false, true };
		boolean[] expEditable = Arrays.copyOf(a, a.length + unitEditable.length);
		System.arraycopy(unitEditable, 0, expEditable, a.length, unitEditable.length);
		// Οι επεξεργαστές για τα πεδία του πίνακα με τα στοιχεία της δαπάνης.
		// Πρέπει να είναι τύπου Component γιατί τα null στοιχεία, αντικαθίστανται με JTextField.
		Component[] expEditors = Stream.concat(Stream.of(new Component[] {
			null, null, cbBoolean, cbBoolean, null, null, new JComboBox(FINANCING), null, null, null,
			null, null, cbBoolean
		}), Stream.of(unitEditors)).toArray(Component[]::new);
		// Ρύθμιση του πίνακα με τα στοιχεία δαπάνης
		ptm = new PropertiesTableModel((int index) -> data.getActiveExpenditure(),
		expHeader, 1, expEditable);
		JScrollPane spExpenditure = new JScrollPane(createTable(ptm, expEditors));

		// Το tabbed panel πρέπει να είναι πρώτο στοιχείο της φορμας και οι Εργασίες το 4ο στοιχείο
		// του tabbed panel.
		// Μετά από κάθε προσθηκη, διαγραφή, αλλαγή σειράς να ελέγχω το MainFrame.updatePanels().
		JTabbedPane tabs = new JTabbedPane();
		tabs.addTab("Στοιχεία Δαπάνης", spExpenditure);
		tabs.addTab("Τιμολόγια", pnlInvoices);
		tabs.addTab("Συμβάσεις", new JPanel());
		tabs.addTab("Φύλλο καταχώρησης", createContentsPanel());
		tabs.addTab("Εργασίες", spWorks);
		tabs.addTab("Στοιχεία Μονάδας", spUnit);
		tabs.addTab("Δικαιούχοι", createContractorsPanel());
		tabs.addTab("Ανάλυση Κρατήσεων", createDeductionsPanel());
		tabs.addTab("Προσωπικό Μονάδας", createPersonnelPanel());

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

	/** Δημιουργεί ένα panel με τον πίνακα των αναδόχων. */
	private JScrollPane createContractorsPanel() {
		TableData td = new TableData() {
			@Override public List<Contractor> get() { return data.contractors; }
			@Override public Contractor createNew() { return new Contractor(); }
		};
		JComboBox contractorTypes = new JComboBox(Contractor.Type.values());
		contractorTypes.setBorder(createLineBorder(WHITE, 0));
		tblContractors = createTable(new ResizableTableModel(td, Contractor.H), true, true );
		TableColumnModel cm = tblContractors.getColumnModel();
		cm.getColumn(1).setCellEditor(new DefaultCellEditor(contractorTypes));

		return new JScrollPane(tblContractors);
	}

	/** Δημιουργεί ένα panel με τον πίνακα των κρατήσεων. */
	private JScrollPane createDeductionsPanel() {
		TableData<Deduction> td = new TableData() {
			@Override public List<Deduction> get() { return data.deductions; }
			@Override public Deduction createNew() { return new Deduction(); }
		};
		// Η πρώτη στήλη του πίνακα, που έχει το σύνολο, δεν πρέπει να είναι επεξεργάσιμη
		ResizableTableModel rtm = new ResizableTableModel(td, Deduction.TABLE_HEADER) {
			@Override public boolean isCellEditable(int row, int col) { return col != 0; }
		};
		tblDeductions = createTable(rtm, false, true);
		// Ενημέρωση του πίνακα ότι το κελί του συνόλου της κράτησης πρέπει να ανανεωθεί
		rtm.addTableModelListener((TableModelEvent e) ->
				window.tblDeductions.tableChanged(			// με το window.* γλιτώνουμε το capture
						new TableModelEvent((ResizableTableModel) e.getSource(),
						e.getFirstRow(), e.getLastRow(), 0)));
		// Προσθήκη popup menu στην επικεφαλίδα του πίνακα, για προσθήκη νέας επιμέρους κράτησης
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem m = new JMenuItem("Προσθήκη στήλης");
		m.addActionListener((ActionEvent e) -> {
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
					window.tblDeductions.getTableHeader().setDraggedColumn(null);
					((ResizableTableModel) window.tblDeductions.getModel()).fireTableChanged();
				}
			}
		});
		popupMenu.add(m);
		tblDeductions.getTableHeader().setComponentPopupMenu(popupMenu);

		return new JScrollPane(tblDeductions);
	}

	/** Δημιουργεί το panel με τον πίνακα του προσωπικού της Μονάδας / Υπηρεσίας. */
	private JScrollPane createPersonnelPanel() {
		String[] header = {	// Οι επικεφαλίδες του πίνακα
			Person.H[0], Person.H[1], Person.H[2], "<html>Μονάδα <font color=gray size=2>(γενική με άρθρο)"
		};
		TableData td = new TableData() {
			@Override public List<Person> get() { return data.personnel; }
			@Override public Person createNew() { return new Person(); }
		};
		ResizableTableModel rtm = new ResizableTableModel(td, header);
		tblPersonnel = createTable(rtm, true, true);
		return new JScrollPane(tblPersonnel);
	}

	/** Δημιουργεί το panel με τον πίνακα του φύλλου καταχώρησης της δαπάνης. */
	private JScrollPane createContentsPanel() {
		TableData td = new TableData() {
			@Override public List<ContentItem> get() {
				return data.isEmpty() ? null : data.getActiveExpenditure().contents;
			}
			@Override public ContentItem createNew() { return new ContentItem(); }
			@Override public void remove(int index) {
				List<ContentItem> lst = get();
				if (lst.get(index).isUserDefined()) lst.remove(index);
			}
		};
		ResizableTableModel rtm = new ResizableTableModel(td, ContentItem.H) {
			@Override public boolean isCellEditable(int row, int column) {
				List<ContentItem> ar = d.get();
				if (row == ar.size()) return true;
				ContentItem ci = ar.get(row);
				return ci.isUserDefined() || column == 1 && ci.hasChoice();
			}
		};
		tblContents = createTable(rtm, true, false);
		tblContents.getColumnModel().getColumn(1).setCellEditor(new ContentTableCellEditor(td));
		return new JScrollPane(tblContents);
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
			i.addActionListener((ActionEvent e) -> {
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
	private JMenuBar createMainMenu() {
		return createMenuBar(new JMenuItem[] {
			createMenu("Αρχείο", new JMenuItem[] {
				createMenuItem("Νέα Δαπάνη", "new", (ActionEvent e) -> newExpenditure()),
				createMenuItem("Άνοιγμα Δαπάνης...", "open", (ActionEvent e) -> openExpenditure()),
				createMenuItem("Αποθήκευση Δαπάνης...", "save", (ActionEvent e) -> saveCurrentExpenditure()),
				createMenuItem("Εισαγωγή στοιχείων...", "import", (ActionEvent e) -> importData()),
				createMenuItem("Κλείσιμο Δαπάνης", "close", (ActionEvent e) -> closeActiveExpenditure()),
				null,
				createMenuItem("Έξοδος", "exit", (ActionEvent e) ->
						dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)))
			}),
			createMenu("Εξαγωγή", new JMenuItem[] {
				createMenuItem("Δαπάνη", (ActionEvent e) -> {
					TreeMap<String, String> env = new TreeMap<>();
					if (data.onlyOnce) env.put("one", "true");
					exportReport("Δαπάνη.php", env);
				}),
				createMenu("ΦΕ", new JMenuItem[] {
					createMenuItem("Εφορία", (ActionEvent e) -> exportReport("ΦΕ για την Εφορία.php")),
					createMenuItem("Προμηθευτής", (ActionEvent e) -> exportReport("ΦΕ για τον Προμηθευτή.php"))
				}),
				createMenu("Αλληλογραφία", new JMenuItem[] {
					createMenuItem("Συγκρότηση Επιτροπών", (ActionEvent e) ->
							showDraftDialogExport("Δγη Συγκρότησης Επιτροπών.php")),
					createMenu("Διαγωνισμοί", new JMenuItem[] {
						createMenuItem("Διακήρυξη", (ActionEvent e) ->
							showDraftDialogExport("Δγη Διακήρυξης Διαγωνισμού.php")),
						null,
						createMenuItem("Πρακτικό", (ActionEvent e) ->
								exportReport("Πρακτικό Διαγωνισμού.php")),
						createMenuItem("Εισηγητική Έκθεση", (ActionEvent e) ->
								exportReport("Εισηγητική Έκθεση Διαγωνισμού.php")),
						createMenuItem("Κατακύρωση", (ActionEvent e) ->
							showDraftDialogExport("Δγη Κατακύρωσης Διαγωνισμού.php"))
					}),
					createMenuItem("Διαβιβαστικό Δαπάνης", (ActionEvent e) ->
							showDraftDialogExport("Διαβιβαστικό Δαπάνης.php")),
					createMenuItem("Εκθεση Απατούμενης Δαπάνης", (ActionEvent e) ->
							showDraftDialogExport("Έκθεση Απαιτούμενης Δαπάνης.php"))
				}),
				createMenu("Διάφορα", new JMenuItem[] {
					createMenuItem("Κατάσταση Πληρωμών", null),
					createMenuItem("Πρόχειρη Λίστα Τιμολογίων", (ActionEvent e) ->
							exportReport("Πρόχειρη Λίστα Τιμολογίων.php")),
					createMenuItem("Απόδειξη για Προκαταβολή",
							(ActionEvent e) -> exportReport("Απόδειξη για Προκαταβολή.php"))
				})
			}),
			createMenu("Ρυθμίσεις", new JMenuItem[] {
				createMenuItem("Οδηγός Τιμολογίου", "wizard",
						(ActionEvent e) -> new InvoiceWizardDialog(this).setVisible(true)),
				null,
				createMenuWithSkins("Κέλυφος", "skins"),
				createMenuItem("Ένα Αντίγραφο", "only_one", data.onlyOnce,
						(ActionEvent e) -> data.onlyOnce = !data.onlyOnce),
			}),
			createMenu("Δαπάνες", new JMenuItem[] {}),
			createMenu("Βοήθεια", new JMenuItem[] {
				createMenuItem("Εγχειρίδιο", "help", (ActionEvent e) -> help()),
				createMenuItem("Περί...", "about", (ActionEvent e) -> about())
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
		export.setEnabled(has);
		JMenu expenditures = getJMenuBar().getMenu(3);		// μενού Δαπάνες
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
				ActionListener al = (ActionEvent e) -> {
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
				"<center>Το πρόγραμμα είναι 16 ετών!</center>",
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
	private void newExpenditure() {
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
	private void saveCurrentExpenditure() {
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
	private void openExpenditure() {
		try {
			// Ο διάλογος Άνοιγμα... θα ανοίξει στο φάκελο που είναι αποθηκευμένη η τρέχουσα δαπάνη
			JFileChooser fc = new JFileChooser(data.isEmpty() ? null : data.getActiveExpenditure().file);
			fc.setFileFilter(new ExtensionFileFilter("δαπάνη", "Αρχείο Δαπάνης"));
			int returnVal = fc.showOpenDialog(this);
			if(returnVal != JFileChooser.APPROVE_OPTION) return;
			openExpenditure(this, appendExpenditureExt(fc.getSelectedFile()));
		} catch (IOException ex) {}
	}

	/** Ανοίγει μια δαπάνη και την κάνει τρέχουσα.
	 * Ενημερώνει το παράθυρο του προγράμματος.
	 * @param frame Το παράθυρο του προγράμματος, προκειμένου να ενημερωθεί με την νέα τρέχουσα δαπάνη
	 * @param file Το αρχείο της δαπάνης */
	static private void openExpenditure(MainFrame frame, File file) {
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
		TableModelEvent e = new TableModelEvent(tblDeductions.getModel());
		tblContractors.tableChanged(e);
		tblDeductions.tableChanged(e);
		tblPersonnel.tableChanged(e);
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
				importData(loadIni(is), flags);
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
			MainFrame.importData(expenditure.unitInfo);
			//TODO: τυχόν άλλα πρόσωπα στα μεταβλητά στοιχεία δαπάνης
		}
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
	private void closeActiveExpenditure() {
		if (YES_OPTION == showConfirmDialog(this,
				"<html>Να κλείσω την τρέχουσα δαπάνη;", "Κλείσιμο Δαπάνης",
				YES_NO_OPTION, WARNING_MESSAGE)) {
			data.expenditures.remove(data.activeExpenditure);
			if (data.activeExpenditure == data.expenditures.size()) --data.activeExpenditure;
			updatePanels(); updateMenus();
		}
	}

	/** Ενεργοποιεί και απενεργοποιεί τις καρτέλες δαπανών στο παράθυρο του προγράμματος.
	 * Αν δεν υπάρχει καμία ανοικτή δαπάνη απενεργοποιεί τις καρτέλες δαπανών. */
	private void updatePanels() {
		JTabbedPane j = (JTabbedPane) getContentPane().getComponent(0);
		for (int z = 0; z < 4; z++)
			j.setEnabledAt(z, !data.isEmpty());
		if (data.isEmpty() && j.getSelectedIndex() < 5) j.setSelectedIndex(5);
		// Ανανέωση πινάκων οι οποίοι ενδεχομένως να έχουν αλλάξει μέγεθος
		tblInvoices.tableChanged(new TableModelEvent(tblInvoices.getModel()));
		tblWorks.tableChanged(new TableModelEvent(tblWorks.getModel()));
		tblContents.tableChanged(new TableModelEvent(tblContents.getModel()));
		j.setEnabledAt(2, false);	//TODO: Προσωρινό
	}

	@Override protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			try { saveIni(); }
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
	static private void saveIni() throws IOException {
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

	/** Εμφάνιση παραθύρου σφάλματος.
	 * @param c Το πατρικό παράθυρο (ή στοιχείο αυτού). Μπορεί να είναι null.
	 * @param e Η εξαίρεση (exception) που συνέβη. Μπορεί να είναι null.
	 * @param title Ο τίτλος του παραθύρου σφάλματος
	 * @param info Πληροφορίες για το σφάλμα. Μπορεί να είναι null. */
	static private void showExceptionMessage(Component c, Exception e, String title, String info) {
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
	static private AppData loadDefaultIni() {
		try { return loadIni(getSystemResourceAsStream(INI)); }
		catch(Exception e) {
			showExceptionMessage(null, e, "Πρόβλημα",
				"Πρόβλημα κατά τη φόρτωση του default <b>" + INI + "</b>.");
			return new AppData();
		}
	}

	/** Φορτώνει το αρχείο ρυθμίσεων του προγράμματος.
	 * @param is Ένα stream που περιέχει το αρχείο ρυθμίσεων του προγράμματος
	 * @return Τα δεδομένα του προγράμματος */
	static private AppData loadIni(InputStream is) throws Exception {
		return new AppData(PhpSerializer.unserialize(is, UTF_8));
	}

	/** Φορτώνει το default αρχείο ρυθμίσεων του προγράμματος.
	 * Δεν πρόκειται για μια καθημερινή λειτουργία. Συμβαίνει μόνο όταν κάτι πολύ καταστροφικό έχει
	 * συμβεί. Π.χ. Το κανονικό αρχείο ρυθμίσεων δεν περιέχει σωστά δεδομένα.
	 * <p>Η καταστροφή που απαγορεύει τη φόρτωση του κανονικου αρχείου ρυθμίσεων, εμφανίζεται σε
	 * ένα παράθυρο σφάλματος.
	 * @return Τα δεδομένα του προγράμματος */
	static private AppData loadDefaultIniWarn(Exception e) {
		showExceptionMessage(null, e, "Πρόβλημα",
				"Πρόβλημα κατά τη φόρτωση του <b>" + INI + "</b><br>"
				+ "Θα φορτώσω τη default έκδοσή του.");
		return loadDefaultIni();
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
	static private void autosaveIni() {
		newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
				try { saveIni(); } catch(IOException ex) {}
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
	static private void loadIni() {
		// Πρώτα ψάχνει το αρχείο ρυθμίσεων στο φάκελο εγκατάστασης (φορητή λειτουργία)
		iniPath = rootPath + INI;
		try { data = loadIni(new FileInputStream(iniPath)); }
		catch(FileNotFoundException ex) {
			// Αν αποτύχει, ψάχνει το αρχείο ρυθμίσεων στο φάκελο του χρήστη (κανονική λειτουργία)
			iniPath = System.getProperty("user.home") + "/" + INI;
			try { data = loadIni(new FileInputStream(iniPath)); }
			catch(FileNotFoundException e) { data = loadDefaultIni(); }
			catch(Exception e) { data = loadDefaultIniWarn(e); }
		} catch(Exception ex) { data = loadDefaultIniWarn(ex); }
	}

	/** Εισάγει τις κρατήσεις στα δεδομένα του προγράμματος.
	 * Όταν εγκαθιστούμε νέα έκδοση του προγράμματος, την πρώτη φορά που θα εκτελεστεί, θα εισάγει
	 * τυχόν νέες κρατήσεις που έχουν θεσμοθετηθεί. */
	static private void importNewDeductions() {
		if (!VERSION.equals(data.version)) {
			Deduction[] deductions = {
				D4, D4_096, D4_15816, D4_35816, D4_22032, D4_42032, D0_06216, D0_26216, D0_12432,
				D0_32432, D14, D14_096, D14_15816, D14_35816, D14_22032, D14_42032
			};
			importFiltered(Stream.of(deductions), data.deductions);
			data.version = VERSION;	// ανανέωση της έκδοσης στις ρυθμίσεις στην τρέχουσα
		}
	}

	/** Φορτώνει δαπάνες στο πρόγραμμα.
	 * @param filenames Τα ονόματα αρχείου των δαπανών για φόρτωση */
	static private void openExpenditures(String[] filenames) {
		for (String s : filenames)
			openExpenditure(null, new File(s));
	}

	/** Επιλογή αν θέλουμε μια διαταγή να εξαχθεί ως σχέδιο ή ως ακριβές αντίγραφο.
	 * Εμφανίζει ένα διάλογο όπου ο χρήστης επιλέγει εξαγωγή μιας διαταγής σαν σχέδιο ή σαν ακριβές
	 * αντίγραφο.
	 * @param filename Το όνομα αρχείου PHP που εξάγει τη Δγη */
	private void showDraftDialogExport(String filename) {
		TreeMap<String, String> env = new TreeMap();
		final String[] a = { "Ακριβές Αντίγραφο", "Σχέδιο" };
		int b = showOptionDialog(this, "Επιλέξτε σαν τι θα βγεί η διαταγή.",
				"Επιλογή", OK_CANCEL_OPTION, QUESTION_MESSAGE, null, a, a[0]);
		switch(b) {
			case CLOSED_OPTION: return;
			case 1: env.put("draft", "true");	// χωρίς break
			default: exportReport(filename, env);
		}
	}

	/** Εξάγει ένα PHP πρότυπο.
	 * Εκτελεί ένα PHP script και εξάγει ένα αρχείο κειμένου RTF. Ζητά από το χρήστη με διάλογο, που
	 * να το αποθηκεύσει ή εμφανίζει τυχόν λάθη που προέκυψαν κατά τη διαδικασία.
	 * @param fname Το όνομα αρχείου του PHP προτύπου */
	private void exportReport(String fname) { exportReport(fname, null); }

	/** Εξάγει ένα PHP πρότυπο.
	 * Εκτελεί ένα PHP script και εξάγει ένα αρχείο κειμένου RTF. Ζητά από το χρήστη με διάλογο, που
	 * να το αποθηκεύσει ή εμφανίζει τυχόν λάθη που προέκυψαν κατά τη διαδικασία.
	 * @param fname Το όνομα αρχείου του PHP προτύπου
	 * @param env Οι μεταβλητές περιβάλλοντος για το PHP script που θα δημιουργήσει το εξαγόμενο
	 * αρχείο. Το null επιτρέπεται. */
	private void exportReport(String fname, Map<String, String> env) {
		// Αρχικοποίηση των ρυθμίσεων εκτέλεσης του PHP script
		PhpScriptRunner php = new PhpScriptRunner(rootPath + "php/", fname, null);
		if (env != null) php.getEnvironment().putAll(env);
		try {
			// Εκτέλεση του PHP script με την αντίστοιχη είσοδο και έξοδο
			StdInStream sin = (OutputStream os) -> {
				PhpSerializer.serialize(data.getActiveExpenditure(), os, GREEK);
				os.close();
			};
			PhpScriptRunner.StdOut out = new PhpScriptRunner.StdOut(), err = new PhpScriptRunner.StdOut();
			int errCode = php.exec(sin, out, err);
			// Έλεγχος σφαλμάτων εκτέλεσης του PHP script
			byte[] a = err.join();
			String error = a != null ? new String(a, GREEK) : "";
			if (errCode != 0) error += "<html><font color=red><b>Το php script τερμάτισε με σοβαρό σφάλμα";
			if (!error.isEmpty()) throw new Exception(error);	// δε χρειάζεται να περιμένουμε το stdout
			// Διάλογος αποθήκευσης του αρχείου δαπάνης
			JFileChooser fc = new JFileChooser(data.getActiveExpenditure().file);
			fc.setFileFilter(new ExtensionFileFilter("rtf", "Rich Text"));
			int returnVal = fc.showSaveDialog(this);
			if(returnVal != JFileChooser.APPROVE_OPTION) return;
			File file = appendExt(fc.getSelectedFile(), ".rtf");
			a = out.join();	// πριν δημιουργηθεί το αρχείο προκειμένου να πετάξει τυχόν exception
			try (FileOutputStream f = new FileOutputStream(file)) {
				f.write(a);
				f.close();
			}
			try { Desktop.getDesktop().open(file); }
			catch (RuntimeException | IOException ex) {}
		} catch (Exception e) { showError(e.getMessage()); }
	}

	/** Εμφανίζει ένα διάλογο με τα σφάλματα της εξαγωγής αρχείου από το PHP script.
	 * @param err Το κείμενο του σφάλματος αποτελούμενο από αριθμό γραμμών. */
	private void showError(String err) {
		JDialog dlg = new JDialog(this, "Εμφάνιση σφαλμάτων εκτέλεσης του PHP Script", true);
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
		loadIni();									// Οι ρυθμίσεις του προγράμματος
		importNewDeductions();		// Εισάγει τυχόν νέες κρατήσεις κατά την πρώτη εκτέλεση νέας έκδοσης
		openExpenditures(args);		// Ανοίγει δαπάνες που έχουν δωθεί ως παράμετροι
		setSkin();					// Ορίζει το κέλυφος του παραθύρου (L&F)
		window = new MainFrame();	// Δημιουργία του παραθύρου του προγράμματος
		// Όχι μέσα στη MainFrame() γιατί θα υπάρξουν αναφορές στο window πριν αυτό τεθεί
		window.setVisible(true);	// Εμφάνιση του παραθύρου του προγράμματος
		autosaveIni();				// Αποθήκευση αλλαγών κάθε 5 λεπτά
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
					openExpenditure(window, new File(new String(buf, UTF_8)));	// Ανοιγμα δαπάνης
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

//	/** Ένα 100% διαφανές εικονίδιο 16 x 16.
//	 * Χρησιμοποιείται σαν padding σε ένα JMenuItem, όταν τα άλλα JMenuItem του ιδίου JMenu, έχουν
//	 * εικονίδια, προκειμένου να στοιχίζονται όλα το ίδιο. */
//	static private class MenuBlankIcon implements Icon {
//		@Override public int getIconHeight() { return 16; }
//		@Override public int getIconWidth() { return 16; }
//		@Override public void paintIcon(Component c, Graphics g, int x, int y) {}
//	}
}