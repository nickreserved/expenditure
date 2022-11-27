<?php
require_once('functions.php');

init(6);
$invoices = array();
foreach($data['����������'] as $per_contractor) {
	if (isset($per_contractor['�������'])) continue;
	$invoices = $per_contractor['���������'];

	foreach(get_invoices_by_category($invoices) as $a => $invoices) {
		// �������� ����������� - ������ ������� �� ��� ������ ����������
		$c = count($invoices) > 1 ? '�' : '�';
?>

\sectd\sbkodd\pgwsxn11906\pghsxn16838\marglsxn1984\margrsxn1134\margtsxn1134\margbsxn1134\facingp\margmirror

\pard\plain\qr <?=rtf($data['�����������'])?>\line <?=rtf($data['������'])?>\line <?=rtf($data['����']) . ', ' . strftime('%d %b %y', get_newer_timestamp($invoices))?>\par\par
\fs24\qc{\b �������� ��������� <?=$a ? '����������' : '���������'?>}\par\par

\qj ����������� � �������� ��� �������� �������� <?=$a ? '��� �������� ���� ����������� ������, ' : ''?>��� ��������� �����, ��� ������������ ��<?=$c?> ��' ������� <?=get_names_key($invoices, '���������')?> ��������<?=$c?> <?=$a ? '���������� ������' : '������� ���������'?> �� ����� ������� � ����������� ������ <?=rtf(get_contractor_id($per_contractor['����������']))?>.\par
<?php report($invoices); ?>
\pard\plain\par
\pard\plain\qc\li4535 ���������\line - � -\line �����\line\line\line <?=rtf($data['�����']['�������������'])?>\line <?=rtf($data['�����']['������'])?>\par

\sect

<?php }
}

unset($a, $c, $invoices, $per_contractor);

rtf_close(__FILE__);