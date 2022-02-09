package comp533.mvc;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import comp533.salve.Slave;

//import gradingTools.comp533s19.assignment0.AMapReduceTracer;

//public class SummingModel extends AMapReduceTracer implements ModelInterface
public class SummingModel extends Model implements ModelInterface{
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	private String inputString = null;
	private Map<String, Integer> result = new HashMap<String, Integer>();
	final ReducerInterface<String, Integer> reducer = ReducerFactory.getReducer();
	final IntSummingMapperInterface<String, Integer> mapper = IntSummingMapperFactory.getMapper();


	@Override
	public void addPropertyChangeListener(final PropertyChangeListener newListener) {
		propertyChangeSupport.addPropertyChangeListener(newListener);
	}

	@Override
	public void setInputString(final String newVal) {
		final String oldString = inputString;
		inputString = newVal;
		final String label = "InputString";
		final PropertyChangeEvent inputEvent = new PropertyChangeEvent(this, label, oldString, newVal);
		propertyChangeSupport.firePropertyChange(inputEvent);
	}

	@Override
	public void computeResult() {
		String oldResult = result.toString();
		if (result.isEmpty()) {
			oldResult = null;
		}
		final String tokens = inputString;
		result.clear();

		final String space = " ";
		final List<String> ListOfToken = Arrays.asList(tokens.split(space));
		final List<KeyValueInterface<String, Integer>> keyValList = mapper.map(ListOfToken);
		result = reducer.reduce(keyValList);
		final String label = "Result";
		final PropertyChangeEvent resultComputed = new PropertyChangeEvent(this, label, oldResult,result.toString());
		propertyChangeSupport.firePropertyChange(resultComputed);
	}

	@Override
	public String toString() {// overriding the toString() method
		return MODEL;
	}

}
