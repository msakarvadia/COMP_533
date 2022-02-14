package comp533.mvc;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import comp533.barrier.Barrier;
import comp533.joiner.Joiner;
import comp533.salve.SlaveInterface;


//import gradingTools.comp533s19.assignment0.AMapReduceTracer;

//public class SummingModel extends AMapReduceTracer implements ModelInterface
public class SummingModel extends Model implements ModelInterface{
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	private String inputString = null;
	private Map<String, Integer> result = new HashMap<String, Integer>();
	final ReducerInterface<String, Integer> reducer = ReducerFactory.getReducer();
	final IntSummingMapperInterface<String, Integer> mapper = IntSummingMapperFactory.getMapper();


	@Override
	public void setInputString(final String newVal) {
		barrier = new Barrier(aNumThreads);
		joiner = new Joiner(aNumThreads);
		traceBarrierCreated(barrier, aNumThreads);
		traceJoinerCreated(joiner, aNumThreads);

		final String oldResult = result.toString();
		final String oldInputString = inputString;
		inputString = newVal;
		final String label = "InputString";

		propertyChangeSupport.firePropertyChange(label, oldInputString, newVal);

		// A2 tasks
		// 1
		aKeyValueQueue = new ArrayBlockingQueue<KeyValueInterface<String, Integer>>(BUFFER_SIZE);
		aReductionQueueList = new ArrayList<LinkedList<KeyValueInterface<String, Integer>>>();
		aKeyValueQueue.clear();
		aReductionQueueList.clear();
		result.clear();

		if (!slavesStarted) {
			for (int i = 0; i < threads.size(); i++) {
				// TODO this cannot happen multiple times:
				threads.get(i).start();
			}
			slavesStarted = true;
		}

	

		aReductionQueueList = notifySlaves(slaves, aReductionQueueList);

		// 3
		final String tokens = inputString;
		final List<String> listOfToken = Arrays.asList(tokens.split(space));
		

		aKeyValueQueue = mapping(aKeyValueQueue, listOfToken, slaves);
	
		// 5
		aKeyValueQueue = enqueuer(threads, slaves, aKeyValueQueue);

		// 6
		joiner.join();
		result = combineReductionQueueList(aReductionQueueList, result);

		final String resultLabel = "Result";

		propertyChangeSupport.firePropertyChange(resultLabel, oldResult, result.toString());

	}
/*	public void addPropertyChangeListener(final PropertyChangeListener newListener) {
		propertyChangeSupport.addPropertyChangeListener(newListener);
	}

	@Override
	public void setInputString(final String newVal) {
		barrier = new Barrier(aNumThreads);
		joiner = new Joiner(aNumThreads);
		final String oldResult = result.toString();
		final String oldInputString = inputString;
		inputString = newVal;
		final String label = "InputString";
		final PropertyChangeEvent inputEvent = new PropertyChangeEvent(this, label, oldInputString, newVal);
		propertyChangeSupport.firePropertyChange(inputEvent);

		// A2 tasks
		// 1
		aKeyValueQueue = new ArrayBlockingQueue<KeyValueInterface<String, Integer>>(BUFFER_SIZE);
		aReductionQueueList = new ArrayList<LinkedList<KeyValueInterface<String, Integer>>>();
		aKeyValueQueue.clear();
		aReductionQueueList.clear();
		result.clear();

		if (!slavesStarted) {
			for (int i = 0; i < threads.size(); i++) {
				// TODO this cannot happen multiple times:
				threads.get(i).start();
			}
			slavesStarted = true;
		}
		
		// 2
		for (int i = 0; i < threads.size(); i++) {
			slaves.get(i).notifySlave();
			aReductionQueueList.add(i, new LinkedList<KeyValueInterface<String, Integer>>());
		}

		// 3
		final String tokens = inputString;
		final List<String> listOfToken = Arrays.asList(tokens.split(space));
		final IntSummingMapperInterface<String, Integer> aMapper = IntSummingMapperFactory.getMapper();

		for (int i = 0; i < listOfToken.size(); i++) {
			final int slaveNum = i % threads.size();
			slaves.get(slaveNum).notifySlave();
			final KeyValueInterface<String, Integer> keyVal = aMapper.map(listOfToken.get(i));

			try {
				traceEnqueueRequest(keyVal);
				aKeyValueQueue.put(keyVal);
				traceEnqueue(aKeyValueQueue);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			slaves.get(slaveNum).notifySlave();
		}

		// 5
		for (int i = 0; i < threads.size(); i++) {
			final KeyValueInterface<String, Integer> keyVal = new KeyValue<String, Integer>();
			keyVal.setKey(null);
			keyVal.setValue(null);
			try {
				traceEnqueueRequest(keyVal);
				aKeyValueQueue.put(keyVal);
				traceEnqueue(aKeyValueQueue);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			slaves.get(i).notifySlave();
		}

		// 6
		joiner.join();

		for (int i = 0; i < aReductionQueueList.size(); i++) {
			final LinkedList<KeyValueInterface<String, Integer>> linkedList = aReductionQueueList.get(i);
			for (int listIndex = 0; listIndex < linkedList.size(); listIndex++) {
				result.put(linkedList.get(listIndex).getKey(), linkedList.get(listIndex).getValue());
			}
			traceAddedToMap(result, linkedList);
		}

		final String resultLabel = "Result";
		final PropertyChangeEvent resultComputed = new PropertyChangeEvent(this, resultLabel, oldResult,
				result.toString());
		propertyChangeSupport.firePropertyChange(resultComputed);

	}*/

	@Override
	public void computeResult() {
		return;
	}

	@Override
	public String toString() {// overriding the toString() method
		return MODEL;
	}
	
	@Override
	public BlockingQueue<KeyValueInterface<String, Integer>> mapping(
			final BlockingQueue<KeyValueInterface<String, Integer>> keyValueQueue, final List<String> listOfToken,
			final List<SlaveInterface> salves) {
		
		final IntSummingMapperInterface<String, Integer> aMapper = IntSummingMapperFactory.getMapper();
		
		for (int i = 0; i < listOfToken.size(); i++) {
			final int slaveNum = i % slaves.size();
			slaves.get(slaveNum).notifySlave();
			final KeyValueInterface<String, Integer> keyVal = aMapper.map(listOfToken.get(i));

			try {
				traceEnqueueRequest(keyVal);
				aKeyValueQueue.put(keyVal);
				traceEnqueue(keyValueQueue);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			slaves.get(slaveNum).notifySlave();
		}
		return aKeyValueQueue;
	}

}
