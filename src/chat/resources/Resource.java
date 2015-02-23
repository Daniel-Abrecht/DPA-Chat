package chat.resources;

import chat.Chat;
import chat.checksum.HashCalculator;
import chat.manager.EndpointManager;
import serialisation.Expose;

public abstract class Resource {
	private ResourcePool<Resource> resourcePool;
	@Expose
	private Integer id = -1;
	@Preserve
	private int checksum = 0;

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

	public boolean isRegistred() {
		return resourcePool != null;
	}

	@SuppressWarnings("unchecked")
	public void register(EndpointManager endpointManager) {
		register((ResourcePool<Resource>) endpointManager.getResourcePool(this
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
		register(em);
		updateChecksum();
		return this;
	}

	public static Resource reconstruct(Object x, EndpointManager em) {
		return ((Resource) x).reconstruct(em);
	}

	public void updateRemote() {
		Chat.connectionManager.send(this);
		updateChecksum();
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

	private void updateChecksum() {
		int oldChecksum = checksum;
		checksum = HashCalculator.calcHash(this);
		getResourcePool().updatePublicHashCode(oldChecksum, checksum);
	}

}
