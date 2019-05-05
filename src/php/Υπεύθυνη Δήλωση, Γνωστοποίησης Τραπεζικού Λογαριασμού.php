<?
require_once('init.php');
require_once('header.php');
require_once('statement.php');

foreach($data['Τιμολόγια ανά Δικαιούχο'] as $per_contractor) {
	$v = $per_contractor['Τιμολόγια'][0]['Δικαιούχος'];
	if ($v['Τύπος'] != 'PRIVATE_SECTOR') continue;
	$v['Προς'] = 'ΔΟΥ';
	if (!isset($v['Ονοματεπώνυμο'])) $v['Ονοματεπώνυμο'] = $v['Επωνυμία'];
	if (!isset($v['Τόπος Κατοικίας'])) $v['Τόπος Κατοικίας'] = $v['Διεύθυνση'];
	if (!isset($v['e-mail'])) $v['e-mail'] = '';
	$v['Δήλωση'] = 'Το χρηματικό ποσό που δικαιούμαι να εισπράξω σαν «{\b ' . rtf($v['Επωνυμία']) . '}», ΑΦΜ: ' . rtf($v['ΑΦΜ']) . ', τηλέφωνο: ' . rtf($v['Τηλέφωνο']) . ', να κατατεθεί στο λογαριασμό IBAN {\b ' . iban($v['ΙΒΑΝ']) . '} ({\b ' . bank($v['ΙΒΑΝ']) . '}).\par Επισυνάπτεται φωτοτυπία του βιβλιαρίου καταθέσεων.';
	$v['Ημερομηνία Έκδοσης'] = get_newer_invoice_date($per_contractor['Τιμολόγια']);
	statement($v);
}

rtf_close(__FILE__); ?>