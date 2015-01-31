package chat.resources;

import static chat.manager.EndpointMap.endpointMap;

import java.util.Collection;
import java.util.Iterator;
import chat.manager.EndpointManager;
import chat.manager.UserManager;
import com.google.gson.annotations.Expose;

public abstract class Resource {
	private ResourcePool<Resource> resourcePool;
	@Expose
	private Integer id = -1;

	public void register(ResourcePool<Resource> parent) {
		if (this.resourcePool != null)
			deregister();
		this.resourcePool = parent;
		if (id < 0) {
			id = parent.register(this);
		} else {
			parent.register(this, id);
		}
	}

	@SuppressWarnings("unchecked")
	public void register(UserManager userManager) {
		register((ResourcePool<Resource>) userManager.getResourcePool(this
				.getClass()));
	}

	public void deregister() {
		resourcePool.deregister(this);
		id = -1;
	}

	public Integer getId() {
		return id;
	}

	public Resource reconstruct(UserManager um) {
		register(um);
		return this;
	}

	public static Resource reconstruct(Object x, UserManager user) {
		return ((Resource) x).reconstruct(user);
	}

	public static <T extends Resource> void every(Class<T> resourceType,
			ResourceHandler<T> rh) {
		Collection<EndpointManager> endpoints = endpointMap.getEndpoints();
		for (Iterator<EndpointManager> iterator = endpoints.iterator(); iterator
				.hasNext();) {
			EndpointManager endpointManager = iterator.next();
			Collection<UserManager> userManagers = endpointManager
					.getUserManagers();
			for (UserManager userManager : userManagers) {
				ResourcePool<T> resourcePool = userManager
						.getResourcePool(resourceType);
				Collection<T> resources = resourcePool.getResources();
				for (Iterator<T> iterator2 = resources.iterator(); iterator2
						.hasNext();) {
					T resource = iterator2.next();
					rh.handler(endpointManager, userManager, resourcePool,
							resource);
				}
			}
		}
	}

}
