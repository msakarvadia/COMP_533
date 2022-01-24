package comp533.mvc;

import java.util.List;

public interface IntSummingMapperInterface<K, V> {
	//public List<KeyValueInterface<KeyType, ValueType>> map(String aStrings);
	public List<KeyValueInterface<K, V>> map(List<String> aList);
	public String toString();
}
