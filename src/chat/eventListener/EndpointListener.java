package chat.eventListener;

import chat.manager.EndpointManager;

/**
 * Behandelt events die Endpoints betreffen
 * 
 * @see connectionManager.EventHandlerIface
 * @see connectionManager.EventHandler
 */
public interface EndpointListener {
	
	/**
	 * Behandelt neue gefundene Endpoints
	 * 
	 * @param em neuer Endpoint
	 */
	public void endpointFound(EndpointManager em);

	/**
	 * Behandelt verlorene Endpoints
	 * 
	 * @param em verlorener Endpoint
	 */
	public void endpointLost(EndpointManager em);
}
