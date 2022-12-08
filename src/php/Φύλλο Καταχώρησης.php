<?php
require_once('functions.php');
init(8);

if (!function_exists('contents_subfolder')) {

/** ������ ��� ������� ���� ����������.
 * @param array $content_item � ���������� ������������ */
function contents_subfolder($content_item) {
	global $data, $countA;
	$name = $content_item['��������������'];
	$a = substr($name, 13 - 1, 1);
	// ��� ������� ��� ����� ����� ���� ������� ������� �������� ��� ������ �� �����������
	// ���������� �».
	// ��� �� ��� ���� ����� ���������� ����������� ���� ��������� ��������� ���� ���� ��������� �»
	// ��� ��� ��� ����, �� ��� ������� ���������� �����������, ����������� ���� ���������� �»
	// ����� ������� �����������.
	// � ������������ �����, �� ������ ������ ������� ��������, �� ������� ��� ���� ��������� ���
	// ��� �� ��� ������� ���������� �».
	if (!isset($data['���������']) && $a == '�') return;
	$countA = $a;
	$name = substr($name, 13 + 3);
	echo '\cell\cell\b\line ' . $countA . '\cell\line ' . $name . '\b0\cell\cell\row' . PHP_EOL;
}

/** ������ ��� �������� ��� ����������. */
function contents_invoices() {
	global $data, $count, $countA;
	foreach($data['���������'] as $invoice)
		echo ++$count . '\cell ' . rtf($invoice['����������']['��������']) . '\cell ' . $countA
				. '\cell ��������� ' . $invoice['���������'] . '\cell\cell\row' . PHP_EOL;
}

/** ������ ��� �������� ��� ����������� ��������� ��������� ��� ��������� ���������. */
function contents_acceptance_protocol() {
	global $data;
	if (isset($data['���������']))
		foreach($data['���������'] as $per_contract) {
			$flags = 0;	// FLAGS: 1: ��������� ������, 2: ������ ���������
			foreach($per_contract['���������'] as $invoice) {
				$flags |= is_supply($invoice['���������']) ? 1 : 2;
				if ($flags == 3) break;
			}
			if ($flags) {
				$f = function($a) use($per_contract) {
					global $data, $count, $countA;
					echo ++$count . '\cell ' . rtf($data['������']) . '\cell ' . $countA
							. '\cell ���������� ��������� ��������� ��� ��������� ��������� '
							. $a . ' �' . rtf($per_contract['����������']['��������'])
							. '�\cell\cell\row' . PHP_EOL;
				};
				if ($flags & 1) $f('����������');
				if ($flags & 2) $f('���������');
			}
		}
}

/** ������ ��� �������� ��� ���������� ���������. */
function contents_acceptance_affirmation() {
	global $data;
	$flags = 0;	// FLAGS: 1: ��������� ������, 2: ������ ���������
	foreach($data['����������'] as $per_contractor)
		if (!isset($per_contractor['�������']))
			foreach($per_contractor['���������'] as $invoice) {
				$flags |= is_supply($invoice['���������']) ? 1 : 2;
				if ($flags == 3) break 2;
			}
	if ($flags) {
		$f = function($a) {
			global $data, $count, $countA;
			echo ++$count . '\cell ' . rtf($data['������']) . '\cell ' . $countA . '\cell �������� ��������� '
					. $a . '\cell\cell\row' . PHP_EOL;
		};
		if ($flags & 1) $f('����������');
		if ($flags & 2) $f('���������');
	}
}

/** ������ ��� �������� �������� ���������� ��� �������� ��� ����������. */
function contents_contractor_certificate($certificate) {
	global $data, $count, $countA;
	foreach($data['����������'] as $per_contractor) {
		$contractor = $per_contractor['����������'];
		if ($contractor['�����'] == '��������� ������' && isset($per_contractor['�������']))
			echo ++$count . '\cell ' . rtf($contractor['��������']) . '\cell ' . $countA
				. '\cell ' . $certificate . '\cell\cell\row' . PHP_EOL;
	}
}

/** ������ ��� �������� ��� ��������� ��������.
 * @param array $content_item � ���������� ������������ */
function contents_statement($content_item) {
	global $data, $count, $countA;
	foreach($data['����������'] as $per_contractor) {
		$contractor = $per_contractor['����������'];
		if ($contractor['�����'] == '��������� ������')
			echo ++$count . '\cell ' . rtf($contractor['��������']) . '\cell ' . $countA
				. '\cell ' . rtf($content_item['��������������']) . '\cell\cell\row' . PHP_EOL;
	}
}

/** ������ ��� �������� ��� ����������� ��� ������������ ������������. */
function contents_tax_insurrance_currency() {
	global $data, $count, $countA;
	foreach($data['����������'] as $per_contractor) {
		$contractor = $per_contractor['����������'];
		if ($contractor['�����'] == '��������� ������') {
			$mixed = $per_contractor['�����']['������������'];
			if ($mixed > 1500)
				echo ++$count . '\cell ' . rtf($contractor['��������']) . '\cell ' . $countA
					. '\cell ���������� �����������\cell\cell\row' . PHP_EOL;
			if ($mixed > 3000 || $per_contractor['�����']['������ ����'] > 2500)
				echo ++$count . '\cell ' . rtf($contractor['��������']) . '\cell ' . $countA
					. '\cell ����������� �����������\cell\cell\row' . PHP_EOL;
		}
	}
}

/** ������ �������� ���������.
 * �� �������� ��������� ����� ��������� �������� ��������� ��� ������� ��������� ��������.
 * @param array $content_item �� �������� ���� �������� ������������ */
function contents_order_contract($content_item) {
	global $data;
	if (isset($data['���������']))
		foreach($data['���������'] as $per_contract) {
			$contract = $per_contract['�������'];
			if (!isset($contract['�����������']))
				contents_order($content_item, $contract[$content_item['��������������']]);
		}
}

/** ������ ��� �������� ��� ���������. */
function contents_contract() {
	global $data, $count, $countA;
	if (isset($data['���������']))
		foreach($data['���������'] as $per_contract)
			echo ++$count . '\cell ' . rtf($data['������']) . '\cell ' . $countA . '\cell ������� '
					. $per_contract['�������']['�������'] . ' �� �'
					. rtf($per_contract['����������']['��������'])
					. '�\cell\cell\row' . PHP_EOL;
}

/** ��������� ��� ������ �� �������������� ��� �������� �� ��������������. */
function contents_interested_documents() {
	global $data, $count, $countA;
	foreach($data['�����������'] as $per_tender)
		foreach($per_tender['�����������']['��������������'] as $per_competitor) {
			$a = '\cell ' . rtf($per_competitor['��������������']['��������']) . '\cell ' . $countA . '\cell ';
			foreach($per_competitor['�������������� ����������'] as $document)
				echo ++$count . $a . $document . '\cell\cell\row' . PHP_EOL;
		}
}

/** ��������� ��� ������ �� �������������� ��� �������� �� ���������� ��������. */
function contents_legal_documents() {
	global $data, $count, $countA;
	foreach($data['�����������'] as $per_tender) {
		foreach($per_tender['���������'] as $contract) {
			$contractor = $contract['��������'];
			$a = '\cell ' . rtf($contractor['��������']) . '\cell ' . $countA . '\cell ';
			foreach($per_tender['�����������']['�������������� �����������'] as $document)
				echo ++$count . $a . $document . '\cell\cell\row' . PHP_EOL;
		}
	}
}

/** ������ �������� ����������� ���� �� �� ����������� ������������ ����.
 * @param array $content_item �� �������� ���� �������� ������������ */
function contents_order_tender($content_item, $proof = true) {
	global $data, $count, $countA;
	foreach($data['�����������'] as $per_tender) {
		$tender = $per_tender['�����������'];
		contents_order($content_item, $tender[$content_item['��������������']]);
		if ($proof)
			echo ++$count . '\cell ' . rtf($data['������']) . '\cell ' . $countA
					. '\cell ����������� ������������ ���\par'
					. $tender[$content_item['��������������']]['���������'] . '\cell\cell\row' . PHP_EOL;
	}
}

/** ������ �� �������� ������� ��������������� �����������.
 * @param array $content_item �� �������� ���� �������� ������������ */
function contents_award_check() {
	global $data, $count, $countA;
	$a = '\cell ' . rtf($data['������']) . '\cell ' . $countA . '\cell �������� ������� ��������������� ����������� (';
	foreach($data['�����������'] as $per_tender)
		echo ++$count . $a . strftime('%d %b %y', $per_tender['�����������']['������ ��������� ��������������� �����������']) . ')\cell\cell\row' . PHP_EOL;
}

/** ������ �� �������� ������������ ���������. */
function contents_unseal() {
	global $data, $count, $countA;
	$a = '\cell ' . rtf($data['������']) . '\cell ' . $countA . '\cell ';
	foreach($data['�����������'] as $per_tender) {
		$tender = $per_tender['�����������'];
		$b = strftime('%d %b %y', $tender['������ ������������ ���������']) . ')\cell\cell\row' . PHP_EOL;
		if (has_2_unseals($tender)) {
			echo ++$count . $a . '�������� ������������ ��������������� ���������� ��� �������� ��������� (' . $b;
			echo ++$count . $a . '�������� ������������ ����������� ��������� ('
					. strftime('%d %b %y', $tender['������ ������������ ����������� ���������'])
					. ')\cell\cell\row' . PHP_EOL;
		} else echo ++$count . $a . '�������� ������������ ��������� (' . $b;
	}
}

/** ������ ��� �������� ��������.
 * @param array $content_item � ���������� ������������
 * @param string $order � ��������� ��� �������� */
function contents_order($content_item, $order) {
	contents_default_low($content_item, $order['�������'], '\par ' . $order['���������']);
}

/** ������ ��� ������ �������.
 * @param array $content_item � ���������� ������������ */
function contents_default($content_item) {
	global $data;
	$issuer = orelse($content_item, '�������', $data['������']);
	contents_default_low($content_item, $issuer, null);
}

/** ������ ��� ������ �������.
 * @param array $content_item � ���������� ������������
 * @param string $issuer � ������� ��� ��������
 * @param string $line2 ��� ������� ������ ���� ��� ��� ������ ��������� ��� ����������� */
function contents_default_low($content_item, $issuer, $line2) {
	global $count, $countA;
	$c = $content_item['������'] > 1 ? " x{$content_item['������']}" : null;
	echo ++$count . '\cell ' . rtf($issuer) . '\cell ' . $countA . '\cell '
			. rtf($content_item['��������������']) . $line2 . '\cell' . $c . '\cell\row' . PHP_EOL;
}

/** ������ ��� �������� �� �������, � ��� ����.
 * @param array $content_item � ���������� ������������ */
function contents_debit_affirmation($content_item) {
	global $data;
	foreach($data['���������'] as $invoice)
		if (is_supply($invoice['���������'])) {
			contents_default($content_item);
			break;
		}
}

}
?>

