package chat.resources;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import connectionManager.EventHandlerIface;
import chat.event.ResourceEventHandler;
import chat.eventListener.ResourceListener;
import chat.manager.EndpointManager;
import static connectionManager.EventHandler.createEventHandler;

public class ResourcePool<T extends Resource> implements
		EventHandlerIface<ResourceListener<T>> {

	private int resIdCounter = 0;
	private Map<Integer, T> resources = new ConcurrentHashMap<Integer, T>();

	private final EndpointManager endpointManager;

	@SuppressWarnings("unchecked")
	private ResourceEventHandler<T> resourceEventHandler = createEventHandler(ResourceEventHandler.class);

	public ResourcePool(EndpointManager endpointManager) {
		this.endpointManager = endpointManager;
	}

	public Integer register(T resource) {
		resources.put(resIdCounter, resource);
		return resIdCounter++;
	}

	public void register(T resource, int id) {
		boolean created = !resources.containsKey(id);
		resources.put(id, resource);
		if (created) {
			resourceEventHandler.resourceCreation(this, resource);
		}
		resourceEventHandler.resourceChange(this, resource);
	}

	public void deregister(T resource) {
		resources.remove(resource.getId());
		resourceEventHandler.resourceRemovation(this, resource);
	}

	public Collection<T> getResources() {
		return resources.values();
	}

	public EndpointManager getEndpointManager() {
		return endpointManager;
	}

	@Override
	public void addEventListener(ResourceListener<T> endpointListener) {
		resourceEventHandler.addEventListener(endpointListener);
	}

	public T getResource(Integer id) {
		return resources.get(id);
	}

	public T getOrCreateResource(Integer rid, Class<? extends Resource> clazz) {
		T res = resources.get(rid);
		if (res == null)
			try {
				@SuppressWarnings("unchecked")
				T newres = (T) clazz.newInstance();
				register(res = newres, rid);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
				return null;
			}
		return res;
	}

}
