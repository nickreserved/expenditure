<?php

/** ������� �� RTF ������, �� ����������, �� ������� ��� ���������� ��������.
 * �� RTF ������ ������� �� �� ��������� '}', ���� ��� script ������ �� ����������� ������ ������
 * ��������������� �� ����� ��� ������ �� �������� �� RTF ����� ��� ��������� ��� ���������� ��� ����
 * ��������������.
 * <p>�.�. � ������ ������ ��� �������������� ��������, ���� � ������� �������� ��� ���� ���. �����
 * ���� �� ����������� ��� PHP script ��� ������� (��� ��� �� ������ ��� ��������� �� require) �������
 * �� RTF ������.
 * @param string $file �� ����� ������� ��� script ��� �������� �������� ��� RTF ������� */
function rtf_close($file) {
	$a = get_required_files();
	if ($file == $a[0]) echo "\n\n\n}";
}


/** �������� �������� ����������.
 * ����� ���������� ��� ������� ��� ������� ������ ���� ��� ������� (specification) ��� RTF ����
 * ���� ����� escape �� �� ��������� backslash.
 * @param type $a ��� �������
 * @return type ������� ��� $a ��� ���������� ������� ��� RTF Spec ��� ������������� ���� */
function rtf($a) { return str_replace(array('\\', '{', '}'), array('\\\\', '\{', '\}'), $a); }

/** ������� ��� �������� ��� ���������� ���� ������������ ��������.
 * �� � ��������� ��� �������� ��� ����� ����, �� ��������� ���������� �� ��� ������ ���������.
 * @param string $order � ��������� ��� ��������
 * @param array $parts ���������� 6 string �������� �� ����� ����� � ������� �������, � �������
 * ����������, � ������� �����������, � ������� �������, � ���������� ��� �������� ��� � �������
 * @return string � ��������� ��� �������� �� ����� ���� */
function order($order, & $parts = null) {
	if (preg_match('/(�\.?\d{3}(\.\d+)?)\/(\d+)\/(\d+)\/(�\.?\d+)\/(\d{1,2} (���|���|���|���|���|����|����|���|���|���|���|���) (\d{2}))\/(.+)/', $order, $parts)) {
		$parts = array($parts[1], $parts[3], $parts[4], $parts[5], $parts[6], $parts[9]);
		return $order;
	}
	trigger_error(($order ? "�� '<b>$order</b>' ��� ����� ��������� ������������ ��������"
		: '� ��������� ������������ �������� ������ �� �������')
		. ' ��� ����� �.800.12/23/1234/�.123/31 ��� 19/3 ���/4� ��.', E_USER_ERROR);
}

/** ������� ��� ��������� ���� ����������
 * @param string $invoice � ��������� ��� ���������� ��� ������ �� ���� �� ����� 1234/31-12-2019
 * @param array $date �� � ��������� ��� ���������� ����� ������, ���������� ��� array �� 3 ��������:
 * ��� �����, �� ���� ��� �� ���� ������� ��� ����������, ��� ����������. �.�. 31, 12, 2019.
 * @return string � ��������� ��� ����������, ���� ������ */
function invoice($invoice, & $date = null) {
	if (preg_match('/\d+\/(\d{1,2})-(\d{1,2})-(\d{4})/', $invoice, $date)) {
		array_shift($date);
		if ($date[2] >= 2000 && $date[2] < date('Y') + 3 && checkdate($date[1], $date[0], $date[2]))
			return $invoice;
	}
	trigger_error(($invoice ? "�� '<b>$invoice</b>' ��� ����� ��������� ����������"
		: '� ��������� ���������� ������ �� �������') . ' ��� ����� 1324/31-12-2006', E_USER_ERROR);
}


/** ���������� ��� ������� �� ��������, ���������� ��� �������.
 * @param string $s �� ������� �������, �.�. '1�� �����'
 * @return string �� ������� ������� �� ��� �� �������� ��������, ����� �������, �.�. '1�� �����' */
function strtoupperg($s) {
	static $pre = array('�', '�', '�', '�', '�', '�', '�', '�');
	static $aft = array('�', '�', '�', '�', '�', '�', '�', '�');
	return str_replace($pre, $aft, strtoupper($s));
}
/** ���������� ��� ������� �� ��������, ���������� ��� ������� ��� �� ������� �� ��������.
 * @param string $s �� ������� �������, �.�. '1�� �����'
 * @return string �� ������� ������� �� ��� �� �������� ��������, ����� �������, �.�. '1�� �����' */
function strtouppergn($s) {
	return preg_replace_callback('/(\W|^)\D\w+/', function($m) { return strtoupperg($m[0]); }, $s);
}


