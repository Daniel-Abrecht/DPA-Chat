package connectionManager;

import java.lang.reflect.Type;

import serialisation.AnnotationProcessorAdapter;
import serialisation.Deserializable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import serialisation.Expose;

public class Container {

	@Expose
	private String type;
	@Expose
	private Object object;

	public Container(Object o) {
		Class<?> c = o.getClass();
		do
			if (c.getAnnotation(Deserializable.class) != null)
				break;
		while ((c = c.getSuperclass()) != null);
		this.type = (c == null) ? "java.lang.Object" : c.getName();
		this.object = o;
	}

	static private class ContainerDeserializer implements
			JsonDeserializer<Container> {
		public Container deserialize(JsonElement json, Type typeOfT,
				JsonDeserializationContext context) throws JsonParseException {
			Container c = null;
			try {
				String typeString = json.getAsJsonObject().get("type")
						.getAsJsonPrimitive().getAsString();
				Class<?> objectType = Class.forName(typeString);
				c = new Container(context.deserialize(json.getAsJsonObject()
						.get("object"), objectType));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return c;
		}
	}

	public static Container parse(String s, Object... o) {
		Gson gson = new GsonBuilder()
				.registerTypeHierarchyAdapter(Object.class,
						new AnnotationProcessorAdapter(o))
				.registerTypeAdapter(Container.class,
						new ContainerDeserializer()).create();
		Container c = gson.fromJson(s, Container.class);
		return c;
	}

	public String getTypeString() {
		return type;
	}

	public Object getObject() {
		return object;
	}

}
