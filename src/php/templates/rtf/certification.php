<?
require_once('engine/init.php');
require_once('header.php');

if (isset($bills_buy)) {
?>

\sectd\pgwsxn11906\pghsxn16838\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

\pard\plain <?=chk(getBrigate($data['ΓραφείοΣχηματισμού']))?>\par <?=chk($data['ΣύντμησηΜονάδας'])?>\par
\qc{\fs24\b\ul ΒΕΒΑΙΩΣΗ}\par\par
\qj Βεβαιώνεται η αγορά, παραλαβή, μη χρέωση και διάθεση των υλικών που αναγράφονται στην έκθεση γενομένης δαπάνης σύμφωνα με την Φ.600/50/29216/09 Απρ 2002/ΥΕΘΑ (ΦΕΚ Β' 467/15-04-2002).\par\par

\pard\plain\fs23\par
\trowd\cellx5103\cellx10206\qc
ΕΘΕΩΡΗΘΗ\line - Ο -\line ΔΚΤΗΣ\line\line\line <?=chk($data['Δκτης']['Ονοματεπώνυμο'])?>\line <?=chk($data['Δκτης']['Βαθμός'])?>\cell
\line - Ο -\line Δχστης\line\line\line <?=chk($data['Δχστης']['Ονοματεπώνυμο'])?>\line <?=chk($data['Δχστης']['Βαθμός'])?>\cell\row

\pard\plain\qj\par\par Βεβαιώνουμε ότι αφού επισκεφθήκαμε τα διάφορα καταστήματα για να βρούμε τα καλύτερα ποιοτικά και συμφέροντα κατά τιμή είδη ενεργήσαμε την προμήθεια των υλικών που αναγράφονται στην έκθεση γενομένης δαπάνης.\par\par

\pard\plain\fs23\par
\trowd\cellx5103\cellx10206\qc
- Ο -\line ΠΡΟΕΔΡΟΣ\line\line\line <?=chk($data['ΠρόεδροςΑγοράςΔιάθεσης']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΠρόεδροςΑγοράςΔιάθεσης']['Βαθμός'])?>\cell
- ΤΑ -\line ΜΕΛΗ\line\line\line <?=chk($data['ΜέλοςΑγοράςΔιάθεσηςΑ']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΜέλοςΑγοράςΔιάθεσηςΑ']['Βαθμός'])?>
\line\line\line <?=chk($data['ΜέλοςΑγοράςΔιάθεσηςΒ']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΜέλοςΑγοράςΔιάθεσηςΒ']['Βαθμός'])?>\cell\row

\sect

<? } ?>