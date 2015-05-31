package chat.checksum;

import java.lang.reflect.Field;
import java.util.zip.Checksum;
import java.util.zip.CRC32;
import serialisation.Expose;
import static utils.BinaryUtils.toBytes;

/**
 * Klasse zum berechnen einer Prüfsumme
 * 
 * @author Daniel Abrecht
 */
public class HashCalculator {

	private static int calcHash(Object o, Checksum ch, int off) {
		if (o == null)
			return 0;
		if (o instanceof Byte) {
			final int len = 1;
			ch.update(toBytes(((Byte) o).byteValue()), 0, len);
			off += len;
		} else if (o instanceof Short) {
			final int len = 2;
			ch.update(toBytes(((Short) o).shortValue()), 0, len);
			off += len;
		} else if (o instanceof Integer) {
			final int len = 4;
			ch.update(toBytes(((Integer) o).intValue()), 0, len);
			off += len;
		} else if (o instanceof Long) {
			final int len = 8;
			ch.update(toBytes(((Long) o).longValue()), 0, len);
			off += len;
		} else if (o instanceof Character) {
			short x = (short) (char) o;
			return calcHash(x, ch, off);
		} else if (o instanceof String) {
			byte[] by = ((String) o).getBytes();
			return calcHash(by, ch, off);
		} else if (o instanceof byte[]) {
			byte[] by = (byte[]) o;
			ch.update(by, 0, by.length);
			off += by.length;
		} else if (o instanceof Object[]) {
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

	/**
	 * Berechnet eine Platformübergreifend eindeutige Checksumme
	 * für die in einem Objekt enthaltenen Daten.
	 * 
	 * @param o Das objekt dessen checksumme zu berechnen ist
	 * @return Die checksumme des Objekts
	 */
	public static int calcHash(Object o) {
		CRC32 ch = new CRC32();
		calcHash(o, ch, 0);
		return (int) ch.getValue();
	}
}
