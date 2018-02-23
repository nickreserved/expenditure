package cost;

import java.io.*;
import java.util.*;
import java.net.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.UIManager.*;

import common.*;


public class MainFrame extends JFrame implements ActionListener {
	static private final String[] mnu = {
		"file", null, null, "Αρχείο",
			"new", "file", "new", "Νέα Δαπάνη",
			"open", "file", "open", "’νοιγμα Δαπάνης...",
			"save", "file", "save", "Αποθήκευση Δαπάνης...",
			"close", "file", "close", "Κλείσιμο Δαπάνης",
			null, "file", null, null,
			"loadini", "file", "loadini", "’νοιγμα Σταθερών",
			"saveini", "file", "saveini", "Αποθήκευση Σταθερών",
			null, "file", null, null,
			"exit", "file", "exit", "Έξοδος",
		"export", null, null, "Εξαγωγή",
			"cost", "export", null, "Δαπάνη",
			"fe", "export", null, "ΦΕ",
				"taxis", "fe", null, "Εφορία",
				"provider", "fe", null, "Προμηθευτής",
			"mail", "export", null, "Αλληλογραφία",
				"committee", "mail", null, "Συγκρότηση Επιτροπών",
					"committee_draft", "committee", null, "Σχέδιο",
					"committee_nodraft", "committee", null, "Ακριβές Αντίγραφο",
				"route_slip", "mail", null, "Διαβιβαστικό Δαπάνης",
					"route_slip_draft", "route_slip", null, "Σχέδιο",
					"route_slip_nodraft", "route_slip", null, "Ακριβές Αντίγραφο",
				"prereport", "mail", null, "Έκθεση Απαιτούμενης Δαπάνης",
					"prereport_draft", "prereport", null, "Σχέδιο",
					"prereport_nodraft", "prereport", null, "Ακριβές Αντίγραφο",
			"other", "export", null, "Διάφορα",
				"hold", "other", null, "Ανάλυση Κρατήσεων",
				"bills", "other", null, "Λίστα Τιμολογίων",
		"costs", null, null, "Δαπάνες",
		"help", null, null, "Βοήθεια",
			"help_open", "help", "help", "Βοήθεια"
	};
	private static JMenuItem[] menu = new JMenuItem[mnu.length / 4];
	
	public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public static String rootPath;
	static protected HashObject data;
	static protected Hashtable<String, HashObject> costs = new Hashtable<String, HashObject>();
	static protected String currentCost;
	
	static protected MainFrame ths;
	static private Holds holds = new Holds();
	static private Providers providers = new Providers();
	static private Men men = new Men();
	static private Bills bills = new Bills();
	static private CostData costData = new CostData();
	static private StaticData staticData = new StaticData();
	
	
	public MainFrame() {
		super("Στρατιωτικές Δαπάνες");
		ths = this;
		
		this.setIconImage(new ImageIcon(ClassLoader.getSystemResource("cost/app.png")).getImage());
		
		JTabbedPane mainTab = new JTabbedPane();
		mainTab.addTab("Στοιχεία Δαπάνης", costData);
		mainTab.addTab("Τιμολόγια", bills);
		mainTab.addTab("Αμετάβλητα Στοιχεία", staticData);
		//mainTab.addTab("Εργασίες", new Works());
		mainTab.addTab("Προμηθευτές", providers);
		mainTab.addTab("Κρατήσεις", holds);
		mainTab.addTab("Προσωπικό Μονάδας", men);
		getContentPane().add(mainTab);
		Color c = Color.decode("#b0d0b0");
		mainTab.setBackgroundAt(0, c);
		mainTab.setBackgroundAt(1, c);
		mainTab.setBackgroundAt(2, c);
		//c = Color.decode("#e0b0b0");
		//mainTab.setBackgroundAt(3, c);
		c = Color.decode("#e0e0b0");
		mainTab.setBackgroundAt(3, c);
		mainTab.setBackgroundAt(4, c);
		mainTab.setBackgroundAt(5, c);
		//mainTab.setBackgroundAt(7, Color.decode("#b0d0e0"));
		
		updatePanels();
		
		setJMenuBar(createMenus(new JMenuBar()));
		updateMenus();
		
		setSize(635, 450);
		setLocation((screenSize.width - getWidth()) / 2, (screenSize.height - getHeight()) / 2);
		setVisible(true);
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
	}
	
