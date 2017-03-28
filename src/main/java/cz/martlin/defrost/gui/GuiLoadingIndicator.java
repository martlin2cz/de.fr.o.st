package cz.martlin.defrost.gui;

import cz.martlin.defrost.tasks.BaseLoadingIndicator;
import cz.martlin.defrost.utils.DefrostException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

/**
 * Indicates the status of task into GUI (to status bar as well as disabling the
 * whole frame when running).
 * 
 * @author martin
 *
 */
public class GuiLoadingIndicator implements BaseLoadingIndicator {

	private final MainController main;

	public GuiLoadingIndicator(MainController main) {
		super();
		this.main = main;
	}

	@Override
	public DoubleProperty getProgress() {
		return main.getLoadingPrgbr().progressProperty();
	}

	@Override
	public StringProperty getMessage() {
		return main.getStatusBarLbl().textProperty();
	}

	@Override
	public BooleanProperty getStatus() {
		return new TaskStatusToGuiProperty(main);
	}

	@Override
	public void error(DefrostException e) {
		main.error(e.getMessage(), true);
	}

	/**
	 * Property which reports its changes to particular GUI components
	 * visibility and disability.
	 * 
	 * @author martin
	 *
	 */
	private static class TaskStatusToGuiProperty extends SimpleBooleanProperty {

		private final MainController main;

		public TaskStatusToGuiProperty(MainController main) {
			super();
			this.main = main;
		}

		@Override
		public void bind(ObservableValue<? extends Boolean> rawObservable) {
			super.bind(rawObservable);
			main.getLoadingIconPrind().visibleProperty().bind(rawObservable);
			main.getLoadingPrgbr().visibleProperty().bind(rawObservable);
			main.getMainGrid().disableProperty().bind(rawObservable);
			main.getStopButt().disableProperty().bind(((BooleanProperty) rawObservable).not());
		}

		@Override
		public void unbind() {
			super.unbind();
			main.getLoadingIconPrind().visibleProperty().unbind();
			main.getLoadingPrgbr().visibleProperty().unbind();
			main.getMainGrid().disableProperty().unbind();
			main.getStopButt().disableProperty().unbind();
		}
	}

}
