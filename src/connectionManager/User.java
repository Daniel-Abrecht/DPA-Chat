package connectionManager;

public class User {
	
	private Byte id = null;
	private Endpoint endpoint;

	public void setId(byte id) {
		this.id = id;
	}

	public Byte getId() {
		return id;
	}

	@Override
	public String toString() {
		return "User [id=" + id + "]";
	}

	public void setEndpoint(Endpoint endpoint) {
		if (id != null && endpoint.getUser(id) != this)
			endpoint.setUser(id, this);
		this.endpoint = endpoint;
	}

	public Endpoint getEndpoint() {
		return endpoint;
	}

}