/** ���������� ��� �������� ������������ ���� �������� �������.
 * @param int $n �������� ������� ��� �� 1 ����� �� 999
 * @return string � �������� ������������ ��� ������� (�.�. �� ��� �� 6) */
function greeknum($n) {
	if ($n < 1 || $n > 999) trigger_error("��� �� ����������� �� '<b>$n</b>' �� �������� �������� ������ �� ����� �������� ��� �� ������ ��� [1, 999]");
	static $m = array(null, '�', '�', '�', '�', '�', '��', '�', '�', '�');
	static $d = array(null, '�', '�', '�', '�', '�', '�', '�', '�', '\u991  ');
	static $e = array(null, '�', '�', '�', '�', '�', '�', '�', '�', '\u993  ');
	return $e[floor($n / 100)] . $d[floor($n / 10) % 10] . $m[$n % 10];
}


/** ���������� ���� ��� ������ ��� �������� ��� ���������� �� ���� ����� ��� ������.
 * ��� ����������� ����� ��������, ���� ��������� ��� �������� ���� ���� ����� ��� ��� �����������.
 * �� ������ ������ �� ����� �� ����� �������� � ����������� � �������� ��� ������. ������ ������
 * �� ����� ����������� 2 ��������.
 * @param string $a �� ������� �� ������ ���� ���������� ������
 * @param int $w ��������� �� (0) ����������, (1) ������, (2) ���������, (3) ������� ��� ������
 * @return string �� $a �� ��� ������ ��� ����� ����� ��� ������ */
function inflectPhrase($a, $w) {
	return !$w ? $a : preg_replace_callback('/[�-��-��-��-�����]{3,}|\d+(��|�|�)/',
		function($m) use($w) { return inflection($m[0], $w); }, $a);
}
/** ���������� ��� ���������� ���� ��������� � ������� �� ���� ����� ��� ������.
 * ��� ����������� ����� ��������, ���� ��������� ��� �������� ��� ��� �����������. � ���� ������
 * �� ����� �� ����� �������� � ����������� � �������� ��� �����. ������ ������ �� ����� �����������
 * 2 ��������.
 * @param string $o � ���������� ������ ���� ��������� � ������� ������������ � �������� (��� ����)
 * @param int $w ��������� �� (0) ����������, (1) ������, (2) ���������, (3) ������� ��� ������
 * @return string �� $o ��� ����� ����� ��� ������ */
function inflection($o, $w) {
	$b = substr($o, -2);
	if ($b == '��' && $w == 1) return substr($o, 0, -1) . '�';
	elseif ($b == '��' && $w == 1) return substr($o, 0, -2) . '��';
	elseif ($w >= 2 && $w <= 3 && ($b == '��' || $b == '��') ||
					$w >= 1 && $w <= 3 && strpos('�� �� �� �� �� �� �� �� ', "$b ") !== false)
		return substr($o, 0, -1);
  elseif ($w == 1 && strpos('������', substr($o, -1)) !== false)
		return $o . '�';
	return $o;
}


/** ���������� ��� string �� �� ������������� ���� ������������.
 * @param array $a T� �������� ��� ������������
 * @return string �� ������������� ��� ������������ ��� ����� '���� (��) ������ ������' */
function person($a) { return rtf($a['������'] . ' ' . $a['�������������']); }
/** ���������� ��� string �� �� ������������� ���� ������������.
 * @param array $a T� �������� ��� ������������
 * @param int $c �� ����� 0, � ������������ ����� ���� ���������� �����. �� ����� 1 ����� ��� ������.
 * �� ����� 2 ����� ���� ���������. �� ����� 3 ����� ���� �������.
 * @return string �� ������������� ��� ������������ ��� ������� �����, ������� �� ����� �.�.
 * '��� (��) ������ ������ ��� 3�� ���', �� �� ������ �� �������� ���� �� ������� */
function personi($a, $c) {
	return inflectPhrase(person($a), $c) . (isset($a['������']) ? " {$a['������']}" : null);
}


/** ���������� ��� ��������� �� ����, �� ����� �������, �������� ��������� ������.
 * @param int|float $a � �������
 * @param bool $zero �� ����� true, � ������� 0 ���������� '0,00 �', �������� ���������� null
 * @return string � ������� ��� ����� '1.234,10 �' */
function euro($a, $zero = false) {
	$a = num($a);
	if (!$a) return $zero ? '0,00 �' : null;
	else return sprintf('%01.2f �', $a);
}

/** ���������� ��� ��������� �� �������, �� ����� �������, �������� ��������� ������.
 * @param int|float $a � �������
 * @return float � ������� �������������� ��� �� '%', � �� ����� 0, �� 0 */
