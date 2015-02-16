package chat.resources;

import chat.manager.EndpointManager;

public interface ResourceHandler<T extends Resource> {
	public void handler(EndpointManager endpointManager,
			ResourcePool<T> resourcePool, T resource);
}
