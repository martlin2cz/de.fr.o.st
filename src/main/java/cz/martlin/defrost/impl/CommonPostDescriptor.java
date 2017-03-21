package cz.martlin.defrost.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.tags.Html;
import org.htmlparser.util.NodeList;

import cz.martlin.defrost.base.ForumDescriptorBase;
import cz.martlin.defrost.core.DefrostException;
import cz.martlin.defrost.core.ParserTools;
import cz.martlin.defrost.dataobj.User;

/**
 * Implements basic forum descripor with some common spefications.
 * 
 * @author martin
 *
 */
public abstract class CommonPostDescriptor implements ForumDescriptorBase {

	protected final ParserTools tools;
	private final DateFormat dateFormat;
	private final NodeFilter commentsElemsFilter;

	public CommonPostDescriptor(DateFormat dateFormat) {
		super();
		this.tools = new ParserTools();
		this.dateFormat = dateFormat;
		this.commentsElemsFilter = new IsCommentFilter();
	}

	/**
	 * From the whole document infers the element of post content (i.e. main
	 * bar, with title, text and discuss).
	 * 
	 * @param document
	 * @return
	 * @throws DefrostException
	 */
	public abstract Node inferPostContentElem(Html document) throws DefrostException;

	@Override
	public String inferPostTitle(Html document) throws DefrostException {
		Node content = inferPostContentElem(document);

		Node title = inferTitleElemFromPC(content);
		Node text = title.getFirstChild();
		return text.getText().trim();
	}

	/**
	 * From the post content element infers the element owning the post title.
	 * 
	 * @param content
	 * @return
	 * @throws DefrostException
	 */
	public abstract Node inferTitleElemFromPC(Node content) throws DefrostException;

	@Override
	public NodeList inferCommentsElements(Html document) throws DefrostException {
		Node content = inferPostContentElem(document);
		Node discuss = inferDiscussElemFromPC(content);
		NodeList children = discuss.getChildren();

		NodeList filtered = children.extractAllNodesThatMatch(commentsElemsFilter);

		return filtered;
	}

	/**
	 * From the post content element infers the element owning the discuss.
	 * 
	 * @param content
	 * @return
	 * @throws DefrostException
	 */
	public abstract Node inferDiscussElemFromPC(Node content) throws DefrostException;

	/**
	 * Checks whether is given node (child of
	 * {@link #inferDiscussElemFromPC(Node)}) is the comment element or not
	 * (i.e. advertisment or so).
	 * 
	 * @param node
	 * @return
	 */
	public abstract boolean isCommentElem(Node node);

	@Override
	public User inferCommentAuthor(Node comment) throws DefrostException {
		Node author = inferCommentAuthorElemFromC(comment);

		String name = inferNameFromCA(author);
		String id = inferIdFromCA(author);

		return new User(id, name);
	}

	/**
	 * From given comment element infers element containing the author.
	 * 
	 * @param comment
	 * @return
	 * @throws DefrostException
	 */
	public abstract Node inferCommentAuthorElemFromC(Node comment) throws DefrostException;

	/**
	 * From given author element infers the author's name.
	 * 
	 * @param author
	 * @return
	 * @throws DefrostException
	 */
	public abstract String inferNameFromCA(Node author) throws DefrostException;

	/**
	 * From given author element infers the author's id.
	 * 
	 * @param author
	 * @return
	 * @throws DefrostException
	 */
	public abstract String inferIdFromCA(Node author) throws DefrostException;

	@Override
	public Calendar inferCommentDate(Node comment) throws DefrostException {
		Node elem = inferDateElemFromC(comment);
		String text = tools.inferTextChild(elem);
		String trimmed = text.trim();

		Date date;
		try {
			date = dateFormat.parse(trimmed);
		} catch (ParseException e) {
			throw new DefrostException("Date " + trimmed + " cannot be parsed");
		}
		return tools.dateToCalendar(date);
	}

	/**
	 * From given comment element infers element owning the comment date string.
	 * 
	 * @param comment
	 * @return
	 * @throws DefrostException
	 */
	public abstract Node inferDateElemFromC(Node comment) throws DefrostException;

	@Override
	public String inferCommentContent(Node coment) throws DefrostException {
		Node node = inferCommentElementFromC(coment);

		String content = node.getChildren().toHtml();
		return content.trim();
	}

	/**
	 * From given comment element infers element owning the comment content
	 * string.
	 * 
	 * @param coment
	 * @return
	 * @throws DefrostException
	 */
	public abstract Node inferCommentElementFromC(Node coment) throws DefrostException;

	/**
	 * Class implementing filtering of nodes using the
	 * {@link CommonPostDescriptor#isCommentElem(Node)} method.
	 * 
	 * @author martin
	 *
	 */
	private class IsCommentFilter implements NodeFilter {

		private static final long serialVersionUID = 7251877789819204590L;

		@Override
		public boolean accept(Node node) {
			return isCommentElem(node);
		}

	}

}