!define PROGRAM "PHP Command Line Interpreter"
!define SHORTNAME "PHP_cli"
!define VERSION "5.3.2.0"
!define ле "http://www.php.net"


Name "${PROGRAM} ${VERSION}"
OutFile "..\php_cli.exe"

VIProductVersion "${VERSION}"
VIAddVersionKey "ProductName" "${PROGRAM}"
VIAddVersionKey "FileDescription" "The Command Line Interpreter for PHP Scripts"
VIAddVersionKey "LegalCopyright" "${ле}"
VIAddVersionKey "FileVersion" "${VERSION}"

Section
	IfFileExists $WINDIR\php.ini 0 write
	IfFileExists $WINDIR\php.exe 0 write
	IfFileExists $WINDIR\php5ts.dll end write
write:	SetOutPath $WINDIR
	File c:\windows\php.exe
	File c:\windows\php5ts.dll
end:
SectionEnd
