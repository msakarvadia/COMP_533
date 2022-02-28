package comp533.mvc;

import java.beans.PropertyChangeSupport;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import comp533.barrier.Barrier;
import comp533.barrier.BarrierInterface;
import comp533.clientServer.RemoteClientInterface;
import comp533.joiner.Joiner;
import comp533.joiner.JoinerInterface;
import comp533.salve.Slave;
import comp533.salve.SlaveInterface;

import java.util.HashMap;
import java.util.LinkedList;
import java.beans.PropertyChangeListener;
//import java.beans.PropertyChangeEvent;
import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class DistributedSummerModel extends AMapReduceTracer implements ModelInterface, RemoteModelInterface {
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	private String inputString = null;
	private Map<String, Integer> result = new HashMap<String, Integer>();
	final String space = " ";
	boolean slavesStarted = false;

	// properties from A2
	int aNumThreads = 0;
	List<Thread> threads = new ArrayList<Thread>();
	List<SlaveInterface> slaves = new ArrayList<SlaveInterface>();
	BarrierInterface barrier;
	JoinerInterface joiner;
	BlockingQueue<KeyValueInterface<String, Integer>> aKeyValueQueue = new ArrayBlockingQueue<KeyValueInterface<String, Integer>>(
			BUFFER_SIZE);
	List<LinkedList<KeyValueInterface<String, Integer>>> aReductionQueueList = new ArrayList<LinkedList<KeyValueInterface<String, Integer>>>();

	// A3
	Stack<SlaveInterface> unassignedSlaves = new Stack<SlaveInterface>();
	Stack<Thread> unassignedThreads = new Stack<Thread>();
	Stack<RemoteClientInterface> unassignedClients = new Stack<RemoteClientInterface>();
	Map<Integer, RemoteClientInterface> slaveClientMap = new HashMap<Integer, RemoteClientInterface>();

	@Override
	public int getNumThreads() {
		return aNumThreads;
	}

	@Override
	public void setNumThreads(final int numThreads) {

		final String oldValue = Integer.toString(aNumThreads);
		String oldThreads = threads.toString();
		if (threads.isEmpty()) {
			oldThreads = null;
			aNumThreads = numThreads;
		}

		threads = new ArrayList<Thread>();
		for (int i = 0; i < numThreads; i++) {
			final String name = "Slave" + Integer.toString(i);

			final SlaveInterface newSlave = new Slave(i, this);
			slaves.add(newSlave);
			unassignedSlaves.push(newSlave);

			final Thread slaveThread = new Thread(newSlave);
			slaveThread.setName(name);
			threads.add(slaveThread);

			unassignedThreads.push(slaveThread);

			// Add separate reduction queue to Reduction queue list for each slave thread
			aReductionQueueList.add(new LinkedList<KeyValueInterface<String, Integer>>());
		}

		// Match function - Check placement of match function
		match(unassignedSlaves, unassignedClients);

		final String label = "NumThreads";
		final String newValue = Integer.toString(aNumThreads);

		final String labelThread = "Threads";
		final String newThreads = threads.toString();

		propertyChangeSupport.firePropertyChange(label, oldValue, newValue);
		propertyChangeSupport.firePropertyChange(labelThread, oldThreads, newThreads);
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
		barrier = new Barrier(aNumThreads);
		joiner = new Joiner(aNumThreads);
		traceBarrierCreated(barrier, aNumThreads);
		traceJoinerCreated(joiner, aNumThreads);

		final String oldResult = result.toString();
		final String oldInputString = inputString;
		inputString = newVal;
		final String label = "InputString";
		
		final String quit = "quit";
		if (quit.equals(inputString)) {
			
			traceQuit();
			terminate();
			return;
		}

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

		// Match function - Check placement of match function
		// match(unassignedSlaves, unassignedClients);

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

	@Override
	public String toString() {// overriding the toString() method
		return MODEL;
	}

	@Override
	public Map<Integer, RemoteClientInterface> getSlaveClientMap() {
		return slaveClientMap;
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

	@Override
	public void terminate() {
		// TODO Auto-generated method stub
		for (int i = 0; i < threads.size(); i++) {
			slaves.get(i).slaveQuit();
			threads.get(i).interrupt();

		}

		while (!unassignedClients.empty()) {
			try {
				unassignedClients.pop().quit();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public Map<String, Integer> combineReductionQueueList(
			final List<LinkedList<KeyValueInterface<String, Integer>>> reductionQueueList,
			final Map<String, Integer> aResult) {
		Map<String, Integer> results = aResult;
		for (int i = 0; i < reductionQueueList.size(); i++) {
			final LinkedList<KeyValueInterface<String, Integer>> linkedList = reductionQueueList.get(i);

			results = getResults(reductionQueueList, aResult, i);
			traceAddedToMap(results, linkedList);
		}
		return results;
	}

	@Override
	public Map<String, Integer> getResults(
			final List<LinkedList<KeyValueInterface<String, Integer>>> reductionQueueList,
			final Map<String, Integer> resultMap, final int index) {
		final LinkedList<KeyValueInterface<String, Integer>> linkedList = reductionQueueList.get(index);
		final Map<String, Integer> results = resultMap;
		for (int listIndex = 0; listIndex < linkedList.size(); listIndex++) {
			results.put(linkedList.get(listIndex).getKey(), linkedList.get(listIndex).getValue());
		}
		return results;

	}

	@Override
	public BlockingQueue<KeyValueInterface<String, Integer>> enqueuer(final List<Thread> aThreads,
			final List<SlaveInterface> aSlaves, final BlockingQueue<KeyValueInterface<String, Integer>> keyValueQueue) {

		final BlockingQueue<KeyValueInterface<String, Integer>> keyValQueue = keyValueQueue;
		for (int i = 0; i < aThreads.size(); i++) {
			final KeyValueInterface<String, Integer> keyVal = new KeyValue<String, Integer>();
			keyVal.setKey(null);
			keyVal.setValue(null);
			try {
				traceEnqueueRequest(keyVal);
				keyValQueue.put(keyVal);
				traceEnqueue(keyValQueue);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			aSlaves.get(i).notifySlave();
		}
		return keyValQueue;

	}

	@Override
	public List<LinkedList<KeyValueInterface<String, Integer>>> notifySlaves(final List<SlaveInterface> aSlaves,
			final List<LinkedList<KeyValueInterface<String, Integer>>> redQueue) {
		final List<LinkedList<KeyValueInterface<String, Integer>>> reductionQueue = redQueue;
		for (int i = 0; i < aSlaves.size(); i++) {
			aSlaves.get(i).notifySlave();
			reductionQueue.add(i, new LinkedList<KeyValueInterface<String, Integer>>());
		}
		return reductionQueue;
	}

	@Override
	public BlockingQueue<KeyValueInterface<String, Integer>> mapping(
			final BlockingQueue<KeyValueInterface<String, Integer>> keyValueQueue, final List<String> listOfToken,
			final List<SlaveInterface> salves) {

		final IntSummingMapperInterface<String, Integer> mapper = IntSummingMapperFactory.getMapper();
		//IntSummingMapperFactory.setMapper(IntSummingMapperFactory.getMapper());

		for (int i = 0; i < listOfToken.size(); i++) {
			final int slaveNum = i % slaves.size();
			slaves.get(slaveNum).notifySlave();
			final KeyValueInterface<String, Integer> keyVal = mapper.map(listOfToken.get(i));

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

	@Override
	public void register(RemoteClientInterface client) {

		traceRegister(client);

		unassignedClients.push(client);

		match(unassignedSlaves, unassignedClients);

	}

	@Override
	public void match(Stack<SlaveInterface> unassigned_slave, Stack<RemoteClientInterface> client) {

		Object extra = unassignedClients.clone();
		while (!((Stack<SlaveInterface>) extra).empty()) {
			final RemoteClientInterface waiterClient = ((Stack<RemoteClientInterface>) extra).pop();
			
			if (!unassignedSlaves.empty()) {
				final RemoteClientInterface freeClient = unassignedClients.pop();
				final SlaveInterface freeSlave = unassignedSlaves.pop();
				traceClientAssignment(freeClient);
				freeSlave.setClient(freeClient);

			}

		}

	}
}
