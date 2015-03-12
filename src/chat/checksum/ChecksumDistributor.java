package chat.checksum;

import java.util.TimerTask;

import chat.Chat;
import chat.manager.EndpointManager;

public class ChecksumDistributor extends TimerTask {
	private EndpointManager endpointManager;

	public ChecksumDistributor(EndpointManager endpointManager) {
		this.endpointManager = endpointManager;
	}

	public void run() {
		int rootChecksum = endpointManager.getChechsum();
		EndpointControlInfoContainer chc = new EndpointControlInfoContainer();
		chc.setRootChecksum(rootChecksum);
		Chat.connectionManager.send(chc);
	}
}
