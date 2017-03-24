package cz.martlin.defrost.test;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import cz.martlin.defrost.dataobj.Comment;
import cz.martlin.defrost.dataobj.PostInfo;

public class PrettyPrinter {
	private final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

	public PrettyPrinter() {
	}

	public void printComment(List<Comment> comments, PrintStream out) {
		if (comments.isEmpty()) {
			System.out.println("No comments");
		} else {
			Comment some = comments.get(0);

			out.print(some.getPost().getTitle());
			out.print(" (");
			out.print(some.getPost().getIdentifier().getCategory());
			out.print(" / ");
			out.print(some.getPost().getIdentifier().getId());
			out.print("), ");
			out.print(comments.size() + " comments");
			out.print(":");
			out.println();

			for (Comment comment : comments) {

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
				out.println("\n");
			}
		}
	}

	public void printCommentsInfos(List<PostInfo> infos, PrintStream out) {
		for (PostInfo info : infos) {
			out.print(info.getIdentifier().getCategory());
			out.print(" / ");
			out.print(info.getIdentifier().getId());
			out.print(": ");
			out.print(info.getTitle());
			out.println();
		}
	}
}
