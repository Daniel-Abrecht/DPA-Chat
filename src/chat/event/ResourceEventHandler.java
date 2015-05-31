package chat.event;

import connectionManager.EventHandlerIface;
import chat.eventListener.ResourceListener;
import chat.resources.Resource;

/**
 * @see connectionManager.EventHandlerIface
 * @see connectionManager.EventHandler
 */
public interface ResourceEventHandler<T extends Resource> extends ResourceListener<T>, EventHandlerIface<ResourceListener<T>> {}
