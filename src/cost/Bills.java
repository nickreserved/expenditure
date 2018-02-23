package cost;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.event.*;

public class Bills extends JPanel implements ListSelectionListener {
  static final String[] propertiesHeader = { null, "Τιμολόγιο", "Σύνολο" };
  static final String[] propertiesNames = { "Καθαρή Αξία", "ΦΠΑ", "Καταλογιστέο",
      "Κρατήσεις", "Πληρωτέο", "ΦΕ", "Υπόλοιπο" };

  MainFrame main;

  int currentBill = -1;

  ResizableTableModel billsModel;
  ResizableTableModel billModel;
  PropertiesTableModel propertiesModel;
  JTable billsTable;
  JTable billTable;
  JTable propertiesTable;

  JComboBox cbFpa = new JComboBox(BillItem.fpaList);
  JComboBox cbMeasures = new JComboBox(BillItem.measures);

  JComboBox cbFe = new JComboBox(Bill.feList);
  JComboBox cbType = new JComboBox(Bill.types);
  JComboBox cbCategory = new JComboBox(Bill.categories);
  JComboBox providers = new JComboBox();
  JComboBox holds = new JComboBox();

  public Bills(MainFrame m) throws Exception {
    main = m;

    billsModel = new ResizableTableModel(new Vector(), Bill.header, Bill.class);
    billsTable = new JTable(billsModel);
    billsTable.getSelectionModel().addListSelectionListener(this);
    billsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    TableColumnModel cm = billsTable.getColumnModel();
    cm.getColumn(Bill.PROVIDER).setCellEditor(new DefaultCellEditor(providers));
    cm.getColumn(Bill.HOLDS).setCellEditor(new DefaultCellEditor(holds));
    cm.getColumn(Bill.FE).setCellEditor(new DefaultCellEditor(cbFe));
    cm.getColumn(Bill.TYPE).setCellEditor(new DefaultCellEditor(cbType));
    cm.getColumn(Bill.CATEGORY).setCellEditor(new DefaultCellEditor(cbCategory));

    billModel = new ResizableTableModel(null, BillItem.header, BillItem.class);
    billTable = new JTable(billModel);
    cm = billTable.getColumnModel();
    cm.getColumn(BillItem.FPA).setCellEditor(new DefaultCellEditor(cbFpa));
    cm.getColumn(BillItem.MEASURE).setCellEditor(new DefaultCellEditor(cbMeasures));
    billTable.getSelectionModel().addListSelectionListener(this);

    propertiesModel = new ReportTableModel(billsModel.getData());
    propertiesTable = new PropertiesTable(propertiesModel, 85, null);

    setLayout(new BorderLayout());
    Box bv = Box.createVerticalBox();
    bv.add(billsTable.getTableHeader());
    bv.add(billsTable);
    bv.add(billTable.getTableHeader());
    bv.add(billTable);

    Box bv1 = Box.createVerticalBox();
    bv1.add(propertiesTable.getTableHeader());
    bv1.add(propertiesTable);

    add(new JScrollPane(bv), BorderLayout.CENTER);
    add(bv1, BorderLayout.SOUTH);
    setVisible(true);
  }

  public void setProviders(Vector v) {
    providers.removeAllItems();
    for (int z = 0; z < v.size(); z++)
      providers.addItem(((Provider) v.elementAt(z)).clone());
    providers.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, 100000));
  }

  public void setHolds(Vector v) {
    holds.removeAllItems();
    for (int z = 0; z < v.size(); z++)
      holds.addItem(((Hold) v.elementAt(z)).clone());
    holds.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, 100000));
  }

  // ============================ ListSelectionListener ================================= //

  public void valueChanged(ListSelectionEvent e) {
    int a = billsTable.getSelectedRow();
    if (a != -1) {
      Vector v = billsModel.getData();
      if (v.size() > a) {
        currentBill = a;
	Bill b = (Bill) v.elementAt(a);
        billModel.setData((Vector) b.getCell(Bill.ITEMS));
      } else {
        currentBill = -1;
        billModel.setData(null);
      }
    }
    propertiesModel.fireTableDataChanged();
  }


  // =============================== ReportTableModel ===================================== //

  protected class ReportTableModel extends PropertiesTableModel {
    public ReportTableModel(Vector data) {
      super(propertiesNames, null, propertiesHeader);
    }
    public boolean isCellEditable(int row, int col) { return false; }
    public Object getValueAt(int row, int col) {
      Vector v = billsModel.getData();
      switch (col) {
        case 0: return super.getValueAt(row, col);
        case 1:
          if (currentBill == -1) return null;
          Bill b = (Bill) v.elementAt(currentBill);
          switch(row) {
            case 0: return b.getCell(Bill.CLEAR_COST);
            case 1: return b.getCell(Bill.FPA_TOTAL);
            case 2: return b.getCell(Bill.KATALOGISTEO);
            case 3: return b.getCell(Bill.HOLD_TOTAL);
            case 4: return b.getCell(Bill.PLHRWTEO);
            case 5: return b.getCell(Bill.FE_TOTAL);
            case 6: return b.getCell(Bill.YPOLOIPO_PLHRWTEO);
          }
        case 2:
          switch(row) {
            case 0: return AnalyzeBills.getSumFromBills(v, Bill.CLEAR_COST);
            case 1: return AnalyzeBills.getSumFromBills(v, Bill.FPA_TOTAL);
            case 2: return AnalyzeBills.getSumFromBills(v, Bill.KATALOGISTEO);
            case 3: return AnalyzeBills.getSumFromBills(v, Bill.HOLD_TOTAL);
            case 4: return AnalyzeBills.getSumFromBills(v, Bill.PLHRWTEO);
            case 5: return AnalyzeBills.getSumFromBills(v, Bill.FE_TOTAL);
            case 6: return AnalyzeBills.getSumFromBills(v, Bill.YPOLOIPO_PLHRWTEO);
          }
      }
      return null;
    }
  }
}