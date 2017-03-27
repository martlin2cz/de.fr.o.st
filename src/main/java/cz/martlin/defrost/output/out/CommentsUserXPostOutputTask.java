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

public class CommentsUserXPostOutputTask extends CSVExportTask<Entry<User, Map<PostInfo, Set<Comment>>>> {
	private static final File FILE = new File("comments_user_x_post.csv");

	private static final Entry<User, Map<PostInfo, Set<Comment>>> EMPTY_ENTRY = //
			new AbstractMap.SimpleEntry<>(null, null);

	private final List<PostInfo> columns;

	public CommentsUserXPostOutputTask(BaseLoadingIndicator indicator, List<Comment> comments) {
		super("Outputting comments", "outputting row of user", indicator, FILE, convert(comments));

		columns = inferPosts(comments);
	}

	public static List<PostInfo> inferPosts(List<Comment> comments) {
		Set<PostInfo> set = new HashSet<>();

		comments.forEach((c) -> set.add(c.getPost()));

		return new ArrayList<>(set);
	}

	private static List<Entry<User, Map<PostInfo, Set<Comment>>>> convert(List<Comment> comments) {
		Map<User, Map<PostInfo, Set<Comment>>> map = convertToMap(comments);
		return new ArrayList<>(map.entrySet());
	}

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
			return headerLine();
		} else {
			User user = item.getKey();
			Map<PostInfo, Set<Comment>> ofPosts = item.getValue();

			return putUserLine(user, ofPosts);
		}

	}

	private Object[] headerLine() {
		List<String> line = new ArrayList<>(columns.size() + 1);

		line.add("");

		for (PostInfo column : columns) {
			line.add(column.getIdentifier().getId());
		}

		return line.toArray();

	}

	private Object[] putUserLine(User user, Map<PostInfo, Set<Comment>> ofPosts) {
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
