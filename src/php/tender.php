<?php
require_once('functions.php');
require_once('report.php');

/** ���������� ��� ���� ��� �������. */
function at_unit_address() {
	global $data;
	return '���� ���� ' . article(gender($data['������ ������']), 1) . ' '
			. inflectPhrase($data['������ ������'], 1) . ', ' . get_unit_info();
}

/** ������ ����������� ������������ ���� ��������.
 * @param array $contractor � ����������� ������
 * @param string $title �� ����������� ��� �����������
 * @param string $order �� ������� ��� �� ������������ */
function sharing_proof($contractor, $title, $order) {
	global $data;
?>
\pard\plain\sb120\sa120\fi567\tx1134\tx1701\tx2268\qc{\b\ul ����������� ������������}\par
\qj ������ \u8230.\u8230.\u8230.\u8230.\u8230. {\fs20\i (����������)} ��� ��� \u8230.\u8230.\u8230. � ������������� \u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230. {\fs20\i (������, �������������)} ��� ������� <?=rtf(article(gender($data['������ ������']), 2, true) . ' ' . inflectPhrase($data['������ ������'], 2))?> ���� ���� ���� ��� ����������� <?=rtf($contractor['��������'])?> ��� ��������� <?=rtf($contractor['���������'])?>, ��� ����������� ��� ������ ��������� ���, ��� ���������� <?=isset($contractor['�����']) ? rtf($contractor['�����']) : '\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230. {\fs20\i (�������������)}'?>, �� ������� <?=$order?> ��� ����� <?=rtf($title)?>.\par

\pard\plain\par
\trowd\trkeep\trqc\trautofit1\trpaddfl3\trpaddl113\trpaddfr3\trpaddr113
\clftsWidth1\clNoWrap\cellx4394\clftsWidth1\clNoWrap\cellx8788\qc
� ������������\line\line\line\line\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\line{\fs20\i (������, �������������)}\cell
���������� ��� �����������\line\line\line\line <?=isset($contractor['�����']) ? rtf($contractor['�����']) : '\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\line{\fs20\i (�������������)}'?>\cell\row

\sect

<?php
}

/** ������ �� ������ ������� ��� ������ �� ��������� � ���������� ��������.
 * @param array $tender � ����������� */
function contractor_legal_documents($tender) {
	foreach($tender['�������������� �����������'] as $par => $document)
		echo '\tab ' . greeknum($par + 1) . '.\tab ' . $document . '.\par' . PHP_EOL;
	++$par;
?>

\tab <?=greekNum(++$par)?>.\tab ��������� �������� �������.\par
\tab <?=greekNum(++$par)?>.\tab ���������� ��� ����������� �����������.\par
\tab <?=greekNum(++$par)?>.\tab �������� ������, �� �������������� ������������ ��������, ����� ��� ��.\par
\tab <?=greekNum(++$par)?>.\tab �������� ������, ������������� ���������� �����������.\par
<?php
	if ($tender['��������� �������� ����� ���������'])
		echo '\tab ' . greekNum(++$par) . '.\tab �����������, ��� ��� �������� ��� ��������, ���������� � ���������� ���������� ��������� ����� ��������� ����� 5% ��� ������� ����� ��� ��������.\par' . PHP_EOL;
}

/** ������ ���� ���������� �����, �� 2� ������� ���������� "�.".
 * @param array $contractor � �������������� ����������� ������ ��� �� �������
 * @param int $count � ����� ������� ��� ����������� ����� �� ��� ����� */
function tender_competitor($contractor, $count) {
	echo '\tab ' . greekNum($count + 1) . '.\tab ' . rtf(get_contractor_id($contractor)) . '.\par' . PHP_EOL;
}

/** ������ ��� ����� ��������� ���� ����������� �����, �� 3� ������� ���������� "(1)".
 * @param array $ar � ����� ��������� ��� ����������� ����� ��� �� �������
 * @param int $count � ����������� ����� ������� ��� ������ ��������� */
function tender_competitor_list($ar, & $count = 1) {
	foreach($ar as $i)
		echo '\tab\tab (' . $count++ . ')\tab ' . rtf($i) . '.\par' . PHP_EOL;
}

