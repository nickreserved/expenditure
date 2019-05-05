package util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/** Μετατρέπει δεδομένα Java σε κείμενο συμβατό με το serialization του PHP, και το αντίστροφο.
 * Η λειτουργικότητα αυτή χρησιμοποιείται για 2 λόγους:
 * <ul><li>Αποθηκεύει τα δεδομένα του προγράμματος σε ένα αρχείο κειμένου.
 * <li>Εξάγει μια δομή δεδομένων στο PHP, προκειμένου να εξαχθούν τα δικαιολογητικά της δαπάνης.</ul> */
final public class PhpSerializer {
	/** Εξάγει μια τιμή null.
	 * @param out To stream στο οποίο εξάγονται τα δεδομένα του serialization
	 * @throws IOException */
	static private void serializeNull(OutputStream out) throws IOException {
		new PhpSerializer(out, null).writeNull();
	}
	/** Εξάγει μια τιμή boolean.
	 * @param v Η τιμή
	 * @param out To stream στο οποίο εξάγονται τα δεδομένα του serialization
	 * @throws IOException */
	static private void serialize(boolean v, OutputStream out) throws IOException {
		new PhpSerializer(out, null).write(v);
	}
	/** Εξάγει έναν ακέραιο αριθμό.
	 * @param v Ο αριθμός
	 * @param out To stream στο οποίο εξάγονται τα δεδομένα του serialization
	 * @throws IOException */
	static private void serialize(long v, OutputStream out) throws IOException {
		new PhpSerializer(out, null).write(v);
	}
	/** Εξάγει έναν δεκαδικό αριθμό.
	 * @param v Ο αριθμός
	 * @param out To stream στο οποίο εξάγονται τα δεδομένα του serialization
	 * @throws IOException */
	static private void serialize(double v, OutputStream out) throws IOException {
		new PhpSerializer(out, null).write(v);
	}
	/** Εξάγει ένα κείμενο.
	 * @param v Το κείμενο
	 * @param out To stream στο οποίο εξάγονται τα δεδομένα του serialization
	 * @param charset Το σετ χαρακτήρων με το οποίο θα κωδικοποιηθεί το κείμενο στο stream
	 * @throws IOException */
	static private void serialize(String v, OutputStream out, Charset charset) throws IOException {
		new PhpSerializer(out, charset).write(v);
	}
	/** Εξάγει ένα αντικείμενο που υποστηρίζει php serialization.
	 * @param v Το αντικείμενο
	 * @param out To stream στο οποίο εξάγονται τα δεδομένα του serialization
	 * @param charset Το σετ χαρακτήρων με το οποίο θα κωδικοποιηθεί το κείμενο στο stream
	 * @throws IOException */
	static public void serialize(Serializable v, OutputStream out, Charset charset)
			throws IOException {
		new PhpSerializer(out, charset).write(v);
	}
	/** Εξάγει ένα αντικείμενο που υποστηρίζει php serialization.
	 * @param v Το αντικείμενο
	 * @param out To stream στο οποίο εξάγονται τα δεδομένα του serialization
	 * @param charset Το σετ χαρακτήρων με το οποίο θα κωδικοποιηθεί το κείμενο στο stream
	 * @throws IOException */
	static public void serialize(VariableSerializable v, OutputStream out, Charset charset)
			throws IOException {
		new PhpSerializer(out, charset).write(v);
	}
	/** Εξάγει ένα array από αντικείμενα που υποστηρίζουν php serialization.
	 * @param v Το array
	 * @param out To stream στο οποίο εξάγονται τα δεδομένα του serialization
	 * @param charset Το σετ χαρακτήρων με το οποίο θα κωδικοποιηθεί το κείμενο στο stream
	 * @throws IOException */
	static private void serialize(List<? extends Serializable> v, OutputStream out,
			Charset charset) throws IOException {
		new PhpSerializer(out, charset).write(v);
	}
	/** Εξάγει ένα array από αντικείμενα που υποστηρίζουν php serialization.
	 * @param v Το array
	 * @param out To stream στο οποίο εξάγονται τα δεδομένα του serialization
	 * @param charset Το σετ χαρακτήρων με το οποίο θα κωδικοποιηθεί το κείμενο στο stream
	 * @throws IOException */
	static private void serializeV(List<? extends VariableSerializable> v, OutputStream out,
			Charset charset) throws IOException {
		new PhpSerializer(out, charset).writeV(v);
	}

