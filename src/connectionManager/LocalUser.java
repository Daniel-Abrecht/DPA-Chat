package connectionManager;

public class LocalUser extends User {
	private ConnectionManager connectionManager;

	public LocalUser(ConnectionManager connectionManager) {
		super();
		this.connectionManager = connectionManager;
	}

	public void send(Object o) {
		connectionManager.send(this, o);
	}

}
