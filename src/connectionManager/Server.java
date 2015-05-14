package connectionManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import utils.BinaryUtils;
import static utils.BinaryUtils.bytesToHex;

class Server extends Thread {
	private int port;
	private MulticastSocket socket;
	private boolean running;
	private ConnectionManager connectionManager;
	private Map<InetAddress, Endpoint> endpoints = new ConcurrentHashMap<InetAddress, Endpoint>();
	private InetAddress group;
	private Endpoint localEndpoint;

	private final int endpointExpires = 60 * 1000; // 1 minute

	public Server(ConnectionManager cm, InetAddress addr, int port) {
		this.port = port;
		this.connectionManager = cm;
		this.group = addr;
		try {
			localEndpoint = getOrCreateEndpoint(InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public static boolean isLocal(InetAddress addr) {
		// Check if the address is a valid special local or loop back
		if (addr.isAnyLocalAddress() || addr.isLoopbackAddress())
			return true;

		// Check if the address is defined on any interface
		try {
			return NetworkInterface.getByInetAddress(addr) != null;
		} catch (SocketException e) {
			return false;
		}
	}

	public void end() {
		running = false;
	}

	private class DataParser {

		public void parse(DatagramPacket packet) {
			int length = packet.getLength();
			byte[] bs = packet.getData();
			if (length < 1) {
				System.err.println("Invalid packet size!");
				Thread.dumpStack();
				return;
			}
			boolean first = (bs[0] & 0x80) != 0;
			byte packetId = (byte) (bs[0] & 0x7F);
			Endpoint e = getOrCreateEndpoint(((InetSocketAddress) packet
					.getSocketAddress()).getAddress());
			e.updateLastMessageArrivalTime();
			DataPacket dp = null;
			int offset = 0;
			if (first) {
				if (length < 6) {
					System.err.println("Invalid packet size!");
					Thread.dumpStack();
					return;
				}
				int packetSize = BinaryUtils.asInt(bs, 1);
				if (packetSize < 0) {
					System.out.println("--dump--\n" + bytesToHex(bs)
							+ "\n--dump end--");
					return;
				}
				byte type = bs[5];
				dp = e.createPacket(packetId, packetSize);
				dp.setType(type);
				dp.setDestination(e.getAddress());
			} else {
				dp = e.getPacket(packetId);
				if (dp == null)
					dp = e.createPacket(packetId, 0);
				int packetNr = BinaryUtils.asInt(bs, 1) & 0x01FFFFFF;
				offset = (DataPacket.segmentSize - 6) + packetNr * (DataPacket.segmentSize - 5);
//				System.err.println("Packet "+packetId+" | nr: "+packetNr+" | offset: "+offset );
			}
			Boolean full = dp.fill(bs, first ? 6 : 5, length - (first ? 6 : 5),
					offset);
			if (full == null) {
				e.removePacket(packetId);
				return;
			}
			if (full) {
				// data packet full
				e.removePacket(packetId);
				System.out.println("-- recived from "+e.getAddress()+" --\n"+ bytesToHex(dp.getBuffer()));
				connectionManager.reciveHandler(e, dp);
			}
		}
	}

	public void run() {
		running = true;
		try {
			System.out.println("Server running on port " + port);
			DataParser dataParser = new DataParser();
			socket = new MulticastSocket(port);
			socket.joinGroup(group);
			byte[] buffer = new byte[DataPacket.segmentSize];
			while (running) {
				try {
					cleanup();
					DatagramPacket packet = new DatagramPacket(buffer,
							buffer.length);
					socket.receive(packet);
					dataParser.parse(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			socket.leaveGroup(group);
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Endpoint getOrCreateEndpoint(InetAddress inetAddress) {
		Endpoint e;
		if (isLocal(inetAddress)) {
			e = localEndpoint;
		} else {
			e = endpoints.get(inetAddress);
		}
		if (e == null) {
			e = new Endpoint(inetAddress);
			endpoints.put(inetAddress, e);
		}
		return e;
	}

	private void cleanup() {
		for (Endpoint e : endpoints.values()) {
			if (new Date().getTime() - e.getLastMessageArrivalTime().getTime() > endpointExpires) {
				endpoints.remove(e.getAddress());
			} else {
				e.cleanup(connectionManager);
			}
		}
	}

	public Endpoint getLocalEndpoint() {
		return localEndpoint;
	}

}
