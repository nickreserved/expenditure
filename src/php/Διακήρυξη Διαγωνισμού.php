<?php
require_once('init.php');
require_once('order.php');
require_once('contract.php');
require_once('statement.php');
require_once('tender.php');
require_once('header.php');

foreach($data['�����������'] as $per_tender) {
	$tender = $per_tender['�����������'];
	// ����� ������ ��������
	$payer = null;
	order($data['������� �������� ����������'], $payer);
	$payer = $payer[5];
	// ������� ��������� ��� ����������� (������ ��� �� ��������� �� ����� ��� ����� ���������)
	foreach($per_tender['���������'] as $invoice) {
		$total = $invoice['���������']['������'];
		if (!isset($deduction)) $deduction = $total;
		else if ($total != $deduction) { unset($deduction); break; }
	}

	start_35_20();
	order_publish($tender, '��� ����������', $output);
	order_header_recipients(ifexist2($output, $tender, '��������� �����������'), $output, null, '��������� �����������',
			array(
				'�.4412/2016 (��� �\' 147) ��������� ��������� �����, ���������� ��� ��������� (���������� ���� ������� 2014/24/�� ��� 2014/25/��)�',
				'�.4314/2014 (��� �\' 265) ��) ��� �� ����������, ��� ������ ��� ��� �������� ������������ ����������� ��� ��� ������������� ������� 2014-2020, �) ���������� ��� ������� 2012/17 ��� ���������� ������������ ��� ��� ���������� ��� 13�� ������� 2012 (EE L 156/16.6.2012) ��� �������� ������, ����������� ��� �. 3419/2005 (�1297) ��� ����� ��������� ��� ��� �.3614/2007 (��� �\' 267) �����������, ������� ��� �������� ������������ ����������� ��� ��� ������������� ������� 2007-2013�',
				'�.4270/2014 (��� �\' 143) ������ �������������� ����������� ��� ��������� (���������� ��� ������� 2011/85/��) - ������� ��������� ��� ����� ���������',
				'�.4250/2014 (��� �\' 74) ������������ ������������� - �����������, ������������ ������� �������� ��� ��������� ��� �������� �����-����������� ��������� ��� �.�. 318/1992 (��� �\' 161) ��� ������ ��������� ��� ���������� ��� ��������� ��� ������ 1',
				'���. � ��� �.4152/2013 (��� �\' 107) ����������� ��� ��������� ���������� ���� ������ 2011/7 ��� 16.2.2011 ��� ��� ������������ ��� ������������� �������� ���� ��������� ����������',
				'�.4129/2013 (��� �\' 52) ������� ��� ������ ����� ��� �� ��������� ��������',
				'����� 26 ��� �.4024/2011 (��� �\' 226) ����������� ���������� ������� ��� ��������� ��� ������� ��� ����� ���� �� �������',
				'�.4013/2011 (��� �\' 204) �������� ������� ����������� ����� �������� ��������� ��� ��������� ������������ ������� �������� ����������',
				'�.3861/2010 (��� �\' 112) ��������� ��� ���������� �� ��� ����������� �������� ����� ��� ������� ��� ������������, ����������� ��� ��������������� ������� ��� ��������� "��������� ��������" ��� ����� ���������',
				'����� 4 ��� �.�.118/07 (��� �\' 150)',
				'����� 5 ��� �������� �� �����. 11389/1993 (��� �\' 185) ��� �������� ����������',
				'�.3548/2007 (��� �\' 68) ����������� ������������ ��� ������ ��� �������� ��� ���������� ��� ������ ���� ��� ����� ���������',
				'�.2859/2000 (��� �\' 248) ������� ������ ����� ������������� �����',
				'�.2690/1999 (��� �\' 45) ������� ��� ������ ����������� ����������� ��� ����� ��������� ��� ����� ��� ������ 7 ��� 13 ��� 15',
				'�.2121/1993 (��� �\' 25) ����������� ����������, ��������� ���������� ��� ����������� ������',
				'�.�. 28/2015 (��� �\' 34) ������������� ��������� ��� ��� �������� �� ������� ������� ��� ��������',
				'�.�. 80/2016 (��� �\' 145) �������� ����������� ��� ���� ���������',
				'��\' �����. 57654 (��� �\' 1781/23.5.2017) ������� ��� �������� ���������� ��� ��������� �������� ����������� ������� ����������� ��� ����������� ��� ��������� ������������ ������� �������� ��������� (������) ��� ���������� ���������� ��� ���������',
				'��\' �����. 56902/215 (��� �\' 1924/2.6.2017) ������� ��� �������� ���������� ��� ��������� ��������� ������������ ��� ����������� ����������� ��� ������� ���������� ������������ �������� ��������� (�.�.�.��.�.)�',
				'��.721/1970 (��� �\' 251) ���� ������������ �������� ��� ���������� ��� ������� ���������',
				'N.2292/1995 (��� �\' 35) ��������� ��� ���������� ���������� ������� ������, �������� ��� ������� ������� �������� ��� ����� ���������',
				'�.032/8/66625/�.15516/05 ��� 17/������� ������ (��� �\' 3495)',
				$data['������� �������� ����������'] . ' ������� �������� ����������' . (published() ? " (���: {$data['��� �������� �������� ����������']})" : null),
			));
?>
1.\tab �� �������� ��� (��) ��������\par
\qc{\b � � � � � � � � � � � � �}\par
\qj ���������� ��� �<?=rtf($tender['������'])?>�.\par
2.\tab �� ����� ��� ����������� ����� <?=$tender['�����']?>, ������� �� ��� ��������� ��� ��������.\par
3.\tab � ������� ���� ��� ��������, ���� �� �� ���, ��������� ��� ���� ��� <?=euro($tender['������������'])?>, ��� ��������� ��� �� ��������� <?=isset($deduction) ? percent($deduction) . ' ' : null?>��� ��� ������� �����. ����������� ��������� ���� ��� �� ������� ���� �� �����������.\par
<?php if ($tender['�������� ���� �����']) { ?>
4.\tab ��������� � ���������� ����� ��������������� �� ��������� �������� ��� ������ ����� ��� �����������. ������ ���� ����������� �� ����������� �� ������ ��� ��������� ��� ����� ��� ��� �������� ��� ����. �������� ��� ��������, ����� ��� � ����������� ������� �� ����������� �� ������������� ��� ���� ���������.\par
<?php } else { ?>
4.\tab �� ��������� ��� �� ����������, ������ �� ������� �� �������� ����������� ��� ����������� ��� ��� ������ ����� �����. �������� ��� ��������, ����� ��� � ����������� �� ����������� �� ���� ���� �������.\par
<?php } ?>
5.\tab �� �������������� �� ���������� ��� ��������� ����, ��������� � �� ��������� � ������������, <?=rtf(at_unit_address())?>. � �������� ��� ��������� �� ����������� ����� ��� <?=strftime('%A, %d %b %y, ��� %H:%M', $tender['������ ������������ ���������'])?>, ������� ������ ����� ��� ������ ��� �� �������� ������ ���������. �������� �������� � ��������� ����, � �������� ��������� ���� ������� ���������� ��� ����������� ����� �������, ���� ��� ������� ��� ������������ �������������� ����������� ������.\par
6.\tab �� �������������� ���������� ��� ������ �� ������������ �� ��������������, ����� �� ��������:\par
<?php
	foreach($tender['�������������� ����������'] as $par => $document)
		echo '\tab ' . greeknum($par + 1) . '.\tab ' . $document . '.\par' . PHP_EOL;

	if ($tender['�����'] == '���������� �����������') {
?>
7.\tab �� ��������������, � ������� �������� ��� � ���������� ��������
<?php } else {
?>7.\tab � ���������� �������� �� ��������� ����� ������������ ������� ���� ����� �� ������������ ���' ��������� ����������� ���������, � �������� ��� �������������� ����������� ��� �� ��� ���. ���� � ������� ��� ����������� ���������, ��� ��� �� �������������� ��� � ������� ��������
<?php } ?>, �� ���������� ����� ������������ �������, ���� ����� �� ������������ ���' �������� � ���� ����������, � ��������� ��� ���������� ��� �����������<?=isset($tender['��������� �����������']) ? ' �' . order($tender['��������� �����������']) . '�' : null?>, � �������� ��� �������������� ����������� ��� �� ��� ���. ���� �� ������������ ��� ����� �������� ������������ (���������, ��������, ��������� ������������ ������������ ���).\par
8.\tab �� ��������� ����� �������� ������ ����������� 60 ������ ��� ��� ����������� ���������� �������� ����. �� ���� �������� ����������� ��������� �������� ������, � �������� ������������. �� ��� �����������, ���������� ��� � �������� ����� ��� 60 ������.\par
9.\tab � ����������� ��� ��������� �� ��������������� �� ������� ��������, <?=rtf(at_unit_address())?>, <?=strftime('%A, %d %b %y ��� ��� %H:%M', $tender['������ ������������ ���������'])?>.\par
10.\tab �������� ��� ����������� �� ���������� � �������������� �� ��� �������������� ��������, ���� �� ���� ��� ����.\par
11.\tab ���� ��� �������� ��� ��������, ����� �� ������ �� ����������� �� ����� ��� �� ��� �������, �� �������� �������������� �������������:\par
<?php contractor_legal_documents($tender); ?>
12.\tab � ������� ��� ����������� �� �� ��������, ��� ���������� ������ ��� �� ����� �������������� �� ����������, ��� ��� ��������, ����� ��������, ��� �� ���������� ��� �����������, ���� ��������.\par
13.\tab � ���������� ��� ����������� ����� ��� ��� ������� ��� �������� ��� ����������� �����.\par
14.\tab ��������� �������������� �� �������� ������:\par
\tab �.\tab ������������ ������ ��������� �������.\par
\tab �.\tab ������ ����������� ���������.\par
\tab �.\tab �������� ������, �� �������������� ������������, ����� ��� ��.\par
15.\tab �� ����������� ��� �������� ������������ ����� ���������� �������� ��� ������ ���������� �������� ��������� (CPV): <?=rtf($tender['CPV'])?><?php if (isset($tender['�������������� CPV'])) { ?> ��� ��������������� CPV: <?=rtf($tender['�������������� CPV'])?><?php } ?>.\par
16.\tab � ������ ���� �������� �� �� �������� ��������:\par
\tab �.\tab ������ ��������������: <?=rtf($payer)?>.\par
\tab �.\tab ������� ������ (��): <?=rtf($data['��'])?>.\par
\tab �.\tab ���������� ����������� ������/������ (���): <?=rtf($data['���'])?>.\par
\tab �.\tab ���������� ����: <?=date('Y', $tender['������ ������������ ���������'])?>.\par
\tab �.\tab ��� �����: <?=$data['����� ��������������']?>.\par
17.\tab ��������� ������� ��� �������� ��� �����������: <?=person($data['����� ��������'])?>, <?=rtf($data['�������� �����'])?>, ���.: <?=rtf($data['��������'])?>.\par
<?php
	order_footer($output);

	// ������� ���������
	$to = array();
	if (published()) $to[] = '������';
	else
		foreach($tender['��������������'] as $competitor)
			$to[] = get_contractor_recipient($competitor['��������������']);
	order_recipient_table($to, array($data['������']));

	// ������� ������������
	$appendices = array('������ ��������', '������� ���������');
	if ($data['����']) {
		$appendices[] = '��������� ������������';
		$appendices[] = '������ �������� �����������';
		$appendices[] = '������� �������� �����������';
		$appendices[] = '��������������� �������� ������������';
		$appendices[] = '�������������� ������������';
		$appendices[] = '������� ������';
	}
	order_appendices($appendices);
?>

\sect


<?php
	// �� �������������� ����������� 3
	if (!published() && count($tender['��������������']) < 3)
		trigger_error('�� ��������� �������������� ������ �� ����� ����������� 3');

	// ����������� ��� ��������� ���� �� � ��������� ����� �������� ��� ��� ����� �������
	if (!is_expenditure()) {
		// ������� ������������ "�": ������ ��������
		start_35_20();
		appendix_header(ifexist2($output, $tender, '��������� �����������'), '�', '������ ��������');
		echo '\pard\plain\brdrb\brdrdash\par' . PHP_EOL;
		export_contract($per_tender, false);
		echo '\pard\plain\brdrt\brdrdash\par' . PHP_EOL;
		appendix_footer($output);
		echo '\sect' . PHP_EOL . PHP_EOL;

		require('����.php');
		require('���������� ��������.php');
		// ������� ����� ��������� �������
		statement_representative(array(
			'��������' => '�������� ��������', '�������������' => '', '������������� ������' => '',
			'������������� �������' => '', '���������� ��������' => '', '����� ��������' => '',
			'������� ����������' => '', '��������� ���������' => '', '��������' => '',
			'���������� �������' => ''));
	}

}	// foreach

unset($appendices, $competitor, $deduction, $document, $invoice, $payer, $per_tender, $tender, $to, $total);

rtf_close(__FILE__);