package chat.event;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class EventHandler<T> implements EventHandlerIface<T>,
		java.lang.reflect.InvocationHandler {

	private List<T> eventListeners = new ArrayList<T>();

	public void addEventListener(T endpointListener) {
		eventListeners.add(endpointListener);
	}

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

	@SuppressWarnings("unchecked")
	public static <T> T createEventHandler(Class<T> eventHandlerClass) {
		return (T) Proxy.newProxyInstance(eventHandlerClass.getClassLoader(),
				new Class[] { eventHandlerClass }, new EventHandler<Object>());
	}

}