/** ������ ���� ������������ ������ �� ������ �������� ����, �� 2�-3� ������� ���������� "�. (1)".
 * @param array $competitors �������� ��� ����������� ������
 * @param string $key �� ������ ��� $competitors ��� �������� ��� array ��� �������� ���� �������� */
function tender_competitors_list($competitors, $key) {
	foreach($competitors as $count => $competitor) {
		tender_competitor($competitor['��������������'], $count);
		if (isset($competitor[$key]))
			tender_competitor_list($competitor[$key]);
	}
}

/** ������ ���� ������������ ������ �� ���� ������ ��������� ����, �� 2�-3� ������� ���������� "�. (1)".
 * @param array $competitors �������� ��� ����������� ������ */
function tender_rejected_competitors_list($competitors) {
	foreach($competitors as $count => $competitor) {
		tender_competitor($competitor['��������������'], $count);
		$count = 1;
		if (isset($competitor['����� ��������� ����������']))
			tender_competitor_list($competitor['����� ��������� ����������'], $count);
		if (isset($competitor['����� ��������� ����������� ���������']))
			tender_competitor_list($competitor['����� ��������� ����������� ���������'], $count);
	}
}

/** ������� ��� ��������� ����������� ��� ���������, ���������� 1. */
function unseal_intro_committee() {
	global $data;
?>

\pard\plain\sb120\sa120\fi567\tx1134\tx1701\tx2268\qj
1.\tab � �������� ����������� ��� ����������� ��� ����������� ���������, ��� ������������ �� ��� <?=$data['��� ����������� ���������']['���������']?> ��� ����������� ��� ����:\par
\tab �.\tab <?=personi($data['�������� �����������'], 2)?> �� �������\par
\tab �.\tab <?=personi($data['� ����� �����������'], 2)?> ���\par
\tab �.\tab <?=personi($data['� ����� �����������'], 2)?> �� ����,\par

<?php
}

/** ������� ��� ��������� ������������, ���������� 1.
 * @param array $per_tender �� �������� ��� ����������� */
function unseal_intro($per_tender) {
	$tender = $per_tender['�����������'];
	unseal_intro_committee();
?>
������ ���� ����������� ��� ���������� ��� ��������� ��� ����������� ��� ������� ����������� �����������, ��� ������������ �� ��� <?=$tender['��������� �����������']['���������']?> ��� <?=$per_tender['���������']?>.\par
<?php }

/** ������� ��� ��������� ������������, ���������� 1-3.
 * @param array $per_tender �� �������� ��� �����������
 * @param array $invoices �� ��������� ��� ����������� */
function unseal_a_1_3($per_tender) {
	$tender = $per_tender['�����������'];
	unseal_intro($per_tender); ?>

2.\tab � �������� ������� ��� <?=strftime('%d %b %y', $tender['������ ������������ ���������'])?> ��� ��������� ��� ��� <?=strftime('%H:%M', $tender['������ ������������ ���������'])?> ��� �������� ��� ������� ��������� ��� ��������� �� ��������������, ��������������� ��� ������ ��� ����� ��������� ���� ��������.\par
3.\tab ���� �� ���� ��� �������� ���������� ��������� ��� ���������, � �������� ������ ��� �� ������ �� ����� ����� ����� ���� �������� ��� ������ � ���������� ��� ������������.\par
<?php }

/** ������� ��� ��������� ������������, ���������� 4-3.
 * @param array $tender � �����������
 * @param int $par �������� ������� ����������, � ������ ����������� �� ��� ��������� */
