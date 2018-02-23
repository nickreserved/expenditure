<?
require_once('engine/init.php');
require_once('header.php');

if (!strpos($data['ΤύποςΔαπάνης'], 'Εργοληπτική Επιχείρηση'))
	trigger_error('Ο Αξκός Έργου ορίζεται με ξεχωριστή Δγη μόνο για ανάθεση Έργου σε Εργολαβικές Επιχειρήσεις', E_USER_ERROR);
?>

\sectd\pgwsxn11906\pghsxn16838\marglsxn1984\margrsxn1134\margtsxn1134\margbsxn1134

<?
echo preOrder(!$draft || isset($data['ΔγηΑξκουΈργου']) ? $data['ΔγηΑξκουΈργου'] : null, array(man_ext($data['ΑξκοςΈργου'], 2)), array(null), $draft);
echo '\pard\plain\par\par\par';
echo subjectOrder('Κατασκευές Στρατιωτικών Έργων', array($data['ΔγηΔιάθεσης']));
?>
\pard\plain\sb57\sa57\fs24\tx567\tx1134\tx1701\tx2268\qj
\tab 1.\tab Έχοντας υπ' όψιν το σχετικό βάση του οποίου διατέθηκαν <?=euro2str($data['Ποσό'])?> (<?=euro($data['Ποσό'])?>) για «<?=chk($data['Τίτλος'])?>»\par
\qc{\b ο ρ ί ζ ω}\par\qj
τον <?=man_ext($data['ΑξκοςΈργου'], 2)?> ως υπεύθυνο Αξκό Έργου για την εκτέλεση του παραπάνω έργου και τη σύνταξη των δικαιολογητικών της δαπάνης σύμφωνα με τους ισχύοντες Νόμους και Διαταγές.\par\par
\tab 2.\tab Η δαπάνη υπόκειται στις νόμιμες κρατήσεις.\par\par
<? if (isset($data['ΗμερομηνίαΥποβολής'])) {?>\tab 3.\tab Φάκελος γενομένης δαπάνης μαζί μα τα υπόλοιπα δικαιολογητικά να υποβληθούν μέχρι <?=chk_date($data['ΗμερομηνίαΥποβολής'])?>.\par\par<? } ?>


<? if ($draft) draftOrder(); else bottomOrder(); ?>

\sect

