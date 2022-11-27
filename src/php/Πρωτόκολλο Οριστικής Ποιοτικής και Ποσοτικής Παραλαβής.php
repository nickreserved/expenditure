<?php
require_once('functions.php');
init(7);

if (isset($data['���������']))
	foreach($data['���������'] as $per_contract) {
		$invoices = $per_contract['���������'];
		foreach(get_invoices_by_category($invoices) as $a => $invoices) {
			$b = $a ? '����������' : '���������';
			// �������� ����������� - ������ ������� �� ��� ������ ����������
			$c = count($invoices) > 1 ? '�' : '�';
?>

\sectd\sbkodd\pgwsxn11906\pghsxn16838\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

\pard\plain\qr <?=rtf($data['�����������'])?>\line <?=rtf($data['������'])?>\line <?=rtf($data['����']) . ', ' . strftime('%d %b %y', get_newer_timestamp($invoices))?>\par\par
\fs24\qc\b ���������� ��������� ��������� ��� ��������� ��������� <?=$a[0] ? '����������' : '���������'?>\par\par
\pard\plain\sb120\sa120\tx567\tx1134\tx1701\qj
\tab ������ ��� <?=strftime('%d %b %y', get_newer_timestamp($invoices))?>, � �������� � ����� ����������� ��� ����:\par
\tab\tab �.\tab <?=personi($data["�������� ��������� $b"], 0)?>, �������� ��� ���������.\par
\tab\tab �.\tab <?=personi($data["� ����� ��������� $b"], 0)?>, ���\par
\tab\tab �.\tab <?=personi($data["� ����� ��������� $b"], 0)?>, ���� ��� ���������.\par
����� ��� ����������� ������ <?=$data['��� ����������� ���������']['���������']?>, ��������� ��� �������� ��� �������� ��������<?=$a ? ' ��� �������� ��� ����������� ������' : ''?>, ��� ��������� ����� �� ����� ������� � ����������� ������ <?=rtf(get_contractor_id($per_contract['����������']))?>, ������� �� ���� ����� ��� ��' ������� <?=$per_contract['�������']['�������']?> ��������, ��� ������������� ������������ ��� �<?=$c?> ��' ������� <?=get_names_key($invoices, '���������');?> ��������<?=$c?>.\par

<?php report($invoices); ?>

\pard\plain\sb120\sa120\tx567\tx1134\tx1701\qj
���� ���������� �� ����� �� ����������, ����������� ���� ��������:

\pard\plain\fs23\par
\trowd\trkeep\trqc\trautofit1\trpaddfl3\trpaddl113\trpaddfr3\trpaddr113
\clftsWidth1\cellx3402\clftsWidth1\cellx6804\clftsWidth1\cellx10206\qc
- � -\line ��������\line\line\line <?=rtf($data["�������� ��������� $b"]['�������������'])?>\line <?=rtf($data["�������� ��������� $b"]['������'])?>\cell
- �� -\line ����\line\line\line <?=rtf($data["� ����� ��������� $b"]['�������������'])?>\line <?=rtf($data["� ����� ��������� $b"]['������'])?>
\line\line\line <?=rtf($data["� ����� ��������� $b"]['�������������'])?>\line <?=rtf($data["� ����� ��������� $b"]['������'])?>\cell
- � -\line ��������� <?php
if ($data['����']) echo '����� �����\line\line\line ' . rtf($data['����� �����']['�������������']) . '\line' . rtf($data['����� �����']['������']);
elseif ($a) echo '������\line\line\line ' . rtf($data['������']['�������������']) . '\line' . rtf($data['������']['������']);
elseif (isset($data['����� �����'])) echo '�����������\line\line\line ' . rtf($data['����� �����']['�������������']) . '\line' . rtf($data['����� �����']['������']);
else echo '����� 4�� ��������\line\line\line ' . rtf($data['����� ��������']['�������������']) . '\line' . rtf($data['����� ��������']['������']);
?>\line\line\line ���������\line - � -\line ���������\line\line\line <?=rtf($data['�����']['�������������'])?>\line <?=rtf($data['�����']['������'])?>\cell\row

\sect

<?		}
	}

unset($a, $b, $c, $invoices, $per_contract);

rtf_close(__FILE__); ?>