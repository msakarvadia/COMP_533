package comp533.joiner;

import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class Joiner extends AMapReduceTracer implements JoinerInterface {
	int count;
	int totalFinish = 0;
	
	public Joiner(final int numThreads) {
		count = numThreads;
	}
	
	public int getTotalFinished() {
		return totalFinish;
	}
	
	@Override
	public String toString() {
		return JOINER;
	}
	
	@Override
	public synchronized void finished() {
		totalFinish++;
		traceJoinerFinishedTask(this, count, totalFinish);
		
		
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
		//totalFinish = 0;
		traceNotify();
		notify();
		traceJoinerRelease(this, count, totalFinish);
		
		
	}

}
