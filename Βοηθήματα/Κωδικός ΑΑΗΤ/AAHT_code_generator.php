<?php


function short_name($a) {
	$a = aaht_name($a);
	switch($a) {
		case 'упоуяцеио ехмийгс алумас': return 'упеха';
		case 'цемийо епитекеио ехмийгс алумас': return 'цееха';
		case 'цемийо епитекеио стяатоу': return 'цес';
		case 'стяатиытийо жаялайеио ахгмым': return 'сжа';
		case '401 цемийо стяатиытийо мосойолеио ахгмым': return '401 цсма';
		case '424 цемийо стяатиытийо мосойолеио ейпаидеусеыс': return '424 цсме';
		case 'талеио ехмийгс алумас': return 'теха';
		case 'летовийо талеио стяатоу': return 'лтс';
		case 'еидийос йкадос оийомолийгс емисвусгс леяислатоувым стяатоу нгяас': return 'ейоелс';
		case 'еидийос коцаяиаслос аккгкобогхеиас стяатоу': return 'екоас';
		case 'талеио аяыцгс ломилым покитийым упаккгкым уеха/цес': return 'талпу/упеха/цес';
		case 'талеио аяыцгс амапгяым йаи хулатым покелоу': return 'таахп';
		default:
			$a = str_replace('.', '', $a);
			// pattern: 'longname (shortname) other'
			if (preg_match('/\((.*)\)/', $a, $b)) return $b[1];
			// pattern: 'short name [other]'
			$b = explode('[', $a);
			$a = trim($b[0]);
			// pattern: 'short name ╚other╩'
			$b = explode('╚', $a);
			$a = trim($b[0]);
			// pattern: '3Г тан' -> '3 тан'
			if (preg_match('/(\d+)Г (.+)/', $a, $b)) $a = "$b[1] $b[2]";
			break;
	}
	return $a;
}

function aaht_name($a) {
	$a = trim($a);
	// pattern: '7г л/й тан' -> '7 л/й тан'
	if (preg_match('/(\d+)г (.+)/', $a, $b)) $a = "{$b[1]}Г {$b[2]}";
	return $a;
}

function long_name($a) {
	$a = aaht_name($a);
	$b = explode('(', $a);
	$a = trim($b[0]);
	return $a;
}


if ($argc < 3) die("Usage: php {$argv[0]} AAHT_input.csv output.php\n");

// жЭЯТЫСГ ТОУ АЯВЕъОУ csv ЛЕ ТОУР AAHT
if (!file_exists($argv[1])) die("File not found: {$argv[1]}\n");
$fp = fopen($argv[1], 'r');
if (!$fp) die("Cannot open file: {$argv[1]}\n");
$data = array();
while ($row = fgetcsv($fp, 0, ";")) $data[] = $row;
fclose($fp);

// ╦МАЯНГ ЕНЭДОУ СТО stdin АККэ СТГМ ПЯАЦЛАТИЙЭТГТА СУЦЙЯэТГСч ТОУ ЙАИ АПЭДОСГ СЕ ЛЕТАБКГТч.
ob_start(); ?>
/** еПИСТЯщЖЕИ щМА array ЛЕ ЭКА ТА ОМЭЛАТА ТЫМ аМАХЕТОУСЧМ аЯВЧМ САМ ЙКЕИДИэ ТОУР аагт САМ ТИЛщР.
 * @param string|null $a г АМАХщТОУСА АЯВч ТГР ОПОъАР ТА СТОИВЕъА АМАФГТОЩЛЕ ч null.
 * @return аМ $a ЕъМАИ null ТЭТЕ ЕПИСТЯщЖЕИ КъСТА ЛЕ ЭКЕР ТИР АМАХщТОУСЕР АЯВщР. еИДэККЫР ЕПИСТЯщЖЕИ
 * ЛЭМО ТА СТОИВЕъА ТГР ДОСЛщМГР АМАХщТОУСАР АЯВчР. */
function aaht_list($a = null) {
	$b = array(
<?php 
	$total = count($data);
	for ($i = 1; $i < $total; ++$i) {
		$row = $data[$i];
		//if ($row[3] != 'цемийо епитекеио стяатоу') continue;
		if ($row[6] != 'упоуяцеио ехмийгс алумас') continue;
		if ($row[3] == 'цемийо епитекеио маутийоу' || $row[5] == 'цемийо епитекеио маутийоу') continue;
		if ($row[3] == 'цемийо епитекеио аеяопояиас' || $row[5] == 'цемийо епитекеио аеяопояиас') continue;
		if (strstr($row[2], 'аеяопояи') !== false) continue;
		if (strstr($row[2], 'маутий') !== false) continue;
		// iconv('UTF-8', 'ISO-8859-7', $row[2])
		$short = short_name($row[2]); $long = long_name($row[2]); $aaht = aaht_name($row[2]);
		echo "\t\t'" . addcslashes($aaht, "'") . "' => array('йЫДИЙЭР аагт' => '{$row[0]}'";
		if ($row[1] != '090153025') echo ", 'ажл' => '{$row[1]}'";
		if ($short != $aaht) echo ", 'сЩМТЛГСГ' => '" . addcslashes($short, "'") . "'";
		echo '),' . PHP_EOL;
	}
?>	);
	return $a === null ? $b : $a;
}
<?php
file_put_contents($argv[2], ob_get_clean());
?>