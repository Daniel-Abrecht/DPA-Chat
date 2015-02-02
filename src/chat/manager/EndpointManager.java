package chat.manager;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import connectionManager.Endpoint;
import connectionManager.User;
import static chat.Chat.userManagerEventHandler;

public class EndpointManager {

	private Endpoint endpoint;
	private Map<Byte, UserManager> userManagers = new ConcurrentHashMap<Byte, UserManager>();

	public EndpointManager(Endpoint e) {
		this.endpoint = e;
	}

	public UserManager sync(User u) {
		byte id = u.getId();
		UserManager um = getUserManager(id);
		if (um == null) {
			userManagers.put(id, um = new UserManager(u));
			userManagerEventHandler.userManagerCreation(this, um);
		}
		userManagerEventHandler.userManagerChange(this, um);
		return um;
	}

	public Endpoint getEndpoint() {
		return endpoint;
	}

	public UserManager getUserManager(byte userId){
		return userManagers.get(userId);
	}

	public Collection<UserManager> getUserManagers() {
		return userManagers.values();
	}

}
