package cost;

import java.awt.*;
import javax.swing.*;
import common.*;
import tables.*;

public class StaticData extends JPanel implements DataTransmitter {
	public StaticData() {
		final String[] hash = { "���������������", "������", "������������������",
				"�������", "�������������", "����", "���������", "�����������������",
				"�����������������", "��", "�����", "���", "�������������"
		};
		Component[] cmp = new Component[hash.length];
		for (int z = hash.length - 3; z < hash.length; z++) cmp[z] = Men.men;
		setLayout(new BorderLayout());
		add(PropertiesTable.getScrolled(
				new PropertiesTableModel(
				hash, this,
				new String[] { "<html>������ <font color=gray size=2>(��������)", "<html>������ <font color=gray size=2>(������)",
						"������� �����������", null, "�������� �����",	"���� � �����", null, "<html>�������� <font color=gray size=2>(���������)",
						"<html>�������� <font color=gray size=2>(���������)", "�.�.",	null,	null, "����� ��������" }
		), cmp, 130));
	}
	
	public Object getData() {	return MainFrame.data.get("�������������������������");	}
}