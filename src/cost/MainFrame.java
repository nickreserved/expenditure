package cost;

import java.io.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.UIManager.*;

import common.*;


public class MainFrame extends JFrame implements ActionListener {
  static private final String[] txtmenu = { "Νέα Δαπάνη", "’νοιγμα Δαπάνης...", "Αποθήκευση Δαπάνης...",
      "Κλείσιμο Δαπάνης", "’νοιγμα Σταθερών", "Αποθήκευση Σταθερών", "Έξοδος", "Δαπάνη",
      "Συγκρότηση Επιτροπών", "Διαβιβαστικό Δαπάνης",
      "Απόδειξη", "Δήλωση ΦΕ", "Βεβαίωση απόδοσης ΦΕ", "Έκθεση Δαπάνης", "Ανάλυση Κρατήσεων" };

  static final int EXPORT_EPITROPES = 5;
  static final int EXPORT_DIABIBASTIKO = 6;
  static final int EXPORT_APODEI3H = 7;
  static final int EXPORT_DHLWSH_FE = 8;
  static final int EXPORT_BEBAIWSH_FE = 9;
  static final int EXPORT_EK8ESH = 10;
  static final int EXPORT_KRATHSEIS = 11;

  public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
  public static String rootPath = ClassLoader.getSystemResource("").getPath();

  static protected HashObject data = (HashObject) TreeFileLoader.loadFile(rootPath + "main.ini");
  static protected Hashtable costs = new Hashtable();
  static protected String currentCost;

  private static JMenuItem[] menu = new JMenuItem[txtmenu.length];
  private static JMenu window;

  static private Holds holds = new Holds();
  static private Providers providers = new Providers();
  static private Men men = new Men();
  static private Bills bills = new Bills();
  static private CostData costData = new CostData();
  static private StaticData staticData = new StaticData();

  public MainFrame() {
    super("Στρατιωτικές Δαπάνες");
    JTabbedPane mainTab = new JTabbedPane();
    mainTab.addTab("Στοιχεία Δαπάνης", costData);
    mainTab.addTab("Τιμολόγια", bills);
    mainTab.addTab("Αμετάβλητα Στοιχεία", staticData);
    mainTab.addTab("Εργασίες", null/*new Works()*/);
    mainTab.addTab("Προμηθευτές", providers);
    mainTab.addTab("Κρατήσεις", holds);
    mainTab.addTab("Προσωπικό Μονάδας", men);
    mainTab.addTab("Πληροφορίες", about());
    getContentPane().add(mainTab);
    Color c = Color.decode("#b0d0b0");
    mainTab.setBackgroundAt(0, c);
    mainTab.setBackgroundAt(1, c);
    mainTab.setBackgroundAt(2, c);
    c = Color.decode("#e0b0b0");
    mainTab.setBackgroundAt(3, c);
    c = Color.decode("#e0e0b0");
    mainTab.setBackgroundAt(4, c);
    mainTab.setBackgroundAt(5, c);
    mainTab.setBackgroundAt(6, c);
    mainTab.setBackgroundAt(7, Color.decode("#b0d0e0"));

    updatePanels();

    for (int z = 0; z < menu.length; z++) {
      menu[z] = new JMenuItem(txtmenu[z]);
      menu[z].addActionListener(this);
    }

    JMenu allhlografia = new JMenu("Αλληλογραφία");
    allhlografia.add(menu[EXPORT_EPITROPES]);
    allhlografia.add(menu[EXPORT_DIABIBASTIKO]);
    JMenu file = new JMenu("Αρχείο");
    file.add(menu[0]);
    file.add(menu[1]);
    file.add(menu[2]);
    file.add(menu[3]);
    file.addSeparator();
    file.add(menu[4]);
    file.add(menu[5]);
    file.addSeparator();
    file.add(menu[6]);
    JMenu export = new JMenu("Εξαγωγή");
//    export.add(menu[EXPORT_COST]);
    export.addSeparator();
    export.add(allhlografia);
    export.addSeparator();
    export.add(menu[EXPORT_APODEI3H]);
    export.addSeparator();
    export.add(menu[EXPORT_DHLWSH_FE]);
    export.add(menu[EXPORT_BEBAIWSH_FE]);
    export.addSeparator();
    export.add(menu[EXPORT_EK8ESH]);
    export.add(menu[EXPORT_KRATHSEIS]);
    JMenuBar jmb = new JMenuBar();
    jmb.add(file);
    jmb.add(export);
    jmb.add(window = new JMenu("Δαπάνες"));
    setJMenuBar(jmb);
    updateMenus();

    setSize(635, 450);
    setLocation( (screenSize.width - getWidth()) / 2, (screenSize.height - getHeight()) / 2);
    setVisible(true);
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
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
    h.classes.put("ΔγηΔιάθεσης", OrderId.class);
    h.classes.put("ΔγηΑνάθεσης", OrderId.class);
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
    window.removeAll();
    Enumeration en = costs.keys();
    while (en.hasMoreElements()) {
      String m = en.nextElement().toString();
      JRadioButtonMenuItem jmi = new JRadioButtonMenuItem(m, m.equals(currentCost));
      jmi.addActionListener(this);
      window.add(jmi);
    }
    menu[2].setEnabled(currentCost != null);
    menu[3].setEnabled(currentCost != null);
    window.setEnabled(currentCost != null);
  }

