<?php
require_once('statement.php');
require_once('unserialize.php');
require_once('header.php');

$data['Δήλωση'] = str_replace(array("\r\n", "\r", "\n"), '\par ', str_replace("\t", '\tab ', rtf($data['Δήλωση'])));

statement($data);

rtf_close(__FILE__);