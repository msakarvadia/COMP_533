package comp533.mvc;

import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import comp533.barrier.BarrierInterface;
import comp533.joiner.JoinerInterface;

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
			Map<String, Integer> resultMap, int index);

	// A2 methods
	public int getNumThreads();

	public void setNumThreads(final int numThreads);

	public List<Thread> getThreads();

	public BlockingQueue getKeyValueQueue();

	public List<LinkedList<KeyValueInterface<String, Integer>>> getReductionQueueList();

}
