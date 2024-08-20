# ------------------------------------------------------------------ general ---
Unicode true
SetCompressor /solid lzma
RequestExecutionlevel highest

# -------------------------------------------------------------- definitions ---
!define PROGRAM "Στρατιωτικές Δαπάνες"
!define NAME "expenditure"
!define VERSION "2024.08.20"
!define EXTENSION ".δαπάνη"
!define ME "Γκέσος Παύλος (Σ.Σ.Ε. 2002)"
!define JAVA_VERSION "1.8"
!define JAVA_PATH "$PROGRAMFILES\java"
!define UNINSTALL_REG "Software\Microsoft\Windows\CurrentVersion\Uninstall\${NAME}"
!define INSTALLER_INFO "Πρόγραμμα συντάξεως στρατιωτικών δαπανών"
!define SHORTCUT_INFO "${INSTALLER_INFO}$\nΈκδοση: ${VERSION}$\nΠρογραμματιστής: ${ME}$\nΆδεια χρήσης: BSD"

# --------------------------------------------------------- function include ---
!include LogicLib.nsh
!include functions.nsh

# ------------------------------------------------------------------ general ---
Name "${PROGRAM} ${VERSION}"
InstallDir "$PROGRAMFILES\${PROGRAM}"
InstallDirRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${NAME}" "UninstallString"
Icon "${NAME}.ico"
UninstallIcon "${NAME}.ico"
OutFile "../${NAME}_${VERSION}.exe"


/* ----------------------------------------------------------- initialization ---
Έλεγχος ύπαρξης εγκαταστημένης Java από JDK 11 και πάνω (64 bit)
Επιστρέφεται
	$0 με το φάκελο εγκατάστασης του JDK, αν υπάρχει, ή αλλιώς κενό "".
	$1="1" αν ο χρήστης έχει δικαιώματα διαχειριστή ή $1="0" αν είναι απλός χρήστης, οπότε σε αυτή
	την περίπτωση τροποποιεί το φάκελο εγκατάστασης να δείχνει προς το φάκελο του χρήστη
*/
Function .onInit
	SetRegView 64				# Έλεγχος του 64-bit μητρώου
	ReadRegStr $1 HKLM "Software\JavaSoft\JDK" "CurrentVersion"
	StrCmp $1 "" javanotexist	# Το JDK 11 ή μεταγενέστερο, δεν είναι εγκατεστημένο
	# Έλεγχος της έκδοσης του εγκατεστημένου JDK με το απαιτούμενο από την εφαρμογή
	${VersionCompare} $1 "${JAVA_VERSION}" $2
	IntCmp $2 2 javanotexist	# Αν επιστρέψει 2, τότε η εγκατεστημένη Java είναι παλιά
	# Ο φάκελος εγκατάστασης της Java
	ReadRegStr $0 HKLM "Software\JavaSoft\JDK\$1" "JavaHome"
	Goto continue
javanotexist:
	StrCpy $0 ""
continue:
	Call IsAdmin				# Προσδιορισμός του τύπου του χρήστη και -ίσως- αλλαγή φακέλου εγκατάστασης
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
VIAddVersionKey /LANG=${LANG_GREEK} "FileDescription" "${INSTALLER_INFO}"
VIAddVersionKey /LANG=${LANG_GREEK} "LegalCopyright" "${ME}"
VIAddVersionKey /LANG=${LANG_GREEK} "FileVersion" "${VERSION}"

