<?php
require_once('init.php');
require_once('order.php');
require_once('header.php');

startOrder();
if ($data['Αναρτητέα στο διαδίκτυο']) { ?>

\pard\plain\qr ΑΔΑ: <?=orelse2(!$draft, $data, 'ΑΔΑ Απόφασης Απευθείας Ανάθεσης', '........')?>\par
\pard\plain\qc{\ul{\b ΑΝΑΡΤΗΤΕΑ ΣΤΟ ΔΙΑΔΙΚΤΥΟ}}\par\par

<?php } else { ?>

\pard\plain\qc{\ul{\b ΜΗ ΑΝΑΡΤΗΤΕΑ ΣΤΟ ΔΙΑΔΙΚΤΥΟ}}\par\par

<?php
}
preOrderN(ifexist2(!$draft, $data, 'Απόφαση Απευθείας Ανάθεσης'), $draft, null,
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
2.\tab Την ανάθεση της πίστωσης για «<?=rtf($data['Τίτλος'])?>», όπως παρακάτω:\par
<?php
$count = count($data['Τιμολόγια ανά Δικαιούχο']) > 1 ? 1 : 0;
$contracts = 0;
foreach($data['Τιμολόγια ανά Δικαιούχο'] as $per_contractor) {
	$invoices = $per_contractor['Τιμολόγια'];
	$prices = $per_contractor['Τιμές'];
	$categories = calc_per_deduction_incometax_vat($invoices);
	$contractor = $invoices[0]['Δικαιούχος'];
	//TODO: Εδώ όσοι βρίσκονται με διαγωνισμό πρέπει να μην συμπεριλαμβάνονται
	$contract = get_contract($invoices[0]);
	if (isset($contract)) ++$contracts;
	echo '\tab ' . ($count ? greeknum($count++) . '.\tab ' : null);
	if (isset($contract['Τίτλος'])) echo "Για «{$contract['Τίτλος']}» ";
	echo ucfirst(get_contractor_title($invoices, 2, 2)) . " «{$contractor['Επωνυμία']}», ΑΦΜ {$contractor['ΑΦΜ']}";
	if (isset($contractor['Τηλέφωνο'])) echo ', τηλέφωνο: ' . $contractor['Τηλέφωνο'];
	if (isset($contractor['Διεύθυνση'])) echo ', διεύθυνση: ' . $contractor['Διεύθυνση'];
	if (isset($contractor['ΤΚ'])) echo ', Τ.Κ.: ' . $contractor['ΤΚ'];
	if (isset($contractor['e-mail'])) echo ', e-mail: ' . $contractor['e-mail'];
	?>, με τα ακόλουθα αναλυτικά οικονομικά στοιχεία.\par
<?php ob_start();	// Buffer με επικεφαλίδες πίνακα ?>
\trautofit1\trpaddfl3\trpaddl28\trpaddfr3\trpaddr28
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx454
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx3799
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx4819
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx6066
<? if (count($categories['ΦΠΑ']) > 1) { ?>
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx6633
<? } ?>
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx7654
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx8788
<? $c2 = ob_get_clean(); ?>

\pard\plain\fs21
\trowd\trhdr<?=$c2?>

\qc\b A/A\cell ΠΕΡΙΓΡΑΦΗ\cell ΜΟΝΑΔΑ\cell ΠΟΣΟΤΗΤΑ\cell<? if (count($categories['ΦΠΑ']) > 1) echo ' ΦΠΑ\cell'; ?> ΜΟΝΑΔΑ\cell ΣΥΝΟΛΟ\b0\cell\row

\trowd<?=$c2?>

<?
$count_items = 0;
foreach($invoices as $invoice)
	foreach($invoice['Είδη'] as $item) {
		?>\qr <?=++$count_items?>\cell\qj <?=rtf($item['Είδος'])?>\cell\qc <?=rtf($item['Μονάδα Mέτρησης'])?>\cell <?=num($item['Ποσότητα'])?>\cell\qr <? if (count($categories['ΦΠΑ']) > 1) echo percent($item['ΦΠΑ']) . '\cell'; ?> <?=euro($item['Τιμή Μονάδας'])?>\cell <?=euro($item['Συνολική Τιμή'])?>\cell\row
<?
	}
?>

\pard\tx1\tqdec\tx8760\trowd\trpaddfl3\trpaddl28\trpaddfr3\trpaddr28
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx8788
\b ΚΑΘΑΡΗ ΑΞΙΑ:\tab <?=euro($prices['Καθαρή Αξία'])?>\b0\cell\row<?
if ($contractor['Τύπος'] != 'PRIVATE_SECTOR')
	foreach($categories['Κρατήσεις'] as $k => $v)
		echo '+ Κρατήσεις ' . $k . '%:\tab ' . euro($v) . '\cell\row' . PHP_EOL;
foreach($categories['ΦΠΑ'] as $k => $v)
	echo '+ ΦΠΑ ' . percent($k) . ':\tab ' . euro($v) . '\cell\row' . PHP_EOL;
echo '\b ΚΑΤΑΛΟΓΙΣΤΕΟ:\tab ' . euro($prices['Καταλογιστέο']) . '\b0\cell\row' . PHP_EOL;
foreach($categories['Κρατήσεις'] as $k => $v)
	echo '- Κρατήσεις ' . $k . '%:\tab ' . euro($v) . '\cell\row' . PHP_EOL;
echo '\b ΠΛΗΡΩΤΕΟ:\tab ' . euro($prices['Πληρωτέο']) . '\b0\cell\row' . PHP_EOL;
foreach($categories['ΦΕ'] as $k => $v)
	echo '- ΦΕ ' . percent($k) . ':\tab ' . euro($v) . '\cell\row' . PHP_EOL;
if (count($categories['ΦΕ']))
	echo '\b ΥΠΟΛΟΙΠΟ ΠΛΗΡΩΤΕΟ:\tab ' . euro($prices['Υπόλοιπο Πληρωτέο']) . '\b0\cell\row' . PHP_EOL;
echo '\pard\plain\sb120\sa120\fi567\tx1134\tx1701\tx2268\qj';
}
?> 3.\tab Η ως άνω δαπάνη έχει εγκριθεί με τα παρακάτω στοιχεία:\par
\tab α.\tab ΑΛΕ: <?=$data['ΑΛΕ']?>.\par
\tab β.\tab Εις βάρος: <?=$data['Τύπος Χρηματοδότησης']?><?php
if ($data['Τύπος Χρηματοδότησης'] != 'Ιδίων Πόρων')
	echo " έτους " . date('Y', $data['Timestamp Τελευταίου Τιμολογίου']);
?>.\par
\tab γ.\tab Αρμοδιότητας: Ε.Φ. <?=$data['ΕΦ']?>.\par
4.\tab Κατά τα λοιπά ισχύουν<?=$contracts ? ' οι όροι ' . ($contracts == 1 ? 'της Σύμβασης' : 'των Συμβάσεων') . ' και' : null?> οι διατάξεις του (ε) σχετικού νόμου.\par
5.\tab Στοιχεία Αναθέτουσας Αρχής: <?=$data['Μονάδα Πλήρες']?>, διεύθυνση: <?=$data['Έδρα']?><?=isset($data['Διεύθυνση']) ? ", {$data['Διεύθυνση']}" : null?><?=isset($data['Τηλέφωνο']) ? ", τηλέφωνο: {$data['Τηλέφωνο']}" : null?><?=isset($data['ΤΚ']) ? ", Τ.Κ. {$data['ΤΚ']}" : null?>.\par
<?php
if ($data['Αναρτητέα στο διαδίκτυο']) { ?>
6.\tab Η παρούσα απόφαση αναρτάται στο ΚΗΜΔΗΣ και στο ΔΙΑΥΓΕΙΑ.\par

<?php
}

postOrder($draft);

$to = array();
foreach($data['Τιμολόγια ανά Δικαιούχο'] as $per_contractor) {
	$contractor = $per_contractor['Τιμολόγια'][0]['Δικαιούχος'];
	//TODO: Εδώ όσοι βρίσκονται με διαγωνισμό πρέπει να μην συμπεριλαμβάνονται
	$a = $contractor['Επωνυμία'];
	$pre = ' ('; $pre2 = ', ';
	if (isset($contractor['Διεύθυνση'])) { $a .= $pre . $contractor['Διεύθυνση']; $pre = $pre2; }
	if (isset($contractor['ΤΚ'])) { $a .= $pre . $contractor['ΤΚ']; $pre = $pre2; }
	if ($pre == $pre2) $a .= ')';
	$to[] = $a;
}
recipientTableOrder($to, array($data['Μονάδα']));
?>

\sect

<?php
unset($a, $pre, $pre2, $to, $c2, $categories, $contract, $contractor, $contracts, $count, $count_items, $invoice, $invoices, $item, $k, $per_contractor);

rtf_close(__FILE__);