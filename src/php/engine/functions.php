<?

// Υπολογίζει κάποια πράγματα για το σύνολο ορισμένων τιμολογίων που βρίσκονται
// στο array $a
function calc_bills($a) {
	$b = array();
	foreach($a as $v) {
		calc_bills_sum($b, $v, 'Καταλογιστέο');
		calc_bills_sum($b, $v, 'Πληρωτέο');
		calc_bills_sum($b, $v, 'ΚαθαρήΑξία');
		calc_bills_sum($b, $v, 'ΚαθαρήΑξίαΓιαΦΕ');
		calc_bills_sum($b, $v, 'ΦΕΣεΕυρώ');
		calc_bills_sum($b, $v, 'ΥπόλοιποΠληρωτέο');
		calc_bills_sum_arr($b, $v, 'ΚατηγορίεςΦΠΑ');
		calc_bills_sum_arr($b, $v, 'ΑνάλυσηΚρατήσεωνΣεΕυρώ');
	}
	return $b;
}

function calc_bills_sum_arr(& $b, $a, $i) {
	if (!isset($b[$i])) $b[$i] = array();
	foreach($a[$i] as $k => $v)
		if (!isset($b[$i][$k])) $b[$i][$k] = $v; else $b[$i][$k] += $v;
}

// αν π.χ. δεν υπάρχει το $b['Καταλογιστέο'] δημιούργησέ το από το Τιμολόγιο $a
// αν υπάρχει τότε πρόσθεσε από το Τιμολόγιο $a
function calc_bills_sum(& $b, $a, $i) { if (!isset($b[$i])) $b[$i] = $a[$i]; else $b[$i] += $a[$i]; }


// Από το array $a επιστρέφει τα τιμολόγια που είναι ($f == true)
// ή δεν είναι ($f == false) κατηγορίας $i.
function getBillsCategory($a, $i, $f = true) {
	$b = array();
	foreach($a as $v)
		if (($v['Κατηγορία'] == $i) == $f) $b[] = $v;
	return $b;
}

// Από το array $a επιστρέφει τα τιμολόγια που είναι τύπου $i.
function getBillsType($a, $i) {
	$b = array();
	foreach($a as $v)
		if (strpos($i, "*{$v['Προμηθευτής']['Τύπος']}*") !== false) $b[] = $v;
	return $b;
}


// Επιστρέφει ένα array με τιμολόγια που έχουν ΦΕ.
function bills_with_fe($a) {
	$b = array();
	foreach($a as $v)
		if ($v['ΠοσοστόΦΕ']) $b[] = $v;
	return $b;
}

// Επιστρέφει ένα array με keys τις κρατήσεις σε ποσοστό και
// values array με όλα τα τιμολόγια που έχουν τέτοια κράτηση.
// Αν $zero == false δεν επιστρέφει τα τιμολόγια που έχουν
// κρατήσεις 0 (σπάνιο να έχουν 0 αλλά όχι απίθανο)
function bills_by_hold($a, $zero = false) {
	$b = array();
	foreach($a as $v) {
		$n = $v['ΑνάλυσηΚρατήσεωνΣεΠοσοστά']['Σύνολο'];
		if ($n || $zero) $b[str_replace(',', '.', $n)][] = $v;
	}
	return $b;
}

// Επιστρέφει ενα array με keys το ΦΕ σε ποσοστό και
// values array με όλα τα τιμολόγια που έχουν τέτοιο ΦΕ.
// Δεν επιστρέφει τιμολόγια δίχως ΦΕ.
function bills_by_fe($a) {
	$b = array();
	foreach($a as $v) {
		$n = $v['ΠοσοστόΦΕ'];
		if ($n) $b[str_replace(',', '.', $n)][] = $v;
	}
	return $b;
}

// Επιστρέφει ένα array με keys τον προμηθευτή και
// values array με όλα τα τιμολόγια από αυτόν.
function bills_by_provider($a) {
	$b = array();
	foreach($a as $v)
		$b[$v['Προμηθευτής']['Επωνυμία']][] = $v;
	return $b;
}

