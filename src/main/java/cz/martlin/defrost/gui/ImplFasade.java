package cz.martlin.defrost.gui;

import java.util.Arrays;
import java.util.List;

import cz.martlin.defrost.base.BaseForumDescriptor;
import cz.martlin.defrost.dataobj.Comment;
import cz.martlin.defrost.dataobj.PostInfo;
import cz.martlin.defrost.input.load.CommentsLoadingTask;
import cz.martlin.defrost.input.load.PostsLoadingTask;
import cz.martlin.defrost.input.tools.ObservableUniquesList;
import cz.martlin.defrost.misc.DefrostException;
import cz.martlin.defrost.output.ie.ImportExportClassesWrapper.CSVCommentsExportTask;
import cz.martlin.defrost.output.ie.ImportExportClassesWrapper.CSVCommentsImportTask;
import cz.martlin.defrost.output.ie.ImportExportClassesWrapper.CSVPostsExportTask;
import cz.martlin.defrost.output.ie.ImportExportClassesWrapper.CSVPostsImportTask;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableStringValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

public class ImplFasade {

	private final BaseForumDescriptor descriptor;
	private final GuiLoadingIndicator indicator;

	private final ObservableList<PostInfo> posts;
	private final ObservableList<Comment> comments;

	private Task<?> currentTask;

	public ImplFasade(BaseForumDescriptor descriptor, MainController controller) {
		this.descriptor = descriptor;
		this.indicator = new GuiLoadingIndicator(controller);

		this.posts = new ObservableUniquesList<>();
		this.comments = new ObservableUniquesList<>();
	}

	///////////////////////////////////////////////////////////////////////////////

	public ObservableStringValue getDiscussInUseDesc() {
		return new SimpleStringProperty(descriptor.getDescription());
	}

	public ObservableList<String> getCategories() {
		List<String> categories = Arrays.asList(descriptor.listAvaibleCategories());
		return new ObservableUniquesList<String>(categories);
	}

	public ObservableList<PostInfo> getPosts() {
		return posts;
	}

	public ObservableList<Comment> getComments() {
		return comments;
	}

	///////////////////////////////////////////////////////////////////////////////

	public void startLoadingPosts(List<String> categories) {
		Task<?> task = new PostsLoadingTask(descriptor, indicator, categories);

		addFinishListeners(task, this.posts);

		startTaskInBackground(task);
	}

	public void startLoadingComments(List<PostInfo> posts) {
		Task<?> task = new CommentsLoadingTask(descriptor, indicator, posts);

		addFinishListeners(task, this.comments);

		startTaskInBackground(task);
	}

	///////////////////////////////////////////////////////////////////////////////

	public void startExportingPosts() {
		Task<?> task = new CSVPostsExportTask(indicator, posts);

		startTaskInBackground(task);
	}

	public void startImportingPosts() {
		Task<?> task = new CSVPostsImportTask(indicator);

		addFinishListeners(task, this.posts);

		startTaskInBackground(task);
	}

	public void startExportingComments() {
		Task<?> task = new CSVCommentsExportTask(indicator, comments);

		startTaskInBackground(task);
	}

	public void startImportingComments() {
		Task<?> task = new CSVCommentsImportTask(indicator);

		addFinishListeners(task, this.comments);

		startTaskInBackground(task);
	}

	///////////////////////////////////////////////////////////////////////////////

	private <T> void addFinishListeners(Task<?> task, List<T> resultList) {
		EventHandler<WorkerStateEvent> handler = (event) -> {

			@SuppressWarnings("unchecked")
			List<T> items = (List<T>) event.getSource().getValue();
			
			if (items != null) {
				resultList.addAll(items);
			}
		};

		task.setOnSucceeded(handler);
		task.setOnCancelled(handler);
		task.setOnFailed(handler);
	}

	private void startTaskInBackground(Task<?> task) {
		String name = task.getClass().getSimpleName() + "Thread";

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
