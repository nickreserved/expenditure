<?
require_once('engine/init.php');
require_once('header.php');

if (strpos($data['ΤύποςΔαπάνης'], 'Εργοληπτική Επιχείρηση'))
	trigger_error('Για ανάθεση Έργου σε Εργοληπτικές Επιχειρήσεις οι επιτροπές και ο Αξκος Έργου ορίζονται με ξεχωριστές Δγες', E_USER_ERROR);

$a = $data['ΤύποςΔιαγωνισμού'] == 'Δημόσιος Διαγωνισμός';
$b = $data['ΤύποςΔιαγωνισμού'] != 'Χωρίς Διαγωνισμό';
?>

\sectd\pgwsxn11906\pghsxn16838\marglsxn1984\margrsxn1134\margtsxn1134\margbsxn1134

<?
echo preOrder(!$draft || isset($data['ΔγηΑνάθεσης']) ? $data['ΔγηΑνάθεσης'] : null, array(man_ext($data['ΑξκοςΈργου'], 2), 'Μέλη Επιτροπών'), array(null), $draft);
echo '\pard\plain\par\par\par';
echo subjectOrder('Διάθεση Πίστωσης', array($data['ΔγηΔιάθεσης']));
?>
\pard\plain\fs24\tx567\tx1134\tx1701\tx2268\qj
\tab\b 1.\b0\tab Έχοντας υπ' όψιν το παραπάνω σχετικό βάση του οποίου διατέθηκαν στη Μονάδα <?=euro2str($data['Ποσό'])?> (<?=euro($data['Ποσό'])?>), για «<?=chk($data['Τίτλος'])?>»\par\par
\qc{\b σ α ς  ο ρ ί ζ ω}\par\qj\par
Υπεύθυνο Αξκό Έργου για την εκτέλεση της παραπάνω εργασίας και τη σύνταξη των δικαιολογητικών της δαπάνης σύμφωνα με τους ισχύοντες Νόμους και Διαταγές.\par\par
\tab\b 2.\b0\tab Συγκροτώ επιπρόσθετα τις παρακάτω επιτροπές:\par\par
<? $count = 0;
if ($b) { ?>
\tab\tab <?=countGreek(++$count)?>.\tab\ul Διενέργειας <?$a ? 'Δημόσιου' : 'Πρόχειρου'?> Διαγωνισμού\ul0  <?=$data['Τίτλος'] == $data['ΘέμαΔιαγωνισμού'] ? '' : 'για «' . chk($data['ΘέμαΔιαγωνισμού']) .'», '?>αποτελούμενη από τους:\par
\tab\tab\tab (1)\tab <?=man_ext($data['ΠρόεδροςΔιαγωνισμού'], 2)?> ως πρόεδρο\par
\tab\tab\tab (2)\tab <?=man_ext($data['ΜέλοςΔιαγωνισμούΑ'], 2)?> και\par
\tab\tab\tab (3)\tab <?=man_ext($data['ΜέλοςΔιαγωνισμούΒ'], 2)?> ως μέλη.\par\par
<? } else { ?>
\tab\tab <?=countGreek(++$count)?>.\tab\ul Aγοράς και διάθεσης\ul0  αποτελούμενη από τους:\par
\tab\tab\tab (1)\tab <?=man_ext($data['ΠρόεδροςΑγοράςΔιάθεσης'], 2)?> ως πρόεδρο\par
\tab\tab\tab (2)\tab <?=man_ext($data['ΜέλοςΑγοράςΔιάθεσηςΑ'], 2)?> και\par
\tab\tab\tab (3)\tab <?=man_ext($data['ΜέλοςΑγοράςΔιάθεσηςΒ'], 2)?> ως μέλη.\par\par
<? }
if ($a || !$b) { ?>
\tab\tab <?=countGreek(++$count)?>.\tab\ul Ποιοτικού ελέγχου και ποσοτικής παραλαβής\ul0  αποτελούμενη από τους:\par
\tab\tab\tab (1)\tab <?=man_ext($data['ΠρόεδροςΠοιοτικήςΠοσοτικήςΠαραλαβής'], 2)?> ως πρόεδρο\par
\tab\tab\tab (2)\tab <?=man_ext($data['ΜέλοςΠοιοτικήςΠοσοτικήςΠαραλαβήςΑ'], 2)?> και\par
\tab\tab\tab (3)\tab <?=man_ext($data['ΜέλοςΠοιοτικήςΠοσοτικήςΠαραλαβήςΒ'], 2)?> ως μέλη.\par\par
<? }
if ($data['ΤύποςΔαπάνης'] != 'Προμήθεια - Συντήρηση - Επισκευή') { ?>
\tab\tab <?=countGreek(++$count)?>.\tab\ul Αφανών εργασιών\ul0  αποτελούμενη από τους:\par
\tab\tab\tab (1)\tab <?=man_ext($data['ΠρόεδροςΑφανώνΕργασιών'], 2)?> ως πρόεδρο\par
\tab\tab\tab (2)\tab <?=man_ext($data['ΜέλοςΑφανώνΕργασιών'], 2)?> ως μέλος.\par\par
\tab\tab <?=countGreek(++$count)?>.\tab\ul Προσωρινής και οριστικής παραλαβής\ul0  αποτελούμενη από τους:\par
\tab\tab\tab (1)\tab <?=man_ext($data['ΠρόεδροςΠροσωρινήςΟριστικήςΠαραλαβής'], 2)?> ως πρόεδρο\par
\tab\tab\tab (2)\tab <?=man_ext($data['ΜέλοςΠροσωρινήςΟριστικήςΠαραλαβήςΑ'], 2)?> και\par
\tab\tab\tab (3)\tab <?=man_ext($data['ΜέλοςΠροσωρινήςΟριστικήςΠαραλαβήςΒ'], 2)?> ως μέλη.\par\par
'<? }
$count = 3;
if (isset($data['ΗμερομηνίαΥποβολής'])) {?>\tab\b <?=$count++?>.\b0\tab Φάκελος γενομένης δαπάνης μαζί μα τα υπόλοιπα δικαιολογητικά να υποβληθούν μέχρι <?=chk_date($data['ΗμερομηνίαΥποβολής'])?>.\par\par<? }
if ($b) {?>\tab\b <?=$count++?>.\b0\tab Ο διαγωνισμός θα γίνει <?=$a || isset($data['ΏραΔιαγωνισμού'], $data['ΤόποςΔιαγωνισμού']) ? 'τις ' . chk_datetime($data['ΏραΔιαγωνισμού']) . ', ' . chk($data['ΤόποςΔιαγωνισμού']) : 'με περιφορά'?>.\par\par<? } ?>

<? if ($draft) draftOrder(); else bottomOrder(); ?>

\sect