	/** Αρχικοποίηση του αντικειμένου.
	 * @param out To stream στο οποίο εξάγονται τα δεδομένα του serialization
	 * @param charset Το σετ χαρακτήρων με το οποίο θα κωδικοποιηθεί το κείμενο στο stream */
	private PhpSerializer(OutputStream out, Charset charset) {
		this.out = out; this.charset = charset;
	}

	/** To stream στο οποίο εξάγονται τα δεδομένα του serialization. */
	final private OutputStream out;
	/** Το σετ χαρακτήρων με το οποίο θα κωδικοποιηθεί το κείμενο στο stream. */
	final private Charset charset;

	/** Εξάγει μια τιμή null.
	 * @return this
	 * @throws IOException */
	private PhpSerializer writeNull() throws IOException { out.write('N'); out.write(';'); return this; }
	/** Εξάγει μια τιμή boolean.
	 * @param v Η τιμή
	 * @return this
	 * @throws IOException */
	private PhpSerializer write(boolean v) throws IOException {
		out.write('b'); out.write(':'); out.write(v ? '1' : '0'); out.write(';');
		return this;
	}
	/** Εξάγει έναν ακέραιο αριθμό.
	 * @param v Ο αριθμός
	 * @return this
	 * @throws IOException */
	private PhpSerializer write(long v) throws IOException {
		out.write('i'); out.write(':'); out.write(Long.toString(v).getBytes()); out.write(';');
		return this;
	}
	/** Εξάγει έναν δεκαδικό αριθμό.
	 * @param v Ο αριθμός
	 * @return this
	 * @throws IOException */
	private PhpSerializer write(double v) throws IOException {
		out.write('d'); out.write(':'); out.write(Double.toString(v).getBytes()); out.write(';');
		return this;
	}
	/** Εξάγει ένα κείμενο.
	 * @param v Το κείμενο
	 * @return this
	 * @throws IOException */
	private PhpSerializer write(String v) throws IOException {
		if (v == null) writeNull();
		else {
			out.write('s'); out.write(':');
			byte[] b = v.getBytes(charset);
			out.write(Long.toString(b.length).getBytes());
			out.write(':'); out.write('"');
			out.write(b);
			out.write('"'); out.write(';');
		}
		return this;
	}
	/** Εξάγει ένα αντικείμενο που υποστηρίζει php serialization.
	 * @param v Το αντικείμενο
	 * @return this
	 * @throws IOException */
	private PhpSerializer write(Serializable v) throws IOException {
		if (v == null) writeNull();
		else { v.serialize(this); out.write('}'); }
		return this;
	}
	/** Εξάγει ένα αντικείμενο που υποστηρίζει php serialization.
	 * @param v Το αντικείμενο
	 * @return this
	 * @throws IOException */
	private PhpSerializer write(VariableSerializable v) throws IOException {
		if (v == null) writeNull();
		else {
			VariableFields f = new VariableFields();
			v.serialize(f); f.write(this);
		}
		return this;
	}
	/** Εξάγει ένα array από αντικείμενα που υποστηρίζουν php serialization.
	 * @param v Το array
	 * @return this
	 * @throws IOException */
	private PhpSerializer write(List<? extends Serializable> v) throws IOException {
		if (v == null) writeNull();
		else {
			out.write('a'); out.write(':');
			out.write(Long.toString(v.size()).getBytes());
			out.write(':'); out.write('{');
			for (int z = 0; z < v.size(); ++z)
				write(z).write(v.get(z));
			out.write('}');
		}
		return this;
	}
	/** Εξάγει ένα array από αντικείμενα που υποστηρίζουν php serialization.
	 * @param v Το array
	 * @return this
	 * @throws IOException */
	private PhpSerializer writeV(List<? extends VariableSerializable> v) throws IOException {
		if (v == null) writeNull();
		else {
			out.write('a'); out.write(':');
			out.write(Long.toString(v.size()).getBytes());
			out.write(':'); out.write('{');
			for (int z = 0; z < v.size(); ++z)
				write(z).write(v.get(z));
			out.write('}');
		}
		return this;
	}

