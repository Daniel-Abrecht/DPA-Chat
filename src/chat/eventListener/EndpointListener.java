package chat.eventListener;

import chat.manager.EndpointManager;

public interface EndpointListener {
	public void endpointFound(EndpointManager em);
	public void endpointLost(EndpointManager em);
}
