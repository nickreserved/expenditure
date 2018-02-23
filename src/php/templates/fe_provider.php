<?
require_once('engine/functions.php');
require_once('header.php');

if (!isset($bills_provider)) $bills_provider = bills_by_provider($data['Τιμολόγια']);

foreach ($bills_provider as $list) {
	$list_info = calc_bills($list);
	$provider = $list[0]['Προμηθευτής'];
?>

{

\sectd\lndscpsxn\pgwsxn16838\pghsxn11906\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

\pard\plain\li11339 <?=chk(toUppercase($data['Μονάδα']))?>\line <?=chk(toUppercase($data['Γραφείο']))?>\par
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
\qc <?=chk($provider['Επωνυμία'])?>\line\b Επωνυμία\b0\line\line
<?
if (isset($provider['Πόλη'])) echo chk($provider['Πόλη']);
if (isset($provider['Διεύθυνση']) && isset($provider['Πόλη'])) echo ', ';
if (isset($provider['Διεύθυνση'])) echo chk($provider['Διεύθυνση']);
?>
\line\b Διεύθυνση (Πόλη - Οδός)\b0\cell
<? if (isset($provider['ΤΚ'])) echo chk($provider['ΤΚ']); ?>
\line\b T.K.\b0\line\line
<? if (isset($provider['Τηλέφωνο'])) echo chk($provider['Τηλέφωνο']); ?>
\line\b Τηλέφωνο\b0\cell
<?=chk($provider['ΑΦΜ'])?>\line\b Α.Φ.Μ.\b0\line\line <?=chk($provider['ΔΟΥ'])?>\line\b Δ.Ο.Υ.\b0\cell\row

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
<? foreach ($list as $v) { ?>
\qc <?=chk($v['Κατηγορία'])?>\cell <?=chk($v['Τιμολόγιο'])?>\cell\qr <?=euro($v['ΚαθαρήΑξίαΜείονΚρατήσεις'])?>\cell\qc <?=percent($v['ΠοσοστόΦΕ'])?>\cell\qr <?=euro($v['ΦΕΣεΕυρώ'])?>\cell\row
<? } ?>
\trowd\trpaddfl3\trpaddl28\trpaddfr3\trpaddr28
\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\cellx5669
\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\cellx8787
\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx15136
\qr ΣΥΝΟΛΟ\cell \qr <?=euro($list_info['ΚαθαρήΑξίαΜείονΚρατήσεις'])?>\cell\qr <?=euro($list_info['ΦΕΣεΕυρώ'])?>\cell\row

\pard\plain\qr <?=chk($data['Πόλη']) . ', ' . now()?>\par


\pard\plain\fs23\trowd\trkeep\cellx7568\cellx15136\qc
ΕΘΕΩΡΗΘΗ\line - Ο -\line ΔΚΤΗΣ\line\line\line <?=chk($data['Δκτης']['Ονοματεπώνυμο'])?>\line <?=chk($data['Δκτης']['Βαθμός'])?>\cell
\line - Ο -\line ΑΞΚΟΣ ΕΡΓΟΥ\line\line\line <?=chk($data['ΑξκοςΈργου']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΑξκοςΈργου']['Βαθμός'])?>\cell\row

\sect

}

<? } ?>