	/** Λειτουργικότητα εξαγωγής πεδίων από Serializable αντικείμενα.
	 * Ο αριθμός των πεδίων που θα εξαχθούν πρέπει να είναι από πριν γνωστός. */
	static final public class Fields {
		/** Ο μετατροπέας δεδομένων σε κείμενο. */
		final private PhpSerializer serializer;
		/** Αρχικοποίηση του αντικειμένου.
		 * @param serializer Ο μετατροπέας δεδομένων σε κείμενο
		 * @param fields Ο αριθμός των πεδίων του αντικειμένου που θα εξαχθούν
		 * @throws IOException */
		public Fields(PhpSerializer serializer, int fields) throws IOException {
			this.serializer = serializer;
			serializer.out.write('a'); serializer.out.write(':');
			serializer.out.write(Long.toString(fields).getBytes());
			serializer.out.write(':'); serializer.out.write('{');
		}
		/** Εξάγει μια τιμή boolean.
		 * @param field Το όνομα του πεδίου
		 * @param v Η τιμή
		 * @return this
		 * @throws IOException */
		public Fields write(String field, boolean v) throws IOException {
			serializer.write(field).write(v);
			return this;
		}
		/** Εξάγει έναν ακέραιο αριθμό.
		 * @param field Το όνομα του πεδίου
		 * @param v Ο αριθμός
		 * @return this
		 * @throws IOException */
		public Fields write(String field, long v) throws IOException {
			serializer.write(field).write(v);
			return this;
		}
		/** Εξάγει έναν δεκαδικό αριθμό.
		 * @param field Το όνομα του πεδίου
		 * @param v Ο αριθμός
		 * @return this
		 * @throws IOException */
		public Fields write(String field, double v) throws IOException {
			serializer.write(field).write(v);
			return this;
		}
		/** Εξάγει ένα κείμενο.
		 * @param field Το όνομα του πεδίου
		 * @param v Το κείμενο
		 * @return this
		 * @throws IOException */
		public Fields write(String field, String v) throws IOException {
			serializer.write(field).write(v);
			return this;
		}
		/** Εξάγει ένα αντικείμενο που υποστηρίζει php serialization.
		 * @param field Το όνομα του πεδίου
		 * @param v Το αντικείμενο
		 * @return this
		 * @throws IOException */
		public Fields write(String field, Serializable v) throws IOException {
			serializer.write(field).write(v);
			return this;
		}
		/** Εξάγει ένα αντικείμενο που υποστηρίζει php serialization.
		 * @param field Το όνομα του πεδίου
		 * @param v Το αντικείμενο
		 * @return this
		 * @throws IOException */
		public Fields write(String field, VariableSerializable v) throws IOException {
			serializer.write(field).write(v);
			return this;
		}
		/** Εξάγει ένα array από αντικείμενα που υποστηρίζουν php serialization.
		 * @param field Το όνομα του πεδίου
		 * @param v Το array
		 * @return this
		 * @throws IOException */
		public Fields write(String field, List<? extends Serializable> v) throws IOException {
			serializer.write(field).write(v);
			return this;
		}
		/** Εξάγει ένα array από αντικείμενα που υποστηρίζουν php serialization.
		 * @param field Το όνομα του πεδίου
		 * @param v Το array
		 * @return this
		 * @throws IOException */
		public Fields writeV(String field, List<? extends VariableSerializable> v) throws IOException {
			serializer.write(field).writeV(v);
			return this;
		}
	}

