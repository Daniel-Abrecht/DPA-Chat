package test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import connectionManager.ConnectionManager;
import connectionManager.Container;
import connectionManager.ReceiveHandler;
import connectionManager.RemoteEndpoint;
import connectionManager.User;

public class ConnectionManagerTest {

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
	
	ConnectionManager cm ;

	@Before
	public void setupManager(){
		cm = new ConnectionManager(multicastAddr, port);
		cm.start();
	}
	
	@After
	public void removeManager(){
		cm.end();
		cm=null;
	}
	
	@Test
	public void testSendRecive() {
		UserReciveHandler userReciveHandler = new UserReciveHandler();
		
		cm.addHandler(userReciveHandler);
		
		User user = new User();
		user.setName("test");
		cm.send(user,user);
	}

}
