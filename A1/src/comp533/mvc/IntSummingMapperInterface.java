package comp533.mvc;

import java.util.List;

public interface IntSummingMapperInterface<KeyType, ValueType> {
	public List<KeyValueInterface<KeyType, ValueType>> map(String aStrings);
	public String toString();
}
