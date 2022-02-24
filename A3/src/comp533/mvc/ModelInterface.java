package comp533.mvc;

import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import comp533.barrier.BarrierInterface;
import comp533.joiner.JoinerInterface;
import comp533.salve.SlaveInterface;

public interface ModelInterface {
	public Map<String, Integer> getResult();

	public void addPropertyChangeListener(PropertyChangeListener newListener);

	public void setInputString(String newVal);

	public void computeResult();

	public String toString();

	public BarrierInterface getBarrier();

	public JoinerInterface getJoiner();

	public void terminate();

	public Map<String, Integer> combineReductionQueueList(
			List<LinkedList<KeyValueInterface<String, Integer>>> aReductionQueueList, Map<String, Integer> aResult);

	public Map<String, Integer> getResults(List<LinkedList<KeyValueInterface<String, Integer>>> reductionQueueList,
			Map<String, Integer> resultMap, final int index);

	public BlockingQueue<KeyValueInterface<String, Integer>> enqueuer(List<Thread> aThreads,
			List<SlaveInterface> aSlaves, BlockingQueue<KeyValueInterface<String, Integer>> keyValueQueue);

	// A2 methods
	public int getNumThreads();

	public void setNumThreads(final int numThreads);

	public List<Thread> getThreads();

	public BlockingQueue getKeyValueQueue();

	public List<LinkedList<KeyValueInterface<String, Integer>>> getReductionQueueList();

	public List<LinkedList<KeyValueInterface<String, Integer>>> notifySlaves(List<SlaveInterface> aSlaves,
			List<LinkedList<KeyValueInterface<String, Integer>>> redQueue);

	public BlockingQueue<KeyValueInterface<String, Integer>> mapping(
			BlockingQueue<KeyValueInterface<String, Integer>> aKeyValueQueue, List<String> listOfToken,
			 List<SlaveInterface> salves);

}
