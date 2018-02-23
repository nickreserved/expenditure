<?
require_once('engine/init.php');
require_once('header.php');

$a = $bills_info['ÁíÜëõóçÊñáôŞóåùíÓåÅõñş'];
?>

\sectd\pgwsxn11906\pghsxn16838\marglsxn850\margrsxn850\margtsxn2835\margbsxn1134
\pard\plain\qc\fs32\b\i\ul <?=chk($data['ÓıíôìçóçÌïíÜäáò'])?>\line\line\line\fs36 ÖÜêåëïò Ãåíïìİíçò ÄáğÜíçò\line\line\line\line\par
\pard\plain\box\brdrs\brdrw1\brsp28 {\b ÅÑÃÏ:} «<?=chk(ucwords($data['Ôßôëïò']))?>»\par
\pard\par
\pard\plain\box\brdrs\brdrw1\brsp28 {\b ĞÏÓÏ:} <?=euro($data['Ğïóü'])?>\par
\pard\par
\pard\plain\box\brdrs\brdrw1\brsp28 {\b ÁÎÉÙÌÁÔÉÊÏÓ ÅÑÃÏÕ:} <?=man_ext($data['Áîêïò¸ñãïõ'], 0)?>\par
\pard\par
\pard\plain\box\brdrs\brdrw1\brsp28 {\b ÅÃÊÑÉÔÉÊÇ ÄÉÁÔÁÃÇ:} <?=chk(chk_order($data['ÄãçÄéÜèåóçò']))?>\par
\pard\par
\pard\plain\box\brdrs\brdrw1\brsp28 {\b ÅÖ:} <?=chk($data['ÅÖ'])?>\par
\pard\par
\pard\plain\box\brdrs\brdrw1\brsp28 {\b ÊÁ:} <? if (isset($data['ÊÁ'])) echo $data['ÊÁ']; ?>\par
\pard\line\line\line\line\line\line\par


\pard\plain\li6236\box\brdrs\brdrw1\brsp170\tqdec\tx9496

\b ÊÁÔÁËÏÃÉÓÔÅÏ\tab <?=euro($bills_info['Êáôáëïãéóôİï'])?>\par
ĞËÇÑÙÔÅÏ\tab <?=euro($bills_info['Ğëçñùôİï'])?>\par\par
ÊÑÁÔÇÓÅÉÓ\tab <?=euro($a['Óıíïëï'], true)?>\par\b0\tx6803\tqdec\tx9496
<?
	foreach($a as $k => $v)
		if ($k != 'Óıíïëï')
			echo "\\tab $k\\tab " . euro($v) . '\par';
?>

\sect

