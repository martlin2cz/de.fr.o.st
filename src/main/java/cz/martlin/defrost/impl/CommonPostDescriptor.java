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
import cz.martlin.defrost.dataobj.User;
import cz.martlin.defrost.tools.NetworkingException;
import cz.martlin.defrost.tools.ParserTools;

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

	public abstract Node inferPostContentElem(Html document) throws NetworkingException;

	@Override
	public String inferPostTitle(Html document) throws NetworkingException {
		Node content = inferPostContentElem(document);

		Node title = inferTitleElemFromPC(content);
		Node text = title.getFirstChild();
		return text.getText();
	}

	public abstract Node inferTitleElemFromPC(Node content) throws NetworkingException;

	@Override
	public NodeList inferCommentsElements(Html document) throws NetworkingException {
		Node content = inferPostContentElem(document);
		Node discuss = inferDiscussElemFromPC(content);
		NodeList children = discuss.getChildren();

		NodeList filtered = children.extractAllNodesThatMatch(commentsElemsFilter);

		return filtered;
	}

	public abstract Node inferDiscussElemFromPC(Node content) throws NetworkingException;

	public abstract boolean isCommentElem(Node node);

	@Override
	public User inferCommentAuthor(Node comment) throws NetworkingException {
		Node author = inferCommentAuthorElemFromC(comment);

		String name = inferNameFromCA(author);
		String id = inferIdFromCA(author);

		return new User(id, name);
	}

	public abstract Node inferCommentAuthorElemFromC(Node comment) throws NetworkingException;

	public abstract String inferNameFromCA(Node author) throws NetworkingException;

	public abstract String inferIdFromCA(Node author) throws NetworkingException;

	@Override
	public Calendar inferCommentDate(Node comment) throws NetworkingException {
		Node elem = inferDateElemFromC(comment);
		String text = tools.inferTextChild(elem);

		Date date;
		try {
			date = dateFormat.parse(text);
		} catch (ParseException e) {
			throw new NetworkingException("Date " + text + " cannot be parsed");
		}
		return tools.dateToCalendar(date);
	}

	public abstract Node inferDateElemFromC(Node comment) throws NetworkingException;

	private class IsCommentFilter implements NodeFilter {

		private static final long serialVersionUID = 7251877789819204590L;

		@Override
		public boolean accept(Node node) {
			return isCommentElem(node);
		}

	}

}