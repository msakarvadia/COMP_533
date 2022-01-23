package comp533.mvc;

public class KeyValue<KeyType, ValueType> implements KeyValueInterface<KeyType, ValueType> {
	private KeyType key;
	private ValueType val;

	@Override
	public KeyType getKey() {
		return key;
	}

	@Override
	public void setKey(KeyType newKey) {
		key = newKey;
	}
	
	@Override
	public void setValue(ValueType newValue) {
		// TODO Auto-generated method stub
		val = newValue;
	}

	@Override
	public ValueType getValue() {
		// TODO Auto-generated method stub
		return val;
	}
	
	@Override
	public String toString() {
		return "("+key.toString()+","+val.toString()+")";
	}
	
}
