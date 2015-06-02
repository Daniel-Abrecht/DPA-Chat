package serialisation;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * Interface für Serializer
 * 
 * @author Daniel Abrehct
 * @param <T> Zieltyp
 */
public interface ObjectEncoder<T> {
	/**
	 * Serialiser für unbekannte Objekte
	 * @param o das Objekt
	 */
	public T encode(Object o);

	/**
	 * Serialiser für objekte mit vorgegebem typ
	 * @param o Das Objekt
	 * @param type Typ des Objekts
	 */
	public List<T> encode(Object o, Class<?> c);

	/**
	 * Serializer für ein Feld einer Klasse
	 * @param f Feld der Klasse
	 * @param root Objekt, welches das Feld enthält
	 */
	public List<T> encodeField(Field f, Object o);

	/**
	 * Serializer für Primitive Datentypen
	 * @param c Datentyp
	 * @param value Das Objekt
	 */
	public List<T> encodePrimitive(Class<?> c, Object o);

	public <R> R decode(byte[] o, Class<R> c);

	/**
	 * Deserializer für Objekte vom typ type
	 * @param type zieltyp
	 * @param o Daten
	 */
	public <R> R decode(ByteBuffer buffer, Class<R> type);

	/**
	 * Deserializer für primitive Datentypen
	 * @param c Zieltyp
	 * @param buffer daten
	 */
	public <R> R decodePrimitive(Class<R> c, ByteBuffer buffer);

	/**
	 * Deserializer für das Feld eines Objekts
	 * @param root Classe, die das Feld enthalt
	 * @param f zu decodierendes Feld
	 * @param buffer Zu decodierende daten
	 */
	public Object decodeField(Object root, Field f, ByteBuffer buffer);

	/**
	 * Getter zur Emittlung der Felder einer Klasse
	 * @param c Die Klasse
	 * @return Eine Liste aller Felder einer Klasse
	 */
	public List<Field> getFields(Class<?> c);

	/**
	 * Getter für Parameter
	 */
	public Object getParameter(String value);

	/**
	 * Setter für Parameter
	 */
	public void setParameter(String key, Object value);

}
