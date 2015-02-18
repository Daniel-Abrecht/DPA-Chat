package chat.event;

import connectionManager.EventHandlerIface;
import chat.eventListener.ChatListener;

public interface ChatEventHandler extends ChatListener, EventHandlerIface<ChatListener> {}
