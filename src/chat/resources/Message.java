package chat.resources;

import serialisation.Deserializable;
import serialisation.Expose;

@Deserializable
public class Message extends Resource {
	@Expose
	private String content;
	@Expose(adapter=ResourceGsonAdapter.class)
	private ChatRoom chatRoom;
	@Expose(adapter=ResourceGsonAdapter.class)
	private Profil profil;
	
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

	@Override
	public String toString() {
		return "Message [content=" + content + ", chatRoom=" + chatRoom + "]";
	}

	public Profil getProfil() {
		return profil;
	}

	public void setProfil(Profil profil) {
		this.profil = profil;
	}
}
