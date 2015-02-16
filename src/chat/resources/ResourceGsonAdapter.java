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
import connectionManager.Endpoint;
import static chat.manager.EndpointMap.endpointMap;
import static chat.Chat.connectionManager;

public class ResourceGsonAdapter implements GsonAdapter<Resource> {

	private Endpoint remote;

	public ResourceGsonAdapter(Object... args) {
		if (args != null && args.length >= 1 && args[0] instanceof Endpoint)
			this.remote = (Endpoint) args[0];
	}

	@Override
	public JsonElement serialize(Resource resource, Type type,
			JsonSerializationContext context) {
		JsonObject object = new JsonObject();
		object.addProperty("rid", resource.getId());
		InetAddress addr = resource.getEndpointManager().getEndpoint()
				.getAddress();
		if (!resource.isLocal())
			object.addProperty("eip", addr.getHostAddress());
		return object;
	}

	@Override
	public Resource deserialize(JsonElement e, Type type,
			JsonDeserializationContext context) throws JsonParseException {
		if (!e.isJsonObject())
			return null;
		JsonObject jsonObj = (JsonObject) e;
		int rid = jsonObj.get("rid").getAsJsonPrimitive().getAsInt();
		EndpointManager em;
		if (jsonObj.has("eip")) {
			String eip = jsonObj.get("eip").getAsJsonPrimitive().getAsString();
			try {
				em = connectionManager.getEndpointManager(InetAddress
						.getByName(eip));
			} catch (UnknownHostException e2) {
				e2.printStackTrace();
				return null;
			}
		} else {
			em = endpointMap.sync(remote);
		}
		@SuppressWarnings("unchecked")
		Class<? extends Resource> clazz = (Class<? extends Resource>) type;
		return em.getResourcePool(clazz).getOrCreateResource(rid, clazz);
	}
}
