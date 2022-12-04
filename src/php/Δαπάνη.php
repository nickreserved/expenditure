<?php
require_once('contract.php');
require_once('tender.php');
require_once('statement.php');

/* ������ ��������� �������.
 * @param string $name �� ������� ��� ���������� ���� �� ��� ������ ���, ��� ����� ��� ������� ���
 * �� ������� ���������� */
function export_subfolder($name) {
	$pos = strpos($name, ':', 13);
	$num = substr($name, 0, $pos);
	$name = substr($name, $pos + 2);
?>

\sectd\sbkodd\pgwsxn11906\pghsxn16838\marglsxn1984\margrsxn1133\margtsxn6850\margbsxn5669

\pard\plain\sa283\qc\fs72 <?=rtf($num)?>\par\hyphpar0\fs46 <?=rtf($name)?>\par

\sect

<?php

}

// ��� �� ���� ������� ��������� ��� �������� (true) ��� ��� ������ (false)
// ��������� �� �� '������', ������������ �� true ��� ���������� ��� �������� ������
$output = '������';
// �� � ������� "���� ��� ����" ����� ������
$onlyone = isset($_ENV['one']) && $_ENV['one'] == 'true';

init(8);

foreach($data['����� �����������'] as $paper_v)
	if ($paper_v['�������']) {
		ob_start();

		$name = $paper_v['��������������'];
		switch($name) {
			case '�������� ������, ������������� ���������� �����������':
				statement_common('statement_IBAN', $data['������ ������']); break;
			case '�������� ������, �� �������������� ������������ ��������, ����� ��� ��':
				statement_common('statement_representative', $data['������ ������']); break;
			case '�������': export_contracts(); break;
			case '�������� ������������ ��������������� ����������': offer_unseal_reports(); break;
			default:
				if (substr($name, 0, 12) == '���������� �') {
					// ��� ������� ��� ����� ����� ���� ������� ������� �������� ��� ������ ��
					// ����������� ���������� �».
					// ��� �� ��� ���� ����� ���������� ����������� ���� ��������� ��������� ����
					// ���� ��������� �» ��� ��� ��� ����, �� ��� ������� ���������� �����������,
					// ����������� ���� ���������� �» ����� ������� �����������.
					// � ������������ �����, �� ������ ������ ������� ��������, �� ������� ��� ����
					// ��������� ��� ��� �� ��� ������� ���������� �».
					if (isset($data['���������']) || substr($name, 12, 1) != '�')
						export_subfolder($name);
				} else require($name . '.php');
		}

		echo str_repeat(ob_get_clean(), $onlyone ? 1 : $paper_v['������']);
	}

unset($output, $onlyone, $paper_v, $name);

rtf_close(__FILE__);