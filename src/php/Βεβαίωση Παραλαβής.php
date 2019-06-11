<?php
require_once('init.php');
require_once('header.php');

$invoices = array();
foreach($data['Τιμολόγια ανά Δικαιούχο'] as $per_contractor) {
	if (isset($per_contractor['Σύμβαση'])) continue;
	$invoices = array_merge($invoices, $per_contractor['Τιμολόγια']);
}
foreach(get_invoices_by_category($invoices) as $category => $invoices) {
	// Τμήματα κειμένου που αλλάζουν μέσα στο πρωτόκολλο σχετικά με το είδος τιμολογίου
	$a = $category == 'Προμήθεια Υλικών'
			? array('προμηθειών', 'Προμηθειών', 'ΠΡΟΜΗΘΕΙΩΝ')
			: array('υπηρεσιών', 'Υπηρεσιών', 'ΥΠΗΡΕΣΙΩΝ');
	// Κατάληξη πληθυντικού - ενικού σχετικά με τον αριθμό τιμολογίων
	$c = count($invoices) > 1 ? 'α' : 'ο';
?>

\sectd\sbkodd\pgwsxn11906\pghsxn16838\marglsxn1984\margrsxn1134\margtsxn1134\margbsxn1134\facingp\margmirror

\pard\plain\qr <?=rtf($data['Σχηματισμός'])?>\line <?=rtf($data['Μονάδα'])?>\par\par
\fs24\qc{\b ΒΕΒΑΙΩΣΗ ΠΑΡΑΛΑΒΗΣ <?=$a[2]?>}\par\par

\qj Βεβαιώνεται η αγορά, παραλαβή και διάθεση για ικανοποίηση άμεσων αναγκών της Μονάδας, των αναγραφόμενων στ<?=$c?> υπ' αριθμόν <?=get_names_key($invoices, 'Τιμολόγιο')?> τιμολόγι<?=$c?> <?=$a[0]?> σύμφωνα με την Φ.092/235631/9 Αυγ 1982 Απόφαση ΥΦΕΘΑ και Φ.600.9/12/601600/Σ.129/13 Ιαν 2005/ΓΕΣ/ΔΟΙ/3γ.\par
\pard\plain\qr\par <?=rtf($data['Έδρα']) . ', ' . strftime('%d %b %y', get_newer_invoice_timestamp($invoices))?>\par

\pard\plain\par
\trowd\trkeep\trqc\trautofit1\trpaddfl3\trpaddl113\trpaddfr3\trpaddr113
\clftsWidth1\clNoWrap\cellx2929\clftsWidth1\clNoWrap\cellx5859\clftsWidth1\clNoWrap\cellx8788\qc
ΘΕΩΡΗΘΗΚΕ\line - Ο -\line ΔΚΤΗΣ\line\line\line <?=rtf($data['Δκτης']['Ονοματεπώνυμο'])?>\line <?=rtf($data['Δκτης']['Βαθμός'])?>\cell
\line - Ο -\line ΠΡΟΕΔΡΟΣ\line\line\line <?=rtf($data["Πρόεδρος Παραλαβής $a[1]"]['Ονοματεπώνυμο'])?>\line <?=rtf($data["Πρόεδρος Παραλαβής $a[1]"]['Βαθμός'])?>\cell
\line - ΤΑ -\line ΜΕΛΗ\line\line\line <?=rtf($data["Α Μέλος Παραλαβής $a[1]"]['Ονοματεπώνυμο'])?>\line <?=rtf($data["Α Μέλος Παραλαβής $a[1]"]['Βαθμός'])?>
\line\line\line <?=rtf($data["Β Μέλος Παραλαβής $a[1]"]['Ονοματεπώνυμο'])?>\line <?=rtf($data["Β Μέλος Παραλαβής $a[1]"]['Βαθμός'])?>\cell\row

\sect

<?php }

unset($a, $c, $category, $invoices, $per_contractor);

rtf_close(__FILE__);