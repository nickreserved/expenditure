<?php
require_once('functions.php');
init(6);

if (!$data['�����']['��']) trigger_error('��� �������� ��������� �� ��', E_USER_ERROR);
// ��� ��������������� �� ������ ���� �������������� ����� � �������, ����������� �� ���������� ��� functions.php
// ����������� ���������� ���� ����.
$per_months = array();
foreach($data['���������'] as $invoice)
	if ($invoice['��']) $per_months[strftime('%B %Y', $invoice['����������'])][] = $invoice;
// ����������� ����� �� � ��� ��� �������� ������ ����������.
foreach($per_months as & $per_month)
	$per_month = array(
		'���������' => $per_month,
		'�����' => calc_sum_of_invoices_prices($per_month)
	);
unset($per_month); // �� ��� ���������� � ������� ���, �� ��������� �������� ��� array �� ����� ���� �� �� �����, ������ ��������:
// ��������� ��� ���� ����
foreach ($per_months as $date => $per_month) {
?>

\sectd\pgwsxn11906\pghsxn16838\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

\pard\plain\fs21\trowd\cellx7370\cellx10206
���� �� ���\line ��������: <?=$date?>\cell
������:\line ������� �������:\line ���. ������:\cell\row

\pard\plain\qc{\b � � � � � �}\par\par
{\fs20 �������� ��������������� ����� ��� ���������� ���� ������ ������ � ������� ��������� ��� ��� �������� ���������, �.�.�., �.�.�.�. �.�.�. (����������� ��' ���������� 1 ������ 37� �.�. 3323/1955)}\par\par

\pard\plain\trowd
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\cellx5670
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx10206
\qc <?=rtf($data['������ ������'])?>\line\b ��������\b0\line\line <?=rtf(get_unit_address())?>\line\b ��������� (���� - ���� - �.�.)\b0\line\line 090153025\line\b �.�.�.\b0\cell
����������� ������\line\b ������ �����\b0\line\line <?=rtf($data['��������']) ?>\line\b ��. ���������\b0\line\line ��� �. �������\line\b �.�.�.\b0\cell\row

\pard\plain\par\par


\trowd\trpaddfl3\trpaddl28\trpaddfr3\trpaddr28
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\cellx3118
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\cellx5386
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\cellx8220
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx10206
\qc ����� ����������\cell ������ ����\cell ����������� �����\cell ���� �����\cell\row
<?php
	foreach($per_month['���������'] as $invoice)
		echo '\qc ' . $invoice['���������'] . '\cell\qr ' . euro($invoice['�����']['������ ���� ��� ��'])
			. '\cell\qc ' . percent($invoice['��']) . '\cell\qr ' . euro($invoice['�����']['��'])
			. '\cell\row' . PHP_EOL
?>
\trowd\trpaddfl3\trpaddl28\trpaddfr3\trpaddr28
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\cellx3118
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\cellx5386
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx10206
\qr ������\cell <?=euro($per_month['�����']['������ ���� ��� ��'])?>\cell <?=euro($per_month['�����']['��'])?>\cell\row
\ql ����� ���� �����������\cell\cell\cell\row
\qr ������ ������\cell\cell\cell\row

\pard\plain\par\qc{\fs24\b\ul �������� ������}\par\par
\ql{\fs20 �������� �������� ��� �� �������� ��� ��������� ��� ����� ��� �������� ��� �������� �������.}\par\par
\qc {\ul � �����}\line\line\line\line <?=rtf($data['����� ��������']['�������������'])?>\line <?=rtf($data['����� ��������']['������'])?>\par\par

\trowd\cellx3402\cellx6804\cellx10206\qc\fs20
����������� - ���������\cell ������������\cell �����������\cell\row
\trowd\trpaddfl3\trpaddl113\trpaddfr3\trpaddr113
\cellx1701\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx3402
\cellx5103\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx6804
\cellx8505\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx10206\qr
����������\cell\cell ����\cell\cell ����\cell\cell\row
����������\cell\cell ��. ����������\cell\cell ��. ����������\cell\cell\row
����������\cell\cell ����������\cell\cell ����������\cell\cell\row
\pard\plain\par
\trowd\cellx3402\cellx6804\cellx10206\qc
� ��������� - ��������\cell � �������\cell � ��������� �� ��������\cell\row
\pard\plain

\sect

\sectd\lndscpsxn\pgwsxn16838\pghsxn11906\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

\pard\plain\qc{\fs24\b\ul ������� ������������}\par\par
{\fs20 ��� ��� ������ ������������� ���� ������ ����� � ��������� �� �������� ������������\line ����� ����������� ��� ������. ��' ��� ������. 1 ��� ������ 37� ��� �.�. 3323/1955.}\par\par

\trowd\trpaddfl3\trpaddl28\trpaddfr3\trpaddr28\fs20
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\cellx3175
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\cellx4252
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\cellx7262
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\cellx9468
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\cellx11169
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\cellx12416
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\cellx13946
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx15137
��������\cell �.�.�.\cell ���������\cell ����� ����������\cell ���������\cell\ ������ ����\cell ������� �����\cell ���� �����\cell\row
<?php
	foreach($per_month['���������'] as $invoice) {
		$contractor = $invoice['����������'];
		echo '\ql ' . rtf($contractor['��������']) . '\cell\qc ' . rtf($contractor['���']) . '\cell\ql '
				. rtf($contractor['���������']) . '\cell\qc ' . $invoice['���������'] . '\cell '
				. $invoice['���������'] . '\cell\qr ' . euro($invoice['�����']['������ ���� ��� ��'])
				. '\cell\qc ' . percent($invoice['��']) . '\cell\qr ' . euro($invoice['�����']['��'])
				. '\cell\row' . PHP_EOL;
	}
?>
\trowd\trpaddfl3\trpaddl28\trpaddfr3\trpaddr28\fs20
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\cellx11169
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\cellx12416
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx15137
\qr ������:\cell <?=euro($per_month['�����']['������ ���� ��� ��'])?>\cell <?=euro($per_month['�����']['��'])?>\cell\row
\pard\plain\qr <?=rtf($data['����']) . ', ' . strftime('%d %b %y', time())?>\par

\pard\plain\fs23\par
\trowd\trkeep\cellx7568\cellx15136\qc
��������\line - � -\line �����\line\line\line <?=rtf($data['�����']['�������������'])?>\line <?=rtf($data['�����']['������'])?>\cell
\line - � -\line ����� �����\line\line\line <?=rtf($data['����� ��������']['�������������'])?>\line <?=rtf($data['����� ��������']['������'])?>\cell\row

\sect

<?php
}

unset($contractor, $date, $invoice, $per_month, $per_months);

rtf_close(__FILE__);