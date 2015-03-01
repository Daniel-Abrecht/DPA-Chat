package serialisation;

public interface ObjectEncoder<T> {
	public T encode(Object o);
}
