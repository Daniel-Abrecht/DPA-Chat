package chat.event;

import connectionManager.EventHandlerIface;
import chat.eventListener.EndpointListener;

public interface EndpointEventHandler extends EndpointListener, EventHandlerIface<EndpointListener> {}