	/** Μετατρέπει ένα αντικείμενο java σε php serialize string format. */
	public interface Serializable {
		/** Μετατρέπει τα πεδία ενός αντικειμένου σε κείμενο.
		 * @param export Η μηχανή του php serialize στην οποία εξάγονται τα ονόματα πεδίων του
		 * αντικειμένου και οι τιμές τους
		 * @throws IOException */
		void serialize(PhpSerializer export) throws IOException;
	}

	/** Λειτουργικότητα εξαγωγής πεδίων από VariableSerializable αντικείμενα.
	 * Ο αριθμός των πεδίων που θα εξαχθούν, δεν απαιτείται να είναι από πριν γνωστός. */
	static final public class VariableFields {
		/** Λίστα με όλα τα πεδία του αντικειμένου για εξαγωγή.
		 * Αφού συγκεντρωθούν όλα, πλέον είναι γνωστός ο αριθμός τους. */
		final private ArrayList<Field> fields = new ArrayList<>(30);
		/** Εξάγει το αντικείμενο.
		 * @param export Η μηχανή του php serialize */
		private void write(PhpSerializer export) throws IOException {
			export.out.write('a'); export.out.write(':');
			export.out.write(Long.toString(fields.size()).getBytes());
			export.out.write(':'); export.out.write('{');
			for(Field field : fields)
				field.serialize(export);
			export.out.write('}');
		}
		/** Προσθέτει ένα πεδίο boolean στη λίστα πεδίων για εξαγωγή.
		 * @param field Το όνομα του πεδίου
		 * @param v Η τιμή */
		public void add(String field, boolean v) {
			fields.add((PhpSerializer export) -> export.write(field).write(v));
		}
		/** Προσθέτει ένα πεδίο ακέραιου αριθμού στη λίστα πεδίων για εξαγωγή.
		 * @param field Το όνομα του πεδίου
		 * @param v Ο αριθμός */
		public void add(String field, long v) {
			fields.add((PhpSerializer export) -> export.write(field).write(v));
		}
		/** Προσθέτει ένα πεδίο δεκαδικού αριθμού στη λίστα πεδίων για εξαγωγή.
		 * @param field Το όνομα του πεδίου
		 * @param v Ο αριθμός */
		public void add(String field, double v) {
			fields.add((PhpSerializer export) -> export.write(field).write(v));
		}
		/** Προσθέτει ένα πεδίο κειμένου στη λίστα πεδίων για εξαγωγή.
		 * @param field Το όνομα του πεδίου
		 * @param v Το κείμενο */
		public void add(String field, String v) {
			fields.add((PhpSerializer export) -> export.write(field).write(v));
		}
		/** Προσθέτει ένα πεδίο αντικειμένου που υποστηρίζει php serialization στη λίστα πεδίων για εξαγωγή.
		 * @param field Το όνομα του πεδίου
		 * @param v Το αντικείμενο */
		public void add(String field, Serializable v) {
			fields.add((PhpSerializer export) -> export.write(field).write(v));
		}
		/** Προσθέτει ένα πεδίο αντικειμένου που υποστηρίζει php serialization στη λίστα πεδίων για εξαγωγή.
		 * @param field Το όνομα του πεδίου
		 * @param v Το αντικείμενο */
		public void add(String field, VariableSerializable v) {
			fields.add((PhpSerializer export) -> export.write(field).write(v));
		}
		/** Προσθέτει ένα πεδίο array από αντικείμενα που υποστηρίζουν php serialization στη λίστα πεδίων για εξαγωγή.
		 * @param field Το όνομα του πεδίου
		 * @param v Το array */
		public void add(String field, List<? extends Serializable> v) {
			fields.add((PhpSerializer export) -> export.write(field).write(v));
		}
		/** Προσθέτει ένα πεδίο array από αντικείμενα που υποστηρίζουν php serialization στη λίστα πεδίων για εξαγωγή.
		 * @param field Το όνομα του πεδίου
		 * @param v Το array */
		public void addV(String field, List<? extends VariableSerializable> v) {
			fields.add((PhpSerializer export) -> export.write(field).writeV(v));
		}

