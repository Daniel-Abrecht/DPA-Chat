package chat.manager;

import java.net.InetAddress;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static chat.Chat.endpointEventHandler;
import connectionManager.Endpoint;

/**
 * Hilfsklasse um die Beziehungen zwischen einer InetAddress und einem EndpointManager zu verwalten
 * 
 * @author Daniel Abrecht
 */
public class EndpointMap {
	public static EndpointMap endpointMap = new EndpointMap();
	private Map<InetAddress, EndpointManager> emm = new ConcurrentHashMap<InetAddress, EndpointManager>();

	/**
	 * Sucht/Erstellt/Speichert einen EndpointManager zum Endpoint in einer Map mittels dessen Adresse
	 * 
	 * @param e Der Endpoint
	 * @return Der dazugehörige EndpointManager
	 */
	public EndpointManager sync(Endpoint e) {
		EndpointManager em = emm.get(e.getAddress());
		if (em == null){
			em = new EndpointManager(e);
			emm.put(e.getAddress(), em);
			endpointEventHandler.endpointFound(em);
		}
		return em;
	}
	
	/**
	 * Getter für Liste aller EndpointManager
	 * 
	 * @return Liste aller EndpointManager
	 */
	public Collection<EndpointManager> getEndpoints() {
		return emm.values();
	}
}