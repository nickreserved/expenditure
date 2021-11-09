<?php
require_once('functions.php');
require_once('order.php');
require_once('report.php');

init(6);

if (has_direct_assignment()) {

start_35_20();
order_publish(ifexist($data, '������� ��������� ��������'));
order_header_recipients(ifexist2($output, $data, '������� ��������� ��������'), $output, null,
		'������� ��������� �������� ��� ' . rtf(ucwords($data['������'])),
		array(
			'��.721/1970 (��� �\' 251) ���� ������������ �������� ��� ���������� ��� ������� ��������',
			'N.2292/1995 (��� �\' 35) ��������� ��� ���������� ���������� ������� ������, �������� ��� ������� ������� �������� ��� ����� ���������',
			'�.3861/2010 (��� �\' 112) ��������� ��� ���������� �� ��� ����������� �������� ����� ��� ������� ��� ������������, ����������� ��� ��������������� ������� ��� ��������� "��������� ��������" ��� ����� ���������',
			'�.4270/2014 ������ �������������� ����������� ��� ��������� (���������� ��� ������� 2011/85/��) - ������� ��������� ��� ����� ���������',
			'�.4412/2016 (��� �\' 147) ��������� ��������� �����, ���������� ��� ��������� (���������� ���� ������� 2014/24/�� ��� 2014/25/��)�',
			'�.032/8/66625/�.15516/05 ��� 17/������� ������ (��� �\' 3495)',
			$data['������� �������� ����������']['���������']
		));
?>1.\tab ������� ����� �� �������:\par
\qc{\b � � � � � � � � � � � �}\par\qj
��� ������� ��� �������� ��� �<?=rtf($data['������'])?>�, ���� ��������:\par
<?php
$count = count($data['����������']) > 1 ? 1 : 0;
$contracts = array();
$to = array();	// ���������
foreach($data['����������'] as $per_contractor) {
	if (isset($per_contractor['�������'])) {
		if (isset($per_contractor['�����������'])) continue;
		$contracts[] = $per_contractor['�������']['�������'];
	}
	$to[] = get_contractor_recipient($per_contractor['����������']);
	$invoices = $per_contractor['���������'];
	$contractor = $per_contractor['����������'];
	echo '\tab ' . ($count ? greeknum($count++) . '.\tab ' : null);
	echo isset($per_contractor['�������']) && $per_contractor['�������']['������'] != $data['������']
		? '��� �' . rtf($per_contractor['�������']['������']) . '� �' : '�';
	echo '��� ���������� ' . rtf(get_contractor_full_info($contractor));
	?>, �� �� �������� ��������� ���������� ��������.\par
<?php report($invoices, $per_contractor['�����']); ?>
\pard\plain\sb120\sa120\fi567\tx1134\tx1701\tx2268\qj
<?php } ?>

2.\tab � �� ��� ������ ���� �������� �� �� �������� ��������:\par
\tab �.\tab ���: <?=$data['���']?>.\par
\tab �.\tab ��� �����: <?=$data['����� ��������������']?><?php
if ($data['����� ��������������'] != '����� �����')
	echo " ����� " . date('Y', orelse($data, '���������� ���������� ����������', time()));
?>.\par
\tab �.\tab ������������: �.�. <?=$data['��']?>.\par
3.\tab ���� �� ����� �������<?=$contracts ? ' �� ���� ' . (count($contracts) == 1 ? '��� �������� ' : '��� ��������� ') . get_names($contracts) . ' ���' : null?> �� ��������� ��� (�) �������� �����.\par
4.\tab �������� ����������� �����: <?=rtf($data['������ ������'] . ', ' . get_unit_info())?>.\par
5.\tab ��������� �������: <?=personi($data['����� ��������'], 0)?>, ���. <?=rtf($data['��������'])?>.\par
<?php
order_footer($output);
order_recipient_table($to, array($data['������']));
?>

\sect

<?php
unset($contractor, $contracts, $count, $invoices, $per_contractor, $to);

}	// if

rtf_close(__FILE__);