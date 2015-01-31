package connectionManager;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ConnectionManager extends Endpoint {

	private Server server;
	private Client client;
	private List<ReceiveHandler> reciveHandlers = new ArrayList<ReceiveHandler>();

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
	}

	public void start(){
		server.start();
		client.start();
	}

	public void end(){
		server.end();
		client.end();
	}
	
	public void reciveHandler(RemoteEndpoint e, DataPacket dp) {
		byte uid = dp.getUserId();
		User u = e.getUser(uid);
		if (u == null)
			e.setUser(uid, u = new User());
		Container c = Container.parse(new String(dp.getBuffer()));
		Object obj = c.getObject();
		for(ReceiveHandler reciveHandler : reciveHandlers){
			if(reciveHandler.getHandledClass().isAssignableFrom(obj.getClass())){
				reciveHandler.onReceive(c,e,u);
				break;
			}
		}
	}

	public void addHandler(ReceiveHandler reciveHandler) {
		reciveHandlers.add(reciveHandler);
	}

	public void send(LocalUser u, Object o) {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
				.create();
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

	public LocalUser createUser() {
		LocalUser u = new LocalUser(this);
		if (tryAddUser(u))
			return u;
		else
			return null;
	}

}