function percent($a) {
	$a = num($a);
	return !$a ? '0' : "$a%";
}

/** ���������� ��� ��������� �� ����� �������, �������� ��������� ������.
 * @param int|float $a � �������
 * @return float � ������� */
function num($a) {
	if (is_numeric($a)) return (float) $a;
	if ($a) trigger_error("� ���� '<b>$a</b>' �� ������ �� ����� �������");
}


/** ������� �� ���� ���������� ����������� ���� ����� ��������� ��� �������.
 * @param string $iban � ����������� ����
 * @return string � ����������� IBAN �� ����� ��������� ��� �������, ������ ��������� ������ */
function iban($iban) {
	$iban = str_replace(' ', '', $iban);
	if (is_greek_iban($iban) && is_valid_iban($iban)) return $iban;
	trigger_error($iban ? "�� IBAN '<b>$iban</b>' ��� ����� �������� ��� ������" : '��� ������ ������ �������� ����', E_USER_ERROR);
}

/** ������� �� ���� ���������� ����������� ���� �������� �� �� �������� �������.
 * ��� ����� ������ ����������� ���� IBAN. ���� ������� �� �� ����� ��� ��������� �� ���� ��� �����
 * ���������� ��� ���� ���������� IBAN.
 * @param string $iban � ����������� ���� ����� ����, ���� �� ���������� 0-9�-�
 * @return bool � ����������� IBAN ����� ��������� ���� ������� �� ������� */
function is_greek_iban($iban) { return preg_match('/GR\d{25}/', $iban); }

/** ������� �� ���� ���������� ����������� ���� �������� �� �� ������ �������.
 * ��� ����� ������ ����������� ���� IBAN. ���� ������� �� �� ����� ��� ��������� �� ���� ��� �����
 * ���������� ��� ���� IBAN.
 * @param string $iban � ����������� ���� ����� ����, ���� �� ���������� 0-9�-�
 * @return bool ����� ����������� IBAN, ���� ������� �� ������� */
function is_iban($iban) { return preg_match('/[A-Z]{2}\d{2}[A-Z0-9]{1,30}/', $iban); }

/** ������� �� ���� ���������� ����������� ���� ����� �������.
 * ���� ��� ��������, �� ������ ���� ��� ���� is_iban(), is_greek_iban() �� ���� ���������� true.
 * <p>���� ����������� IBAN �����������:
 * <ul><li>��� 2 �������� �������� �������� (�-�) ��� �����. ���� ������ GR.
 * <li>��� 2 �������� (0-9) ������� �����������.
 * <li>����� 30 ����������, �������� � �������� �������� �������� (0-9 �-�) ��� ������������
 * ����������� �� ���� ���� ��� ��������� ��� Basic Bank Account Number (BBAN). ���� ������
 *  ������������ ����� 23 ������� (0-9):
 * <ul><li>��� 3 �������� (0-9) ��� �������� ��� �������.
 * <li>��� 4 �������� (0-9) ��� �������� �� ������������ ��� ��������.
 * <li>��� 16 �������� (0-9) ��� �������� �� ���������� ���� �������.</ul></ul>
 * <p>� ������� ������� ����������� ��������� �� ����:
 * <ul><li>������������ ���� ������� ��� ����������� ��� ��� BBAN, ��� ������ ��� ����� ��� �� '00'
 * ��� ����������� ���� �������� ������� ����������� ��� ��� ������ ���������� �����. ������ ����
 * ���� ������� 4 ���������� ��� ���� ��� ����� ��� ��� �������� ������� ����������� ������� '00'.
 * <li>������������ ���� ������ ��� �������� �� ������� ������, ���������� ��� ��� ASCII ���������
 * �� 55. ���� ���� ���������� �������� ��� �� 10.
 * <li>������������ �� �������� ��� ��������� ��� �������� ��������, ��� ����� ����� �������, ��� ��
 * 97.
 * <li>��������� �� �������� ��� ��������� ��� ������� �������� ��� �� 98.
 * <li>�� ���������� ����� � ������� ������� �����������.</ul>
 * <p>� ������� ����������� ���������������� �� ����:
 * <ul><li>������������ ���� ������� ��� ����������� ��� ��� BBAN, ��� ������ ��� ����� ��� ����
 * �������� ������� �����������. ������ ���� ���� ������� 4 ���������� ��� ���� ��� �����.
 * <li>������������ ���� ������ ��� �������� �� ������� ������, ���������� ��� ��� ASCII ���������
 * �� 55. ���� ���� ���������� �������� ��� �� 10.
 * <li>������������ �� �������� ��� ��������� ��� �������� ��������, ��� ����� ����� �������, ��� ��
 * 97.
 * <li>�� �� ���������� ����� 1, � ���� ����� �������.</ul>
 * @param string $iban � ����������� ���� ����� ����, ���� �� ���������� 0-9�-�
 * @return bool � ����������� IBAN ����� �������
 * @see is_iban(), is_greek_iban() */