\sectd\sbkodd\pgwsxn11906\pghsxn16838\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

\pard\plain\sa113\qc\fs24\b ����� ����������� ��������\par
\qj ������ �������: \b0{\ul <?=rtf($data['������'])?>}\par

\pard\plain\fs22
\trowd\trhdr
<?php ob_start();	// Buffer �� ������������ ������ ?>
\trautofit1\trpaddfl3\trpaddl28\trpaddfr3\trpaddr28
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clvertalc\cellx453
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clvertalc\cellx3288
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clvertalc\cellx3741
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clvertalc\cellx9637
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clvertalc\cellx10205
<? $c2 = ob_get_flush(); ?>
\qc\b �/�\cell �������\cell �/�\cell �������� ��������\cell ���.\b0\cell\row
<?php
echo '\trowd' . $c2 . PHP_EOL . '\qj ';

foreach($data['����� �����������'] as $content_item) {
	if (!$content_item['����������']) continue;
	$name = $content_item['��������������'];
	if (substr($content_item['��������������'], 0, 12) == '���������� �') contents_subfolder($content_item);
	else
		switch($name) {
			case '���������': contents_invoices(); break;
			case '���������� ��������� ��������� ��� ��������� ���������': contents_acceptance_protocol(); break;
			case '�������� ���������': contents_acceptance_affirmation(); break;
			case '�������� �� ������� ������': contents_debit_affirmation($content_item); break;
			case '�������� ������ ������� �� ���� ����������� ������ ��� ����������� ��������� ��� ������������� ��������':
			case '�������� ������ ���� �� ������� ���������� � ����������� �������� �� ���������� ��� ���������� ����, ��� ��� ������� ��� �����������, ���� ����� ���� �������� ����� � �������� ���������� ���������':
			case '��������� �������� �������': contents_contractor_certificate($name); break;
			case '�������� ������, ������������� ���������� �����������':
			case '�������� ������, �� �������������� ������������ ��������, ����� ��� ��': contents_statement($content_item); break;
			case '���������� ��� ����������� �����������': contents_tax_insurrance_currency(); break;
			case '�������': contents_contract(); break;
			case '�������������� ����������� ���������� ��������': contents_legal_documents(); break;
			case '�������������� ���������� ����������� ������': contents_interested_documents(); break;
			case '�������� ������������ ��������������� ����������': contents_unseal(); break;
			case '�������� ������� ��������������� �����������': contents_award_check(); break;
			case '���������� �����������':
			case '������� ��������� ���������� ��������': contents_order_tender($content_item, true); break;
			case '��������� �����������': contents_order_tender($content_item, false); break;
			case '��������� �������� ���������':	
			case '������� ��������� ��������': contents_order_contract($content_item); break;
			case '��� ����������� ���������':
				if (isset($data['���������'])) contents_order($content_item, $data[$name]);
				break;
			case '������� �������� ����������': contents_order($content_item, $data[$name]); break;
			case '�������� ��������� ��� ����� ��� �������':
				if (has_service_category($data['���������'])) break; // else continue
			case '���������� ��������� ������ ��������':
			case '���������� ���������� ��� ��������� ���������':
			case '�������� ��� ��������� ����������':
				if (!$data['����']) break; // else continue
			default: contents_default($content_item); break;
		}
}

unset($c2, $content_item, $count, $countA, $name);
?>

\sect

<?php

rtf_close(__FILE__);