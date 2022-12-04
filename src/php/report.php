<?php
require_once('functions.php');

/** ��������� ��� ���������� ���������, �� ��� ��� ��� ��� ����� ����������.
 * @param array $invoices ����� �� ���������
 * @return array 3 array �� �� �������� �������:
 * <ul><li>'���������': array �� ������� �� ������� ��� ��������� ��� ���������� ��� ����� ��
 * ���������� �������� �� �. (��� ������� ������� ���������, ���� ���� ��� �� �������� ������� ����
 * ����������)
 * <li>'��': array �� ������� �� ������� ��� �� ��� ���������� ��� ����� �� ���������� �������� �� �.
 * <li>'���': array �� ������� �� ������� ��� ��� ��� ���������� ��� ����� �� ���������� ��������
 * �� �.<ul> */
function calc_per_deduction_incometax_vat($invoices) {
	$deductions = array(); $vat = array(); $incometax = array();
	foreach($invoices as $invoice) {
		if ($invoice['��']) {			// ������� ��� �� ��� ���� ���������
			$key = $invoice['��']; $value = $invoice['�����']['��'];
			if (isset($incometax[$key])) $incometax[$key] += $value; else $incometax[$key] = $value;
		}
		if ($invoice['�����']['���������']) {	// ������� ��� ��������� ��� ���� ���������
			$key = (string) $invoice['���������']['������']; $value = $invoice['�����']['���������'];
			if (isset($deductions[$key])) $deductions[$key] += $value; else $deductions[$key] = $value;
		}
		foreach($invoice['���������� ���'] as $key => $value)	// ������� ��� ����� ��� ��� ���� ���������
			if (isset($vat[$key])) $vat[$key] += $value; else $vat[$key] = $value;
	}
	return array('���������' => $deductions, '��' => $incometax, '���' => $vat);
}

/** ������ �� �������� ���� �������.
 * @param array $invoices ����� �� �� ��������� ��� �������
 * @param array $prices ����� �� �� ���������� ��� ����� ��� ���������� */
function report($invoices, $prices = null) {
	if (!isset($prices)) $prices = calc_sum_of_invoices_prices($invoices);
	$categories = calc_per_deduction_incometax_vat($invoices);
	$cells = <<<'EOD'
\trautofit1\trpaddfl3\trpaddl28\trpaddfr3\trpaddr28
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx454
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx3799
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx5046
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx6066

EOD;
	if (count($categories['���']) > 1)
		$cells .= <<<'EOD'
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx6633

EOD;
	$cells .= <<<'EOD'
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx7654
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx8788

EOD;
?>
\pard\plain\fs21
\trowd\trhdr<?=$cells?>
\qc\b A/A\cell ���������\cell ������\line ��������\cell ��������\cell<?php if (count($categories['���']) > 1) echo ' ���\cell'; ?> ����\line �������\cell ����\line �������\b0\cell\row
\trowd<?=$cells?>
<?php
$count_items = 0;
foreach($invoices as $invoice)
	foreach($invoice['����'] as $item) {
		?>\qr <?=++$count_items?>\cell\qj <?=rtf($item['�����'])?>\cell\qc <?=rtf($item['������ M�������'])?>\cell <?=num($item['��������'])?>\cell\qr <?php if (count($categories['���']) > 1) echo percent($item['���']) . '\cell '; ?><?=euro($item['���� �������'])?>\cell <?=euro($item['�������� ����'])?>\cell\row
<?php } ?>
\pard\tx1\tqr\tx8760\trowd\trpaddfl3\trpaddl28\trpaddfr3\trpaddr28
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx8788
\b ������ ����:\tab <?=euro($prices['������ ����'])?>\b0\cell\row
<?php
	if ($invoices[0]['����������']['�����'] != '��������� ������')
		foreach($categories['���������'] as $k => $v)
			echo '+ ��������� ' . $k . '%:\tab ' . euro($v) . '\cell\row' . PHP_EOL;
	foreach($categories['���'] as $k => $v)
		echo '+ ��� ' . percent($k) . ':\tab ' . euro($v) . '\cell\row' . PHP_EOL;
	echo '\b ������������:\tab ' . euro($prices['������������']) . '\b0\cell\row' . PHP_EOL;
	foreach($categories['���������'] as $k => $v)
		echo '- ��������� ' . $k . '%:\tab ' . euro($v) . '\cell\row' . PHP_EOL;
	echo '\b ��������:\tab ' . euro($prices['��������']) . '\b0\cell\row' . PHP_EOL;
	foreach($categories['��'] as $k => $v)
		echo '- �� ' . percent($k) . ':\tab ' . euro($v) . '\cell\row' . PHP_EOL;
	if (count($categories['��']))
		echo '\b �������� ��������:\tab ' . euro($prices['�������� ��������']) . '\b0\cell\row' . PHP_EOL;
}

