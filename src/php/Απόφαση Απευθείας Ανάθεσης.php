<?php
require_once('init.php');
require_once('order.php');
require_once('report.php');
require_once('header.php');

if (has_direct_assignment()) {

start_35_20();
order_publish($data, 'ΑΔΑ Απόφασης Απευθείας Ανάθεσης', $output);
order_header_recipients(ifexist2($output, $data, 'Απόφαση Απευθείας Ανάθεσης'), $output, null,
		'Απόφαση Απευθείας Ανάθεσης για ' . rtf(ucwords($data['Τίτλος'])),
		array(
			'ΝΔ.721/1970 (ΦΕΚ Α\' 251) περί «Οικονομικής Μερίμνης και Λογιστικού των Ενόπλων Δυνάμεων»',
			'N.2292/1995 (ΦΕΚ Α\' 35) «Οργάνωση και Λειτουργία Υπουργείου Εθνικής ’μυνας, διοίκηση και έλεγχος Ενόπλων Δυνάμεων και άλλες διατάξεις»',
			'Ν.3861/2010 (ΦΕΚ Α\' 112) «Ενίσχυση της διαφάνειας με την υποχρεωτική ανάρτηση νόμων και πράξεων των κυβερνητικών, διοικητικών και αυτοδιοικητικών οργάνων στο διαδίκτυο "Πρόγραμμα Διαύγεια" και άλλες διατάξεις»',
			'Ν.4270/2014 «Αρχές δημοσιονομικής διαχείρισης και εποπτείας (ενσωμάτωση της οδηγίας 2011/85/ΕΕ) - Δημόσιο λογιστικό και άλλες διατάξεις»',
			'Ν.4412/2016 (ΦΕΚ Α\' 147) «Δημόσιες Συμβάσεις Έργων, Προμηθειών και Υπηρεσιών (προσαρμογή στις Οδηγίες 2014/24/ΕΕ και 2014/25/ΕΕ)»',
			'Φ.032/8/66625/Σ.15516/05 Οκτ 17/Απόφαση ΑΝΥΕΘΑ (ΦΕΚ Β\' 3495)',
			$data['Απόφαση Ανάληψης Υποχρέωσης']
		));
?>1.\tab Έχοντας υπόψη τα σχετικά:\par
\qc{\b Α π ο φ α σ ί ζ ο υ μ ε}\par\qj
την ανάθεση της πίστωσης για «<?=rtf($data['Τίτλος'])?>», όπως παρακάτω:\par
<?php
$count = count($data['Δικαιούχοι']) > 1 ? 1 : 0;
$contracts = 0;
foreach($data['Δικαιούχοι'] as $per_contractor) {
	if (isset($per_contractor['Σύμβαση'])) {
		if (isset($per_contractor['Διαγωνισμός'])) continue;
		++$contracts;
	}
	$invoices = $per_contractor['Τιμολόγια'];
	$contractor = $per_contractor['Δικαιούχος'];
	echo '\tab ' . ($count ? greeknum($count++) . '.\tab ' : null);
	echo isset($per_contractor['Σύμβαση']) && $per_contractor['Σύμβαση']['Τίτλος'] != $data['Τίτλος']
		? 'Για «' . rtf($per_contractor['Σύμβαση']['Τίτλος']) . '» στον ανάδοχο ' : 'Στον ανάδοχο ';
	echo rtf(get_contractor_full_info($contractor));
	?>, με τα ακόλουθα αναλυτικά οικονομικά στοιχεία.\par
<?php report($invoices, $per_contractor['Τιμές']); ?>
\pard\plain\sb120\sa120\fi567\tx1134\tx1701\tx2268\qj
<?php } ?>

2.\tab Η ως άνω δαπάνη έχει εγκριθεί με τα παρακάτω στοιχεία:\par
\tab α.\tab ΑΛΕ: <?=$data['ΑΛΕ']?>.\par
\tab β.\tab Εις βάρος: <?=$data['Τύπος Χρηματοδότησης']?><?php
if ($data['Τύπος Χρηματοδότησης'] != 'Ιδίων Πόρων')
	echo " έτους " . date('Y', orelse($data, 'Ημερομηνία Τελευταίου Τιμολογίου', time()));
?>.\par
\tab γ.\tab Αρμοδιότητας: Ε.Φ. <?=$data['ΕΦ']?>.\par
3.\tab Κατά τα λοιπά ισχύουν<?=$contracts ? ' οι όροι ' . ($contracts == 1 ? 'της Σύμβασης' : 'των Συμβάσεων') . ' και' : null?> οι διατάξεις του (ε) σχετικού νόμου.\par
4.\tab Στοιχεία Αναθέτουσας Αρχής: <?=rtf($data['Μονάδα Πλήρες'] . ', ' . get_unit_address())?>.\par
<?php
order_footer($output);

$to = array();
foreach($data['Δικαιούχοι'] as $per_contractor)
	if (!isset($per_contractor['Διαγωνισμός']))
		$to[] = get_contractor_recipient($per_contractor['Δικαιούχος']);
order_recipient_table($to, array($data['Μονάδα']));
?>

\sect

<?php
unset($contractor, $contracts, $count, $invoices, $per_contractor, $to);

}	// if

rtf_close(__FILE__);