		/** Εξάγει ένα πεδίο με την τιμή του σε κείμενο. */
		public interface Field {
			/** Εξάγει ένα πεδίο με την τιμή του σε κείμενο.
			 * @param export Η μηχανή του php serialize
			 * @throws IOException */
			void serialize(PhpSerializer export) throws IOException;
		}
	}

	/** Μετατρέπει το αντικείμενο java σε php serialize string format. */
	public interface VariableSerializable {
		/** Καθορίζει τα πεδία του αντικειμένου που θα εξαχθούν σε php serialize string format.
		 * @param fields Διαχειριστής των πεδίων του αντικεμένου για εξαγωγή. Όταν το αντικείμενο
		 * θέλει να επιλέξει ένα πεδίο για εξαγωγή, χρησιμοποιεί την VariableFields.add(). */
		void serialize(VariableFields fields);
	}


	// ================= UNSERIALIZE STUFF =========================================================


	/** Τροποποιεί το ArrayList<Node> ώστε να προστατεύει από out of bounds exception. */
	static private final class Array extends ArrayList<Node> {
		@Override public Node get(int index) {
			return index < this.size() ? super.get(index) : ABSENT_NODE;
		}
	}

	/** Ένας node δεδομένων, ο οποίος μας προστατεύει από bad casts και null pointers κατά τη μετατροπη στα πραγματικά δεδομένα.
	 * Κάνοντας χρήση π.χ. του node.getField("fieldName").getField("fieldName2").isExist(), γνωρίζουμε
	 * αν το συγκεκριμένο πεδίο υπάρχει, ενώ στην πραγματικότητα, αν δεν υπήρχε το πεδίο 'fieldName'
	 * θα είχαμε NullPointerException. Με τον τρόπο αυτό, αποφεύγουμε κατά πολύ τον επιπλέον κώδικα. */
	public interface Node {
		/** Ο node υπάρχει.
		 * @return Ο node υπάρχει */
		default boolean isExist() { return true; }
		/** Ο node είναι null.
		 * @return Ο node είναι null */
		default boolean isNull() { return false; }
		/** Ο node είναι boolean.
		 * @return Ο node είναι boolean */
		default boolean isBoolean() { return false; }
		/** Ο node είναι ακέραιος.
		 * @return Ο node είναι ακέραιος */
		default boolean isInteger() { return false; }
		/** Ο node είναι δεκαδικός αριθμός.
		 * @return Ο node είναι δεκαδικός */
		default boolean isDecimal() { return false; }
		/** Ο node είναι κείμενο.
		 * @return Ο node είναι κείμενο */
		default boolean isString() { return false; }
		/** Ο node είναι array.
		 * @return Ο node είναι array */
		default boolean isArray() { return false; }
		/** Ο node είναι αντικείμενο με πεδία.
		 * @return Ο node είναι αντικείμενο */
		default boolean isObject() { return false; }
		/** Επιστρέφει την τιμή boolean του node, ή αν η μετατροπή δεν είναι εφικτή, επιστρέφει false.
		 * @return Η τιμή boolean */
		default boolean getBoolean() { return false; }
		/** Επιστρέφει τον ακέραιο αριθμό του node, ή αν η μετατροπή δεν είναι εφικτή, επιστρέφει 0.
		 * @return Ο αριθμός */
		default long getInteger() { return 0; }
		/** Επιστρέφει τον δεκαδικό αριθμό του node, ή αν η μετατροπή δεν είναι εφικτή, επιστρέφει 0.
		 * @return Ο αριθμός */
		default double getDecimal() { return 0; }
		/** Επιστρέφει το κείμενο του node, ή αν η μετατροπή δεν είναι εφικτή, επιστρέφει null.
		 * @return Το κείμενο */
		default String getString() { return null; }
		/** Επιστρέφει το array του node, ή αν ο node δεν είναι array, επιστρέφει ένα κενό array.
		 * @return Το array του node, το οποίο είναι ένα array από nodes */
		default List<Node> getArray() { return new Array(); }
		/** Επιστρέφει το πεδίο του αντικειμένου του node.
		 * @param field Το όνομα του πεδίου του αντικειμένου
		 * @return Το πεδίο του αντικειμένου του node, ή αν ο node δεν είναι αντικείμενο, ή το πεδίο
		 * δεν υπάρχει στο αντικείμενο, επιστρέφει έναν κενό node. */
		default Node getField(String field) { return ABSENT_NODE; }
		/** Επιστρέφει τα ονόματα των πεδίων του αντικειμένου του node.
		 * @return Τα ονόματα των πεδίων του αντικειμένου του node, ή αν ο node δεν είναι
		 * αντικείμενο, επιστρέφει έναν κενό array. */
		default String[] getFieldNames() { return new String[0]; }
	}

