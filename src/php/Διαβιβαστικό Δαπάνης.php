<?php
require_once('functions.php');
require_once('order.php');
init(5);

start_35_20();
order_header(
		ifexist2($output, $data, '������������ �������'),
		array($data['��������� ����']), array($data['������']), $output, '1 �������',
		'�������', array($data['������� �������� ����������']['���������']));
?>
1.\tab ��� ����������� ��������� ������ ��������� ������� ��� ����� �<?=rtf($data['������'])?>� ����� <?=euro($data['�����']['������������'])?>, ��� ��������������� ������.\par
2.\tab ����������� ��� ��� ��������� ���.\par
3.\tab ��������� �������: <?=personi($data['����� ��������'], 0)?>, ���. <?=rtf($data['��������'])?>.\par

<?php order_footer($output); ?>

\sect

<?php

rtf_close(__FILE__);