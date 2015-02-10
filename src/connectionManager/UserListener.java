package connectionManager;

public interface UserListener {
	public void userCreation(User user);
	public void userChange(User user);
	public void userRemovation(User user);
}
