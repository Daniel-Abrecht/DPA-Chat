package chat.checksum;

import java.util.TimerTask;

import chat.manager.EndpointManager;

public class ChecksumDistributor extends TimerTask {
	private EndpointManager endpointManager;

	public ChecksumDistributor(EndpointManager endpointManager) {
		this.endpointManager = endpointManager;
	}

	public void run() {
		int checksum = endpointManager.getChechsum();
		System.out.println(checksum);
	}
}
