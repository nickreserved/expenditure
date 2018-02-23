<?
require_once('engine/functions.php');
require_once('header.php');

if (!isset($bills_info)) $bills_info = calc_bills($data['Τιμολόγια']);
if (!isset($data['Ποσό'])) $data['Ποσό'] = $bills_info['Καταλογιστέο'];
$bills_info_hold = $bills_info['ΑνάλυσηΚρατήσεωνΣεΕυρώ'];
?>

{

\sectd\pgwsxn11906\pghsxn16838\marglsxn850\margrsxn850\margtsxn2835\margbsxn1134
\pard\plain\qc\fs32\b\i\ul <?=chk($data['ΣύντμησηΜονάδας'])?>\line\line\line\fs36 Φάκελος Γενομένης Δαπάνης\line\line\line\line\par
\pard\plain\box\brdrs\brdrw1\brsp28 {\b ΕΡΓΟ:} «<?=chk($data['Τίτλος'])?>»\par
\pard\par
\pard\plain\box\brdrs\brdrw1\brsp28 {\b ΠΟΣΟ:} <?=euro($data['Ποσό'])?>\par
\pard\par
\pard\plain\box\brdrs\brdrw1\brsp28 {\b ΑΞΙΩΜΑΤΙΚΟΣ ΕΡΓΟΥ:} <?=man($data['ΑξκοςΈργου'])?>\par
\pard\par
\pard\plain\box\brdrs\brdrw1\brsp28 {\b ΕΓΚΡΙΤΙΚΗ ΔΙΑΤΑΓΗ:} <?=chk(chk_order($data['ΔγηΔιάθεσης']))?>\par
\pard\par
\pard\plain\box\brdrs\brdrw1\brsp28 {\b ΕΦ:} <?=chk($data['ΕΦ'])?>\par
\pard\par
\pard\plain\box\brdrs\brdrw1\brsp28 {\b ΚΑ:} <? if (isset($data['ΚΑ'])) echo $data['ΚΑ']; ?>\par
\pard\line\line\line\line\line\line\par


\pard\tx1\tqdec\tx4457

\trowd
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx5103
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx10206
\qc{\b ΑΝΑΛΥΣΗ ΚΡΑΤΗΣΕΩΝ\cell ΚΑΤΑΛΟΓΙΣΤΕΟ ΠΟΣΟ}\cell\row
\ql ΜΤΣ:\tab <? if (isset($bills_info_hold['ΜΤΣ'])) echo euro($bills_info_hold['ΜΤΣ']); ?>\cell\qc <?=euro($bills_info['Καταλογιστέο'])?>\cell\row
\ql ΕΜΠ:\tab <? if (isset($bills_info_hold['ΕΜΠ'])) echo euro($bills_info_hold['ΕΜΠ']); ?>\cell\qc{\b ΠΛΗΡΩΤΕΟ ΠΟΣΟ}\cell\row
\ql ΤΣΜΕΔΕ:\tab <? if (isset($bills_info_hold['ΤΣΜΕΔΕ'])) echo euro($bills_info_hold['ΤΣΜΕΔΕ']); ?>\cell\qc <?=euro($bills_info['Πληρωτέο'])?>\cell\row
\trowd
\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx5103
\cellx10206
\ql ΤΠΕΔΕ:\tab <? if (isset($bills_info_hold['ΤΠΕΔΕ'])) echo euro($bills_info_hold['ΤΠΕΔΕ']); ?>\cell\cell\row
ΥΠΚ:\tab <? if (isset($bills_info_hold['ΥΠΚ'])) echo euro($bills_info_hold['ΥΠΚ']); ?>\cell\cell\row
ΤΑΣ:\tab <? if (isset($bills_info_hold['ΤΑΣ'])) echo euro($bills_info_hold['ΤΑΣ']); ?>\cell\cell\row
ΑΟΟΑ:\tab <? if (isset($bills_info_hold['ΑΟΟΑ'])) echo euro($bills_info_hold['ΑΟΟΑ']); ?>\cell\cell\row
Χαρτόσημο:\tab <? if (isset($bills_info_hold['Χαρτόσημο'])) echo euro($bills_info_hold['Χαρτόσημο']); ?>\cell\cell\row
Χαρτόσημο ΟΓΑ:\tab <? if (isset($bills_info_hold['ΟΓΑ'])) echo euro($bills_info_hold['ΟΓΑ']); ?>\cell\cell\row
ΕΚΟΕΜΣ:\tab <? if (isset($bills_info_hold['ΕΚΟΕΜΣ'])) echo euro($bills_info_hold['ΕΚΟΕΜΣ']); ?>\cell\cell\row
{\b ΣΥΝΟΛΟ ΚΡΑΤΗΣΕΩΝ:\tab <? if (isset($bills_info_hold['Σύνολο'])) echo euro($bills_info_hold['Σύνολο'], true); ?>}\cell\cell\row

\sect

}