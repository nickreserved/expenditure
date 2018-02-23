<?
require_once('engine/init.php');
require_once('header.php');

if ($data['ΤύποςΔιαγωνισμού'] == 'Χωρίς Διαγωνισμό')
	trigger_error('Επιλέξατε τη δαπάνη «Χωρίς Διαγωνισμό»', E_USER_ERROR);
$a = $data['ΤύποςΔιαγωνισμού'] == 'Δημόσιος Διαγωνισμός';
?>

\sectd\lndscpsxn\pgwsxn16838\pghsxn11906\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

\pard\plain\b <?=chk(toUppercase($data['Μονάδα']))?>\par
\sa113\qc{\fs24\ul ΠΡΑΚΤΙΚΟ <?=$a ? 'ΔΗΜΟΣΙΟΥ' : 'ΠΡΟΧΕΙΡΟΥ'?> ΔΙΑΓΩΝΙΣΜΟΥ\par}
\qj\b0 για την ανάδειξη μειοδότη για «<?=chk($data['ΘέμαΔιαγωνισμού'])?>» κατόπιν της \ul <?=chk_order($data['ΔγηΔιαγωνισμού'])?>\ul0  Δγης.\par
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

\pard\plain\b <?=chk(toUppercase($data['Μονάδα']))?>\par\par
\fs24\ul\qc ΠΡΟΔΙΑΓΡΑΦΕΣ ΚΑΙ ΟΡΟΙ ΔΙΑΓΩΝΙΣΜΟΥ\par\par
\pard\plain\tx567\tx1134\sa113\qj
\tab\b 1.\b0\tab Στις τιμές περιλαμβάνεται το ΦΠΑ.\par
\tab\b 2.\b0\tab Στις τιμές δεν περιλαμβάνονται οι Κρατήσεις.\par
\tab\b 3.\b0\tab Ο νικητής του διαγωνισμού είναι υποχρεωμένος να δώσει εγγυοδοσία ίση με το 5% του χρηματικού ποσού που πρότεινε.\par
\tab\b 4.\b0\tab <? for ($z = 0; $z < 5689; $z++) echo '. '; ?>\par

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

\sect


\pard\plain\b <?=chk(toUppercase($data['Μονάδα']))?>\par\par
\fs24\ul\qc ΠΡΑΞΗ ΕΠΙΤΡΟΠΗΣ ΔΙΕΝΕΡΓΕΙΑΣ ΔΙΑΓΩΝΙΣΜΟΥ\par\par
\pard\plain\qj Κλείνεται το παρόν πρακτικό διαγωνισμού στο όνομα του μειοδότη . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . για «<?=chk($data['ΘέμαΔιαγωνισμού'])?>».\par
Τα προσφερθέντα από αυτόν είδη είναι κατάλληλα και με τιμές συμφέρουσες για το δημόσιο. Αυτός κατέθεσε την εγγυοδοσία καλής εκτέλεσης της προμήθειας.\par

\pard\plain\fs23\par
\trowd\trkeep\trqc\trautofit1\trpaddfl3\trpaddl113\trpaddfr3\trpaddr113
\clftsWidth1\clNoWrap\cellx3402\clftsWidth1\clNoWrap\cellx6804\clftsWidth1\clNoWrap\cellx10206\qc
ΕΘΕΩΡΗΘΗ\line - Ο -\line ΔΚΤΗΣ\line\line\line <?=chk($data['Δκτης']['Ονοματεπώνυμο'])?>\line <?=chk($data['Δκτης']['Βαθμός'])?>\line\line\line\line - Ο -\line ΠΡΟΕΔΡΟΣ\line\line\line <?=chk($data['ΠρόεδροςΔιαγωνισμού']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΠρόεδροςΔιαγωνισμού']['Βαθμός'])?>\cell
\line - Ο -\line ΕΟΥ\line\line\line <?=chk($data['ΕΟΥ']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΕΟΥ']['Βαθμός'])?>\line\line\line\line - ΤΑ -\line ΜΕΛΗ\line\line\line <?=chk($data['ΜέλοςΔιαγωνισμούΑ']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΜέλοςΔιαγωνισμούΑ']['Βαθμός'])?>\cell
\line - Ο -\line ΤΕΛΕΥΤΑΙΟΣ ΜΕΙΟΔΟΤΗΣ\line\line\line\line\line\line\line\line\line\line\line\line <?=chk($data['ΜέλοςΔιαγωνισμούΒ']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΜέλοςΔιαγωνισμούΒ']['Βαθμός'])?>\cell\row

\pard\plain\fs24\par\par\b\ul\qc ΠΡΑΞΗ ΕΠΙΤΡΟΠΗΣ ΠΑΡΑΛΑΒΩΝ\par\par
\pard\plain\qj Βεβαιώνεται η παραλαβή του θέματος για χρήση, σύμφωνα με τους υποβληθέντες όρους.\par


\pard\plain\fs23\par
\trowd\trkeep\trqc\trautofit1\trpaddfl3\trpaddl113\trpaddfr3\trpaddr113
\clftsWidth1\clNoWrap\cellx3402\clftsWidth1\clNoWrap\cellx6804\clftsWidth1\clNoWrap\cellx10206\qc
ΕΘΕΩΡΗΘΗ\line - Ο -\line ΔΚΤΗΣ\line\line\line <?=chk($data['Δκτης']['Ονοματεπώνυμο'])?>\line <?=chk($data['Δκτης']['Βαθμός'])?>\cell
\line - Ο -\line ΠΡΟΕΔΡΟΣ\line\line\line <?=chk($data['ΠρόεδροςΔιαγωνισμού']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΠρόεδροςΔιαγωνισμού']['Βαθμός'])?>\cell
\line - ΤΑ -\line ΜΕΛΗ\line\line\line <?=chk($data['ΜέλοςΔιαγωνισμούΑ']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΜέλοςΔιαγωνισμούΑ']['Βαθμός'])?>
\line\line\line <?=chk($data['ΜέλοςΔιαγωνισμούΒ']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΜέλοςΔιαγωνισμούΒ']['Βαθμός'])?>\cell\row

