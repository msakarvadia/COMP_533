package comp533.mvc;

public class KeyValue<K, V> implements KeyValueInterface<K, V> {
	private K key;
	private V val;

	@Override
	public K getKey() {
		return key;
	}

	@Override
	public void setKey(final K newKey) {
		key = newKey;
	}
	
	@Override
	public void setValue(final V newValue) {
		// TODO Auto-generated method stub
		val = newValue;
	}

	@Override
	public V getValue() {
		// TODO Auto-generated method stub
		return val;
	}
	
	@Override
	public String toString() {
		final String keyName;
		final String aNull= "null";
		if (key != null) {
			keyName = key.toString();
		}
		else {
			keyName = aNull;
		}
		
		final String valName;
		if (val != null) {
			valName = val.toString();
		}
		else {
			valName = aNull;
		}
			
		final String string = "("+keyName+","+valName+")";
		return string;
	}
	
}
