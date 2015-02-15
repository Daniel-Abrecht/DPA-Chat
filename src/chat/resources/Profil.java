package chat.resources;

import serialisation.Deserializable;
import serialisation.Expose;

@Deserializable
public class Profil extends Resource {
	@Expose
	private String name;
	@Expose
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Profil [name=" + name + ", description=" + description + "]";
	}

}
