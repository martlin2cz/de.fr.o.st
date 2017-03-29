package cz.martlin.defrost.gui;

import java.util.Arrays;
import java.util.List;

import cz.martlin.defrost.dataobj.Comment;
import cz.martlin.defrost.dataobj.PostInfo;
import cz.martlin.defrost.forums.base.BaseForumDescriptor;
import cz.martlin.defrost.input.load.CommentsLoadingTask;
import cz.martlin.defrost.input.load.PostsLoadingTask;
import cz.martlin.defrost.output.ie.CommentsImportExportWrapper.CSVCommentsExportTask;
import cz.martlin.defrost.output.ie.CommentsImportExportWrapper.CSVCommentsImportTask;
import cz.martlin.defrost.output.ie.PostsImportExportWrapper.CSVPostsExportTask;
import cz.martlin.defrost.output.ie.PostsImportExportWrapper.CSVPostsImportTask;
import cz.martlin.defrost.output.out.CommentsByPostOutputTask;
import cz.martlin.defrost.output.out.CommentsByUserOutputTask;
import cz.martlin.defrost.output.out.CommentsUserXPostOutputTask;
import cz.martlin.defrost.tasks.BaseLoadingIndicator;
import cz.martlin.defrost.utils.DefrostException;
import cz.martlin.defrost.utils.Msg;
import cz.martlin.defrost.utils.ObservableUniquesList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableStringValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

/**
 * Makes the bridge between the UI and the core implementation.
 * 
 * @author martin
 *
 */
public class ImplFasade {

	private final BaseForumDescriptor descriptor;
	private final BaseLoadingIndicator indicator;

	private final ObservableList<PostInfo> posts;
	private final ObservableList<Comment> comments;

	private Task<?> currentTask;

	public ImplFasade(BaseForumDescriptor descriptor, BaseLoadingIndicator indicator) {
		this.descriptor = descriptor;
		this.indicator = indicator;

		this.posts = new ObservableUniquesList<>();
		this.comments = new ObservableUniquesList<>();
	}

	public ImplFasade(BaseForumDescriptor descriptor, MainController controller) {
		this.descriptor = descriptor;
		this.indicator = new GuiLoadingIndicator(controller);

		this.posts = new ObservableUniquesList<>();
		this.comments = new ObservableUniquesList<>();
	}

	///////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns description of discuss in use.
	 * 
	 * @return
	 */
	public ObservableStringValue getDiscussInUseDesc() {
		return new SimpleStringProperty(descriptor.getDescription());
	}

	/**
	 * Returns list of avaible categories.
	 * 
	 * @return
	 */
	public ObservableList<String> getCategories() {
		List<String> categories = Arrays.asList(descriptor.listAvaibleCategories());
		return new ObservableUniquesList<String>(categories);
	}

	/**
	 * Returns list of current posts.
	 * 
	 * @return
	 */
	public ObservableList<PostInfo> getPosts() {
		return posts;
	}

	/**
	 * Returns list of current comments.
	 * 
	 * @return
	 */
	public ObservableList<Comment> getComments() {
		return comments;
	}

	///////////////////////////////////////////////////////////////////////////////

	/**
	 * Starts loading of posts of given categories.
	 * 
	 * @param categories
	 */
	public void startLoadingPosts(List<String> categories) {
		Task<List<PostInfo>> task = new PostsLoadingTask(descriptor, indicator, categories);

		addFinishListeners(task, this.posts);

		startTaskInBackground(task);
	}

	/**
	 * Starts loading of comments of given posts.
	 * 
	 * @param posts
	 */
	public void startLoadingComments(List<PostInfo> posts) {
		Task<List<Comment>> task = new CommentsLoadingTask(descriptor, indicator, posts);

		addFinishListeners(task, this.comments);

		startTaskInBackground(task);
	}

	///////////////////////////////////////////////////////////////////////////////

	/**
	 * Starts exporting of current posts.
	 */
	public void startExportingPosts() {
		Task<Void> task = new CSVPostsExportTask(indicator, posts);

		startTaskInBackground(task);
	}

	/**
	 * Starts importing into current posts.
	 */
	public void startImportingPosts() {
		Task<List<PostInfo>> task = new CSVPostsImportTask(indicator);

		addFinishListeners(task, this.posts);

		startTaskInBackground(task);
	}

	/**
	 * Starts exporting of current comments.
	 */
	public void startExportingComments() {
		Task<Void> task = new CSVCommentsExportTask(indicator, comments);

		startTaskInBackground(task);
	}

	/**
	 * Starts importing into current comments.
	 */
	public void startImportingComments() {
		Task<List<Comment>> task = new CSVCommentsImportTask(indicator);

		addFinishListeners(task, this.comments);

		startTaskInBackground(task);
	}

	///////////////////////////////////////////////////////////////////////////////
	/**
	 * Starts outputting by post.
	 */
	public void startOutputByPost() {
		Task<Void> task = new CommentsByPostOutputTask(indicator, comments);

		startTaskInBackground(task);
	}

	/**
	 * Starts outputting by user.
	 */
	public void startOutputByUser() {
		Task<Void> task = new CommentsByUserOutputTask(indicator, comments);

		startTaskInBackground(task);
	}

	/**
	 * Starts outputting user x post table.
	 */
	public void startOutputUserXPost() {
		Task<Void> task = new CommentsUserXPostOutputTask(indicator, comments);

		startTaskInBackground(task);
	}

	///////////////////////////////////////////////////////////////////////////////

	/**
	 * Stops currently running task.
	 */
	public void stopTaskInBackground() {
		boolean succ = this.currentTask.cancel();
		if (!succ) {
			DefrostException e = new DefrostException(Msg.getString("Canceling_task_failed")); //$NON-NLS-1$
			indicator.error(e);
		}

		this.currentTask = null;
	}

	///////////////////////////////////////////////////////////////////////////////

	/**
	 * Starts given task, in background.
	 * 
	 * @param task
	 */
	private void startTaskInBackground(Task<?> task) {
		String name = task.getClass().getSimpleName() + "Thread";

		Thread thread = new Thread(task, name);
		thread.start();

		this.currentTask = task;
	}

	/**
	 * Adds "add result of task to given resultList" handlers to given task.
	 * 
	 * @param task
	 * @param resultList
	 */
	private <T> void addFinishListeners(Task<List<T>> task, List<T> resultList) {
		ChangeListener<? super List<T>> listener = (value, oldVal, newVal) -> {
			if (newVal != null) {
				resultList.addAll(newVal);
			}
		};

		task.valueProperty().addListener(listener);
	}

}
