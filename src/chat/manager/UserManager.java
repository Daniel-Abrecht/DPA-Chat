package chat.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
		resourcePools.put(Profil.class, new ResourcePool<Profil>());
		resourcePools.put(ChatRoom.class, new ResourcePool<ChatRoom>());
		resourcePools.put(Message.class, new ResourcePool<Message>());
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

}
