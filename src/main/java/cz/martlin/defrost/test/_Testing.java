package cz.martlin.defrost.test;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.IdentityHashMap;
import java.util.List;

import org.htmlparser.tags.Html;

import cz.martlin.defrost.base.BaseForumDescriptor;
import cz.martlin.defrost.base.XXX_ForumDescriptorBase;
import cz.martlin.defrost.core.DefrostException;
import cz.martlin.defrost.core.Networker;
import cz.martlin.defrost.core.PostParser;
import cz.martlin.defrost.core.CategoryParser;
import cz.martlin.defrost.dataobj.Post;
import cz.martlin.defrost.dataobj.PostIdentifier;
import cz.martlin.defrost.dataobj.PostInfo;
import cz.martlin.defrost.impl.EmiminoDateFormat;
import cz.martlin.defrost.impl.EmiminoForumDesc;
import cz.martlin.defrost.impl.IDnesForumDesc;
import cz.martlin.defrost.impl.XXX_EmiminoForumDesc;
import cz.martlin.defrost.impl.XXX_IDnesForumDesc;

public class _Testing {

	public static void main(String[] args) {

		System.out.println("Testing someting...");
		// testNetworker();
		// testDateParsers();

		testEmimino();
		//testIdnes();
		
		System.out.println("Done.");
	}

	private static void testCategory(BaseForumDescriptor desc, String category1, String category2) {
		CategoryParser parser = new CategoryParser(desc);
		PrettyPrinter printer = new PrettyPrinter();

		try {
			System.out.println("Category " + category1 + " of " + desc + ":");
			List<PostInfo> infos1 = parser.listPosts(category1, 1);
			printer.printPostsInfos(infos1, System.out);
			System.out.println();

			System.out.println("Category " + category2 + " of " + desc + ":");
			List<PostInfo> infos2 = parser.listPosts(category2, 2);
			printer.printPostsInfos(infos2, System.out);
			System.out.println();

		} catch (DefrostException e) {
			e.printStackTrace();
		}
	}

	private static void testPosts(BaseForumDescriptor desc, PostIdentifier identifier1, PostIdentifier identifier2) {
		PostParser parser = new PostParser(desc);
		PrettyPrinter printer = new PrettyPrinter();

		try {
			System.out.println("Post " + identifier1 + " of " + desc + ":");
			Post post1 = parser.loadAndParse(identifier1, 1);
			printer.printPost(post1, System.out);
			System.out.println();

			System.out.println("Post " + identifier2 + " of " + desc + ":");
			Post post2 = parser.loadAndParse(identifier2, 1);
			printer.printPost(post2, System.out);
			System.out.println();

		} catch (DefrostException e) {
			e.printStackTrace();
		}
	}

	private static void testEmimino() {
		BaseForumDescriptor desc = new EmiminoForumDesc();

		String category1 = "tehotenstvi-porod";
		String category2 = "od-batolete-do-puberty";
		testCategory(desc, category1, category2);
		
		PostIdentifier identifier1 = new PostIdentifier("whatever", "dat-odklad-65roku-284416");
		PostIdentifier identifier2 = new PostIdentifier("whatever", "kamaradka-z-uherskeho-brodu-266104");
		testPosts(desc, identifier1, identifier2);

	}

	private static void testIdnes() {
		BaseForumDescriptor desc = new IDnesForumDesc();

		String category1 = "zpravy";
		String category2 = "zpravy";
		testCategory(desc, category1, category2);

		PostIdentifier identifier1 = new PostIdentifier("zpravy", "A170321_083811_domaci_fka");
		PostIdentifier identifier2 = new PostIdentifier("zpravy", "A170321_151510_domaci_kop");
		testPosts(desc, identifier1, identifier2);
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

	/*
	 * private static void testEmimino() { XXX_ForumDescriptorBase desc = new
	 * XXX_EmiminoForumDesc(); PostParser parser = new PostParser(desc);
	 * PrettyPrinter printer = new PrettyPrinter(); URL url =
	 * toURL("http://www.emimino.cz/clanky/hormonalni-antikoncepce-2016/"); //
	 * http://www.emimino.cz/denicky/trapeni-13148/ //
	 * http://www.emimino.cz/denicky/introvert-na-malomeste-13119/ //
	 * http://www.emimino.cz/denicky/me-hororove-tehotenstvi-12804/ //
	 * http://www.emimino.cz/denicky/vysledky-a-jak-jit-dal-13023/ //
	 * http://www.emimino.cz/souteze/vyrobte-si-zivy-domaci-jogurt/ //
	 * http://www.emimino.cz/clanky/pet-porci-ovoce-a-zeleniny-je-to-snadne/
	 * 
	 * Post post; try { post = parser.loadAndParse(url); } catch
	 * (DefrostException e) { e.printStackTrace(); return; }
	 * 
	 * // System.out.println(post); printer.printPost(post, System.out); }
	 * 
	 * private static void testEmiminos() { String[] urls = new String[] { //
	 * "http://www.emimino.cz/denicky/trapeni-13148/", //
	 * "http://www.emimino.cz/denicky/introvert-na-malomeste-13119/", //
	 * "http://www.emimino.cz/denicky/me-hororove-tehotenstvi-12804/", // };
	 * 
	 * XXX_ForumDescriptorBase desc = new XXX_EmiminoForumDesc(); PostParser
	 * parser = new PostParser(desc);
	 * 
	 * for (String url : urls) { URL theUrl = toURL(url);
	 * 
	 * try { Post post = parser.loadAndParse(theUrl);
	 * System.out.println(post.getTitle() + ", " + post.getComments().size() +
	 * " comments"); } catch (DefrostException e) { System.err.println(e); } } }
	 * 
	 * private static void testNovinkycz() { XXX_ForumDescriptorBase desc = new
	 * XXX_IDnesForumDesc(); PostParser parser = new PostParser(desc);
	 * PrettyPrinter printer = new PrettyPrinter(); URL url = toURL(
	 * "http://technet.idnes.cz/diskuse.aspx?iddiskuse=A170317_143422_veda_dvz")
	 * ;
	 * 
	 * Post post; try { post = parser.loadAndParse(url); } catch
	 * (DefrostException e) { e.printStackTrace(); return; }
	 * 
	 * // System.out.println(post); printer.printPost(post, System.out);
	 * 
	 * }
	 */
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