function is_valid_iban($iban) {
	$trg = '';
	foreach(str_split(substr($iban, 4) . substr($iban, 0, 4)) as $c)
		if (ord($c) > 64) $trg .= ord($c) - 55; else $trg .= $c;
	return bcmod($trg, 97) == 1;
}

/** ���������� ��� ������� ���� ����� ������ ���� ����������� IBAN.
 * � ������� ������ �� ����������������� ���� ������.
 * @param string $iban � ����������� ����, � ������ ������ �� ����� ���������
 * @param bool $trigger ��������� ������ �� � ���� ��� ����������� �� ����� �������
 * @return string|null � �������� ��� �������� ���� ����� ������ � ����������� */
function bank($iban, $trigger = true) {
	switch((int) substr($iban, 4, 3)) {
		case  10: return '������� ��� ������� �.�.';
		case  11: return '������ ������� ��� ������� �.�.';
		case  14: return 'ALPHA BANK';
		case  16: return 'ATTICA BANK ������� ��������� ��������';
		case  17: return '������� �������� �.�.';
		case  26: return '������� EUROBANK ERGASIAS A.E.';
		case  34: return '���������� ������� ������� �.�.';
		case  39: return '��� �� �� ������� ����������� ��������';
		case  40: return 'FCA BANK GmbH';
		case  50: return '������� �������� ����';
		case  56: return 'AEGEAN BALTIC BANK �.�.�.';
		case  57: return 'CREDICOM CONSUMER FINANCE ������� �.�.';
		case  58: return 'UNION DE CREDITOS INMOBILIARIOS S.A. ESTABLECIMIENTO FINANCIER';
		case  59: return 'OPEL BANK GmbH';
		case  61: return 'FCE BANK PLC';
		case  64: return 'THE ROYAL BANK OF SCOTLAND PLC';
		case  69: return '�������������� ������� ������ ���. �.�.';
		case  71: return 'HSBC BANK PLC';
		case  72: return 'UNICREDIT BANK AG';
		case  73: return '������� ������ ������� �������� ���';
		case  75: return '�������������� ������� ������� ���. �.�.';
		case  81: return '����� �� ������� ��';
		case  84: return 'CITIBANK EUROPE PLC';
		case  87: return '��������� �������������� ������� ���. �.�.';
		case  88: return '�������������� ������� �. ����� ���. �.�.';
		case  89: return '�������������� ������� ��������� ���. �.�.';
		case  91: return '�������������� ������� ��������� ���. �.�.';
		case  92: return '�������������� ������� ������������ ���. �.�.';
		case  94: return '�������������� ������� ������� "��������� �����" ���. �.�.';
		case  95: return '�������������� ������� ������ ���. �.�.';
		case  97: return '������ ������������� & �������';
		case  99: return '�������������� ������� �. ������ ���. �.�.';
		case 102: return 'VOLKSWAGEN BANK GMBH';
		case 105: return 'BMW AUSTRIA BANK GmbH';
		case 106: return 'MERCEDES-BENZ BANK POLSKA S.A.';
		case 107: return 'GREEK BRANCH OF KEDR OPEN JOINTSTOCK COMPANY COMMERCIAL B';
		case 109: return 'T.C ZIRAAT BANKASI A.S';
		case 111: return 'DEUTSCHE BANK AG';
		case 113: return 'CREDIT SUISSE (LUXEMBOURG) S.A.';
		case 114: return 'FIMBANK PLC.';
		case 115: return 'HSH NORDBANK AG';
		case 116: return 'PROCREDIT BANK (BULGARIA) EAD';
		default: if ($trigger) trigger_error("� IBAN '<b>$iban</b>' ����������� �� �� ������������ �������.", E_USER_ERROR);
	}
}

/** ���������� ��� �� ���������� Java ����������� �� ��������� ����������� ��� ��� ���������� IBAN.
 * ������ ����������� �� ������ ����������� ��� ���������� Java ��� ������������ �� ��� ��������.
 * @param string $iban � ����������� ���� ����� ����, ���� �� ���������� 0-9�-� */
