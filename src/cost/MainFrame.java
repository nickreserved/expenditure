package cost;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame {
  public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

  public Bills bills = new Bills(this);
  public StaticData staticData = new StaticData();
  public CostData cost = new CostData(this);

  JMenuItem newCost = new JMenuItem("Νέα Δαπάνη");
  JMenuItem openCost = new JMenuItem("’νοιγμα Δαπάνης...");
  JMenuItem saveCost = new JMenuItem("Αποθήκευση Δαπάνης...");
  JMenuItem exportCost = new JMenuItem("Δαπάνη");
  JMenuItem exportPlan = new JMenuItem("Σχέδιο");
  JMenuItem exportBill = new JMenuItem("Απόδειξη");
  JMenuItem exportEforia = new JMenuItem("Δήλωση ΦΕ");
  JMenuItem exportProvider = new JMenuItem("Βεβαίωση απόδοσης ΦΕ");
  JMenuItem exportPreReport = new JMenuItem("Έκθεση Απαιτουμένης Δαπάνης");
  JMenuItem exportReport = new JMenuItem("Έκθεση Γενομένης Δαπάνης");
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
    mainTab.addTab("Πληροφορίες", new About());
    getContentPane().add(mainTab);

    newCost.addActionListener(cost);
    openCost.addActionListener(cost);
    saveCost.addActionListener(cost);
    exportCost.addActionListener(cost);
    exportPlan.addActionListener(cost);
    exportBill.addActionListener(cost);
    exportEforia.addActionListener(cost);
    exportProvider.addActionListener(cost);
    exportPreReport.addActionListener(cost);
    exportReport.addActionListener(cost);
    exportHolds.addActionListener(cost);

    JMenu file = new JMenu("Αρχείο");
    file.add(newCost);
    file.add(openCost);
    file.add(saveCost);
    JMenu export = new JMenu("Εξαγωγή");
    export.add(exportCost);
    export.add(exportPlan);
    export.add(exportBill);
    export.add(exportEforia);
    export.add(exportProvider);
    export.add(exportPreReport);
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
      // Save something???
      System.exit(0);
    }
    super.processWindowEvent(e);
  }

  public static void main(String[] args) throws Exception {
    new MainFrame();
  }
}