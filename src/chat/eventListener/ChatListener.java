package chat.eventListener;

import chat.resources.Profil;

public interface ChatListener {
	public void currentProfilChanged(Profil o, Profil n);
}
