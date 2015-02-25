package chat.receiveHandler;

import static chat.manager.EndpointMap.endpointMap;
import chat.checksum.ChecksumContainer;
import chat.manager.EndpointManager;
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
		Integer rootChecksum = chc.getRootChecksum();
		if (rootChecksum != null && (int) em.getChechsum() != rootChecksum) {
			System.out.println("Checksum mismatch");
		}
	}
}
