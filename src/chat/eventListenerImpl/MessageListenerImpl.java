package chat.eventListenerImpl;

import chat.eventListener.ResourceListener;
import chat.resources.ChatRoom;
import chat.resources.Message;
import chat.resources.ResourcePool;

/**
 * Behandelt Nachrichts events
 * 
 * @author Daniel Abrecht
 * @see chat.eventListener.ResourceListener
 */
public class MessageListenerImpl implements ResourceListener<Message> {

	/**
	 * Leitet event an Chatroom weiter
	 */
	@Override
	public void resourceCreation(ResourcePool<Message> resourcePool,
			Message message) {
		ChatRoom ch = message.getChatRoom();
		if (ch != null)
			ch.messageCreation(resourcePool, message);
	}

	/**
	 * Leitet event an Chatroom weiter
	 */
	@Override
	public void resourceChange(ResourcePool<Message> resourcePool,
			Message message) {
		ChatRoom ch = message.getChatRoom();
		if (ch != null)
			ch.messageChange(resourcePool, message);
	}
	/**
	 * Leitet event an Chatroom weiter
	 */
	@Override
	public void resourceRemovation(ResourcePool<Message> resourcePool,
			Message message) {
		ChatRoom ch = message.getChatRoom();
		if (ch != null)
			ch.messageRemovation(resourcePool, message);
	}

}
