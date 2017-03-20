package cz.martlin.defrost.test;

import java.net.MalformedURLException;
import java.net.URL;

import org.htmlparser.tags.Html;

import cz.martlin.defrost.base.ForumDescriptorBase;
import cz.martlin.defrost.dataobj.Post;
import cz.martlin.defrost.impl.IDnesForumDesc;
import cz.martlin.defrost.impl.PostParser;
import cz.martlin.defrost.tools.Networker;
import cz.martlin.defrost.tools.NetworkingException;

public class _Testing {

	public static void main(String[] args) {
		
		System.out.println("Testing someting...");
		//testNetworker();
		testNovinkycz();
		
		System.out.println("Done.");
	}

	private static void testNovinkycz() {
		ForumDescriptorBase desc = new IDnesForumDesc();
		PostParser parser = new PostParser(desc);
		URL url = toURL("http://technet.idnes.cz/diskuse.aspx?iddiskuse=A170317_143422_veda_dvz");
		
		Post post;
		try {
			 post = parser.loadAndParse(url);
		} catch (NetworkingException e) {
			e.printStackTrace();
			return;
		}
		
		System.out.println(post);
		
	}

	/**
	 * 
	 */
	private static void testNetworker() {
		Networker networker = new Networker();
		
		URL url = toURL("https://github.com/martlin2cz");
		
		Html html;
		try {
			html = networker.query(url);
		} catch (NetworkingException e) {
			e.printStackTrace();
			return;
		}
		
		System.out.println(html.toHtml());
		System.out.println("----");
			}

	private static URL toURL(String path) {
		URL url;
		
		try {
			url = new URL(path);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
		
		return url;
	}

}
