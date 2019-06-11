<?php
require_once('init.php');
require_once('order.php');
require_once('report.php');
require_once('header.php');

if (has_direct_assignment()) {

start_35_20();
order_publish('ΑΔΑ Απόφασης Απευθείας Ανάθεσης', $draft);
order_header_recipients(ifexist2(!$draft, $data, 'Απόφαση Απευθείας Ανάθεσης'), $draft, null,
		'Απόφαση Απευθείας Ανάθεσης για ' . rtf(ucwords($data['Τίτλος'])),
		array(
			'ΝΔ.721/70 (ΦΕΚ Α\' 251) περί «Οικονομικής Μερίμνης και Λογιστικού των Ενόπλων Δυνάμεων»',
			'N.2292/95 (ΦΕΚ Α\' 35) «Οργάνωση και Λειτουργία Υπουργείου Εθνικής ’μυνας, διοίκηση και έλεγχος Ενόπλων Δυνάμεων και άλλες διατάξεις»',
			'Ν.3861/10 (ΦΕΚ Α\' 112) «Ενίσχυση της διαφάνειας με την υποχρεωτική ανάρτηση νόμων και πράξεων των κυβερνητικών, διοικητικών και αυτοδιοικητικών οργάνων στο διαδίκτυο "Πρόγραμμα Διαύγεια" και άλλες διατάξεις»',
			'Ν.4270/14 «Αρχές δημοσιονομικής διαχείρισης και εποπτείας (ενσωμάτωση της οδηγίας 2011/85/ΕΕ) - Δημόσιο λογιστικό και άλλες διατάξεις»',
			'Ν.4412/16 (ΦΕΚ Α\' 147) «Δημόσιες Συμβάσεις Έργων, Προμηθειών και Υπηρεσιών (προσαρμογή στις Οδηγίες 2014/24/ΕΕ και 2014/25/ΕΕ)»',
			'Φ.032/8/66625/Σ.15516/05 Οκτ 17/Απόφαση ΑΝΥΕΘΑ (ΦΕΚ Β\' 3495)',
			$data['Απόφαση Ανάληψης Υποχρέωσης']
		));
?>1.\tab Έχοντας υπόψη τα σχετικά:\par
\qc{\b Α π ο φ α σ ί ζ ο υ μ ε}\par\qj
την ανάθεση της πίστωσης για «<?=rtf($data['Τίτλος'])?>», όπως παρακάτω:\par
<?php
$count = count($data['Τιμολόγια ανά Δικαιούχο']) > 1 ? 1 : 0;
$contracts = 0;
foreach($data['Τιμολόγια ανά Δικαιούχο'] as $per_contractor) {
	if (isset($per_contractor['Σύμβαση'])) {
		if ($per_contractor['Σύμβαση']['Τύπος Διαγωνισμού'] != 'Απευθείας Ανάθεση') continue;
		++$contracts;
	}
	$invoices = $per_contractor['Τιμολόγια'];
	$contractor = $per_contractor['Δικαιούχος'];
	echo '\tab ' . ($count ? greeknum($count++) . '.\tab ' : null);
	echo isset($per_contractor['Σύμβαση']) && $per_contractor['Σύμβαση']['Τίτλος'] != $data['Τίτλος']
		? "Για «{$per_contractor['Σύμβαση']['Τίτλος']}» " . get_contractor_title($invoices, 2, true)
		: ucfirst(get_contractor_title($invoices, 2, true));
	echo " «{$contractor['Επωνυμία']}», ΑΦΜ {$contractor['ΑΦΜ']}";
	if (isset($contractor['Τηλέφωνο'])) echo ', τηλέφωνο: ' . $contractor['Τηλέφωνο'];
	if (isset($contractor['Διεύθυνση'])) echo ', διεύθυνση: ' . $contractor['Διεύθυνση'];
	if (isset($contractor['e-mail'])) echo ', e-mail: ' . $contractor['e-mail'];
	?>, με τα ακόλουθα αναλυτικά οικονομικά στοιχεία.\par
<?php report($invoices, $per_contractor['Τιμές']); ?>
\pard\plain\sb120\sa120\fi567\tx1134\tx1701\tx2268\qj
<?php } ?>

2.\tab Η ως άνω δαπάνη έχει εγκριθεί με τα παρακάτω στοιχεία:\par
\tab α.\tab ΑΛΕ: <?=$data['ΑΛΕ']?>.\par
\tab β.\tab Εις βάρος: <?=$data['Τύπος Χρηματοδότησης']?><?php
if ($data['Τύπος Χρηματοδότησης'] != 'Ιδίων Πόρων')
	echo " έτους " . date('Y', orelse($data, 'Timestamp Τελευταίου Τιμολογίου', time()));
?>.\par
\tab γ.\tab Αρμοδιότητας: Ε.Φ. <?=$data['ΕΦ']?>.\par
3.\tab Κατά τα λοιπά ισχύουν<?=$contracts ? ' οι όροι ' . ($contracts == 1 ? 'της Σύμβασης' : 'των Συμβάσεων') . ' και' : null?> οι διατάξεις του (ε) σχετικού νόμου.\par
4.\tab Στοιχεία Αναθέτουσας Αρχής: <?=get_unit_address()?>.\par
<?php
if ($data['Αναρτητέα στο διαδίκτυο']) { ?>
5.\tab Η παρούσα απόφαση αναρτάται στο ΚΗΜΔΗΣ και στο ΔΙΑΥΓΕΙΑ.\par

<?php
}

order_footer($draft);

$to = array();
foreach($data['Τιμολόγια ανά Δικαιούχο'] as $per_contractor)
	if (get_invoice_tender_type($per_contractor) == 'Απευθείας Ανάθεση')
		$to[] = get_contractor_recipient($per_contractor['Δικαιούχος']);
order_recipient_table($to, array($data['Μονάδα']));
?>

\sect

<?php
unset($a, $pre, $pre2, $to, $c2, $categories, $contractor, $contracts, $count, $count_items, $invoice, $invoices, $item, $k, $per_contractor);

}	// if

rtf_close(__FILE__);