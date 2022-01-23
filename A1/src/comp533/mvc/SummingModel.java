package comp533.mvc;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class SummingModel extends AMapReduceTracer implements ModelInterface{
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	private String inputString = null;
	private Map<String, Integer> Result = new HashMap<String, Integer>();
	ReducerInterface<String, Integer> Reducer = ReducerFactory.getReducer();
	IntSummingMapperInterface<String, Integer> Mapper = IntSummingMapperFactory.getMapper();
	
	@Override
	public Map<String, Integer> getResult() {
		return Result;
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener newListener) {
		propertyChangeSupport.addPropertyChangeListener(newListener);
	}

	@Override
	public void setInputString(String newVal) {
		String oldInputString = inputString;
		inputString = newVal;
		PropertyChangeEvent inputEvent = new PropertyChangeEvent(this, "InputString", oldInputString, newVal);
		propertyChangeSupport.firePropertyChange(inputEvent);
	}

	@Override
	public void computeResult() {
		String oldResult = Result.toString();
		if (Result.isEmpty()) {
			oldResult = null;
		}
		final String tokens = inputString;
		Result.clear();
		
		List<KeyValueInterface<String, Integer>> KeyValList = Mapper.map(tokens);
		//TODO PROPERTY CHANGE FOR KeyValList
		Result = Reducer.reduce(KeyValList);
		//TODO property change for Result
		PropertyChangeEvent resultComputed = new PropertyChangeEvent(this, "Result", oldResult, Result.toString());
		propertyChangeSupport.firePropertyChange(resultComputed);		
	}

	@Override
	public void computeResultOld() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public String toString() {// overriding the toString() method
		return MODEL;
	}
	

}
