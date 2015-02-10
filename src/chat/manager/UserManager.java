package chat.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import chat.eventListenerImpl.ChatRoomListenerImpl;
import chat.eventListenerImpl.MessageListenerImpl;
import chat.eventListenerImpl.ProfilListenerImpl;
import chat.resources.ChatRoom;
import chat.resources.Message;
import chat.resources.Profil;
import chat.resources.Resource;
import chat.resources.ResourcePool;
import connectionManager.User;

public class UserManager {

	private User user;
	private Map<Class<? extends Resource>, ResourcePool<? extends Resource>> resourcePools = new ConcurrentHashMap<Class<? extends Resource>, ResourcePool<? extends Resource>>();

	public UserManager(User user) {
		this.user = user;

		ResourcePool<Profil> profilResourcePool = new ResourcePool<Profil>(this);
		profilResourcePool.addEventListener(new ProfilListenerImpl());
		resourcePools.put(Profil.class, profilResourcePool);

		ResourcePool<ChatRoom> chatRoomResourcePool = new ResourcePool<ChatRoom>(this);
		chatRoomResourcePool.addEventListener(new ChatRoomListenerImpl());
		resourcePools.put(ChatRoom.class, chatRoomResourcePool);

		ResourcePool<Message> messageResourcePool = new ResourcePool<Message>(this);
		messageResourcePool.addEventListener(new MessageListenerImpl());
		resourcePools.put(Message.class, messageResourcePool);

	}

	public User getUser() {
		return user;
	}

	public boolean tryAdd(Resource resource) {
		Class<? extends Resource> resClass = resource.getClass();
		@SuppressWarnings("unchecked")
		ResourcePool<Resource> resPool = (ResourcePool<Resource>) resourcePools.get(resClass);
		if (resPool == null)
			return false;
		resPool.register(resource);
		return true;
	}

	@SuppressWarnings("unchecked")
	public <T extends Resource> ResourcePool<T> getResourcePool(Class<T> resClass) {
		return (ResourcePool<T>) resourcePools.get(resClass);
	}

	@Override
	public String toString() {
		return "UserManager [user=" + user + ", resourcePools=" + resourcePools
				+ "]";
	}

	void setUser(User user) {
		this.user = user;
	}

}
