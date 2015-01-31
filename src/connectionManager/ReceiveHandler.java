package connectionManager;

public interface ReceiveHandler {

	public Class<?> getHandledClass();
	public void onReceive(Container container,RemoteEndpoint e, User user);

}
