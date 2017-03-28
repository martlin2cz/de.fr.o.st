package cz.martlin.defrost.output.ie;

import java.io.File;
import java.util.List;

import org.apache.commons.csv.CSVRecord;

import cz.martlin.defrost.dataobj.PostIdentifier;
import cz.martlin.defrost.dataobj.PostInfo;
import cz.martlin.defrost.tasks.BaseCSVTasks.CSVExportTask;
import cz.martlin.defrost.tasks.BaseCSVTasks.CSVImportTask;
import cz.martlin.defrost.tasks.BaseLoadingIndicator;

/**
 * Wrapper class for {@link CSVPostsExportTask} and {@link CSVPostsImportTask}.
 * Posts are exported into/imported from file {@link #POSTS_FILE}.
 * 
 * @author martin
 *
 */
public class PostsImportExportWrapper {

	public static final File POSTS_FILE = new File("posts_infos.csv");

	private PostsImportExportWrapper() {
	}

	/**
	 * Task performing exporting of posts into CSV file.
	 * 
	 * @author martin
	 *
	 */
	public static class CSVPostsExportTask extends CSVExportTask<PostInfo> {

		public CSVPostsExportTask(BaseLoadingIndicator indicator, List<PostInfo> items) {
			super(Messages.getString("Exporting_posts"), Messages.getString("Exporting_post"), indicator, POSTS_FILE, items); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Override
		public Object[] exportItem(PostInfo item) {
			return new String[] { //
					item.getIdentifier().getCategory(), //
					item.getIdentifier().getId(), //
					item.getTitle() //
			};//
		}

	}

	/**
	 * Task performing importing of posts from CSV file.
	 * 
	 * @author martin
	 *
	 */
	public static class CSVPostsImportTask extends CSVImportTask<PostInfo> {

		public CSVPostsImportTask(BaseLoadingIndicator indicator) {
			super(Messages.getString("Importing_posts"), Messages.getString("Importing_post"), indicator, POSTS_FILE); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Override
		public PostInfo exctractItem(CSVRecord record) {
			String category = record.get(0);
			String id = record.get(1);
			String title = record.get(2);

			PostIdentifier post = new PostIdentifier(category, id);
			return new PostInfo(title, post);
		}
	}

}
