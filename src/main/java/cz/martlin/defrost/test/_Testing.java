package cz.martlin.defrost.test;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.htmlparser.tags.Html;

import cz.martlin.defrost.base.ForumDescriptorBase;
import cz.martlin.defrost.core.DefrostException;
import cz.martlin.defrost.core.Networker;
import cz.martlin.defrost.core.PostParser;
import cz.martlin.defrost.dataobj.Post;
import cz.martlin.defrost.impl.IDnesForumDesc;

public class _Testing {

	public static void main(String[] args) {

		System.out.println("Testing someting...");
		// testNetworker();
		testDateParsers();
		testNovinkycz();

		System.out.println("Done.");
	}

	private static void testDateParsers() {
		SimpleDateFormat sdf1 = new SimpleDateFormat("d.M.yyyy h:mm");
		String str1 = "20.3.2017 7:07";

		Date d1;
		try {
			d1 = sdf1.parse(str1);

		} catch (ParseException e) {
			e.printStackTrace();
			return;
		}
		System.out.println(d1);

	}

	private static void testNovinkycz() {
		ForumDescriptorBase desc = new IDnesForumDesc();
		PostParser parser = new PostParser(desc);
		PostPrettyPrinter printer = new PostPrettyPrinter();
		URL url = toURL("http://technet.idnes.cz/diskuse.aspx?iddiskuse=A170317_143422_veda_dvz");

		Post post;
		try {
			post = parser.loadAndParse(url);
		} catch (DefrostException e) {
			e.printStackTrace();
			return;
		}

		// System.out.println(post);
		printer.print(post, System.out);

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
		} catch (DefrostException e) {
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
