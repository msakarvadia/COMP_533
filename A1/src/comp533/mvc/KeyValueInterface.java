package comp533.mvc;

public interface KeyValueInterface<K, V> {
	public K getKey();
	public void setKey(K newKey);
	public V getValue();
	public void setValue(V newValue);
	public String toString();
	
}
