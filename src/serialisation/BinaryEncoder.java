package serialisation;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import serialisation.Expose.TvpeGetter;
import static utils.BinaryUtils.toBytes;

public class BinaryEncoder implements ObjectEncoder<byte[]> {

	public byte[] encode(Object o) {
		List<byte[]> bl = encodeInternal(o, o.getClass());
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

	public List<byte[]> encodeInternal(Object o, Class<?> type) {
		List<byte[]> bl = new ArrayList<byte[]>();
		List<Field> fl = getFields(type);
		for (Iterator<Field> iterator = fl.iterator(); iterator.hasNext();) {
			Field f = iterator.next();
			f.setAccessible(true);
			Object value = null;
			try {
				value = f.get(o);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			List<byte[]> b = encodeInternal(f, value);
			if (b != null)
				bl.addAll(b);
			else {
				Class<?> t = null;
				Expose e = f.getAnnotation(Expose.class);
				if (e != null) {
					Class<? extends TvpeGetter>[] tgt = e.getTypeGetterType();
					if (tgt != null && tgt.length >= 1) {
						try {
							t = tgt[0].newInstance().getType(o, f);
						} catch (InstantiationException
								| IllegalAccessException e1) {
							e1.printStackTrace();
						}
					}
				}
				if (t == null)
					t = f.getType();
				bl.addAll(encodeInternal(value, t));
			}
		}
		return bl;
	}

	private List<Field> getFields(Class<?> c) {
		while (c != null && !c.isAnnotationPresent(Deserializable.class))
			c = c.getSuperclass();
		if (c == null)
			return new ArrayList<Field>();
		List<Field> fl = new ArrayList<Field>();
		do {
			List<Field> fields = new ArrayList<Field>();
			for (Field f : c.getDeclaredFields()) {
				Expose e = f.getAnnotation(Expose.class);
				if (e == null)
					continue;
				fields.add(f);
			}
			Collections.sort(fl, new Comparator<Field>() {
				@Override
				public int compare(Field f1, Field f2) {
					Expose e1 = f1.getAnnotation(Expose.class);
					Expose e2 = f2.getAnnotation(Expose.class);
					if (e1 == null || e2 == null)
						return f1.getName().compareTo(f2.getName());
					return e1.position() - e2.position();
				}
			});
			fl.addAll(fields);
		} while ((c = c.getSuperclass()) != null);
		return fl;
	}

	private List<byte[]> encodeInternal(Field f, Object value) {
		Class<?> c = f.getType();
		if (Byte.class.isAssignableFrom(c))
			return Arrays
					.asList(new byte[][] { toBytes((byte) ((value == null) ? 0
							: value)) });
		if (Short.class.isAssignableFrom(c))
			return Arrays
					.asList(new byte[][] { toBytes((short) ((value == null) ? 0
							: value)) });
		if (Integer.class.isAssignableFrom(c))
			return Arrays
					.asList(new byte[][] { toBytes((int) ((value == null) ? 0
							: value)) });
		if (Long.class.isAssignableFrom(c))
			return Arrays
					.asList(new byte[][] { toBytes((long) ((value == null) ? 0
							: value)) });
		if (Character.class.isAssignableFrom(c))
			return Arrays
					.asList(new byte[][] { toBytes((char) ((value == null) ? 0
							: value)) });
		if (String.class.isAssignableFrom(c))
			try {
				byte[] res = ((String) ((value == null) ? 0 : value))
						.getBytes("UTF-8");
				return Arrays.asList(new byte[][] { toBytes(res.length), res });
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		if (Float.class.isAssignableFrom(c))
			return Arrays
					.asList(new byte[][] { toBytes((float) ((value == null) ? 0
							: value)) });
		if (Double.class.isAssignableFrom(c))
			return Arrays
					.asList(new byte[][] { toBytes((double) ((value == null) ? 0
							: value)) });
		return null;
	}

	private <R> R decode(ByteBuffer buffer, Class<R> type) {
		R o;
		try {
			o = type.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
		List<Field> fl = getFields(type);
		for (Iterator<Field> iterator = fl.iterator(); iterator.hasNext();) {
			Field f = iterator.next();
			f.setAccessible(true);
			Object value = decodeInternal(f, buffer);
			try {
				if (value != null) {
					f.set(o, value);
				} else {
					Class<?> t = null;
					Expose e = f.getAnnotation(Expose.class);
					if (e != null) {
						Class<? extends TvpeGetter>[] tgt = e
								.getTypeGetterType();
						if (tgt != null && tgt.length >= 1) {
							try {
								t = tgt[0].newInstance().getType(o, f);
							} catch (InstantiationException
									| IllegalAccessException e1) {
								e1.printStackTrace();
							}
						}
					}
					if (t == null)
						t = f.getType();
					f.set(o, decode(buffer, t));
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return o;
	}

	private Object decodeInternal(Field f, ByteBuffer buffer) {
		byte[] dst = new byte[8];
		Class<?> c = f.getType();
		if (Byte.class.isAssignableFrom(c)) {
			buffer.get(dst, 0, 1);
			return utils.BinaryUtils.asByte(dst, 0);
		}
		if (Short.class.isAssignableFrom(c)) {
			buffer.get(dst, 0, 2);
			return utils.BinaryUtils.asShort(dst, 0);
		}
		if (Integer.class.isAssignableFrom(c)) {
			buffer.get(dst, 0, 4);
			return utils.BinaryUtils.asInt(dst, 0);
		}
		if (Long.class.isAssignableFrom(c)) {
			buffer.get(dst, 0, 8);
			return utils.BinaryUtils.asLong(dst, 0);
		}
		if (Character.class.isAssignableFrom(c)) {
			buffer.get(dst, 0, 2);
			return utils.BinaryUtils.asChar(dst, 0);
		}
		if (String.class.isAssignableFrom(c)) {
			buffer.get(dst, 0, 4);
			int length = utils.BinaryUtils.asInt(dst, 0);
			byte[] strBuff = new byte[length];
			buffer.get(strBuff, 0, length);
			return new String(strBuff, Charset.forName("UTF-8"));
		}
		if (Float.class.isAssignableFrom(c)) {
			buffer.get(dst, 0, 4);
			return utils.BinaryUtils.asFloat(dst, 0);
		}
		if (Float.class.isAssignableFrom(c)) {
			buffer.get(dst, 0, 8);
			return utils.BinaryUtils.asDouble(dst, 0);
		}
		return null;
	}

	@Override
	public <R> R decode(byte[] o, Class<R> c) {
		return decode(ByteBuffer.wrap(o), c);
	}

}
