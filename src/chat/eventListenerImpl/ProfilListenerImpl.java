package chat.eventListenerImpl;

import ui.ProfilManager;
import ui.ProfilView;
import chat.eventListener.ResourceListener;
import chat.resources.Profil;
import chat.resources.ResourcePool;

/**
 * Behandelt Profil events
 * 
 * @author Daniel Abrecht
 * @see chat.eventListener.ResourceListener
 */
public class ProfilListenerImpl implements ResourceListener<Profil> {

	@Override
	public void resourceCreation(ResourcePool<Profil> resourcePool,
			Profil resource) {
		System.out.println("resourceCreation: " + resource);
	}

	/**
	 * Aktualisiert UI
	 */
	@Override
	public void resourceChange(ResourcePool<Profil> resourcePool,
			Profil resource) {
		if (resource.isLocal())
			ProfilManager.getInstance().update(resource);
		ProfilView.updateViewsWithProfile(resource);
		System.out.println("resourceChange: " + resource);
	}

	/**
	 * Aktualisiert UI
	 */
	@Override
	public void resourceRemovation(ResourcePool<Profil> resourcePool,
			Profil resource) {
		if (resource.isLocal())
			ProfilManager.getInstance().remove(resource.getId());
		System.out.println("resourceRemovation: " + resource);
	}

}
