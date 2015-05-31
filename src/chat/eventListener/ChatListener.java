package chat.eventListener;

import chat.resources.Profil;

/**
 * Globaleevents der ChatApp
 * 
 * @see connectionManager.EventHandlerIface
 * @see connectionManager.EventHandler
 */
public interface ChatListener {
	/**
	 * Behandelt Profiländerungen
	 * @param o Altes profil
	 * @param n Neues profil
	 */
	public void currentProfilChanged(Profil o, Profil n);
}