	/** Node που αντιστοιχεί σε δεδομένα που δεν υπάρχουν. */
	static private final AbsentNode ABSENT_NODE = new AbsentNode();
	/** Node που αντιστοιχεί σε null. */
	static private final NullNode NULL_NODE = new NullNode();
	/** Node που αντιστοιχεί σε boolean true. */
	static private final BooleanNode TRUE_NODE = new BooleanNode(true);
	/** Node που αντιστοιχεί σε boolean false. */
	static private final BooleanNode FALSE_NODE = new BooleanNode(false);

	/** Node που αντιστοιχεί σε δεδομένα που δεν υπάρχουν. */
	static private class AbsentNode implements Node {
		@Override public boolean isExist() { return false; }
	}

	/** Node που αντιστοιχεί σε null. */
	static private class NullNode implements Node {
		@Override public boolean isNull() { return true; }
	}

	/** Node που αντιστοιχεί σε boolean. */
	static private class BooleanNode implements Node {
		BooleanNode(boolean l) { val = l; }
		boolean val;
		@Override public boolean isBoolean() { return true; }
		@Override public boolean getBoolean() { return val; }
		@Override public long getInteger() { return val ? 1 : 0; }
	}

	/** Node που αντιστοιχεί σε ακέραιο αριθμό. */
	static private class IntegerNode implements Node {
		IntegerNode(long l) { val = l; }
		/** Ο ακέραιος αριθμός. */
		long val;
		@Override public boolean isInteger() { return true; }
		@Override public boolean isDecimal() { return true; }
		@Override public boolean getBoolean() { return val != 0; }
		@Override public long getInteger() { return val; }
		@Override public double getDecimal() { return val; }
		@Override public String getString() { return Long.toString(val); }
	}

	/** Node που αντιστοιχεί σε δεκαδικό αριθμό. */
	static private class DecimalNode implements Node {
		DecimalNode(double d) { val = d; }
		/** Ο δεκαδικός αριθμός. */
		double val;
		@Override public boolean isDecimal() { return true; }
		@Override public boolean getBoolean() { return val != 0; }
		@Override public long getInteger() { return (long) val; }
		@Override public double getDecimal() { return val; }
		@Override public String getString() { return Double.toString(val); }
	}

	/** Node που αντιστοιχεί σε κείμενο. */
	static private class StringNode implements Node {
		StringNode(String s) { val = s; }
		/** Το κείμενο. */
		String val;
		@Override public boolean isString() { return true; }
		@Override public long getInteger() { return Long.parseLong(val); }
		@Override public double getDecimal() { return Double.parseDouble(val); }
		@Override public String getString() { return val; }
	}

	/** Node που αντιστοιχεί σε array. */
	static private class ArrayNode implements Node {
		ArrayNode(Array a) { val = a; }
		/** Το array. */
		Array val;
		@Override public boolean isArray() { return true; }
		@Override public ArrayList<Node> getArray() { return val; }
	}

