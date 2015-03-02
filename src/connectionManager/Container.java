package connectionManager;

import java.lang.reflect.Field;

import serialisation.Deserializable;
import serialisation.Expose;

@Deserializable
public class Container {

	public static class ContainerObjectTypeGetter implements Expose.TvpeGetter {
		@Override
		public Class<?> getType(Object o, Field f) {
			try {
				return Class.forName(((Container)o).type);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				return Object.class;
			}
		}
	}
	
	@Expose(position = 0)
	private String type;
	@Expose(position = 1, getTypeGetterType = ContainerObjectTypeGetter.class)
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

	public Container() {}

	public String getTypeString() {
		return type;
	}

	public Object getObject() {
		return object;
	}
	
	@Override
	public String toString() {
		return "Container [type=" + type + ", object=" + object + "]";
	}


}
