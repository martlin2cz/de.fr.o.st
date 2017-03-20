package cz.martlin.defrost.base;

import java.util.Calendar;

import org.htmlparser.Node;
import org.htmlparser.tags.Html;
import org.htmlparser.util.NodeList;

import cz.martlin.defrost.dataobj.User;
import cz.martlin.defrost.tools.NetworkingException;

public interface ForumDescriptorBase {
	
	public String inferPostTitle(Html document) throws NetworkingException;

	public NodeList inferCommentsElements(Html document) throws NetworkingException;

	public User inferCommentAuthor(Node coment) throws NetworkingException;

	public Calendar inferCommentDate(Node coment) throws NetworkingException;

	public String inferCommentContent(Node coment) throws NetworkingException;

}