/** ������ �� �������� ���� �������, ����� �����, ��� ��� ������ ��������.
 * ��������������� ������������ ���� ������� ���������� �����������.
 * @param array $invoices ����� �� �� ��������� ��� ������� */
function report_no_prices($invoices) {
	$categories = calc_per_deduction_incometax_vat($invoices);
	report_no_prices_no_sums($invoices); ?>

\pard\tx1\tqdec\tx8760\trowd\trpaddfl3\trpaddl28\trpaddfr3\trpaddr28
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx8788
{\b ������ ����:}\cell\row
<?php
	if ($invoices[0]['����������']['�����'] != '��������� ������')
		foreach($categories['���������'] as $k => $v)
			echo '+ ��������� ' . $k . '%:\cell\row' . PHP_EOL;
	foreach($categories['���'] as $k => $v)
		echo '+ ��� ' . percent($k) . ':\cell\row' . PHP_EOL;
	echo '{\b ������������:}\cell\row' . PHP_EOL;
	foreach($categories['���������'] as $k => $v)
		echo '- ��������� ' . $k . '%:\cell\row' . PHP_EOL;
	echo '{\b ��������:}\cell\row' . PHP_EOL;
	foreach($categories['��'] as $k => $v)
		echo '- �� ' . percent($k) . ':\cell\row' . PHP_EOL;
	if (count($categories['��']))
		echo '{\b �������� ��������:}\cell\row' . PHP_EOL;
}

/** ������ �� �������� ���� �������, ����� �����, ��� ��� ������ ��������.
 * ��������������� ������������ ���� ������� ���������� �����������.
 * @param array $invoices ����� �� �� ��������� ��� ������� */
function report_no_prices_no_sums($invoices) {
	$cells = <<<'EOD'
\trautofit1\trpaddfl3\trpaddl28\trpaddfr3\trpaddr28
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx454
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx3799
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx5046
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx6066
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx7654
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx8788

EOD;
?>
\pard\plain\fs21
\trowd\trhdr<?=$cells?>
\qc\b A/A\cell ���������\cell ������\line ��������\cell ��������\cell ����\line �������\cell ����\line �������\b0\cell\row
\trowd<?=$cells?>
<?php
$count_items = 0;
foreach($invoices as $invoice)
	foreach($invoice['����'] as $item) {
		?>\qr <?=++$count_items?>\cell\qj <?=rtf($item['�����'])?>\cell\qc <?=rtf($item['������ M�������'])?>\cell <?=num($item['��������'])?>\cell\cell\cell\row
<?php }
}


/** ������ �� �������� ���� �������, ����� �����.
 * ��������������� ������������ ���� ���� ��������� �������� ���������.
 * @param array $invoices ����� �� �� ��������� ��� ������� */
function report_no_prices_no_sums2($invoices) {
	$cells = <<<'EOD'
\trautofit1\trpaddfl3\trpaddl28\trpaddfr3\trpaddr28
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx454
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx4366
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx5613
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx6916
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx8788

EOD;
?>
\pard\plain\fs21
\trowd\trhdr<?=$cells?>
\qc\b A/A\cell ���������\cell ������\line ��������\cell ��������\cell ������������\b0\cell\row
\trowd<?=$cells?>
<?php
$count_items = 0;
foreach($invoices as $invoice)
	foreach($invoice['����'] as $item) {
		?>\qr <?=++$count_items?>\cell\qj <?=rtf($item['�����'])?>\cell\qc <?=rtf($item['������ M�������'])?>\cell <?=num($item['��������'])?>\cell\cell\row
<?php }
}