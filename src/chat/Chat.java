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
import chat.resources.ChatRoom;
import connectionManager.ConnectionManager;
import connectionManager.LocalUser;

public class Chat {

	private final static String multicastAddr = "231.255.255.10";
	private final static int port = 3311;

	public static ConnectionManager connectionManager = new ConnectionManager(multicastAddr, port);;
	public static ChatroomManager chatroomManager = new ChatroomManager();

	public static EndpointEventHandler endpointEventHandler = createEventHandler(EndpointEventHandler.class);
	public static UserManagerEventHandler userManagerEventHandler = createEventHandler(UserManagerEventHandler.class);

	public static void main(String[] args) {
		
		connectionManager.addHandler(new UserReciveHandler());
		connectionManager.addHandler(new ResourceReciveHandler());
		
		endpointEventHandler.addEventListener(new EndpointListenerImpl());
		userManagerEventHandler.addEventListener(new UserManagerListenerImpl());
		
		connectionManager.start();
		
		run();
	}

	private static void run() {

		LocalUser user = connectionManager.createUser();
		UserManager userManager = new UserManager(user);
		user.setName("test");
		user.send(user);

		ChatRoom room = new ChatRoom();
		room.register(userManager);
		room.setName("ChatRoom 1");
		user.send(room);
		room.setName("ChatRoom 1 Modified");
		user.send(room);

		ChatRoom room2 = new ChatRoom();
		room2.register(userManager);
		room2.setName("ChatRoom 2");
		user.send(room2);

		chatroomManager.setVisible(true);

		ProfileManager pm = new ProfileManager();
		pm.setVisible(true);

	}
}
