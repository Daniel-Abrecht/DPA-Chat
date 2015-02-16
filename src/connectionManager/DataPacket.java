package connectionManager;

import java.util.Arrays;
import java.util.Date;

class DataPacket {

	private int size;
	private byte type;
	private byte[] buffer;
	private int currentSize;
	private Date creationTime;

	public DataPacket(int packetSize) {
		this.size = packetSize;
		this.currentSize = 0;
		buffer = new byte[packetSize];
		creationTime = new Date();
	}

	public void setType(byte type) {
		this.type = type;
	}

	public byte getType() {
		return type;
	}

	public Boolean fill(byte[] bs, int i, int j, int offset) {
		if (size != 0 && offset + j > size) {
			buffer = null;
			System.err.println("Recived too much datas");
			return null;
		}
		if (offset - currentSize > 256 * 100
				|| (size == 0 && offset > 256 * 100)) {
			buffer = null;
			System.err.println("Packet reorder limit exceeded");
			return null;
		}
		if (buffer == null) {
			buffer = new byte[0];
		}
		if (size == 0) {
			int requiredSize = offset - j;
			if (buffer.length < requiredSize)
				buffer = Arrays.copyOf(buffer, requiredSize); // Slow
		}
		System.arraycopy(bs, i, buffer, offset, j);
		currentSize += j;
		return currentSize == size;
	}

	public Date getCreationTime(){
		return creationTime;
	}
	
	public byte[] getBuffer() {
		return buffer;
	}

	public int getSize() {
		return size;
	}

}