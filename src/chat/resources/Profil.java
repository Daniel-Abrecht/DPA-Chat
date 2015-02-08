package chat.resources;

import serialisation.Deserializable;

import serialisation.Expose;

import connectionManager.User;

@Deserializable
public class Profil extends Resource {
	private User user;
	@Expose
	private Integer userId;
	private String description;

	public User getUser() {
		userId = (int) user.getId();
		return user;
	}

	public void setUser(User user) {
		userId = null;
		this.user = user;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
