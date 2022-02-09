package comp533.barrier;

import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class Barrier extends AMapReduceTracer implements BarrierInterface {
	final int count;
	int numCalls = 0;

	public Barrier(int numThreads) {
		count = numThreads;
	}

	@Override
	public String toString() {
		return BARRIER;
	}

	@Override
	public void barrier() {
		numCalls++;
		while (numCalls < count) {
			try {
				traceBarrierWaitStart(this, count, numCalls);
				wait();
				traceBarrierWaitStart(this, count, numCalls);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		notifyAll();
		traceBarrierReleaseAll(this, count, numCalls);
		
	}
}
