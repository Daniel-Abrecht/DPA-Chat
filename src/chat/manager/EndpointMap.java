package chat.manager;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import static chat.event.EventHandlers.*;
import connectionManager.Endpoint;

public class EndpointMap {
	public static EndpointMap endpointMap = new EndpointMap();
	private Map<Endpoint, EndpointManager> emm = new ConcurrentHashMap<Endpoint, EndpointManager>();
	public EndpointManager sync(Endpoint e) {
		EndpointManager em = emm.get(e);
		if (em == null){
			em = new EndpointManager(e);
			emm.put(e, em);
			endpointEventHandler.endpointFound(em);
		}
		return em;
	}
	public Collection<EndpointManager> getEndpoints() {
		return emm.values();
	}
}