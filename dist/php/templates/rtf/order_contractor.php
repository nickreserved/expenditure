<?
require_once('engine/init.php');
require_once('header.php');

if (!strpos($data['ΤύποςΔαπάνης'], 'Εργοληπτική Επιχείρηση'))
	trigger_error('Ο Εργολάβος ορίζεται μόνο για ανάθεση Έργου σε Εργολαβικές Επιχειρήσεις', E_USER_ERROR);
?>

\sectd\pgwsxn11906\pghsxn16838\marglsxn1984\margrsxn1134\margtsxn1134\margbsxn1134

<?
echo preOrder(!$draft || isset($data['ΔγηΑνάθεσηςΕργολαβίας']) ? $data['ΔγηΑνάθεσηςΕργολαβίας'] : null, array(null), array(null), $draft);
echo '\pard\plain\par\par\par';
echo subjectOrder('Κατασκευές Στρατιωτικών Έργων', array($data['ΔγηΔιάθεσης']));
?>
\pard\plain\sb57\sa57\fs24\tx567\tx1134\tx1701\tx2268\qj
\tab\b 1.\b0\tab Έχοντας υπ' όψιν το σχετικό βάση του οποίου διατέθηκαν <?=euro2str($data['Ποσό'])?> (<?=euro($data['Ποσό'])?>) για «<?=chk($data['Τίτλος'])?>»\par
\qc{\b α ν α θ έ τ ο υ μ ε}\par\qj
την κατασκευή του παραπάνω έργου στην Εργοληπτική Επιχείρηση «<?=chk($data['Εργολάβος']['Επωνυμία'])?>» (ΑΦΜ: <?=chk($data['Εργολάβος']['ΑΦΜ'])?>).\par\par

<? if ($draft) draftOrder(); else bottomOrder(); ?>

\sect