# ----------------------------------------------------- default installation ---
Section

	SetOutPath $INSTDIR
	File ..\dist\${NAME}.jar
	File /r ..\src\php
	File ..\bsd.txt
	File ..\history.txt
	
	SetOverwrite IfNewer
	File /r ..\runtime\php5
	${If} $0 == ""				# Η απαιτούμενη έκδοση Java δεν είναι εγκατεστημένη
	${If} $1 == "1"				# Ο χρήστης έχει δικαιώματα administrator
	SetOutPath "${JAVA_PATH}"	# Επειδή το JRE8 θέλει να τρέχει από φάκελο με αγγλικούς χαρακτήρες...
	${EndIf}
	File /r ..\runtime\jre8
	${EndIf}
	SetOverwrite on

	${If} $1 == "1"				# Ο χρήστης έχει δικαιώματα administrator
	SetShellVarContext all		# Εγκατάσταση για όλους τους χρήστες - επηρεάζει μητρώο και menu Έναρξη
	${EndIf}

	WriteRegStr SHCTX "${UNINSTALL_REG}" "DisplayName" "${PROGRAM}"
	WriteRegStr SHCTX "${UNINSTALL_REG}" "UninstallString" '"$INSTDIR\uninstall.exe"'
	WriteRegStr SHCTX "${UNINSTALL_REG}" "DisplayIcon" '"$INSTDIR\uninstall.exe"'
	WriteRegDWORD SHCTX "${UNINSTALL_REG}" "NoModify" 1
	WriteRegDWORD SHCTX "${UNINSTALL_REG}" "NoRepair" 1
	WriteRegStr SHCTX "${UNINSTALL_REG}" "DisplayVersion" "${VERSION}"
	WriteRegStr SHCTX "${UNINSTALL_REG}" "Publisher" "${ME}"
	WriteUninstaller "uninstall.exe"

	WriteRegStr SHCTX "Software\Classes\${EXTENSION}" "" "Αρχείο δαπάνης"
	WriteRegStr SHCTX "Software\Classes\${EXTENSION}\DefaultIcon" "" "$INSTDIR\uninstall.exe"
	WriteRegStr SHCTX "Software\Classes\${EXTENSION}\Shell" "" "άνοιγμα"
	${If} $0 == ""				# Η απαιτούμενη έκδοση Java δεν είναι εγκατεστημένη
		${If} $1 == "1"				# Ο χρήστης έχει δικαιώματα administrator
			WriteRegStr SHCTX "Software\Classes\${EXTENSION}\Shell\άνοιγμα\Command" "" '"${JAVA_PATH}\jre8\bin\javaw.exe" -jar "$INSTDIR\${NAME}.jar" "%1"'
			CreateShortCut "$SMPROGRAMS\${PROGRAM}.lnk" "${JAVA_PATH}\jre8\bin\javaw.exe" '-jar "$INSTDIR\${NAME}.jar"' "$INSTDIR\uninstall.exe" "" "" ALT|CONTROL|D "${SHORTCUT_INFO}"
		${Else}						# Ο χρήστης είναι απλός χρήστης
			WriteRegStr SHCTX "Software\Classes\${EXTENSION}\Shell\άνοιγμα\Command" "" '"$INSTDIR\jre8\bin\javaw.exe" -jar "$INSTDIR\${NAME}.jar" "%1"'
			CreateShortCut "$SMPROGRAMS\${PROGRAM}.lnk" "$INSTDIR\jre8\bin\javaw.exe" '-jar "$INSTDIR\${NAME}.jar"' "$INSTDIR\uninstall.exe" "" "" ALT|CONTROL|D "${SHORTCUT_INFO}"
		${EndIf}
	${Else}						# Η απαιτούμενη έκδοση Java είναι εγκατεστημένη
		WriteRegStr SHCTX "Software\Classes\${EXTENSION}\Shell\άνοιγμα\Command" "" '"$0\bin\javaw.exe" -jar "$INSTDIR\${NAME}.jar" "%1"'
		CreateShortCut "$SMPROGRAMS\${PROGRAM}.lnk" "$INSTDIR\${NAME}.jar" "" "$INSTDIR\uninstall.exe" "" "" ALT|CONTROL|D "${SHORTCUT_INFO}"
	${EndIf}
	
SectionEnd

# --------------------------------------------------------------------- help ---
Section 'Βοήθεια'

	SetOutPath $INSTDIR

	File /r ..\src\help

SectionEnd

# -------------------------------------------------------------- Uninstaller ---
Section "Uninstall"

	SetRegView 64
	Delete "$SMPROGRAMS\${PROGRAM}.lnk"
	DeleteRegKey SHCTX "${UNINSTALL_REG}"
	DeleteRegKey SHCTX "Software\Classes\${EXTENSION}"

	UserInfo::GetAccountType	# Προσδιορισμός του τύπου του χρήστη
	Pop $8
	${If} $8 == "Admin"			# Ο χρήστης έχει δικαιώματα administrator
		SetShellVarContext all	# Απεγκατάσταση για όλους τους χρήστες
		Delete "$SMPROGRAMS\${PROGRAM}.lnk"
		DeleteRegKey SHCTX "${UNINSTALL_REG}"
		DeleteRegKey SHCTX "Software\Classes\${EXTENSION}"
	${EndIf}

	IfFileExists $PROFILE\${NAME}.ini 0 +2
	MessageBox MB_YESNO|MB_ICONEXCLAMATION|MB_DEFBUTTON2 "Στο αρχείο ${NAME}.ini φυλάγονται όλα τα δεδομένα του προγράμματος.$\nΔεν προτείνεται να το διαγράψετε.$\nΘέλετε να το διαγράψω;" IDNO +2
	Delete $PROFILE\${NAME}.ini
	RMDir /r $INSTDIR
	RMDir /r "${JAVA_PATH}\jre8"
	
SectionEnd