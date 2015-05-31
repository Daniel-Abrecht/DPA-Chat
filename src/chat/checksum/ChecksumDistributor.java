package chat.checksum;

import java.util.TimerTask;

import chat.Chat;
import chat.manager.EndpointManager;

/**
 * @author Daniel Abrecht
 */
public class ChecksumDistributor extends TimerTask {
	private EndpointManager endpointManager;

	/**
	 * Ein timer, der anderen Anwendungen regelmässig die Checksumme der
	 * Datensätze des endpointManagers mitteilt.
	 * 
	 * @param endpointManager
	 */
	public ChecksumDistributor(EndpointManager endpointManager) {
		this.endpointManager = endpointManager;
	}

	/**
	 * Versendet die Checksume des EndpointManagers.
	 */
	@Override
	public void run() {
		int rootChecksum = endpointManager.getChecksum();
		EndpointControlInfoContainer chc = new EndpointControlInfoContainer();
		chc.setRootChecksum(rootChecksum);
		Chat.connectionManager.send(chc);
	}
}
