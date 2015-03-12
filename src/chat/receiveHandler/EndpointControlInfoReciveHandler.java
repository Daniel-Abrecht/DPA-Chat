package chat.receiveHandler;

import static chat.manager.EndpointMap.endpointMap;

import java.util.List;

import chat.Chat;
import chat.checksum.EndpointControlInfoContainer;
import chat.checksum.RespoolChecksumContainer;
import chat.manager.EndpointManager;
import chat.resources.Resource;
import chat.resources.ResourcePool;
import connectionManager.Container;
import connectionManager.ReceiveHandler;
import connectionManager.Endpoint;

public class EndpointControlInfoReciveHandler implements ReceiveHandler {

	@Override
	public Class<?> getHandledClass() {
		return EndpointControlInfoContainer.class;
	}

	@Override
	public void onReceive(Container container, Endpoint e) {
		EndpointManager em = endpointMap.sync(e);
		EndpointControlInfoContainer chc = (EndpointControlInfoContainer) container.getObject();
		Integer rootChecksum = chc.getRootChecksum();
		if (rootChecksum == null || (int) em.getChechsum() == rootChecksum)
			return;
		System.out.println("Checksum mismatch");
		RespoolChecksumContainer rpch = new RespoolChecksumContainer();
		List<ResourcePool<? extends Resource>> resPools = em
				.getResourcePoolList();
		for (ResourcePool<? extends Resource> resPool : resPools)
			rpch.addRespoolChecksum(resPool.getChecksum());
		Chat.connectionManager.send(e, rpch);
	}
}