	private final JMenuBar createMenus(JMenuBar jtb) {
		for(int z = 0, c = 0; z < mnu.length; z += 4)
			if (mnu[z] == null) ((JMenu) getMenuFromName(mnu[z + 1])).addSeparator();
			else {
			if (mnu[z + 1] == null || z + 5 < mnu.length && mnu[z + 5] == mnu[z])
				menu[c] = new JMenu(mnu[z + 3]);
			else {
				menu[c] = new JMenuItem(mnu[z + 3]);
				menu[c].addActionListener(this);
			}
			menu[c].setActionCommand(mnu[z]);
			if (mnu[z + 2] != null) {
				if (mnu[z + 2] == "") menu[c].setIcon(new MenuBlankIcon());
				else menu[c].setIcon(new ImageIcon(ClassLoader.getSystemResource("cost/" + mnu[z + 2] + ".png")));
			}
			if (mnu[z + 1] == null) jtb.add(menu[c++]);
			else ((JMenu) getMenuFromName(mnu[z + 1])).add(menu[c++]);
			}
		return jtb;
	}
	
	static private final JMenuItem getMenuFromName(String s) {
		for(int z = 0; menu[z] != null; z++)
			if (s.equals(menu[z].getActionCommand())) return menu[z];
		return null;
	}
	
	public void newCost() {
		for(int c = 0;; c++) {
			String s = "Νέα Δαπάνη - " + c + ".cost";
			if (!costs.containsKey(s)) {
				currentCost = s;
				HashString2Object hso = new HashString2Object();
				hso.putAll((Map) data.get("ΑμετάβληταΣτοιχείαΔαπάνης"));
				costs.put(currentCost, hso);
				setCostClasses(hso);
				updatePanels();
				updateMenus();
				return;
			}
		}
	}
	
	private void setCostClasses(HashString2Object h) {
		h.classes.put("Ποσό", Double.class);
	}
	
	public void saveCost() {
		JFileChooser fc = new JFileChooser(currentCost);
		fc.setSelectedFile(new File(currentCost));
		fc.setFileFilter(new ExtensionFileFilter("cost", "Αρχείο Δαπάνης"));
		int returnVal = fc.showSaveDialog(this);
		if(returnVal != JFileChooser.APPROVE_OPTION) return;
		String s = fc.getSelectedFile().getPath();
		if (!s.endsWith(".cost")) s += ".cost";
		LoadSaveFile.save(s, (Saveable) costs.get(currentCost));
		costs.put(s, costs.remove(currentCost));
		currentCost = s;
		updateMenus();
	}
	
