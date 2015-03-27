package chat.utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import chat.manager.EndpointManager;
import chat.resources.ChatRoom;
import chat.resources.Message;
import chat.resources.ResourcePool;
import static chat.manager.EndpointMap.endpointMap;

public class Data {
	public static void saveChatlog(File file, ChatRoom chatroom) {
		PrintWriter os = null;
		try {
			file.createNewFile();
			os = new PrintWriter(file);
		} catch ( IOException e) {
			return;
		}
		Collection<EndpointManager> endpoints = endpointMap.getEndpoints();
		for (EndpointManager endpoint : endpoints) {
			ResourcePool<Message> resPool = endpoint
					.getResourcePool(Message.class);
			Collection<Message> messages = resPool.getResources();
			for (Message message : messages) {
				if(message.getChatRoom().hasSameIdentifierAs(chatroom)){
					os.println("--- Message from "+message.getProfil().getName()+" ---");
					os.println(message.getContent()+"\n");
				}
			}
		}
		os.close();
	}
}
