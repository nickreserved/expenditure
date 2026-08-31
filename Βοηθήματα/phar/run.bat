@echo off
echo bundle.jar
..\..\runtime\php5\php.exe -n -r "require('phar://bundle.jar/Δαπάνη.php');" -- bundle.jar
echo bundle.phar
..\..\runtime\php5\php.exe -n -r "require('phar://bundle.phar/output1.php');" bundle.phar
echo bundle.phar.zip
..\..\runtime\php5\php.exe -n -r "require('phar://bundle.phar.zip/output1.php');" bundle.phar.zip
echo bundle.phar.tar.gr
..\..\runtime\php5\php.exe -n -r "require('phar://bundle.phar.tar.gz/output1.php');" bundle.phar.tar.gr
