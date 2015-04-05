package chat.utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.nio.file.Path;

import serialisation.BinaryEncoder;
import serialisation.Deserializable;
import serialisation.Expose;
import serialisation.ObjectEncoder;
import chat.Chat;
import chat.manager.EndpointManager;
import chat.resources.ChatRoom;
import chat.resources.Message;
import chat.resources.Profil;
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

	@Deserializable
	public static class ProfileMemoryConfig {
		public static class ProfileTypeGetter implements Expose.TypeGetter {
			@Override
			public Class<?> getType(Object o, Field f) {
				return Profil.class;
			}
		};
		@Expose(position=0,getTypeGetterType=ProfileTypeGetter.class)
		public List<Profil> profiles;
	};
	
	final static Path profileStorageFile = Paths.get(System.getProperty("user.home") + "/.DPA-Chat.profile");

	public static void loadProfiles() {
		if(Files.notExists(profileStorageFile, LinkOption.NOFOLLOW_LINKS))
			return;
		byte buffer[];
		try {
			buffer = Files.readAllBytes(profileStorageFile);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		EndpointManager localEndpointManager = Chat.connectionManager.getLocalEndpointManager();
		ObjectEncoder<byte[]> encoder = new BinaryEncoder();
		ProfileMemoryConfig pmc = encoder.decode(buffer, ProfileMemoryConfig.class);
		if (pmc == null)
			return;
		for (Profil profil : pmc.profiles) {
			profil.update(localEndpointManager);
			profil.updateRemote();
		}
	}

	public static void saveProfiles() {
		ObjectEncoder<byte[]> encoder = new BinaryEncoder();
		ProfileMemoryConfig pmc = new ProfileMemoryConfig();
		pmc.profiles = new ArrayList<Profil>(Chat.connectionManager.getLocalEndpointManager().getResourcePool(Profil.class).getResources());
		byte[] buffer = encoder.encode(pmc);
		try {
			Files.write(profileStorageFile, buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
