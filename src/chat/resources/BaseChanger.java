package chat.resources;

public class BaseChanger {
	private static class State {
		public String base;
		public int offset;
		public String enc;
		public int b;
		public int nextChar;

		public State(String base) {
			this.base = base;
			this.b = base.length();
			this.offset = 0;
			this.enc = new String();
			this.nextChar = 0;
		}
	};

	private String encode(State s, Object... objs) {
		for (int i = 0; i < objs.length; i++) {
			Object o = objs[i];
			if (o instanceof Object[]) {
				encode(s, o);
			} else if (o.getClass().equals(byte[].class)) {
				byte[] o2 = (byte[]) o;
				Byte[] ba = new Byte[o2.length];
				for (int j = 0; j < o2.length; j++)
					ba[j] = o2[j];
				encode(s, (Object[]) ba);
			} else if (o instanceof String) {
				encode(s, ((String) o).getBytes());
			} else if (o instanceof Integer) {
				encode(s, (short) ((((Integer) o).intValue() >> 16) & 0xFFFF));
				encode(s, (short) (((Integer) o).intValue() & 0xFFFF));
			} else if (o instanceof Short) {
				encode(s, (byte) ((((Short) o).shortValue() >> 8) & 0xFF));
				encode(s, (byte) (((Short) o).shortValue() & 0xFF));
			} else if (o instanceof Byte) {
				int v = ((Byte) o).byteValue() & 0xFF;
				while (true) {
					int uoff = s.offset % s.b;
					int boff = s.offset & 0xFF;
					int j = s.offset / 0x100;
					s.nextChar += (v * (s.b + boff) / 0x100) % (s.b - uoff)
							+ uoff;
					if (0x100 - boff < s.b - uoff) {
						s.offset += 0x100 - boff;
						continue;
					}
					if (s.b - uoff + boff > 0x100)
						break;
					String character = s.base.substring(s.nextChar,
							s.nextChar + 1);
					s.enc += character;
					s.nextChar = 0;
					s.offset += s.b - uoff;
					if (j != (s.b - uoff + s.offset) / 0x100)
						break;
				}
			} else {
				System.err.println("Datatype unsupported");
			}
		}
		return s.enc;
	}

	public String encode(String base, Object... objs) {
		return encode(new State(base), objs);
	}
}