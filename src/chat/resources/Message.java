package chat.resources;

import serialisation.Deserializable;
import serialisation.Expose;

@Deserializable
public class Message extends Resource {
	@Expose
	private String content;
	@Expose(adapter=ResourceGsonAdapter.class)
	private ChatRoom chatRoom;

	public ChatRoom getChatRoom() {
		return chatRoom;
	}
	
	public void setChatRoom(ChatRoom chatRoom) {
		this.chatRoom = chatRoom;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
