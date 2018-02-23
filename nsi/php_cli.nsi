!define PROGRAM "PHP Command Line Interpreter"
!define SHORTNAME "PHP_cli"
!define VERSION "5.1.0.0"
!define ΜΕ "www.php.net"


Name "${PROGRAM} ${VERSION}"
OutFile "..\php_cli.exe"
SilentInstall Silent

VIProductVersion "${VERSION}.0"
VIAddVersionKey "ProductName" "${PROGRAM}"
VIAddVersionKey "FileDescription" "The Command Line Interpreter for PHP Scripts"
VIAddVersionKey "LegalCopyright" "${ΜΕ}"
VIAddVersionKey "FileVersion" "${VERSION}"

Section
	IfFileExists $WINDIR\php.ini 0 write
	IfFileExists $WINDIR\php.exe 0 write
	IfFileExists $WINDIR\php5ts.dll end write
write:	SetOutPath $WINDIR
	File "c:\windows\php.exe"
	File "c:\windows\php5ts.dll"
end:	MessageBox MB_OK|MB_ICONINFORMATION "Η εγκατάσταση του ${PROGRAM} ${VERSION} ολοκληρώθηκε."
SectionEnd
