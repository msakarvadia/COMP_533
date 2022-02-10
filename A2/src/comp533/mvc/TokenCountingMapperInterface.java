package comp533.mvc;

import java.util.List;

public interface TokenCountingMapperInterface<K, V> {
	public KeyValueInterface<K, V> map(String aStrings);
	public String toString();

}

