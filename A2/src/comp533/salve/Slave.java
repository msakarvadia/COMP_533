package comp533.salve;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import comp533.mvc.KeyValue;
import comp533.mvc.KeyValueInterface;
import comp533.mvc.ModelInterface;
import comp533.mvc.ReducerFactory;
import comp533.mvc.ReducerInterface;
import comp533.partitioner.PartitionerFactory;
import comp533.partitioner.PartitionerInterface;
import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class Slave extends AMapReduceTracer implements SlaveInterface {
	final int number;
	final int numThreads;
	final ModelInterface slaveModel;
	LinkedList<KeyValueInterface<String, Integer>> localList = new LinkedList<KeyValueInterface<String, Integer>>();
	LinkedList<KeyValueInterface<String, Integer>> reductionLinkedList;
	private Map<String, Integer> result = new HashMap<String, Integer>();
	private Map<String, Integer> keyToPartition = new HashMap<String, Integer>();

	public Slave(final int identifier, final ModelInterface model, final int totalThreads) {
		number = identifier;
		slaveModel = model;
		numThreads = totalThreads;
	}

	@Override
	public void run() {
		System.out.println("RUN METHOD");
		while (true) {
			boolean loop = true;
			while (loop) {
				BlockingQueue<KeyValueInterface<String, Integer>> aKeyValueQueue = slaveModel.getKeyValueQueue();

				KeyValueInterface<String, Integer> keyVal = null;

				try {
					traceDequeueRequest(aKeyValueQueue);
					keyVal = aKeyValueQueue.take();
					traceDequeue(keyVal);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					// continue;
				}

				// if keyVal is null, null need to stop consuming
				if (keyVal.getKey() == null) {
					loop = false;
					continue;
				}

				localList.add(keyVal);

			}

			// partially reduce list

			final ReducerInterface<String, Integer> reducer = ReducerFactory.getReducer();
			result = reducer.reduce(localList);

			// this gets the correct partitions for each key
			final PartitionerInterface<String, Integer> partitioner = PartitionerFactory.getPartitioner();
			for (Map.Entry<String, Integer> keyVal : result.entrySet()) {
				final int partition = partitioner.getPartitioner(keyVal.getKey(), keyVal.getValue(), numThreads);
				keyToPartition.put(keyVal.getKey(), partition);
			}
			// System.out.println("key to partition "+keyToPartition.toString());

			// add partitally reduced keyValues to the ReducitonQueueList

			List<LinkedList<KeyValueInterface<String, Integer>>> aReductionQueueList = slaveModel
					.getReductionQueueList();

			for (Map.Entry<String, Integer> keyVal : keyToPartition.entrySet()) {
				final String key = keyVal.getKey();
				final Integer partition = keyVal.getValue();
				final Integer val = result.get(key);

				KeyValueInterface<String, Integer> partialKeyVal = new KeyValue<String, Integer>();
				partialKeyVal.setKey(key);
				partialKeyVal.setValue(val);
				LinkedList<KeyValueInterface<String, Integer>> localLinkedList = aReductionQueueList.get(partition);
				localLinkedList.add(partialKeyVal);
				// aReductionQueueList.set(partition, localLinkedList);
				synchronized (aReductionQueueList) {
					aReductionQueueList.set(partition, localLinkedList);
				}

			}

			// wait for the other slaves to complete their splitting
			slaveModel.getBarrier().barrier();
			traceSplitAfterBarrier(number, aReductionQueueList);

			// Need to do final reduction of this threads linkedList
			synchronized (aReductionQueueList) {
				LinkedList<KeyValueInterface<String, Integer>> updatedLocalLinkedList = aReductionQueueList.get(number);
				result = reducer.reduce(updatedLocalLinkedList);
				LinkedList<KeyValueInterface<String, Integer>> finalLocalLinkedList = new LinkedList<KeyValueInterface<String, Integer>>();
				for (Map.Entry<String, Integer> keyVal : result.entrySet()) {
					KeyValueInterface<String, Integer> finalKeyVal = new KeyValue<String, Integer>();
					finalKeyVal.setKey(keyVal.getKey());
					finalKeyVal.setValue(keyVal.getValue());
					finalLocalLinkedList.add(finalKeyVal);
				}
				// aReductionQueueList.set(number, finalLocalLinkedList);
				aReductionQueueList.set(number, finalLocalLinkedList);
			}

			loop = true;
			slaveModel.getJoiner().finished();
			// System.out.println(aReductionQueueList.toString());

			// System.out.println(slaveModel.getJoiner().getTotalFinished());
			if (slaveModel.getJoiner().getTotalFinished() == numThreads) {
				slaveModel.getJoiner().join();

			}

			synchronized (this) {
				try {
					traceWait();
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			continue;
		}

		//return;
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

}
