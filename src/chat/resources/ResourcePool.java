package chat.resources;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.annotations.Expose;

import connectionManager.Deserializable;

@Deserializable
public class ResourcePool<T extends Resource> {

	@Expose
	private int id = 0;

	private int resIdCounter = 0;
	@Expose
	private Map<Integer, T> resources = new ConcurrentHashMap<Integer, T>();

	public Integer register(T resource) {
		resources.put(resIdCounter, resource);
		return resIdCounter++;
	}

	public void register(T resource,int id) {
		resources.put(id, resource);
	}

	public void deregister(T resource) {
		resources.remove(resource.getId());
	}

	public int getId() {
		return id;
	}

	public Collection<T> getResources() {
		return resources.values();
	}

}
