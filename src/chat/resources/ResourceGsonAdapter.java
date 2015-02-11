package chat.resources;

import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.UnknownHostException;

import serialisation.GsonAdapter;
import chat.manager.EndpointManager;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;

import static chat.Chat.connectionManager;

public class ResourceGsonAdapter implements GsonAdapter<Resource> {

	@Override
	public JsonElement serialize(Resource resource, Type type,
			JsonSerializationContext context) {
		JsonObject object = new JsonObject();
		object.addProperty("rid", resource.getId());
		object.addProperty("uid", resource.getResourcePool().getUserManager()
				.getUser().getId());
		InetAddress addr = resource.getResourcePool().getUserManager()
				.getUser().getEndpoint().getAddress();
		object.addProperty("eip", addr.getHostAddress());
		return object;
	}

	@Override
	public Resource deserialize(JsonElement e, Type type,
			JsonDeserializationContext context) throws JsonParseException {
		if(!e.isJsonObject())
			return null;
		JsonObject jsonObj = (JsonObject)e;
		int rid = jsonObj.get("rid").getAsJsonPrimitive().getAsInt();
		byte uid = jsonObj.get("uid").getAsJsonPrimitive().getAsByte();
		String eip = jsonObj.get("eip").getAsJsonPrimitive().getAsString();
		EndpointManager em;
		try {
			em = connectionManager.getEndpointManager(InetAddress.getByName(eip));
		} catch (UnknownHostException e2) {
			e2.printStackTrace();
			return null;
		}
		@SuppressWarnings("unchecked")
		Class<? extends Resource> clazz = (Class<? extends Resource>)type;
		return em.getUserManager(uid).getResourcePool(clazz).getOrCreateResource(rid, clazz);
	}
}
