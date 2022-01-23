package comp533.mvc;

import java.util.List;
import java.util.Map;

public interface ReducerInterface<KeyType, ValueType> {
	public Map<KeyType, ValueType> reduce (List<KeyValueInterface<KeyType, ValueType>> aList);
	public String toString();
}
