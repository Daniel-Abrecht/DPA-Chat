package chat.resources;

import com.google.gson.annotations.Expose;

import connectionManager.Deserializable;

@Deserializable
public class Message extends Resource {
	@Expose
	private String title;
	@Expose
	private String content;
	@Expose
	private Integer chatRoomId;
	private ChatRoom chatRoom;

	public ChatRoom getChatRoom() {
		return chatRoom;
	}
	
	public void setChatRoom(ChatRoom chatRoom) {
		this.chatRoom = chatRoom;
		this.chatRoomId = chatRoom.getId();
	}

	public Integer getChatRoomId() {
		return chatRoomId;
	}

}
