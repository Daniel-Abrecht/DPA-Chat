package chat.resources;

import java.lang.reflect.Type;

import serialisation.GsonAdapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

public class ResourceGsonAdapter implements GsonAdapter<Resource> {

	@Override
	public JsonElement serialize(Resource arg0, Type arg1,
			JsonSerializationContext arg2) {
		return new JsonPrimitive("1) test");
	}

	@Override
	public Resource deserialize(JsonElement arg0, Type arg1,
			JsonDeserializationContext arg2) throws JsonParseException {
		return null;
	}

}
