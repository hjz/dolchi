

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class HTTPHelper {

	public static StringBuilder getHTML(String url) throws IllegalStateException, IOException {
		BufferedReader in = null;
		StringBuilder sb = null;
        try {
        	HttpClient httpClient = new DefaultHttpClient();
    		HttpGet request = new HttpGet(url);
    		HttpResponse response = httpClient.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            sb = new StringBuilder("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb; 
	}
	
	 public static Document getXML(String url) throws SAXException, IOException, ParserConfigurationException {
		 StringBuilder html = HTTPHelper.getHTML(url);
		 System.out.println(html.toString());
		 DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		 DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		 ByteArrayInputStream bs = new ByteArrayInputStream(html.toString().getBytes());
		 Document doc = dBuilder.parse(bs);
		 return doc;
	 }
}
