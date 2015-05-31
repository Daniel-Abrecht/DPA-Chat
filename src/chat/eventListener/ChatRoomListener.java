package chat.eventListener;

import chat.resources.Message;
import chat.resources.ResourcePool;

/**
 * Behandelt events die ChatRooms betreffen
 * 
 * @see connectionManager.EventHandlerIface
 * @see connectionManager.EventHandler
 */
public interface ChatRoomListener {
	
	/**
	 * Behandelt neue Nachrichten
	 * @param resourcePool resourcePool der nachricht
	 * @param message Die nachricht
	 */
	public void messageCreation(ResourcePool<Message> resourcePool, Message message);
	
	/**
	 * Behandelt die änderung einer Nachricht
	 * @param resourcePool resourcePool der nachricht
	 * @param message Die nachricht
	 */
	public void messageChange(ResourcePool<Message> resourcePool, Message message);

	/**
	 * Behandelt das Löschen einer Nachricht
	 * @param resourcePool resourcePool der nachricht
	 * @param message Die nachricht
	 */
	public void messageRemovation(ResourcePool<Message> resourcePool, Message message);
}
