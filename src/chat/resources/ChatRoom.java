package chat.resources;

import serialisation.Deserializable;
import ui.ChatRoomView;
import chat.eventListener.ChatRoomListener;
import serialisation.Expose;

@Deserializable
public class ChatRoom extends Resource implements ChatRoomListener {
	@Expose
	private String name;
	@Preserve
	private ChatRoomView view;

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
		if (view == null)
			view = new ChatRoomView(this);
		return view;
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
