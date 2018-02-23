<?
require_once('engine/init.php');
require_once('header.php');

$c = bills_with_fe($bills);
if (!$c) trigger_error('Δεν υπάρχουν τιμολόγια με ΦΕ', E_USER_ERROR);
$c = bills_by_provider($c);
foreach ($c as $v) {
	$b = calc_bills($v);
	$a = $v[0]['Προμηθευτής'];
?>

\sectd\lndscpsxn\pgwsxn16838\pghsxn11906\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

\pard\plain ΣΤΟΙΧΕΙΑ ΤΟΥ ΥΠΟΧΡΕΟΥ\par

\pard\plain\trowd\trpaddfl3\trpaddl28\trpaddfr3\trpaddr28
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\cellx6178
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx11227
\cellx15136
\qc <?=chk($data['Μονάδα'])?>\line\b Επωνυμία\b0\line\line
<?
echo chk($data['Πόλη']);
if (isset($data['Διεύθυνση'])) echo ', ' . chk($data['Διεύθυνση']);
if (isset($data['ΤΚ'])) echo ', ' . chk($data['ΤΚ']);
?>
\line\b Διεύθυνση (Πόλη - Οδός - Τ.Κ.)\b0\line\line 090153025\line\b Α.Φ.Μ.\b0\cell
Στρατιωτική Μονάδα\line\b Νομική Μορφή\b0\line\line
<?
if (isset($data['ΕξωτερικόΤηλέφωνο'])) echo 'Εξωτ.: ' . chk($data['ΕξωτερικόΤηλέφωνο']);
if (isset($data['ΕσωτερικόΤηλέφωνο'])) echo '  Εσωτ.: ' . chk($data['ΕσωτερικόΤηλέφωνο']);
?>
\line\b Αρ. Τηλεφώνου\b0\line\line ΔΟΥ Ν. Ψυχικού\line\b Δ.Ο.Υ.\b0\cell
\b ΒΕΒΑΙΩΣΗ\b0\line\line Παρακρατούμενου φόρου από προμήθειες κάθε είδους αγαθών ή παροχής υπηρεσιών από τις δημόσιες ΟΤΑ, ΝΠΔΔ κ.λ.π. (περιπτ. στ' παραγ. 1 άρθρου 37α του Ν.Δ. 3323/1955)\cell\row

\pard\plain\line I. ΣΤΟΙΧΕΙΑ ΕΠΙΧΕΙΡΗΣΗΣ\par
\trowd\trautofit1\trpaddfl3\trpaddl28\trpaddfr3\trpaddr28
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\cellx6178
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\cellx11227
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx15136
\qc <?=chk($a['Επωνυμία'])?>\line\b Επωνυμία\b0\line\line
<?
if (isset($a['Πόλη'])) echo chk($a['Πόλη']);
if (isset($a['Διεύθυνση']) && isset($a['Πόλη'])) echo ', ';
if (isset($a['Διεύθυνση'])) echo chk($a['Διεύθυνση']);
?>
\line\b Διεύθυνση (Πόλη - Οδός)\b0\cell
<? if (isset($a['ΤΚ'])) echo chk($a['ΤΚ']); ?>
\line\b T.K.\b0\line\line
<? if (isset($a['Τηλέφωνο'])) echo chk($a['Τηλέφωνο']); ?>
\line\b Τηλέφωνο\b0\cell
<?=chk($a['ΑΦΜ'])?>\line\b Α.Φ.Μ.\b0\line\line <?=chk($a['ΔΟΥ'])?>\line\b Δ.Ο.Υ.\b0\cell\row

\pard\plain\line IΙ. ΣΤΟΙΧΕΙΑ ΣΥΝΑΛΛΑΓΗΣ\par

\trowd\trpaddfl3\trpaddl28\trpaddfr3\trpaddr28
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\cellx2835
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\cellx5669
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\cellx8787
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx15136
\qc Είδος τιμολογίου\cell Αριθμός τιμολογίου\cell Καθαρό ποσό συναλλαγής\cell Ποσό φόρου που παρακρατήθηκε\cell\row
\trowd\trpaddfl3\trpaddl28\trpaddfr3\trpaddr28
\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\cellx2835
\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\cellx5669
\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\cellx8787
\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\cellx11961
\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx15136
<? foreach ($v as $i) { ?>
\qc <?=chk($i['Κατηγορία'])?>\cell <?=chk_bill($i['Τιμολόγιο'])?>\cell\qr <?=euro($i['ΚαθαρήΑξίαΜείονΚρατήσεις'])?>\cell\qc <?=percent($i['ΠοσοστόΦΕ'])?>\cell\qr <?=euro($i['ΦΕΣεΕυρώ'])?>\cell\row
<? } ?>
\trowd\trpaddfl3\trpaddl28\trpaddfr3\trpaddr28
\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\cellx5669
\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\cellx8787
\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx15136
\qr ΣΥΝΟΛΟ\cell \qr <?=euro($b['ΚαθαρήΑξίαΜείονΚρατήσεις'])?>\cell\qr <?=euro($b['ΦΕΣεΕυρώ'])?>\cell\row

\pard\plain\qr <?=chk($data['Πόλη']) . ', ' . now()?>\par


\pard\plain\fs23\trowd\trkeep\cellx7568\cellx15136\qc
ΕΘΕΩΡΗΘΗ\line - Ο -\line ΔΚΤΗΣ\line\line\line <?=chk($data['Δκτης']['Ονοματεπώνυμο'])?>\line <?=chk($data['Δκτης']['Βαθμός'])?>\cell
\line - Ο -\line ΑΞΚΟΣ ΕΡΓΟΥ\line\line\line <?=chk($data['ΑξκοςΈργου']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΑξκοςΈργου']['Βαθμός'])?>\cell\row

\sect

<? } ?>