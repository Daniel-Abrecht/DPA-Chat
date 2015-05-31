package chat.resources;

import serialisation.Deserializable;
import ui.ChatRoomView;
import chat.eventListener.ChatRoomListener;
import serialisation.Expose;

/**
 * Hilfsklasse zur (De)serialisierung von ChatRoom Ressourcen,
 * Repräsentiert einen Chatroom und kümmert sich deshalb um
 * Für diesen relevante Events
 * 
 * @author DanielAbrecht
 * @see chat.resources.Resource
 */
@Deserializable
public class ChatRoom extends Resource implements ChatRoomListener {
	@Expose(position=0)
	private String name;
	@Preserve
	private ChatRoomView view;

	/**
	 * Getter für name
	 * 
	 * @return Name des Chatrooms
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter für name
	 * @param name neuer Name des Chatrooms
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "ChatRoom [name=" + name + "]";
	}

	/**
	 * Getter/Singelton für den Chatroom View.
	 * Es kann nur ein Fenster pro ChatRoom geben, 
	 * dies vereinfacht die Architektur massiv
	 * 
	 * @return Der View der zum Chatroom gehört
	 */
	public ChatRoomView getView() {
		if (view == null)
			view = new ChatRoomView(this);
		return view;
	}

	@Override
	public void messageCreation(ResourcePool<Message> resourcePool,
			Message message) {
	}

	/**
	 * Aktualisieren der Nachrich im ChatRoomView
	 */
	@Override
	public void messageChange(ResourcePool<Message> resourcePool,
			Message message) {
		getView().update(message);
	}

	/**
	 * Entfernen der Nachricht vom ChatRoomView
	 */
	@Override
	public void messageRemovation(ResourcePool<Message> resourcePool,
			Message message) {
		getView().remove(message.getId());
	}

}
