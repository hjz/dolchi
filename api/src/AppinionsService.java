

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



public class AppinionsService {

	public static String GROUP_API_KEY = "rdv36eev64tga52ceg4664dd";
	public static String OPINION_API_KEY = "eafb6qq7csys88tky87cegw9";
	
	private static List<GroupResult> getGroupResults(String url) throws ClientProtocolException, IOException, SAXException, ParserConfigurationException {
		Document doc = HTTPHelper.getXML(url);
		List<Element> values = XMLHelper.getElementsByTag(doc, "value");
		List<GroupResult> results = new ArrayList<GroupResult>();
		for(Element value : values) {
			results.add(new GroupResult(value.getAttribute("name"), value.getAttribute("id")));
		}
		return results;
	}
	
	public static List<GroupResult> getTopics(String opHolderId, int numResults) throws ClientProtocolException, IOException, SAXException, ParserConfigurationException {
		return getGroupResults(getBaseGroupsURL() + "&opholder_id=" + opHolderId + "&group=topic_id&group_rows=" + String.valueOf(numResults));
	}
	
	public static List<GroupResult> getOpinionHolders(String query, int numResults) throws ClientProtocolException, IOException, SAXException, ParserConfigurationException {
		return getGroupResults(getBaseGroupsURL() + "&sent=" + query + "&group=opholder_id&group_rows=" + String.valueOf(numResults));
	}
	
	private static List<OpinionResult> getOpinionResults(String url) throws ClientProtocolException, IOException, SAXException, ParserConfigurationException {
		Document doc = HTTPHelper.getXML(url);
		List<Element> opinions = XMLHelper.getElementsByTag(doc, "opinion");
		List<OpinionResult> results = new ArrayList<OpinionResult>();
		
		for(Element op : opinions) {
		    // authors
			List<Element> authorEls = XMLHelper.getElementsByTag(op, "authors");
			List<String> authors = new ArrayList<String>();
		    for(Element authorEl : authorEls) {
		    	authors.add(XMLHelper.getTagValue("author", authorEl));
		    }
		    
			System.out.println(XMLHelper.getTagValue("pre_sent", op));

		    
		    results.add(new OpinionResult(XMLHelper.getTagValue("pre_sent", op),
	    		  						  XMLHelper.getTagValue("sent", op),
	    		  						  XMLHelper.getTagValue("post_sent", op),
	    		  						  XMLHelper.getTagValue("doc_link", op),
	    		  						  XMLHelper.getTagValue("doc_title", op),
	    		  						  XMLHelper.getTagValue("publisher", op),
	    		  						  XMLHelper.getTagValue("type", op),
	    		  						  authors));
		}
		
		return results;
	}
	
	public static List<OpinionResult> getOpinions(String query, String opHolderId, int numResults) throws ClientProtocolException, IOException, SAXException, ParserConfigurationException {
		String url = getBaseOpinionsURL() + "&sent=" + query + "&rows=" + String.valueOf(numResults);
		if(opHolderId != "")
			url += "&opholder_id=" + opHolderId;
		System.out.println(url);
		return getOpinionResults(url);
	}
	
	public static List<OpinionResult> getOpinions(String query, int numResults) throws ClientProtocolException, IOException, SAXException, ParserConfigurationException {
		return getOpinions(query, "", numResults);
	}
	
	private static String getBaseOpinionsURL() {
		return getBaseURL() + "opinions?appkey=" + OPINION_API_KEY;
	}
	
	private static String getBaseGroupsURL() {
		return getBaseURL() + "groups?appkey=" + GROUP_API_KEY;
	}
	
	private static String getBaseURL() {
		return "http://api.appinions.com/search/v2/";
	}
	
	static class GroupResult {
		public String name;
		public String id;
		
		public GroupResult(String name, String id) {
			this.name = name;
			this.id = id;
		}
		
		public String toString() {
			return "name = " + name + ", id = " + id;
		}
	}
	
	static class OpinionResult {
		public String preSentence;
		public String sentence;
		public String postSentence;
		public String docLink;
		public String docTitle;
		public String publisher;
		public String type;
		public List<String> authors;
		
		public OpinionResult(String preSentence, String sentence,
				String postSentence, String docLink, String docTitle, String publisher, String type, List<String> authors) {
			this.preSentence = preSentence;
			this.sentence = sentence;
			this.postSentence = postSentence;
			this.docLink = docLink;
			this.docTitle = docTitle;
			this.publisher = publisher;
			this.type = type;
			this.authors = authors;
		}

		public OpinionResult(Node node) {
		}
				
		public String toString() {
			return "Opinion: \n\tpreSentence: " + this.preSentence + "\n\tsentence: " + 
					this.sentence + "\n\tpostSentence: " + this.postSentence + "\n\tdocLink: " + this.docLink +
					"\n\tdocTitle: " + this.docTitle + "\n\tpublisher: " + this.publisher + "\n\ttype: " + this.type + "\n\tauthors: " + authors;
		}
	}
	
	public static void main(String[] args) throws ClientProtocolException, IOException, SAXException, ParserConfigurationException {
//		List<GroupResult> results = AppinionsService.getOpinionHolders("911", 30);
//		for(GroupResult result : results) {
//			List<GroupResult> results1 = AppinionsService.getTopics(result.id, 10);
//			System.out.println(result.name);
//			for(GroupResult result1 : results1) {
//				System.out.println("\t" + result1.name);
//			}
//		}
		for(OpinionResult op : getOpinions("iraq", 30)) {
			System.out.println(op);
		}
	}
}
