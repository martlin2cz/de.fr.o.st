package cz.martlin.defrost.gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import cz.martlin.defrost.base.BaseForumDescriptor;
import cz.martlin.defrost.dataobj.Comment;
import cz.martlin.defrost.dataobj.PostInfo;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.layout.GridPane;

public class MainController implements Initializable {

	// statusbar
	@FXML
	private Label statusBarLbl;
	@FXML
	private ProgressIndicator loadingIconPrind;
	@FXML
	private ProgressBar loadingPrgbr;

	// the top
	@FXML
	private Label discussInUseLbl;
	@FXML
	private Button stopButt;

	// the grid pane
	@FXML
	private GridPane mainGrid;
	// load buttons
	@FXML
	private Button loadPostsButt;
	@FXML
	private Button loadCommentsButt;
	// list views
	@FXML
	private ListView<String> categoriesLst;
	@FXML
	private ListView<PostInfo> postsLst;
	// labels
	@FXML
	private Label resultLbl;
	@FXML
	private Label categoriesTotalLbl;
	@FXML
	private Label postsTotalLbl;
	@FXML
	private Label commentsTotalLbl;

	private final ImplFasade impl;

	///////////////////////////////////////////////////////////////////////////////

	public MainController(BaseForumDescriptor descriptor) {
		super();
		this.impl = new ImplFasade(descriptor, this);
	}

	///////////////////////////////////////////////////////////////////////////////

	protected Label getStatusBarLbl() {
		return statusBarLbl;
	}

	protected ProgressIndicator getLoadingIconPrind() {
		return loadingIconPrind;
	}

	protected ProgressBar getLoadingPrgbr() {
		return loadingPrgbr;
	}

	protected GridPane getMainGrid() {
		return mainGrid;
	}

	protected Button getStopButt() {
		return stopButt;
	}

	///////////////////////////////////////////////////////////////////////////////

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initBindings();
	}

	private void initBindings() {
		ObservableValue<String> discussInUseProp = impl.getDiscussInUseDesc();
		discussInUseLbl.textProperty().bind(discussInUseProp);

		ObservableList<String> categories = impl.getCategories();
		categoriesLst.itemsProperty().bind(new SimpleListProperty<>(categories));
		categoriesLst.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		categoriesTotalLbl.textProperty().bind(Bindings.size(categories).asString());

		ObservableList<PostInfo> posts = impl.getPosts();
		postsLst.itemsProperty().bind(new SimpleListProperty<>(posts));
		postsLst.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		postsLst.setCellFactory(new PostsListViewCallback());
		postsTotalLbl.textProperty().bind(Bindings.size(posts).asString());

		ObservableList<Comment> comments = impl.getComments();
		commentsTotalLbl.textProperty().bind(Bindings.size(comments).asString());
	}

	///////////////////////////////////////////////////////////////////////////////

	///////////////////////////////////////////////////////////////////////////////

	@FXML
	private void loadPostsButtAction(ActionEvent event) {
		List<String> categories = categoriesLst.getSelectionModel().getSelectedItems();

		if (categories.isEmpty()) {
			error("Select at least one category", false);
		} else {
			impl.startLoadingPosts(categories);
		}
	}

	@FXML
	private void loadCommentsButtAction(ActionEvent event) {
		List<PostInfo> posts = postsLst.getSelectionModel().getSelectedItems();

		if (posts.isEmpty()) {
			error("Select at least one post", false);
		} else {
			impl.startLoadingComments(posts);
		}
	}

	@FXML
	private void stopButtAction(ActionEvent event) {
		impl.stopTaskInBackground();
	}

	///////////////////////////////////////////////////////////////////////////////

	@FXML
	private void exportPostsButtAction(ActionEvent event) {
		impl.startExportingPosts();
	}

	@FXML
	private void importPostsButtAction(ActionEvent event) {
		impl.startImportingPosts();
	}

	@FXML
	private void exportCommentsButtAction(ActionEvent event) {
		impl.startExportingComments();
	}

	@FXML
	private void importCommentsButtAction(ActionEvent event) {
		impl.startImportingComments();
	}

	///////////////////////////////////////////////////////////////////////////////

	@FXML
	private void outputByPostButtAction(ActionEvent event) {
		// TODO
	}

	@FXML
	private void outputByUserButtAction(ActionEvent event) {
		// TODO
	}

	@FXML
	private void outputUserXpostButtAction(ActionEvent event) {
		// TODO
	}

	///////////////////////////////////////////////////////////////////////////////

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
