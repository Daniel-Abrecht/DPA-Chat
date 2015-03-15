package chat.resources;

import chat.Chat;
import chat.checksum.HashCalculator;
import chat.manager.EndpointManager;
import serialisation.Expose;

public abstract class Resource {
	private ResourcePool<Resource> resourcePool;
	@Expose(position = 0)
	private Integer id = -1;
	@Preserve
	private int checksum = 0;
	
	public Resource update(ResourcePool<Resource> parent) {
		if (this.resourcePool != null)
			deregister();
		this.resourcePool = parent;
		if (id < 0) {
			id = parent.register(this);
			return this;
		} else {
			return parent.update(this, id);
		}
	}

	public boolean isRegistred() {
		return resourcePool != null;
	}

	@SuppressWarnings("unchecked")
	public Resource update(EndpointManager endpointManager) {
		return update((ResourcePool<Resource>) endpointManager
				.getResourcePool(this.getClass()));
	}

	public void deregister() {
		resourcePool.deregister(this);
		id = -1;
	}

	public Integer getId() {
		return id;
	}

	public Resource reconstruct(EndpointManager em) {
		Resource r = update(em);
		r.updateChecksum();
		return r;
	}

	public static Resource reconstruct(Object x, EndpointManager em) {
		return ((Resource) x).reconstruct(em);
	}

	public Resource updateRemote() {
		Resource r = update();
		Chat.connectionManager.send(r);
		r.updateChecksum();
		return r;
	}

	private Resource update() {
		return resourcePool.update(this, id);
	}

	public ResourcePool<Resource> getResourcePool() {
		return resourcePool;
	}

	public EndpointManager getEndpointManager() {
		return resourcePool.getEndpointManager();
	}

	public boolean isLocal() {
		EndpointManager e = getEndpointManager();
		return e == Chat.connectionManager.getLocalEndpointManager();
	}

	public int getChecksum() {
		return checksum;
	}

	void updateChecksum() {
		int oldChecksum;
		Resource old = getResourcePool().getResource(id);
		synchronized (old) {
			if (old == null)
				return;
			oldChecksum = old.getChecksum();
			checksum = HashCalculator.calcHash(this);
		}
		getResourcePool().updatePublicHashCode(oldChecksum, checksum);
	}

	/**
	 * Check if two resources have the same identifying fields
	 * 
	 * @param res The other resource
	 * @return Returns true if both resource share the same types, IDs, and EndpointManagers 
	 */
	public boolean hasSameIdentifierAs(Resource res) {
		return (
				this != null && res != null &&
				this.getClass().equals(res.getClass()) &&
				this.getId() != null && res.getId() != null &&
				this.isRegistred() && res.isRegistred() &&
				this.getId().equals(res.getId()) &&
				this.getEndpointManager().equals(res.getEndpointManager())
		);
	}
	
	public int compareIdentifier(Resource res) {
		if (this == res)
			return 0;
		if (res == null)
			return 1;
		Integer a = this.getId();
		Integer b = res.getId();
		if(a==null&&b==null)
			return 0;
		if(a==null)
			return -1;
		if(b==null)
			return 1;
		return a.compareTo(b);
	}

}
