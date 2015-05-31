package serialisation;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

/**
 * Interface für Benutzerdefinierte (de)serialisier
 * 
 * @author Daniel Abrecht
 */
public interface CustomFieldEncoder {
	/**
	 * Serializer für Feld
	 * @param encoder Default encoder
	 * @param f Zu codierendes feld
	 * @param o Das Objekt, welches das Feld enthält
	 * @return Die kodierten daten
	 */
	public Object encodeField(ObjectEncoder<?> encoder, Field f, Object o);

	/**
	 * Deserializer für Feld
	 * @param encoder Default deencoder
	 * @param f Zu decodierendes feld
	 * @param o Zu decodierende Daten
	 * @return Das Decodierte Objekt
	 */
	public Object decodeField(ObjectEncoder<?> encoder, Field f, ByteBuffer o);
}
