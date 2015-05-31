package chat.resources;

import serialisation.Deserializable;
import serialisation.Expose;

/**
 * Hilfsklasse zur (De)serialisierung von Message Ressourcen,
 * Repräsentiert eine Message.
 * 
 * @author Daniel Abrecht
 * @see chat.resources.Resource
 */
@Deserializable
public class Message extends Resource implements Comparable<Message> {
	@Expose(position = 0, customFieldEncoder = ResourceReferenceEncoder.class)
	private ChatRoom chatRoom;
	@Expose(position = 1, customFieldEncoder = ResourceReferenceEncoder.class)
	private Profil profil;
	@Expose(position = 2)
	private long creationTime = System.currentTimeMillis();
	@Expose(position = 3)
	private String content;

	/**
	 * Getter für ChatRoom
	 * @return Der Chatroom
	 */
	public ChatRoom getChatRoom() {
		return chatRoom;
	}

	/**
	 * Setter für ChatRoom
	 * @param chatRoom Der übergeordnete Chatroom
	 */
	public void setChatRoom(ChatRoom chatRoom) {
		this.chatRoom = chatRoom;
	}

	/**
	 * Getter für Inhalt der Nachricht
	 * @return Der Inhalt der Nachricht
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Getter für Inhalt der Nachricht
	 * @param content Der Inhalt der Nachricht
	 */
	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Message [content=" + content + ", chatRoom=" + chatRoom + "]";
	}

	/**
	 * Getter für Profil des Verfassers der Nachricht
	 * @return Der Verfasser der Nachricht
	 */
	public Profil getProfil() {
		return profil;
	}

	/**
	 * Getter für Profil des Verfassers der Nachricht
	 * @param profil Der neue Verfasser der Nachricht
	 */
	public void setProfil(Profil profil) {
		this.profil = profil;
	}

	/**
	 * Comperator zur Sortierung der Nachrichten
	 * Noch unvollständig
	 * @param message die zu Vergleichende Nachricht
	 */
	public int compareTo(Message message) {
		int timeDiff = (int) (creationTime - message.creationTime);
		if (timeDiff != 0)
			return timeDiff;
		return compareIdentifier(message);
	}
}
