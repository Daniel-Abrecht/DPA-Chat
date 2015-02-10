package connectionManager;

import serialisation.Deserializable;
import serialisation.Expose;

@Deserializable
public class User {
	
	private Byte id = null;
	@Expose
	private String name;
	private Endpoint endpoint;

	public void setId(byte id) {
		this.id = id;
	}

	public Byte getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + "]";
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
