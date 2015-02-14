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

public class ConnectionManager extends Endpoint {

	private Server server;
	private Client client;
	private List<ReceiveHandler> reciveHandlers = new ArrayList<ReceiveHandler>();
	private Endpoint localEndpoint;
	private static Gson gson = new GsonBuilder().registerTypeHierarchyAdapter(
			Object.class, new AnnotationProcessorAdapter()).create();

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

	public void reciveHandler(RemoteEndpoint e, DataPacket dp) {
		byte uid = dp.getUserId();
		User u = e.getUser(uid);
		if (u == null)
			e.setUser(uid, u = new User());
		Container c;
		try {
			c = Container.parse(new String(dp.getBuffer(),"UTF-8"));
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
			return;
		}
		Object obj = c.getObject();
		if(obj instanceof User)
			e.setUser(uid, u=(User)obj);
		for (ReceiveHandler reciveHandler : reciveHandlers) {
			if (reciveHandler.getHandledClass()
					.isAssignableFrom(obj.getClass())) {
				reciveHandler.onReceive(c, e, u);
				break;
			}
		}
	}

	public void addHandler(ReceiveHandler reciveHandler) {
		reciveHandlers.add(reciveHandler);
	}

	public void send(User u, Object o) {
		if(u.getEndpoint()==null){
			localEndpoint.tryAddUser(u);
		}
		String str = gson.toJson(new Container(o));
		System.out.println(str);
		byte[] buffer = null;
		try {
			buffer = str.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		DataPacket packet = new DataPacket(buffer.length);
		packet.setUserId(u.getId());
		packet.fill(buffer, 0, buffer.length, 0);
		client.send(packet);
	}

	public EndpointManager getEndpointManager(InetAddress addr) {
		Endpoint e = server.getOrCreateEndpoint(addr);
		return endpointMap.sync(e);
	}

}
