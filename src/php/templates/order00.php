<?
require_once('engine/functions.php');
require_once('header.php');
require_once('engine/order.php');

if (!isset($bills_info)) $bills_info = calc_bills($data['Τιμολόγια']);
if (!isset($data['Ποσό'])) $data['Ποσό'] = $bills_info['Καταλογιστέο'];

if (!isset($draft)) $draft = getEnvironment('draft', 'true');
?>

{

\sectd\pgwsxn11906\pghsxn16838\marglsxn1984\margrsxn1134\margtsxn1134\margbsxn1134

<?
echo preOrder(isset($data['ΔγηΑνάθεσης']) ? $data['ΔγηΑνάθεσης'] : null, array('Αξκο Έργου', 'Επιτροπές'), array(null), $draft);
echo '\pard\plain\par\par\par';
echo subjectOrder('Διάθεση Πίστωσης', array($data['ΔγηΔιάθεσης']));
?>
\pard\plain\fs28\tx567\tx1134\tx1701\tx2268\qj
\tab\b 1.\b0\tab Έχοντας υπ' οψην την παραπάνω σχετική, που σας επισυνάπτουμε σε πρωτότυπο και βάση της οποίας διατέθηκαν στη Μονάδα <?=euro2str($data['Ποσό'])?> (<?=euro($data['Ποσό'])?>), για «<?=chk($data['Τίτλος'])?>»\par\par
\qc{\b σ α ς  ο ρ ί ζ ω}\par\qj\par
Υπεύθυνο Αξκό Έργου για την εκτέλεση της παραπάνω εργασίας και τη σύνταξη των δικαιολογητικών της δαπάνης σύμφωνα με τους ισχύοντες Νόμους και Διαταγές καθώς και την ΠαΔ 8-2/2002/Δ'ΣΣ/4ο ΕΓ.\par\par
\tab\b 2.\b0\tab Συγκροτώ επιπρόσθετα τις παρακάτω επιτροπές:\par\par
\tab\tab α.\tab\ul Aγοράς και διάθεσης\ul0  αποτελούμενη από τους:\par
\tab\tab\tab (1)\tab <?=inflection(man($data['ΠρόεδροςΑγοράςΔιάθεσης']), 2)?> ως πρόεδρο\par
\tab\tab\tab (2)\tab <?=inflection(man($data['ΜέλοςΑγοράςΔιάθεσηςΑ']), 2)?> και\par
\tab\tab\tab (3)\tab <?=inflection(man($data['ΜέλοςΑγοράςΔιάθεσηςΒ']), 2)?> ως μέλη\par\par
\tab\tab β.\tab\ul Ποιοτικού ελέγχου και ποσοτικής παραλαβής\ul0  αποτελούμενη από τους:\par
\tab\tab\tab (1)\tab <?=inflection(man($data['ΠρόεδροςΠοιοτικήςΠοσοτικήςΠαραλαβής']), 2)?> ως πρόεδρο\par
\tab\tab\tab (2)\tab <?=inflection(man($data['ΜέλοςΠοιοτικήςΠοσοτικήςΠαραλαβήςΑ']), 2)?> και\par
\tab\tab\tab (3)\tab <?=inflection(man($data['ΜέλοςΠοιοτικήςΠοσοτικήςΠαραλαβήςΒ']), 2)?> ως μέλη\par
<? if (isset($data['ΠρόεδροςΖυγίσεωςΥλικώνΕμπορίου'], $data['ΜέλοςΖυγίσεωςΥλικώνΕμπορίου'])) { ?>
\tab\tab γ.\tab\ul Ζυγίσεως Εμπορίου\ul0  αποτελούμενη από τους:\par
\tab\tab\tab (1)\tab <?=inflection(man($data['ΠρόεδροςΖυγίσεωςΥλικώνΕμπορίου']), 2)?> ως πρόεδρο\par
\tab\tab\tab (2)\tab <?=inflection(man($data['ΜέλοςΖυγίσεωςΥλικώνΕμπορίου']), 2)?> ως μέλος\par
<? } ?>
\par
\tab\b 3.\b0\tab Απολογισμός γενομένης δαπάνης μαζί μα τα υπόλοιπα δικαιολογητικά να υποβληθούν μέχρι <?=chk_date($data['ΗμερομηνίαΥποβολής'])?>.\par\par
\tab\b 4.\b0\tab Η δαπάνη υπόκειται στις νόμιμες κρατήσεις.\par\par


<? if ($draft) draftOrder(); else bottomOrder(); ?>

\sect

}

