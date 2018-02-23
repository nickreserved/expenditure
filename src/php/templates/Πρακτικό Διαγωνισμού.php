<?
require_once('engine/init.php');
require_once('header.php');

if ($data['ΤύποςΔιαγωνισμού'] == 'Χωρίς Διαγωνισμό')
	trigger_error('Επιλέξατε τη δαπάνη «Χωρίς Διαγωνισμό»', E_USER_ERROR);
$a = $data['ΤύποςΔιαγωνισμού'] == 'Δημόσιος Διαγωνισμός';
?>

\sectd\lndscpsxn\pgwsxn16838\pghsxn11906\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

\pard\plain\qr\b <?=chk(toUppercase($data['Μονάδα']))?>\par
\sa113\qc{\fs24\ul ΠΡΑΚΤΙΚΟ <?=$a ? 'ΔΗΜΟΣΙΟΥ' : 'ΠΡΟΧΕΙΡΟΥ'?> ΔΙΑΓΩΝΙΣΜΟΥ\par}
\qj\b0 για την ανάδειξη μειοδότη για «<?=chk($data['ΘέμαΔιαγωνισμού'])?>» σε εκτέλεση της \ul <?=chk_order($data['ΔγηΑνάθεσης'])?>\ul0  Δγης.\par
\qc\b Οι όροι του διαγωνισμού επισυνάπτονται.\par

\pard\plain\fs24
\trowd\trhdr<?$a = '
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx567
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx5803
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx13039
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx15137
'; ?><?=$a?>\qc\b A/A\cell ΣΤΟΙΧΕΙΑ ΜΕΙΟΔΟΤΗ (Σφραγίδα)\cell ΠΡΟΣΦΟΡΑ\cell ΥΠΟΓΡΑΦΗ\cell\row
\trowd<?=$a?>\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\cell\cell\cell\cell\row

