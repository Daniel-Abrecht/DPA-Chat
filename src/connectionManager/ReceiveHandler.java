package connectionManager;

public interface ReceiveHandler {

	public Class<?> getHandledClass();

	public void onReceive(Container container, Endpoint e);

}
