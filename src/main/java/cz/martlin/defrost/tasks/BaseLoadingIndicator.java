package cz.martlin.defrost.tasks;

import cz.martlin.defrost.misc.DefrostException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;

public interface BaseLoadingIndicator {

	public DoubleProperty getProgress();

	public StringProperty getMessage();

	public BooleanProperty getStatus();
	
	public void error(DefrostException e);
	
}