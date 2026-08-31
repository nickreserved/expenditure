@echo off
del bundle.phar bundle.phar.zip bundle.phar.tar.gz
..\..\runtime\php5\php.exe -d phar.readonly=0 build-phar.php