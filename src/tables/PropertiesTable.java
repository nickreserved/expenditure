package tables;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class PropertiesTable extends JTable {
  TableCellEditor edit;

  public PropertiesTable(PropertiesTableModel tcr, int[] w, Component[][] cmp) {
    super();

    if (cmp != null) edit = new RichTableCellEditor(cmp);

    tableHeader.setReorderingAllowed(false);
    if (tcr.getHeaders() == null) tableHeader = null;

    setModel(tcr);
    setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    for (int z = 0; z < this.getColumnCount(); z++) {
      TableColumn cm = getColumnModel().getColumn(z);
      cm.setCellRenderer(new RichTableCellRenderer());
      if (w != null && w.length > z && w[z] > 0) {
	cm.setMaxWidth(w[z]);
	cm.setMinWidth(w[z]);
      }
      if (cmp != null) cm.setCellEditor(edit);
    }
  }

  public void setEditorComponents(Component[][] cmp) {
    if (edit instanceof RichTableCellEditor)
      ((RichTableCellEditor) edit).setComponents(cmp);
  }

  public Component[][] getEditorComponents() {
    if (edit instanceof RichTableCellEditor)
      return ((RichTableCellEditor) edit).getComponents();
    else return null;
  }


  // ========================== RichTableCellRenderer ===================================== //

  public class RichTableCellRenderer extends DefaultTableCellRenderer {
    JButton b = new JButton();

    public RichTableCellRenderer() {
      b.setBorder(BorderFactory.createMatteBorder(1,1,0,0,Color.WHITE));
      b.setFont(new Font(null, Font.PLAIN, 12));
      b.setHorizontalAlignment(SwingConstants.LEFT);
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
	boolean isSelected, boolean hasFocus, int row, int column) {
      if (value instanceof String && value.toString().startsWith(":")) {
	b.setText(value.toString().substring(1));
	return b;
      } else
	return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
  }
}