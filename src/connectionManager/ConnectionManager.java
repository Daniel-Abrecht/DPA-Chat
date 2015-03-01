package connectionManager;

import static chat.manager.EndpointMap.endpointMap;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import serialisation.AnnotationProcessorAdapter;
import chat.manager.EndpointManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
		Container c;
		try {
			String content = new String(dp.getBuffer(), "UTF-8");
			System.out.println(e.getAddress()+": " + content);
			c = Container.parse(content, e);
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
			return;
		}
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

	private static byte[] encodeJson(Object o) {
		Gson gson = new GsonBuilder().registerTypeHierarchyAdapter(
				Object.class, new AnnotationProcessorAdapter()).create();
		String str = gson.toJson(new Container(o));
		System.out.println(str);
		byte[] buffer = null;
		try {
			buffer = str.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return buffer;
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
		byte[] buffer = encodeJson(o);
		DataPacket packet = new DataPacket(buffer.length);
		packet.fill(buffer, 0, buffer.length, 0);
		if (e != null)
			packet.setDestination(e.getAddress());
		client.send(packet);
	}

}