	private void openCost() {
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new ExtensionFileFilter("cost", "Αρχείο Δαπάνης"));
		int returnVal = fc.showOpenDialog(this);
		if(returnVal != JFileChooser.APPROVE_OPTION) return;
		String s = fc.getSelectedFile().getPath();
		if (!s.endsWith(".cost")) s += ".cost";
		openCost(s);
	}
	
	private void openCost(String file) {
		HashString2Object o = null;
		try {
			o = (HashString2Object) TreeFileLoader.loadFile(file);
		} catch (Exception e) {}
		if (o == null) return;
		currentCost = file;
		costs.put(currentCost, o);
		setCostClasses((HashString2Object) o);
		updateMenus();
		updatePanels();
	}
	
	private boolean closeCost() {
		if (currentCost == null) return true;
		if (JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog(this,
				"<html>Να κλείσω την δαπάνη<br><b>" + currentCost + "</b>;",
				"Κλείσιμο Δαπάνης", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE))
			return false;
		costs.remove(currentCost);
		try {
			currentCost = costs.keys().nextElement().toString();
		} catch (Exception e) {
			currentCost = null;
		}
		updatePanels();
		updateMenus();
		return true;
	}
	
	private void updatePanels() {
		costData.repaint();
		staticData.repaint();
		bills.updateObject();
		holds.updateObject();
		men.updateObject();
		providers.updateObject();
	}
	
	private void updateMenus() {
		JMenu window = (JMenu) getMenuFromName("costs");
		JMenu export = (JMenu) getMenuFromName("export");
		window.removeAll();
		Enumeration en = costs.keys();
		while (en.hasMoreElements()) {
			String m = en.nextElement().toString();
			JRadioButtonMenuItem jmi = new JRadioButtonMenuItem(m, m.equals(currentCost));
			jmi.addActionListener(this);
			window.add(jmi);
		}
		getMenuFromName("save").setEnabled(currentCost != null);
		getMenuFromName("close").setEnabled(currentCost != null);
		window.setEnabled(currentCost != null);
		export.setEnabled(currentCost != null);
	}
	
	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			while (currentCost != null)
				if (!closeCost()) return;
			System.exit(0);
		}
		super.processWindowEvent(e);
	}
	
	static public void setSkin(String skin) {
		try {
			LookAndFeelInfo[] laf = UIManager.getInstalledLookAndFeels();
			for (int z = 0; z < laf.length; z++)
				if (laf[z].getName().equalsIgnoreCase(skin)) {
				UIManager.setLookAndFeel(laf[z].getClassName());
				return;
				}
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
		}
	}
	
	public static void main(String[] args) throws Exception {
		setSkin(args.length > 0 && !args[0].endsWith(".cost") ? args[0] : null);
		PhpScriptRunner.init(null);
		rootPath = URLDecoder.decode(ClassLoader.getSystemResource("cost/MainFrame.class").getPath().
				replaceAll("(Cost\\.jar!/)?cost/MainFrame\\.class$|^(file\\:)?/", ""), "UTF-8");
		data = (HashObject) TreeFileLoader.loadFile(rootPath + "main.ini");
		MainFrame m = new MainFrame();
		String a = null;
		for (int z = 0; z < args.length; z++)
			if (args[z].endsWith(".cost")) m.openCost(args[z]);
	}
	
	
	// ================================ ActionListener ==================================== //
	
	public void actionPerformed(ActionEvent e) {
		String ac = e.getActionCommand();
		JMenuItem j = (JMenuItem) e.getSource();

		// if we must output a draft order
		Map<String, String> env = new Hashtable();
		env.put("draft", "true");

		if (j instanceof JRadioButtonMenuItem) {
			currentCost = j.getText();
			updatePanels();
			updateMenus();
		} else if (ac == "new") newCost();
		else if (ac == "open") openCost();
		else if (ac == "save") saveCost();
		else if (ac == "close") closeCost();
		else if (ac == "loadini") {
			data = (HashObject) TreeFileLoader.loadFile(rootPath + "main.ini");
			updatePanels();
		} else if (ac == "saveini") LoadSaveFile.save(rootPath + "main.ini", data);
		else if (ac == "exit") dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		else if (ac == "cost") ExportReport.exportReport("templates/cost.php");
		else if (ac == "taxis") ExportReport.exportReport("templates/fe_tax.php");
		else if (ac == "provider") ExportReport.exportReport("templates/fe_provider.php");
		else if (ac == "prereport_draft") ExportReport.exportReport("templates/prereport.php", env);
		else if (ac == "prereport_nodraft") ExportReport.exportReport("templates/prereport.php");
		else if (ac == "hold") ExportReport.exportReport("templates/holds.php");
		else if (ac == "bills") ExportReport.exportReport("templates/bills.php");
		else if (ac == "committee_draft") ExportReport.exportReport("templates/order00.php", env);
		else if (ac == "committee_nodraft") ExportReport.exportReport("templates/order00.php");
		else if (ac == "route_slip_draft") ExportReport.exportReport("templates/route_slip.php", env);
		else if (ac == "route_slip_nodraft") ExportReport.exportReport("templates/route_slip.php");
		else if (ac == "help_open") {
			try {
				BrowserLauncher.openURL(rootPath + "help/index.html");
			} catch(Exception ex) {
				Functions.showExceptionMessage(ex, "Πρόβλημα στην εκκίνηση του browser");
			}
		}
	}

	
	
	class MenuBlankIcon implements Icon {
		public int getIconHeight() { return 16; }
		public int getIconWidth() { return 16; }
		public void paintIcon(Component c, Graphics g, int x, int y) {}
	}
}