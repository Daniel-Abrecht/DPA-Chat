package chat.event;

import connectionManager.EventHandlerIface;
import chat.eventListener.UserManagerListener;

public interface UserManagerEventHandler extends UserManagerListener, EventHandlerIface<UserManagerListener> {}
