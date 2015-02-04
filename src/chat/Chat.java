package chat;

import static chat.event.EventHandler.createEventHandler;
import ui.ChatroomManager;
import ui.ProfileManager;
import chat.event.EndpointEventHandler;
import chat.event.UserManagerEventHandler;
import chat.eventListenerImpl.EndpointListenerImpl;
import chat.eventListenerImpl.UserManagerListenerImpl;
import chat.manager.UserManager;
import chat.receiveHandler.ResourceReciveHandler;
import chat.receiveHandler.UserReciveHandler;
import connectionManager.ConnectionManager;
import connectionManager.User;

public class Chat {

	private final static String multicastAddr = "231.255.255.10";
	private final static int port = 3311;

	public static ConnectionManager connectionManager = new ConnectionManager(multicastAddr, port);;
	public static ChatroomManager chatroomManager = ChatroomManager.getInstance();

	public static EndpointEventHandler endpointEventHandler = createEventHandler(EndpointEventHandler.class);
	public static UserManagerEventHandler userManagerEventHandler = createEventHandler(UserManagerEventHandler.class);
	public static UserManager currentUserManager;

	public static void main(String[] args) {
		
		connectionManager.addHandler(new UserReciveHandler());
		connectionManager.addHandler(new ResourceReciveHandler());
		
		endpointEventHandler.addEventListener(new EndpointListenerImpl());
		userManagerEventHandler.addEventListener(new UserManagerListenerImpl());
		
		connectionManager.start();
		
		run();
	}

	private static void run() {

		User user = new User();

		UserManager userManager = new UserManager(user);
		currentUserManager = userManager;
		user.setName("test");
		connectionManager.send(user,user);

		chatroomManager.setVisible(true);

		ProfileManager pm = new ProfileManager();
		pm.setVisible(true);

	}
}