<? ob_start(); ?>
\pard\plain\par
\trowd\trkeep\trqc\trautofit1\trpaddfl3\trpaddl113\trpaddfr3\trpaddr113\clftsWidth1\clNoWrap\cellx3784\clftsWidth1\clNoWrap\cellx7568\clftsWidth1\clNoWrap\cellx11372\clftsWidth1\clNoWrap\cellx15137\qc
ΕΘΕΩΡΗΘΗ\line - Ο -\line ΔΚΤΗΣ\line\line\line <?=chk($data['Δκτης']['Ονοματεπώνυμο'])?>\line <?=chk($data['Δκτης']['Βαθμός'])?>\cell
\line - Ο -\line ΠΡΟΕΔΡΟΣ\line\line\line <?=chk($data['ΠρόεδροςΔιαγωνισμού']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΠρόεδροςΔιαγωνισμού']['Βαθμός'])?>\cell
\line - ΤΑ -\line ΜΕΛΗ\line\line\line <?=chk($data['ΜέλοςΔιαγωνισμούΑ']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΜέλοςΔιαγωνισμούΑ']['Βαθμός'])?>\cell
\line\line\line\line\line <?=chk($data['ΜέλοςΔιαγωνισμούΒ']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΜέλοςΔιαγωνισμούΒ']['Βαθμός'])?>\cell\row

\sect


<? $b = ob_get_flush(); ?>
\pard\plain\fs24
\trowd\trhdr<?=$a?>\qc\b A/A\cell ΣΤΟΙΧΕΙΑ ΜΕΙΟΔΟΤΗ (Σφραγίδα)\cell ΠΡΟΣΦΟΡΑ\cell ΥΠΟΓΡΑΦΗ\cell\row
\trowd<?=$a?>\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\line\cell\cell\cell\cell\row
<?=$b?>


\sectd\pgwsxn11906\pghsxn16838\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

\pard\plain\qr\b <?=chk(toUppercase($data['Μονάδα']))?>\par\par
\qj ΘΕΜΑ:\b0  <?=chk($data['ΘέμαΔιαγωνισμού'])?>\par\par
\fs24\ul\qc\b ΟΡΟΙ <?=$a ? 'ΔΗΜΟΣΙΟΥ' : 'ΠΡΟΧΕΙΡΟΥ'?> ΔΙΑΓΩΝΙΣΜΟΥ\b0\par\par
\pard\plain\tx567\tx1134\tx1701\tx2268\sa113\qj
\tab 1.\tab{\b\ul Τεχνικοί Όροι}\par
\tab\tab α.\tab . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .\par
\tab\tab β.\tab . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .\par
\tab 2.\tab{\b\ul Οικονομικοί Όροι}\par
\tab\tab α.\tab Στις προσφερόμενες τιμές δεν περιλαμβάνεται το ΦΠΑ.\par
\tab\tab β.\tab Οι νόμιμες κρατήσεις βαρύνουν τον Ανάδοχο.\par
\tab\tab γ.\tab Παρακρατείται ΦΕ . . . %.\par
\tab\tab δ.\tab Ο νικητής του διαγωνισμού είναι υποχρεωμένος να δώσει εγγυητική επιστολή ίση με το 5% του χρηματικού ποσού του έργου.\par
\tab\tab ε.\tab Η πληρωμή του αναδόχου θα γίνει μετά το πέρας των εργασιών και αφού ο ανάδοχος προσκομίσει:\par
\tab\tab\tab (1)\tab Φορολογική και ασφαλιστική ενημερότητα από το ΙΚΑ.\par
\tab\tab\tab (2)\tab Υπεύθυνη δήλωση ή βεβαίωση από αρμόδιο φορέα σχετικά με την ασφάλισή του ή όχι στο ΤΣΜΕΔΕ και ΤΠΕΔΕ και εάν είναι ή όχι εργολήπτης δημοσίων έργων, στην περίπτωση που δεν επιβαρυνθεί με κράτηση υπέρ ΤΠΕΔΕ  και/ή δεν καταβάλει τις εισφορές υπέρ ΤΣΜΕΔΕ.\par
\tab\tab στ.\tab . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .\par
\tab\tab ζ.\tab . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .\par
\tab 3.\tab{\b\ul Λοιποί Όροι}\par
\tab\tab α.\tab Χρόνος ισχύος της προσφοράς, 2 μήνες, από την ημερομηνία του διαγωνισμού.\par
\tab\tab β.\tab Η εργασία θα πρέπει να ολοκληρωθεί εντός 20 εργασίμων ημερών από την υπογραφή της σύμβασης. Για κάθε ημέρα καθυστέρησης και για . . . ημέρες, ο ανάδοχος υπόκειται ημερησία ποινική ρήτρα ίση με το . . .% της μέσης ημερήσιας αξίας του έργου (ποσό σύμβασης/συμβατικό χρόνο). Μετά την παρέλευση των . . . ημερών ο ανάδοχος μπορεί να κηρυχθεί έκπτωτος, οπότε δεν θα πληρωθεί για τις εργασίες που έχει τυχόν εκτελέσει.\par
\tab\tab γ.\tab Ο ανάδοχος οφείλει να γνωστοποιήσει στην Υπηρεσία το προσωπικό που θα εκτελέσει εργασίες. Το προσωπικό αυτό υποχρεούται να δέχεται τους ελέγχους των οργάνων ασφαλείας του Στρατοπέδου.\par
\tab\tab δ.\tab Η έγκριση του διαγωνισμού υπόκειται στο Διοικητή της Μονάδας.\par
\tab\tab ε.\tab . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .\par
\tab\tab στ.\tab . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .\par
<? for ($z = 0; $z < 2618; $z++) echo '. '; ?>\par

\pard\plain\fs23\par
\trowd\trkeep\trqc\trautofit1\trpaddfl3\trpaddl113\trpaddfr3\trpaddr113
\clftsWidth1\clNoWrap\cellx3402\clftsWidth1\clNoWrap\cellx6804\clftsWidth1\clNoWrap\cellx10206\qc
ΕΘΕΩΡΗΘΗ\line - Ο -\line ΔΚΤΗΣ\line\line\line <?=chk($data['Δκτης']['Ονοματεπώνυμο'])?>\line <?=chk($data['Δκτης']['Βαθμός'])?>\cell
\line - Ο -\line ΠΡΟΕΔΡΟΣ\line\line\line <?=chk($data['ΠρόεδροςΔιαγωνισμού']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΠρόεδροςΔιαγωνισμού']['Βαθμός'])?>\line\line\line\line\b - ΟΙ -\line ΔΙΑΓΩΝΙΣΘΕΝΤΕΣ\b0\line\line\line\line\line\line\line\line\line\cell
\line - ΤΑ -\line ΜΕΛΗ\line\line\line <?=chk($data['ΜέλοςΔιαγωνισμούΑ']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΜέλοςΔιαγωνισμούΑ']['Βαθμός'])?>
\line\line\line <?=chk($data['ΜέλοςΔιαγωνισμούΒ']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΜέλοςΔιαγωνισμούΒ']['Βαθμός'])?>\cell\row

\sect


\pard\plain\b <?=chk(toUppercase($data['Μονάδα']))?>\par\par
\fs24\ul\qc ΠΑΡΑΤΗΡΗΣΕΙΣ ΔΙΑΓΩΝΙΖΟΜΕΝΩΝ\par\par
\pard\plain<? for ($z = 0; $z < 6006; $z++) echo '. '; ?>\par

\pard\plain\fs23\par
\trowd\trkeep\trqc\trautofit1\trpaddfl3\trpaddl113\trpaddfr3\trpaddr113
\clftsWidth1\clNoWrap\cellx3402\clftsWidth1\clNoWrap\cellx6804\clftsWidth1\clNoWrap\cellx10206\qc
ΕΘΕΩΡΗΘΗ\line - Ο -\line ΔΚΤΗΣ\line\line\line <?=chk($data['Δκτης']['Ονοματεπώνυμο'])?>\line <?=chk($data['Δκτης']['Βαθμός'])?>\cell
\line - Ο -\line ΠΡΟΕΔΡΟΣ\line\line\line <?=chk($data['ΠρόεδροςΔιαγωνισμού']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΠρόεδροςΔιαγωνισμού']['Βαθμός'])?>\line\line\line\line\b - ΟΙ -\line ΔΙΑΓΩΝΙΣΘΕΝΤΕΣ\b0\line\line\line\line\line\line\line\line\line\cell
\line - ΤΑ -\line ΜΕΛΗ\line\line\line <?=chk($data['ΜέλοςΔιαγωνισμούΑ']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΜέλοςΔιαγωνισμούΑ']['Βαθμός'])?>
\line\line\line <?=chk($data['ΜέλοςΔιαγωνισμούΒ']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΜέλοςΔιαγωνισμούΒ']['Βαθμός'])?>\cell\row
