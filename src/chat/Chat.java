package chat;

import static connectionManager.EventHandler.createEventHandler;
import ui.ProfilManager;
import chat.event.EndpointEventHandler;
import chat.eventListenerImpl.EndpointListenerImpl;
import chat.receiveHandler.ResourceReciveHandler;
import chat.resources.Profil;
import connectionManager.ConnectionManager;

public class Chat {

	private final static String multicastAddr = "231.255.255.10";
	private final static int port = 3311;

	public static ConnectionManager connectionManager = new ConnectionManager(
			multicastAddr, port);;
	public static EndpointEventHandler endpointEventHandler = createEventHandler(EndpointEventHandler.class);
	public static Profil currentProfil = null;

	public static void main(String[] args) {

		connectionManager.addHandler(new ResourceReciveHandler());
		endpointEventHandler.addEventListener(new EndpointListenerImpl());
		connectionManager.start();

		ProfilManager.getInstance().setVisible(true);
	}
}
