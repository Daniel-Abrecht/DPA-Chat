package chat;

import static connectionManager.EventHandler.createEventHandler;

import java.util.Timer;

import ui.ProfilManager;
import chat.checksum.ChecksumDistributor;
import chat.event.ChatEventHandler;
import chat.event.EndpointEventHandler;
import chat.eventListenerImpl.EndpointListenerImpl;
import chat.receiveHandler.ChecksumReciveHandler;
import chat.receiveHandler.ResourceReciveHandler;
import chat.resources.Profil;
import connectionManager.ConnectionManager;

public class Chat {

	private final static String multicastAddr = "231.255.255.10";
	private final static int port = 3311;

	public static final ConnectionManager connectionManager = new ConnectionManager(
			multicastAddr, port);
	public static final EndpointEventHandler endpointEventHandler = createEventHandler(EndpointEventHandler.class);
	private static Profil currentProfil = null;
	public static final ChatEventHandler events = createEventHandler(ChatEventHandler.class);;

	public static void main(String[] args) {

		connectionManager.addHandler(new ChecksumReciveHandler());
		connectionManager.addHandler(new ResourceReciveHandler());
		endpointEventHandler.addEventListener(new EndpointListenerImpl());
		connectionManager.start();

		Timer timer = new Timer();
//		timer.scheduleAtFixedRate(new ChecksumDistributor(connectionManager.getLocalEndpointManager()), 0, 3 * 1000);

		ProfilManager.getInstance().setVisible(true);
	}

	public static Profil getCurrentProfil() {
		return currentProfil;
	}

	public static void setCurrentProfil(Profil currentProfil) {
		Profil oldProfil = Chat.currentProfil;
		Chat.currentProfil = currentProfil;
		events.currentProfilChanged(oldProfil, currentProfil);
	}
}
