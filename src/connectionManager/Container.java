package connectionManager;

import java.lang.reflect.Field;

import serialisation.Deserializable;
import serialisation.Expose;

/**
 * Hilfklasse für (de)serializer, zur Speicherung belibiger Objekte
 * 
 * @author Daniel Abrecht
 */
@Deserializable
public class Container {

	/**
	 * Hilfsklasse für BinaryEncoder zur ermittlung Des datentyps des Objekts
	 * 
	 * @author Daniel Abrecht
	 */
	public static class ContainerObjectTypeGetter implements Expose.TypeGetter {
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

	/**
	 * Konstruktor, erwartet das enthaltene Objekt
	 * @param o
	 */
	public Container(Object o) {
		Class<?> c = o.getClass();
		do
			if (c.getAnnotation(Deserializable.class) != null)
				break;
		while ((c = c.getSuperclass()) != null);
		this.type = (c == null) ? "java.lang.Object" : c.getName();
		this.object = o;
	}

	/**
	 * Konstruktor für BinaryEncoder
	 */
	public Container() {}

	/**
	 * Getter für Typstring
	 * @return String mit volständigem Namen der klasse
	 */
	public String getTypeString() {
		return type;
	}

	/**
	 * Getter für enthaltenes Objekt
	 * @return
	 */
	public Object getObject() {
		return object;
	}
	
	@Override
	public String toString() {
		return "Container [type=" + type + ", object=" + object + "]";
	}

}
