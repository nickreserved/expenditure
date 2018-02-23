<?
require_once('engine/init.php');
require_once('header.php');

if (!$bills_contract) {
?>

\sectd\pgwsxn11906\pghsxn16838\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

\pard\plain\qr\b <?=chk(toUppercase($data['Μονάδα']))?>\par\par
\fs24\ul\qc ΒΕΒΑΙΩΣΗ\line ΕΚΤΕΛΕΣΗΣ ΤΟΥ ΕΡΓΟΥ ΑΠΟ ΟΠΛΙΤΕΣ ΤΗΣ ΜΟΝΑΔΑΣ\par\par
\pard\plain\tx567\tx1134\qj
\tab <?=chk(ucfirst($data['ΠεριοχήΈργου']))?> σήμερα την <?=$data['ΗμερομηνίαΠροσωρινήςΟριστικήςΠαραλαβής']?> ο Αξκος Έργου <?=man_ext($data['ΑξκοςΈργου'], 0)?> που ορίστηκε με την \ul <?=chk_order($data['ΔγηΑνάθεσης'])?>\ul0 , βεβαιώνει ότι το έργο εκτελέστηκε από Οπλίτες.\par\par

\pard\plain\fs23\par
\trowd\trkeep\trqc\trautofit1\trpaddfl3\trpaddl113\trpaddfr3\trpaddr113
\clftsWidth1\clNoWrap\cellx5103\clftsWidth1\clNoWrap\cellx10206\qc
ΕΘΕΩΡΗΘΗ\line - Ο -\line ΔΚΤΗΣ\line\line\line <?=chk($data['Δκτης']['Ονοματεπώνυμο'])?>\line <?=chk($data['Δκτης']['Βαθμός'])?>\cell
\line - Ο -\line ΑΞΚΟΣ ΕΡΓΟΥ\line\line\line <?=chk($data['ΑξκοςΈργου']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΑξκοςΈργου']['Βαθμός'])?>\cell\row

\sect

<? } ?>