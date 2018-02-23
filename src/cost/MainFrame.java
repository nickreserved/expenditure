package cost;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.UIManager.*;

public class MainFrame extends JFrame {
  public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

  public Bills bills = new Bills(this);
  public StaticData staticData = new StaticData();
  public CostData cost = new CostData(this);

  JMenuItem newCost = new JMenuItem("Νέα Δαπάνη");
  JMenuItem openCost = new JMenuItem("’νοιγμα Δαπάνης...");
  JMenuItem saveCost = new JMenuItem("Αποθήκευση Δαπάνης...");
  JMenuItem exit = new JMenuItem("Έξοδος");
  JMenuItem exportCost = new JMenuItem("Δαπάνη");
  JMenuItem exportEpitropes = new JMenuItem("Συγκρότηση Επιτροπών");
  JMenuItem exportDiabibastiko = new JMenuItem("Διαβιβαστικό Δαπάνης");
  JMenuItem exportParatash = new JMenuItem("Αίτηση Παράτασης Υποβολής");
  JMenuItem exportBill = new JMenuItem("Απόδειξη");
  JMenuItem exportEforia = new JMenuItem("Δήλωση ΦΕ");
  JMenuItem exportProvider = new JMenuItem("Βεβαίωση απόδοσης ΦΕ");
  JMenuItem exportReport = new JMenuItem("Έκθεση Δαπάνης");
  JMenuItem exportHolds = new JMenuItem("Ανάλυση Κρατήσεων");

  public MainFrame() throws Exception {
    super("Στρατιωτικές Δαπάνες");

    JTabbedPane mainTab = new JTabbedPane();
    mainTab.addTab("Στοιχεία Δαπάνης", cost);
    mainTab.addTab("Τιμολόγια", bills);
    mainTab.addTab("Αμετάβλητα Στοιχεία", staticData);
    mainTab.addTab("Προμηθευτές", new Providers(this));
    mainTab.addTab("Κρατήσεις", new Holds(this));
    mainTab.addTab("Προσωπικό Μονάδας", new Men(this));
    mainTab.addTab("Πληροφορίες", about());
    getContentPane().add(mainTab);
    Color c = Color.decode("#b0d0b0");
    mainTab.setBackgroundAt(0, c);
    mainTab.setBackgroundAt(1, c);
    mainTab.setBackgroundAt(2, c);
    c = Color.decode("#e0e0b0");
    mainTab.setBackgroundAt(3, c);
    mainTab.setBackgroundAt(4, c);
    mainTab.setBackgroundAt(5, c);
    mainTab.setBackgroundAt(6, Color.decode("#b0d0e0"));

    newCost.addActionListener(cost);
    openCost.addActionListener(cost);
    saveCost.addActionListener(cost);
    exit.addActionListener(cost);
    exportCost.addActionListener(cost);
    exportEpitropes.addActionListener(cost);
    exportBill.addActionListener(cost);
    exportEforia.addActionListener(cost);
    exportProvider.addActionListener(cost);
    exportDiabibastiko.addActionListener(cost);
    exportReport.addActionListener(cost);
    exportHolds.addActionListener(cost);

    JMenu allhlografia = new JMenu("Αλληλογραφία");
    allhlografia.add(exportEpitropes);
    allhlografia.add(exportParatash);
    allhlografia.add(exportDiabibastiko);
    JMenu file = new JMenu("Αρχείο");
    file.add(newCost);
    file.addSeparator();
    file.add(openCost);
    file.add(saveCost);
    file.addSeparator();
    file.add(exit);
    JMenu export = new JMenu("Εξαγωγή");
    export.add(exportCost);
    export.addSeparator();
    export.add(allhlografia);
    export.addSeparator();
    export.add(exportBill);
    export.addSeparator();
    export.add(exportEforia);
    export.add(exportProvider);
    export.addSeparator();
    export.add(exportReport);
    export.add(exportHolds);
    JMenuBar jmb = new JMenuBar();
    jmb.add(file);
    jmb.add(export);
    this.setJMenuBar(jmb);

    setSize(635, 450);
    setLocation( (screenSize.width - getWidth()) / 2,
	     (screenSize.height - getHeight()) / 2);
    setVisible(true);
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
  }

  protected void processWindowEvent(WindowEvent e) {
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      int a = JOptionPane.showConfirmDialog(null, "Θέλετε να σωθεί η τρέχουσα δαπάνη;", "Επιβεβαίωση εξόδου",
					    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
      boolean s = true;
      if (a == JOptionPane.YES_OPTION) s = cost.save();
      if (a == JOptionPane.CANCEL_OPTION || !s) return;
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
    setSkin(args.length > 0 && !args[0].endsWith(".cost") ? args[0] : null);
    MainFrame m = new MainFrame();
    String a = null;
    if (args.length > 0 && args[0].endsWith(".cost")) a = args[0];
    else if (args.length > 1 && args[1].endsWith(".cost")) a = args[1];
    if (a != null) m.cost.loadFile(a);
  }
}