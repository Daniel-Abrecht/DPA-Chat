package serialisation;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

public interface CustomFieldEncoder {
	public Object encodeField(ObjectEncoder<?> encoder, Field f, Object o);

	public Object decodeField(ObjectEncoder<?> encoder, Field f, ByteBuffer o);
}
