package connectionManager;

/**
 * Dieses interface spezifiziert die Schnittstelle zur Behandlung empfangener Objekte
 * 
 * @author Daniel Abrecht
 */
public interface ReceiveHandler {

	/**
	 * Getter für zu behandelnden typ
	 */
	public Class<?> getHandledClass();

	/**
	 * Behandelt Empfagene Objekte
	 * 
	 * @param container Container, der das Objekt enthält
	 * @param e Der Endpoint, von welchem dieses gesendet wurde
	 */
	public void onReceive(Container container, Endpoint e);

}
