package chat.receiveHandler;

import java.util.List;
import chat.Chat;
import chat.checksum.ResourceChecksumContainer;
import chat.checksum.RespoolChecksumContainer;
import chat.manager.EndpointManager;
import chat.resources.Resource;
import chat.resources.ResourcePool;
import connectionManager.Container;
import connectionManager.ReceiveHandler;
import connectionManager.Endpoint;
import static chat.Chat.connectionManager;

public class RespoolChecksumReciveHandler implements ReceiveHandler {

	@Override
	public Class<?> getHandledClass() {
		return RespoolChecksumContainer.class;
	}

	@Override
	public void onReceive(Container container, Endpoint e) {
		EndpointManager em = connectionManager.getLocalEndpointManager();
		RespoolChecksumContainer chc = (RespoolChecksumContainer) container.getObject();
		if (!chc.hasResPoolChecksums())
			return;
		List<ResourcePool<? extends Resource>> resPools = em
				.getResourcePoolList();
		List<Integer> correctChecksums = chc.getResPoolChecksums();
		for (int i = 0, n = Math.min(resPools.size(), correctChecksums.size()); i < n; i++) {
			ResourcePool<? extends Resource> resourcePool = resPools.get(i);
			int currentChecksum = resourcePool.getChecksum();
			int correctChecksum = correctChecksums.get(i);
			if (currentChecksum == correctChecksum)
				continue;
			System.out.println("Respool " + i + ": Checksum mismatch");
			ResourceChecksumContainer resChecksums = new ResourceChecksumContainer();
			resChecksums.setChecksums(resourcePool,i);
			Chat.connectionManager.send(e, resChecksums);
		}
	}
}
