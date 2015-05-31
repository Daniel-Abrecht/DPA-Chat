package chat.eventListener;

import chat.resources.Resource;
import chat.resources.ResourcePool;

/**
 * Behandelt events die Resourcen von type T betreffen
 * 
 * @see connectionManager.EventHandlerIface
 * @see connectionManager.EventHandler
 */
public interface ResourceListener<T extends Resource> {
	
	/**
	 * Behandelt neue Ressourcen
	 * 
	 * @param resourcePool RessourcePool der Ressource
	 * @param resource die neue Ressource
	 */
	public void resourceCreation(ResourcePool<T> resourcePool, T resource);

	/**
	 * Behandelt geänderte Ressourcen
	 * 
	 * @param resourcePool RessourcePool der Ressource
	 * @param resource die Ressource
	 */
	public void resourceChange(ResourcePool<T> resourcePool, T resource);
	
	/**
	 * Behandelt gelöschte Ressourcen
	 * 
	 * @param resourcePool RessourcePool der Ressource
	 * @param resource die gelöschte Ressource
	 */
	public void resourceRemovation(ResourcePool<T> resourcePool, T resource);
}
