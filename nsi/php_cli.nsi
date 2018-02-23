!define PROGRAM "PHP Command Line Interpreter"
!define SHORTNAME "PHP_cli"
!define VERSION "5.6.15"
!define VER1 0x00050006
!define VER2 0x000F0000
!define ΜΕ "http://www.php.net"
!define VCREDIST_URL "http://www.microsoft.com/en-us/download/details.aspx?id=30679"

Name "${PROGRAM} ${VERSION}"
OutFile "..\php_cli.exe"

VIProductVersion "${VERSION}.0"
VIAddVersionKey "ProductName" "${PROGRAM}"
VIAddVersionKey "FileDescription" "The Command Line Interpreter for PHP Scripts"
VIAddVersionKey "LegalCopyright" "${ΜΕ}"
VIAddVersionKey "FileVersion" "${VERSION}"


!include functions.nsh


Function .onInit

	# Check if Visual C++ 2012 Redistributable (ver. 11) is installed
	ReadRegDWORD $1 HKLM "Software\Wow6432Node\Microsoft\VisualStudio\11.0\VC\Runtimes\x32" Installed
	StrCmp $1 1 redistexist
	ReadRegDWORD $1 HKLM "Software\Wow6432Node\Microsoft\VisualStudio\11.0\VC\Runtimes\x64" Installed
	StrCmp $1 1 redistexist
	ReadRegDWORD $1 HKLM "Software\Microsoft\VisualStudio\11.0\VC\Runtimes\x32" Installed
	StrCmp $1 1 redistexist

	# If Visual C++ 2012 Redistributable Installer is in the same directory with this installer
	IfFileExists $EXEDIR\vcredist_x86.exe 0 +3
	ExecWait '"$EXEDIR\vcredist_x86.exe" /quiet'
	Goto redistexist
	IfFileExists $EXEDIR\vcredist_x64.exe 0 redistdownload
	ExecWait '"$EXEDIR\vcredist_x64.exe" /quiet'
	Goto redistexist

redistdownload:
	MessageBox MB_YESNO|MB_ICONEXCLAMATION "Για να λειτουργήσει το PHP Command Line Interpreter$\nπρέπει να κατεβάσετε και εγκαταστήσετε$\nτο Microsoft Visual C++ 2012 Redistributable.$\nΘέλετε να το κατεβάσετε τώρα;" /SD IDNO IDNO redistabort
	ExecShell "open" "${VCREDIST_URL}"
	Abort

redistabort:
	Abort 'Microsoft Visual C++ 2012 Redistributable MUST be installed'

redistexist:

FunctionEnd


Section
	Call PHPstatus
	IntCmp $0 2 0 phpinstall phpend
	MessageBox MB_YESNO|MB_ICONEXCLAMATION "Βρέθηκε η έκδοση PHP Command Line Interpreter $1 και μπορεί να γίνει αναβάθμιση στην έκδοση ${VERSION}.$\n$\nΩστόσο υπάρχει υποψία ότι η παλαιότερη έκδοση χρησιμοποιείται από κάποιο web server. Αν ισχύει κάτι τέτοιο, μην προχωρήσετε σε αναβάθμιση.$\n$\nΑν πάλι ο υπολογιστής αυτός χρησιμοποιείται ΜΟΝΟ από εσάς και δεν έχετε εγκαταστήσει κάποιο web server ή δεν καταλαβαίνετε τίποτα από αυτά, προχωρήστε σε αναβάθμιση του PHP στην έκδοση ${VERSION}.$\n$\nΘέλετε να το αναβαθμίσετε;" /SD IDNO IDNO phpend

phpinstall:
	SetOutPath $WINDIR
	File ..\php5ts.dll
	File ..\php.exe

phpend:

SectionEnd
