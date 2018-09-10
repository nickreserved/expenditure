# -------------------------------------------------------------- definitions ---
!define PROGRAM "Στρατιωτικές Δαπάνες"
!define SHORTNAME "Cost"
!define VERSION "2018.2.25"
!define ME "Γκέσος Παύλος (Σ.Σ.Ε. 2002)"
!define JAVA_VERSION "1.8"
!define PHP_VERSION "5.6.15"
!define VER1 0x00050006
!define VER2 0x000F0000
!define PHP_RE_URL "https://sourceforge.net/projects/ha-expenditure/files/redistributable/x32/php_cli.exe/download"
!define JAVA_RE_URL "http://www.oracle.com/technetwork/java/javase/downloads/"

# --------------------------------------------------------- function include ---
!include functions.nsh

# ------------------------------------------------------------------ general ---
Name "${PROGRAM} ${VERSION}"
InstallDir "$PROGRAMFILES\Στρατιωτικές Δαπάνες"
InstallDirRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${SHORTNAME}" "UninstallString"
Icon "..\cost.ico"
UninstallIcon "..\cost.ico"
OutFile "..\cost_${VERSION}.exe"

# ----------------------------------------------------- check for php & java ---
Function .onInit
	# Check for PHP
	Call PHPstatus
	IntCmp 3 $0 phpend
	IfFileExists $EXEDIR\php_cli.exe 0 +3
	ExecWait '"$EXEDIR\php_cli.exe" /S'
	Goto phpend
	IntCmp  0 $0 0 phpend
	MessageBox MB_YESNO|MB_ICONEXCLAMATION "Για να λειτουργήσει το πρόγραμμα πρέπει να$\nκατεβάσετε το PHP Command Line Interpreter.$\nΘέλετε να το κατεβάσετε τώρα;" IDNO +2
	ExecShell "open" "${PHP_RE_URL}"
	Abort
phpend:

	# Check for Java
	SetRegView 32	# Check 32-bit registry
	ReadRegStr $1 HKLM "Software\JavaSoft\Java Runtime Environment" "CurrentVersion"
	StrCmp $1 "" 0 java32_8	# java 8 not exist
	ReadRegStr $1 HKLM "Software\JavaSoft\JRE" "CurrentVersion"
	StrCmp $1 "" java64	# java 9 not exist
java32_8:
	${VersionCompare} $1 "${JAVA_VERSION}" $2
	IntCmp $2 1 javaexists javaexists	# less or equal
java64:
	SetRegView 64	# Check 64-bit registry
	ReadRegStr $1 HKLM "Software\JavaSoft\Java Runtime Environment" "CurrentVersion"
	StrCmp $1 "" 0 java64_8	# java 8 not exist
	ReadRegStr $1 HKLM "Software\JavaSoft\JRE" "CurrentVersion"
	StrCmp $1 "" javanotexist	# java 9 not exist
java64_8:
	${VersionCompare} $1 "${JAVA_VERSION}" $2
	IntCmp $2 1 javaexists javaexists	# less or equal
	MessageBox MB_YESNO|MB_ICONEXCLAMATION "Έχετε εγκατεστημένο το Java Runtime Environment $1$\nκαι το πρόγραμμα απαιτεί το ${JAVA_VERSION} και πάνω.$\nΘέλετε να το κατεβάσετε τώρα;" IDNO +3 IDYES +2
javanotexist:
	MessageBox MB_YESNO|MB_ICONEXCLAMATION "Για να λειτουργήσει το πρόγραμμα πρέπει$\nνα κατεβάσετε το Java Runtime Environment ${JAVA_VERSION}.$\nΘέλετε να το κατεβάσετε τώρα;" IDNO +2
	ExecShell "open" "${JAVA_RE_URL}"
	Abort
javaexists:
	!define CSIDL_COMMONAPPDATA '0x23' ; Common Application Data path
	System::Call 'shell32::SHGetSpecialFolderPath(i $HWNDPARENT, t.r0, i ${CSIDL_COMMONAPPDATA}, i0) v'
	#ReadEnvStr $0 'ALLUSERSPROFILE'
	StrCpy $0 "$0\Oracle\Java\javapath\javaw.exe"
