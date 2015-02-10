package chat.manager;

import java.net.InetAddress;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static chat.Chat.endpointEventHandler;
import connectionManager.Endpoint;

public class EndpointMap {
	public static EndpointMap endpointMap = new EndpointMap();
	private Map<InetAddress, EndpointManager> emm = new ConcurrentHashMap<InetAddress, EndpointManager>();
	public EndpointManager sync(Endpoint e) {
		EndpointManager em = emm.get(e.getAddress());
		if (em == null){
			em = new EndpointManager(e);
			emm.put(e.getAddress(), em);
			endpointEventHandler.endpointFound(em);
		}
		return em;
	}
	public Collection<EndpointManager> getEndpoints() {
		return emm.values();
	}
}