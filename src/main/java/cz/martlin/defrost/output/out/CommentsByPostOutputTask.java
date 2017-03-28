package cz.martlin.defrost.output.out;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import cz.martlin.defrost.dataobj.Comment;
import cz.martlin.defrost.dataobj.PostIdentifier;
import cz.martlin.defrost.dataobj.PostInfo;
import cz.martlin.defrost.tasks.BaseCSVTasks.CSVExportTask;
import cz.martlin.defrost.tasks.BaseLoadingIndicator;

/**
 * Task performing outputing of comments groupped by posts.
 * 
 * @author martin
 *
 */
public class CommentsByPostOutputTask extends CSVExportTask<Comment> {
	private static final File FILE = new File("comments_by_post.csv");

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yy hh:mm:ss");
	private static final PostInfo EMPTY_INFO = new PostInfo("", new PostIdentifier("", ""));

	private PostInfo currentPost;

	public CommentsByPostOutputTask(BaseLoadingIndicator indicator, List<Comment> comments) {
		super(Messages.getString("Outputting_comments"), Messages.getString("Outputting_comment"), indicator, FILE, comments); //$NON-NLS-1$ //$NON-NLS-2$

		this.currentPost = null;
	}

	@Override
	protected void preprocessItems(List<Comment> comments) {
		comments.sort((c1, c2) -> c1.getPost().compareTo(c2.getPost()));
	}

	@Override
	public Object[] exportItem(Comment comment) {
		PostInfo post;
		if (comment.getPost().equals(currentPost)) {
			post = EMPTY_INFO;
		} else {
			currentPost = comment.getPost();
			post = comment.getPost();
		}

		return new String[] { //
				post.getIdentifier().getCategory(), //
				post.getIdentifier().getId(), //
				comment.getAuthor().getId(), //
				DATE_FORMAT.format(comment.getDate().getTime()), //
				comment.getContent() //
		};
	}
}