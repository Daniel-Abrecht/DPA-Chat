package connectionManager;

import serialisation.Deserializable;

import serialisation.Expose;

@Deserializable
public class User {

	private byte id;
	@Expose private String name;

	public void setId(byte id) {
		this.id = id;
	}

	public byte getId() {
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

}
