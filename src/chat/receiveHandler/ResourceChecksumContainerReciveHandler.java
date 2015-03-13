package chat.receiveHandler;

import java.util.List;

import chat.checksum.ResourceChecksumContainer;
import chat.checksum.ResourceChecksumContainer.IdChecksumPair;
import connectionManager.Container;
import connectionManager.ReceiveHandler;
import connectionManager.Endpoint;

public class ResourceChecksumContainerReciveHandler implements ReceiveHandler {
	@Override
	public Class<?> getHandledClass() {
		return ResourceChecksumContainer.class;
	}

	@Override
	public void onReceive(Container container, Endpoint e) {
		ResourceChecksumContainer res = (ResourceChecksumContainer)container.getObject();
		List<IdChecksumPair> checksums = res.getChecksums();
		for (IdChecksumPair c : checksums) {
			
		}
	}
}
