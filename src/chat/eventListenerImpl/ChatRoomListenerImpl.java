package chat.eventListenerImpl;

import chat.eventListener.ResourceListener;
import chat.resources.ChatRoom;
import chat.resources.ResourcePool;
import static chat.Chat.chatroomManager;

public class ChatRoomListenerImpl implements ResourceListener<ChatRoom> {

	@Override
	public void resourceCreation(ResourcePool<ChatRoom> resourcePool,
			ChatRoom resource) {
		System.out.println("resourceCreation: " + resource);
	}

	@Override
	public void resourceChange(ResourcePool<ChatRoom> resourcePool,
			ChatRoom resource) {
		chatroomManager.update(resource);
		System.out.println("resourceChange: " + resource);
	}

	@Override
	public void resourceRemovation(ResourcePool<ChatRoom> resourcePool,
			ChatRoom resource) {
		chatroomManager.remove(resource.getId());
		System.out.println("resourceRemovation: " + resource);
	}

}
