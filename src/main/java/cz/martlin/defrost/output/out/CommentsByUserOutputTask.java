package cz.martlin.defrost.output.out;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import cz.martlin.defrost.dataobj.Comment;
import cz.martlin.defrost.dataobj.User;
import cz.martlin.defrost.tasks.BaseCSVTasks.CSVExportTask;
import cz.martlin.defrost.tasks.BaseLoadingIndicator;

/**
 * Task performing outputing of comments groupped by users.
 * 
 * @author martin
 *
 */
public class CommentsByUserOutputTask extends CSVExportTask<Comment> {
	private static final File FILE = new File("comments_by_user.csv");

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yy hh:mm:ss");
	private static final User EMPTY_USER = new User("", "");

	private User currentUser;

	public CommentsByUserOutputTask(BaseLoadingIndicator indicator, List<Comment> comments) {
		super("Outputting comments", "Outputting comment", indicator, FILE, comments);

		this.currentUser = null;
	}

	@Override
	protected void preprocessItems(List<Comment> comments) {
		comments.sort((c1, c2) -> c1.getAuthor().compareTo(c2.getAuthor()));
	}

	@Override
	public Object[] exportItem(Comment comment) {
		User user;
		if (comment.getAuthor().equals(currentUser)) {
			user = EMPTY_USER;
		} else {
			currentUser = comment.getAuthor();
			user = comment.getAuthor();
		}

		return new String[] { //
				user.getId(), //
				comment.getPost().getIdentifier().getCategory(), //
				comment.getPost().getIdentifier().getId(), //
				DATE_FORMAT.format(comment.getDate().getTime()), //
				comment.getContent() //
		};
	}
}