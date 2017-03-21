package cz.martlin.defrost.test;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import cz.martlin.defrost.dataobj.Comment;
import cz.martlin.defrost.dataobj.Post;

public class PostPrettyPrinter {
	private final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

	public PostPrettyPrinter() {
	}

	public void print(Post post, PrintStream out) {
		out.print(post.getTitle());
		out.print(" (");
		out.print(post.getUrl().toExternalForm());
		out.print("):");
		out.println();

		for (Comment comment : post.getComments()) {

			out.print(comment.getAuthor().getId());
			out.print(" (");
			out.print(comment.getAuthor().getName());
			out.print("), ");
			if (comment.getDate() != null) {
				out.print(dateFormat.format(comment.getDate().getTime()));
			} else {
				out.print("???");
			}

			out.print(":");
			out.println();

			out.print(comment.getContent());
			out.println();
		}
	}
}
