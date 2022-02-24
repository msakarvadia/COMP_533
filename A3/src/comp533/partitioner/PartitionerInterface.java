package comp533.partitioner;

public interface PartitionerInterface<K, V> {
	public int getPartition(K key, V value, int numPartitions);
	public String toString();
}