function unseal_a_4_8($tender, & $par) { ?>
<?=$par++?>.\tab ���� �� ��������� ����������� �� ����������� ������, �������� ��� ��������� ��� ��������� � ��������� ��� �����������.\par
<?=$par++?>.\tab ��������� � ��������, ���� ������� �� ��������� �������� ��� ��������� (������������ �������, ���), ����������� ��� ������ ������ ���������. ��� �� �������������� ��� � ������� ��������, ��� �����, �������������� ��� ������������.\par
<?=$par++?>.\tab �� ��������������, ����� ��� �� �������������� ��� � ������� �������� ��� ��������, ��������� ��������:\par
<?php tender_competitors_list($tender['��������������'], '�������������� ����������'); ?>
<?=$par++?>.\tab � �������� ��������� ���� ���������� ��� ��������������� ���������� ��� ��� �������� ��������� ��� ���������� <?php
	$rejected = $tender['������������� �'];
	// ������� ������� �� �� ����� �����������.
	if (!count($tender['������������ �'])) {
		echo '��� �������� ��� ��������������� ���������� ���� ��� ������������� ������������, ����� ��� ������� ��� ���������� ��� ����������, ��� ���� �������� ������:\par' . PHP_EOL;
		tender_competitors_list($rejected, '����� ��������� ����������');
	} else {
		echo '��� ������� ��� ��������������� ���������� ��� ������������� ������������, ����� ������� ��� ���������� ��� ����������';
		if (count($rejected)) {
			echo ', �� �������� ��� �������� ������������ ��� �� �������������� ���� �� �������� ����� ��� ���� �������� ������:\par' . PHP_EOL;
			tender_competitors_list($rejected, '����� ��������� ����������');
		} else echo '.\par' . PHP_EOL;
	}
}

/** ������� ��� ��������� ������������ ����������� ���������, �� ���� ������ ���������.
 * @param array $tender � �����������
 * @param int $par �������� ������� ����������, � ������ ����������� �� ��� ��������� */
function unseal_economic($tender, & $par) {
	$rejected = $tender['������������� �'];
	$accepted = $tender['������������ �'];
	// ������� ������� �� �� ����� �����������.
	if (!count($accepted)) {
		echo '��� �������� ��� ����������� ��������� ���� ��� ������������� ������������, ����� ��� ������� ��� ���������� ��� ����������, ��� ���� �������� ������:\par' . PHP_EOL;
		tender_competitors_list($rejected, '����� ��������� ����������� ���������');
	} else {
		echo '��� ������� ��� ����������� ��������� ��� ������������� ������������, ����� ������� ��� ���������� ��� ����������';
		if (count($rejected)) echo ', �� �������� ��� �������� ������������ ��� �� ����������� ��������� ���� ��� �������� ������ ��� ���� �������� ������:\par' . PHP_EOL;
		else echo '.\par' . PHP_EOL;
		tender_competitors_list($rejected, '����� ��������� ����������� ���������');
		// �������� ���������
		echo $par++ . '.\tab �� ������������� ����� (�� ���) �������������� ';
		if ($tender['�������� ���� �����'])
			echo '���� ���� ������������ ����������� ��������� ��� ��������������.\par' . PHP_EOL;
		else {
			echo '�� ����:\par' . PHP_EOL;
			foreach($accepted as $count => $competitor)
				echo '\tab ' . greekNum($count + 1) . '.\tab '
						. rtf($competitor['��������������']['��������']) . ' (���: '
						. rtf($competitor['��������������']['���']) . '): '
						. euro($competitor['��������']) . '.\par' . PHP_EOL;
		}
	}
}

/** ������� ��� ����� "���������" ��� ���������.
 * @param int $par � ������� ��� ���������� */
function unseal_suggestion($par) {
	echo $par;
?>.\tab ������� ��� �������, � �������� ����������� ��� ����������� ��� ����������� ��� ���������\par
\qc{\b � � � � � � � � � �}\par\qj
<?php
}

/** ������� ��������� ��� ��������� �����������. */
function tender_committee_signs() {
	global $data;
?>

\pard\plain\par
\trowd\trkeep\trqc\trautofit1\trpaddfl3\trpaddl113\trpaddfr3\trpaddr113
\clftsWidth1\clNoWrap\cellx4394\clftsWidth1\clNoWrap\cellx8788\qc
���������\line - � -\line ��������\line\line\line <?=rtf($data['�������� �����������']['�������������'])?>\line <?=rtf($data['�������� �����������']['������'])?>\cell
\line - �� -\line ����\line\line\line <?=rtf($data['� ����� �����������']['�������������'])?>\line <?=rtf($data['� ����� �����������']['������'])?>
\line\line\line <?=rtf($data['� ����� �����������']['�������������'])?>\line <?=rtf($data['� ����� �����������']['������'])?>\cell\row

\sect

<?php }


