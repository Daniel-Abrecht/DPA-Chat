package connectionManager;

import static chat.manager.EndpointMap.endpointMap;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import serialisation.BinaryEncoder;
import serialisation.ObjectEncoder;
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
		ObjectEncoder<byte[]> encoder = new BinaryEncoder();
		encoder.setParameter("endpointIp", e.getAddress());
		Container c = (Container) encoder.decode(dp.getBuffer(),Container.class);
		Object obj = c.getObject();
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

	public void send(Endpoint e, Object o) {
		o = new Container(o);
		ObjectEncoder<byte[]> encoder = new BinaryEncoder();
		byte[] buffer = encoder.encode(o);
		System.out.println(o);
		System.out.println(bytesToHex(buffer));
		DataPacket packet = new DataPacket(buffer.length);
		packet.fill(buffer, 0, buffer.length, 0);
		if (e != null)
			packet.setDestination(e.getAddress());
		client.send(packet);
	}

}
