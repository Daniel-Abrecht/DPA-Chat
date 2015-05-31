package chat.event;

import connectionManager.EventHandlerIface;
import chat.eventListener.ChatListener;

/**
 * @see connectionManager.EventHandlerIface
 * @see connectionManager.EventHandler
 */
public interface ChatEventHandler extends ChatListener, EventHandlerIface<ChatListener> {}
