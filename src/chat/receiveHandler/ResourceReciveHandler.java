package chat.receiveHandler;

import static chat.manager.EndpointMap.endpointMap;
import chat.manager.EndpointManager;
import chat.resources.Resource;
import connectionManager.Container;
import connectionManager.ReceiveHandler;
import connectionManager.Endpoint;

/**
 * Diese kalsse behandelt empfangene Resource Objecte
 * 
 * @author Daniel Abrecht
 * @see connectionManager.ReceiveHandler
 */
public class ResourceReciveHandler implements ReceiveHandler {
	@Override
	public Class<?> getHandledClass() {
		return Resource.class;
	}

	/**
	 * Aktualisiert die Datenbestände
	 * 
	 * @param container Enthält die neue/geänderte Ressource
	 * @param e Der Endpoint, von welchem diese gesendet wurde
	 */
	@Override
	public void onReceive(Container container, Endpoint e) {
		EndpointManager em = endpointMap.sync(e);
		Resource res = Resource.reconstruct(container.getObject(), em);
		System.out.println(e.getAddress() + " | "
				+ em.getEndpoint().getAddress());
		System.out.println(res.isLocal() + " | " + res + " | id: "
				+ res.getId());
	}
}
