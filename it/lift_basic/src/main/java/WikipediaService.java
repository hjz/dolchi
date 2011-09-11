

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WikipediaService {

	private static Pattern wikiResults =  Pattern.compile("([^\"]+)");
    
	public static List<String> getMatchedArticleNames(String query) throws IllegalStateException, IOException {
		StringBuilder html = HTTPHelper.getHTML("http://en.wikipedia.org/w/api.php?action=opensearch&search=" + query);
		Matcher matcher = wikiResults.matcher(html.toString().substring(0,html.length()-3));
		List<String> results = new ArrayList<String>();
		int count = 0;
		while(matcher.find()) {
			if(count >= 2 && !matcher.group().equals("[") && !matcher.group().equals(",[") && !matcher.group().equals("]") && !matcher.group().equals(",]") && !matcher.group().equals(",") && !matcher.group().equals("]]"))
				results.add(matcher.group());
			count++;
        }
		return results;
	}
	
	public static String getArticle(String query) throws IllegalStateException, IOException {
		List<String> topResults = getMatchedArticleNames(query);
		if(topResults.size() == 0)
			return "";
		else
			return "http://en.wikipedia.org/wiki/" + topResults.get(0);
	}
	
	public static void main(String[] args) throws IllegalStateException, IOException {
		System.out.println(getArticle("barack"));
	}
}
