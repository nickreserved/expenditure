<?
require_once('engine/init.php');
require_once('header.php');

if (isset($bills_buy)) {
?>

\sectd\pgwsxn11906\pghsxn16838\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

\pard\plain\qr\b <?=chk(toUppercase($data['Μονάδα']))?>\b0\par\par
\pard\plain
\qc{\fs24\b\ul ΒΕΒΑΙΩΣΗ}\par\par
\qj Βεβαιώνουμε ότι αφού επισκεφθήκαμε τα διάφορα καταστήματα για να βρούμε τα καλύτερα ποιοτικά και συμφέροντα κατά τιμή είδη ενεργήσαμε την προμήθεια των υλικών που αναγράφονται στην έκθεση γενομένης δαπάνης.\par
\pard\plain\qr\par <?=chk($data['Πόλη']) . ', ' . $data['ΗμερομηνίαΤελευταίουΤιμολογίου']?>\par

\pard\plain\fs23\par
\trowd\cellx5103\cellx10206\qc
- Ο -\line ΠΡΟΕΔΡΟΣ\line\line\line <?=chk($data['ΠρόεδροςΑγοράςΔιάθεσης']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΠρόεδροςΑγοράςΔιάθεσης']['Βαθμός'])?>\cell
- ΤΑ -\line ΜΕΛΗ\line\line\line <?=chk($data['ΜέλοςΑγοράςΔιάθεσηςΑ']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΜέλοςΑγοράςΔιάθεσηςΑ']['Βαθμός'])?>
\line\line\line <?=chk($data['ΜέλοςΑγοράςΔιάθεσηςΒ']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΜέλοςΑγοράςΔιάθεσηςΒ']['Βαθμός'])?>\cell\row

\pard\plain\par\par\par
\qc{\fs24\b\ul ΒΕΒΑΙΩΣΗ}\par\par
\qj Βεβαιώνεται η αγορά, παραλαβή, μη χρέωση και διάθεση των υλικών που αναγράφονται στην έκθεση γενομένης δαπάνης σύμφωνα με τις Φ.092/235631/9 Αυγ 1982 Απόφαση ΥΦΕΘΑ και Φ.600.9/12/601600/Σ.129/13 Ιαν 2005/ΓΕΣ/ΔΟΙ/3γ.\par
\pard\plain\qr\par <?=chk($data['Πόλη']) . ', ' . $data['ΗμερομηνίαΤελευταίουΤιμολογίου']?>\par

\pard\plain\fs23\par
\trowd\cellx3402\cellx6804\cellx10206\qc
ΕΘΕΩΡΗΘΗ\line - Ο -\line ΔΚΤΗΣ\line\line\line <?=chk($data['Δκτης']['Ονοματεπώνυμο'])?>\line <?=chk($data['Δκτης']['Βαθμός'])?>\cell
\line - Ο -\line ΕΟΥ\line\line\line <?=chk($data['ΕΟΥ']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΕΟΥ']['Βαθμός'])?>\cell
\line - Ο -\line Δχστης\line\line\line <?=chk($data['Δχστης']['Ονοματεπώνυμο'])?>\line <?=chk($data['Δχστης']['Βαθμός'])?>\cell\row

\sect

<? } ?>