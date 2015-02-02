package chat.eventListenerImpl;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import chat.eventListener.EndpointListener;
import chat.manager.EndpointManager;

public class EndpointListenerImpl implements EndpointListener {
	@Override
	public void endpointFound(EndpointManager em) {
		InetAddress inetAddr = ((InetSocketAddress) em.getEndpoint()
				.getSocketAddress()).getAddress();
		System.out.println("Endpoint found: " + inetAddr);				
	}

	@Override
	public void endpointLost(EndpointManager em) {
		InetAddress inetAddr = ((InetSocketAddress) em.getEndpoint()
				.getSocketAddress()).getAddress();
		System.out.println("Endpoint lost: " + inetAddr);				
	}

}
