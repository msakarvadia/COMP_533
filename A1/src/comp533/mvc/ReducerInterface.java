package comp533.mvc;

import java.util.List;
import java.util.Map;

public interface ReducerInterface<K, V> {
	public Map<K, V> reduce (List<KeyValueInterface<K, V>> aList);
	public String toString();
}
