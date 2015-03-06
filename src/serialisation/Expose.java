package serialisation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface Expose {
	static public interface TypeGetter {
		public Class<?> getType(Object o, Field f);
	}

	Class<? extends TypeGetter>[] getTypeGetterType() default {};

	int position();

	Class<? extends CustomFieldEncoder>[] customFieldEncoder() default {};
}
