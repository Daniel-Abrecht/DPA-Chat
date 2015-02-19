package chat.resources;

import java.lang.reflect.Field;
import java.util.zip.Checksum;
import java.util.zip.CRC32;

import serialisation.Expose;

public class HashCalculator {

	private int calcHash(Object o, Checksum ch, int off) {
		if (o == null)
			return 0;
		if (o instanceof Byte) {
			final int len = Byte.BYTES;
			ch.update(toBytes(((Byte) o).byteValue()), off, len);
			off += len;
		} else if (o instanceof Short) {
			final int len = Short.BYTES;
			ch.update(toBytes(((Short) o).shortValue()), off, len);
			off += len;
		} else if (o instanceof Integer) {
			final int len = Integer.BYTES;
			ch.update(toBytes(((Integer) o).intValue()), off, len);
			off += len;
		} else if (o instanceof Long) {
			final int len = Long.BYTES;
			ch.update(toBytes(((Long) o).longValue()), off, len);
			off += len;
		} else if (o instanceof Character) {
			short x = (short) (char) o;
			return calcHash(x, ch, off);
		} else if (o instanceof String) {
			byte[] by = ((String) o).getBytes();
			return calcHash(by, ch, off);
		} else if (o instanceof byte[]) {
			byte[] by = (byte[]) o;
			ch.update(toBytes(((Long) o).longValue()), off, by.length);
			off += by.length;
		} else if (o instanceof Object) {
			Object[] ob = (Object[]) o;
			for (int i = 0; i < ob.length; i++) {
				off += calcHash(ob[i], ch, off);
			}
		} else {
			Class<?> c = o.getClass();
			do {
				Field[] fields = c.getDeclaredFields();
				for (int i = 0; i < fields.length; i++) {
					Field field = fields[i];
					Expose e = field.getAnnotation(Expose.class);
					if (e == null)
						continue;
					field.setAccessible(true);
					try {
						Object ob = field.get(o);
						off += calcHash(ob, ch, off);
					} catch (IllegalArgumentException | IllegalAccessException e1) {
						e1.printStackTrace();
					}
				}
			} while ((c = c.getSuperclass()) != null);
		}
		return off;
	}

	private byte[] toBytes(byte value) {
		return new byte[] { value };
	}

	private byte[] toBytes(short value) {
		return new byte[] { (byte) ((value >> 8) & 0xFF),
				(byte) ((value >> 0) & 0xFF) };
	}

	private byte[] toBytes(int value) {
		return new byte[] { (byte) ((value >> 24) & 0xFF),
				(byte) ((value >> 16) & 0xFF), (byte) ((value >> 8) & 0xFF),
				(byte) ((value >> 0) & 0xFF) };
	}

	private byte[] toBytes(long value) {
		return new byte[] { (byte) ((value >> 56) & 0xFF),
				(byte) ((value >> 48) & 0xFF), (byte) ((value >> 40) & 0xFF),
				(byte) ((value >> 32) & 0xFF), (byte) ((value >> 24) & 0xFF),
				(byte) ((value >> 16) & 0xFF), (byte) ((value >> 8) & 0xFF),
				(byte) ((value >> 0) & 0xFF) };
	}

	public byte[] calcHash(Object o) {
		CRC32 ch = new CRC32();
		calcHash(o, ch, 0);
		return toBytes((int) ch.getValue());
	}
}
