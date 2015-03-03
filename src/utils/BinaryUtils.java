package utils;

public class BinaryUtils {
	public final static byte[] toBytes(byte value) {
		return new byte[] { value };
	}

	public final static byte[] toBytes(short value) {
		return new byte[] { (byte) ((value >> 8) & 0xFF),
				(byte) ((value >> 0) & 0xFF) };
	}

	public final static byte[] toBytes(int value) {
		return new byte[] { (byte) ((value >> 24) & 0xFF),
				(byte) ((value >> 16) & 0xFF), (byte) ((value >> 8) & 0xFF),
				(byte) ((value >> 0) & 0xFF) };
	}

	public final static byte[] toBytes(long value) {
		return new byte[] { (byte) ((value >> 56) & 0xFF),
				(byte) ((value >> 48) & 0xFF), (byte) ((value >> 40) & 0xFF),
				(byte) ((value >> 32) & 0xFF), (byte) ((value >> 24) & 0xFF),
				(byte) ((value >> 16) & 0xFF), (byte) ((value >> 8) & 0xFF),
				(byte) ((value >> 0) & 0xFF) };
	}

	public final static byte[] toBytes(float value) {
		return toBytes(Float.floatToIntBits(value));
	}

	public final static byte[] toBytes(double value) {
		return toBytes(Double.doubleToLongBits(value));
	}

	public static String bytesToHex(byte[] bytes) {
		final char[] hexArray = "0123456789ABCDEF".toCharArray();
		char[] hexChars = new char[bytes.length * 3];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 3] = hexArray[v >>> 4];
			hexChars[j * 3 + 1] = hexArray[v & 0x0F];
			hexChars[j * 3 + 2] = (((j + 1) % 16) == 0) ? '\n' : ' ';
		}
		return new String(hexChars);
	}

	public static byte asByte(byte[] dst, int offset) {
		return dst[offset];
	}

	public static short asShort(byte[] dst, int i) {
		return (short) ((dst[i + 0] & 0xFF) << 8 | (dst[i + 1] & 0xFF));
	}

	public static int asInt(byte[] dst, int i) {
		return (dst[i + 0] & 0xFF) << 24 | (dst[i + 1] & 0xFF) << 16
				| (dst[i + 2] & 0xFF) << 8 | (dst[i + 3] & 0xFF);
	}

	public static long asLong(byte[] dst, int i) {
		return (dst[i + 0] & 0xFF) << 56 | (dst[i + 1] & 0xFF) << 48
				| (dst[i + 2] & 0xFF) << 40 | (dst[i + 3] & 0xFF) << 32
				| (dst[i + 0] & 0xFF) << 24 | (dst[i + 1] & 0xFF) << 16
				| (dst[i + 2] & 0xFF) << 8 | (dst[i + 3] & 0xFF);
	}

	public static char asChar(byte[] dst, int i) {
		return (char) asShort(dst, i);
	}

	public static float asFloat(byte[] dst, int i) {
		return Float.intBitsToFloat(asInt(dst,i));
	}

	public static double asDouble(byte[] dst, int i) {
		return Double.longBitsToDouble(asLong(dst,i));
	}

}
