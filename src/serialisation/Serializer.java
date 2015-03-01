package serialisation;

import java.lang.reflect.Field;
import java.util.List;

public interface Serializer {
	public List<Field> getFields(Object o);
}
