package chat.resources;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import chat.event.EventHandlerIface;
import chat.event.ResourceEventHandler;
import chat.eventListener.ResourceListener;
import chat.manager.UserManager;

import com.google.gson.annotations.Expose;

import connectionManager.Deserializable;
import static chat.event.EventHandler.createEventHandler;

@Deserializable
public class ResourcePool<T extends Resource> implements EventHandlerIface<ResourceListener<T>> {

	@Expose
	private int id = 0;

	private int resIdCounter = 0;
	@Expose
	private Map<Integer, T> resources = new ConcurrentHashMap<Integer, T>();

	private final UserManager userManager;

	@SuppressWarnings("unchecked")
	private ResourceEventHandler<T> resourceEventHandler = createEventHandler(ResourceEventHandler.class);

	public ResourcePool(UserManager userManager) {
		this.userManager = userManager;
	}

	public Integer register(T resource) {
		resources.put(resIdCounter, resource);
		return resIdCounter++;
	}

	public void register(T resource,int id) {
		boolean created = !resources.containsKey(id);
		resources.put(id, resource);
		if(created){
			resourceEventHandler.resourceCreation(this, resource);
		}
		resourceEventHandler.resourceChange(this, resource);
	}

	public void deregister(T resource) {
		resources.remove(resource.getId());
		resourceEventHandler.resourceRemovation(this, resource);
	}

	public int getId() {
		return id;
	}

	public Collection<T> getResources() {
		return resources.values();
	}

	public UserManager getUserManager() {
		return userManager;
	}

	@Override
	public void addEventListener(ResourceListener<T> endpointListener) {
		resourceEventHandler.addEventListener(endpointListener);
	}

}
