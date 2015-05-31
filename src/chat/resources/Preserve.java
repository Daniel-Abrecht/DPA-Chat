package chat.resources;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Hinweis für Deserializer diesen wert nicht zu Beeinflussen
 * 
 * @author Daniel Abrecht
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value=ElementType.FIELD)
public @interface Preserve {
}