/** ������� ��������� ��� �� �������� ������������.
 * ��������� ���������� ��� ���������, ��������� ��� ��������� ��� �������� ���
 * ��������.
 * @param int $par � ������� ��� ���������� ���������� */
function unseal_signs($par) { ?>
<?=$par?>.\tab ��� ��� ���������� ��� �������, ���������� �� ����� ��������, �� ����� ���� ����������� ��� ����������, �����������.\par

<?php
	tender_committee_signs();
}

/** �� �������� ��� ��������� �� ���� ��������� �� ������������ ���� �����.
 * @param array $tender � �����������
 * @return array ����� �� �� �������� ��� ��������� */
function per_item_contractors($tender) {
	global $data;
	return array_values(array_filter($data['���������'],
			function($per_contract) use($tender) {
				return isset($per_contract['�����������']) && $tender == $per_contract['�����������'];
			}));
}

/** ������� ��� �������� ��� ��� ����� ��� � ������� �������, �� ������������ ���� �����.
 * @param array $tender � ����������� */
function unseal_per_item_contractors($tender) {
	echo '��� ������� ��� ��������������� ���� ����� ���������, ���� ��������:\par' . PHP_EOL;
	foreach (per_item_contractors($tender) as $count => $per_contract) {
		$contractor = $per_contract['����������'];
		tender_competitor($contractor, $count);
		report($per_contract['���������'], $per_contract['�����']);
		echo '\pard\plain\sb120\sa120\fi567\tx1134\tx1701\tx2268\qj' . PHP_EOL;
	}
}

/** ������� ��� ��������� ��������� ������������ ����������� ���������.
 * ������������ �� �������� ��� ���������
 * @param array $per_tender �������� ��� �����������
 * @param int $par �������� ������� ����������, � ������ ����������� �� ��� ��������� */
function unseal_suggestion_economic($per_tender, $par) {
	unseal_suggestion($par++);
	$tender = $per_tender['�����������'];
	if (!count($tender['������������ �'])) {
		echo '��� ��������� ��� ����������� �� ���� �����, ������� ����� ������������ ���������� ��� �������� ��� ���������� ��� ����������.\par' . PHP_EOL;
		if (is_expenditure()) trigger_error('� ����������� �� ������ �� ����� ������');
	} else if ($tender['�������� ���� �����']) unseal_per_item_contractors($tender);
	else if (count($tender['���������']) != 1) {
		if (is_expenditure()) trigger_error('� ����������� �� ������ �� ����� ������');
		echo '��� ��������� ��� ����������� �� 24 ����, �� ������� ���\' ��������� ���� ����������� ���������, ���� ��� ���� �������� ������������ ������, �� ������ �������� ��� �������������� ��������, ���� �������:\par' . PHP_EOL;
		foreach($tender['���������'] as $count => $competitor)
			tender_competitor($competitor['��������������'], $count);
	} else {
		$contractor = $per_tender['���������'][0]['��������'];
		echo '��� ������� ��� ��������������� ��������� ��� ��� �������� �� ���������� ��������, ��� ����������� '
				. rtf($contractor['��������']) . ' (���: ' . rtf($contractor['���']) .
				'), ����� ����� ������� �� ���� ����� ��� ���������� ��� ������ ��� �������� ������������.\par' . PHP_EOL;
	}
	unseal_signs($par);
}

/** ������� ���� ��������� ������������ ��������������� ���������� ��� �������� ���������.
 * @param array $per_tender �������� ��� ����������� */
