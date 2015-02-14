package chat.eventListenerImpl;

import ui.ProfilManager;
import chat.eventListener.ResourceListener;
import chat.resources.Profil;
import chat.resources.ResourcePool;

public class ProfilListenerImpl implements ResourceListener<Profil> {

	@Override
	public void resourceCreation(ResourcePool<Profil> resourcePool,
			Profil resource) {
		System.out.println("resourceCreation: " + resource);
	}

	@Override
	public void resourceChange(ResourcePool<Profil> resourcePool,
			Profil resource) {
		ProfilManager.getInstance().update(resource);
		System.out.println("resourceChange: " + resource);
	}

	@Override
	public void resourceRemovation(ResourcePool<Profil> resourcePool,
			Profil resource) {
		ProfilManager.getInstance().remove(resource.getId());
		System.out.println("resourceRemovation: " + resource);
	}

}
