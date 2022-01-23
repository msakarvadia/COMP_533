package comp533.mvc;

public interface KeyValueInterface<KeyType, ValueType> {
	public KeyType getKey();
	public void setKey(KeyType newKey);
	public ValueType getValue();
	public void setValue(ValueType newValue);
	public String toString();
	
}
