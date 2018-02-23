<?
require_once('engine/init.php');
require_once('header.php');

$a = $bills_info['ΑνάλυσηΚρατήσεωνΣεΕυρώ'];
?>

\sectd\pgwsxn11906\pghsxn16838\marglsxn850\margrsxn850\margtsxn2835\margbsxn1134
\pard\plain\qc\fs32\b\i\ul <?=chk($data['ΣύντμησηΜονάδας'])?>\line\line\line\fs36 Φάκελος Γενομένης Δαπάνης\line\line\line\line\par
\pard\plain\box\brdrs\brdrw1\brsp28 {\b ΕΡΓΟ:} «<?=chk($data['Τίτλος'])?>»\par
\pard\par
\pard\plain\box\brdrs\brdrw1\brsp28 {\b ΠΟΣΟ:} <?=euro($data['Ποσό'])?>\par
\pard\par
\pard\plain\box\brdrs\brdrw1\brsp28 {\b ΑΞΙΩΜΑΤΙΚΟΣ ΕΡΓΟΥ:} <?=man_ext($data['ΑξκοςΈργου'], 0)?>\par
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
\ql ΜΤΣ:\tab <? if (isset($a['ΜΤΣ'])) echo euro($a['ΜΤΣ']); ?>\cell\qc <?=euro($bills_info['Καταλογιστέο'])?>\cell\row
\ql ΕΜΠ:\tab <? if (isset($a['ΕΜΠ'])) echo euro($a['ΕΜΠ']); ?>\cell\qc{\b ΠΛΗΡΩΤΕΟ ΠΟΣΟ}\cell\row
\ql ΤΣΜΕΔΕ:\tab <? if (isset($a['ΤΣΜΕΔΕ'])) echo euro($a['ΤΣΜΕΔΕ']); ?>\cell\qc <?=euro($bills_info['Πληρωτέο'])?>\cell\row
\trowd
\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx5103
\cellx10206
\ql ΤΠΕΔΕ:\tab <? if (isset($a['ΤΠΕΔΕ'])) echo euro($a['ΤΠΕΔΕ']); ?>\cell\cell\row
ΑΟΟΑ:\tab <? if (isset($a['ΑΟΟΑ'])) echo euro($a['ΑΟΟΑ']); ?>\cell\cell\row
Χαρτόσημο:\tab <? if (isset($a['Χαρτόσημο'])) echo euro($a['Χαρτόσημο']); ?>\cell\cell\row
Χαρτόσημο ΟΓΑ:\tab <? if (isset($a['ΟΓΑ'])) echo euro($a['ΟΓΑ']); ?>\cell\cell\row
ΕΚΟΕΜΣ:\tab <? if (isset($a['ΕΚΟΕΜΣ'])) echo euro($a['ΕΚΟΕΜΣ']); ?>\cell\cell\row
{\b ΣΥΝΟΛΟ ΚΡΑΤΗΣΕΩΝ:\tab <? if (isset($a['Σύνολο'])) echo euro($a['Σύνολο'], true); ?>}\cell\cell\row

\sect

