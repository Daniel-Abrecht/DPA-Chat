package chat.eventListener;

import chat.resources.Message;
import chat.resources.ResourcePool;

public interface ChatRoomListener {
	public void messageCreation(ResourcePool<Message> resourcePool, Message message);
	public void messageChange(ResourcePool<Message> resourcePool, Message message);
	public void messageRemovation(ResourcePool<Message> resourcePool, Message message);
}
