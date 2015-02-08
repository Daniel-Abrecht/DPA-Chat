package chat.resources;

import serialisation.Deserializable;
import ui.ChatRoomView;
import chat.eventListener.ChatRoomListener;

import serialisation.Expose;

@Deserializable
public class ChatRoom extends Resource implements ChatRoomListener {
	@Expose
	private String name;
	private ChatRoomView chv;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "ChatRoom [name=" + name + "]";
	}

	public ChatRoomView getView() {
		if (chv == null)
			chv = new ChatRoomView(this);
		return chv;
	}

	public void display() {
		getView().setVisible(true);
	}

	@Override
	public void messageCreation(ResourcePool<Message> resourcePool,
			Message message) {
	}

	@Override
	public void messageChange(ResourcePool<Message> resourcePool,
			Message message) {
		getView().update(message);
	}

	@Override
	public void messageRemovation(ResourcePool<Message> resourcePool,
			Message message) {
		getView().remove(message.getId());
	}

}
