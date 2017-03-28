package cz.martlin.defrost.tasks;

import cz.martlin.defrost.utils.DefrostException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;

/**
 * Interface representing something which can hold the info of the currently
 * running task. {@link ItemsLoadingTask}} uses this.
 * 
 * @author martin
 *
 */
public interface BaseLoadingIndicator {

	/**
	 * Property holding the progress of current task.
	 * 
	 * @return
	 */
	public DoubleProperty getProgress();

	/**
	 * Property holding the current description.
	 * 
	 * @return
	 */
	public StringProperty getMessage();

	/**
	 * Property holding whether is the task currently running or not.
	 * 
	 * @return
	 */
	public BooleanProperty getStatus();

	/**
	 * Handles the error.
	 * 
	 * @param e
	 */
	public void error(DefrostException e);

}