package connectionManager;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class Endpoint {
	private int userCount = 0;
	protected final int maxUsers = 0x100;
	protected User[] users = new User[maxUsers];
	protected InetAddress address;

	public interface UserEventHandler extends UserListener,
			EventHandlerIface<UserListener> {
	};

	public final UserEventHandler userEventHandler = EventHandler
			.createEventHandler(UserEventHandler.class);

	public User getUser(byte id) {
		return users[id];
	}

	protected void setUser(byte id, User user) {
		if (user.getEndpoint() != this)
			user.setEndpoint(this);
		user.setId(id);
		if (users[id] == null && user != null) {
			userCount++;
			userEventHandler.userCreation(user);
		} else if (users[id] != null && user == null) {
			userCount--;
			userEventHandler.userRemovation(user);
		}
		users[id] = user;
		userEventHandler.userChange(user);
	}

	public void setUser(User user) {
		byte id = user.getId();
		setUser(id, user);
	}

	protected void removeUser(byte id, User user) {
		if (users[id] == null)
			return;
		userCount--;
		users[id] = null;
		userEventHandler.userRemovation(user);
	}

	public int getUserCount() {
		return userCount;
	}

	protected boolean tryAddUser(User u) {
		if (u == null || userCount >= maxUsers)
			return false;
		int i;
		for (i = 0; i < maxUsers; i++) {
			if (users[i] == null)
				break;
		}
		users[i] = u;
		u.setId((byte) i);
		u.setEndpoint(this);
		userCount++;
		userEventHandler.userCreation(u);
		return true;
	}

	public List<User> getUsers() {
		List<User> userList = new ArrayList<User>();
		for (int i = 0; i < users.length; i++) {
			User user = users[i];
			if (user == null)
				continue;
			userList.add(user);
		}
		return userList;
	}

	protected void setAddress(InetAddress address) {
		this.address = address;
	}

	public InetAddress getAddress() {
		return address;
	}

}
