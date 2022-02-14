package comp533.salve;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import comp533.mvc.KeyValueInterface;
import comp533.mvc.ModelInterface;

public interface SlaveInterface extends Runnable{
	public void run();
	public void notifySlave();
	public String toString();
	public LinkedList<KeyValueInterface<String, Integer>> addKeyValToLocalLinkedList();
	public List<LinkedList<KeyValueInterface<String, Integer>>> updateReductionQueueListWithPartitions(List<LinkedList<KeyValueInterface<String, Integer>>> aReductionQueueList, Map<String, Integer> keyToPartition);
	public Map<String, Integer> partitionKeys(Map<String, Integer> aKeyToPartition, Map<String, Integer> aResult);
	public LinkedList<KeyValueInterface<String, Integer>> block(ModelInterface aSlaveModel, LinkedList<KeyValueInterface<String, Integer>> aLocalList, boolean aLoop);
}
