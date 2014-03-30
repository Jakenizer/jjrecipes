package se.jacob.xml;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.FileInputStream;
import java.io.IOException;
import org.xml.sax.SAXException;

import se.jacob.Constants;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class SearchTool {
	private static Logger log = LoggerFactory.getLogger(SearchTool.class.getName());

	
	/**
	 * 
	 * @return the next ID to use as increment in the xml.
	 */
	public static Integer getNextId() {
		Integer intRes = 1;
		NodeList nodeList = searchForMultipleNodes("//@id[not(. < //@id)]"); //högsta idvärdet
		if (nodeList != null && nodeList.getLength() != 0) {
			String res = nodeList.item(0).getFirstChild().getNodeValue();
			intRes = Integer.parseInt(res) + 1;
		}
		return intRes;
	}
	
	public static boolean idExists(Integer id) {
		Node node = searchForSingleNodeById(id.toString());
		return node == null ? false : true;
	}
	
	/**
	 * Dude, use this function in others to search.
	 * @param expression
	 * @return nodeList A list of resulting nodes
	 */
	private static NodeList searchForMultipleNodes(String expression) {
		DocumentBuilder builder;
		Document document;
		NodeList nodeList = null;
		
		DocumentBuilderFactory builderFactory =
		        DocumentBuilderFactory.newInstance();
		
		try {
		    builder = builderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
		    e.printStackTrace();
		    return null;
		}
		
		try {
		    document = builder.parse(
		            new FileInputStream(Constants.XML_PATH));
		} catch (SAXException | IOException e) {
		    e.printStackTrace();
		    return null;
		}
		
		XPath xPath =  XPathFactory.newInstance().newXPath();
		
		try {
			nodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			return null;
		}
		
		return nodeList;		
	}
	
	
	public static Node searchForSingleNodeById(String id) {
		DocumentBuilder builder;
		Document document;
		Node node = null;
		
		DocumentBuilderFactory builderFactory =
		        DocumentBuilderFactory.newInstance();
		
		try {
		    builder = builderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
		    e.printStackTrace();
		    return null;
		}
		
		try {
		    document = builder.parse(
		            new FileInputStream(Constants.XML_PATH));
		} catch (SAXException | IOException e) {
		    e.printStackTrace();
		    return null;
		}
		
		XPath xPath =  XPathFactory.newInstance().newXPath();
		
		String expression = "/recipes/recipe[@id='" + id +"']";
		try {
			node = (Node) xPath.compile(expression).evaluate(document, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			return null;
		}
		
		return node;
	}
	
	/**
	 * Possible to search by parts of the title
	 * @param title
	 * @return
	 */
	public static NodeList searchForNodesByTitle(String title) {
		DocumentBuilder builder;
		Document document;
		NodeList nodeList = null;
		
		if (title == null || title.length() == 0)
			return null;
		
		DocumentBuilderFactory builderFactory =
		        DocumentBuilderFactory.newInstance();
		
		try {
		    builder = builderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
		    e.printStackTrace();
		    return null;
		}
		
		try {
		    document = builder.parse(
		            new FileInputStream(Constants.XML_PATH));
		} catch (SAXException | IOException e) {
		    return null;
		}
		
		XPath xPath =  XPathFactory.newInstance().newXPath();
		
		String expression = "/recipes/recipe[title[contains(text(), '" + title + "')]]";
		
		try {
			nodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		
		return nodeList;	
	}
	
	
	public static NodeList getDuplicateNodes() throws Exception {
		DocumentBuilder builder;
		Document document;
		NodeList nodeList = null;
		
		DocumentBuilderFactory builderFactory =
		        DocumentBuilderFactory.newInstance();
		
		try {
		    builder = builderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
		    e.printStackTrace();
		    throw new Exception("det blev del");
		}
		
		try {
		    document = builder.parse(
		            new FileInputStream(Constants.XML_PATH));
		} catch (SAXException | IOException e) {
		    e.printStackTrace();
		    throw new Exception("Det blev fel");
		}
		
		XPath xPath =  XPathFactory.newInstance().newXPath();
		
		String expression = "//recipes/recipe[(@id=preceding::recipe/@id)]"; //@id;

		try {
			nodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			log.error(e.getMessage());
		}
		//System.out.println(nodeList.getLength());
		return nodeList;
		/*for (int i = 0; i < nodeList.getLength(); i++) {
			Node n = nodeList.item(i);
			System.out.println(n.getTextContent());
		}*/
	}
	
	public static void resolveDuplicatesInXML() throws Exception {
		NodeList list = getDuplicateNodes();
		if (list.getLength() == 0)
			return;
		
		
	}
}
