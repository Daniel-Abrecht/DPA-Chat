package serialisation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

public class AnnotationProcessorAdapter implements GsonAdapter<Object> {

	private Object[] args;
	
	public AnnotationProcessorAdapter(Object... args){
		this.args = args;
	}
	
	@Override
	public JsonElement serialize(Object object, Type type,
			JsonSerializationContext context) {
		final JsonObject obj = new JsonObject();
		if (object instanceof Number) {
			return new JsonPrimitive((Number) object);
		} else if (object instanceof Character) {
			return new JsonPrimitive((Character) object);
		} else if (object instanceof Boolean) {
			return new JsonPrimitive((Boolean) object);
		} else if (object instanceof String) {
			return new JsonPrimitive((String) object);
		} else if (object instanceof List) {
			JsonArray array = new JsonArray();
			for (Iterator<?> iterator = ((List<?>) object).iterator(); iterator
					.hasNext();) {
				Object value = iterator.next();
				array.add(context.serialize(value));
			}
			return array;
		}
		Class<? extends Object> objClass = object.getClass();
		do {
			Field fields[] = objClass.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				Expose annotation = field.getAnnotation(Expose.class);
				if (annotation == null)
					continue;
				Object value;
				try {
					field.setAccessible(true);
					value = field.get(object);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
					continue;
				}
				if (value == null)
					continue;
				Class<? extends GsonAdapter<?>>[] adapterClass = annotation
						.adapter();
				if (adapterClass == null || adapterClass.length != 1) {
					obj.add(field.getName(), context.serialize(value));
				} else {
					GsonAdapter<Object> adapter;
					try {
						@SuppressWarnings("unchecked")
						GsonAdapter<Object> a = (GsonAdapter<Object>) adapterClass[0]
								.getConstructor(Object[].class).newInstance(new Object[]{args});
						adapter = a;
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
						e.printStackTrace();
						continue;
					}
					JsonElement v = adapter.serialize(value, field.getType(),
							context);
					obj.add(field.getName(), v);
				}
			}
		} while ((objClass = objClass.getSuperclass()) != null);
		return obj;
	}

	@Override
	public Object deserialize(JsonElement element, Type type,
			JsonDeserializationContext context) throws JsonParseException {
		if (element.isJsonNull()) {
			return null;
		} else if (element.isJsonPrimitive()) {
			JsonPrimitive p = element.getAsJsonPrimitive();
			if (p.isNumber()) {
				if (Number.class.isAssignableFrom((Class<?>) type)) {
					@SuppressWarnings("unchecked")
					Class<? extends Number> clazz = (Class<? extends Number>) type;
					Number n = p.getAsNumber();
					final Class<?>[] numberClasses = { double.class,
							float.class, long.class, int.class, short.class,
							byte.class, String.class };
					// check castabillity to target type
					if (clazz.isPrimitive()
							|| ((Class<?>) type).isAssignableFrom(n.getClass())) {
						return clazz.cast(n);
					} else {
						Constructor<?> match = null;
						Object arg = null;
						// Search supportet constructor
						for (Class<?> fromType : numberClasses) {
							try {
								match = clazz.getConstructor(fromType);
								if (fromType.equals(double.class)) {
									arg = n.doubleValue();
								} else if (fromType.equals(double.class)) {
									arg = n.doubleValue();
								} else if (fromType.equals(float.class)) {
									arg = n.floatValue();
								} else if (fromType.equals(long.class)) {
									arg = n.longValue();
								} else if (fromType.equals(int.class)) {
									arg = n.intValue();
								} else if (fromType.equals(short.class)) {
									arg = n.shortValue();
								} else if (fromType.equals(byte.class)) {
									arg = n.byteValue();
								} else if (fromType.equals(String.class)) {
									arg = n.toString();
								}
								break;
							} catch (NoSuchMethodException | SecurityException e) {
							}
						}
						if (match == null) {
							System.out.println("Warn: Can't cast number " + n
									+ " from type " + type + " to type "
									+ clazz);
							return null;
						}
						try {
							return match.newInstance(arg);
						} catch (InstantiationException
								| IllegalAccessException
								| IllegalArgumentException
								| InvocationTargetException e) {
							e.printStackTrace();
						}
					}
				}
				return null;
			} else if (p.isBoolean()) {
				return p.getAsBoolean();
			} else if (p.isString()) {
				return p.getAsString();
			}
		} else if (element.isJsonObject()) {
			Object object = null;
			try {
				object = ((Class<?>) type).newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
				return null;
			}
			JsonObject jo = element.getAsJsonObject();
			Set<Entry<String, JsonElement>> entries = jo.entrySet();
			for (Iterator<Entry<String, JsonElement>> iterator = entries
					.iterator(); iterator.hasNext();) {
				Entry<String, JsonElement> entry = iterator.next();
				String key = entry.getKey();
				Field f = null;
				Class<?> classObject = (Class<?>) type;
				do {
					try {
						f = classObject.getDeclaredField(key);
					} catch (NoSuchFieldException | SecurityException e) {
						continue;
					}
				} while ((classObject = classObject.getSuperclass()) != null
						&& f == null);
				if (f == null)
					continue;
				Expose annotation = f.getAnnotation(Expose.class);
				if (annotation == null)
					continue;
				Class<? extends GsonAdapter<?>>[] adapterClass = annotation
						.adapter();
				if (adapterClass == null || adapterClass.length != 1) {
					f.setAccessible(true);
					JsonElement value = entry.getValue();
					try {
						f.set(object, context.deserialize(value, f.getType()));
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				} else {
					GsonAdapter<Object> adapter;
					JsonElement value = entry.getValue();
					try {
						@SuppressWarnings("unchecked")
						GsonAdapter<Object> a = (GsonAdapter<Object>) adapterClass[0]
								.getConstructor(Object[].class).newInstance(new Object[]{args});
						adapter = a;
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
						e.printStackTrace();
						continue;
					}
					Object v = adapter.deserialize(value, f.getType(), context);
					try {
						f.setAccessible(true);
						f.set(object, v);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
			return object;
		} else if (element.isJsonArray()) {
			Class<?> objectClass = (Class<?>) type;
			List<Object> list = null;
			try {
				if (List.class.isAssignableFrom(objectClass)) {
					@SuppressWarnings("unchecked")
					List<Object> newInstance = (List<Object>) objectClass
							.newInstance();
					list = newInstance;
				}
			} catch (Exception e2) {
			}
			if (list == null)
				list = new ArrayList<Object>();
			JsonArray a = element.getAsJsonArray();
			for (Iterator<JsonElement> iterator = a.iterator(); iterator
					.hasNext();) {
				JsonElement e = iterator.next();
				Object eo = context.deserialize(e, type);
				list.add(eo);
			}
		}
		return null;
	}
}
