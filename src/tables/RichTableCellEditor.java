package tables;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;

public class RichTableCellEditor extends AbstractCellEditor
    implements TableCellEditor, ActionListener {

  protected Component[][] cls;
  protected int x, y;
  protected Component current;
  protected JTextField text = new JTextField();

  public RichTableCellEditor(Component[][] cls) {
    text.setBorder(BorderFactory.createMatteBorder(1,1,0,0,Color.WHITE));
    setListener(text);
    setComponents(cls);
  }

  public void setComponents(Component[][] cmp) {
    cls = cmp;
    for (int z = 0; z < cls.length / cls[0].length; z++)
      for (int y = 0; y < cls[z].length; y++)
	setListener(cls[z][y]);
  }

  public Component[][] getComponents() { return cls; }

  public Object getCellEditorValue() {
    if (current instanceof JTextField)
      return ((JTextField) current).getText();
    else if (current instanceof JComboBox)
      return ((JComboBox) current).getSelectedItem();
    else return null;
  }

  public void setValue(Object o) {
    if (current instanceof JTextField)
      ((JTextField) current).setText(o.toString());
    else if (current instanceof JComboBox)
      ((JComboBox) current).setSelectedItem(o);
  }

  public void setListener(Component o) {
    if (o instanceof JTextField)
      ((JTextField) o).addActionListener(this);
    else if (o instanceof JComboBox)
      ((JComboBox) o).addActionListener(this);
  }

  public boolean isCellEditable(EventObject anEvent) {
    if (anEvent instanceof MouseEvent)
      return ((MouseEvent)anEvent).getClickCount() >= 2;
    return true;
  }

  public boolean shouldSelectCell(EventObject anEvent) {
    if (anEvent instanceof MouseEvent && current instanceof JComboBox)
      return ((MouseEvent)anEvent).getID() != MouseEvent.MOUSE_DRAGGED;
    return true;
  }

  public boolean stopCellEditing() {
    if (current instanceof JComboBox) {
      JComboBox j = (JComboBox) current;
      if (j.isEditable()) j.actionPerformed(new ActionEvent(this, 0, ""));
    }
    return super.stopCellEditing();
  }

  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
					       int row, int column) {
    current = cls[row][column];
    if (current != null) setValue(value);
    else {
      current = text;
      text.setText((value == null) ? null : value.toString());
    }
    return current;
  }


  // ------------------------------------- ActionListener ------------------------------- //

  public void actionPerformed(ActionEvent e) {
    stopCellEditing();
  }
}