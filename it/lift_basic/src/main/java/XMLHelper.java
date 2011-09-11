package code.snippet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLHelper {

	 public static List<Element> getElementsByTag(Document doc, String sTag) {
		 List<Element> elements = new ArrayList<Element>();
		 NodeList nList = doc.getElementsByTagName(sTag);
		 for (int i = 0; i < nList.getLength(); i++) {
			 Node nNode = nList.item(i);
			 if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				 elements.add((Element) nNode);
			 }
		 }
		 return elements;
	 }

	 public static List<Element> getElementsByTag(Element el, String sTag) {
		 List<Element> elements = new ArrayList<Element>();
		 NodeList nList = el.getElementsByTagName(sTag);
		 for (int i = 0; i < nList.getLength(); i++) {
			 Node nNode = nList.item(i);
			 if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				 elements.add((Element) nNode);
			 }
		 }
		 return elements;
	 }

	 public static String getTagValue(String sTag, Element eElement) {
		 NodeList nlList = eElement.getElementsByTagName(sTag);
		 if(nlList == null)
			 return "";
		 Node node = nlList.item(0);
		 if(node == null)
			 return "";
		 nlList = node.getChildNodes();
		 if(nlList == null)
			 return "";
		 Node nValue = (Node) nlList.item(0);
		 return nValue == null ? "" : nValue.getNodeValue();
	 }


}
