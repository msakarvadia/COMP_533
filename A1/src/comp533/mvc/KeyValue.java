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
		final String string = "("+key.toString()+","+val.toString()+")";
		return string;
	}
	
}
