package chat;

import static connectionManager.EventHandler.createEventHandler;
import ui.ProfilManager;
import chat.event.EndpointEventHandler;
import chat.event.UserManagerEventHandler;
import chat.eventListenerImpl.EndpointListenerImpl;
import chat.eventListenerImpl.UserManagerListenerImpl;
import chat.receiveHandler.ResourceReciveHandler;
import chat.receiveHandler.UserReciveHandler;
import chat.resources.Profil;
import connectionManager.ConnectionManager;

public class Chat {

	private final static String multicastAddr = "231.255.255.10";
	private final static int port = 3311;

	public static ConnectionManager connectionManager = new ConnectionManager(
			multicastAddr, port);;
	public static EndpointEventHandler endpointEventHandler = createEventHandler(EndpointEventHandler.class);
	public static UserManagerEventHandler userManagerEventHandler = createEventHandler(UserManagerEventHandler.class);
	public static Profil currentProfil = null;

	public static void main(String[] args) {

		connectionManager.addHandler(new UserReciveHandler());
		connectionManager.addHandler(new ResourceReciveHandler());

		endpointEventHandler.addEventListener(new EndpointListenerImpl());
		userManagerEventHandler.addEventListener(new UserManagerListenerImpl());

		connectionManager.start();

		ProfilManager.getInstance().setVisible(true);
	}
}
