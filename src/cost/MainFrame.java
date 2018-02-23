package cost;

import common.ExtensionFileFilter;
import common.Functions;
import common.HashObject;
import common.IteratorHashObject;
import common.LoadSaveFile;
import common.PhpScriptRunner;
import common.Saveable;
import common.TreeFileLoader;
import common.VectorObject;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

public class MainFrame extends JFrame implements ActionListener {
	private static JMenuItem[] menu;

	public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public static String rootPath;
	public static String ini = System.getProperty("user.home") + "/cost.ini";
	static protected HashObject data;
	static protected IteratorHashObject costs;
	static protected MainFrame ths;
	static protected CostWizardDialog cwf;

	public MainFrame() {
		super("Στρατιωτικές Δαπάνες 1.6.7b");
		setIconImage(new ImageIcon(ClassLoader.getSystemResource("cost/app.png")).getImage());

		// Πρέπει να δημιουργηθούν πρώτα!
		Providers prov = new Providers();
		Men men = new Men();
		Holds holds = new Holds();
		Contents contents = new Contents();

		// Το tabbed panel πρέπει να είναι πρώτο στοιχείο της φορμας και
		// οι Εργασίες το 4ο στοιχείο του tabbed panel.
		// Ειδάλλως στο Bills θα έχουμε προβλήματα.
		// Και στην ενεργοποίηση-απενεργοποίηση καρτελών όταν ανοίγει-κλείνει δαπάνη.
		JTabbedPane mainTab = new JTabbedPane();
		mainTab.addTab("Στοιχεία Δαπάνης", new CostData(contents));
		mainTab.addTab("Τιμολόγια", new Bills());
		mainTab.addTab("Φύλλο καταχώρησης", contents);
		mainTab.addTab("Εργασίες", new Works());
		mainTab.addTab("Αμετάβλητα Στοιχεία", new StaticData());
		mainTab.addTab("Προμηθευτές", prov);
		mainTab.addTab("Ανάλυση Κρατήσεων", holds);
		mainTab.addTab("Προσωπικό Μονάδας", men);

		getContentPane().add(mainTab);
		Color c = Color.decode("#b0d0b0");
		mainTab.setBackgroundAt(0, c);
		mainTab.setBackgroundAt(1, c);
		mainTab.setBackgroundAt(2, c);
		mainTab.setBackgroundAt(3, c);
		c = Color.decode("#e0e0b0");
		mainTab.setBackgroundAt(4, c);
		mainTab.setBackgroundAt(5, c);
		mainTab.setBackgroundAt(6, c);
		mainTab.setBackgroundAt(7, c);

		updatePanels();

		setJMenuBar(createMenus(new JMenuBar()));
		updateMenus();
		addOptionsMenu();

		setSize(750, 450);
		setLocation((screenSize.width - getWidth()) / 2, (screenSize.height - getHeight()) / 2);
		setVisible(true);
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
	}

	
	private JMenuBar createMenus(JMenuBar jtb) {
		final String[] mnu = {
			"Αρχείο", null, null,
				"Νέα Δαπάνη", "Αρχείο", "new",
				"’νοιγμα Δαπάνης...", "Αρχείο", "open",
				"Αποθήκευση Δαπάνης...", "Αρχείο", "save",
				"Κλείσιμο Δαπάνης", "Αρχείο", "close",
				null, "Αρχείο", null,
				"Εισαγωγή στοιχείων...", "Αρχείο", "import",
				null, "Αρχείο", null,
				"Έξοδος", "Αρχείο", "exit",
			"Εξαγωγή", null, null,
				"Δαπάνη", "Εξαγωγή", null,
				"ΦΕ", "Εξαγωγή", null,
					"Εφορία", "ΦΕ", null,
					"Προμηθευτής", "ΦΕ", null,
				"Αλληλογραφία", "Εξαγωγή", null,
					"Συγκρότηση Επιτροπών", "Αλληλογραφία", null,
					"Διαγωνισμοί", "Αλληλογραφία", null,
						"Διακήρυξη", "Διαγωνισμοί", null,
						null, "Διαγωνισμοί", null,
						"Πρακτικό", "Διαγωνισμοί", null,
						"Εισηγητική Έκθεση", "Διαγωνισμοί", null,
						"Κατακύρωση", "Διαγωνισμοί", null,
					"Διαβιβαστικό Δαπάνης", "Αλληλογραφία", null,
					"Έκθεση Απαιτούμενης Δαπάνης", "Αλληλογραφία", null,
				"Διάφορα", "Εξαγωγή", null,
					"Ανάλυση Κρατήσεων", "Διάφορα", null,
					"Πρόχειρη Λίστα Τιμολογίων", "Διάφορα", null,
					"Απόδειξη για Προκαταβολή", "Διάφορα", null,
			"Ρυθμίσεις", null, null,
				"Οδηγός Τιμολογίου", "Ρυθμίσεις", "wizard",
				null, "Ρυθμίσεις", null,
				"Κέλυφος ", "Ρυθμίσεις", "skins",
			"Δαπάνες", null, null,
			"Βοήθεια", null, null,
				"Εγχειρίδιο", "Βοήθεια", "help",
				"Περί...", "Βοήθεια", "about"
		};
		menu = new JMenuItem[mnu.length / 3];
			
		for(int z = 0, c = 0; z < mnu.length; z += 3)
			if (mnu[z] == null) ((JMenu) getMenuFromName(mnu[z + 1])).addSeparator();
			else {
			if (mnu[z + 1] == null || mnu[z].endsWith(" ") || z + 4 < mnu.length && mnu[z + 4] == mnu[z])
				menu[c] = new JMenu(mnu[z]);
			else {
				menu[c] = new JMenuItem(mnu[z]);
				menu[c].addActionListener(this);
			}
			if (mnu[z + 2] != null) {
				/*if (mnu[z + 2] == "") menu[c].setIcon(new MenuBlankIcon());
				else*/ menu[c].setIcon(new ImageIcon(ClassLoader.getSystemResource("cost/" + mnu[z + 2] + ".png")));
			}
			if (mnu[z + 1] == null) jtb.add(menu[c++]);
			else ((JMenu) getMenuFromName(mnu[z + 1])).add(menu[c++]);
			}
		return jtb;
	}

