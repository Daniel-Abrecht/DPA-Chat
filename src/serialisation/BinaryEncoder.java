package serialisation;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
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

	@Override
	public byte[] encode(Object o) {
		List<byte[]> bl = encode(o, o.getClass());
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

	@Override
	public List<byte[]> encode(Object o, Class<?> type) {
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
			List<byte[]> b = encodeField(f, value);
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
				bl.addAll(encode(value, t));
			}
		}
		return bl;
	}

	@Override
	public List<Field> getFields(Class<?> c) {
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
			Collections.sort(fields, new Comparator<Field>() {
				@Override
				public int compare(Field f1, Field f2) {
					Expose e1 = f1.getAnnotation(Expose.class);
					Expose e2 = f2.getAnnotation(Expose.class);
					if (e1 == null || e2 == null)
						return f1.getName().compareTo(f2.getName());
					return e1.position() - e2.position();
				}
			});
			fields.addAll(fl);
			fl = fields;
		} while ((c = c.getSuperclass()) != null);
		return fl;
	}

	@Override
	public List<byte[]> encodeField(Field f, Object value) {
		Expose e = f.getAnnotation(Expose.class);
		Class<? extends CustomFieldEncoder>[] en = e.customFieldEncoder();
		enBlock: if (en != null && en.length >= 1)
			try {
				Object result = en[0].newInstance().encodeField(this, f, value);
				if (result == null)
					break enBlock;
				if (result instanceof byte[])
					return Arrays.asList((byte[]) result);
				if (result instanceof List) {
					@SuppressWarnings("unchecked")
					List<byte[]> result2 = (List<byte[]>) result;
					return result2;
				}
				return Arrays.asList(encode(result));
			} catch (InstantiationException | IllegalAccessException e1) {
				e1.printStackTrace();
			}
		Class<?> c = f.getType();
		return encodePrimitive(c, value);
	}

	@Override
	public List<byte[]> encodePrimitive(Class<?> c, Object value) {
		if (Byte.class.isAssignableFrom(c) || byte.class.isAssignableFrom(c))
			return Arrays
					.asList(new byte[][] { toBytes((byte) ((value == null) ? 0
							: value)) });
		if (Short.class.isAssignableFrom(c) || short.class.isAssignableFrom(c))
			return Arrays
					.asList(new byte[][] { toBytes((short) ((value == null) ? 0
							: value)) });
		if (Integer.class.isAssignableFrom(c) || int.class.isAssignableFrom(c))
			return Arrays
					.asList(new byte[][] { toBytes((int) ((value == null) ? 0
							: value)) });
		if (Long.class.isAssignableFrom(c) || long.class.isAssignableFrom(c))
			return Arrays
					.asList(new byte[][] { toBytes((long) ((value == null) ? 0
							: value)) });
		if (Character.class.isAssignableFrom(c)
				|| char.class.isAssignableFrom(c))
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
		if (Float.class.isAssignableFrom(c) || float.class.isAssignableFrom(c))
			return Arrays
					.asList(new byte[][] { toBytes((float) ((value == null) ? 0
							: value)) });
		if (Double.class.isAssignableFrom(c)
				|| double.class.isAssignableFrom(c))
			return Arrays
					.asList(new byte[][] { toBytes((double) ((value == null) ? 0
							: value)) });
		if (InetAddress.class.isAssignableFrom(c)) {
			if (value == null)
				return Arrays.asList(new byte[][] { { 0 } });
			InetAddress addr = (InetAddress) value;
			byte[] ret = addr.getAddress();
			return Arrays.asList(new byte[][] { { (byte) ret.length }, ret });
		}
		return null;
	}

	@Override
	public <R> R decode(ByteBuffer buffer, Class<R> type) {
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
			Object value = null;
			boolean success = true;
			try {
				value = decodeField(f, buffer);
			} catch(UnsupportedOperationException e){
				success = false;
			}
			try {
				if (success) {
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

	@Override
	public Object decodeField(Field f, ByteBuffer buffer) {
		Expose e = f.getAnnotation(Expose.class);
		Class<? extends CustomFieldEncoder>[] en = e.customFieldEncoder();
		if (en != null && en.length >= 1)
			try {
				return en[0].newInstance().decodeField(this, f, buffer);
			} catch (InstantiationException | IllegalAccessException e1) {
				e1.printStackTrace();
			}
		Class<?> c = f.getType();
		return decodePrimitive(c, buffer);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <R> R decodePrimitive(Class<R> c, ByteBuffer buffer) throws UnsupportedOperationException {
		byte[] dst = new byte[8];
		if (Byte.class.isAssignableFrom(c) || byte.class.isAssignableFrom(c)) {
			buffer.get(dst, 0, 1);
			return (R) (Object) utils.BinaryUtils.asByte(dst, 0);
		}
		if (Short.class.isAssignableFrom(c) || short.class.isAssignableFrom(c)) {
			buffer.get(dst, 0, 2);
			return (R) (Object) utils.BinaryUtils.asShort(dst, 0);
		}
		if (Integer.class.isAssignableFrom(c) || int.class.isAssignableFrom(c)) {
			buffer.get(dst, 0, 4);
			return (R) (Object) utils.BinaryUtils.asInt(dst, 0);
		}
		if (Long.class.isAssignableFrom(c) || long.class.isAssignableFrom(c)) {
			buffer.get(dst, 0, 8);
			return (R) (Object) utils.BinaryUtils.asLong(dst, 0);
		}
		if (Character.class.isAssignableFrom(c)
				|| char.class.isAssignableFrom(c)) {
			buffer.get(dst, 0, 2);
			return (R) (Object) utils.BinaryUtils.asChar(dst, 0);
		}
		if (String.class.isAssignableFrom(c)) {
			buffer.get(dst, 0, 4);
			int length = utils.BinaryUtils.asInt(dst, 0);
			byte[] strBuff = new byte[length];
			buffer.get(strBuff, 0, length);
			return (R) (Object) new String(strBuff, Charset.forName("UTF-8"));
		}
		if (Float.class.isAssignableFrom(c) || float.class.isAssignableFrom(c)) {
			buffer.get(dst, 0, 4);
			return (R) (Object) utils.BinaryUtils.asFloat(dst, 0);
		}
		if (Double.class.isAssignableFrom(c)
				|| double.class.isAssignableFrom(c)) {
			buffer.get(dst, 0, 8);
			return (R) (Object) utils.BinaryUtils.asDouble(dst, 0);
		}
		if (InetAddress.class.isAssignableFrom(c)) {
			buffer.get(dst, 0, 1);
			if (dst[0] == 0)
				return null;
			byte[] addrBytes = new byte[dst[0]];
			buffer.get(addrBytes, 0, dst[0]);
			try {
				return (R) InetAddress.getByAddress(addrBytes);
			} catch (UnknownHostException e) {
				e.printStackTrace();
				return null;
			}
		}
		throw new UnsupportedOperationException("Unsupported type");
	}

	@Override
	public <R> R decode(byte[] o, Class<R> c) {
		return decode(ByteBuffer.wrap(o), c);
	}

}