function iban_gui($iban) {
	if (!is_iban($iban) || !is_valid_iban($iban))
		echo $iban ? "O $iban ��� ����� ������� ����" : '�� ������� IBAN';
	else {
		echo "O $iban ����� ������� ����\n";
		if (!is_greek_iban($iban)) echo '� ���� ��� ����� ��������� ��� ��� �������������.';
		else {
			echo "O IBAN ����� ��������� (GR)\n";
			$bank = bank($iban, false);
			if (!isset($bank)) echo '� IBAN ����������� �� ������� ��� ��� �������.';
			else {
				echo "� ������� ����� $bank (�������: " . substr($iban, 4, 3) . ")\n";
				echo '�� ������������ ��� �������� ���� ������ ' . substr($iban, 7, 4) . "\n";
				echo '� ������� ����������� ����� ' . substr($iban, 11);
			}
		}
	}
}


/** ������� �� � ���������� ������ ����� ��� ��� ����������, ������ ��������� ������.
 * @param string $a ���������� ��� ����� '31 ��� 19'
 * @return string �� $a �� � ���������� ����� ��� ����� ����� */
function chk_date($a) { parse_date($a); return $a; }

/** ������� ��� ���������� �� ������� ��� ���������� array �� ��� ����������.
 * @param string $a ���������� ��� ������ '31 ��� 19'
 * @return array ���������� ��� ������ ('31', '12', '2019') */
function parse_date($a) {
	$m = explode(' ', $a, 3);
	$m[1] = get_month($m[1]);
	$curyear = date('Y');
	// �� �� ���� ������� �������, ���� �� �� ���� + 2000 > ������_���� + 3, ����������� 1900 ������ 2000
	if ($m[2] >= 0 && $m[2] < 100 || $m[2] >= 1900 && $m[2] < $curyear + 3) {
		if ($m[2] < 100)
			$m[2] += 1997 + $m[2] > $curyear ? 1900 : 2000;
		if (checkdate($m[1], $m[0], $m[2])) return $m;
	}
	trigger_error(($a ? "�� '<b>$a</b>' ��� ����� ����������" : '�� ����������� ������ �� ��������') . " ��� ����� �.�. '20 ��� 19'");
}

/** ���������� ��� ������ ��� ���� ��� �� ������� ����� ��� ����.
 * @param string $month �������� ��� ���� �.�. '���'
 * @return int|null � ������� ��� ����, �.�. 11 � null �� �� ������ ������� ����� */
function get_month($month) {
	$months = array(
		'���' => 1, '���' => 2, '���' => 3, '���' => 4, '���' => 5, '����' => 6,
		'����' => 7, '���' => 8, '���' => 9, '���' => 10, '���' => 11, '���' => 12);
	if (isset($months[$month])) return $months[$month];
}

/** ���������� �� ����� �� ����� '05 ���� 19'.
 * @param int $a ��� timestamp. �� ������������ ����� �� ������ timestamp.
 * @return string � ���������� ��� ����������� ��� timestamp, �� ����� '05 ���� 19'. */
function now($a = null) { return strftime('%d %b %y', $a ? $a : time()); }


/** ���������� �� �������� ���� array �� �������.
 * @param array $ar �� array
 * @param string|int $key �� ������ ��� ��������� ��� array
 * @return mixed|null � ���� ��� ��������� �� �� ������� ������ � null */
function ifexist($ar, $key) { if (isset($ar[$key])) return $ar[$key]; }
/** ���������� �� �������� ���� array �� ������� � �� ������ ��� ������������ �����.
 * @param bool $exp ��� ������������ ������ �����
 * @param array $ar �� array
 * @param string|int $key �� ������ ��� ��������� ��� array
 * @return mixed|null � ���� ��� ��������� �� �� ������� ������ � null */
function ifexist2($exp, $ar, $key) { if ($exp || isset($ar[$key])) return $ar[$key]; }
/** ���������� �� �������� ���� array �� �������, ������ ���������� ���� ����.
 * @param array $ar �� array
 * @param string|int $key �� ������ ��� ��������� ��� array
 * @param mixed $else ��� ����������� ���� �� ��� ����� �� ���������� � ��������
 * @return mixed|null � ���� ��� ��������� �� �� ������� ������ � null */
function orelse($ar, $key, $else) { return isset($ar[$key]) ? $ar[$key] : $else; }
/** ���������� �� �������� ���� array �� ������� � �� ������ ��� ������������ �����, ������ ���������� ���� ����.
 * @param bool $exp ��� ������������ ������ �����
 * @param array $ar �� array
 * @param string|int $key �� ������ ��� ��������� ��� array
 * @param mixed $else ��� ����������� ���� �� ��� ����� �� ���������� � ��������
 * @return mixed � ���� ��� ��������� �� �� ������� ������ � $else */
function orelse2($exp, $ar, $key, $else) { return $exp || isset($ar[$key]) ? $ar[$key] : $else; }


