package chat.event;

import connectionManager.EventHandlerIface;
import chat.eventListener.EndpointListener;

/**
 * @see connectionManager.EventHandlerIface
 * @see connectionManager.EventHandler
 */
public interface EndpointEventHandler extends EndpointListener, EventHandlerIface<EndpointListener> {}
