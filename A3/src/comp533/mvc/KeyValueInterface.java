package comp533.mvc;

import java.io.Serializable;

public interface KeyValueInterface<K, V> extends Serializable{
	public K getKey();
	public void setKey(K newKey);
	public V getValue();
	public void setValue(V newValue);
	public String toString();
	
}