/** ���������� �� ���������� ��� ����� ������ ����������.
 * @param array $invoices � ����� ����������. ������ �� �������� ����������� ��� ���������.
 * @param array $keys �� ������� ��� ����� ��� ��� ������ �� ������������ �� ���������� ����, �.�.
 * '������ ����'. �� ����� null, ������������� �� ���������� ���� ��� �����.
 * @return array �� ���������� ��� �����, ���� ��� ���������� ��� �������� ������ */
function calc_sum_of_invoices_prices($invoices, $keys = null) {
	if (!$keys) $keys = array_keys($invoices[0]['�����']);	// ���� ���� ��� �������� ��� ����� ��� �� ����� ���������
	$b = array_fill_keys($keys, 0);		// ������������ ��� ����������� �� ���� 0
	foreach($invoices as $invoice) {
		$price = $invoice['�����'];
		foreach($keys as $key)
			$b[$key] += $price[$key];
	}
	return $b;
}

/** ���������� ����������, ��� ��������� ���������, ���� ������ ����������.
 * @param array $invoices ��� ����� ����������
 * @return array ���� ������� �� ������� ��� ��������� ��������� ��� �� '������' ��� ����� ���
 * ����������� ����� �� ����, �������� �� ������ ������� ��� �� ��� �������� �������� ��������� ���
 * �������� */
function calc_partial_deductions($invoices) {
	$a = array();
	$priceSum = 0;
	// ���������� ��������� ���������
	foreach ($invoices as $invoice) {
		$deduction = $invoice['���������'];
		$sum = $deduction['������'];
		$price = $invoice['�����']['���������'];	// ��� ��� ��� ������� ����� ����� �� ��������� ����� ����������������
		$priceSum += $price;
		foreach($deduction as $name => $term) {
			if ($name != '������') {
				$val = $price * $term / $sum;
				if (!isset($a[$name])) $a[$name] = $val; else $a[$name] += $val;
			}
		}
	}
	// �������� ����������������
	$a = adjust_partials($a, $priceSum);
	$a['������'] = $priceSum;
	return $a;
}

/** �������� ��� ����� ����� ���� �� ����� ��� ������������ ��������.
 * �� ��� ������� ������������ �� ��� ���� ��� ����������������� �� 2 �������� �����, ��� ����������
 * ���� �� ������� ��������� �� ��������� �������, ���� �� �� ����������� ����� ��� ��������� ��������
 * ����������������� ��� 2 �������� ����� ��� ����������, � �������� ���� ����, ��������� ����
 * ��������� ���������������� �� ��� ����� ���� �� ��� ���� ��� ������������� ��� ������ �������.
 * ��� �� ���� ����, �� ��������� ����� �������������� ������� ���� 0.01 ����������� �� �������� ����
 * �� ����� ���� �� ��� ���� ��� ������� ��������.
 * @param array $in �� ����� ��� ��������� �������� ����� ���������������
 * @param float $desirableSum �� ��������� �������� ��� �������� �����, ���� �� ���������������
 * @return array �� ����� ��� ��������� �������� ���� �� ��������������� */
function adjust_partials($in, $desirableSum) {
	$sum = 0;
	$remainders = array(); $out = array();
	foreach($in as $key => $term) {
		$term_new = round($term, 2);
		$remainders[] = array($key, $term_new - $term);
		$out[$key] = $term_new;
		$sum += $term;
	}
	$remainder = round(($sum - $desirableSum) * 100);
	if ($remainder) {
		$comparator = function($v1, $v2) {
			if ($v1[1] < $v2[1]) return -1;
			if ($v1[1] > $v2[1]) return 1;
			return 0;
		};
		usort($remainders, $comparator);
		if ($remainder > 0)
			for ($z = 0; $z < $remainder; ++$z)
				$out[$remainders[count($remainders) - 1 - $z][0]] -= 0.01;
		else
			for ($z = 0; $z < -$remainder; ++$z)
				$out[$remainders[$z][0]] += 0.01;
	}
	return $out;
}

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


/** ���������� ��� array �� ��� ������ ���������� ��� ���� ����������.
 * @param array $invoices ����� �� ���������
 * @return array ��� �������� ��� ���� ���������, ��� �������� ��� array �� ��� ��� �� ��������� */
function get_invoices_by_contractor($invoices) {
	$b = array();
	foreach($invoices as $invoice)
		$b[$invoice['����������']['��������']][] = $invoice;
	return array_values($b);
}

