package cz.martlin.defrost.tasks;

import java.util.List;

import cz.martlin.defrost.dataobj.Comment;
import cz.martlin.defrost.misc.DefrostException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Deprecated
public class EmbeddedIndicator implements BaseLoadingIndicator {

	private final BaseLoadingIndicator owning;
	private final double originalProgress;
	private final String originalMessage;

	public EmbeddedIndicator(BaseLoadingIndicator owning, List<Comment> result) {
		this.owning = owning;

		this.originalProgress = owning.getProgress().get();
		this.originalMessage = owning.getMessage().get();
	}

	@Override
	public DoubleProperty getProgress() {
		return owning.getProgress();
	}

	@Override
	public StringProperty getMessage() {
		return owning.getMessage();
	}

	@Override
	public BooleanProperty getStatus() {
		return owning.getStatus();
	}

	@Override
	public void error(DefrostException e) {
		owning.error(e);
	}

	public void finish() {
		this.owning.getProgress().set(originalProgress);
		this.owning.getMessage().set(originalMessage);
	}

}
