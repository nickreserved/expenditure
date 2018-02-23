<?
require_once('engine/init.php');
require_once('header.php');

if (!strpos($data['ΤύποςΔαπάνης'], 'Εργοληπτική Επιχείρηση με Υλικά'))
	trigger_error('Η Επιτροπή Προσωρινής και Οριστικής Παραλαβής ορίζεται με ξεχωριστή Δγη μόνο για ανάθεση Έργου σε Εργολαβικές Επιχειρήσεις με υλικά Δημοσίου ή Εμπορίου', E_USER_ERROR);
?>

\sectd\pgwsxn11906\pghsxn16838\marglsxn1984\margrsxn1134\margtsxn1134\margbsxn1134

<?
echo preOrder(!$draft || isset($data['ΔγηΠοιοτικήςΠοσοτικήςΠαραλαβής']) ? $data['ΔγηΠοιοτικήςΠοσοτικήςΠαραλαβής'] : null, array('Μέλη Επιτροπής'), array(null), $draft);
echo '\pard\plain\par\par\par';
echo subjectOrder('Κατασκευές Στρατιωτικών Έργων', array($data['ΔγηΔιάθεσης'], $data['ΔγηΑξκουΈργου']));
?>
\pard\plain\sb57\sa57\fs24\tx567\tx1134\tx1701\tx2268\qj
\tab\b 1.\b0\tab Έχοντας υπ' όψιν το (α) σχετικό βάση του οποίου διατέθηκαν <?=euro2str($data['Ποσό'])?> (<?=euro($data['Ποσό'])?>) για «<?=chk($data['Τίτλος'])?>»\par
\qc{\b σ υ γ κ ρ ο τ ο ύ μ ε}\par\qj
την Επιτροπή Ποιοτικής και Ποσοτικής Παραλαβής όπως παρακάτω:\par
\tab\tab α.\tab <?=man_ext($data['ΠρόεδροςΠοιοτικήςΠοσοτικήςΠαραλαβής'], 2)?> \b ως πρόεδρο\b0\par
\tab\tab β.\tab <?=man_ext($data['ΜέλοςΠοιοτικήςΠοσοτικήςΠαραλαβήςΑ'], 2)?> \b και\b0\par
\tab\tab γ.\tab <?=man_ext($data['ΜέλοςΠοιοτικήςΠοσοτικήςΠαραλαβήςΒ'], 2)?> \b ως μέλη\b0 .\par\par
\tab\b 2.\b0\tab Σύγκληση της επιτροπής με μέριμνα του προέδρου της.\par\par

<? if ($draft) draftOrder(); else bottomOrder(); ?>

\sect

