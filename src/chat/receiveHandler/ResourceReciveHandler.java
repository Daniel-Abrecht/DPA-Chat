package chat.receiveHandler;

import static chat.manager.EndpointMap.endpointMap;
import chat.manager.EndpointManager;
import chat.resources.Resource;
import connectionManager.Container;
import connectionManager.ReceiveHandler;
import connectionManager.Endpoint;

public class ResourceReciveHandler implements ReceiveHandler {
	@Override
	public Class<?> getHandledClass() {
		return Resource.class;
	}

	@Override
	public void onReceive(Container container,Endpoint e) {
		EndpointManager em = endpointMap.sync(e);
		Resource res = Resource.reconstruct(container.getObject(),em);
		System.out.println(res+" | id: "+res.getId());
	}
}
