package comp533.salve;

import gradingTools.comp533s19.assignment0.AMapReduceTracer; 

public class Slave extends AMapReduceTracer implements SlaveInterface{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		return;
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
