package cz.martlin.defrost.tasks;

import cz.martlin.defrost.utils.DefrostException;
import cz.martlin.defrost.utils.Msg;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

/**
 * Implements task with additional methods to using {@link BaseLoadingIndicator}
 * 's instances report status of loading. Automatically generates progress and
 * message.
 * 
 * @author martin
 *
 * @param <T>
 */
public abstract class ItemsLoadingTask<T> extends Task<T> {

	private final BaseLoadingIndicator indicator;
	protected final String itemsDesc;
	protected final String itemDesc;

	private int totalCount;
	private int currentItemIndex;
	private String currentItem;

	public ItemsLoadingTask(String itemsDesc, String itemDesc, BaseLoadingIndicator indicator) {
		super();

		this.indicator = indicator;

		this.itemsDesc = itemsDesc;
		this.itemDesc = itemDesc;

		updateTitle(itemDesc);

		setStatusIndicator(indicator);
		makeStatusIndicatorUnset(indicator);
	}

	///////////////////////////////////////////////////////////////////////////////

	/**
	 * Sets up the binding to indicator.
	 * 
	 * @param indicator
	 */
	protected void setStatusIndicator(BaseLoadingIndicator indicator) {
		indicator.getProgress().bind(progressProperty());
		indicator.getMessage().bind(messageProperty());
		indicator.getStatus().bind(runningProperty());
	}

	/**
	 * Unsets binding of indicator.
	 * 
	 * @param indicator
	 */
	protected void unsetStatusIndicators(BaseLoadingIndicator indicator) {
		indicator.getProgress().unbind();
		indicator.getMessage().unbind();
		indicator.getStatus().unbind();
	}

	/**
	 * Makes unset of binding to be performed at the end of task.
	 * 
	 * @param indicator
	 */
	protected void makeStatusIndicatorUnset(BaseLoadingIndicator indicator) {

		EventHandler<WorkerStateEvent> handler = (event) -> {
			unsetStatusIndicators(indicator);
		};

		this.setOnCancelled(handler);
		this.setOnSucceeded(handler);
		this.setOnFailed(handler);
	}

	///////////////////////////////////////////////////////////////////////////////

	/**
	 * Reports that loading of given count of items has started.
	 * 
	 * @param totalCount
	 */
	public void loadingStarted(int totalCount) {
		this.totalCount = totalCount;
		this.currentItemIndex = 0;

		updateProgressAndMessage(itemsDesc, null, Msg.getString("started")); //$NON-NLS-1$

	}

	/**
	 * Reports that loading of next item has started.
	 * 
	 * @param currentItem
	 */
	public void loadingNextItem(String currentItem) {
		this.currentItem = currentItem;

		updateProgressAndMessage(itemDesc, currentItem, null);

		this.currentItemIndex++;
	}

	/**
	 * Reports that something with the current item happened.
	 * 
	 * @param message
	 */
	public void loadingItemUpdated(String message) {
		updateProgressAndMessage(itemDesc, currentItem, message);
	}

	/**
	 * Reports that loading have been completed.
	 */
	public void loadingFinished() {
		// this.currentItemIndex = 0;
		this.currentItem = null;

		updateProgressAndMessage(itemsDesc, null, Msg.getString("completed")); //$NON-NLS-1$

	}

	///////////////////////////////////////////////////////////////////////////////

	/**
	 * Reports error.
	 * 
	 * @param e
	 */
	public void error(DefrostException e) {
		indicator.error(e);
	}

	///////////////////////////////////////////////////////////////////////////////

	/**
	 * Updates progress and message.
	 * 
	 * @param desc
	 *            {@link #itemDesc} or {@link #itemsDesc}
	 * @param item
	 *            description of current item (or null if nonsence)
	 * @param suffix
	 *            additional info of item or loading (or null if not required)
	 */
	private void updateProgressAndMessage(String desc, String item, String suffix) {
		updateProgress(currentItemIndex - 1, totalCount);

		String msg = generateMessage(desc, item, suffix);
		updateMessage(msg);
	}

	/**
	 * Generates message in format
	 * <code>(currentItemIndex/totalCount) item desc suffix</code>. Params item
	 * and suffix are optional.
	 * 
	 * @param desc
	 * @param item
	 * @param suffix
	 * @return
	 */
	private String generateMessage(String desc, String item, String suffix) {
		StringBuilder msg = new StringBuilder();

		msg.append('(');
		msg.append(currentItemIndex);
		msg.append('/');
		msg.append(totalCount);
		msg.append(')');
		msg.append(' ');
		msg.append(desc);

		if (item != null) {
			msg.append(' ');
			msg.append(item);
		}

		if (suffix != null) {
			msg.append(' ');
			msg.append(suffix);
		}

		return msg.toString();
	}

}
