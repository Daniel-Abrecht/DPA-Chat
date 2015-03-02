package serialisation;

public interface ObjectEncoder<T> {
	public T encode(Object o);

	public <R> R decode(T o, Class<R> c);
}
