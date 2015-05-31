package connectionManager;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * Generische klass um EventHandler aus Interfacen mithilfe von Java Proxies zu generieren/instanzieren
 * 
 * @author Daniel Abrecht
 *
 * @param <T> Interface des event handlers
 */
public class EventHandler<T> implements EventHandlerIface<T>,
		java.lang.reflect.InvocationHandler {

	private List<T> eventListeners = new ArrayList<T>();

	/**
	 * Eventlistener hinzufügen, von Event handler aus aufrufbar
	 */
	public void addEventListener(T endpointListener) {
		eventListeners.add(endpointListener);
	}

	/**
	 * Funktionsaufrufe beim EventHandler behandeln,
	 * funktionsaufrufe des EventHandlerIface umleiten
	 * 
	 * @param target Objekt, auf welchem die Funktion aufgerufen werden sollte
	 * @param method aufzurufende Methode
	 * @param args Argumente des Funktionsaufrufs
	 */
	@Override
	public Object invoke(Object target, Method method, Object[] args)
			throws Throwable {
		if (method.getDeclaringClass() == EventHandlerIface.class) {
			method.invoke(this, args);
		} else {
			for (T eventListener : eventListeners) {
				method.invoke(eventListener, args);
			}
		}
		return null;
	}

	/**
	 * EventHandler Factory
	 * 
	 * @param eventHandlerClass
	 * @return Ein EventHandler
	 */
	@SuppressWarnings("unchecked")
	public static <T> T createEventHandler(Class<T> eventHandlerClass) {
		return (T) Proxy.newProxyInstance(eventHandlerClass.getClassLoader(),
				new Class[] { eventHandlerClass }, new EventHandler<Object>());
	}

}
