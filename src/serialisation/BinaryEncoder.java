package serialisation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class BinaryEncoder implements ObjectEncoder<byte[]> {

	private Serializer serializer;

	public BinaryEncoder(Serializer serializer) {
		this.setSerializer(serializer);
	}

	@Override
	public byte[] encode(Object o) {
		List<byte[]> bl = encodeInternal(o);
		int n = 0;
		for (byte[] b : bl)
			n += b.length;
		byte[] buffer = new byte[n];
		int offset = 0;
		for (byte[] b : bl) {
			System.arraycopy(b, 0, buffer, offset, b.length);
			offset += b.length;
		}
		return buffer;
	}

	public List<byte[]> encodeInternal(Object o) {
		List<byte[]> bl = new ArrayList<byte[]>();
		List<Field> fl = serializer.getFields(o);
		Collections.sort(fl, new Comparator<Field>() {
			@Override
			public int compare(Field f1, Field f2) {
				return f1.getName().compareTo(f2.getName());
			}
		});
		for (Iterator<Field> iterator = fl.iterator(); iterator.hasNext();) {
			Field f = iterator.next();
			f.setAccessible(true);
			Object value = null;
			try {
				value = f.get(o);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			byte[] b = encode(f, value);
			if (b != null)
				bl.add(b);
			else
				bl.addAll(encodeInternal(value));
		}
		return bl;
	}

	private byte[] encode(Field f, Object value) {
		Expose e = f.getAnnotation(Expose.class);
		return null;
	}

	public Serializer getSerializer() {
		return serializer;
	}

	public void setSerializer(Serializer serializer) {
		this.serializer = serializer;
	}

}