// Επιστρέφει ενα array με keys τον "μήνα έτος" π.χ. "Νοε 2005" και
// values array με όλα τα τιμολόγια του μηνός.
function bills_by_month($a) {
	$b = array();
	foreach($a as $v)
		if (preg_match('/\d{1,2}-(\d{1,2})-(\d{4})/', $v['Τιμολόγιο'], $m)) {
			$b[mktime(0, 0, 0, $m[1], 1, $m[2])][] = $v;
		} else chk_bill($v['Τιμολόγιο']);
	return $b;
}


// Έλεγχος του $a για χαρακτήρες ελέγχου στο RTF Spec και αντικατάστασή τους
// αν υπάρχουν με backslash και το χαρακτήρα
function chk($a) { return preg_replace('/([\\\{\}])/', '$0$0', $a); }

// Έλεγχος του $a για χαρακτήρες ελέγχου στο RTF Spec και αντικατάστασή τους
// αν υπάρχουν με backslash και το χαρακτήρα
function chk_measure($a) { return preg_replace('/^\<html\>(\w+)\<sup\>(\d+)$/', '$1{\super $2}', chk($a)); }

// Ελέγχει την ταυτότητα μιας διαταγής που πρέπει να έχει τη μορφή π.χ.
// Φ.830/23/3424/Σ.891/12 Δεκ 2005/7ος ΛΜΧ/4ο Γρ.
// Φ.830/23/3424/Σ.891/12 Δεκ 05/7ος ΛΜΧ/4ο Γρ.
// Αν δεν την έχει και το $warning είναι true τότε πετάει ένα warning
function chk_order($a, $warning = true) {
	if (preg_match('/Φ\.?\d{3}(\.\d+)?\/\d+\/\d+\/Σ\.?\d+\/\d{2} (Ιαν|Φεβ|Μαρ|Απρ|Μαι|Ιουν|Ιουλ|Αυγ|Σεπ|Οκτ|Νοε|Δεκ) (\d{2}|\d{4})\/.+/', $a))
		return chk($a);
	if ($warning) trigger_error(($a ? "Το '<b>$a</b>' δεν είναι σωστή" : 'Εσφαλμένη') . ' ταυτότητα διαταγής', E_USER_ERROR);
}

// Ελέγχει την ταυτότητα ενός τιμολογίου που πρέπει να έχει τη μορφή π.χ.
// 123/21-04-1967
function chk_bill($a) {
	if (preg_match('/\d+\/\d{1,2}-\d{1,2}-\d{4}/', $a))
		return chk($a);
	trigger_error("Το '<b>$a</b>' δεν είναι σωστή ταυτότητα τιμολογίου όπως π.χ. το 1324/31-12-2006", E_USER_ERROR);
}

// Ελέγχει ημερομηνία που πρέπει να έχει τη μορφή π.χ. 21 Απρ 1967 ή 21 Απρ 67
function chk_date($a) {
	if (preg_match('/\d{1,2} (Ιαν|Φεβ|Μαρ|Απρ|Μαι|Ιουν|Ιουλ|Αυγ|Σεπ|Οκτ|Νοε|Δεκ) (\d{2}|\d{4})/', $a))
		return chk($a);
	trigger_error(($a ? "Το '<b>$a</b>' δεν είναι ημερομηνία" : 'Οι ημερομηνίες πρέπει να δίνονται') . " στη μορφή π.χ. '20 Νοε 2005' ή '20 Νοε 05'");
}

// Παίρνει ένα string της μορφής "21:10 26 Νοε 2006"
function chk_datetime($a) {
	if (preg_match('/\d{1,2} \d{1,2}\:\d{2} (Ιαν|Φεβ|Μαρ|Απρ|Μαι|Ιουν|Ιουλ|Αυγ|Σεπ|Οκτ|Νοε|Δεκ) \d{4}/', $a))
		return chk($a);
	trigger_error(($a ? "Το '<b>$a</b>' δεν είναι χρονική στιγμή" : 'Οι χρονικές στιγμές πρέπει να δίνονται') . " στη μορφή π.χ. '20 21:34 Νοε 2005'");
}

