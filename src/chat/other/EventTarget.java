package chat.other;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public abstract class EventTarget {

	private static class ListenerMethodPair {
		public Object listener;
		public Method method;
	};

	private Map<String, ListenerMethodPair> eventListeners = new HashMap<String, ListenerMethodPair>();

	public void addEventListener(String name, Object listener, Method method) {
		ListenerMethodPair listenerMethodPair = new ListenerMethodPair();
		listenerMethodPair.listener = listener;
		listenerMethodPair.method = method;
		eventListeners.put(name, listenerMethodPair);
	}

	public void dispatchEvent(String name, Object... args) {
		ListenerMethodPair lm = eventListeners.get(name);
		Object allArgs[] = new Object[args.length + 1];
		System.arraycopy(args, 0, allArgs, 1, args.length);
		allArgs[0] = this;
		try {
			lm.method.invoke(lm.listener, allArgs);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
	}

}
