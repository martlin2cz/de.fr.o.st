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
import cz.martlin.defrost.input.tools.ParserTools;
import cz.martlin.defrost.tasks.BaseCSVTasks.CSVExportTask;
import cz.martlin.defrost.tasks.BaseCSVTasks.CSVImportTask;
import cz.martlin.defrost.tasks.BaseLoadingIndicator;

public class ImportExportClassesWrapper {
	public static final File POSTS_FILE = new File("posts_infos.csv");
	public static final File COMMENTS_FILE = new File("posts_comments.csv");

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

	private ImportExportClassesWrapper() {
	}

	///////////////////////////////////////////////////////////////////////////////

	public static class CSVPostsExportTask extends CSVExportTask<PostInfo> {

		public CSVPostsExportTask(BaseLoadingIndicator indicator, List<PostInfo> items) {
			super("Exporting posts", "Exporting post", indicator, POSTS_FILE, items);
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

	public static class CSVPostsImportTask extends CSVImportTask<PostInfo> {

		public CSVPostsImportTask(BaseLoadingIndicator indicator) {
			super("Imporing posts", "Importing post", indicator, POSTS_FILE);
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

	///////////////////////////////////////////////////////////////////////////////

	public static class CSVCommentsExportTask extends CSVExportTask<Comment> {

		public CSVCommentsExportTask(BaseLoadingIndicator indicator, List<Comment> items) {
			super("Exporting comments", "Exporting comment", indicator, COMMENTS_FILE, items);
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

	public static class CSVCommentsImportTask extends CSVImportTask<Comment> {

		public CSVCommentsImportTask(BaseLoadingIndicator indicator) {
			super("Imporing comments", "Importing comment", indicator, COMMENTS_FILE);
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