// Παίρνει ένα string της μορφής "21:10 26 Νοε 2006" και επιστρέφει την ώρα και την ημερομηνία
function get_datetime($a) {
	if (!preg_match('/(\d{1,2}) (\d{1,2}\:\d{2}) (Ιαν|Φεβ|Μαρ|Απρ|Μαι|Ιουν|Ιουλ|Αυγ|Σεπ|Οκτ|Νοε|Δεκ) (\d{4})/', $a, $m))
		trigger_error(($a ? "Το '<b>$a</b>' δεν είναι χρονική στιγμή" : 'Οι χρονικές στιγμές πρέπει να δίνονται') . " στη μορφή π.χ. '20 21:34 Νοε 2005'");
	else {
		$b[] = $m[2]; $b[] = "{$m[1]} {$m[3]} {$m[4]}";
		return $b;
	}
}

function man($a) { return chk($a['Βαθμός'] . ' ' . $a['Ονοματεπώνυμο']); }

function man_ext($a, $c) {
	return inflection(man($a), $c) . (isset($a['Μονάδα']) ? " {$a['Μονάδα']}" : null);
}


// Σπάει μια διαταγή σε κομμάτια
//('Φάκελος', 'Υποφάκελος', 'Πρωτόκολλο', 'Σχέδιο', 'Ημερομηνία', 'Εκδότης')
function get_order($a) {
	$m = null;
	if (preg_match('/(Φ\.?\d{3}(\.\d+)?)\/(\d+)\/(\d+)\/(Σ\.?\d+)\/(\d{2} (Ιαν|Φεβ|Μαρ|Απρ|Μαι|Ιουν|Ιουλ|Αυγ|Σεπ|Οκτ|Νοε|Δεκ) (\d{2}|\d{4}))\/(.+)/', $a, $m))
		return array('Φάκελος' => $m[1], 'Υποφάκελος' => $m[3], 'Πρωτόκολλο' => $m[4],
			'Σχέδιο' => $m[5], 'Ημερομηνία' => $m[6], 'Εκδότης' => $m[9]);
	chk_order($a);
}

// Επιστρέφει array με τις εφημερίδες
function getNewspapers($a) {
	$a = preg_split("/([ ]*,[ ]*)/", $a);
	foreach($a as & $v)
		$v = 'Εφημερίδα «' . toUppercase($v) . '»';
	return $a;
}

function getDates($a) {
	$a = preg_split("/([ ]*,[ ]*)/", $a);
	if (count($a) == 1) return chk_date($a[0]);
	$b = null;
	for ($z = 0; $z < count($a) - 1; $z++)
		$b .= ', ' . chk_date($a[$z]);
	$b .= ' και ' . chk_date($a[count($a) - 1]);
	return substr($b, 2);
}


// αν το $a είναι π.χ. 1234,1 επιστρέφει '1.234,10 '
// αν δεν είναι αριθμός πετάει error και αν είναι 0 ή null, επιστρέφει null
function euro($a, $zero = false) {
	$a = num($a);
	if (!$a) return $zero ? '0,00 ' : null;
	else return sprintf('%01.2f ', $a);
}

// αν δεν είναι αριθμός πετάει error και αν είναι 0 ή null, επιστρέφει 0
function percent($a) {
	$a = num($a);
	return !$a ? '0' : "$a%";
}

function num($a) {
	if (is_numeric($a)) return (float) $a;
	if ($a) trigger_error("Η τιμή '<b>$a</b>' θα έπρεπε να είναι αριθμός");
}


function getEnvironment($a, $b = null) {
	if (isset($_ENV[$a])) {
		$a = $_ENV[$a];
		return $b ? $a == $b : $a;
	}
}

// convert a string to uppercase (in Greek capital letters has no tone)
// if we have e.g. "1ος Λόχος" it returns "1ΟΣ ΛΟΧΟΣ"
function toUppercaseFull($s) {
	static $pre = array('/’/', '/Έ/', '/Ή/', '/Ί/', '/Ό/', '/Ύ/', '/Ώ/', '/ς/');
	static $aft = array('Α', 'Ε', 'Η', 'Ι', 'Ο', 'Υ', 'Ω', 'Σ');
	return preg_replace($pre, $aft, strtoupper($s));
}

