package comp533.mvc;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public interface ViewInterface extends PropertyChangeListener{
	public void propertyChange(PropertyChangeEvent evt);
	public String toString();
}
