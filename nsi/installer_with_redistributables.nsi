!include installer_common.nsh
!include functions.nsh

OutFile "..\cost_${VERSION} with Java ${JAVA_VERSION}, PHP ${PHP_VERSION}.exe"


# ---------------------------------------------------------------------- php ---
Section '' section_php

	SetOutPath $INSTDIR

	File ..\php_cli.exe
	File ..\vcredist_x86.exe
	ExecWait '"$INSTDIR\php_cli.exe" /S'	# vcredist_x86.exe installed by php_cli.exe
	Delete "$INSTDIR\vcredist_x86.exe"
	Delete "$INSTDIR\php_cli.exe"

SectionEnd

# --------------------------------------------------------------------- java ---
Section '' section_java

	File /oname=$INSTDIR\jre.exe ..\jre.exe
	ExecWait '"$INSTDIR\jre.exe" /s'
	Delete "$INSTDIR\jre.exe"

SectionEnd


Function .onInit
	# Check for PHP
	Call PHPstatus
	StrCpy $2 "PHP cli ${PHP_VERSION}"
	IntCmp 0 $0 phpinstall
	IntCmp $0 3 phpuptodate
	SectionSetText ${section_php} "Αναβάθμιση σε $2 (από $1)"
	SectionSetFlags ${section_php} 1 # SF_SELECTED
	Goto phpend
phpuptodate:
	SectionSetFlags ${section_php} 16 # SF_RO
	Goto phpend
phpinstall:
	SectionSetText ${section_php} $2
	SectionSetFlags ${section_php} 17 # SF_SELECTED | SF_RO
phpend:

	# Check for Java
	SetRegView 32	# Check 32-bit registry
	ReadRegStr $1 HKLM "Software\JavaSoft\Java Runtime Environment" "CurrentVersion"
	StrCmp $1 "" java64	# javanotexist
	StrCpy $2 $1 1
	StrCpy $3 $1 1 2
	StrCpy $4 "${JAVA_VERSION}" 1
	StrCpy $5 "${JAVA_VERSION}" 1 2
	IntCmp $2 $4 0 javaoldexists javaexists
	IntCmp $3 $5 0 javaoldexists
	Goto javaexists
java64:
	SetRegView 64	# Check 64-bit registry
	ReadRegStr $1 HKLM "Software\JavaSoft\Java Runtime Environment" "CurrentVersion"
	StrCmp $1 "" javanotexist
	StrCpy $2 $1 1
	StrCpy $3 $1 1 2
	StrCpy $4 "${JAVA_VERSION}" 1
	StrCpy $5 "${JAVA_VERSION}" 1 2
	IntCmp $2 $4 0 javaoldexists javaexists
	IntCmp $3 $5 0 javaoldexists
	Goto javaexists

javaoldexists:
	SectionSetText ${section_java} "Αναβάθμιση σε Java RE ${JAVA_VERSION} (από $1)"
	SectionSetFlags ${section_java} 17 # SF_SELECTED | SF_RO
	Goto javaend
javanotexist:
	SectionSetText ${section_java} "Java RE ${JAVA_VERSION}"
	SectionSetFlags ${section_java} 17 # SF_SELECTED | SF_RO
	Goto javaend
javaexists:
	SectionSetFlags ${section_java} 16 # SF_RO
javaend:
	!define CSIDL_COMMONAPPDATA '0x23' ; Common Application Data path
	System::Call 'shell32::SHGetSpecialFolderPath(i $HWNDPARENT, t.r0, i ${CSIDL_COMMONAPPDATA}, i0) v'
	#ReadEnvStr $0 'ALLUSERSPROFILE'
	StrCpy $0 "$0\Oracle\Java\javapath\javaw.exe"
FunctionEnd