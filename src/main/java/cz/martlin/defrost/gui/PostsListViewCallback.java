package cz.martlin.defrost.gui;

import cz.martlin.defrost.dataobj.PostInfo;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.util.Callback;

/**
 * View callback creating {@link ListCell}s of {@link PostInfo}.
 * 
 * @author martin
 *
 */
public class PostsListViewCallback implements Callback<ListView<PostInfo>, ListCell<PostInfo>> {

	/**
	 * {@link ListCell} for {@link PostInfo}. Displays info's title as text and
	 * additional info as tooltip.
	 * 
	 * @author martin
	 *
	 */
	public class PostsListViewCell extends ListCell<PostInfo> {
		@Override
		protected void updateItem(PostInfo item, boolean empty) {
			super.updateItem(item, empty);

			if (item != null) {
				this.setText(item.getTitle());

				String tooltip = //
						"Category: " + item.getIdentifier().getCategory() + ", " //
								+ "id: " + item.getIdentifier().getId(); //

				this.setTooltip(new Tooltip(tooltip));
			}
		}
	}

	@Override
	public ListCell<PostInfo> call(ListView<PostInfo> param) {
		return new PostsListViewCell();
	}

}
