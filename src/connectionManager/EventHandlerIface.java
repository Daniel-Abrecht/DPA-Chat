package connectionManager;

/**
 * Standardmethoden eines Eventhandlers, eine Instanz, welche mit
 * connectionManager.EventHandler.createEventHandler erstellt wurde,
 *  ist immer auch ein Event Listener vom type T
 *  
 *  Wenn eine Methode vom type T bei einer mit connectionManager.EventHandler.createEventHandler
 *  erstellten instanz aufgerufen wird, wird die methode bei allen 
 *  registrierten EventListenern aufgerufen
 * 
 * @author Daniel Abrecht
 *
 * @see connectionManager.EventHandler
 * @see connectionManager.EventHandler.createEventHandler
 * @param <T> Interface für behandelten EventListener
 */
public interface EventHandlerIface<T> {
	/**
	 * Fügt einen neuen Event listener hinzu
	 * @param endpointListener Der neue Event listener
	 */
	public void addEventListener(T endpointListener);
}
