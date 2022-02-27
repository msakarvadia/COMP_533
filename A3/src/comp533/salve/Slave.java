package comp533.salve;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import comp533.clientServer.RemoteClientInterface;
import comp533.mvc.KeyValue;
import comp533.mvc.KeyValueInterface;
import comp533.mvc.ModelInterface;
import comp533.mvc.ReducerFactory;
import comp533.mvc.ReducerInterface;
import comp533.mvc.RemoteModelInterface;
import comp533.partitioner.PartitionerFactory;
import comp533.partitioner.PartitionerInterface;
import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class Slave extends AMapReduceTracer implements SlaveInterface {
	final int number;
	final int numThreads;
	final ModelInterface slaveModel;

	LinkedList<KeyValueInterface<String, Integer>> reductionLinkedList;
	private Map<String, Integer> result = new HashMap<String, Integer>();
	private Map<String, Integer> keyToPartition = new HashMap<String, Integer>();

	private RemoteClientInterface client;

	public Slave(final int identifier, final ModelInterface model) {
		number = identifier;
		slaveModel = model;
		numThreads = model.getNumThreads();
	}

	@Override
	//TODO somehow have the client assigned in the runnable
	public void setClient(RemoteClientInterface aClient) {
		System.out.println("setClient");
		client = aClient;
//		try {
//			client.clientWait();
//		} catch (RemoteException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
//		//traceClientAssignment(aClient);
//		synchronized (this) {
//			try {
//				traceWait();
//				wait();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				traceQuit();
//				e.printStackTrace();
//			}
//		}

	}

	@Override
	final public void run() {
		System.out.println("RUN METHOD");

		while (true) {
			boolean loop = true;

			// TODO make wait a separate method
			synchronized (this) {
				try {
					traceWait();
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					traceQuit();
					e.printStackTrace();
					break; // this breaks out of run loop then thread terminates
				}
			}

			LinkedList<KeyValueInterface<String, Integer>> localList = new LinkedList<KeyValueInterface<String, Integer>>();
			localList = block(slaveModel, localList, loop);

			loop = false;

			// partially reduce list
			Map<Integer, RemoteClientInterface> slaveClientMap = slaveModel.getSlaveClientMap();
			if(client!=null) {
				try {
		//			client = slaveClientMap.get(number);
					traceRemoteList (localList);
					result = client.reduce(localList);
					traceRemoteResult (result);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				final ReducerInterface<String, Integer> reducer = ReducerFactory.getReducer();
				result = reducer.reduce(localList);
			}
			localList.clear();
			// this gets the correct partitions for each key

			keyToPartition.clear();
			keyToPartition = partitionKeys(keyToPartition, result);
			// System.out.println("key to partition "+keyToPartition.toString());

			// add partitally reduced keyValues to the ReducitonQueueList

			List<LinkedList<KeyValueInterface<String, Integer>>> aReductionQueueList = slaveModel
					.getReductionQueueList();

			aReductionQueueList = updateReductionQueueListWithPartitions(aReductionQueueList, keyToPartition);

			keyToPartition.clear();

			// wait for the other slaves to complete their splitting
			slaveModel.getBarrier().barrier();
			traceSplitAfterBarrier(number, aReductionQueueList);

			// Need to do final reduction of this threads linkedList
			synchronized (aReductionQueueList) {
				final LinkedList<KeyValueInterface<String, Integer>> updatedLocalLinkedList = aReductionQueueList
						.get(number);

				slaveClientMap = slaveModel.getSlaveClientMap();
				if(client!=null) {
					try {
						//client = slaveClientMap.get(number);
						traceRemoteList (localList);
						result = client.reduce(localList);
						traceRemoteResult (result);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					final ReducerInterface<String, Integer> reducer = ReducerFactory.getReducer();
					result = reducer.reduce(updatedLocalLinkedList);

				}
				final LinkedList<KeyValueInterface<String, Integer>> finalLocalLinkedList = addKeyValToLocalLinkedList();

				aReductionQueueList.set(number, finalLocalLinkedList);
			}

			loop = true;
			slaveModel.getJoiner().finished();

			if (slaveModel.getJoiner().getTotalFinished() == numThreads) {
				slaveModel.getJoiner().join();

			}

			synchronized (this) {
				try {
					traceWait();
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					traceQuit();
//					try {
//						client.clientQuit();
//					} catch (RemoteException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
					e.printStackTrace();
				}
			}
			continue;
		}

	}

	@Override
	public synchronized void notifySlave() {
		// TODO This should be when a line is read

		traceNotify();
		this.notify();
	}

	@Override
	public String toString() {
		return SLAVE;
	}

	@Override
	public LinkedList<KeyValueInterface<String, Integer>> addKeyValToLocalLinkedList() {
		final LinkedList<KeyValueInterface<String, Integer>> finalLocalLinkedList = new LinkedList<KeyValueInterface<String, Integer>>();

		for (Map.Entry<String, Integer> keyVal : result.entrySet()) {
			final KeyValueInterface<String, Integer> finalKeyVal = new KeyValue<String, Integer>();
			finalKeyVal.setKey(keyVal.getKey());
			finalKeyVal.setValue(keyVal.getValue());
			finalLocalLinkedList.add(finalKeyVal);
		}
		return finalLocalLinkedList;
	}

	@Override
	public List<LinkedList<KeyValueInterface<String, Integer>>> updateReductionQueueListWithPartitions(
			final List<LinkedList<KeyValueInterface<String, Integer>>> reductionQueueList,
			final Map<String, Integer> aKeyToPartition) {
		for (Map.Entry<String, Integer> keyVal : aKeyToPartition.entrySet()) {
			final String key = keyVal.getKey();
			final Integer partition = keyVal.getValue();
			final Integer val = result.get(key);

			final KeyValueInterface<String, Integer> partialKeyVal = new KeyValue<String, Integer>();
			partialKeyVal.setKey(key);
			partialKeyVal.setValue(val);
			final LinkedList<KeyValueInterface<String, Integer>> localLinkedList = reductionQueueList.get(partition);
			localLinkedList.add(partialKeyVal);
			// aReductionQueueList.set(partition, localLinkedList);
			synchronized (reductionQueueList) {
				reductionQueueList.set(partition, localLinkedList);
			}

		}

		return reductionQueueList;
	}

	@Override
	public Map<String, Integer> partitionKeys(final Map<String, Integer> aKeyToPartition,
			final Map<String, Integer> aResult) {
		final PartitionerInterface<String, Integer> partitioner = PartitionerFactory.getPartitioner();
		for (Map.Entry<String, Integer> keyVal : aResult.entrySet()) {
			final int partition = partitioner.getPartition(keyVal.getKey(), keyVal.getValue(), numThreads);
			aKeyToPartition.put(keyVal.getKey(), partition);
		}
		return aKeyToPartition;
	}
	
	@Override
	public void slaveQuit() {
		if (client!=null) {
			try {
				client.quit();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public LinkedList<KeyValueInterface<String, Integer>> block(final ModelInterface aSlaveModel,
			final LinkedList<KeyValueInterface<String, Integer>> aLocalList, final boolean aLoop) {
		boolean loopy = aLoop;
		while (loopy) {
			final BlockingQueue<KeyValueInterface<String, Integer>> aKeyValueQueue = aSlaveModel.getKeyValueQueue();

			KeyValueInterface<String, Integer> keyVal = null;

			try {
				traceDequeueRequest(aKeyValueQueue);
				keyVal = aKeyValueQueue.take();
				traceDequeue(keyVal);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				traceQuit();
				e.printStackTrace();
				// continue;
			}

			// if keyVal is null, null need to stop consuming
			if (keyVal.getKey() == null) {
				loopy = false;
				continue;
			}

			aLocalList.add(keyVal);

		}
		return aLocalList;

	}

}
