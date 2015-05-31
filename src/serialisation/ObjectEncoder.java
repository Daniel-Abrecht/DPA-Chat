package serialisation;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * Interface für Serializer
 * 
 * @author Daniel Abrehct
 * @param <T> Zieltyp
 * @see serialisation.BinaryEncoder for futur details
 */
public interface ObjectEncoder<T> {
	public T encode(Object o);

	public List<T> encode(Object o, Class<?> c);

	public List<T> encodeField(Field f, Object o);

	public List<T> encodePrimitive(Class<?> c, Object o);

	public <R> R decode(byte[] o, Class<R> c);

	public <R> R decode(ByteBuffer buffer, Class<R> type);

	public <R> R decodePrimitive(Class<R> c, ByteBuffer buffer);

	public Object decodeField(Object root, Field f, ByteBuffer buffer);

	List<Field> getFields(Class<?> c);

	public Object getParameter(String value);
	public void setParameter(String key, Object value);

}
