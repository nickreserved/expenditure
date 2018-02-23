package cost;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class PropertiesTable extends JTable {
  TableCellEditor edit;

  public PropertiesTable(PropertiesTableModel tcr, int w, Component[][] cmp) {
    super();

    if (cmp != null) edit = new RichTableCellEditor(cmp);

    tableHeader.setReorderingAllowed(false);
    if (tcr.getHeaders() == null) tableHeader = null;

    setModel(tcr);
    setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    for (int z = 0; z < this.getColumnCount(); z++) {
      TableColumn cm = getColumnModel().getColumn(z);
      if (z == 0) {
	cm.setCellRenderer(new RichTableCellRenderer());
	cm.setMaxWidth(w);
        cm.setMinWidth(w);
      } else
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
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
	boolean isSelected, boolean hasFocus, int row, int column) {
      b.setText(value.toString());
      return b;
    }
  }
}