package chat.receiveHandler;

import chat.resources.Resource;
import chat.resources.ResourcePool;
import chat.resources.ResourceRequest;
import connectionManager.Container;
import connectionManager.ReceiveHandler;
import connectionManager.Endpoint;
import static chat.Chat.connectionManager;

public class ResourceRequestHandler implements ReceiveHandler {
	@Override
	public Class<?> getHandledClass() {
		return ResourceRequest.class;
	}

	@Override
	public void onReceive(Container container, Endpoint e) {
		ResourceRequest rq = (ResourceRequest) container.getObject();
		ResourcePool<? extends Resource> resPool = connectionManager.getLocalEndpointManager().getResourcePoolList().get(rq.getRespoolIndex());
		if (resPool == null) {
			System.out.println("Requested resourcepool "+ rq.getRespoolIndex() +" not found");
			return;
		}
		Resource res = resPool.getResource(rq.getId());
		if(res == null){
			System.out.println("Requested resource "+ rq.getId() +" not found in resourcepool " + rq.getRespoolIndex());
			return;
		}
		res.updateRemote();
	}
}
