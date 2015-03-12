package chat.receiveHandler;

import chat.checksum.ResourceChecksumContainer;
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
		System.out.println(res);
	}
}
