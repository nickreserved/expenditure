package cost;

import java.awt.*;
import javax.swing.*;
import tables.*;

public class Holds extends JPanel implements DataTransmitter {
	static protected JComboBox holds;

	public Holds() {
		holds = new JComboBox(new ComboDataModel(this, new Hold()));
		setLayout(new BorderLayout());
		add(new JScrollPane(new ResizableTable(new ResizableTableModel(this, new String[] { "ÌÔÓ", "ÅÌĞ", "ÔÓÌÅÄÅ", "ÁÏÏÁ", "ÔĞÅÄÅ", "ÅÊÏÅÌÓ", "×áñôüóçìï", "ÏÃÁ", "Óıíïëï" }, null, Hold.class), false, true)));
	}
	
	public Object getData() { return MainFrame.data.get("ÊñáôŞóåéò"); }
}