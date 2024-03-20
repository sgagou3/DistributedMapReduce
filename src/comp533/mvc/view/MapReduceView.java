package comp533.mvc.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.EventListener;

public interface MapReduceView extends PropertyChangeListener, EventListener, Serializable {
	void propertyChange(PropertyChangeEvent aPropertyChangeEvent);
}
