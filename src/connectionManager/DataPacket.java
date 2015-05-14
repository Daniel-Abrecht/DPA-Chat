package connectionManager;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.Date;

class DataPacket {

	public static final byte TYPE_CUSTOM_DATAS = 0;
	public static final byte TYPE_PACKET_REQUEST = 1;
	public static final int segmentSize = 1<<8;
	private int size;
	private byte type = TYPE_CUSTOM_DATAS;
	private byte[] buffer;
	private boolean[] recivedSegments;
	private int recivedSegmentCount = 0;
	private int currentSize;
	private Date updateTime;
	private InetAddress destination;
	public boolean fixMissing = true;

	public DataPacket(int packetSize) {
		this.size = packetSize;
		this.currentSize = 0;
		buffer = new byte[packetSize];
		recivedSegments = new boolean[getSegmentCount(packetSize,segmentSize)];
		updateTime = new Date();
	}

	public int getRecivedSegmentCount() {
		return recivedSegmentCount;
	}

	public boolean[] getRecivedSegmentInfo() {
		return recivedSegments;
	}

	private int getSegmentCount(int size,int bufferSize){
		return (bufferSize+size)/(bufferSize-5);
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
		if (buffer == null) {
			buffer = new byte[0];
		}
		if (size == 0) {
			int requiredSize = offset + j;
			if (buffer.length < requiredSize){
				buffer = Arrays.copyOf(buffer, requiredSize); // Slow
				recivedSegments = Arrays.copyOf(recivedSegments, getSegmentCount(requiredSize,segmentSize));
			}
		}
		int pnr = (offset + 1) / (segmentSize - 5);
		if(recivedSegments[pnr])
			return false; // already recived
		recivedSegmentCount++;
		recivedSegments[pnr] = true;
		System.arraycopy(bs, i, buffer, offset, j);
		updateTime = new Date();
		currentSize += j;
		return currentSize == size;
	}

	public Date getLastMessageArrivalTime(){
		return updateTime;
	}
	
	public byte[] getBuffer() {
		return buffer;
	}

	public int getSize() {
		return size;
	}

	public InetAddress getDestination() {
		return destination;
	}

	public void setDestination(InetAddress destination) {
		this.destination = destination;
	}

}