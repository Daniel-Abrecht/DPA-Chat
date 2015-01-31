package chat.resources;

import chat.manager.EndpointManager;
import chat.manager.UserManager;

public interface ResourceHandler<T extends Resource> {
	public void handler(EndpointManager endpointManager, UserManager userManager,
			ResourcePool<T> resourcePool, T resource);
}
