# -------------------------------------------------------------- definitions ---
!define PROGRAM "Στρατιωτικές Δαπάνες"
!define SHORTNAME "Cost"
!define VERSION "1.4.2"
!define ME "Υπλγος(ΜΧ) Γκέσος Παύλος"
!define JAVA_RE_URL "http://www.java.com/"
!define JAVA_VERSION "1.5"
!define PHP_RE_URL "http://tassadar.physics.auth.gr/~chameleon/programs/cost/program/php_cli.exe"

# ------------------------------------------------------------------ general ---
Name "${PROGRAM} ${VERSION}"
OutFile "..\cost_${VERSION}.exe"
InstallDir "$PROGRAMFILES\Στρατιωτικές Δαπάνες"
InstallDirRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${SHORTNAME}" "UninstallString"
Icon "..\cost.ico"
UninstallIcon "..\cost.ico"

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
VIAddVersionKey /LANG=${LANG_GREEK} "LegalCopyright" "${ΜΕ}"
VIAddVersionKey /LANG=${LANG_GREEK} "FileVersion" "${VERSION}"

# ----------------------------------------------------- check for php & java ---
Function .onInit
	IfFileExists $WINDIR\php.exe 0 +3
	IfFileExists $WINDIR\php5ts.dll phpexists
	IfFileExists $WINDIR\php4ts.dll phpexists
	IfFileExists $EXEDIR\php_cli.exe 0 +3
	ExecWait '"$EXEDIR\php_cli.exe" /S'
	Goto phpexists
	MessageBox MB_YESNO|MB_ICONEXCLAMATION "Για να λειτουργήσει το πρόγραμμα πρέπει να$\nκατεβάσετε το PHP Command Line Interpreter.$\nΘέλετε να το κατεβάσετε τώρα;" IDNO +2
	ExecShell "open" "${PHP_RE_URL}"
	Abort
phpexists:

	ReadRegStr $1 HKLM "Software\JavaSoft\Java Runtime Environment" "CurrentVersion"
	StrCmp $1 "" javanotexist
	ReadRegStr $0 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$1" "JavaHome"
	StrCpy $0 "$0\bin\javaw.exe"
	StrCpy $2 $1 1
	StrCpy $3 $1 1 2
	StrCpy $4 "${JAVA_VERSION}" 1
	StrCpy $5 "${JAVA_VERSION}" 1 2
	IntCmp $2 $4 0 javaoldexists javaexists
	IntCmp $3 $5 0 javaoldexists
	Goto javaexists

javaoldexists:
	MessageBox MB_YESNO|MB_ICONEXCLAMATION "Έχετε εγκατεστημένο το Java Runtime Environment $1$\nκαι το πρόγραμμα απαιτεί το ${JAVA_VERSION} και πάνω.$\nΘέλετε να το κατεβάσετε τώρα;" IDNO +3 IDYES +2
javanotexist:
	MessageBox MB_YESNO|MB_ICONEXCLAMATION "Για να λειτουργήσει το πρόγραμμα πρέπει$\nνα κατεβάσετε το Java Runtime Environment ${JAVA_VERSION}.$\nΘέλετε να το κατεβάσετε τώρα;" IDNO +2
	ExecShell "open" "${JAVA_RE_URL}"
	Abort
javaexists:
FunctionEnd


# ----------------------------------------------------- default installation ---
Section

	SetOutPath $INSTDIR

	File ..\dist\Cost.jar
	File /r ..\dist\php
	File ..\*.txt
	File ..\cost.ico

	IfFileExists $INSTDIR\main.ini 0 +2
	MessageBox MB_YESNO|MB_ICONEXCLAMATION|MB_DEFBUTTON2 "Στο φάκελο εγκατάστασης βρέθηκε το παλιό σας αρχείο main.ini$\nΔεν προτείνεται να το διαγράψετε$\nΘέλετε να το διαγράψω;" IDNO +2
	File ..\dist\main.ini

	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${SHORTNAME}" "DisplayName" "${PROGRAM}"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${SHORTNAME}" "UninstallString" '"$INSTDIR\uninstall.exe"'
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${SHORTNAME}" "DisplayIcon" '"$INSTDIR\uninstall.exe"'
	WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${SHORTNAME}" "NoModify" 1
	WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${SHORTNAME}" "NoRepair" 1
	WriteUninstaller "uninstall.exe"

	WriteRegStr HKCR ".cost" "" "Αρχείο δαπάνης"
	WriteRegStr HKCR ".cost\DefaultIcon" "" "$INSTDIR\cost.ico"
	WriteRegStr HKCR ".cost\Shell" "" "άνοιγμα"
	WriteRegStr HKCR ".cost\Shell\άνοιγμα\Command" "" '"$0" -jar "$INSTDIR\cost.jar" "%1"'

SectionEnd

# --------------------------------------------------------------- start menu ---
Section 'Συντομεύσεις στο μενού "Έναρξη"'

	CreateShortCut "$SMPROGRAMS\${PROGRAM}.lnk" $SYSDIR\javaw.exe "-jar $\"$INSTDIR\cost.jar$\"" "$INSTDIR\cost.ico" "" "" ALT|CONTROL|D "Πρόγραμμα συντάξεως στρατιωτικών δαπανών$\nΈκδοση: ${VERSION}$\nΠρογραμματιστής: ${ME}$\nΆδεια χρήσης: GNU GPL"

SectionEnd

# -------------------------------------------------------------- source code ---
Section /o 'Πηγαίος Κώδικας'

	SetOutPath "$INSTDIR\source"

	File /r ..\src
	File /r ..\nbproject
	File /r ..\nsi
	File ..\manifest.mf
	File ..\build.xml

SectionEnd

# --------------------------------------------------------------------- help ---
Section 'Βοήθεια'

	SetOutPath $INSTDIR

	File /r ..\help

SectionEnd

# ------------------------------------------------------------------ scripts ---
Section 'Χρήσιμα Scripts'

	SetOutPath $INSTDIR

	File /r ..\scripts

SectionEnd

# -------------------------------------------------------------- Uninstaller ---

Section "Uninstall"

	DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${SHORTNAME}"
	Delete "$SMPROGRAMS\${PROGRAM}.lnk"
	DeleteRegKey HKCR ".cost"

	IfFileExists $INSTDIR\main.ini 0 +2
	MessageBox MB_YESNO|MB_ICONEXCLAMATION|MB_DEFBUTTON2 "Στο αρχείο main.ini φυλάγονται όλα τα δεδομένα του προγράμματος.$\nΔεν προτείνεται να το διαγράψετε.$\nΘέλετε να το διαγράψω;" IDNO +3
	RMDir /r $INSTDIR
	Goto end

	Delete "$INSTDIR\Cost.*"
	Delete "$INSTDIR\*.txt"
	Delete "$INSTDIR\*.exe"
	RMDir /r "$INSTDIR\php"
	RMDir /r "$INSTDIR\help"
	RMDir /r "$INSTDIR\source"
	RMDir /r "$INSTDIR\scripts"
end:
SectionEnd