// convert a string to uppercase (in Greek capital letters has no tone)
// if we have e.g. "1ος Λόχος" it returns "1ος ΛΟΧΟΣ" and not "1ΟΣ ΛΌΧΟΣ"
function toUppercase($s) {
	return preg_replace_callback('/(\W|^)\D\w+/', create_function('$m', 'return toUppercaseFull($m[0]);'), $s);
}

// όπως η wordInflection αλλά κλίνει όλες τις λέξεις της πρότασης
// αρκεί να είναι μεγαλύτερες από 2 γράμματα
function inflection($a, $w) {
	return !$w ? $a : preg_replace_callback('/[Α-Ωα-ω’-Ώά-ώΐΰΪΫ]{3,}|\d+(ος|η|ο)/',
		create_function('$m', "return wordInflection(\$m[0], $w);"), $a);
}

// μετατρέπει την ονομαστική ενός αρσενικού ή θηλυκού σε
// (1) γενική, (2) αιτιατική, (3) κλιτική μόνο του ενικού
// δεν υποστηρίζει τρελά πράματα
// η λέξη πρέπει να είναι με μικρά γράμματα (η κατάληξη)
function wordInflection($o, $w) {
	$b = substr($o, -2);
	if ($b == 'ος' && $w == 1) return substr($o, 0, -1) . 'υ';
	elseif ($b == 'ός' && $w == 1) return substr($o, 0, -2) . 'ού';
	elseif ($w >= 2 && $w <= 3 && ($b == 'ος' || $b == 'ός') ||
					$w >= 1 && $w <= 3 && strpos('ης ής ας άς ες ές υς ύς ', "$b ") !== false)
		return substr($o, 0, -1);
  elseif ($w == 1 && strpos('ηήαάωώ', substr($o, -1)) !== false)
		return $o . 'ς';
	return $o;
}


// επιστρέφει το χρόνο σε μορφή π.χ. '05 Ιουν 2005'
function now($a = null) { return strftime('%d %b %Y', $a == null ? time() : $a); }


// returns the greek enumeration system for numbers 1 to 999
function countGreek($n) {
	if ($n < 1 || $n > 999) trigger_error("Για να μεταφραστεί το '<b>$n</b>' σε ελληνική αρίθμηση πρέπει να είναι ακέραιος και να ανήκει στο [1, 999]");
	static $m = array(null, 'α', 'β', 'γ', 'δ', 'ε', 'στ', 'ζ', 'η', 'θ');
	static $d = array(null, 'ι', 'κ', 'λ', 'μ', 'ν', 'ξ', 'ο', 'π', '\u991  ');
	static $e = array(null, 'ρ', 'σ', 'τ', 'υ', 'φ', 'χ', 'ψ', 'ω', '\u993  ');
	return $e[floor($n / 100)] . $d[floor($n / 10) % 10] . $m[$n % 10];
}


// Αν δώσετε 100.91 θα πάρετε "εκατό ευρώ και ενενήντα ένα λεπτά"
function euro2str($d) {
	$a = int2str((int) floor($d), 2);
	$b = int2str((int) (($d - floor($d)) * 100 + 0.4), 2);
	if ($b != 'μηδέν') $b .= ' λεπτά'; else $b = null;
	if ($a == 'μηδέν')
		return ($b == null) ? 'μηδέν ευρώ' : $b;
	else
		return $a . ' ευρώ' . ($b == null ? '' : ' και ' . $b);
}

