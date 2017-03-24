package cz.martlin.defrost.output.ie;

import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import cz.martlin.defrost.dataobj.Comment;
import cz.martlin.defrost.dataobj.PostIdentifier;
import cz.martlin.defrost.dataobj.PostInfo;
import cz.martlin.defrost.dataobj.User;
import cz.martlin.defrost.input.tools.ParserTools;
import cz.martlin.defrost.misc.DefrostException;
import cz.martlin.defrost.misc.StatusReporter;

public class ImportExport {
	public static final File INFOS_FILE = new File("posts_infos.csv");
	public static final File CommentS_FILE = new File("posts_comments.csv");

	private static final CSVFormat FORMAT = CSVFormat.DEFAULT;
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

	private final StatusReporter reporter;

	public ImportExport(StatusReporter reporter) {
		this.reporter = reporter;
	}

	///////////////////////////////////////////////////////////////////////////////

	public void exportPosts(List<PostInfo> posts) {
		Writer fw = null;
		CSVPrinter printer = null;
		try {
			fw = new FileWriter(INFOS_FILE);
			printer = new CSVPrinter(fw, FORMAT);
			final CSVPrinter pr = printer;

			posts.forEach((post) -> {
				Object[] fields = postToCSVline(post);
				try {
					pr.printRecord(fields);
				} catch (Exception e) {
					DefrostException de = new DefrostException("Cannot export info", e);
					reporter.error(de);
				}
			});

			printer.flush();
		} catch (IOException e) {
			DefrostException de = new DefrostException("Cannot export infos", e);
			reporter.error(de);
		} finally {
			closeQuietly(fw);
			closeQuietly(printer);
		}
	}

	public void exportComments(List<Comment> Comments) {
		Writer fw = null;
		CSVPrinter printer = null;
		try {
			fw = new FileWriter(CommentS_FILE);
			printer = new CSVPrinter(fw, FORMAT);
			final CSVPrinter pr = printer;

			Comments.forEach((info) -> {
				Object[] fields = commentToCSVline(info);
				try {
					pr.printRecord(fields);
				} catch (Exception e) {
					DefrostException de = new DefrostException("Cannot export Comment", e);
					reporter.error(de);
				}
			});

			printer.flush();
		} catch (IOException e) {
			DefrostException de = new DefrostException("Cannot export Comments", e);
			reporter.error(de);
		} finally {
			closeQuietly(fw);
			closeQuietly(printer);
		}
	}

	public List<PostInfo> importPosts() {
		Reader fr = null;
		CSVParser parser = null;

		List<PostInfo> result = new ArrayList<>();
		try {
			fr = new FileReader(INFOS_FILE);
			parser = new CSVParser(fr, FORMAT);

			parser.forEach((record) -> {
				try {
					PostInfo info = csvRecordToPost(record);
					result.add(info);
				} catch (Exception e) {
					DefrostException de = new DefrostException("Cannot import info", e);
					reporter.error(de);
				}
			});
		} catch (IOException e) {
			DefrostException de = new DefrostException("Cannot import infos", e);
			reporter.error(de);
		} finally {
			closeQuietly(fr);
			closeQuietly(parser);
		}

		return result;
	}

	public List<Comment> importComments() {
		Reader fr = null;
		CSVParser parser = null;

		List<Comment> result = new ArrayList<>();
		try {
			fr = new FileReader(CommentS_FILE);
			parser = new CSVParser(fr, FORMAT);

			parser.forEach((record) -> {
				try {
					Comment Comment = csvRecordToComment(record);
					result.add(Comment);
				} catch (Exception e) {
					DefrostException de = new DefrostException("Cannot import Comment", e);
					reporter.error(de);
				}
			});
		} catch (IOException e) {
			DefrostException de = new DefrostException("Cannot import Comments", e);
			reporter.error(de);
		} finally {
			closeQuietly(fr);
			closeQuietly(parser);
		}

		return result;
	}

	///////////////////////////////////////////////////////////////////////////////

	private String[] postToCSVline(PostInfo info) {
		return new String[] { //
				info.getIdentifier().getCategory(), //
				info.getIdentifier().getId(), //
				info.getTitle() //
		};//
	}

	private PostInfo csvRecordToPost(CSVRecord record) {
		String category = record.get(0);
		String id = record.get(1);
		String title = record.get(2);

		PostIdentifier post = new PostIdentifier(category, id);
		return new PostInfo(title, post);
	}

	private String[] commentToCSVline(Comment comment) {
		return new String[] { //
				comment.getPost().getIdentifier().getCategory(), //
				comment.getPost().getIdentifier().getId(), //
				comment.getPost().getTitle(), //
				comment.getAuthor().getId(), //
				comment.getAuthor().getName(), //
				DATE_FORMAT.format(comment.getDate().getTime()), //
				comment.getContent() //
		};
	}

	private Comment csvRecordToComment(CSVRecord record) throws ParseException {
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

	///////////////////////////////////////////////////////////////////////////////

	private void closeQuietly(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
				DefrostException de = new DefrostException("Cannot close " + closeable, e);
				reporter.error(de);
			}
		}
	}

}
