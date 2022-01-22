package comp533.mvc;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import gradingTools.comp533s19.assignment0.AMapReduceTracer; 

public class Model extends AMapReduceTracer implements ModelInterface{
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	private String inputString = null;
	public Map<String, Integer> Result =new HashMap<String, Integer>();

	public void addPropertyChangeListener(PropertyChangeListener newListener) {
		propertyChangeSupport.addPropertyChangeListener(newListener);
	}

	public void setInputString(String newVal) {
		String oldInputString = inputString;
		inputString = newVal;
		PropertyChangeEvent inputEvent = new PropertyChangeEvent(this, "InputString", oldInputString, newVal);
		propertyChangeSupport.firePropertyChange(inputEvent);
	}
	
	public boolean quit() {
		if (inputString.equals(QUIT)) {
			return true;
		}
		return false;
	}
	
	public void computeResult() {
		String oldResult = Result.toString();
		if (Result.isEmpty()) {
			oldResult = null;
		}
		final String tokens = inputString;
		Result.clear();
		
		final String[] arrayOfToken = tokens.split(" ");
		// make an array that contains unique tokens
		final List<String> uniqueTokens = new ArrayList<>();
		// make a second array that maintains the counts of said unique tokens
		final List<Integer> counts = new ArrayList<>();
		for (String a : arrayOfToken) {
			if (!uniqueTokens.contains(a)) {
				uniqueTokens.add(a);
			}
			int index = uniqueTokens.indexOf(a);

			if (counts.size() < uniqueTokens.size()) {
				counts.add(1);
			} else {
				counts.set(index, counts.get(index) + 1);
			}
		}
		
		for (int i = 0; i < counts.size(); i++) {
			Result.put(uniqueTokens.get(i), counts.get(i));	
		}
		
		PropertyChangeEvent resultComputed = new PropertyChangeEvent(this, "Result", oldResult, Result.toString());
		propertyChangeSupport.firePropertyChange(resultComputed);
		
	}

	public Map<String, Integer> getResult(){
		return Result;
	}
	public String toString() {// overriding the toString() method
		return MODEL;
	}
}
