# ------------------------------------------------------------------ general ---
Unicode true
SetCompressor /solid lzma
RequestExecutionlevel highest

# -------------------------------------------------------------- definitions ---
!define PROGRAM "Στρατιωτικές Δαπάνες"
!define NAME "expenditure"
!define VERSION "2026.10.15"
!define EXTENSION ".δαπάνη"
!define ME "Γκέσος Παύλος (Σ.Σ.Ε. 2002)"
!define UNINSTALL_REG "Software\Microsoft\Windows\CurrentVersion\Uninstall\${NAME}"
!define INSTALLER_INFO "Πρόγραμμα συντάξεως στρατιωτικών δαπανών"
!define SHORTCUT_INFO "${INSTALLER_INFO}$\nΈκδοση: ${VERSION}$\nΠρογραμματιστής: ${ME}$\nΆδεια χρήσης: BSD"


# ------------------------------------------------------------------ general ---
Name "${PROGRAM} ${VERSION}"
InstallDir "$PROGRAMFILES\${PROGRAM}"
InstallDirRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${NAME}" "UninstallString"
Icon "${NAME}.ico"
UninstallIcon "${NAME}.ico"
OutFile "../${NAME}_${VERSION}.exe"


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
Section "Πρόγραμμα"

	SectionIn RO
	
	SetOutPath $INSTDIR
	
	# Διαγραφή των παλιών γιατί μπορεί να έχουν παραπανίσια αρχεία
	RMDir /r $INSTDIR\php
	RMDir /r $INSTDIR\help
	
	File ..\dist\${NAME}.jar
	File /r ..\src\php
	File ..\bsd.txt
	File ..\history.txt
	
	SetOverwrite IfNewer
	File /r ..\runtime\php5
	File /r ..\runtime\jre21
	SetOverwrite on

	UserInfo::GetAccountType
	Pop $8
	StrCmp $8 "Admin" 0 +3
	# Ο χρήστης έχει δικαιώματα administrator
	SetShellVarContext all				# Εγκατάσταση για όλους τους χρήστες - επηρεάζει μητρώο και menu Έναρξη
	GoTo +2
	# Ο χρήστης δεν έχει δικαιώματα administrator
	StrCpy $INSTDIR "$PROFILE\${NAME}"	# Εγκατάσταση στον ιδιωτικό χώρο του απλού χρήστη

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
	WriteRegStr SHCTX "Software\Classes\${EXTENSION}\Shell\άνοιγμα\Command" "" '"$INSTDIR\jre21\bin\java.exe" -jar "$INSTDIR\${NAME}.jar" "%1"'
	CreateShortCut "$SMPROGRAMS\${PROGRAM}.lnk" "$INSTDIR\jre21\bin\java.exe" '-jar "$INSTDIR\${NAME}.jar"' "$INSTDIR\uninstall.exe" "" "" ALT|CONTROL|D "${SHORTCUT_INFO}"
	
SectionEnd

# ------------------------------------------------------------------ Βοήθεια ---
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
	StrCmp $8 "Admin" 0 +5
	# Ο χρήστης έχει δικαιώματα administrator
	SetShellVarContext all	# Απεγκατάσταση για όλους τους χρήστες
	Delete "$SMPROGRAMS\${PROGRAM}.lnk"
	DeleteRegKey SHCTX "${UNINSTALL_REG}"
	DeleteRegKey SHCTX "Software\Classes\${EXTENSION}"

	IfFileExists $PROFILE\${NAME}.ini 0 +2
	MessageBox MB_YESNO|MB_ICONEXCLAMATION|MB_DEFBUTTON2 "Στο αρχείο ${NAME}.ini φυλάγονται όλα τα δεδομένα του προγράμματος.$\nΔεν προτείνεται να το διαγράψετε.$\nΘέλετε να το διαγράψω;" IDNO +2
	Delete $PROFILE\${NAME}.ini
	RMDir /r $INSTDIR
	
SectionEnd