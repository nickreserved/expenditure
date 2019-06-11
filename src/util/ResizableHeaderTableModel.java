package util;

import util.ResizableTableModel.TableRecord;

/** Το μοντέλο του πίνακα που αλλάζει αριθμό εγγραφών-γραμμών.
 * @param <T> Ο τύπος δεδομένων της εγγραφής-γραμμής του μοντέλου του πίνακα */
public abstract class ResizableHeaderTableModel<T extends TableRecord> extends ResizableTableModel<T> {
	/** Λίστα με τις επικεφαλίδες των στηλών του πίνακα. */
	private final String[] header;

	/** Δημιουργία του μοντέλου του πίνακα.
	 * @param headers Οι επικεφαλίδες των στηλών του πίνακα */
	public ResizableHeaderTableModel(String[] headers) { header = headers; }

	@Override public int getColumnCount() { return header.length; }
	@Override public String getColumnName(int col) { return header[col]; }
}