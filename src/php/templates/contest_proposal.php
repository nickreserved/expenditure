<?
require_once('engine/init.php');
require_once('header.php');

if ($data['ΤύποςΔιαγωνισμού'] == 'Χωρίς Διαγωνισμό')
	trigger_error('Επιλέξατε τη δαπάνη «Χωρίς Διαγωνισμό»', E_USER_ERROR);
$a = $data['ΤύποςΔιαγωνισμού'] == 'Δημόσιος Διαγωνισμός';
?>

\sectd\pgwsxn11906\pghsxn16838\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

\pard\plain\b <?=chk(toUppercase($data['Μονάδα']))?>\par\par
\fs28\ul\qc ΕΙΣΗΓΗΤΙΚΗ ΕΚΘΕΣΗ\par\par
\pard\plain\tx567\tx1134\qj

<? $a = get_datetime($data['ΏραΔιαγωνισμού']);
$b = isset($data['ΏραΔιαγωνισμού'], $data['ΤόποςΔιαγωνισμού']); ?>
\tab 1. <?=$b ? "Σήμερα την {$a[1]} η" : 'Η'?> υπογεγραμμένη επιτροπή αποτελούμενη από τους:\par
\tab\tab α. <?=man_ext($data['ΠρόεδροςΔιαγωνισμού'], 2)?> ως πρόεδρο\par
\tab\tab β. <?=man_ext($data['ΜέλοςΔιαγωνισμούΑ'], 2)?> και\par
\tab\tab γ. <?=man_ext($data['ΜέλοςΔιαγωνισμούΒ'], 2)?> ως μέλη\par
που συγκροτήθηκε με την \ul <?=chk_order($data['ΔγηΔιαγωνισμού'])?>\ul0  προέβη στoν <?=$a ? 'Δημόσιο' : 'Πρόχειρο'?> Διαγωνισμό για «<?=chk($data['ΘέμαΔιαγωνισμού'])?>».\par\par
<?
if (isset($data['Νικητής']) || isset($draft)) { ?>
\tab 2. Η επιτροπή εισηγείται την κατακύρωση του Διαγωνισμού στο μειοδότη «<?=chk($data['Νικητής']['Επωνυμία'])?>» (ΑΦΜ: <?=chk($data['Νικητής']['ΑΦΜ'])?>, ΔΟΥ: <?=chk($data['Νικητής']['ΔΟΥ'])?>
<?
if (isset($data['Νικητής']['Τηλέφωνο'])) echo ', τηλέφωνο: ' . chk($data['Νικητής']['Τηλέφωνο']);
if (isset($data['Νικητής']['Τ.Κ.'])) echo ', Τ.Κ.: ' . chk($data['Νικητής']['Τ.Κ.']);
if (isset($data['Νικητής']['Πόλη'])) echo ', πόλη: ' . chk($data['Νικητής']['Πόλη']);
if (isset($data['Νικητής']['Διεύθυνση'])) echo ', διεύθυνση: ' . chk($data['Νικητής']['Διεύθυνση']);
?>).\par
<? } else { ?>
\tab 2. Η επιτροπή εισηγείται την επανάλυψη του Διαγωνισμού για τους παρακάτω λόγους:\par
\tab\tab α. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .\par
\tab\tab β. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .\par
\tab\tab γ. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .\par
<? } ?>

\pard\plain\fs23\par
\trowd\trkeep\trqc\trautofit1\trpaddfl3\trpaddl113\trpaddfr3\trpaddr113
\clftsWidth1\clNoWrap\cellx3402\clftsWidth1\clNoWrap\cellx6804\clftsWidth1\clNoWrap\cellx10206\qc
ΕΘΕΩΡΗΘΗ\line - Ο -\line ΔΚΤΗΣ\line\line\line <?=chk($data['Δκτης']['Ονοματεπώνυμο'])?>\line <?=chk($data['Δκτης']['Βαθμός'])?>\cell
\line - Ο -\line ΠΡΟΕΔΡΟΣ\line\line\line <?=chk($data['ΠρόεδροςΔιαγωνισμού']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΠρόεδροςΔιαγωνισμού']['Βαθμός'])?>\cell
\line - ΤΑ -\line ΜΕΛΗ\line\line\line <?=chk($data['ΜέλοςΔιαγωνισμούΑ']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΜέλοςΔιαγωνισμούΑ']['Βαθμός'])?>
\line\line\line <?=chk($data['ΜέλοςΔιαγωνισμούΒ']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΜέλοςΔιαγωνισμούΒ']['Βαθμός'])?>\cell\row


\sect

