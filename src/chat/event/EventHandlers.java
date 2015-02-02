package chat.event;

import static chat.event.EventHandler.createEventHandler;

public abstract class EventHandlers {
	public static EndpointEventHandler endpointEventHandler = createEventHandler(EndpointEventHandler.class);
}
