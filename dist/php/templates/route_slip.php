<?
require_once('engine/functions.php');
require_once('header.php');
require_once('engine/order.php');

if (!isset($data['����'])) {
	if (!isset($bills_info)) $bills_info = calc_bills($data['���������']);
	$data['����'] = $bills_info['������������'];
}

if (!isset($draft)) $draft = getEnvironment('draft', 'true');
if (!isset($prereport)) $prereport = false;

if ($prereport) $to = $data['������������������'];
else {
	$to = get_order($data['�����������']);
	$to = $to['�������'];
}
$attached = $prereport ? '��� (1)' : '���� (1) �������';
$connect = $prereport ? null : array($data['�����������']);
?>

{

\sectd\pgwsxn11906\pghsxn16838\marglsxn1984\margrsxn1134\margtsxn1134\margbsxn1134

<?
echo preOrder(isset($data['������������']) ? $data['������������'] : null, array($to), array(null), $draft, $attached);
echo '\pard\plain\par\par\par';
echo subjectOrder('�������', $connect);
?>
\pard\plain\fs28\tx567\tx1134\tx1701\tx2268\qj
<? if ($prereport) { ?>
\tab\b 1.\b0\tab ����������� ��������� ������ ������������ ������� ��� ����� �<?=chk($data['������'])?> <?=euro($data['����'])?>�.\par\par
<? } else { ?>
\tab\b 1.\b0\tab ����������� ��������� ������� ��������� ������� ��� ����� �<?=chk($data['������'])?> <?=euro($data['����'])?>�, ��� ��������������� ������.\par\par
<? } ?>
\tab\b 2.\b0\tab ����������� ��� ��� ��������� ���.\par\par


<? if ($draft) draftOrder(); else bottomOrder(); ?>

\sect

}
