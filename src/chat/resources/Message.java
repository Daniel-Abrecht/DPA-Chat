package chat.resources;

import serialisation.Deserializable;
import serialisation.Expose;

@Deserializable
public class Message extends Resource implements Comparable<Message> {
	@Expose(position = 0, customFieldEncoder = ResourceReferenceEncoder.class)
	// (adapter=ResourceGsonAdapter.class)
	private ChatRoom chatRoom;
	@Expose(position = 1, customFieldEncoder = ResourceReferenceEncoder.class)
	// (adapter=ResourceGsonAdapter.class)
	private Profil profil;
	@Expose(position = 2)
	private long creationTime = System.currentTimeMillis();
	@Expose(position = 3)
	private String content;

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

	public int compareTo(Message message) {// incomplete
		int timeDiff = (int) (creationTime - message.creationTime);
		if (timeDiff != 0)
			return timeDiff;
		return compareIdentifier(message);
	}
}