FunctionEnd

# -------------------------------------------------------------------- pages ---
Page components
Page directory
Page instfiles
UninstPage uninstConfirm
UninstPage instfiles

# --------------------------------------------------------------- properties ---
LoadLanguageFile "${NSISDIR}\Contrib\Language files\Greek.nlf"
VIProductVersion "${VERSION}.0"
VIAddVersionKey /LANG=${LANG_GREEK} "ProductName" "${PROGRAM}"
VIAddVersionKey /LANG=${LANG_GREEK} "FileDescription" "Πρόγραμμα για σύνταξη δικαιολογητικών στρατιωτικών δαπανών"
VIAddVersionKey /LANG=${LANG_GREEK} "LegalCopyright" "${ME}"
VIAddVersionKey /LANG=${LANG_GREEK} "FileVersion" "${VERSION}"

# ----------------------------------------------------- default installation ---
Section

	SetOutPath $INSTDIR

	File ..\dist\Cost.jar
	File /r ..\src\php
	File ..\*.txt
	File ..\cost.ico

	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${SHORTNAME}" "DisplayName" "${PROGRAM}"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${SHORTNAME}" "UninstallString" '"$INSTDIR\uninstall.exe"'
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${SHORTNAME}" "DisplayIcon" '"$INSTDIR\uninstall.exe"'
	WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${SHORTNAME}" "NoModify" 1
	WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${SHORTNAME}" "NoRepair" 1
	WriteUninstaller "uninstall.exe"

	WriteRegStr HKLM "Software\Classes\.cost" "" "Αρχείο δαπάνης"
	WriteRegStr HKLM "Software\Classes\.cost\DefaultIcon" "" "$INSTDIR\cost.ico"
	WriteRegStr HKLM "Software\Classes\.cost\Shell" "" "άνοιγμα"
	WriteRegStr HKLM "Software\Classes\.cost\Shell\άνοιγμα\Command" "" '"$0" -jar "$INSTDIR\cost.jar" "%1"'

SectionEnd

# --------------------------------------------------------------- start menu ---
Section 'Συντομεύσεις στο μενού "Έναρξη"'

	#CreateShortCut "$SMPROGRAMS\${PROGRAM}.lnk" "$0" "-jar $\"$INSTDIR\cost.jar$\"" "$INSTDIR\cost.ico" "" "" ALT|CONTROL|D "Πρόγραμμα συντάξεως στρατιωτικών δαπανών$\nΈκδοση: ${VERSION}$\nΠρογραμματιστής: ${ME}$\nΆδεια χρήσης: BSD"
	CreateShortCut "$SMPROGRAMS\${PROGRAM}.lnk" "$INSTDIR\cost.jar" "" "$INSTDIR\cost.ico" "" "" ALT|CONTROL|D "Πρόγραμμα συντάξεως στρατιωτικών δαπανών$\nΈκδοση: ${VERSION}$\nΠρογραμματιστής: ${ME}$\nΆδεια χρήσης: BSD"

SectionEnd

# -------------------------------------------------------------- source code ---
Section /o 'Πηγαίος Κώδικας'

	SetOutPath "$INSTDIR\source"

	File /r ..\src
	File /r ..\nbproject
	File /r ..\nsi
	File ..\build.xml

SectionEnd

# --------------------------------------------------------------------- help ---
Section 'Βοήθεια'

	SetOutPath $INSTDIR

	File /r ..\help

SectionEnd

# -------------------------------------------------------------- Uninstaller ---
Section "Uninstall"

	DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${SHORTNAME}"
	Delete "$SMPROGRAMS\${PROGRAM}.lnk"
	DeleteRegKey HKCR ".cost"

	IfFileExists $PROFILE\cost.ini 0 +2
	MessageBox MB_YESNO|MB_ICONEXCLAMATION|MB_DEFBUTTON2 "Στο αρχείο cost.ini φυλάγονται όλα τα δεδομένα του προγράμματος.$\nΔεν προτείνεται να το διαγράψετε.$\nΘέλετε να το διαγράψω;" IDNO +2
	Delete $PROFILE\cost.ini
	RMDir /r $INSTDIR

SectionEnd