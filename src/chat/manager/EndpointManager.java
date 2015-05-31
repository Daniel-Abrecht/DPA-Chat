package chat.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import chat.eventListenerImpl.ChatRoomListenerImpl;
import chat.eventListenerImpl.MessageListenerImpl;
import chat.eventListenerImpl.ProfilListenerImpl;
import chat.resources.ChatRoom;
import chat.resources.Message;
import chat.resources.Profil;
import chat.resources.Resource;
import chat.resources.ResourcePool;
import connectionManager.Endpoint;

/**
 * Klasse zur verwaltung eines Endpoints und deren RessourcPools
 * 
 * @author Daniel Abrecht
 */
public class EndpointManager {

	private Endpoint endpoint;
	private Map<Class<? extends Resource>, ResourcePool<? extends Resource>> resourcePools = new HashMap<Class<? extends Resource>, ResourcePool<? extends Resource>>();
	private List<ResourcePool<? extends Resource>> resourcePoolList = new ArrayList<ResourcePool<? extends Resource>>();
	private int checksum = 0;

	/**
	 * Konstruktor des EndpointManagers
	 * 
	 * @param e Der zu verwaltende Endpoint
	 */
	public EndpointManager(Endpoint e) {
		this.endpoint = e;
		
		ResourcePool<Profil> profilResourcePool = new ResourcePool<Profil>(this);
		profilResourcePool.addEventListener(new ProfilListenerImpl());
		resourcePools.put(Profil.class, profilResourcePool);
		resourcePoolList.add(profilResourcePool);

		ResourcePool<ChatRoom> chatRoomResourcePool = new ResourcePool<ChatRoom>(this);
		chatRoomResourcePool.addEventListener(new ChatRoomListenerImpl());
		resourcePools.put(ChatRoom.class, chatRoomResourcePool);
		resourcePoolList.add(chatRoomResourcePool);

		ResourcePool<Message> messageResourcePool = new ResourcePool<Message>(this);
		messageResourcePool.addEventListener(new MessageListenerImpl());
		resourcePools.put(Message.class, messageResourcePool);
		resourcePoolList.add(messageResourcePool);
	}

	/**
	 * Getter für Endpoint
	 * @return Der endpoint
	 */
	public Endpoint getEndpoint() {
		return endpoint;
	}

	/**
	 * Hinzufügen einer Ressource zum entsprechenden RessourcePool
	 * 
	 * @param resource Die Ressource
	 * @return Ob das Hinzufügen erfolgreich war
	 */
	public boolean tryAdd(Resource resource) {
		Class<? extends Resource> resClass = resource.getClass();
		@SuppressWarnings("unchecked")
		ResourcePool<Resource> resPool = (ResourcePool<Resource>) resourcePools.get(resClass);
		if (resPool == null)
			return false;
		resPool.register(resource);
		return true;
	}

	/**
	 * Getter für RessourcePool
	 * 
	 * @param resClass Die classe die vom RessourcePool verwalteet wird
	 * @return Der RessourcePool
	 */
	@SuppressWarnings("unchecked")
	public <T extends Resource> ResourcePool<T> getResourcePool(Class<T> resClass) {
		return (ResourcePool<T>) resourcePools.get(resClass);
	}

	private Object checksumLock = new Object();
	
	/**
	 * Aktualisieren der Checksumme
	 * 
	 * @param oldChecksum alte Checksumme eines Ressourcepools
	 * @param newChecksum neue Checksumme eines Ressourcepools
	 */
	public void updateChecksum(int oldChecksum, int newChecksum) {
		synchronized (checksumLock) {
			checksum = checksum - oldChecksum + newChecksum;
		}
	}

	/**
	 * Getter für Checksumme
	 * 
	 * @return Die checksumme
	 */
	public int getChecksum() {
		return checksum;
	}

	/**
	 * Getter für eine Liste aller RessourcePools
	 * 
	 * @return die RessourcePools
	 */
	public List<ResourcePool<? extends Resource>> getResourcePoolList() {
		return resourcePoolList;
	}

}