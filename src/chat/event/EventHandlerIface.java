package chat.event;

public interface EventHandlerIface<T> {
	public void addEventListener(T endpointListener);
}
