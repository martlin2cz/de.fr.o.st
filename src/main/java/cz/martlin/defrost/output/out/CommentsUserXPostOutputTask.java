package cz.martlin.defrost.output.out;

import java.io.File;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cz.martlin.defrost.dataobj.Comment;
import cz.martlin.defrost.dataobj.PostInfo;
import cz.martlin.defrost.dataobj.User;
import cz.martlin.defrost.tasks.BaseCSVTasks.CSVExportTask;
import cz.martlin.defrost.tasks.BaseLoadingIndicator;
import cz.martlin.defrost.utils.Msg;

/**
 * Task performing outputing counts of comments in table users x posts.
 * 
 * @author martin
 *
 */
public class CommentsUserXPostOutputTask extends CSVExportTask<Entry<User, Map<PostInfo, Set<Comment>>>> {
	private static final File FILE = new File("comments_user_x_post.csv");

	private static final Entry<User, Map<PostInfo, Set<Comment>>> EMPTY_ENTRY = //
			new AbstractMap.SimpleEntry<>(null, null);

	private final List<PostInfo> columns;

	public CommentsUserXPostOutputTask(BaseLoadingIndicator indicator, List<Comment> comments) {
		super(Msg.getString("Outputting_comments"), Msg.getString("outputting_row_of_user"), indicator, FILE, convert(comments)); //$NON-NLS-1$ //$NON-NLS-2$

		columns = inferPosts(comments);
	}

	/**
	 * For given list of comments infers set of posts given comments occurs in.
	 * 
	 * @param comments
	 * @return
	 */
	public static List<PostInfo> inferPosts(List<Comment> comments) {
		Set<PostInfo> set = new HashSet<>();

		comments.forEach((c) -> set.add(c.getPost()));

		return new ArrayList<>(set);
	}

	/**
	 * Returns table of comments as list of entries.
	 * 
	 * @param comments
	 * @return
	 */
	private static List<Entry<User, Map<PostInfo, Set<Comment>>>> convert(List<Comment> comments) {
		Map<User, Map<PostInfo, Set<Comment>>> map = convertToMap(comments);
		return new ArrayList<>(map.entrySet());
	}

	/**
	 * For given list of comments returns table of users' posts' comments.
	 * 
	 * @param comments
	 * @return
	 */
	public static Map<User, Map<PostInfo, Set<Comment>>> convertToMap(List<Comment> comments) {
		Map<User, Map<PostInfo, Set<Comment>>> result = new HashMap<>();

		for (Comment comment : comments) {
			User user = comment.getAuthor();
			PostInfo post = comment.getPost();

			Map<PostInfo, Set<Comment>> ofUser = result.get(user);
			if (ofUser == null) {
				ofUser = new HashMap<>();
				result.put(user, ofUser);
			}

			Set<Comment> ofUserOfPost = ofUser.get(post);
			if (ofUserOfPost == null) {
				ofUserOfPost = new HashSet<>();
				ofUser.put(post, ofUserOfPost);

			}

			ofUserOfPost.add(comment);
		}

		return result;
	}

	@Override
	protected void preprocessItems(List<Entry<User, Map<PostInfo, Set<Comment>>>> items) {
		items.add(0, EMPTY_ENTRY);
	}

	@Override
	public Object[] exportItem(Entry<User, Map<PostInfo, Set<Comment>>> item) {
		if (EMPTY_ENTRY.equals(item)) {
			return headerRow();
		} else {
			User user = item.getKey();
			Map<PostInfo, Set<Comment>> ofPosts = item.getValue();

			return userRow(user, ofPosts);
		}

	}

	/**
	 * Creates the header row (lists posts' ids).
	 * 
	 * @return
	 */
	private Object[] headerRow() {
		List<String> line = new ArrayList<>(columns.size() + 1);

		line.add("");

		for (PostInfo column : columns) {
			line.add(column.getIdentifier().getId());
		}

		return line.toArray();

	}

	/**
	 * Creates the user row (counts of comments for each post)
	 * 
	 * @param user
	 * @param ofPosts
	 * @return
	 */
	private Object[] userRow(User user, Map<PostInfo, Set<Comment>> ofPosts) {
		List<String> line = new ArrayList<>(columns.size() + 1);

		line.add(user.getId());

		for (PostInfo column : columns) {
			Set<Comment> ofPost = ofPosts.get(column);
			if (ofPost != null) {
				String count = Integer.toString(ofPost.size());
				line.add(count);
			} else {
				line.add("");
			}
		}

		return line.toArray();
	}

}
