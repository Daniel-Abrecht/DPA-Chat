package chat.resources;

import chat.Chat;
import chat.checksum.HashCalculator;
import chat.manager.EndpointManager;
import serialisation.Expose;

/**
 * Hilfsklasse zur (De)serialisierung von Ressourcen,
 * Repräsentiert eine Ressource
 * 
 * @author Daniel Abrecht
 */
public abstract class Resource {
	private ResourcePool<Resource> resourcePool;
	@Expose(position = 0)
	private Integer id = -1;
	@Preserve
	private int checksum = 0;

	/**
	 * Aktualisieren der Daten der Ressource im ResourcePool
	 * @param parent RessourcePool, der die alte Resource enthalten könnte
	 * @return Referenz auf aktuelle Resource
	 */
	public Resource update(ResourcePool<Resource> parent) {
		if (this.resourcePool != null)
			deregister();
		this.resourcePool = parent;
		if (id < 0) {
			id = parent.register(this);
			return this;
		} else {
			return parent.update(this, id);
		}
	}

	/**
	 * Prüft, ob die Ressource in einem RessourcePool enthalten ist
	 */
	public boolean isRegistred() {
		return resourcePool != null;
	}

	/**
	 * Aktualisieren der Daten der Ressource
	 */
	@SuppressWarnings("unchecked")
	public Resource update(EndpointManager endpointManager) {
		return update((ResourcePool<Resource>) endpointManager
				.getResourcePool(this.getClass()));
	}

	/**
	 * Ressource aus Ressourcepool entfernen/löschen
	 */
	public void deregister() {
		if(resourcePool==null)
			return;
		resourcePool.deregister(this);
		id = -1;
	}

	/**
	 * Getter für id
	 * @return die Id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Verfollständigen/Aktualisieren/registrieren der Ressource und deren Daten
	 * @return Aktualisierte Ressource
	 */
	public Resource reconstruct(EndpointManager em) {
		Resource r = update(em);
		r.updateChecksum();
		return r;
	}

	/**
	 * @see Resource.reconstruct
	 */
	public static Resource reconstruct(Object x, EndpointManager em) {
		return ((Resource) x).reconstruct(em);
	}

	/**
	 * Alle änderungen den anderen Endpoints mitteilen
	 * @return aktuelle ressource
	 */
	public Resource updateRemote() {
		Resource r = update();
		Chat.connectionManager.send(r);
		r.updateChecksum();
		return r;
	}

	private Resource update() {
		return resourcePool.update(this, id);
	}

	/**
	 * Getter für ResourcePool
	 * @return Der resourcepool
	 */
	public ResourcePool<Resource> getResourcePool() {
		return resourcePool;
	}

	/**
	 * Getter für EndpointMAnager
	 * @return der EndpointManager
	 */
	public EndpointManager getEndpointManager() {
		if(resourcePool==null)
			return null;
		return resourcePool.getEndpointManager();
	}

	/**
	 * Prüft, ob die Ressource dem eigenen Endpoint gehört
	 */
	public boolean isLocal() {
		EndpointManager e = getEndpointManager();
		return e == Chat.connectionManager.getLocalEndpointManager();
	}

	/**
	 * Getter für Checksumme
	 * @return die Checksumme
	 */
	public int getChecksum() {
		return checksum;
	}

	/**
	 * Checksumme aktualisieren
	 */
	void updateChecksum() {
		int oldChecksum;
		Resource old = getResourcePool().getResource(id);
		synchronized (old) {
			if (old == null)
				return;
			oldChecksum = old.getChecksum();
			checksum = HashCalculator.calcHash(this);
		}
		getResourcePool().updatePublicHashCode(oldChecksum, checksum);
	}

	/**
	 * Check if two resources have the same identifying fields
	 * 
	 * @param res The other resource
	 * @return Returns true if both resource share the same types, IDs, and EndpointManagers 
	 */
	public boolean hasSameIdentifierAs(Resource res) {
		return (
				this != null && res != null &&
				this.getClass().equals(res.getClass()) &&
				this.getId() != null && res.getId() != null &&
				this.isRegistred() && res.isRegistred() &&
				this.getId().equals(res.getId()) &&
				this.getEndpointManager().equals(res.getEndpointManager())
		);
	}
	
	/**
	 * Vergleichsfunktion zur Sortierung von Ressourcen nach Identifier
	 * @param res Zu vergleichende Resource
	 * @return Distanz
	 */
	public int compareIdentifier(Resource res) {
		if (this == res)
			return 0;
		if (res == null)
			return 1;
		Integer a = this.getId();
		Integer b = res.getId();
		if(a==null&&b==null)
			return 0;
		if(a==null)
			return -1;
		if(b==null)
			return 1;
		return a.compareTo(b);
	}

}
