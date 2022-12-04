<?php
require_once('functions.php');

// � ��������� ������������� ��������� �� �� �������
$name = $_ENV['export'];
switch($name) {
	case 'statement_IBAN':
	case 'statement_representative':
	case 'statement_disqualification':
		require_once('statement.php');
		init(3);
		if (isset($data['����'])) {	// ������� ������� ������
			init(6);
			statement_common($name, $data['������ ������']);
		} else {
			$data['������ ������'] = $_ENV['unit'];
			$name($data);
		}
		break;
	case 'statement_gui':
		require_once('statement.php');
		init(3);
		$name();
		break;
	default:
		require_once('contract.php');
		require_once('tender.php');
		init(8);
		$name();
		break;
}

rtf_close(__FILE__);