package chat.eventListenerImpl;

import ui.ChatroomManager;
import chat.eventListener.ResourceListener;
import chat.resources.ChatRoom;
import chat.resources.ResourcePool;

public class ChatRoomListenerImpl implements ResourceListener<ChatRoom> {

	@Override
	public void resourceCreation(ResourcePool<ChatRoom> resourcePool,
			ChatRoom resource) {
		System.out.println("resourceCreation: " + resource);
	}

	@Override
	public void resourceChange(ResourcePool<ChatRoom> resourcePool,
			ChatRoom resource) {
		ChatroomManager.getInstance().update(resource);
		resource.getView().updateUI();
		System.out.println("resourceChange: " + resource);
	}

	@Override
	public void resourceRemovation(ResourcePool<ChatRoom> resourcePool,
			ChatRoom resource) {
		ChatroomManager.getInstance().remove(resource.getId());
		System.out.println("resourceRemovation: " + resource);
	}

}
