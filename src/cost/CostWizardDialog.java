package cost;

import common.Functions;
import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

final public class CostWizardDialog extends JDialog implements ActionListener, DocumentListener {

	public CostWizardDialog(Window w) {
		super(w, "������ ����������");
		
		setLayout(new BorderLayout());
		((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		Box bv = Box.createVerticalBox();
		
		Box bh = Box.createHorizontalBox();
		bh.add(new JLabel("���� �������������� ��� �������:"));
		bh.add(Box.createHorizontalStrut(5));
		bh.add(cbMoney = new JComboBox(new String[] { "����� �����", "�������",
				"������ ��������� ���������� (��������� ���������)" }));
		bv.add(bh); bv.add(Box.createVerticalStrut(5));

		bh = Box.createHorizontalBox();
		bh.add(new JLabel("����� �������:"));
		bh.add(Box.createHorizontalStrut(5));
		bh.add(cbCost = new JComboBox(new String[] { "���������, ��������, ��������� ����� ��", "������" }));
		bv.add(bh); bv.add(Box.createVerticalStrut(5));

		bh = Box.createHorizontalBox();
		bh.add(new JLabel("����� ����������:"));
		bh.add(Box.createHorizontalStrut(5));
		bh.add(cbProvider = new JComboBox(new String[] { "������� (��������, �����������, ���������, �������)",
				"������� (������, ��������, �.�.�.)", "������� (���, ���, �.�.�.)", "����������� (��������� �������������)" }));
		bv.add(bh); bv.add(Box.createVerticalStrut(5));

		bh = Box.createHorizontalBox();
		bh.add(new JLabel("����� ����������:"));
		bh.add(Box.createHorizontalStrut(5));
		bh.add(cbBill = new JComboBox(new String[] { "��������� ������", "������ ���������", "����� ����� ��������" }));
		bv.add(bh); bv.add(Box.createVerticalStrut(5));
		
		bh = Box.createHorizontalBox();
		bh.add(new JLabel("������ ���� ����������:"));
		bh.add(Box.createHorizontalStrut(5));
		bh.add(tfValue = new JTextField());
		bv.add(bh); bv.add(Box.createVerticalStrut(5));

		bh = Box.createHorizontalBox();
		bh.add(new JLabel("<html>" + lblBills));
		bh.add(Box.createHorizontalStrut(5));
		bh.add(tfValueProvider = new JTextField());
		bv.add(bh); bv.add(Box.createVerticalStrut(5));

		getContentPane().add(bv, BorderLayout.PAGE_START);

		tpInfo = new JTextPane();
		tpInfo.setEditable(false);
		tpInfo.setContentType("text/html");
		getContentPane().add(new JScrollPane(tpInfo), BorderLayout.CENTER);

		setSize(650, 450);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		
		cbMoney.addActionListener(this);
		cbProvider.addActionListener(this);
		cbCost.addActionListener(this);
		cbBill.addActionListener(this);
		tfValue.getDocument().addDocumentListener(this);
		tfValueProvider.getDocument().addDocumentListener(this);
		
		calc();
	}

	private final String lblBills = "������ ���� <b>����</b> ��� ���������� ��� <b>�����</b> ����������:";
	private final JComboBox cbMoney;
	private final JComboBox cbProvider;
	private final JComboBox cbCost;
	private final JComboBox cbBill;
	private final JTextField tfValue;
	private final JTextField tfValueProvider;
	private final JTextPane tpInfo;

	@Override public void actionPerformed(ActionEvent e) { calc(); }
	@Override public void insertUpdate(DocumentEvent e) { calc(); }
	@Override public void removeUpdate(DocumentEvent e) { calc(); }
	@Override public void changedUpdate(DocumentEvent e) { calc(); }
	
	private void calc() {
		try {
			int idxMoney = cbMoney.getSelectedIndex();
			int idxCost = cbCost.getSelectedIndex();
			int idxProvider = cbProvider.getSelectedIndex();
			int idxBill = cbBill.getSelectedIndex();
			
			double value = Functions.round(Double.parseDouble(tfValue.getText()), 2);
			double valueprovider = 0;
			boolean fpa = true;
			double hold = 0;
			boolean agreement = false;
			int fe = 0;			
			String text = "<html><style>ul {margin-top: -15px; margin-bottom: 0}</style>";
			
			try {
				valueprovider = Functions.round(Double.parseDouble(tfValueProvider.getText()), 2);
			} catch (NumberFormatException e) {}
			if (valueprovider < value) valueprovider = value;
			text += "<b>������ ����:</b> " + value + " �<br>" + lblBills + " " + valueprovider + " �";
			
			switch(idxMoney) {
				case 0 /*����� �����*/:
				case 1 /*�������*/:
					switch(idxProvider) {
						case 0 /*�������*/:
							if (valueprovider < 2500) hold = 4.15816; else { hold = 4.22032; agreement = true; }
							break;
						case 1 /*�������*/:
						case 2 /*�������*/:
							cbCost.setSelectedIndex(idxCost = 1);	// ������ �������
							if (idxProvider == 1 /*�������*/ && idxBill == 1 /*������ ���������*/)
								cbBill.setSelectedIndex(idxBill = 0); //��������� ������
							hold = 4;
							break;
						default /*�����������*/:
							cbCost.setSelectedIndex(idxCost = 1);	// ������ �������
							cbBill.setSelectedIndex(idxBill = 1); // ������ ���������
							hold = 4.096;
					}
					if (idxMoney == 0 && hold != 0) hold = Functions.round(hold + 10, 5);
					break;
				case 2 /*��.��.*/:
					cbProvider.setSelectedIndex(idxProvider = 0);
					if (valueprovider < 2500) hold = 0; else { hold = 0.06216; agreement = true; }
			}

			if (idxProvider == 1 /*�������*/) fpa = false;
			text += "<br><b>���:</b> " + (fpa ? "����������� ��� �� �������� � �����������" : "0%");

			text += "<br><b>���������:</b> " + hold + "% ��� ������� ����� (" +
					Math.round(value * hold) / 100.0 + " �), ��� �������� " +
					(idxProvider != 0 /*�������*/ && idxProvider != 3 /*�����������*/ ? "����" : "��� ����������") +
					"<br><b>������������:</b> ������ ����" + (fpa ? " + ���" : "") +
					(idxProvider != 0 /*�������*/ && idxProvider != 3 /*�����������*/ ? " + ���������" : "");			
			
			{
			double valueforfe = value;
			if (idxProvider == 0 /*�������*/ && valueprovider > 150 /*���������� ��� ����������*/) {
				final int[] a = { 4, 8, 1 };
				fe = a[idxBill];	// ����� ����������
				if (idxBill == 1 /*������ ���������*/ && idxCost == 0 /*��������� �����*/) fe = 3;
				else valueforfe = value - Math.round(hold * value) / 100.0;
			}
			text += "<br><b>��:</b> " + fe + "% ��� ������� �����" +
					(fe == 3 ? "" : " ����� ���������") + " (" + Math.round(valueforfe * fe) / 100.0 + " �)<br>";
			}
	
			if (idxCost == 0 /*��������� �����*/ && idxBill == 1 /*������ ���������*/)
				text += "<br>� ��������� ������ �� ��� �������� �� ��������� ����������� ��������� ��� 1% <b>�����</b> (" + Functions.round(0.01 * value, 2) +
						" �) ��� 0.5% <b>���</b> (" + Functions.round(0.005 * value, 2) + " �) ��� ������� �����, ����� ��� �� ��������� ��� ��������� ��� ��� �������� �� ����.";
			
			if (idxProvider == 0 /*�������*/) {
				if (valueprovider > 1500) text += "<br>���������� ���������� ����������� ��� ���������� ��� �������� ��� �� �������.";
				else if (valueprovider > 1220) text += "<br>�� �� ������������ ����� ���� ��� 1500�, ���������� ���������� ����������� ��� ���������� ��� �������� ��� �� �������.";
				if (valueprovider > 3000) text += "<br>���������� ����������� ����������� ��� ����������.";
				else if (valueprovider > 2440) text += "<br>�� �� ������������ ����� ���� ��� 3000�, ���������� ����������� ����������� ��� ����������.";
			}
			if (valueprovider > 60000) { text += "<br>���������� �������� �����������."; agreement = true; }
			else if (valueprovider > 15000 || idxCost == 0 /*��������� �����*/)
				{ text += "<br>���������� ��������� �����������."; agreement = true; }
			if (agreement) text += "<br>���������� ������ �������� �� ��� ����������.";
			
			tpInfo.setText(text);
		} catch(NumberFormatException e) {
			tpInfo.setText("����������� ����� �� �������� ����� ��� �� ������ ����������� ��� �� ��������� ���� ��� �� ������.<br>�� ��������� ��� �� �� ������������� ���� ��� �.830/60/918814/�.5965/2 ��� 16/���/���/3� ��� ��� 1191/14 ��� 17 (��� �' 969).");
		}
	}
}
