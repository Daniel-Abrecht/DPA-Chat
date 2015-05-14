package connectionManager;

import static chat.manager.EndpointMap.endpointMap;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import serialisation.BinaryEncoder;
import serialisation.ObjectEncoder;
import utils.BinaryUtils;
import chat.manager.EndpointManager;
import static utils.BinaryUtils.bytesToHex;

public class ConnectionManager {

	private Server server;
	private Client client;
	private List<ReceiveHandler> reciveHandlers = new ArrayList<ReceiveHandler>();
	private Endpoint localEndpoint;

	public ConnectionManager(String multicastAddr, int port) {
		InetAddress addr;
		try {
			addr = InetAddress.getByName(multicastAddr);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return;
		}
		server = new Server(this, addr, port);
		client = new Client(addr, port);
		this.localEndpoint = server.getLocalEndpoint();
	}

	public void start() {
		server.start();
		client.start();
	}

	public void end() {
		server.end();
		client.end();
	}

	public void reciveHandler(Endpoint e, DataPacket dp) {
		switch(dp.getType()){
        	case DataPacket.TYPE_CUSTOM_DATAS: {
        		customdataReciveHandler(e,dp);
        	} break;
        	case DataPacket.TYPE_PACKET_REQUEST: {
        		packetRequest(e,dp);
        	} break;
		}
	}

	private void packetRequest(Endpoint e, DataPacket dp) {
		byte buffer[] = dp.getBuffer();
		byte id = buffer[0];
		DataPacket packet = client.getCachedPacket(id);
		if(packet==null)
			return;
		int n = BinaryUtils.asInt(buffer, 1);
		for(int i=0;i<n;i++){
			int nr = BinaryUtils.asInt(buffer, 5+i*4);
			client.retransmit(packet, nr, id, e.getAddress());
		}
	}

	private void customdataReciveHandler(Endpoint e, DataPacket dp) {
		ObjectEncoder<byte[]> encoder = new BinaryEncoder();
		encoder.setParameter("endpointIp", e.getAddress());
		Container c = (Container) encoder.decode(dp.getBuffer(),Container.class);
		Object obj = c.getObject();
		if (obj == null)
			return;
		for (ReceiveHandler reciveHandler : reciveHandlers) {
			if (reciveHandler.getHandledClass()
					.isAssignableFrom(obj.getClass())) {
				reciveHandler.onReceive(c, e);
				break;
			}
		}
	}

	public void addHandler(ReceiveHandler reciveHandler) {
		reciveHandlers.add(reciveHandler);
	}

	public void send(Object o) {
		send(null,o);
	}

	public EndpointManager getEndpointManager(InetAddress addr) {
		Endpoint e = server.getOrCreateEndpoint(addr);
		return endpointMap.sync(e);
	}

	public EndpointManager getLocalEndpointManager() {
		return endpointMap.sync(localEndpoint);
	}

	public void requestLostPackets(InetAddress inetAddress,byte id,int packetNumber[]) {
		byte buffer[] = new byte[1+4+4*packetNumber.length];
		buffer[0] = id;
		buffer[1 + 0] = (byte) (packetNumber.length >> 24);
		buffer[1 + 1] = (byte) (packetNumber.length >> 16);
		buffer[1 + 2] = (byte) (packetNumber.length >> 8);
		buffer[1 + 3] = (byte) (packetNumber.length >> 0);
		for (int i=0;i<packetNumber.length;i++) {
			buffer[5 + 0 + i * 4] = (byte) (packetNumber[i] >> 24);
			buffer[5 + 1 + i * 4] = (byte) (packetNumber[i] >> 16);
			buffer[5 + 2 + i * 4] = (byte) (packetNumber[i] >> 8);
			buffer[5 + 3 + i * 4] = (byte) (packetNumber[i] >> 0);
		}
		DataPacket packet = new DataPacket(buffer.length);
		packet.setType(DataPacket.TYPE_PACKET_REQUEST);
		packet.fill(buffer, 0, buffer.length, 0);
		packet.setDestination(inetAddress);
		System.err.println("Sent: "+bytesToHex(buffer));
		client.send(packet);
	}

	public void send(Endpoint e, Object o) {
		o = new Container(o);
		ObjectEncoder<byte[]> encoder = new BinaryEncoder();
		byte[] buffer = encoder.encode(o);
		DataPacket packet = new DataPacket(buffer.length);
		packet.setType(DataPacket.TYPE_CUSTOM_DATAS);
		packet.fill(buffer, 0, buffer.length, 0);
		if (e != null)
			packet.setDestination(e.getAddress());
		System.out.println("-- send to "+packet.getDestination()+" --\n" + bytesToHex(buffer));
		client.send(packet);
	}

}
