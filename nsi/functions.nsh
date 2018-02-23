#Function Version2Long
#	Pop $1
	
#FunctionEnd


/* Ελέγχει αν υπάρχει εγκατεστημένο το PHP
Μετά την κλήση:
$0: 0: Δεν είναι εγκατεστημένο, 1: Θέλει update, 2: Θέλει update αλλά υπάρχει web server, 3: up-to-date
$1: Η παλιά έκδοση, αν υπάρχει
Καταστρέφει: $2, $3
*/
Function PHPstatus
	IfFileExists $WINDIR\php.exe 0 PHPstatusinstall
	IfFileExists $WINDIR\php5ts.dll 0 PHPstatusinstall	# Both must exist
	
	GetDllVersion '$WINDIR\php.exe' $1 $2				# Check version of php.exe
	IntCmpU ${VER1} $1 0 PHPstatusend +2
	IntCmpU ${VER2} $2 PHPstatusend PHPstatusend
	
	IntOp $0 $1 & 0xFFFF
	IntOp $1 $1 >> 16
	IntOp $3 $2 & 0xFFFF
	IntOp $2 $2 >> 16
	StrCpy $1 "$1.$0.$2.$3";

	IfFileExists $WINDIR\php.ini 0 PHPstatusupdate		# If php.ini exists, maybe a web server exist. Don't screw it.
	StrCpy $0 2
	Return
PHPstatusend:
	StrCpy $1 ''
	StrCpy $0 3
	Return
PHPstatusupdate:
	StrCpy $0 1
	Return
PHPstatusinstall:
	StrCpy $1 ''
	StrCpy $0 0
FunctionEnd