package comp533.mvc;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class Model extends gradingTools.comp533s19.assignment0.AMapReduceTracer {
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	private String inputString = null;
	public Map<String, Integer> Result =new HashMap<String, Integer>();

	public void addPropertyChangeListener(PropertyChangeListener newListener) {
		propertyChangeSupport.addPropertyChangeListener(newListener);
	}

	public void setInput(String newVal) {
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
		Map<String, Integer> oldResult = Result;
		String tokens = inputString;
		
		String[] arrOfTok = tokens.split(" ");
		// make an array that contains unique tokens
		List<String> uniqueTokens = new ArrayList<>();
		// make a second array that maitains the counts of said unique tokens
		List<Integer> counts = new ArrayList<>();
		for (String a : arrOfTok) {
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
		
		for (int tok_idx = 0; tok_idx < counts.size(); tok_idx++) {
			Result.put(uniqueTokens.get(tok_idx), counts.get(tok_idx));	
		}
		
		PropertyChangeEvent resultComputed = new PropertyChangeEvent(this, "Result", oldResult, Result);
		propertyChangeSupport.firePropertyChange(resultComputed);
		System.out.println("Output:");
	}

	public String toString() {// overriding the toString() method
		return MODEL;
	}
}
