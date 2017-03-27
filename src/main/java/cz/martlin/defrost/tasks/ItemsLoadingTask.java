package cz.martlin.defrost.tasks;

import cz.martlin.defrost.misc.DefrostException;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

/**
 * Shows on statusbar followings:
 * 
 * <pre>
 *  Loading A started
 *  Loading B X
 *  Loading B X Y
 *  Loading A complete
 * 
 * </pre>
 * 
 * @author martin
 *
 * @param <T>
 */
public abstract class ItemsLoadingTask<T> extends Task<T> {

	protected final BaseLoadingIndicator indicator;	//XXX back to private
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

	protected void setStatusIndicator(BaseLoadingIndicator indicator) {
		indicator.getProgress().bind(progressProperty());
		indicator.getMessage().bind(messageProperty());
		indicator.getStatus().bind(runningProperty());
	}

	protected void unsetStatusIndicators(BaseLoadingIndicator indicator) {
		indicator.getProgress().unbind();
		indicator.getMessage().unbind();
		indicator.getStatus().unbind();
	}

	protected void makeStatusIndicatorUnset(BaseLoadingIndicator indicator) {

		EventHandler<WorkerStateEvent> handler = (event) -> {
			unsetStatusIndicators(indicator);
		};

		this.setOnCancelled(handler);
		this.setOnSucceeded(handler);
		this.setOnFailed(handler);
	}

	///////////////////////////////////////////////////////////////////////////////

	public void loadingStarted(int totalCount) {
		this.totalCount = totalCount;
		this.currentItemIndex = 0;

		updateProgressAndMessage(itemsDesc, null, "started");

	}

	public void loadingNextItem(String currentItem) {
		this.currentItem = currentItem;

		updateProgressAndMessage(itemDesc, currentItem, null);

		this.currentItemIndex++;
	}

	public void loadingItemUpdated(String message) {
		updateProgressAndMessage(itemDesc, currentItem, message);
	}

	public void loadingFinished() {
		//this.currentItemIndex = 0;
		this.currentItem = null;

		updateProgressAndMessage(itemsDesc, null, "completed");

	}

	///////////////////////////////////////////////////////////////////////////////

	public void error(DefrostException e) {
		indicator.error(e);
	}

	///////////////////////////////////////////////////////////////////////////////

	private void updateProgressAndMessage(String desc, String item, String suffix) {
		updateProgress(currentItemIndex - 1, totalCount);

		String msg = generateMessage(desc, item, suffix);
		updateMessage(msg);
	}

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
