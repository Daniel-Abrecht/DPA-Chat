package chat.receiveHandler;

import static chat.manager.EndpointMap.endpointMap;

import java.util.List;

import chat.Chat;
import chat.checksum.ChecksumContainer;
import chat.manager.EndpointManager;
import chat.resources.Resource;
import chat.resources.ResourcePool;
import connectionManager.Container;
import connectionManager.ReceiveHandler;
import connectionManager.Endpoint;

public class ChecksumReciveHandler implements ReceiveHandler {

	@Override
	public Class<?> getHandledClass() {
		return ChecksumContainer.class;
	}

	@Override
	public void onReceive(Container container, Endpoint e) {
		EndpointManager em = endpointMap.sync(e);
		ChecksumContainer chc = (ChecksumContainer) container.getObject();
		System.out.println(chc);
		Integer rootChecksum = chc.getRootChecksum();
		if (rootChecksum != null && (int) em.getChechsum() != rootChecksum) {
			System.out.println("Checksum mismatch");
			ChecksumContainer rpch = new ChecksumContainer();
			List<ResourcePool<? extends Resource>> resPools = em
					.getResourcePoolList();
			for (ResourcePool<? extends Resource> resPool : resPools)
				rpch.addRespoolChecksum(resPool.getChecksum());
			Chat.connectionManager.send(e,rpch);
		} else if (chc.hasResPoolChecksums()) {
			List<ResourcePool<? extends Resource>> resPools = em
					.getResourcePoolList();
			List<Integer> correctChecksums = chc.getResPoolChecksums();
			System.err.println(correctChecksums);
			for (int i = 0, n = Math.min(resPools.size(),correctChecksums.size()); i < n; i++) {
				int currentChecksum = resPools.get(i).getChecksum();
				int correctChecksum = correctChecksums.get(i);
				if(currentChecksum==correctChecksum)
					continue;
				System.out.println("Respool "+ i + ": Checksum mismatch");
			}
		}
	}
}
