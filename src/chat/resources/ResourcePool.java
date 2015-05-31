package chat.resources;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import serialisation.Expose;
import connectionManager.EventHandlerIface;
import chat.event.ResourceEventHandler;
import chat.eventListener.ResourceListener;
import chat.manager.EndpointManager;
import static connectionManager.EventHandler.createEventHandler;

/**
 * Klasse zur Verwaltung von Ressourcen
 * 
 * @author Daniel Abrecht
 *
 * @param <T> Typ der zu verwaltenden Ressourcen
 */
public class ResourcePool<T extends Resource> implements
		EventHandlerIface<ResourceListener<T>> {

	private int checksum = 0;
	private int resIdCounter = 0;
	private Map<Integer, T> resources = new ConcurrentHashMap<Integer, T>();

	private final EndpointManager endpointManager;

	@SuppressWarnings("unchecked")
	private ResourceEventHandler<T> resourceEventHandler = createEventHandler(ResourceEventHandler.class);
	
	private Object checksumLock = new Object();

	/**
	 * Contains the network-wide valid hascode for some kind of resource.
	 * 
	 * If the network-wide reflection of an ressource in this ressourcepool
	 * changes, for example by transmitting a new version of the object, this
	 * method must be invoked to update the hash-code.
	 * 
	 * Todo: Find a solution to prevent double transmissions of objects due to
	 * race-conditions between the transmission of the hash-code information and
	 * its change because of the transmission of a changed object. The order in
	 * which the transmitted object and the hascode arrives is inconsistent.
	 * Possible solutions may be a small hash history with an expirtion time for
	 * each hash change info.
	 * 
	 * @param oldChecksum
	 *            the old checksum of the resource
	 * @param newChechsum
	 *            the new checksum of the resource
	 */
	public void updatePublicHashCode(int oldChecksum,
			int newChechsum) {
		synchronized (checksumLock) {
			int oldCh = checksum;
			checksum = checksum - oldChecksum + newChechsum;
			getEndpointManager().updateChecksum(oldCh, checksum);
		}
	}

	/**
	 * Getter für checksumme
	 * @return Die checksumme des RessourcePools
	 */
	public int getChecksum() {
		return checksum;
	}

	/**
	 * Setter für Checksumme
	 * @param publicHashCode neue Checksumme
	 */
	public void setChecksum(int publicHashCode) {
		this.checksum = publicHashCode;
	}

	/**
	 * Konstruktor
	 * @param endpointManager Der entpointManager der diesen RessourcePool instanzierte
	 */
	public ResourcePool(EndpointManager endpointManager) {
		this.endpointManager = endpointManager;
	}

	/**
	 * Ressource hinzufügen
	 * @param resource Hinzuzufügende Ressource
	 * @return Id der Ressource
	 */
	public Integer register(T resource) {
		resources.put(resIdCounter, resource);
		return resIdCounter++;
	}

	/**
	 * Aktualisieren einer Ressource im Ressourcepool
	 * @param resource Die Resource
	 * @param id Die ID der Resource
	 * @return Die Aktualisierte ressource
	 */
	public T update(T resource, int id) {
		T old = resources.get(id);
		synchronized (old == null ? resource : old) {
			if (old == null) {
				resources.put(id, resource);
				resourceEventHandler.resourceCreation(this, resource);
			} else {
				Class<?> clazz = resource.getClass();
				Class<?> classOld = old.getClass();
				do {
					if (!classOld.isAssignableFrom(clazz))
						continue;
					Field[] fields = classOld.getDeclaredFields();
					for (int i = 0; i < fields.length; i++) {
						Field field = fields[i];
						if (field.getAnnotation(Preserve.class) != null
								&& field.getAnnotation(Expose.class) == null)
							continue;
						field.setAccessible(true);
						try {
							if (field.get(resource) == null
									&& field.getAnnotation(Preserve.class) != null)
								continue;
							Object nv = field.get(resource);
							field.set(old, nv);
						} catch (IllegalArgumentException
								| IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				} while ((classOld = classOld.getSuperclass()) != null);
			}
			resourceEventHandler.resourceChange(this, old == null ? resource
					: old);
		}
		return old == null ? resource : old;
	}

	/**
	 * Entfernen einer Ressource im RessourcePool
	 * @param resource
	 */
	public void deregister(T resource) {
		resources.remove(resource.getId());
		resourceEventHandler.resourceRemovation(this, resource);
	}

	/**
	 * Gibt alle Ressourcen zurück
	 * @return Collection aller ressourcen
	 */
	public Collection<T> getResources() {
		return resources.values();
	}

	/**
	 * Getter für Endpoint manager
	 * @return der EndpointManager
	 */
	public EndpointManager getEndpointManager() {
		return endpointManager;
	}

	/**
	 * Eventlistenter zu Eventhandler Hinzufügen
	 * @see connectionManager.EventHandlerIface.addEventListener
	 */
	@Override
	public void addEventListener(ResourceListener<T> endpointListener) {
		resourceEventHandler.addEventListener(endpointListener);
	}

	/**
	 * Getter für Ressource
	 * @param id Id der Ressource
	 * @return Die Ressource
	 */
	public T getResource(Integer id) {
		return resources.get(id);
	}

	/**
	 * Holen oder Erstellen einer Ressource
	 * @param rid id der Ressource auf dem Endpoint dem das Original der Resource gehört
	 * @param clazz Typ der Ressource
	 * @return Die Resource
	 */
	public T getOrCreateResource(Integer rid, Class<? extends Resource> clazz) {
		T res = resources.get(rid);
		if (res == null)
			try {
				@SuppressWarnings("unchecked")
				T newres = (T) clazz.newInstance();
				res = newres = update(newres, rid);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
				return null;
			}
		return res;
	}

	/**
	 * Interface zum Durchsuchen des RessourcePools
	 * 
	 * @author Daniel Abrecht
	 * @param <T> Typ de Ressource
	 */
	public static interface BooleanQuery <T extends Resource> {
		/**
		 * Prüft, ob irgendeine Resource im ResourcePool den Anforderungen der Methode Genügt
		 * @param resource Die zu Prüfende ressource
		 * @return Resultat
		 */
		public boolean checkTruth(T resource);
	};
	
	/**
	 *  Prüft, ob irgendeine Resource im ResourcePool den Anforderungen der Methode Genügt
	 * @param res typ der Ressource
	 * @param q Bedingung
	 * @return Ergebnis
	 */
	public  boolean queryTruth( Class<T> res, BooleanQuery<T> q ){
		Collection<T> resources = this.getResources();
		for(T resource : resources)
			if(q.checkTruth(resource))
				return true;
		return false;
	}

}
