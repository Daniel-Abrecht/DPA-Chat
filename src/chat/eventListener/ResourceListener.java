package chat.eventListener;

import chat.resources.Resource;
import chat.resources.ResourcePool;

public interface ResourceListener<T extends Resource> {
	public void resourceCreation(ResourcePool<T> resourcePool, T resource);
	public void resourceChange(ResourcePool<T> resourcePool, T resource);
	public void resourceRemovation(ResourcePool<T> resourcePool, T resource);
}