	static private JMenuItem getMenuFromName(String s) {
		for(int z = 0; menu[z] != null; z++)
			if (s.equals(menu[z].getText())) return menu[z];
		return null;
	}

	public void newCost() {
		try {
			for(int z = 0;; z++) {
				String s = new File("Νέα Δαπάνη - " + z + ".cost").getCanonicalPath();
				if (!costs.containsKey(s)) {
					Cost c = new Cost();
					c.putAll((Map) data.get("ΑμετάβληταΣτοιχείαΔαπάνης"));
					costs.add(s, c);
					updateMenus();
					updatePanels();
					return;
				}
			}
		} catch (IOException e) {}
	}

	public void saveCost() {
		try {
			JFileChooser fc = new JFileChooser(costs.getPos());
			fc.setSelectedFile(new File(costs.getPos()));
			fc.setFileFilter(new ExtensionFileFilter("cost", "Αρχείο Δαπάνης"));
			if(fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
			File f = fc.getSelectedFile();
			String s = f.getCanonicalPath();
			if (!s.endsWith(".cost")) s += ".cost";
			if (!s.equals(costs.getPos())) {
				if (costs.containsKey(s)) {
					JOptionPane.showMessageDialog(this, "Το όνομα αυτό ανοίκει σε άλλη ανοικτή δαπάνη.\n"
							+ "Παρακαλώ δώστε άλλο όνομα.", "Αποθήκευση Δαπάνης", JOptionPane.ERROR_MESSAGE);
					return;
				} else if (f.exists()) {
					if (JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog(this,
							"Το αρχείο αυτό υπάρχει και θα χαθεί.\nΘελετε να συνεχίσω;",
							"Αποθήκευση Δαπάνης", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE))
						return;
				}
			}
			LoadSaveFile.save(s, (Saveable) costs.get());
			if (!s.equals(costs.getPos())) {
				costs.add(s, costs.remove());
				updateMenus();
			}
		} catch(Exception e) {
			Functions.showExceptionMessage(this, e, "Αποθήκευση Δαπάνης", "Πρόβλημα κατά την αποθήκευση της δαπάνης");
		}
	}

	private void openCost() {
		try {
			JFileChooser fc = new JFileChooser(costs.getPos());
			fc.setFileFilter(new ExtensionFileFilter("cost", "Αρχείο Δαπάνης"));
			int returnVal = fc.showOpenDialog(this);
			if(returnVal != JFileChooser.APPROVE_OPTION) return;
			String s = fc.getSelectedFile().getCanonicalPath();
			if (!s.endsWith(".cost")) s += ".cost";
			openCost(s);
		} catch (HeadlessException | IOException ex) {}
	}

	static private void openCost(String file) {
		try {
			if (costs.containsKey(file)) {
				JOptionPane.showMessageDialog(ths, "Το όνομα αυτό ανοίκει σε ανοικτή δαπάνη.\n"
						+ "Για να ανοίξετε αυτή τη δαπάνη θα πρέπει να κλείσετε την ομόνυμη ανοικτή.",
						"’νοιγμα Δαπάνης", JOptionPane.ERROR_MESSAGE);
				return;
			}
			costs.add(file, TreeFileLoader.loadFile(file));
			if (ths != null) {
				ths.updateMenus();
				ths.updatePanels();
			}
		} catch (Exception e) {
			Functions.showExceptionMessage(ths, e, "’νοιγμα αρχείου",
					"Πρόβλημα κατά το άνοιγμα της δαπάνης<br><b>" + file + "</b>");
		}
	}

	private void importCost() {
		try {
			JFileChooser fc = new JFileChooser(ini);
			fc.setMultiSelectionEnabled(true);
			fc.setFileFilter(new ExtensionFileFilter("ini:cost", "Αρχείο Δαπάνης και Ρυθμίσεων"));
			int returnVal = fc.showOpenDialog(this);
			if(returnVal != JFileChooser.APPROVE_OPTION) return;
			File[] files = fc.getSelectedFiles();
			final String choices[] = new String[] { "Προμηθευτές, Κρατήσεις, Προσωπικό", "Προμηθευτές, Προσωπικό",
				"Αμετάβλητα στοιχεία", "Προμηθευτές", "Κρατήσεις", "Προσωπικό" };
			final char fchoices[] = new char[] { 7, 5, 8, 4, 2, 1 };
			Object a = JOptionPane.showInputDialog(this,
					"Επιλέξτε τι θα εξάγετε από τα επιλεχθέντα αρχεία δαπανών και ρυθμίσεων\n"
					+ "για εισαγωγή στα δεδομένα του προγράμματος", "Εισαγωγή στοιχείων από αρχεία δαπανών και ρυθμίσεων",
					JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
			if (a == null) return;
			char flags = fchoices[Arrays.asList(choices).indexOf(a)];
			for (File f1 : files) {
				importCost(f1.getCanonicalPath(), flags);
				flags &= 7;
			}
		} catch (HeadlessException | IOException ex) {}
		repaint();
	}

	// flags: 1: Man, 2: Hold, 4: Provider, 8: StaticData
	static private void importCost(String file, char flags) {
		ArrayList<Hold> holds = (ArrayList<Hold>) data.get("Κρατήσεις");
		ArrayList<Man> men = (ArrayList<Man>) data.get("Προσωπικό");
		ArrayList<Provider> providers = (ArrayList<Provider>) data.get("Προμηθευτές");
		HashObject staticdata = (HashObject) data.get("ΑμετάβληταΣτοιχείαΔαπάνης");
		try {
			Object o = TreeFileLoader.loadFile(file);
			if (o instanceof Cost) {
				Cost c = (Cost) o;
				if ((flags & 1) != 0)
					for (Object man : c.values())
						if (man instanceof Man && !men.contains((Man) man))
							men.add((Man) man);
				ArrayList<Bill> b = (ArrayList<Bill>) c.get("Τιμολόγια");
				for (Bill b1 : b) {
					if ((flags & 2) != 0) {
						Hold h = (Hold) b1.get("ΑνάλυσηΚρατήσεωνΣεΠοσοστά");
						if (h != null && !holds.contains(h)) holds.add(h);
					}
					if ((flags & 4) != 0) {
						Provider p = (Provider) b1.get("Προμηθευτής");
						if (p != null && !providers.contains(p)) providers.add(p);
					}
				}
				if ((flags & 8) != 0)
					for (String hash : StaticData.hash)
						if (c.containsKey(hash))
							staticdata.put(hash, c.get(hash));
			} else if (o instanceof HashObject) {
				if ((flags & 1) != 0)
					for (Man m : (VectorObject<Man>) ((HashObject) o).get("Προσωπικό"))
						if (!men.contains(m)) men.add(m);
				if ((flags & 2) != 0)
					for (Hold h : (VectorObject<Hold>) ((HashObject) o).get("Κρατήσεις"))
						if (!holds.contains(h)) holds.add(h);
				if ((flags & 4) != 0)
					for (Provider p : (VectorObject<Provider>) ((HashObject) o).get("Προμηθευτές"))
						if (!providers.contains(p)) providers.add(p);
				if ((flags & 8) != 0) {
						staticdata.clear();
						staticdata.putAll((HashObject) ((HashObject) o).get("ΑμετάβληταΣτοιχείαΔαπάνης"));
				}
			}
		} catch (Exception e) {
			Functions.showExceptionMessage(ths, e, "’νοιγμα αρχείου",
					"Πρόβλημα κατά το άνοιγμα του αρχείου δαπάνης ή ρυθμίσεων<br><b>" + file + "</b>");
		}
	}

	private void closeCost() {
		if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(this,
				"<html>Να κλείσω την τρέχουσα δαπάνη;", "Κλείσιμο Δαπάνης",
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE)) {
			costs.remove();
			updatePanels();
			updateMenus();
		}
	}

	private void updatePanels() {
		JTabbedPane j = (JTabbedPane) getContentPane().getComponent(0);
		for (int z = 0; z < 4; z++)
			j.setEnabledAt(z, costs.getPos() != null);
		if (costs.getPos() == null && j.getSelectedIndex() < 4) j.setSelectedIndex(4);
		repaint();
	}

	private void updateMenus() {
		getMenuFromName("Αποθήκευση Δαπάνης...").setEnabled(costs.getPos() != null);
		getMenuFromName("Κλείσιμο Δαπάνης").setEnabled(costs.getPos() != null);
		getMenuFromName("Εξαγωγή").setEnabled(costs.getPos() != null);
		JMenu window = (JMenu) getMenuFromName("Δαπάνες");
		window.setEnabled(costs.getPos() != null);

		if (costs.getPos() != null) {
			window.removeAll();
			ButtonGroup btg = new ButtonGroup();
			Iterator en = costs.keySet().iterator();
			while (en.hasNext()) {
				String m = en.next().toString();
				JRadioButtonMenuItem jmi = new JRadioButtonMenuItem(new File(m).getName(), m.equals(costs.getPos()));
				jmi.addActionListener(this);
				jmi.setActionCommand(m);
				btg.add(jmi);
				window.add(jmi);
			}
		}
	}

	@Override
	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			try {
				LoadSaveFile.save(ini, data);
			} catch(Exception ex) {
				if (JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog(this,
						"<html>Αποτυχία κατά την αποθήκευση του <b>cost.ini</b>.<br>Να κλείσω τo πρόγραμμα;",
						"Τερματισμός", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE))
					return;
			}
			System.exit(0);
		}
		super.processWindowEvent(e);
	}

	public final void addOptionsMenu() {
		JMenu options = (JMenu) getMenuFromName("Ρυθμίσεις");
		HashObject h = (HashObject) data.get("Ρυθμίσεις");
		JCheckBoxMenuItem cbmi = new JCheckBoxMenuItem("Ένα αντίγραφο",
				new ImageIcon(ClassLoader.getSystemResource("cost/only_one.png")),
				Boolean.TRUE.equals(h.get("ΜιαΦορά")));
		cbmi.addActionListener(this);
		options.add(cbmi);
		
		cbmi = new JCheckBoxMenuItem("Χειροκίνητη ρύθμιση τιμολογίου",
				new ImageIcon(ClassLoader.getSystemResource("cost/chain.png")),
				Boolean.TRUE.equals(h.get("ΤιμολόγιοΧειροκίνητα")));
		cbmi.addActionListener(this);
		options.add(cbmi);

		JMenuItem skins = getMenuFromName("Κέλυφος ");
		LookAndFeelInfo[] laf = UIManager.getInstalledLookAndFeels();
		String s = (String) h.get("Κέλυφος");
		if (s == null) s = UIManager.getSystemLookAndFeelClassName();
		ButtonGroup btg = new ButtonGroup();
		for (LookAndFeelInfo laf1 : laf) {
			String s1 = laf1.getClassName();
			JRadioButtonMenuItem jmi = new JRadioButtonMenuItem(laf1.getName(), s1.equals(s));
			jmi.setActionCommand(s1);
			jmi.addActionListener(this);
			btg.add(jmi);
			skins.add(jmi);
		}
	}

	static public void setSkin() {
		try {
			UIManager.setLookAndFeel(((HashObject) data.get("Ρυθμίσεις")).get("Κέλυφος").toString());
		} catch(NullPointerException | ClassNotFoundException | IllegalAccessException
				| InstantiationException | UnsupportedLookAndFeelException e) {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {}
		}
	}

	public static void main(String[] args) {
		// check for other running instance and setup listening server
		if (!OnlyOneInstance.check()) {
			for (String arg : args)
				OnlyOneInstance.send(arg.getBytes());
			System.exit(0);
		}

		try {
			// init php engine
			PhpScriptRunner.init(null);
		} catch (Exception e) {
			Functions.showExceptionMessage(null, e, "Πρόβλημα του PHP cli",
				"Πρόβλημα κατά την αρχικοποίηση του <b>PHP cli</b>.<br>Το πρόγραμμα θα τερματίσει.");
			System.exit(0);
		}

		try {
			// get application path
			rootPath = URLDecoder.decode(ClassLoader.getSystemResource("cost/MainFrame.class").getPath().
				replaceAll("(Cost\\.jar!/)?cost/MainFrame\\.class$|^(file\\:)?/", ""), "UTF-8");
		} catch (UnsupportedEncodingException ex) {
			rootPath = "";
		}

		// load ini file and create data structure
		Object o = null;
		try {
			o = TreeFileLoader.loadFile(ini);
		} catch(Exception e) {
			Functions.showExceptionMessage(null, e, "Πρόβλημα",
				"Πρόβλημα κατά τη φόρτωση του <b>cost.ini</b><br>"
				+ "Αν τρέχετε για πρώτη φορά το πρόγραμμα δεν υπάρχει λόγος ανησυχίας.<br>"
				+ "Θα φορτώσω τη default έκδοσή του.");
			try {
				o = TreeFileLoader.loadResource("cost.ini");
			} catch(Exception e2) {
				Functions.showExceptionMessage(null, e2, "Πρόβλημα",
					"Πρόβλημα κατά τη φόρτωση του default <b>cost.ini</b>.");
			}
		}
		data = o instanceof HashObject ? (HashObject) o : new HashObject();
		if (!(data.get("Προσωπικό") instanceof VectorObject)) data.put("Προσωπικό", new VectorObject<>());
		if (!(data.get("Προμηθευτές") instanceof VectorObject)) data.put("Προμηθευτές", new VectorObject<>());
		if (!(data.get("Κρατήσεις") instanceof VectorObject)) data.put("Κρατήσεις", new VectorObject<>());
		if (!(data.get("ΑμετάβληταΣτοιχείαΔαπάνης") instanceof HashObject)) data.put("ΑμετάβληταΣτοιχείαΔαπάνης", new HashObject());
		if (!(data.get("Ρυθμίσεις") instanceof HashObject)) data.put("Ρυθμίσεις", new HashObject());
		if (!(data.get("ΑνοικτέςΔαπάνες") instanceof IteratorHashObject)) data.put("ΑνοικτέςΔαπάνες", new IteratorHashObject());
		costs = (IteratorHashObject) data.get("ΑνοικτέςΔαπάνες");			// shortcut

		setSkin();
		try {
			for (String arg : args)
				openCost(new File(arg).getCanonicalPath());
		} catch (IOException ex) {}

		ths = new MainFrame();
		
		// Autosave ini file every 5 minutes.
		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
				try {
					LoadSaveFile.save(ini, data);
				} catch(Exception ex) {}
			}, 5, 5, TimeUnit.MINUTES);
	}

	// ----- ActionListener ----- //

	@Override
	public void actionPerformed(ActionEvent e) {
		JMenuItem j = (JMenuItem) e.getSource();
		String ac = j.getText();
		int order = -1;

		// if we must output only one copy
		Map<String, String> env = new HashMap<>();
		Map<String, Object> options = (HashObject) data.get("Ρυθμίσεις");
		if (Boolean.TRUE.equals(options.get("ΜιαΦορά"))) env.put("one", "true");

		if (((JMenu) getMenuFromName("Κέλυφος ")).isMenuComponent(j)) {
			options.put("Κέλυφος", e.getActionCommand());
			setSkin(); dispose(); ths = new MainFrame();
		} else if (((JMenu) getMenuFromName("Δαπάνες")).isMenuComponent(j)) {
			costs.setPos(e.getActionCommand());
			updatePanels();
		}
		else if (ac.equals("Νέα Δαπάνη")) newCost();
		else if (ac.equals("’νοιγμα Δαπάνης...")) openCost();
		else if (ac.equals("Αποθήκευση Δαπάνης...")) saveCost();
		else if (ac.equals("Εισαγωγή στοιχείων...")) importCost();
		else if (ac.equals("Κλείσιμο Δαπάνης")) closeCost();
		else if (ac.equals("Έξοδος")) dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		else if (ac.equals("Δαπάνη")) ExportReport.exportReport("Δαπάνη.php", env);
		else if (ac.equals("Εφορία")) ExportReport.exportReport("ΦΕ για την Εφορία.php");
		else if (ac.equals("Προμηθευτής")) ExportReport.exportReport("ΦΕ για τον Προμηθευτή.php");
		else if (ac.equals("Συγκρότηση Επιτροπών")) order = 0;
		else if (ac.equals("Διακήρυξη")) order = 1;
		else if (ac.equals("Κατακύρωση")) order = 2;
		else if (ac.equals("Διαβιβαστικό Δαπάνης")) order = 3;
		else if (ac.equals("Έκθεση Απαιτούμενης Δαπάνης")) order = 4;
		else if (ac.equals("Εισηγητική Έκθεση")) ExportReport.exportReport("Εισηγητική Έκθεση Διαγωνισμού.php");
		else if (ac.equals("Πρακτικό")) ExportReport.exportReport("Πρακτικό Διαγωνισμού.php");
		else if (ac.equals("Ανάλυση Κρατήσεων")) ExportReport.exportReport("Κρατήσεις υπέρ Τρίτων.php");
		else if (ac.equals("Πρόχειρη Λίστα Τιμολογίων")) ExportReport.exportReport("Πρόχειρη Λίστα Τιμολογίων.php");
		else if (ac.equals("Απόδειξη για Προκαταβολή")) ExportReport.exportReport("Απόδειξη για Προκαταβολή.php");
		else if (ac.equals("Ένα αντίγραφο")) options.put("ΜιαΦορά", Boolean.FALSE.equals(options.get("ΜιαΦορά")));
		else if (ac.equals("Οδηγός Τιμολογίου")) {
			if (cwf == null) cwf = new CostWizardDialog(this);
			cwf.setVisible(true);
		}
		else if (ac.equals("Χειροκίνητη ρύθμιση τιμολογίου")) {
			boolean a = Boolean.TRUE.equals(options.get("ΤιμολόγιοΧειροκίνητα"));
			options.put("ΤιμολόγιοΧειροκίνητα", !a);
			Cost c = (Cost) costs.get();
			if (a && c != null) {
				ArrayList<Bill> b = (ArrayList<Bill>) c.get("Τιμολόγια");
				for (Bill b1 : b) b1.recalculate();
				repaint();
			}
		}
		else if (ac.equals("Εγχειρίδιο")) {
			try {	// open help
				Desktop.getDesktop().open(new File(rootPath + "help/index.html"));
			} catch(IllegalArgumentException | IOException ex) {
				Functions.showExceptionMessage(this, ex, "Πρόβλημα στην εκκίνηση του browser", null);
			}
		}
		else if (ac.equals("Περί...")) JOptionPane.showMessageDialog(this,
				"<html><center><b><font size=4>Στρατιωτικές Δαπάνες</font><br>" +
				"<font size=3>Έκδοση 1.6.7b</font></b></center><br>" +
				"Προγραμματισμός: <b>Γκέσος Παύλος (ΣΣΕ 2002)</b><br>" +
				"’δεια χρήσης: <b>BSD</b><br>" +
				"Δημοσίευση: <b>11 Ιουλ 17</b><br>" +
				"Σελίδα: <b>http://sourceforge.net/projects/ha-expenditure/</b><br><br>" +
				"<center>Το Πρόγραμμα είναι 13 χρονών!</center>",
				getTitle(), JOptionPane.PLAIN_MESSAGE);
		
		// αν ειναι διαταγή απαιτεί extra dialog για σχέδιο ή ακριβές αντίγραφο
		if (order != -1) {
			final String[] file = { "Δγη Συγκρότησης Επιτροπών", "Δγη Διακήρυξης Διαγωνισμού",
				"Δγη Κατακύρωσης Διαγωνισμού", "Διαβιβαστικό Δαπάνης", "Έκθεση Απαιτούμενης Δαπάνης" };
			final String[] a = { "Ακριβές Αντίγραφο", "Σχέδιο" };
			int b = JOptionPane.showOptionDialog(this, "Επιλέξτε σαν τι θα βγεί η διαταγή.",
					"Επιλογή", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, a, a[0]);
			if (b == JOptionPane.CLOSED_OPTION) return;
			else if (b == 1) env.put("draft", "true");
			ExportReport.exportReport(file[order] + ".php", env);
		}
	}


	private static class OnlyOneInstance implements Runnable {
		private static ServerSocket ss;
		public static boolean check() {
			try {
				ss = new ServerSocket(666);
				new Thread(new OnlyOneInstance()).start();
				return true;
			} catch(IOException e) { return false; }
		}

		public static void send(byte[] a) {
			try {
				try (OutputStream s = new Socket("127.0.0.1", 666).getOutputStream()) { s.write(a); }
			} catch(IOException e) {}
		}

		@Override
		public void run() {
			try {
				for(;;) {
					Socket s = ss.accept();
					openCost(new File(LoadSaveFile.loadFile(s.getInputStream())).getCanonicalPath());
				}
			} catch(Exception e) {}
		}
	}

	/*class MenuBlankIcon implements Icon {
		public int getIconHeight() { return 16; }
		public int getIconWidth() { return 16; }
		public void paintIcon(Component c, Graphics g, int x, int y) {}
	}*/
}