	/** Node που αντιστοιχεί σε αντικείμενο. */
	static private class ObjectNode implements Node {
		ObjectNode(TreeMap<String, Node> a) { val = a; }
		/** Το αντικείμενο είναι ένα map, με τα πεδία να είναι τα κλειδιά του map. */
		TreeMap<String, Node> val;
		@Override public boolean isArray() { return val.isEmpty(); }	// Object χωρίς πεδία είναι και array
		@Override public boolean isObject() { return true; }
		@Override public Node getField(String field) { return val.getOrDefault(field, ABSENT_NODE); }
		@Override public String[] getFieldNames() { return val.keySet().toArray(new String[0]); }
	}

	/** Μετατρέπει ένα php serialized κείμενο σε ένα αντικείμενο από το οποίο μπορούν να ανακτηθούν τα δεδομένα.
	 * <ul><li>Αν τα δεδομένα είναι null επιστρέφει null.
	 * <li>Αν τα δεδομένα είναι ακέραιος αριθμός, επιστρέφει Long.
	 * <li>Αν τα δεδομένα είναι δεκαδικός αριθμός, επιστρέφει Double.
	 * <li>Αν τα δεδομένα είναι κείμενο, επιστρέφει String.
	 * <li>Αν τα δεδομένα είναι array με διαδοχικούς ακέραιους για κλειδιά, τότε επιστρέφει
	 * ArrayList με στοιχεία, οποιονδήποτε από τους τύπους δεδομένων που αναφέρονται εδώ.
	 * <li>Αν τα δεδομένα είναι array με κείμενο για κλειδιά, τότε επιστρέφει TreeMap<String, Object>
	 * με τιμές, οποιονδήποτε από τους τύπους δεδομένων που αναφέρονται εδώ.</ul>
	 * @param is Το stream εισόδου που περιέχει το κείμενο από το οποίο θα εξαχθούν τα δεδομένα
	 * @param charset Η ομάδα χαρακτήρων για τη μετατροπή των bytes σε χαρακτήρες
	 * @return Μια ιεραρχική δομή δεδομένων από την οποία θα εξαχθούν τα πραγματικά δεδομένα
	 * @throws FormatException Αν το κείμενο δεν είναι συμβατό με το πρότυπο του php serialize()
	 * @throws IOException Αν υπάρξει πρόβλημα εισόδου (π.χ. χαλασμένο αρχείο) */
	static public Node unserialize(InputStream is, Charset charset) throws FormatException, IOException {
		switch(is.read()) {
			case 'N':	// null
				if (is.read() != ';') throw new FormatException("Parsing null: ';' expected");
				return NULL_NODE;
			case 'b': {	// boolean
				BooleanNode n;
				if (is.read() != ':') throw new FormatException("Parsing boolean: ':' expected");
				int r = is.read();
				switch(r) {
					case '0': n = FALSE_NODE; break;
					case '1': n = TRUE_NODE; break;
					default: throw new FormatException("Parsing boolean: '0' or '1' expected");
				}
				if (is.read() != ';') throw new FormatException("Parsing boolean: ';' expected");
				return n;
			}
			case 'i': {	// integer
				if (is.read() != ':') throw new FormatException("Parsing integer: ':' expected");
				String s = parseUntil(is, ';');
				try { return new IntegerNode(Long.parseLong(s)); }
				catch(NumberFormatException ex) { throw new FormatException("Bad integer format: " + s, ex); }
			}
			case 'd': {	// decimal
				if (is.read() != ':') throw new FormatException("Parsing decimal: ':' expected");
				String s = parseUntil(is, ';');
				try { return new DecimalNode(Double.parseDouble(s)); }
				catch(NumberFormatException ex) { throw new FormatException("Bad decimal format: " + s, ex); }
			}
			case 's': {	// string
				if (is.read() != ':') throw new FormatException("Parsing string: ':' expected");
				int size = parseSize(is);
				if (is.read() != '"') throw new FormatException("Parsing string: '\"' expected");
				byte[] ar = new byte[size];
				if (size != is.read(ar)) throw new FormatException("Parsing string: " + size + " bytes expected");
				// Διαδοχική εκτέλεση στην παρακάτω γραμμή, οπότε δεν υπάρχει πρόβλημα με το ποιο
				// byte θα διαβαστεί πρώτο (δεν ισχύει το ίδιο στη C++, γι' αυτό και το αναφέρω)
				if (is.read() != '"' || is.read() != ';') throw new FormatException("Parsing string: '\";' expected");
				return new StringNode(new String(ar, charset));
			}
			case 'a': {	// array
				Array a = new Array();
				TreeMap<String, Node> map = new TreeMap<>();
				if (is.read() != ':') throw new FormatException("Parsing array: ':' expected");
				int size = parseSize(is);
				if (is.read() != '{') throw new FormatException("Parsing array: '{' expected");
				for (int z = 0; z < size; ++z) {
					Node key = unserialize(is, charset);
					Node value = unserialize(is, charset);
					if (key.isString()) {
						if (!a.isEmpty()) throw new FormatException("Parsing array: mixed string/integer keys");
						map.put(key.getString(), value);
					} else if (key.isInteger()) {
						if (!map.isEmpty()) throw new FormatException("Parsing array: mixed string/integer keys");
						if (key.getInteger() != a.size()) throw new FormatException("Parsing array: sparse array");
						a.add(value);
					} else throw new FormatException("Parsing array: Array key must be integer or string");
				}
				if (is.read() != '}') throw new FormatException("Parsing array: '}' expected");
				return a.isEmpty() ? new ObjectNode(map) : new ArrayNode(a);
			}
			default: throw new FormatException("Parsing: 'N', 'b', 'i', 'd', 's' or 'a' expected");
		}
	}

