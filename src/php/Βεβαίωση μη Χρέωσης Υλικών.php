<?php
require_once('init.php');
require_once('header.php');

$invoices = get_invoices_by_category($data['Τιμολόγια']);
$invoices = $invoices['Προμήθεια Υλικών'];
// Κείμενο με τα τιμολόγια, της μορφής '1/31-12-19, 9/1-12-19 και 5/31-1-19'
$invoice_list = get_names($invoices, 'Τιμολόγιο');
// Η ημερομηνία του νεότερου τιμολογίου από τα επιλεγμένα
$newer_invoice_date = strftime('%d %b %y', get_newer_invoice_timestamp($invoices));
// Κατάληξη πληθυντικού - ενικού σχετικά με τον αριθμό τιμολογίων
$c = count($invoices) > 1 ? 'α' : 'ο';
?>

\sectd\sbkodd\pgwsxn11906\pghsxn16838\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

\pard\plain\qr <?=rtf($data['Σχηματισμός'])?>\line <?=rtf($data['Μονάδα'])?>\par\par
\fs24\qc{\b ΒΕΒΑΙΩΣΗ ΜΗ ΧΡΕΩΣΗΣ ΥΛΙΚΩΝ}\par\par

\qj Βεβαιώνεται η αγορά, παραλαβή, μη χρέωση και διάθεση για ικανοποίηση άμεσων αναγκών της Μονάδας, των αναγραφόμενων στ<?=$c?> υπ' αριθμόν <?=$invoice_list?> τιμολόγι<?=$c?> προμήθειας σύμφωνα με την Φ.092/235631/9 Αυγ 1982 Απόφαση ΥΦΕΘΑ και Φ.600.9/12/601600/Σ.129/13 Ιαν 2005/ΓΕΣ/ΔΟΙ/3γ.\par
\pard\plain\qr\par <?=rtf($data['Έδρα']) . ', ' . $newer_invoice_date?>\par

\pard\plain\par
\trowd\cellx3402\cellx6804\cellx10206\qc
ΘΕΩΡΗΘΗΚΕ\line - Ο -\line ΔΚΤΗΣ\line\line\line <?=rtf($data['Δκτης']['Ονοματεπώνυμο'])?>\line <?=rtf($data['Δκτης']['Βαθμός'])?>\cell
\line - Ο -\line ΕΟΥ\line\line\line <?=rtf($data['ΕΟΥ']['Ονοματεπώνυμο'])?>\line <?=rtf($data['ΕΟΥ']['Βαθμός'])?>\cell
\line - Ο -\line Δχστης\line\line\line <?=rtf($data['Δχστης']['Ονοματεπώνυμο'])?>\line <?=rtf($data['Δχστης']['Βαθμός'])?>\cell\row

\sect

<?
unset($invoices, $newer_invoice_date, $invoice_list, $c);

rtf_close(__FILE__);
?>