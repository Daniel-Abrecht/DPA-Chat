package chat.eventListenerImpl;

import chat.eventListener.ResourceListener;
import chat.resources.ChatRoom;
import chat.resources.ResourcePool;
import static chat.Chat.chatroomManager;

public class ChatRoomListenerImpl implements ResourceListener<ChatRoom> {

	@Override
	public void resourceCreation(ResourcePool<ChatRoom> resourcePool,
			ChatRoom resource) {
		// TODO Auto-generated method stub
		System.out.println("resourceCreation: " + resource);
	}

	@Override
	public void resourceChange(ResourcePool<ChatRoom> resourcePool,
			ChatRoom resource) {
		chatroomManager.update();
		System.out.println("resourceChange: " + resource);
	}

	@Override
	public void resourceRemovation(ResourcePool<ChatRoom> resourcePool,
			ChatRoom resource) {
		// TODO Auto-generated method stub
		System.out.println("resourceRemovation: " + resource);
	}

}
