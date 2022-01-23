package comp533.mvc;

import java.util.List;

public interface TokenCountingMapperInterface<KeyType, ValueType> {
	public List<KeyValueInterface<KeyType, ValueType>> map(List<String> aStrings);
	public String toString();

}

