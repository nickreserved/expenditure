<?
require_once('engine/init.php');
require_once('header.php');

if ($data['ΤύποςΔιαγωνισμού'] != 'Δημόσιος Διαγωνισμός')
	trigger_error('Δεν επιλέξατε <b>Δημόσιο Διαγωνισμό</b>', E_USER_ERROR);
?>

\sectd\pgwsxn11906\pghsxn16838\marglsxn1984\margrsxn1134\margtsxn1134\margbsxn1134

<?
echo preOrder(!$draft || isset($data['ΔγηΔιακήρυξης']) ? $data['ΔγηΔιακήρυξης'] : null, getNewspapers($data['Εφημερίδες']), array(null), $draft, 'Ένα (-1-)');
echo '\pard\plain\par\par\par';
echo subjectOrder('Διακήρυξη Διαγωνισμού', array($data['ΔγηΔιάθεσης']));
?>
\pard\plain\sb57\sa57\fs24\tx567\tx1134\tx1701\tx2268\qj
\tab 1.\tab Παρακαλούμε όπως δημοσιεύσετε στην εφημερίδα σας την συνημμένη διακήρυξη.\par
\tab 2.\tab Η δημοσίευση να λάβει χώρα τις ημερομηνίες <?=getDates($data['ΗμερομηνίεςΔημοσίευσης'])?>.\par
\tab 3.\tab Οι τίτλοι της δημοσίευσης πρέπει να καταλαμβάνουν χώρο όχι ανώτερο των δέκα (-10-) χιλιοστών, οι υπότιτλοι όχι ανώτερο των επτά (-7-) χιλιοστών.\par
\tab 4.\tab Ο τίτλος της Μονάδας στο τέλος της διακήρυξης δε θα υπερβαίνει τον ένα (-1-) στίχο και τρία (-3-) χιλιοστά.\par

<? if ($draft) draftOrder(); else bottomOrder(); ?>

\sect


\pard\plain\fs32\qc\b\ul
ΔΙΑΚΗΡΥΞΗ ΔΙΑΓΩΝΙΣΜΟΥ\par\par
\pard\plain\fs24\qj
ΓΕΝΙΚΟ ΕΠΙΤΕΛΕΙΟ ΣΤΡΑΤΟΥ\par
<?=toUppercase($data['Μονάδα'])?>\par\par
<? $a = get_datetime($data['ΏραΔιαγωνισμού']); ?>
Ανακοινώνεται οτι τις <?=chk($a[1])?> και ώρα <?=chk($a[0])?> θα διενεργηθεί Δημόσιος Διαγωνισμός με ενσφράγιστες προσφορές, <?=chk($data['ΤόποςΔιαγωνισμού'])?>, για <?=chk($data['ΘέμαΔιαγωνισμού'])?>.\par\par
Πληροφορίες καθημερινά στο τηλέφωνο <?=chk($data['ΕξωτερικόΤηλέφωνο'])?> (Εσωτερικό: <?=chk($data['ΕσωτερικόΤηλέφωνο'])?>).\par

\sect

