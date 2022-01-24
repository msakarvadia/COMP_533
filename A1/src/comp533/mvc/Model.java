package comp533.mvc;

import java.beans.PropertyChangeSupport;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class Model extends AMapReduceTracer implements ModelInterface {
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	private String inputString = null;
	private Map<String, Integer> result = new HashMap<String, Integer>();
	//final ReducerInterface<String, Integer> reducer = ReducerFactory.getReducer();
	//final TokenCountingMapperInterface<String, Integer> mapper = TokenCountingMapperFactory.getMapper();
	final String space = " ";

	@Override
	public void addPropertyChangeListener(final PropertyChangeListener newListener) {
		propertyChangeSupport.addPropertyChangeListener(newListener);
	}

	@Override
	public void setInputString(final String newVal) {
		final String oldInputString = inputString;
		inputString = newVal;
		final PropertyChangeEvent inputEvent = new PropertyChangeEvent(this, "InputString", oldInputString, newVal);
		propertyChangeSupport.firePropertyChange(inputEvent);
	}

	@Override
	public void computeResult() {
		final ReducerInterface<String, Integer> reducer = ReducerFactory.getReducer();
		final TokenCountingMapperInterface<String, Integer> mapper = TokenCountingMapperFactory.getMapper();
		String oldResult = result.toString();
		if (result.isEmpty()) {
			oldResult = null;
		}
		final String tokens = inputString;
		result.clear();
		final List<String> listOfToken = Arrays.asList(tokens.split(space));
		final List<KeyValueInterface<String, Integer>> keyValList = mapper.map(listOfToken);
		// TODO PROPERTY CHANGE FOR KeyValList
		result = reducer.reduce(keyValList);
		// TODO property change for Result
		final PropertyChangeEvent resultComputed = new PropertyChangeEvent(this, "Result", oldResult, result.toString());
		propertyChangeSupport.firePropertyChange(resultComputed);

	}


	@Override
	public Map<String, Integer> getResult() {
		return result;
	}

	@Override
	public String toString() {// overriding the toString() method
		return MODEL;
	}
}
