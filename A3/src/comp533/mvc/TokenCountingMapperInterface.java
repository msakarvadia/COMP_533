package comp533.mvc;


public interface TokenCountingMapperInterface<K, V> {
	public KeyValueInterface<K, V> map(String aStrings);
	public String toString();

}

