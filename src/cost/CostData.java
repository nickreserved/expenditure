package cost;

import common.HashObject;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import tables.*;

public class CostData extends JPanel implements DataTransmitter {
	final static protected String[] cosT = { "��������� �����", "��������� - ��������� - ��������" };
	final static protected String[] contesT = { "�������� �����������", "��������� �����������", "����� ����������" };

	public CostData(ItemListener il) {
		final String[] hash = { null, "������������", "����������������", "�����������", "�����������",
				"������������", "����", "��", "��", "������", "������������������", "���������",
				"����������������������", "��������������������", "��������������������",
				"�����������������������������������", "���������������������������������", "���������������������������������",
				null, null, "����", "������޸����", "���������",
				"������������������������", "������������������������������", "��������������������������������������", "������������������������������",
				"����������������������", "�������������������",
				"������������������������������������", "����������������������������������", "����������������������������������",
				null, null, "�������������", "��������������", "����������", "����������������������", "���������������",
				"��������������", "����������������", "�������", "�������������������", "�����������������", "�����������������",
				null, null, "���������������", "������", "������������������", "�������", "�������������", "����",
				"���������", "�����������������", "�����������������", "��", "�����", "���", "�������������", "������"
		};
		JComboBox[] cmp = new JComboBox[hash.length];
		cmp[1] = new JComboBox(cosT);
		cmp[2] = new JComboBox(contesT);
		cmp[1].addItemListener(il);
		cmp[2].addItemListener(il);
		cmp[22] = Providers.providers;
		cmp[41] = Providers.providers;
		for (int z = 11; z < 11 + 7; z++) cmp[z] = Men.men;
		for (int z = 27; z < 27 + 5; z++) cmp[z] = Men.men;
		for (int z = 42; z < 42 + 3; z++) cmp[z] = Men.men;
		for (int z = hash.length - 4; z < hash.length; z++) cmp[z] = Men.men;
		setLayout(new BorderLayout());
		add(PropertiesTable.getScrolled(
				new PropertiesTableModel(hash, this,
				new String[] { "<html><b>������ �������", "����� �������", "�����������", "��� ��������", "��� ����������� ���������",
						null, null, null, null, "<html>������ <font color=gray size=2>(���������)", "���������� ��������", "����� �����",
						"�������� ������ ��������", "�' ����� ������ ��������", "�' ����� ������ ��������",
						"�������� �����. �����. ���������", "�' ����� �����. �����. ���������", "�' ����� �����. �����. ���������",
						null, "<html><b>������� �����", "<html>���� <font color=gray size=2>(���������)", "<html>������� ����� <font color=gray size=2>(��������� �� �����)",
						"��������� � ��������������", "���������� �����. ����. ��������", "���������� �����. �����������",
						"���������� ����. �����. ���������", "���������� ������. ���������", "�������� ������ ��������", "����� ������ ��������",
						"�������� ����. �����. ���������", "�' ����� ����. �����. ���������",	"�' ����� ����. �����. ���������",
						null, "<html><b>�����������", "��� ���������� �����������", "��� ����������� �����������", "<html>���������� <font color=gray size=2>(�������� �� '<b>,</b>')",
						"<html>����������� ����������� <font color=gray size=2>(�������� �� '<b>,</b>')", "<html>���� ����������� <font color=gray size=2>(���������)",
						"���/����������",	"<html>����� ��� �� ��������� <font color=gray size=2>(��������� �� �����)", null,
						"�������� �����������", "�' ����� �����������", "�' ����� �����������",
						null, "<html><b>���������� ��������", "<html>������ <font color=gray size=2>(��������)", "<html>������ <font color=gray size=2>(������)",
						"������� �����������", null, "�������� �����",	"���� � �����", null, "<html>�������� <font color=gray size=2>(���������)",
						"<html>�������� <font color=gray size=2>(���������)", "�.�.", null, null, "����� ��������", null }
		), cmp, 230));
	}
	
	@Override
	public HashObject getData() { return (Cost) MainFrame.costs.get(); }
}