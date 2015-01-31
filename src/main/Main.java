package main;

import connectionManager.ConnectionManager;
import connectionManager.Container;
import connectionManager.LocalUser;
import connectionManager.ReceiveHandler;
import connectionManager.RemoteEndpoint;
import connectionManager.User;

public class Main {

	private final static String multicastAddr = "231.255.255.10";
	private final static int port = 3311;
	
	static class UserReciveHandler implements ReceiveHandler {

		@Override
		public Class<?> getHandledClass() {
			return User.class;
		}

		@Override
		public void onReceive(Container container,RemoteEndpoint e,User user) {
			User userObj = (User) container.getObject();
			System.out.println(userObj);
		}
		
	};
	
	public static void main(String[] args) {
		ConnectionManager cm = new ConnectionManager(multicastAddr, port);
		cm.start();

		UserReciveHandler userReciveHandler = new UserReciveHandler();

		cm.addHandler(userReciveHandler);

		LocalUser user = cm.createUser();
		user.setName("test");
		user.send(user);
	}
}
