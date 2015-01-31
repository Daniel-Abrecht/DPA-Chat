package chat.resources;

import com.google.gson.annotations.Expose;

import connectionManager.Deserializable;

@Deserializable
public class Message extends Resource {
	@Expose
	private String title;
	@Expose
	private String content;
}
