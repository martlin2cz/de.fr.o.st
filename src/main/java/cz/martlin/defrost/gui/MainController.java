package cz.martlin.defrost.gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import cz.martlin.defrost.base.BaseForumDescriptor;
import cz.martlin.defrost.dataobj.Comment;
import cz.martlin.defrost.dataobj.PostInfo;
import cz.martlin.defrost.input.threading.LoaderInThread;
import cz.martlin.defrost.misc.StatusReporter;
import cz.martlin.defrost.output.ie.ImportExport;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SelectionMode;

public class MainController implements Initializable {

	@FXML
	private Label statusBarLbl;

	@FXML
	private ProgressIndicator loadingIconPrind;

	@FXML
	private ProgressBar loadingPrgbr;

	@FXML
	private Label discussInUseLbl;
	@FXML
	private Button stopButt;
	@FXML
	private Button loadPostsButt;
	@FXML
	private Button loadCommentsButt;
	@FXML
	private ListView<String> categoriesLst;
	@FXML
	private ListView<PostInfo> postsLst;
	@FXML
	private Label resultLbl;

	@FXML
	private Label categoriesTotalLbl;
	@FXML
	private Label postsTotalLbl;
	@FXML
	private Label commentsTotalLbl;

	private BaseForumDescriptor descriptor;
	private LoaderInThread loader;
	private ImportExport ie;

	public MainController() {
	}

	///////////////////////////////////////////////////////////////////////////////
	public void setDescriptor(BaseForumDescriptor descriptor) {
		this.descriptor = descriptor;

		// StatusReporter reporter = new LoggingReporter();
		StatusReporter reporter = new GuiReporter(this);

		this.loader = new LoaderInThread(descriptor, reporter);
		this.ie = new ImportExport(reporter);
		reinitialize();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}

	private void reinitialize() {
		initializeLabels();

		initializeCategories();
		loadingFinished();
		updateTotals();
		setLoadingStopped("Ready.");
	}
	///////////////////////////////////////////////////////////////////////////////

	private void initializeLabels() {
		String discussInUse = descriptor.getClass().getSimpleName();
		discussInUseLbl.setText(discussInUse);

		statusBarLbl.setText("Ready!");
	}

	private void initializeCategories() {
		String[] categories = descriptor.listAvaibleCategories();

		ObservableList<String> items = FXCollections.observableArrayList(categories);
		categoriesLst.setItems(items);
		categoriesLst.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		String text = "total: " + categories.length;
		categoriesTotalLbl.setText(text);
	}

	private void initializePosts() {
		List<PostInfo> posts = loader.getLoadedPosts();

		ObservableList<PostInfo> items = FXCollections.observableList(posts);
		postsLst.setItems(items);
		postsLst.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		postsLst.setCellFactory(new PostsListViewCallback());

		String text = "total: " + posts.size();
		postsTotalLbl.setText(text);
	}

	private void initializeComments() {
		// TODO label.setText("totla: " ....) ?

	}

	///////////////////////////////////////////////////////////////////////////////

	public void setEditable(boolean editable) {
		stopButt.setDisable(editable);
		loadPostsButt.setDisable(!editable);
		loadCommentsButt.setDisable(!editable);

		categoriesLst.setDisable(!editable);
		categoriesTotalLbl.setDisable(!editable);

		postsLst.setDisable(!editable);
		postsTotalLbl.setDisable(!editable);
		
		//postsLst.setDisable(!editable);
		commentsTotalLbl.setDisable(!editable);
		
		
	}

	///////////////////////////////////////////////////////////////////////////////

	public void setLoadingStarted(String message) {
		Platform.runLater(() -> {
			statusBarLbl.setText(message);

			loadingIconPrind.setVisible(true);
			loadingPrgbr.setVisible(true);

			loadingPrgbr.setProgress(0.0);
		});
	}

	public void setLoadingProgress(String message, double progress) {
		Platform.runLater(() -> {

			statusBarLbl.setText(message);
			loadingPrgbr.setProgress(progress);
		});
	}

	public void setLoadingStopped(String message) {
		Platform.runLater(() -> {

			statusBarLbl.setText(message);

			loadingIconPrind.setVisible(false);
			loadingPrgbr.setVisible(false);
		});
	}

	///////////////////////////////////////////////////////////////////////////////

	@FXML
	private void loadPostsButtAction(ActionEvent event) {
		loadPostsButt.setDisable(true);

		List<String> categories = categoriesLst.getSelectionModel().getSelectedItems();
		if (categories.isEmpty()) {
			error("Select at least one category", false);
			loadPostsButt.setDisable(false);
			return;
		}
		loader.startLoadingPosts(categories);

		setEditable(false);
	}

	@FXML
	private void loadCommentsButtAction(ActionEvent event) {
		loadCommentsButt.setDisable(true);

		List<PostInfo> infos = postsLst.getSelectionModel().getSelectedItems();
		if (infos.isEmpty()) {
			error("Select at least one post", false);
			loadCommentsButt.setDisable(false);
			return;
		}
		loader.startLoadingComments(infos);

		setEditable(false);
	}

	@FXML
	private void stopButtAction(ActionEvent event) {
		stopButt.setDisable(true);
		stopButt.setText("Stopping...");

		loader.stopLoading();
		loadingFinished();
		stopButt.setText("Stop");
	}

	///////////////////////////////////////////////////////////////////////////////

	@FXML
	private void exportInfosButtAction(ActionEvent event) {
		setLoadingStarted("Exporting");
		List<PostInfo> infos = loader.getLoadedPosts();
		ie.exportPosts(infos);
		setLoadingStopped("Exported");
	}

	@FXML
	private void importInfosButtAction(ActionEvent event) {
		setLoadingStarted("Importing");
		List<PostInfo> infos = ie.importPosts();
		loader.setLoadedPosts(infos);
		setLoadingStopped("Imported");
		initializePosts();
	}

	@FXML
	private void exportCommentsButtAction(ActionEvent event) {
		setLoadingStarted("Exporting");
		List<Comment> Comments = loader.getLoadedComments();
		ie.exportComments(Comments);
		setLoadingStopped("Exported");
	}

	@FXML
	private void importCommentsButtAction(ActionEvent event) {
		setLoadingStarted("Importing");
		List<Comment> comments = ie.importComments();
		loader.setLoadedComments(comments);
		setLoadingStopped("Imported");
		initializeComments();
	}

	///////////////////////////////////////////////////////////////////////////////

	// TODO output by user, output by ...

	///////////////////////////////////////////////////////////////////////////////

	protected void setStatus(final String status) {
		Platform.runLater(() -> {
			statusBarLbl.setText(status);
		});
	}

	protected void updateTotals() {
		Platform.runLater(() -> {
			this.postsTotalLbl.setText("total: " + loader.getLoadedPostsCount());
			this.commentsTotalLbl.setText("total: " + loader.getLoadedCommentsCount());
		});
	}

	protected void loadingFinished() {
		Platform.runLater(() -> {
			initializePosts();
			initializeComments();
			updateTotals();	//this is called inside of another task, problem?

			setEditable(true);
		});
	}

	public void error(String text, boolean isFatal) {
		Platform.runLater(() -> {
			AlertType type;
			if (isFatal) {
				type = AlertType.ERROR;
			} else {
				type = AlertType.WARNING;
			}

			Alert alert = new Alert(type);
			alert.setHeaderText("Error");
			alert.setContentText(text);
			alert.show();
		});

		if (isFatal) {
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (Exception e) {
				// ignore
			}
		}
	}

}
