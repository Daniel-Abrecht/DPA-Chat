package chat.receiveHandler;

import connectionManager.Container;
import connectionManager.ReceiveHandler;
import connectionManager.RemoteEndpoint;
import connectionManager.User;

public class UserReciveHandler implements ReceiveHandler {
	@Override
	public Class<?> getHandledClass() {
		return User.class;
	}

	@Override
	public void onReceive(Container container,RemoteEndpoint e, User u) {
		User user = (User)container.getObject();
		e.setUser(user);
		System.out.println(user);
	}
}
