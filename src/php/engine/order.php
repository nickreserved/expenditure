<?

require_once('functions.php');

if (!isset($draft)) $draft = getEnvironment('draft', 'true');


function preOrder($order, $to, $info, $draft = false, $add = null) {
	global $data;
?>
\pard\plain\fs24\tx1134
\trowd\trautofit1\trpaddl0\trpaddr0\cellx5103\clftsWidth1\clNoWrap\cellx8788
\b ΠΡΟΣ:\b0<? foreach($to as $v) echo '\tab ' . chk($v) . '\line'; ?>
\line\b ΚΟΙΝ:\b0
<?
	if (chk_order($order, !$draft)) $ord = get_order($order);
	if (is_array($info))
		foreach($info as $v) {
			if (!$v && isset($ord)) $v = $ord['Εκδότης'];
			if ($v) echo '\tab ' . chk($v) . '\line';
		}
	?>\cell <?=chk(toUppercase($data['Μονάδα']))?>\line <?=chk(toUppercase($data['Γραφείο']))?>\line Τηλ (Εσωτ.) <?=chk($data['ΕσωτερικόΤηλέφωνο'])?>\line <?
	if (!$draft)
		echo chk($ord['Φάκελος']) . '/' . chk($ord['Υποφάκελος']) . '/' . chk($ord['Πρωτόκολλο']) . '\line ' . chk($ord['Σχέδιο']) . '\line ' . chk($data['Πόλη']) . ', ' . chk($ord['Ημερομηνία']);
	else {
		?>Φ.\line Σ.\line <?
		echo chk($data['Πόλη']);
	}
	if ($add) echo '\line Συνημμένα: ' . $add;
	?>\cell\row
\pard\plain\par<?
}


function subjectOrder($subject, $orders) { ?>
\pard\plain\fs24\tx1134\tx1644
\b ΘΕΜΑ:\b0\tab\ul <?=chk($subject)?>\ul0\par\par
<?
	if ($orders) {
		echo '\bΣΧΕΤ:\b0';
		$a = count($orders);
		if ($a > 1) {
			for($z = 0; $z < $a - 1; $z++)
				echo '\tab ' . countGreek($z + 1) . '.\tab ' . chk($orders[$z]) . '\par';
			echo '\tab ' . countGreek($a) . '.';
		}
		echo '\tab\ul ' . chk($orders[$a - 1]) . '\par';
	}
	echo '\par';
}


function draftOrder() {
	global $data;
?>
\pard\plain\fs24\par
\trowd\trkeep\trqc\trautofit1\trpaddfl3\trpaddl113\trpaddfr3\trpaddr113
\clftsWidth1\clNoWrap\cellx2929\clftsWidth1\clNoWrap\cellx5858\clftsWidth1\clNoWrap\cellx8788\qc
- Το -\line\ul <?=chk($data['Γραφείο'])?>\ul0\line\line\line <?=chk($data['ΑξκοςΓραφείου']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΑξκοςΓραφείου']['Βαθμός'])?>\cell
- Ο -\line\ul Υδκτης\ul0\line\line\line <?=chk($data['ΕΟΥ']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΕΟΥ']['Βαθμός'])?>\cell
- Ο -\line\ul Δκτης\ul0\line\line\line <?=chk($data['Δκτης']['Ονοματεπώνυμο'])?>\line <?=chk($data['Δκτης']['Βαθμός'])?>\cell\row
<?
}


function bottomOrder() {
	global $data;
?>
\pard\plain\fs24\par
\trowd\trkeep\trqc\trautofit1\trpaddfl3\trpaddl113\trpaddfr3\trpaddr113
\clftsWidth1\clNoWrap\cellx4394\clftsWidth1\clNoWrap\cellx8788\qc
Ακριβές Αντίγραφο\line\line\line\line <?=man($data['ΑξκοςΓραφείου'])?>\line <?=chk($data['ΙδιότηταΑξκου'])?>\cell
<?=man($data['Δκτης'])?>\line Διοικητής\cell\row
<?
}

?>