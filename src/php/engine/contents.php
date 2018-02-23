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
		trigger_error("�� '<b>$v</b>' ����� ����� '<b>" . gettype($v) . "</b>' ��� ��� �������������.");
		return;
	}
	return $k ? "$a $k=$v;" : "$a $v;";
}

function get_contents($k) {
	static $papers = array(
		'������������ �������' => array('������' => true, '�����������' => true, '���' => '������������'),
		'�������� �������' => array('������' => true, '�����������' => true),
		'����� ����������� ��������' => array('������' => true, '�����������' => true),
		'����������' => array('������' => true),
		'��� �������� ��������' => array('���' => '�����������'),
		'��� ����������� ���������' => array('������' => true, '���' => '�����������'),
		'���������� ������ ��� ��������' => array('������' => true),
		'���������� ��������� ��������� ��������' => array('������' => true),
		'���������� ��������� ��� ��������� ���������' => array('������' => true),
		'������ ��������� �������' => array('������' => true),
		'���������' => null,
		'��������� ���� ������' => array('������' => true),
		'�������� �������������� �������' => array('������' => true),
		'�������� �������������� �������' => array('������' => true),
		'�������� �������� ��' => null,
		'�������� �������� ���������' => null,
		'����' => null,
		'���������� ��� ����������� �����������' => null,
		'���������� ��������� ������ ��������' => array('������' => true),
		'��������� ����������' => array('������' => true),
		'���������� ������������� ��������' => array('������' => true),
		'�������� ����������' => array('������' => true),
		'���������� ����������� ���������' => array('������' => true),
		'���������� ���������� ��� ��������� ���������' => array('������' => true),
		'�������� �� ������� ������' => array('������' => true),
		'�������' => null,
		'�������� ��������� ��� ����� ��� �������' => array('������' => true),
		'�������� ��������� ��� �����' => array('������' => true),
		'�������� ����������������� �����' => array('������' => true),
		'����� �������' => null,
		'��� ���������� �����������' => array('������' => true, '���' => '�������������'),
		'���������� ���������� �� �� ���������' => null,
		'�������� �����������' => null,
		'���������� ������ �����������' => array('������' => true),
		'��� ����������� �����������' => array('������' => true, '���' => '��������������')
	);

	if (is_string($k)) {
		if (substr($k, -4) != '.php')
			return array_key_exists($k, $papers) ? $papers[$k] : array();
		else {
			$k = substr($k, 0, -4);
			foreach($papers as $key => $v)
				if ($v['������'] == $k) return $key;
		}
	}
	else

		if ($k == -1) {
			$a = array_keys($papers);
			echo 'java.util.ArrayList {';
			foreach($a as $v)
				echo get_java(null, $v);
			echo '};';

		} else {

			static $out = array(

				array(	// ��������� - ��������� - �������� | ����� ����������
					0, 1, // ������������
					1, 1, // ��������
					2, 1, // ����� �����������
					3, 1, // ����������
					4, 1, // ������� ��������
					5, 2, // ���������
					25,1, // �������
					3, 1, // ����������
					6, 3, // ����� - �������
					7, 3, // �������� ��������
					8, 3, // �������� - �������� ��������
					9, 3, // ������
					24,3, // ��������
					10,1, // ���������
					17,1, // ���������� ��� ����������� �����������
					11,3, // ���������
					3, 1, // ����������
					14,1, // ��
					15,1, // ���������
					12,1, // �������� �����������
					20,1, // ������������� ��������
					16,1, // ����
					13,1, // �������� �����������
					26,1  // �������� ��������� ��� ����� ��� �������
				),

				array(	// ��������H E���� | ����� ����������
					0, 1, // ������������
					1, 1, // ��������
					2, 1, // ����� �����������
					3, 1, // ����������
					4, 1, // ������� ��������
					5, 1, // ���������
					3, 1, // ����������
					25,1, // �������
					3, 1, // ����������
					18,2, // ������ ��������
					19,2, // ��������� ����������
					21,2, // �������� ����������
					22,3, // ���������� ��������
					23,3, // ��������� - �������� ��������
					3, 1, // ����������
					6, 3, // ����� - �������
					8, 3, // �������� - �������� ��������
					9, 3, // ������
					24,3, // ��������
					10,1, // ���������
					17,1, // ���������� ��� ����������� �����������
					11,3, // ���������
					15,1, // ���������
					14,1, // ��
					20,1, // ������������� ��������
					16,1, // ����
					28,1, // �������� ����������������� �����
					26,1, // �������� ��������� ��� ����� ��� �������
					27,1  // �������� ���������
				),

				array(	// ��������� - ��������� - �������� | ��������� �����������
					0, 1, // ������������
					1, 1, // ��������
					2, 1, // ����� �����������
					3, 1, // ����������
					4, 1, // ������� ��������
					5, 2, // ���������
					32,3, // �������� �����������
					33,1, // ���������� ������
					34,1, // ���������� �����������
					25,1, // �������
					3, 1, // ����������
					6, 3, // ����� - �������
					7, 3, // �������� ��������
					8, 3, // �������� - �������� ��������
					9, 3, // ������
					24,3, // ��������
					10,1, // ���������
					17,1, // ���������� ��� ����������� �����������
					11,3, // ���������
					3, 1, // ����������
					14,1, // ��
					15,1, // ���������
					12,1, // �������� �����������
					20,1, // ������������� ��������
					16,1, // ����
					13,1, // �������� �����������
					26,1  // �������� ��������� ��� ����� ��� �������
				),

				array(	// ��������H E���� | ��������� �����������
					0, 1, // ������������
					1, 1, // ��������
					2, 1, // ����� �����������
					3, 1, // ����������
					5, 1, // ���������
					4, 1, // ������� ��������
					32,3, // �������� �����������
					33,1, // ���������� ������
					3, 1, // ����������
					34,1, // ���������� �����������
					25,1, // �������
					3, 1, // ����������
					18,2, // ������ ��������
					19,2, // ��������� ����������
					21,2, // �������� ����������
					22,3, // ���������� ��������
					23,3, // ��������� - �������� ��������
					3, 1, // ����������
					6, 3, // ����� - �������
					8, 3, // �������� - �������� ��������
					9, 3, // ������
					24,3, // ��������
					10,1, // ���������
					17,1, // ���������� ��� ����������� �����������
					11,3, // ���������
					15,1, // ���������
					14,1, // ��
					20,1, // ������������� ��������
					16,1, // ����
					28,1, // �������� ����������������� �����
					26,1, // �������� ��������� ��� ����� ��� �������
					27,1  // �������� ���������
				),

				array(	// ��������� - ��������� - �������� | �������� �����������
					0, 1, // ������������
					1, 1, // ��������
					2, 1, // ����� �����������
					3, 1, // ����������
					4, 1, // ������� ��������
					5, 2, // ���������
					30,1, // ��������� �����������
					31,1, // ���������� ����������
					32,3, // �������� �����������
					33,1, // ���������� ������
					34,1, // ���������� �����������
					25,1, // �������
					3, 1, // ����������
					6, 3, // ����� - �������
					7, 3, // �������� ��������
					8, 3, // �������� - �������� ��������
					9, 3, // ������
					24,3, // ��������
					10,1, // ���������
					17,1, // ���������� ��� ����������� �����������
					11,3, // ���������
					3, 1, // ����������
					14,1, // ��
					15,1, // ���������
					12,1, // �������� �����������
					20,1, // ������������� ��������
					16,1, // ����
					13,1, // �������� �����������
					26,1  // �������� ��������� ��� ����� ��� �������
				),

				null

			);

			$a = $out[$k];
			$k = array_keys($papers);
			echo 'common.VectorObject {';
			for($z = 0; $z < count($a); $z += 2) {
				echo 'cost.ContentItem {';
					echo get_java('��������������', $k[$a[$z]]);
					echo get_java('������', $a[$z + 1]);
				echo '};';
			}
			echo '};';
		}
}


if (!isset($data)) get_contents((int) $argv[2]);

?>