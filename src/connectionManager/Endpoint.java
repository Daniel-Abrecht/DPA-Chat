package connectionManager;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

public class Endpoint {
	private int userCount = 0;
	protected final int maxUsers = 0x100;
	protected User[] users = new User[maxUsers];
	protected SocketAddress socketAddress;

	public User getUser(byte id) {
		return users[id];
	}

	protected void setUser(byte id, User user) {
		if (users[id] == null && user != null) {
			userCount++;
		} else if (users[id] != null && user == null) {
			userCount--;
		}
		users[id] = user;
		user.setId(id);
	}

	public void setUser(User user) {
		byte id = user.getId();
		if (users[id] == null && user != null) {
			userCount++;
		} else if (users[id] != null && user == null) {
			userCount--;
		}
		users[id] = user;
	}

	protected void removeUser(byte id, User user) {
		if (users[id] != null)
			userCount--;
		users[id] = null;
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
		userCount++;
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

	protected void setSocketAddress(SocketAddress socketAddress) {
		this.socketAddress = socketAddress;
	}

	public SocketAddress getSocketAddress() {
		return socketAddress;
	}

}
