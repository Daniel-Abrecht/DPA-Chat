package chat.eventListenerImpl;

import chat.eventListener.ResourceListener;
import chat.resources.Message;
import chat.resources.ResourcePool;

public class MessageListenerImpl implements ResourceListener<Message> {

	@Override
	public void resourceCreation(ResourcePool<Message> resourcePool,
			Message message) {
		message.getChatRoom().messageCreation(resourcePool, message);
	}

	@Override
	public void resourceChange(ResourcePool<Message> resourcePool,
			Message message) {
		message.getChatRoom().messageChange(resourcePool, message);
	}

	@Override
	public void resourceRemovation(ResourcePool<Message> resourcePool,
			Message message) {
		message.getChatRoom().messageRemovation(resourcePool, message);
	}

}