// Επιστρέφει το string του φυσικού αριθμού $n
// για αριθμούς από 0 ως 999.999.999.999.999.999.999.999.999.999.999
// Δυστηχώς το php δεν υποστηρίζει integers μεγαλύτερους από 2^31-1 (για 32 bit μηχανήματα)
// Το $genos αναφέρεται στο γένος (αρσενικό = 0, θυληκό = 1, ουδέτερο = 2)
function int2str($n, $genos) {
	if (!is_int($n) || $n < 0) return;
	if (!$n) return 'μηδέν';
	static $xilia = array('χίλιοι', 'χίλιες', 'χίλια');
	static $polla = array('', 'δισ', 'τρισ', 'τετράκις ', 'πεντάκις ', 'εξάκις ', 'επτάκις ', 'οκτάκις ', 'εννιάκις ');

	$out = null;
	$b = $n % 1000;
	$n = floor($n / 1000);
	if ($b) $out = int2str_1_999($b, $genos);
	if (!$n) return $out;

	$b = $n % 1000;
	$n = floor($n / 1000);
	if ($b) {
		if ($out) $out = ' ' . $out;
		if ($b == 1) $out = $xilia[$genos] . $out;
		else $out = int2str_1_999($b, 1) . ' χιλιάδες' . $out;
	}

	$c = 0;
	while($n) {
		$b = $n % 1000;
		$a = floor($n / 1000);
		if ($b) {
			if ($out) $out = ' ' . $out;
			if ($b == 1) $out = 'ένα εκατομμύριο' . $out;
			else $out = int2str_1_999($b, 2) . ' ' . $polla[$c] . 'εκατομμύρια' . $out;
		}
		if (!$n) return $out;
		$c++;
	}
	return $out;
}

// Επιστρέφει το string του φυσικού αριθμού $n για αριθμούς από 1 ως 999.
// Το $genos αναφέρεται στο γένος (αρσενικό = 0, θυληκό = 1, ουδέτερο = 2)
function int2str_1_999($n, $genos) {
	if (!is_int($n) || $n < 1 || $n > 999) return;

	static $ekatodades = array(
		array('εκατό', 'εκατόν', 'διακόσιοι', 'τριακόσιοι', 'τετρακόσιοι', 'πεντακόσιοι', 'εξακόσιοι', 'εφτακόσιοι', 'οκτακόσιοι', 'εννιακόσιοι'),
		array('εκατό', 'εκατόν', 'διακόσιες', 'τριακόσιες', 'τετρακόσιες', 'πεντακόσιες', 'εξακόσιες', 'εφτακόσιες', 'οκτακόσιες', 'εννιακόσιες'),
		array('εκατό', 'εκατόν', 'διακόσια', 'τριακόσια', 'τετρακόσια', 'πεντακόσια', 'εξακόσια', 'εφτακόσια', 'οκτακόσια', 'εννιακόσια')
	);
  static $dekades = array('είκοσι', 'τριάντα', 'σαράντα', 'πενήντα', 'εξήντα', 'εβδομήντα', 'ογδόντα', 'ενενήντα');
	static $monades = array(
		array('ένας', 'δύο', 'τρείς', 'τέσσερεις', 'πέντε', 'έξι', 'επτά', 'οκτώ', 'εννέα', 'δέκα', 'έντεκα', 'δώδεκα', 'δεκατρείς', 'δεκατέσσερεις', 'δεκαπέντε', 'δεκαέξι', 'δεκαεπτά', 'δεκαοκτώ', 'δεκαεννιά'),
		array('ένα', 'δύο', 'τρία', 'τέσσερα', 'πέντε', 'έξι', 'επτά', 'οκτώ', 'εννέα', 'δέκα', 'έντεκα', 'δώδεκα', 'δεκατρία', 'δεκατέσσερα', 'δεκαπέντε', 'δεκαέξι', 'δεκαεπτά', 'δεκαοκτώ', 'δεκαεννιά')
	);

  $out = null;
	$a = floor($n / 100);
	$b = $n % 100;
	if ($a) {
		if (!$b && $a == 1) $a = 0;
		$out = $ekatodades[$genos][$a];
	}
	if (!$b) return $out;
	if ($out) $out .= ' ';
	if ($b < 20) {
		if ($genos == 1 && $b == 1) return $out . 'μία';
		return $out . $monades[$genos == 2 ? 1 : 0][$b - 1];
	}
	$out .= $dekades[floor($b / 10) - 2];
	$b = $n % 10;
	if (!$b) return $out;
	if ($genos == 1 && $b == 1) return $out . ' μία';
	return $out . ' ' . $monades[$genos == 2 ? 1 : 0][$b - 1];
}


?>