/** ���������� ��� array �� ������ ���������� ������� ��� ���������� ��� ��� ���������.
 * @param array $invoices ����� �� ���������
 * @return array �� �������� �� ������ '����������' ����� array �� ��� �� ��������� ��� �������
 *  ����������. �� �������� �� ������ '���������' ����� array �� ��� �� ��������� ��� �������
 *  ���������. �� ��� �������� ���������� ���������, ��� ������� ��� �� ���������� ��������. */
function get_invoices_by_category($invoices) {
	$b = array();
	foreach($invoices as $invoice)
		$b[get_invoice_category($invoice['���������'])][] = $invoice;
	return $b;
}

//TODO: ��������������� ��� ����. ����������;
/** ���������� �� ������ ����� ���� ���������� ����������.
 * @param string $category � ��������� ��� ���������� �� �� ������ �����
 * @return string � ��������� ��� ���������� �� ������ ��� ������ ����� */
function get_invoice_category($category) {
	return is_supply($category) ? '��������� ������' : '������ ���������';
}

/** ���������� true �� � ��������� ��� ���������� ����� ����������.
 * @param string $category � ��������� ��� ����������
 * @return boolean � ��������� ��� ���������� ����� ��������� */
function is_supply($category) { return $category == 'SUPPLIES' || $category == 'LIQUID_FUEL'; }

/** ���������� �� ��� �������� timestamp ����������.
 * @param array $invoices ����� ����������
 * @return int|null �� timestamp ��� ��� ��������� ���������� � null �� ��� �������� ��������� */
function get_newer_invoice_timestamp($invoices) {
	$a = null;
	foreach($invoices as $invoice)
		if (isset($invoice['���������'])) {
			$b = null;
			invoice($invoice['���������'], $b);
			$b = mktime(0, 0, 0, $b[1], $b[0], $b[2]);
			if ($a < $b) $a = $b;
		}
	return $a;
}

/** ���������� ��� ��������� ��� ����������.
 * @param array $invoice ���������
 * @return array ������� ��� �������� ���������� � ���� array */
function get_contract($invoice) {
	global $data;
	return isset($invoice['�������']) ? $data['���������'][$invoice['�������']] : array();
}


/** ���������� �� �������, ��� �� ������� ���� array �� ��������.
 * @param array $a � ����� �� �� ��������. ��� ������ �� ����� �����.
 * @param string $key �� ������, ��� ���� ��������, �� �� ����� ��� ���������
 * @return string �� ������� ��� ��������� ��� array ��������� �� ',' ����� ��� �� ��������� 2 ���
 * ����� ��������� �� '���' */
function get_names($a, $key) {
	$n = count($a);
	$r = '';
	for ($z = 0; $z < $n - 2; ++$z)
		$r .= $a[$z][$key] . ', ';
	if ($n > 1) $r .= $a[$n - 2][$key] . ' ��� ';
	$r .= $a[$n - 1][$key];
	return $r;
}


/** ���������� ���� ����� ��� ���� ��������� ��� ������ '�����������'.
 * @param array $invoices ��� ����� ���������� ��� ��� ����� �� ����������� �� ����� �����������,
 * ��������� � ��������
 * @param int $inflection 0 ��� ����������, 1 ��� ������, 2 ��� ��������� ��� 3 ��� �������
 * @param int $article 0 ����� �����, 1 ��� ����� ��� 2 ��� '����', '����', '����', '����'
 * @return string � ������ ��� ����������, �� �� ����� ��� �������� */
function get_contractor_title($invoices, $inflection, $article) {
	$c = 0;
	foreach($invoices as $invoice)
		if (!is_supply($invoice['���������'])) {
			$c = $invoice['����������']['�����'] == 'PRIVATE_SECTOR' ? 1 : 2;
			break;
		}
	switch($article) {
		case 1:
			switch($inflection) {
				case 0: $article = array('� ', '� '); break;
				case 1: $article = array('��� ', '��� '); break;
				case 2: $article = array('��� ', '��� '); break;
				default: goto def;
			}
			break;
		case 2:
			switch($inflection) {
				case 1: $article = array('���� ', '���� '); break;
				case 2: $article = array('���� ', '���� '); break;
				default: goto def;
			}
			break;
		default:
def:		$article = array(null, null);
	}
	switch($c) {
		case 0: return $article[0] . inflection('�����������', $inflection);
		case 1: return $article[0] . inflection('��������', $inflection);
		case 2: return $article[1] . inflection('��������', $inflection);
	}
}


//TODO: ��� ����������������


