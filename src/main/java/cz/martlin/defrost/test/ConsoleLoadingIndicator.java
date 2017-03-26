package cz.martlin.defrost.test;

import java.io.PrintStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

import cz.martlin.defrost.misc.DefrostException;
import cz.martlin.defrost.tasks.BaseLoadingIndicator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;

public class ConsoleLoadingIndicator implements BaseLoadingIndicator {

	private final PrintStream out;
	private final DoubleProperty progress;
	private final StringProperty message;
	private final BooleanProperty status;

	public ConsoleLoadingIndicator(PrintStream out) {
		initToolkit();

		this.out = out;

		this.progress = new SimpleDoubleProperty();
		this.message = new SimpleStringProperty();
		this.status = new SimpleBooleanProperty();

		this.progress.addListener(new ConsolePrintingListener<Number>("progress", out));
		this.message.addListener(new ConsolePrintingListener<>("message", out));
		this.status.addListener(new ConsolePrintingListener<>("status", out));
	}
	///////////////////////////////////////////////////////////////////////////////

	@Override
	public DoubleProperty getProgress() {
		return progress;
	}

	@Override
	public StringProperty getMessage() {
		return message;
	}

	@Override
	public BooleanProperty getStatus() {
		return status;
	}

	@Override
	public void error(DefrostException e) {
		out.print("ERROR: " + e.getMessage());
		e.printStackTrace();
	}

	///////////////////////////////////////////////////////////////////////////////

	public static void initToolkit() {
		try {
			// http://stackoverflow.com/questions/11273773/javafx-2-1-toolkit-not-initialized
			final CountDownLatch latch = new CountDownLatch(1);
			SwingUtilities.invokeLater(() -> {
				new JFXPanel(); // initializes JavaFX environment
				latch.countDown();
			});

			if (!latch.await(5L, TimeUnit.SECONDS))
				throw new InterruptedException("await interrupted");

		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	///////////////////////////////////////////////////////////////////////////////

	public class ConsolePrintingListener<T> implements ChangeListener<T> {

		private final String prefix;
		private final PrintStream out;

		public ConsolePrintingListener(String prefix, PrintStream out) {
			this.prefix = prefix;
			this.out = out;
		}

		@Override
		public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
			out.println(prefix + ": " + newValue);
		}

	}

}
