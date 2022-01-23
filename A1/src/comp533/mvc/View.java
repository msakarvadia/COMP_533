package comp533.mvc;

import java.beans.PropertyChangeEvent;
import gradingTools.comp533s19.assignment0.AMapReduceTracer; 

public class View extends AMapReduceTracer implements ViewInterface {

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		tracePropertyChange(evt);
	}
	
	@Override
	public String toString(){//overriding the toString() method  
		  return VIEW;  
	}

}
