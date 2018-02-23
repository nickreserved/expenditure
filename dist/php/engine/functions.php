<?

set_time_limit(5);
error_reporting(E_ALL);

setlocale(LC_ALL, 'el_GR', 'ell_grc');

require_once('unserialize.php');


// Υπολογίζει κάποια πράγματα για το σύνολο ορισμένων τιμολογίων που βρίσκονται
// στο array $a
function calc_bills($a) {
	$b = array();
	foreach($a as $v) {
		calc_bills_sum($b, $v, 'Καταλογιστέο');
		calc_bills_sum($b, $v, 'Πληρωτέο');
		calc_bills_sum($b, $v, 'ΚαθαρήΑξία');
		calc_bills_sum($b, $v, 'ΚαθαρήΑξίαΜείονΚρατήσεις');
		calc_bills_sum($b, $v, 'ΦΕΣεΕυρώ');
		calc_bills_sum($b, $v, 'ΥπόλοιποΠληρωτέο');
		calc_bills_sum_arr($b, $v, 'ΚατηγορίεςΦΠΑ');
		calc_bills_sum_arr($b, $v, 'ΑνάλυσηΚρατήσεωνΣεΕυρώ');
	}
	return $b;
}

function calc_bills_sum_arr(&$b, $a, $i) {
	if (!isset($b[$i])) $b[$i] = array();
	foreach($a[$i] as $k => $v)
		if (!isset($b[$i][$k])) $b[$i][$k] = $v;
		else $b[$i][$k] += $v;
}

// αν π.χ. δεν υπάρχει το $b['Καταλογιστέο'] δημιούργησέ το από το Τιμολόγιο $a
// αν υπάρχει τότε πρόσθεσε από το Τιμολόγιο $a
function calc_bills_sum(&$b, $a, $i) {
	if (!isset($b[$i])) $b[$i] = $a[$i];
	else $b[$i] += $a[$i];
}


// Επιστρέφει ενα array με τιμολόγια που έχουν ΦΕ.
function bills_with_fe($a) {
	$b = array();
	foreach($a as $v)
		if ($v['ΠοσοστόΦΕ']) $b[] = $v;
	return $b;
}

// Επιστρέφει ενα array με keys τις κρατήσεις σε ποσοστό και
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

// Επιστρέφει ενα array με keys τον προμηθευτή και
// values array με όλα τα τιμολόγια από αυτόν.
// Δεν επιστρέφει τιμολόγια δίχως ΦΕ.
function bills_by_provider($a) {
	$b = array();
	foreach($a as $v)
		if ($v['ΠοσοστόΦΕ']) $b[$v['Προμηθευτής']['Επωνυμία']][] = $v;
	return $b;
}

// Επιστρέφει ενα array με keys τον "μήνα έτος" π.χ. "Νοε 2005" και
// values array με όλα τα τιμολόγια του μηνός.
// Δεν επιστρέφει τιμολόγια δίχως ΦΕ.
function bills_by_month($a) {
	$b = array();
	foreach($a as $v)
		if (preg_match('/\d+\/\d{1,2}-(\d{1,2})-(\d{4})/', $v['Τιμολόγιο'], $m)) {
			$b[mktime(0, 0, 0, $m[1], 1, $m[2])][] = $v;
		} else trigger_error("Το '{$v['Τιμολόγιο']}' δεν είναι σωστή ταυτότητα τιμολογίου");
	return $b;
}


// Από το array $a επιστρέφει τα τιμολόγια που είναι ($f == true)
// ή δεν είναι ($f == false) κατηγορίας $i.
function getBillsCategory($a, $i, $f = true) {
	$b = null;
	foreach($a as $v)
		if (($v['Κατηγορία'] == $i) == $f) $b[] = $v;
	return $b;
}

// Από το array $a επιστρέφει τα τιμολόγια που είναι τύπου $i.
function getBillsType($a, $i) {
	$b = null;
	foreach($a as $v)
		if ($v['Τύπος'] == $i) $b[] = $v;
	return $b;
}


// Έλεγχος του $a για χαρακτήρες ελέγχου στο RTF Spec και αντικατάστασή τους
// αν υπάρχουν με backslash και το χαρακτήρα
function chk($a) { return preg_replace('/([\\\{\}])/', '$0$0', $a); }

function man($a) {
	if ($a['Βαθμός'] == null) return chk($a['Ονοματεπώνυμο']);
	return chk($a['Βαθμός'] . ' ' . $a['Ονοματεπώνυμο']);
}

// Ελέγχει την ταυτότητα μιας διαταγής που πρέπει να έχει τη μορφή π.χ.
// Φ.830/23/3424/Σ.891/12 Δεκ 2005/7ος ΛΜΧ/4ο Γρ.
// Αν δεν την έχει και το $warning είναι true τότε πετάει ένα warning
function chk_order($a, $warning = true) {
	if (preg_match('/Φ\.?\d{3}\/\d+\/\d+\/Σ\.?\d+\/\d{2} (Ιαν|Φεβ|Μαρ|Απρ|Μαι|Ιουν|Ιουλ|Αυγ|Σεπ|Οκτ|Νοε|Δεκ) \d{4}\/.+/', $a))
		return chk($a);
	if ($warning) trigger_error("Το '$a' δεν είναι σωστή ταυτότητα διαταγής");
}

