package comp533.joiner;

import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class Joiner extends AMapReduceTracer implements JoinerInterface {
	int count;
	int totalFinish = 0;
	
	public Joiner(final int numThreads) {
		count = numThreads;
	}
	
	@Override
	public String toString() {
		return JOINER;
	}
	
	@Override
	public void finished() {
		traceJoinerFinishedTask(this, count, totalFinish);
		totalFinish++;
		
	}

	@Override
	public synchronized void join() {
		// TODO Auto-generated method stub
		while (totalFinish < count) {
			try {
				traceJoinerWaitStart(this, count, totalFinish);
				wait();
				traceJoinerWaitEnd(this, count, totalFinish);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		traceNotify();
		notify();
		traceJoinerRelease(this, count, totalFinish);
		totalFinish = 0;
		
	}

}
