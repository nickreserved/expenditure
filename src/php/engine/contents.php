<?

function get_java($k, $v) {
	if (is_int($v)) $a = 'java.lang.Integer';
	elseif (is_bool($v)) {
		$a = 'java.lang.Boolean';
		$v = $v ? 'true' : 'false';
	} elseif (is_string($v)) {
		$a = 'java.lang.String';
		$v = '"' . preg_replace('/[\\\"]/', '\\\$0' , $v) . '"';
	} elseif (is_float($v)) $a = 'java.lang.Double';
	else {
		trigger_error("'$v' has '" . gettype($v) . "' type which is unsupported.");
		return;
	}
	return $k ? "$a $k=$v;" : "$a $v;";
}

function get($a) {
	echo 'java.util.Vector {';
	foreach($a as $v) {
		echo 'cost.ContentItem {';
		foreach($v as $key => $val)
			echo get_java($key, $val);
		echo '};';
	}
	echo '};';
}

$papers = array(
	array('��������������' => '������������ �������', '������' => 'route_slip', '�����������' => true, '���' => '������������'),
	array('��������������' => '�������� �������', '������' => 'outpaper', '�����������' => true),
	array('��������������' => '����� ����������� ��������', '������' => 'contents', '�����������' => true),
	array('��������������' => '����������', '������' => 'subfolder'),
	array('��������������' => '������� �������� ��������', '���' => '�����������'),
	array('��������������' => '��� ����������� ���������', '������' => 'order', '���' => '�����������'),
	array('��������������' => '���������� ������ ��� ��������', '������' => 'buy_service'),
	array('��������������' => '���������� ��������� ��������� ��������', '������' => 'contracting'),
	array('��������������' => '���������� ��������� ��� ��������� ���������', '������' => 'quality_quantity'),
	array('��������������' => '������ ��������� �������', '������' => 'report'),
	array('��������������' => '���������'),
	array('��������������' => '��������� ���� ������', '������' => 'holds'),
	array('��������������' => '�������� �������������� �������', '������' => 'refit_unit_affirmation'),
	array('��������������' => '�������� �������������� �������', '������' => 'repair_unit_affirmation'),
	array('��������������' => '�������� �������� ��'),
	array('��������������' => '�������� �������� ���������'),
	array('��������������' => '����'),
	array('��������������' => '���������� �����������'),
	array('��������������' => '����������� �����������')
);

if ($argv[2] == 'all') { get($papers); exit; }



function put($a) {
	global $papers;
	echo 'common.VectorObject {';
	for($z = 0; $z < count($a); $z += 2) {
		$v = $papers[$a[$z]];
		echo 'cost.ContentItem {';
		foreach($v as $key => $val)
			echo get_java($key, $val);
		echo "java.lang.Byte ������={$a[$z + 1]};";
		echo '};';
	}
	echo '};';
}


$out = array(

	array(	// ��������� - ��������� - �������� | ����� ����������
		0, 1, // ������������
		1, 1, // ��������
		2, 1, // ����� �����������
		3, 1, // ����������
		4, 1, // ������� ��������
		5, 1, // ���������
		3, 1, // ����������
		5, 1, // ���������
		6, 3, // ����� - �������
		7, 3, // �������� ��������
		8, 3, // �������� - �������� ��������
		9, 3, // ������
		10,1, // ���������
		17,1, // ���������� �����������
		18,1, // ����������� �����������
		11,3, // ���������
		3, 1, // ����������
		14,1, // ��
		15,1, // ���������
		12,1, // �������� �����������
		13,1, // �������� �����������
		16,1  // ����
	),

	array(	// ����� - �� ��������������� �����
		0, 1, // ������������
		1, 1, // ��������
		3, 1, // ����������
		3, 1, // ����������
		3, 1, // ����������
		4, 1, // ������� ��������
		8, 3, // �������� - �������� ��������
		9, 3,  // ������
		11, 3, // ���������
	)

);

$type = 0;
switch($argv[2]) {
	case '��������� ����� �� ����� �������� � �������� ��� ������� � ��������������': $type++;
	case '��������� ����� ��� ����������� ����������': $type++;
}
switch($argv[3]) {
	case '�������� �����������': $type += 3;
	case '��������� �����������': $type += 3;
}


put($out[$type ? 1 : 0]);

?>