package comp533.mvc;

import java.beans.PropertyChangeEvent;


public class View extends gradingTools.comp533s19.assignment0.AMapReduceTracer implements ViewInterface {

	public void propertyChange(PropertyChangeEvent evt) {
		tracePropertyChange(evt);
	}
	
	public String toString(){//overriding the toString() method  
		  return VIEW;  
	}

}