// Ελέγχει την ταυτότητα ενός τιμολογίου που πρέπει να έχει τη μορφή π.χ.
// 123/21-04-1967
function chk_bill($a) {
	if (preg_match('/\d+\/\d{1,2}-\d{1,2}-\d{4}/', $a))
		return chk($a);
	trigger_error("Το '$a' δεν είναι σωστή ταυτότητα τιμολογίου");
}

// Ελέγχει ημερομηνία που πρέπει να έχει τη μορφή π.χ. 21 Απρ 1967
function chk_date($a) {
	if (preg_match('/\d{1,2} (Ιαν|Φεβ|Μαρ|Απρ|Μαι|Ιουν|Ιουλ|Αυγ|Σεπ|Οκτ|Νοε|Δεκ) \d{4}/', $a))
		return chk($a);
	trigger_error("Το '$a' δεν είναι ημερομηνία στη μορφή '21 Απρ 2005'");
}


// Σπάει μια διαταγή σε κομμάτια
//('Φάκελος', 'Υποφάκελος', 'Πρωτόκολλο', 'Σχέδιο', 'Ημερομηνία', 'Εκδότης')
function get_order($a) {
	$m = null;
	if (preg_match('/(Φ\.?\d{3})\/(\d+)\/(\d+)\/(Σ\.?\d+)\/(\d{2} (Ιαν|Φεβ|Μαρ|Απρ|Μαι|Ιουν|Ιουλ|Αυγ|Σεπ|Οκτ|Νοε|Δεκ) \d{4})\/(.+)/', $a, $m))
		return array('Φάκελος' => $m[1], 'Υποφάκελος' => $m[2], 'Πρωτόκολλο' => $m[3],
			'Σχέδιο' => $m[4], 'Ημερομηνία' => $m[5], 'Εκδότης' => $m[7]);
	trigger_error("Το '$a' δεν είναι σωστή ταυτότητα διαταγής");
}

// Επιστρέφει από το Γραφείο του Σχηματισμού το όνομα του Σχηματισμού
// π.χ. από το "7η ΜΚ ΤΑΞΠΖ/4ο ΕΓ" θα επιστρέψει το "7η ΜΚ ΤΑΞΠΖ"
function getBrigate($a) {
	$m = null;
	if (preg_match('/([^\/]+)\/.+/', $a, $m))
		return $m[1];
	trigger_error("Το '$a' δεν είναι σωστή διεύθυνση Γραφείου Σχηματισμού");
}


// αν το $a είναι π.χ. 1234,1 επιστρέφει '1.234,10 '
// αν δεν είναι αριθμός πετάει error και αν είναι 0 ή null, επιστρέφει null
function euro($a, $zero = false) {
	if (is_float($a) || is_int($a) || !isset($a))
		if ($a == 0) return $zero ? '0,00 ' : null;
		else return sprintf('%01.2f ', $a);
	trigger_error("Η τιμή '$a' θα έπρεπε να είναι αριθμός");
}

// αν δεν είναι αριθμός πετάει error και αν είναι 0 ή null, επιστρέφει null
function percent($a) {
	$a = num($a);
	return $a == null ? '0' : "$a%";
}

function num($a) {
	if (is_numeric($a)) return (float) $a;
	if (isset($a) && $a !== 0) trigger_error("Η τιμή '$a' θα έπρεπε να είναι αριθμός");
}


function getEnvironment($a, $b = null) {
	if (isset($_ENV[$a])) {
		$a = $_ENV[$a];
		return $b ? $a == $b : $a;
	}
}


// convert a string to uppercase (in Greek capital letters has no tone)
// if we have e.g. "1ος Λόχος" it returns "1ος ΛΟΧΟΣ" and not "1ΟΣ ΛΌΧΟΣ"
function toUppercase($s) {
	static $pre = array('/’/', '/Έ/', '/Ή/', '/Ί/', '/Ό/', '/Ύ/', '/Ώ/');
	static $aft = array('Α', 'Ε', 'Η', 'Ι', 'Ο', 'Υ', 'Ω');
	$t = preg_replace_callback('/(\W|^)\D\w*/', create_function('$m', 'return strtoupper($m[0]);'), $s);
	return preg_replace($pre, $aft, $t);
}


// όπως η wordInflection αλλά κλίνει όλες τις λέξεις της πρότασης
// αρκεί να είναι μεγαλύτερες από 2 γράμματα
function inflection($a, $w) {
	return preg_replace_callback('/[Α-Ωα-ω’-Ώά-ώΐΰΪΫ]{3,}/',
		create_function('$m', "return wordInflection(\$m[0], $w);"), $a);
}

// μετατρέπει την ονομαστική ενός αρσενικού ή θυλικού σε
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


// returns the greek enumeration system for numbers 1 to 89
// not over 89 because 90 is a non printable character
function countGreek($n) {
	if ($n < 1 || $n > 89) trigger_error("Για να μεταφραστεί το '$n' σε ελληνική αρίθμηση πρέπει να είναι ακέραιος και να ανήκει στο [1, 89]");
	static $m = array(null, 'α', 'β', 'γ', 'δ', 'ε', 'στ', 'ζ', 'η', 'θ');
	static $d = array(null, 'ι', 'κ', 'λ', 'μ', 'ν', 'ξ', 'ο', 'π');
	return $d[floor($n / 10)] . $m[$n % 10];
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