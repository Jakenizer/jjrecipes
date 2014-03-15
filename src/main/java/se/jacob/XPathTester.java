package se.jacob;

import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import se.jacob.xml.FileHandler;

public class XPathTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DocumentBuilder builder;
		Document document;
		Node node = null;
		
		DocumentBuilderFactory builderFactory =
		        DocumentBuilderFactory.newInstance();
		
		try {
		    builder = builderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
		    e.printStackTrace();
		    return;
		}
		
		try {
		    document = builder.parse(
		            new FileInputStream(Constants.XML_PATH));
		} catch (SAXException | IOException e) {
		    e.printStackTrace();
		    return;
		}
		
		XPath xPath =  XPathFactory.newInstance().newXPath();
		
		String expression = String.format("/recipes/recipe[title[contains(text(), %s)]]", "ar3");
		try {
			node = (Node) xPath.compile(expression).evaluate(document, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		System.out.println(node.getFirstChild().getTextContent());
	}

}
