<?
require_once('engine/init.php');
require_once('header.php');

if ($data['ΤύποςΔιαγωνισμού'] == 'Χωρίς Διαγωνισμό')
	trigger_error('Επιλέξατε τη δαπάνη «Χωρίς Διαγωνισμό»', E_USER_ERROR);
$a = $data['ΤύποςΔιαγωνισμού'] == 'Δημόσιος Διαγωνισμός';

if (!strpos($data['ΤύποςΔαπάνης'], 'Εργοληπτική Επιχείρηση'))
	trigger_error('Η Επιτροπή Διενέργειας Διαγωνισμού ορίζεται με ξεχωριστή Δγη μόνο για ανάθεση Έργου σε Εργολαβικές Επιχειρήσεις', E_USER_ERROR);
?>

\sectd\pgwsxn11906\pghsxn16838\marglsxn1984\margrsxn1134\margtsxn1134\margbsxn1134

<?
echo preOrder(!$draft || isset($data['ΔγηΔιαγωνισμού']) ? $data['ΔγηΔιαγωνισμού'] : null, array('Μέλη Επιτροπής'), array(null), $draft);
echo '\pard\plain\par\par\par';
echo subjectOrder('Επιτροπές Διαγωνισμών', array($data['ΔγηΔιάθεσης']));
?>
\pard\plain\sb57\sa57\fs24\tx567\tx1134\tx1701\tx2268\qj
\tab\b 1.\b0\tab Έχοντας υπ' όψιν το (α) σχετικό βάση του οποίου διατέθηκαν <?=euro2str($data['Ποσό'])?> (<?=euro($data['Ποσό'])?>) για «<?=chk($data['Τίτλος'])?>»\par
\qc{\b σ υ γ κ ρ ο τ ο ύ μ ε}\par\qj
την Επιτροπή διενέργειας <?$a ? 'Δημόσιου' : 'Πρόχειρου'?> Διαγωνισμού <?=$data['Τίτλος'] == $data['ΘέμαΔιαγωνισμού'] ? '' : 'για «' . chk($data['ΘέμαΔιαγωνισμού']) .'», '?> όπως παρακάτω:\par
\tab\tab α.\tab <?=man_ext($data['ΠρόεδροςΔιαγωνισμού'], 2)?> \b ως πρόεδρο\b0\par
\tab\tab β.\tab <?=man_ext($data['ΜέλοςΔιαγωνισμούΑ'], 2)?> \b και\b0\par
\tab\tab γ.\tab <?=man_ext($data['ΜέλοςΔιαγωνισμούΒ'], 2)?> \b ως μέλη\b0 .\par\par
\tab\b 2.\b0\tab Σύγκληση της επιτροπής με μέριμνα του προέδρου της.\par\par
\tab\b 3.\b0\tab Ο διαγωνισμός θα γίνει <?=$a || isset($data['ΏραΔιαγωνισμού'], $data['ΤόποςΔιαγωνισμού']) ? 'τις ' . chk_datetime($data['ΏραΔιαγωνισμού']) . ', ' . chk($data['ΤόποςΔιαγωνισμού']) : 'με περιφορά'?>.\par\par

<? if ($draft) draftOrder(); else bottomOrder(); ?>

\sect

