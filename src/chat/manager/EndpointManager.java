package chat.manager;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import connectionManager.Endpoint;
import connectionManager.User;
import connectionManager.UserListener;
import static chat.Chat.userManagerEventHandler;

public class EndpointManager {

	private Endpoint endpoint;
	private Map<Byte, UserManager> userManagers = new ConcurrentHashMap<Byte, UserManager>();

	public EndpointManager(Endpoint e) {
		this.endpoint = e;
		final EndpointManager self = this;
		endpoint.userEventHandler.addEventListener(new UserListener() {

			private void createOrChange(User user) {
				UserManager userManager = userManagers.get(user.getId());
				if (userManager == null) {
					userManager = new UserManager(user);
					userManagerEventHandler.userManagerCreation(self,
							userManager);
				} else if (userManager.getUser() == user) {
					return;
				}
				userManager.setUser(user);
				userManagers.put(user.getId(), userManager);
				userManagerEventHandler.userManagerChange(self, userManager);
			}

			@Override
			public void userCreation(User user) {
				createOrChange(user);
			}

			@Override
			public void userChange(User user) {
				createOrChange(user);
			}

			@Override
			public void userRemovation(User user) {
				UserManager userManager = userManagers.get(user.getId());
				if (userManager == null)
					return;
				userManagers.remove(user.getId());
				userManagerEventHandler
						.userManagerRemovation(self, userManager);
			}

		});
	}

	public Endpoint getEndpoint() {
		return endpoint;
	}

	public UserManager getUserManager(byte userId) {
		return userManagers.get(userId);
	}

	public Collection<UserManager> getUserManagers() {
		return userManagers.values();
	}

	public UserManager sync(User u) {
		byte id = u.getId();
		UserManager um = getUserManager(id);
		if (um == null) {
			userManagers.put(id, um = new UserManager(u));
			userManagerEventHandler.userManagerCreation(this, um);
		} else if (um.getUser() == u)
			return um;
		userManagerEventHandler.userManagerChange(this, um);
		return um;
	}

}
