package comp533.mvc;


import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Map;


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
	

	
	//A2 methods
	public int getNumThreads();
	public void setNumThreads(final int numThreads);
	public List<Thread> getThreads();
	
	
} 
