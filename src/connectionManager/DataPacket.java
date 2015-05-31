package connectionManager;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.Date;

/**
 * Ein Datenpacket
 * 
 * @author Daniel Abrecht
 */
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

	/**
	 * Initialisierung eines Datenpacket definierter grösse
	 * @param packetSize Grösse des Datenpackets
	 */
	public DataPacket(int packetSize) {
		this.size = packetSize;
		this.currentSize = 0;
		buffer = new byte[packetSize];
		recivedSegments = new boolean[getSegmentCount(packetSize,segmentSize)];
		updateTime = new Date();
	}

	/**
	 * Getter für anzahl Empfangener datenfragmente
	 * @return anzahl Empfangener datenfragmente
	 */
	public int getRecivedSegmentCount() {
		return recivedSegmentCount;
	}

	/**
	 * Getter für Array mit informationen ob die Segmente bereits Empfangen wurden
	 */
	public boolean[] getRecivedSegmentInfo() {
		return recivedSegments;
	}

	private int getSegmentCount(int size,int bufferSize){
		return (bufferSize+size)/(bufferSize-5);
	}

	/**
	 * Setter für Packettyp
	 * @param type
	 */
	public void setType(byte type) {
		this.type = type;
	}

	/**
	 * Getter für Packettyp
	 */
	public byte getType() {
		return type;
	}

	/**
	 * Daten in Datenpacket einfüllen
	 * @param bs bytearray mit Daten
	 * @param i offset ab welchem kopiert werden soll
	 * @param j Anzahl daten
	 * @param offset Offset, an welchen die daten hinkopiert werden
	 * @return Ob der Kopiervorgang erfolgreich war
	 */
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

	/**
	 * Getter für die Ankunftszeit der Letzten Nachricht
	 */
	public Date getLastMessageArrivalTime(){
		return updateTime;
	}
	
	/**
	 * Getter für die empfangenen Daten
	 * @return der Buffer mit den Daten
	 */
	public byte[] getBuffer() {
		return buffer;
	}

	/**
	 * Getter für die Datenpacketgrösse
	 * @return die Datenpacketgrösse
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Getter für Ziel/Quelladresse
	 */
	public InetAddress getDestination() {
		return destination;
	}

	/**
	 * Setter für Ziel/Quelladresse
	 */
	public void setDestination(InetAddress destination) {
		this.destination = destination;
	}

}