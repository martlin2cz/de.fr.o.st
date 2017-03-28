package cz.martlin.defrost.output.ie;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.csv.CSVRecord;

import cz.martlin.defrost.dataobj.Comment;
import cz.martlin.defrost.dataobj.PostIdentifier;
import cz.martlin.defrost.dataobj.PostInfo;
import cz.martlin.defrost.dataobj.User;
import cz.martlin.defrost.tasks.BaseLoadingIndicator;
import cz.martlin.defrost.tasks.BaseCSVTasks.CSVExportTask;
import cz.martlin.defrost.tasks.BaseCSVTasks.CSVImportTask;
import cz.martlin.defrost.utils.ParserTools;

/**
 * Wrapper class for {@link CSVCommentsExportTask} and
 * {@link CSVCommentsImportTask}. The comments are exported into/imported from
 * file {@link #COMMENTS_FILE}.
 * 
 * @author martin
 *
 */
public class CommentsImportExportWrapper {

	public static final File COMMENTS_FILE = new File("posts_comments.csv");

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

	private CommentsImportExportWrapper() {
	}

	/**
	 * Task performing exporting of comments into CSV file.
	 * 
	 * @author martin
	 *
	 */
	public static class CSVCommentsExportTask extends CSVExportTask<Comment> {

		public CSVCommentsExportTask(BaseLoadingIndicator indicator, List<Comment> items) {
			super(Messages.getString("Exporting_comments"), Messages.getString("Exporting_comment"), indicator, COMMENTS_FILE, items); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Override
		public Object[] exportItem(Comment item) {
			return new String[] { //
					item.getPost().getIdentifier().getCategory(), //
					item.getPost().getIdentifier().getId(), //
					item.getPost().getTitle(), //
					item.getAuthor().getId(), //
					item.getAuthor().getName(), //
					DATE_FORMAT.format(item.getDate().getTime()), //
					item.getContent() //
			};
		}

	}

	/**
	 * Task performing importing of comments from CSV file.
	 * 
	 * @author martin
	 *
	 */
	public static class CSVCommentsImportTask extends CSVImportTask<Comment> {

		public CSVCommentsImportTask(BaseLoadingIndicator indicator) {
			super(Messages.getString("Importing_comments"), Messages.getString("Importing_comment"), indicator, COMMENTS_FILE); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Override
		public Comment exctractItem(CSVRecord record) throws ParseException {
			String category = record.get(0);
			String postId = record.get(1);
			String title = record.get(2);
			String authorId = record.get(3);
			String authorName = record.get(4);
			Calendar date = ParserTools.dateToCalendar(DATE_FORMAT.parse(record.get(5)));
			String content = record.get(6);

			PostIdentifier identifier = new PostIdentifier(category, postId);
			PostInfo info = new PostInfo(title, identifier);
			User author = new User(authorId, authorName);
			Comment comment = new Comment(info, author, date, content);
			return comment;
		}
	}

}
