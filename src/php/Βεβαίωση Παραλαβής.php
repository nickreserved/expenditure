<?php
require_once('functions.php');

init(6);
$invoices = array();
foreach($data['Δικαιούχοι'] as $per_contractor) {
	if (isset($per_contractor['Σύμβαση'])) continue;
	$invoices = $per_contractor['Τιμολόγια'];

	foreach(get_invoices_by_category($invoices) as $a => $invoices) {
		// Κατάληξη πληθυντικού - ενικού σχετικά με τον αριθμό τιμολογίων
		$c = count($invoices) > 1 ? 'α' : 'ο';
?>

\sectd\sbkodd\pgwsxn11906\pghsxn16838\marglsxn1984\margrsxn1134\margtsxn1134\margbsxn1134\facingp\margmirror

\pard\plain\qr <?=rtf($data['Σχηματισμός'])?>\line <?=rtf($data['Μονάδα'])?>\line <?=rtf($data['Έδρα']) . ', ' . strftime('%d %b %y', get_newer_timestamp($invoices))?>\par\par
\fs24\qc{\b ΒΕΒΑΙΩΣΗ ΠΑΡΑΛΑΒΗΣ <?=$a ? 'ΠΡΟΜΗΘΕΙΩΝ' : 'ΥΠΗΡΕΣΙΩΝ'?>}\par\par

\qj Βεβαιώνεται η ποιοτική και ποσοτική παραλαβή <?=$a ? 'και παράδοση στον διαχειριστή υλικού, ' : ''?>των ακόλουθων ειδών, που αναγράφονται στ<?=$c?> υπ' αριθμόν <?=get_names_key($invoices, 'Τιμολόγιο')?> τιμολόγι<?=$c?> <?=$a ? 'προμήθειας υλικών' : 'παροχής υπηρεσιών'?> τα οποία παρείχε ο οικονομικός φορέας <?=rtf(get_contractor_id($per_contractor['Δικαιούχος']))?>.\par
<?php report($invoices); ?>
\pard\plain\par
\pard\plain\qc\li4535 ΘΕΩΡΗΘΗΚΕ\line - Ο -\line ΔΚΤΗΣ\line\line\line <?=rtf($data['Δκτης']['Ονοματεπώνυμο'])?>\line <?=rtf($data['Δκτης']['Βαθμός'])?>\par

\sect

<?php }
}

unset($a, $c, $invoices, $per_contractor);

rtf_close(__FILE__);