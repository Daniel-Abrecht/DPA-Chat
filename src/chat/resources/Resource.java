package chat.resources;

import chat.Chat;
import chat.checksum.HashCalculator;
import chat.manager.EndpointManager;
import serialisation.Expose;

public abstract class Resource {
	private ResourcePool<Resource> resourcePool;
	@Expose(position=0)
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
		return update((ResourcePool<Resource>) endpointManager.getResourcePool(this
				.getClass()));
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
		Resource old = getResourcePool().getResource(id);
		if (old == null)
			return;
		int oldChecksum = old.getChecksum();
		checksum = HashCalculator.calcHash(this);
		getResourcePool().updatePublicHashCode(oldChecksum, checksum);
	}

}
