<?php
require_once('functions.php');
init(5);

$invoices = array_values(array_filter($data['Τιμολόγια'], function($i) { return is_supply($i['Κατηγορία']); }));
if (count($invoices)) {
	$c = count($invoices) > 1 ? 'α' : 'ο';	// Κατάληξη πληθυντικού - ενικού σχετικά με τον αριθμό τιμολογίων
?>

\sectd\sbkodd\pgwsxn11906\pghsxn16838\marglsxn1984\margrsxn1134\margtsxn1134\margbsxn1134\facingp\margmirror

\pard\plain\qr <?=rtf($data['Σχηματισμός'])?>\line <?=rtf($data['Μονάδα'])?>\par\par
\fs24\qc{\b ΒΕΒΑΙΩΣΗ ΜΗ ΧΡΕΩΣΗΣ ΥΛΙΚΩΝ}\par\par

\qj Βεβαιώνεται η αγορά, παραλαβή, μη χρέωση και διάθεση για ικανοποίηση άμεσων αναγκών <?=rtf(article(gender($data['Μονάδα Πλήρες']), 1) . ' ' . inflectPhrase($data['Μονάδα Πλήρες'], 1))?>, των αναγραφόμενων στ<?=$c?> υπ' αριθμόν <?=get_names_key($invoices, 'Τιμολόγιο')?> τιμολόγι<?=$c?> προμήθειας σύμφωνα με την Φ.092/235631/9 Αυγ 1982 Απόφαση ΥΦΕΘΑ και Φ.600.9/12/601600/Σ.129/13 Ιαν 2005/ΓΕΣ/ΔΟΙ/3γ.\par
\pard\plain\qr\par <?=rtf($data['Έδρα']) . ', ' . strftime('%d %b %y', get_newer_timestamp($invoices))?>\par

\pard\plain\par
\trowd\clftsWidth1\clNoWrap\cellx2929\clftsWidth1\clNoWrap\cellx5859\clftsWidth1\clNoWrap\cellx8788\qc
ΘΕΩΡΗΘΗΚΕ\line - Ο -\line ΔΚΤΗΣ\line\line\line <?=rtf($data['Δκτης']['Ονοματεπώνυμο'])?>\line <?=rtf($data['Δκτης']['Βαθμός'])?>\cell
\line - Ο -\line ΕΟΥ\line\line\line <?=rtf($data['ΕΟΥ']['Ονοματεπώνυμο'])?>\line <?=rtf($data['ΕΟΥ']['Βαθμός'])?>\cell
\line - Ο -\line Δχστης\line\line\line <?=rtf($data['Δχστης']['Ονοματεπώνυμο'])?>\line <?=rtf($data['Δχστης']['Βαθμός'])?>\cell\row

\sect

<?php

}	// if

unset($c, $invoices);

rtf_close(__FILE__);