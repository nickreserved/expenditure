<?php
require_once('functions.php');
init(4);

if ($data['Έργο'] && !has_service_category($data['Τιμολόγια'])) {

	start_35_20();
?>

\pard\plain\sa240\qr\b <?=rtf(strtouppergn($data['Μονάδα Πλήρες']))?>\par
\fs24\ul\qc ΒΕΒΑΙΩΣΗ\line ΕΚΤΕΛΕΣΗΣ ΤΟΥ ΕΡΓΟΥ ΑΠΟ ΟΠΛΙΤΕΣ ΤΗΣ ΜΟΝΑΔΑΣ\par
\pard\plain\sb120\sa120\fi567\tx1134\tx1701\qj
Σήμερα την <?=strftime('%d %b %y', $data['Ημερομηνία Τελευταίου Τιμολογίου'])?>, ο επιβλέπων του έργου «<?=rtf($data['Τίτλος'])?>», <?=personi($data['Αξκος Έργου'], 0)?>, που ορίστηκε με την <?=$data['Δγη Συγκρότησης Επιτροπών']['Ταυτότητα']?>, βεβαιώνει ότι το έργο εκτελέστηκε από Οπλίτες.\par

\pard\plain\fs23\par\par
\trowd\trkeep\trqc\trautofit1\trpaddfl3\trpaddl113\trpaddfr3\trpaddr113
\clftsWidth1\clNoWrap\cellx4394\clftsWidth1\clNoWrap\cellx8787\qc
ΕΘΕΩΡΗΘΗ\line - Ο -\line ΔΚΤΗΣ\line\line\line <?=rtf($data['Δκτης']['Ονοματεπώνυμο'])?>\line <?=rtf($data['Δκτης']['Βαθμός'])?>\cell
\line - Ο -\line ΑΞΚΟΣ ΕΡΓΟΥ\line\line\line <?=rtf($data['Αξκος Έργου']['Ονοματεπώνυμο'])?>\line <?=rtf($data['Αξκος Έργου']['Βαθμός'])?>\cell\row

\sect

<?php

}	// if

rtf_close(__FILE__);