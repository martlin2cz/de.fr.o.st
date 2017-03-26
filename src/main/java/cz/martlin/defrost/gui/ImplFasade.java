package cz.martlin.defrost.gui;

import java.util.List;

import cz.martlin.defrost.base.BaseForumDescriptor;
import cz.martlin.defrost.dataobj.Comment;
import cz.martlin.defrost.dataobj.PostInfo;
import cz.martlin.defrost.misc.DefrostException;
import cz.martlin.defrost.output.ie.ImportExportClassesWrapper.CSVCommentsExportTask;
import cz.martlin.defrost.output.ie.ImportExportClassesWrapper.CSVCommentsImportTask;
import cz.martlin.defrost.output.ie.ImportExportClassesWrapper.CSVPostsExportTask;
import cz.martlin.defrost.output.ie.ImportExportClassesWrapper.CSVPostsImportTask;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableStringValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class ImplFasade {

	private final BaseForumDescriptor descriptor;
	private final GuiLoadingIndicator indicator;

	private final ObservableList<PostInfo> posts;
	private final ObservableList<Comment> comments;

	private Task<?> currentTask;

	public ImplFasade(BaseForumDescriptor descriptor, MainController controller) {
		this.descriptor = descriptor;
		this.indicator = new GuiLoadingIndicator(controller);

		this.posts = FXCollections.observableArrayList();
		this.comments = FXCollections.observableArrayList();
	}

	///////////////////////////////////////////////////////////////////////////////

	public ObservableStringValue getDiscussInUseDesc() {
		return new SimpleStringProperty(descriptor.getDescription());
	}

	public ObservableList<String> getCategories() {
		return FXCollections.observableArrayList(descriptor.listAvaibleCategories());
	}

	public ObservableList<PostInfo> getPosts() {
		return posts;
	}

	public ObservableList<Comment> getComments() {
		return comments;
	}

	///////////////////////////////////////////////////////////////////////////////

	public void startLoadingPosts(List<String> categories) {
		// TODO
	}

	public void startLoadingComments(List<PostInfo> posts) {
		// TODO
	}

	///////////////////////////////////////////////////////////////////////////////

	public void startExportingPosts() {
		Task<?> task = new CSVPostsExportTask(indicator, posts);
		startTaskInBackground(task);
	}

	public void startImportingPosts() {
		Task<?> task = new CSVPostsImportTask(indicator, (event) -> {
			@SuppressWarnings("unchecked")
			List<PostInfo> posts = (List<PostInfo>) event.getSource().getValue();
			this.posts.addAll(posts);
		});

		startTaskInBackground(task);
	}

	public void startExportingComments() {
		Task<?> task = new CSVCommentsExportTask(indicator, comments);
		startTaskInBackground(task);
	}

	public void startImportingComments() {
		Task<?> task = new CSVCommentsImportTask(indicator, (event) -> {
			@SuppressWarnings("unchecked")
			List<Comment> comments = (List<Comment>) event.getSource().getValue();
			this.comments.addAll(comments);
		});

		startTaskInBackground(task);
	}

	///////////////////////////////////////////////////////////////////////////////

	private void startTaskInBackground(Task<?> task) {
		String name = task.getClass().getSimpleName() + "-thread";

		Thread thread = new Thread(task, name);
		thread.start();

		this.currentTask = task;
	}

	public void stopTaskInBackground() {
		boolean succ = this.currentTask.cancel();
		if (!succ) {
			DefrostException e = new DefrostException("Canceling task failed");
			indicator.error(e);
		}

		this.currentTask = null;
	}

}
