package connectionManager;

import java.net.SocketAddress;
import java.util.Date;

public class RemoteEndpoint extends Endpoint {

	private Date lastMessageArrivalTime;
	private DataPacket[] dataPackets = new DataPacket[0x80];
	private final int packetExpires = 10 * 1000; // 10 seconds

	public RemoteEndpoint(SocketAddress socketAddress) {
		setSocketAddress(socketAddress);
		updateLastMessageArrivalTime();
	}

	public Date getLastMessageArrivalTime() {
		return lastMessageArrivalTime;
	}

	public SocketAddress getAddress() {
		return socketAddress;
	}


	public void updateLastMessageArrivalTime() {
		lastMessageArrivalTime = new Date();
	}

	public DataPacket createPacket(byte packetId, int packetSize) {
		if (packetId > 0x3f) {
			System.err.println("Impossible packet id!");
			return null;
		}
		return dataPackets[packetId] = new DataPacket(packetSize);
	}

	public DataPacket getPacket(byte packetId) {
		return dataPackets[packetId];
	}

	public void removePacket(byte packetId) {
		if (packetId > 0x3f) {
			System.err.println("Impossible packet id!");
			return;
		}
		dataPackets[packetId] = null;
	}

	protected void cleanup() {
		for (int i = 0; i < dataPackets.length; i++) {
			DataPacket d = dataPackets[i];
			if(d==null)
				continue;
			if (new Date().getTime() - d.getCreationTime().getTime() > packetExpires) {
				removePacket((byte)i);
			}
		}
	}

}