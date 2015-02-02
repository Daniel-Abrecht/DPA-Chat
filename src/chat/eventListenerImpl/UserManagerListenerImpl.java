package chat.eventListenerImpl;

import chat.eventListener.UserManagerListener;
import chat.manager.EndpointManager;
import chat.manager.UserManager;

public class UserManagerListenerImpl implements UserManagerListener {

	@Override
	public void userManagerCreation(EndpointManager endpointManager, UserManager userManager) {
		// TODO Auto-generated method stub
		System.out.println("userManagerCreation: " + userManager);
	}

	@Override
	public void userManagerChange(EndpointManager endpointManager, UserManager userManager) {
		// TODO Auto-generated method stub
		System.out.println("userManagerChange: " + userManager);
	}

	@Override
	public void userManagerRemovation(EndpointManager endpointManager, UserManager userManager) {
		// TODO Auto-generated method stub
		System.out.println("userManagerRemovation: " + userManager);
	}

}
