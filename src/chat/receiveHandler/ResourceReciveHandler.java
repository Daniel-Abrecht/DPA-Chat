package chat.receiveHandler;

import static chat.manager.EndpointMap.endpointMap;
import chat.manager.UserManager;
import chat.resources.Resource;
import connectionManager.Container;
import connectionManager.ReceiveHandler;
import connectionManager.RemoteEndpoint;
import connectionManager.User;

public class ResourceReciveHandler implements ReceiveHandler {
	@Override
	public Class<?> getHandledClass() {
		return Resource.class;
	}

	@Override
	public void onReceive(Container container,RemoteEndpoint e, User u) {
		UserManager um = endpointMap.sync(e).sync(u);
		Resource res = Resource.reconstruct(container.getObject(),um);
		System.out.println(res+" | id: "+res.getId());
	}
}
