package connectionManager;

import java.net.InetAddress;
import java.util.Date;

/**
 * Klasse, welche ein Netzwerkgerät repräsentiert
 * 
 * @author Daniel Abrecht
 */
public class Endpoint {

	private Date lastMessageArrivalTime;
	private DataPacket[] dataPackets = new DataPacket[0x80];
	private final int packetExpires = 6 * 1000; // 6 seconds
	private final int packetGuessMissingPackets = 3 * 1000; // 3 seconds
	protected InetAddress address;

	/**
	 * @param socketAddress Adresse des Endpoints
	 */
	public Endpoint(InetAddress socketAddress) {
		setAddress(socketAddress);
		updateLastMessageArrivalTime();
	}

	/**
	 * Getter für Datum, wann die Letzte Nachricht empfangen wurde
	 */
	public Date getLastMessageArrivalTime() {
		return lastMessageArrivalTime;
	}

	/**
	 * Ändert die Ankunftszeit der letzten Nachricht auf jetzt
	 */
	public void updateLastMessageArrivalTime() {
		lastMessageArrivalTime = new Date();
	}

	/**
	 * Erstellt ein Daten Packet
	 * 
	 * @param packetId Id des Daten Packets
	 * @param packetSize Grösse des Daten Packets
	 * @return
	 */
	public DataPacket createPacket(byte packetId, int packetSize) {
		if (packetId > 0x7f || packetId < 0) {
			System.err.println("Impossible packet id!");
			Thread.dumpStack();
			return null;
		}
		if (packetSize < 0) {
			System.err.println("Invalid packet size!");
			Thread.dumpStack();
			return null;
		}
		return dataPackets[packetId] = new DataPacket(packetSize);
	}

	/**
	 * Getter für Datenpacket
	 * @param packetId Id des Datenpackets
	 * @return Das Datenppacket
	 */
	public DataPacket getPacket(byte packetId) {
		return dataPackets[packetId];
	}

	/**
	 * Entfernen des Datenpackets
	 * @param packetId Id des Datenpackets
	 */
	public void removePacket(byte packetId) {
		if (packetId > 0x7f || packetId < 0) {
			System.err.println("Impossible packet id!");
			return;
		}
		dataPackets[packetId] = null;
	}

	/**
	 * Aufraumen.
	 * Entfernt referenzen af nichtmehr benötigte
	 *  ressourcen und vordert fehlende Daten nach
	 *  einem Timeout an
	 * 
	 * @param connectionManager Verwendeter ConnectionManager
	 */
	protected void cleanup(ConnectionManager connectionManager) {
		for (int i = 0; i < dataPackets.length; i++) {
			DataPacket d = dataPackets[i];
			if (d == null)
				continue;
			if (new Date().getTime() - d.getLastMessageArrivalTime().getTime() > packetExpires) {
				removePacket((byte) i);
			}else if ( d.fixMissing && new Date().getTime() - d.getLastMessageArrivalTime().getTime() > packetGuessMissingPackets) {
				d.fixMissing = false;
				boolean[] segInf = d.getRecivedSegmentInfo();
				int[] missing = new int[segInf.length-d.getRecivedSegmentCount()];
				System.err.println(i+") Request possibly missing fragments from " + d.getDestination()+":");
				for(int j=0,k=0;j<segInf.length;j++){
					if(segInf[j]==true)
						continue;
					missing[k++] = j;
					System.err.print(j+", ");
				}
				System.err.println();
				connectionManager.requestLostPackets(d.getDestination(), (byte)i, missing);
			}
		}
	}

	/**
	 * Setter für die Addresse des Endpoints
	 * @param address Die Addresse des Endpoints
	 */
	protected void setAddress(InetAddress address) {
		this.address = address;
	}

	/**
	 * Getter fur die Adresse des Endpoints
	 */
	public InetAddress getAddress() {
		return address;
	}
}