/* * ���������� ������������� �� ��������� �� ������������ ����.
 * @param array $invoices ����� ����������
 * @param string $type � ����� ��� ���������� (�.�. '��������� ������')
 * @param bool $invert ���������� �� ��������� ��� ��� ����� �� ������������ ���� (����������)
 * @return array ����� ���������� �� �� ������� ���� * /
function get_invoices_with_category($invoices, $type, $invert = false) {
	$b = array();
	foreach($invoices as $invoice)
		if (($invoice['���������'] == $type) != $invert) $b[] = $invoice;
	return $b;
}

/** ���������� ������������� �� ��������� �� ������������ ���� ����������.
 * @param array $invoices ����� ����������
 * @param string $type � ����� ��� ����������, ���� ������� ��� ��������� (�.�. 'PRIVATE_SECTOR')
 * @return array ����� ���������� �� �� ������� ���� ���������� * /
function get_invoices_with_contractor_type($invoices, $type) {
	$b = array();
	foreach($invoices as $invoice)
		if ($invoice['�����������']['�����'] == $type) $b[] = $invoice;
	return $b;
}


/** ���������� ��� ��������� ��� ����������.
 * @param array $invoices ����� ����������
 * @return array ����� ��������� ��� �������� ���������� * /
function get_contracts($invoices) {
	global $data;
	$b = array();
	foreach($invoices as $invoice)
		if (isset($invoice['�������'])) $b[] = $data['���������'][$invoice['�������']];
	return array_unique($b);
}

// ���������� ��� array �� ��������� ��� ����� ��.
function bills_with_fe($a) {
	$b = array();
	foreach($a as $v)
		if ($v['���������']) $b[] = $v;
	return $b;
}

// ���������� ��� array �� keys ��� ��������� �� ������� ���
// values array �� ��� �� ��������� ��� ����� ������ �������.
// �� $zero == false ��� ���������� �� ��������� ��� �����
// ��������� 0 (������ �� ����� 0 ���� ��� �������)
function bills_by_hold($a, $zero = false) {
	$b = array();
	foreach($a as $v) {
		$n = $v['�������������������������']['������'];
		if ($n || $zero) $b[str_replace(',', '.', $n)][] = $v;
	}
	return $b;
}

// ���������� ��� array �� keys �� �� �� ������� ���
// values array �� ��� �� ��������� ��� ����� ������ ��.
// ��� ���������� ��������� ����� ��.
function bills_by_fe($a) {
	$b = array();
	foreach($a as $v) {
		$n = $v['���������'];
		if ($n) $b[str_replace(',', '.', $n)][] = $v;
	}
	return $b;
}

// ���������� ��� array �� keys ��� "���� ����" �.�. "��� 2005" ���
// values array �� ��� �� ��������� ��� �����.
function bills_by_month($a) {
	$b = array();
	foreach($a as $v)
		if (preg_match('/\d{1,2}-(\d{1,2})-(\d{4})/', $v['���������'], $m)) {
			$b[mktime(0, 0, 0, $m[1], 1, $m[2])][] = $v;
		} else chk_bill($v['���������']);
	return $b;
}

// ������� ��� string ��� ������ "26 21:10 ��� 2006"
function chk_datetime($a) {
	if (preg_match('/\d{1,2} \d{1,2}\:\d{2} (���|���|���|���|���|����|����|���|���|���|���|���) \d{4}/', $a))
		return rtf($a);
	trigger_error(($a ? "�� '<b>$a</b>' ��� ����� ������� ������" : '�� �������� ������� ������ �� ��������') . " ��� ����� �.�. '20 21:34 ��� 2005'");
}

// ������� ��� string ��� ������ "21:10 26 ��� 2006" ��� ���������� ��� ��� ��� ��� ����������
function get_datetime($a) {
	if (!preg_match('/(\d{1,2}) (\d{1,2}\:\d{2}) (���|���|���|���|���|����|����|���|���|���|���|���) (\d{4})/', $a, $m))
		trigger_error(($a ? "�� '<b>$a</b>' ��� ����� ������� ������" : '�� �������� ������� ������ �� ��������') . " ��� ����� �.�. '20 21:34 ��� 2005'");
	else {
		$b[] = $m[2]; $b[] = "{$m[1]} {$m[3]} {$m[4]}";
		return $b;
	}
}

// ���������� array �� ��� ����������
function getNewspapers($a) {
	$a = preg_split("/([ ]*,[ ]*)/", $a);
	foreach($a as & $v)
		$v = '��������� �' . strtouppergn($v) . '�';
	return $a;
}

function getDates($a) {
	$a = preg_split("/([ ]*,[ ]*)/", $a);
	if (count($a) == 1) return chk_date($a[0]);
	$b = null;
	for ($z = 0; $z < count($a) - 1; $z++)
		$b .= ', ' . chk_date($a[$z]);
	$b .= ' ��� ' . chk_date($a[count($a) - 1]);
	return substr($b, 2);
}
*/