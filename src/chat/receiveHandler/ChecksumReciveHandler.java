package chat.receiveHandler;

import chat.checksum.ChecksumContainer;
import connectionManager.Container;
import connectionManager.ReceiveHandler;
import connectionManager.Endpoint;

public class ChecksumReciveHandler implements ReceiveHandler {
	@Override
	public Class<?> getHandledClass() {
		return ChecksumContainer.class;
	}

	@Override
	public void onReceive(Container container,Endpoint e) {
	}
}
