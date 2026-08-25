<?php
require_once('functions.php');

/** Επιστέφει τις κατηγορίες κρατήσεων, ΦΕ και ΦΠΑ για μια λίστα τιμολογίων.
 * @param array $invoices Λίστα με τιμολόγια
 * @return array 3 array με τα παρακάτω κλειδιά:
 * <ul><li>'Κρατήσεις': array με κλειδιά τα ποσοστά των κρατήσεων των τιμολογίων και τιμές το
 * αντίστοιχο άθροισμα σε €. (Δεν γίνεται ανάλυση κρατήσεων, αλλά μόνο για το συνολικό ποσοστό κάθε
 * τιμολογίου)
 * <li>'ΦΕ': array με κλειδιά τα ποσοστά του ΦΕ των τιμολογίων και τιμές το αντίστοιχο άθροισμα σε €.
 * <li>'ΦΠΑ': array με κλειδιά τα ποσοστά των ΦΠΑ των τιμολογίων και τιμές το αντίστοιχο άθροισμα
 * σε €.<ul> */
function calc_per_deduction_incometax_vat($invoices) {
	$deductions = array(); $vat = array(); $incometax = array();
	foreach($invoices as $invoice) {
		if ($invoice['ΦΕ']) {			// Αθροιση του ΦΕ για κάθε τιμολόγιο
			$key = $invoice['ΦΕ']; $value = $invoice['Τιμές']['ΦΕ'];
			if (isset($incometax[$key])) $incometax[$key] += $value; else $incometax[$key] = $value;
		}
		if ($invoice['Τιμές']['Κρατήσεις']) {	// Αθροιση των κρατήσεων για κάθε τιμολόγιο
			$key = (string) $invoice['Κρατήσεις']['Σύνολο']; $value = $invoice['Τιμές']['Κρατήσεις'];
			if (isset($deductions[$key])) $deductions[$key] += $value; else $deductions[$key] = $value;
		}
		foreach($invoice['Κατηγορίες ΦΠΑ'] as $key => $value)	// Αθροιση των αξιών ΦΠΑ για κάθε τιμολόγιο
			if (isset($vat[$key])) $vat[$key] += $value; else $vat[$key] = $value;
	}
	return array('Κρατήσεις' => $deductions, 'ΦΕ' => $incometax, 'ΦΠΑ' => $vat);
}

/** Εξάγει τα στοιχεία μιας έκθεσης.
 * @param array $invoices Λίστα με τα τιμολόγια της έκθεσης
 * @param array $prices Λίστα με τα αθροίσματα των αξιών των τιμολογίων */
function report($invoices, $prices = null) {
	if (!isset($prices)) $prices = calc_sum_of_invoices_prices($invoices);
	$categories = calc_per_deduction_incometax_vat($invoices);
	$cells = <<<'EOD'
\trautofit1\trpaddfl3\trpaddl28\trpaddfr3\trpaddr28
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\clvertalc\cellx454
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clvertalc\cellx3799
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\clvertalc\cellx5046
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\clvertalc\cellx5896

EOD;
	if (count($categories['ΦΠΑ']) > 1)
		$cells .= <<<'EOD'
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\clvertalc\cellx6633

EOD;
	$cells .= <<<'EOD'
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\clvertalc\cellx7654
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\clvertalc\cellx8788

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


/** Εξάγει τα στοιχεία μιας έκθεσης, χωρίς τιμές.
 * Χρησιμοποιείται αποκλειστικά στην στην πρόσκληση υποβολής προσφορών.
 * @param array $invoices Λίστα με τα τιμολόγια της έκθεσης */
function report_no_prices_no_sums2($invoices) {
	$cells = <<<'EOD'
\trautofit1\trpaddfl3\trpaddl28\trpaddfr3\trpaddr28
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx454
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx4366
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx5613
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx6916
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx8788

EOD;
?>
\pard\plain\fs21
\trowd\trhdr<?=$cells?>
\qc\b A/A\cell ΠΕΡΙΓΡΑΦΗ\cell ΜΟΝΑΔΑ\line ΜΕΤΡΗΣΗΣ\cell ΠΟΣΟΤΗΤΑ\cell ΠΑΡΑΤΗΡΗΣΕΙΣ\b0\cell\row
\trowd<?=$cells?>
<?php
$count_items = 0;
foreach($invoices as $invoice)
	foreach($invoice['Είδη'] as $item) {
		?>\qr <?=++$count_items?>\cell\qj <?=rtf($item['Είδος'])?>\cell\qc <?=rtf($item['Μονάδα Mέτρησης'])?>\cell <?=num($item['Ποσότητα'])?>\cell\cell\row
<?php }
}

/** Εξάγει τον πίνακα μιας βεβαίωσης παραλλαβής και πρωτοκόλλου οριστικής ποιοτικής και ποσοτικής παραλαβής.
 * @param array $invoices Λίστα με τα τιμολόγια τα είδη των οποίων αναλύονται στον πίνακα
 * @param array $prices Λίστα με τα αθροίσματα των αξιών των τιμολογίων */
function acceptance_report($invoices, $prices = null) {
	if (!isset($prices))
		$prices = calc_sum_of_invoices_prices($invoices, array('Καθαρή Αξία', 'ΦΠΑ', 'Καταλογιστέο'));
	$cells = <<<'EOD'
\trautofit1\trpaddfl3\trpaddl28\trpaddfr3\trpaddr28
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\clvertalc\cellx425
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clvertalc\cellx2183
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\clvertalc\cellx3203
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\clvertalc\cellx3997
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\clvertalc\cellx5187
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\clvertalc\cellx6378
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\clvertalc\cellx7569
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\clvertalc\cellx8788

EOD;
?>
\pard\plain\fs20
\trowd\trhdr<?=$cells?>
\qc\b A/A\cell Είδος\cell Μονάδα\line Μέτρησης\cell Ποσότητα\cell Τιμή\line Μονάδας\cell Καθαρή\line Αξία\cell ΦΠΑ\cell Σύνολο\b0\cell\row
\trowd<?=$cells?>
<?php
$count_items = 0;
foreach($invoices as $invoice)
	foreach($invoice['Είδη'] as $item) {
		?>\qr <?=++$count_items?>\cell\qj <?=rtf($item['Είδος'])?>\cell\qc <?=rtf($item['Μονάδα Mέτρησης'])?>\cell <?=num($item['Ποσότητα'])?>\cell\qr <?=euro($item['Τιμή Μονάδας'])?>\cell <?=euro($item['Συνολική Τιμή'])?>\cell <?=euro($item['Τιμή ΦΠΑ'])?>\cell <?=euro($item['Συνολική Τιμή με ΦΠΑ'])?>\cell\row
<?php } ?>
\trowd
\trautofit1\trpaddfl3\trpaddl28\trpaddfr3\trpaddr28
\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clvertalc\clftsWidth1\cellx5187
\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\clvertalc\cellx6378
\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\clvertalc\cellx7569
\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\clvertalc\cellx8788
\qc\b ΓΕΝΙΚΟ ΣΥΝΟΛΟ\cell\qr <?=euro($prices['Καθαρή Αξία'])?>\cell <?=euro($prices['ΦΠΑ'])?>\cell <?=euro($prices['Καταλογιστέο'])?>\b0\cell\row
<?php
}