	/** Διαβάζει το μέγεθος από ένα array ή ένα string.
	 * @param is Το stream εισόδου που περιέχει το κείμενο από το οποίο θα εξαχθούν τα δεδομένα
	 * @return Το μέγεθος
	 * @throws FormatException Αν το κείμενο δεν είναι συμβατό με το πρότυπο του php serialize()
	 * @throws IOException Αν υπάρξει πρόβλημα εισόδου (π.χ. χαλασμένο αρχείο) */
	static private int parseSize(InputStream is) throws IOException, FormatException {
		String s = parseUntil(is, ':');
		try {
			int a = Integer.parseInt(s);
			if (a < 0) throw new FormatException("Negative size format: " + s);
			return a;
		} catch(NumberFormatException ex) { throw new FormatException("Bad size format: " + s, ex); }
	}

	/** Διαβάζει κείμενο, μέχρι να συναντήσει το χαρακτήρα τερματισμού.
	 * @param is Το stream εισόδου που περιέχει το κείμενο από το οποίο θα εξαχθούν τα δεδομένα
	 * @param finishChar Ο χαρακτήρας τερματισμού
	 * @return Το κείμενο μέχρι το χαρακτήρα τερματισμού, δίχως αυτόν
	 * @throws FormatException Αν το κείμενο δεν είναι συμβατό με το πρότυπο του php serialize()
	 * @throws IOException Αν υπάρξει πρόβλημα εισόδου (π.χ. χαλασμένο αρχείο) */
	static private String parseUntil(InputStream is, int finishChar) throws IOException, FormatException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(50);
		for (int c = is.read(); c != finishChar; c = is.read())
			if (c != -1) bos.write(c);
			else throw new FormatException("Parsing number: '" + finishChar + "' expected");
		return bos.toString();
	}

	/** Σφάλμα, όταν το κείμενο για το unserialize() δεν είναι συμβατό με το πρότυπο PHP serialize(). */
	static public class FormatException extends Exception {
		/** Αρχικοποίηση του σφάλματος.
		 * @param reason Φιλικό κείμενο με το λόγο του σφάλματος */
		FormatException(String reason) { super(reason); }
		/** Αρχικοποίηση του σφάλματος.
		 * @param reason Φιλικό κείμενο με το λόγο του σφάλματος
		 * @param throwable Ένα προηγούμενο σφάλμα που οδήγησε σε αυτό */
		FormatException(String reason, Throwable throwable) { super(reason, throwable); }
	}
}
