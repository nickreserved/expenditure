package cost;

import java.awt.*;
import javax.swing.*;
import tables.*;

public class Holds extends JPanel implements DataTransmitter {
	static protected JComboBox holds;

	public Holds() {
		final String[] header = { "ÌÔÓ", "ÅÌĞ", "ÔÓÌÅÄÅ", "ÁÏÏÁ", "ÔĞÅÄÅ", "ÅÊÏÅÌÓ", "×áñôüóçìï", "ÏÃÁ", "Óıíïëï" };
		holds = new JComboBox(new ComboDataModel(this, new Hold()));
		setLayout(new BorderLayout());
		add(new JScrollPane(new ResizableTable(new ResizableTableModel(this, header, null, Hold.class), false)));
	}
	
	public Object getData() { return MainFrame.data.get("ÊñáôŞóåéò"); }
}