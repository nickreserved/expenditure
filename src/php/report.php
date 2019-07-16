<?php
require_once('functions.php');

/** Εξάγει τα στοιχεία μιας έκθεσης.
 * @param array $invoices Λίστα με τα τιμολόγια της έκθεσης
 * @param array $prices Λίστα με τα αθροίσματα των αξιών των τιμολογίων */
function report($invoices, $prices = null) {
	if (!isset($prices)) $prices = calc_sum_of_invoices_prices($invoices);
	$categories = calc_per_deduction_incometax_vat($invoices);
	$cells = <<<'EOD'
\trautofit1\trpaddfl3\trpaddl28\trpaddfr3\trpaddr28
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx454
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx3799
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx5046
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx6066

EOD;
	if (count($categories['ΦΠΑ']) > 1)
		$cells .= <<<'EOD'
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx6633

EOD;
	$cells .= <<<'EOD'
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx7654
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx8788

EOD;
?>
\pard\plain\fs21
\trowd\trhdr<?=$cells?>
\qc\b A/A\cell ΠΕΡΙΓΡΑΦΗ\cell ΜΟΝΑΔΑ\line ΜΕΤΡΗΣΗΣ\cell ΠΟΣΟΤΗΤΑ\cell<?php if (count($categories['ΦΠΑ']) > 1) echo ' ΦΠΑ\cell'; ?> ΑΞΙΑ\line ΜΟΝΑΔΑΣ\cell ΑΞΙΑ\line ΣΥΝΟΛΟΥ\b0\cell\row
\trowd<?=$cells?>
<?php
$count_items = 0;
foreach($invoices as $invoice)
	foreach($invoice['Είδη'] as $item) {
		?>\qr <?=++$count_items?>\cell\qj <?=rtf($item['Είδος'])?>\cell\qc <?=rtf($item['Μονάδα Mέτρησης'])?>\cell <?=num($item['Ποσότητα'])?>\cell\qr <?php if (count($categories['ΦΠΑ']) > 1) echo percent($item['ΦΠΑ']) . '\cell '; ?><?=euro($item['Τιμή Μονάδας'])?>\cell <?=euro($item['Συνολική Τιμή'])?>\cell\row
<?php } ?>
\pard\tx1\tqr\tx8760\trowd\trpaddfl3\trpaddl28\trpaddfr3\trpaddr28
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx8788
\b ΚΑΘΑΡΗ ΑΞΙΑ:\tab <?=euro($prices['Καθαρή Αξία'])?>\b0\cell\row
<?php
	if ($invoices[0]['Δικαιούχος']['Τύπος'] != 'Ιδιωτικός Τομέας')
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
}

/** Εξάγει τα στοιχεία μιας έκθεσης, χωρίς τιμές, για ένα σχέδιο σύμβασης.
 * Χρησιμοποιείται αποκλειστικά στην εξαγωγή διακήρυξης διαγωνισμού.
 * @param array $invoices Λίστα με τα τιμολόγια της έκθεσης */
function report_no_prices($invoices) {
	$categories = calc_per_deduction_incometax_vat($invoices);
	report_no_prices_no_sums($invoices); ?>

\pard\tx1\tqdec\tx8760\trowd\trpaddfl3\trpaddl28\trpaddfr3\trpaddr28
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx8788
{\b ΚΑΘΑΡΗ ΑΞΙΑ:}\cell\row
<?php
	if ($invoices[0]['Δικαιούχος']['Τύπος'] != 'Ιδιωτικός Τομέας')
		foreach($categories['Κρατήσεις'] as $k => $v)
			echo '+ Κρατήσεις ' . $k . '%:\cell\row' . PHP_EOL;
	foreach($categories['ΦΠΑ'] as $k => $v)
		echo '+ ΦΠΑ ' . percent($k) . ':\cell\row' . PHP_EOL;
	echo '{\b ΚΑΤΑΛΟΓΙΣΤΕΟ:}\cell\row' . PHP_EOL;
	foreach($categories['Κρατήσεις'] as $k => $v)
		echo '- Κρατήσεις ' . $k . '%:\cell\row' . PHP_EOL;
	echo '{\b ΠΛΗΡΩΤΕΟ:}\cell\row' . PHP_EOL;
	foreach($categories['ΦΕ'] as $k => $v)
		echo '- ΦΕ ' . percent($k) . ':\cell\row' . PHP_EOL;
	if (count($categories['ΦΕ']))
		echo '{\b ΥΠΟΛΟΙΠΟ ΠΛΗΡΩΤΕΟ:}\cell\row' . PHP_EOL;
}

/** Εξάγει τα στοιχεία μιας έκθεσης, χωρίς τιμές, για ένα σχέδιο σύμβασης.
 * Χρησιμοποιείται αποκλειστικά στην εξαγωγή διακήρυξης διαγωνισμού.
 * @param array $invoices Λίστα με τα τιμολόγια της έκθεσης */
function report_no_prices_no_sums($invoices) {
	$cells = <<<'EOD'
\trautofit1\trpaddfl3\trpaddl28\trpaddfr3\trpaddr28
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx454
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx3799
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx5046
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx6066
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx7654
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx8788

EOD;
?>
\pard\plain\fs21
\trowd\trhdr<?=$cells?>
\qc\b A/A\cell ΠΕΡΙΓΡΑΦΗ\cell ΜΟΝΑΔΑ\line ΜΕΤΡΗΣΗΣ\cell ΠΟΣΟΤΗΤΑ\cell ΑΞΙΑ\line ΜΟΝΑΔΑΣ\cell ΑΞΙΑ\line ΣΥΝΟΛΟΥ\b0\cell\row
\trowd<?=$cells?>
<?php
$count_items = 0;
foreach($invoices as $invoice)
	foreach($invoice['Είδη'] as $item) {
		?>\qr <?=++$count_items?>\cell\qj <?=rtf($item['Είδος'])?>\cell\qc <?=rtf($item['Μονάδα Mέτρησης'])?>\cell <?=num($item['Ποσότητα'])?>\cell\cell\cell\row
<?php }
}