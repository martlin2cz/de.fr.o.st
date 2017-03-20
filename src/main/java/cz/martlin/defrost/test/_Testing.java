package cz.martlin.defrost.test;

import java.net.MalformedURLException;
import java.net.URL;

import org.htmlparser.tags.Html;

import cz.martlin.defrost.tools.Networker;
import cz.martlin.defrost.tools.NetworkingException;

public class _Testing {

	public static void main(String[] args) {
		
		System.out.println("Testing someting...");
		testNetworker();
		
		
		System.out.println("Done.");
	}

	/**
	 * 
	 */
	private static void testNetworker() {
		Networker networker = new Networker();
		
		URL url;
		try {
			url = new URL("https://github.com/martlin2cz");
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return;
		}
		
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

}
