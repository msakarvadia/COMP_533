package comp533.mvc;

import java.util.List;

public interface TokenCountingMapperInterface<K, V> {
	public List<KeyValueInterface<K, V>> map(List<String> aStrings);
	public String toString();

}

