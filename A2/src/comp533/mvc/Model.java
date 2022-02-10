package comp533.mvc;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import comp533.barrier.Barrier;
import comp533.barrier.BarrierInterface;
import comp533.joiner.Joiner;
import comp533.joiner.JoinerInterface;
import comp533.salve.Slave;
import comp533.salve.SlaveInterface;

import java.util.HashMap;
import java.util.LinkedList;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class Model extends AMapReduceTracer implements ModelInterface {
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	private String inputString = null;
	private Map<String, Integer> result = new HashMap<String, Integer>();
	final String space = " ";

	// properties from A2
	int NumThreads = 0;
	List<Thread> threads = new ArrayList<Thread>();
	List<SlaveInterface> slaves = new ArrayList<SlaveInterface>();
	BarrierInterface barrier;
	JoinerInterface joiner;
	BlockingQueue<KeyValueInterface<String, Integer>> aKeyValueQueue = new ArrayBlockingQueue<KeyValueInterface<String, Integer>>(
			BUFFER_SIZE);
	List<LinkedList<KeyValueInterface<String, Integer>>> aReductionQueueList = new ArrayList<LinkedList<KeyValueInterface<String, Integer>>>();

	@Override
	public int getNumThreads() {
		return NumThreads;
	}

	@Override
	public void setNumThreads(final int numThreads) {
		barrier = new Barrier(numThreads);
		joiner = new Joiner(numThreads);
		traceBarrierCreated(barrier, numThreads);
		traceJoinerCreated(joiner, numThreads);

		final String oldValue = Integer.toString(NumThreads);
		String oldThreads = threads.toString();
		if (threads.isEmpty()) {
			oldThreads = null;
			NumThreads = numThreads;
		}

		threads = new ArrayList<Thread>();
		for (int i = 0; i < numThreads; i++) {
			final String name = "slave" + Integer.toString(i);

			SlaveInterface newSlave = new Slave(i, this, numThreads);
			slaves.add(newSlave);

			Thread slaveThread = new Thread(newSlave);
			slaveThread.setName(name);
			threads.add(slaveThread);

			// Add separate reduction queue to Reduction queue list for each slave thread
			aReductionQueueList.add(new LinkedList<KeyValueInterface<String, Integer>>());
		}
		final String label = "NumThreads";
		final String newValue = Integer.toString(NumThreads);

		final String labelThread = "Threads";
		final String newThreads = threads.toString();

		final PropertyChangeEvent inputEvent = new PropertyChangeEvent(this, label, oldValue, newValue);
		propertyChangeSupport.firePropertyChange(inputEvent);

		final PropertyChangeEvent inputEvent1 = new PropertyChangeEvent(this, labelThread, oldThreads, newThreads);
		propertyChangeSupport.firePropertyChange(inputEvent1);
	}

	@Override
	public List<Thread> getThreads() {
		return threads;
	}

	@Override
	public Map<String, Integer> getResult() {
		return result;
	}

	@Override
	public void addPropertyChangeListener(final PropertyChangeListener newListener) {
		propertyChangeSupport.addPropertyChangeListener(newListener);
	}

	@Override
	public void setInputString(final String newVal) {
		String oldResult = result.toString();
		final String oldInputString = inputString;
		inputString = newVal;
		final String label = "InputString";
		final PropertyChangeEvent inputEvent = new PropertyChangeEvent(this, label, oldInputString, newVal);
		propertyChangeSupport.firePropertyChange(inputEvent);

		// A2 tasks
		// 1
		aKeyValueQueue = new ArrayBlockingQueue<KeyValueInterface<String, Integer>>(BUFFER_SIZE);
		aReductionQueueList = new ArrayList<LinkedList<KeyValueInterface<String, Integer>>>();
		result.clear();

		for (int i = 0; i < threads.size(); i++) {
			threads.get(i).start();

		}
		// 2
		for (int i = 0; i < threads.size(); i++) {
			slaves.get(i).notifySlave();
			aReductionQueueList.add(i, new LinkedList<KeyValueInterface<String, Integer>>());
		}

		// 3
		final String tokens = inputString;
		final List<String> listOfToken = Arrays.asList(tokens.split(space));
		final TokenCountingMapperInterface<String, Integer> mapper = TokenCountingMapperFactory.getMapper();

		for (int i = 0; i < listOfToken.size(); i++) {

			KeyValueInterface<String, Integer> keyVal = mapper.map(listOfToken.get(i));

			try {
				traceEnqueueRequest(keyVal);
				aKeyValueQueue.put(keyVal);
				traceEnqueue(aKeyValueQueue);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			final int slaveNum = i % threads.size();
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
		//System.out.println("ALL THREADS WERE NOTIFIED AND JOIN HAPPENED");
		for (int i = 0; i < aReductionQueueList.size(); i++) {
			LinkedList<KeyValueInterface<String, Integer>> linkedList = aReductionQueueList.get(i);
			for (int j = 0; j < linkedList.size(); j++) {
				result.put(linkedList.get(j).getKey(), linkedList.get(j).getValue());
			}
		}
		
		final String resultLabel = "Result";
		final PropertyChangeEvent resultComputed = new PropertyChangeEvent(this, resultLabel, oldResult, result.toString());
		propertyChangeSupport.firePropertyChange(resultComputed);

	}

	// @Override
	// public void computeResult() {
	// final ReducerInterface<String, Integer> reducer =
	// ReducerFactory.getReducer();
	// final TokenCountingMapperInterface<String, Integer> mapper =
	// TokenCountingMapperFactory.getMapper();
	// String oldResult = result.toString();
	// if (result.isEmpty()) {
	// oldResult = null;
	// }
	// final String tokens = inputString;
	// result.clear();
	// final List<String> listOfToken = Arrays.asList(tokens.split(space));
	// final List<KeyValueInterface<String, Integer>> keyValList =
	// mapper.map(listOfToken);
	// TODO PROPERTY CHANGE FOR KeyValList
	// result = reducer.reduce(keyValList);
	// TODO property change for Result
	// final String label = "Result";
	// final PropertyChangeEvent resultComputed = new PropertyChangeEvent(this,
	// label, oldResult, result.toString());
	// propertyChangeSupport.firePropertyChange(resultComputed);

	// }

	@Override
	public String toString() {// overriding the toString() method
		return MODEL;
	}

	@Override
	public BarrierInterface getBarrier() {
		return barrier;
	}

	@Override
	public JoinerInterface getJoiner() {
		return joiner;
	}

	@Override
	public BlockingQueue getKeyValueQueue() {
		return aKeyValueQueue;
	}

	@Override
	public List<LinkedList<KeyValueInterface<String, Integer>>> getReductionQueueList() {
		return aReductionQueueList;
	}

	@Override
	public void computeResult() {
		// TODO Auto-generated method stub

	}
}
