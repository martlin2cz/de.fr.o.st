package cz.martlin.defrost.base;

import java.net.URL;
import java.util.Calendar;

import org.htmlparser.Node;
import org.htmlparser.tags.Html;
import org.htmlparser.util.NodeList;

import cz.martlin.defrost.dataobj.PostIdentifier;
import cz.martlin.defrost.dataobj.PostInfo;
import cz.martlin.defrost.dataobj.User;

public interface BaseForumDescriptor {

	public URL urlOfCategory(String categoryID, int page) throws Exception;

	public NodeList findPostItems(Html document) throws Exception;

	public PostInfo postItemToPostInfo(Node postItem, String category) throws Exception;
	
	public boolean hasCategoryNextPage(Html document) throws Exception;


	///////////////////////////////////////////////////////////////////////////

	public URL urlOfPost(PostIdentifier identifier, int page) throws Exception;

	public PostIdentifier identifierOfPost(URL url, String category) throws Exception;
	
	public boolean hasPostNextPage(Html document) throws Exception;

	///////////////////////////////////////////////////////////////////////////

	public PostInfo findPostInfoInPost(PostIdentifier identifier, Html document) throws Exception;

	public NodeList findPostDiscussElements(Html document) throws Exception;

	///////////////////////////////////////////////////////////////////////////

	public User findCommentAuthor(Node comment) throws Exception;

	public Calendar findCommentDate(Node comment) throws Exception;

	public String findCommentContent(Node comment) throws Exception;

	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////

}
