package chat.eventListener;

import chat.manager.EndpointManager;
import chat.manager.UserManager;

public interface UserManagerListener {
	public void userManagerCreation(EndpointManager endpointManager, UserManager userManager);
	public void userManagerChange(EndpointManager endpointManager, UserManager userManager);
	public void userManagerRemovation(EndpointManager endpointManager, UserManager userManager);
}
