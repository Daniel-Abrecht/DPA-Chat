package serialisation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

/**
 * Annotation für BinaryEncoder, Gibt an dass und wie ein feld (de)serialisiert werden soll
 * 
 * @author Daniel Abrecht
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface Expose {
	
	/**
	 * Hilfsinterface für Getter des zu Verwendenden Datentyps
	 * @author Daniel Abrecht
	 */
	static public interface TypeGetter {
		public Class<?> getType(Object o, Field f);
	}

	/**
	 * Getterhilfsklasse für Datentyp
	 */
	Class<? extends TypeGetter>[] getTypeGetterType() default {};

	/**
	 * Position im Binärdatenstream
	 */
	int position();

	/**
	 * Benutzerdefinierter (de)serializer
	 */
	Class<? extends CustomFieldEncoder>[] customFieldEncoder() default {};
}