function technical_offer_unseal_report($per_tender) {
	$tender = $per_tender['�����������'];
	start_35_20();
?>

\pard\plain\sa120\qc\b �������� - ��������\line ������������ ��� �����������\line ��������������� ���������� ��� �������� ���������\par

<?php
	unseal_a_1_3($per_tender);
	$par = 4;
	unseal_a_4_8($tender, $par);
	$accepted = $tender['������������ �'];
	unseal_suggestion($par++);
	if (!count($accepted)) echo '��� ��������� ��� ����������� �� ���� �����, ������� ����� ������������ ���������� ��� �������� ��� ���������� ��� ����������.\par' . PHP_EOL;
	else {
		echo '��� ������� ��� ��������������� ���������� ��� ��� �������� ��������� ��� �������� ������������, ����� ����� �������� �� ���� ����� ��� ���������� ��� ������� ��� �������� ������������:\par' . PHP_EOL;
		foreach($accepted as $count => $competitor)
			tender_competitor($competitor['��������������'], $count);
	}
	unseal_signs($par);
}

/** ������� ���� ��������� ������������ ����������� ���������.
 * @param array $per_tender �������� ��� ����������� */
function economical_offer_unseal_report($per_tender) {
	$tender = $per_tender['�����������'];
	if (count($tender['������������ �'])) {
		start_35_20();
?>

\pard\plain\sa120\qc\b �������� - ��������\line ������������ ��� ����������� ����������� ���������\par

<?php unseal_intro($per_tender); ?>

2.\tab � �������� ������� ��� <?=strftime('%d %b %y ��� ��� %H:%M', $tender['������ ������������ ����������� ���������'])?>, �������� ��� ����������� ������, ��� ������ �� �������������� ���������� ��� �� �������� ��������� ����� ����� ������ ���� ����������� ���������� ��� ���������.\par
3.\tab ���������� � ����������� ��� ����������� ��������� ��� ���� � �������� ��� ����������, ���������� <?php
		$par = 4;
		unseal_economic($tender, $par);
		unseal_suggestion_economic($per_tender, $par);

	}	// if
}

/** ������� ���� ��������� ������������ ��� ������� �� ��� ����.
 * @param array $per_tender �������� ��� ����������� */
function offer_unseal_report($per_tender) {
	$tender = $per_tender['�����������'];
	start_35_20(); ?>

\pard\plain\sa120\qc\b �������� - ��������\line ������������ ��� �����������\line ��������������� ����������,\line �������� ��� ����������� ���������\par

<?php unseal_a_1_3($per_tender); ?>

4.\tab � �������� ��������� � ����������� ��� ������� ��� ��������������� ����������, ��� �������� ��������� ��� ��� ����������� ���������, �� ������ �� ��� ������� ����������.\par
<?php
	$par = 5;
	unseal_a_4_8($tender, $par);
	$accepted = $tender['������������ �'];
	if (count($accepted)) {
		echo $par++ . '.\tab ���������� � ����������� ��� ����������� ���������, ��� ����������� ������ ��� �� �������������� ���� ������ ��������.\par' . PHP_EOL;
		echo $par++ . '.\tab � �������� ���������� ��� ����������� ��������� ��� ���������� ';
		unseal_economic($tender, $par);
	}
	unseal_suggestion_economic($per_tender, $par);
}

/** ������� ��� ��������� ������������ ��������������� ���������� ��� �������� ���������.
 * �������� ��������� ��� �� ����� ��� �������� �������������. */
function technical_offer_unseal_reports() {
	global $data;
	foreach($data['�����������'] as $per_tender)
		if (has_2_unseals($per_tender['�����������'])) technical_offer_unseal_report($per_tender);
		else trigger_error('��� ����������� �� 2 ������, �� ��������� ����������, ������ �� ����� �� ����� <b>������ ������������ ����������� ���������</b>');
}

/** ������� ��� ��������� ������������ ���� �������� �� ��� ���� �� ��� ������.
 * �������� ��������� ��� �� ����� ��� �������� �������������. */
function offer_unseal_reports() {
	global $data;
	foreach($data['�����������'] as $per_tender) {
		$tender = $per_tender['�����������'];
		if (has_2_unseals($tender)) {
			technical_offer_unseal_report($per_tender);
			economical_offer_unseal_report($per_tender);
		} else offer_unseal_report($per_tender);
	}
}