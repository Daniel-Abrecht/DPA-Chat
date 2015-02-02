package chat.event;

import chat.eventListener.ResourceListener;
import chat.resources.Resource;

public interface ResourceEventHandler<T extends Resource> extends ResourceListener<T>, EventHandlerIface<ResourceListener<T>> {}
