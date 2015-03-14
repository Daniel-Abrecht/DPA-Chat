package chat.resources;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import serialisation.Expose;
import connectionManager.EventHandlerIface;
import chat.event.ResourceEventHandler;
import chat.eventListener.ResourceListener;
import chat.manager.EndpointManager;
import static connectionManager.EventHandler.createEventHandler;

public class ResourcePool<T extends Resource> implements
		EventHandlerIface<ResourceListener<T>> {

	private int checksum = 0;
	private int resIdCounter = 0;
	private Map<Integer, T> resources = new ConcurrentHashMap<Integer, T>();

	private final EndpointManager endpointManager;

	@SuppressWarnings("unchecked")
	private ResourceEventHandler<T> resourceEventHandler = createEventHandler(ResourceEventHandler.class);

	/**
	 * Contains the network-wide valid hascode for some kind of resource.
	 * 
	 * If the network-wide reflection of an ressource in this ressourcepool
	 * changes, for example by transmitting a new version of the object, this
	 * method must be invoked to update the hash-code.
	 * 
	 * Todo: Find a solution to prevent double transmissions of objects due to
	 * race-conditions between the transmission of the hash-code information and
	 * its change because of the transmission of a changed object. The order in
	 * which the transmitted object and the hascode arrives is inconsistent.
	 * Possible solutions may be a small hash history with an expirtion time for
	 * each hash change info.
	 * 
	 * @param oldChecksum
	 *            the old checksum of the resource
	 * @param newChechsum
	 *            the new checksum of the resource
	 */
	synchronized public void updatePublicHashCode(int oldChecksum, int newChechsum) {
		int oldCh = checksum;
		checksum = checksum - oldChecksum + newChechsum;
		getEndpointManager().updateChecksum(oldCh, checksum);
	}

	public int getChecksum() {
		return checksum;
	}

	public void setChecksum(int publicHashCode) {
		this.checksum = publicHashCode;
	}

	public ResourcePool(EndpointManager endpointManager) {
		this.endpointManager = endpointManager;
	}

	public Integer register(T resource) {
		resources.put(resIdCounter, resource);
		resourceEventHandler.resourceCreation(this, resource);
		return resIdCounter++;
	}

	public T update(T resource, int id) {
		T old = resources.get(id);
		if (old == null) {
			resources.put(id, resource);
			resourceEventHandler.resourceCreation(this, resource);
		} else {
			Class<?> clazz = resource.getClass();
			Class<?> classOld = old.getClass();
			do {
				if (!classOld.isAssignableFrom(clazz))
					continue;
				Field[] fields = classOld.getDeclaredFields();
				for (int i = 0; i < fields.length; i++) {
					Field field = fields[i];
					if (field.getAnnotation(Preserve.class) != null
							&& field.getAnnotation(Expose.class) == null)
						continue;
					field.setAccessible(true);
					try {
						if (field.get(resource) == null
								&& field.getAnnotation(Preserve.class) != null)
							continue;
						Object nv = field.get(resource);
						field.set(old, nv);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			} while ((classOld = classOld.getSuperclass()) != null);
		}
		resourceEventHandler.resourceChange(this, old == null ? resource : old);
		return old == null ? resource : old;
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
				res = newres = update(newres, rid);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
				return null;
			}
		return res;
	}

}
