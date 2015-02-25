package chat.eventListenerImpl;

import chat.eventListener.ResourceListener;
import chat.resources.ChatRoom;
import chat.resources.Message;
import chat.resources.ResourcePool;

public class MessageListenerImpl implements ResourceListener<Message> {

	@Override
	public void resourceCreation(ResourcePool<Message> resourcePool,
			Message message) {
		ChatRoom ch = message.getChatRoom();
		if (ch != null)
			ch.messageCreation(resourcePool, message);
	}

	@Override
	public void resourceChange(ResourcePool<Message> resourcePool,
			Message message) {
		ChatRoom ch = message.getChatRoom();
		if (ch != null)
			ch.messageChange(resourcePool, message);
	}

	@Override
	public void resourceRemovation(ResourcePool<Message> resourcePool,
			Message message) {
		ChatRoom ch = message.getChatRoom();
		if (ch != null)
			ch.messageRemovation(resourcePool, message);
	}

}