  protected void processWindowEvent(WindowEvent e) {
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      while (currentCost != null)
	if (!closeCost()) return;
      System.exit(0);
    }
    super.processWindowEvent(e);
  }

  static Component about() {
      String s;
      try {
	s = LoadSaveFile.loadFileToString("cost/about.html");
      } catch (Exception e) {
	s = "Δεν βρέθηκε το αρχείο <b>about.html</b>";
      }

      JEditorPane extra_info = new JEditorPane("text/html", s);
      JScrollPane scrlComment = new JScrollPane(extra_info, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
						JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      extra_info.setEditable(false);
      extra_info.setCaretPosition(0);
      return scrlComment;
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
    }
    catch(Exception e) {
    }
  }

  public static void main(String[] args) throws Exception {

//script.Compile.compile("<?= 5 ?>");
//System.out.print(Functions.skipRegex("breaka  { fuck", 0, "\\s*break((\\s*;)|(\\s+\\S))"));
//System.out.print(Functions.getCurrentLine("echo\nska\nta\n", "\n", 9));
/*    Vector v1 = new Vector();
        v1.add(new Integer(8));
        v1.add(new Integer(0));
Vector v = new Vector();
    v.add("Ma8htes: ");
    v.add(new Integer(10));
    script.function.Add add = new script.function.Add();
    script.function.Div add1 = new script.function.Div();
v.add(add1);
    v.add(new Integer(5));
v.add(" toylaxiston");

    add.setVector(v);
    add1.setVector(v1);
    //add = (script.function.Add) add.simplify(null);
    System.out.println(add.simplify(null));
    System.out.println(add);*/
System.out.println("_la1os".matches("[_\\p{Alpha}\\p{InGreek}][_\\p{Alnum}\\p{InGreek}]*"));

    //System.exit(0);

    setSkin(args.length > 0 && !args[0].endsWith(".cost") ? args[0] : null);
    MainFrame m = new MainFrame();
    String a = null;
    if (args.length > 0 && args[0].endsWith(".cost")) a = args[0];
    else if (args.length > 1 && args[1].endsWith(".cost")) a = args[1];
    if (a != null) m.openCost(a);
    script.Compile.findAllFunctions();
  }


  // ================================ ActionListener ==================================== //

  public void actionPerformed(ActionEvent e) {
    JMenuItem j = (JMenuItem) e.getSource();
    if (j instanceof JRadioButtonMenuItem) {
      currentCost = j.getText();
      updatePanels();
      updateMenus();
    } else if (j == menu[0]) newCost();
    else if (e.getSource() == menu[1]) openCost();
    else if (j == menu[2]) saveCost();
    else if (j == menu[3]) closeCost();
    else if (j == menu[4]) {
      data = (HashObject) TreeFileLoader.loadFile(rootPath + "main.ini");
      updatePanels();
    }
    else if (j == menu[5]) LoadSaveFile.save(rootPath + "main.ini", data);
    else if (j == menu[6]) dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
/*
    else if (e.getSource() == menu[SAVE_COST]) cost.save();
    else if (e.getSource() == menu[EXPORT_APODEI3H])
      ExportReport.exportReport("plhrhs_apodei3h", this, null);
    else if (e.getSource() == menu[EXPORT_EPITROPES])
      ExportReport.exportReport("my_order", this, null);
    else if (e.getSource() == menu[EXPORT_DIABIBASTIKO])
      ExportReport.exportReport("diabibastiko", this, null);
    else if (bills.billsModel.getData().size() > 0) {
      if (e.getSource() == menu[EXPORT_COST])
	ExportReport.exportReport("cost", this, null);
      else if (e.getSource() == menu[EXPORT_BEBAIWSH_FE])
	ExportReport.exportReport("fe_provider", this, null);
      else if (e.getSource() == menu[EXPORT_DHLWSH_FE])
	ExportReport.exportReport("fe_doy", this, null);
      else if (e.getSource() == menu[EXPORT_EK8ESH])
	ExportReport.exportReport("report", this, null);
      else if (e.getSource() == menu[EXPORT_KRATHSEIS])
	ExportReport.exportReport("hold", this, null);
    }*/
  }
}