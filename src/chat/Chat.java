package chat;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import ui.ChatroomManager;
import ui.ProfileManager;
import static chat.event.EventHandlers.*;
import chat.eventListener.EndpointListener;
import chat.manager.EndpointManager;
import chat.manager.UserManager;
import chat.receiveHandler.ResourceReciveHandler;
import chat.receiveHandler.UserReciveHandler;
import chat.resources.ChatRoom;
import connectionManager.ConnectionManager;
import connectionManager.LocalUser;

public class Chat {

	private final static String multicastAddr = "231.255.255.10";
	private final static int port = 3311;

	private ConnectionManager cm;
	private ChatroomManager crm;

	public Chat() {
		crm = new ChatroomManager();
		cm = new ConnectionManager(multicastAddr, port);
		cm.addHandler(new UserReciveHandler());
		cm.addHandler(new ResourceReciveHandler());
		cm.start();
		endpointEventHandler.addEventListener(new EndpointListener() {
			@Override
			public void endpointFound(EndpointManager em) {
				InetAddress inetAddr = ((InetSocketAddress) em.getEndpoint()
						.getSocketAddress()).getAddress();
				System.out.println("Endpoint found: " + inetAddr);				
			}

			@Override
			public void endpointLost(EndpointManager em) {
				InetAddress inetAddr = ((InetSocketAddress) em.getEndpoint()
						.getSocketAddress()).getAddress();
				System.out.println("Endpoint lost: " + inetAddr);				
			}
		});
	}

	public static void main(String[] args) {
		Chat chat = new Chat();
		chat.run();
	}

	private void run() {

		LocalUser user = cm.createUser();
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

		crm.setVisible(true);
		crm.update();

		ProfileManager pm = new ProfileManager();
		pm.setVisible(true);

	}
}
