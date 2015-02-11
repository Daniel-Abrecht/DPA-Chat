package connectionManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class Server extends Thread {
	private int port;
	private MulticastSocket socket;
	private boolean running;
	private ConnectionManager connectionManager;
	private Map<InetAddress, RemoteEndpoint> endpoints = new ConcurrentHashMap<InetAddress, RemoteEndpoint>();
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

	public void end() {
		running = false;
	}

	final private static char[] hexArray = "0123456789ABCDEF".toCharArray();

	protected static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 3];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 3] = hexArray[v >>> 4];
			hexChars[j * 3 + 1] = hexArray[v & 0x0F];
			hexChars[j * 3 + 2] = (((j + 1) % 16) == 0) ? '\n' : ' ';
		}
		return new String(hexChars);
	}

	private class DataParser {

		private int getInteger(byte[] bs, int offset) {
			return ((bs[offset] & 0xFF) << 24)
					| ((bs[offset + 1] & 0xFF) << 16)
					| ((bs[offset + 2] & 0xFF) << 8) | (bs[offset + 3] & 0xFF);
		}

		public void parse(DatagramPacket packet) {
			int length = packet.getLength();
			byte[] bs = packet.getData();
			if (length < 1) {
				System.err.println("Invalid packet size!");
				return;
			}
			boolean first = (bs[0] & 0x80) != 0;
			byte packetId = (byte) (bs[0] & 0x7F);
			RemoteEndpoint e = getOrCreateEndpoint(((InetSocketAddress) packet
					.getSocketAddress()).getAddress());
			e.updateLastMessageArrivalTime();
			DataPacket dp = null;
			int offset = 0;
			if (first) {
				if (length < 6) {
					System.err.println("Invalid packet size!");
					return;
				}
				int packetSize = getInteger(bs, 1);
				if (packetSize < 0) {
					System.out.println("--dump--\n" + bytesToHex(bs)
							+ "\n--dump end--");
					return;
				}
				byte userId = bs[5];
				dp = e.createPacket(packetId, packetSize);
				dp.setUserId(userId);
			} else {
				dp = e.getPacket(packetId);
				if (dp == null)
					dp = e.createPacket(packetId, 0);
				int packetNr = getInteger(bs, 1) & 0x01FFFFFF;
				offset = (256 - 6) + packetNr * (256 - 5);
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
			byte[] buffer = new byte[256];
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

	public RemoteEndpoint getOrCreateEndpoint(InetAddress inetAddress) {
		RemoteEndpoint e = endpoints.get(inetAddress);
		if (e == null) {
			e = new RemoteEndpoint(inetAddress);
			endpoints.put(inetAddress, e);
		}
		return e;
	}

	private void cleanup() {
		for (RemoteEndpoint e : endpoints.values()) {
			if (new Date().getTime() - e.getLastMessageArrivalTime().getTime() > endpointExpires) {
				endpoints.remove(e.getAddress());
			} else {
				e.cleanup();
			}
		}
	}

	public Endpoint getLocalEndpoint() {
		return localEndpoint;
	}

	public void setLocalEndpoint(Endpoint localEndpoint) {
		this.localEndpoint = localEndpoint;
	}

}
