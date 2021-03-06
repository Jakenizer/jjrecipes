package se.jacob.xml;


import org.jdom2.Attribute;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import se.jacob.Constants;
import se.jacob.exception.SaveFileException;
import se.jacob.exception.SearchFileException;
import se.jacob.xml.FileHandler.SearchResult.ResultCode;

import java.awt.Container;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


public class FileHandler {
	
	private static Logger LOG = LoggerFactory.getLogger(FileHandler.class.getName());

	public static boolean saveNewRecipeToFile(RecipeObject obj) throws SaveFileException, SearchFileException {
		File f = new File(Constants.XML_PATH);
		if(!f.exists()){
			createNewEmptyFile();
		}
				
		return addRecipe(obj);
	}

	private static void createNewEmptyFile() throws SaveFileException {
		Element root = new Element("recipes");
		Document doc = new Document(root);
		try {
			XMLOutputter outputter = new XMLOutputter();
			outputter.setFormat(Format.getPrettyFormat());
			File file = new File(Constants.XML_PATH);
			if (file != null && !file.exists()) {
				file.createNewFile();
				outputter.output(doc, new FileWriter(file.getAbsoluteFile()));
			}
		}
		catch (IOException e) {
			throw new SaveFileException("Cannot create save file recipes.xml", e);
		}
	}

	private static boolean addRecipe(RecipeObject obj) throws SearchFileException {
		boolean success = true;
		
		SAXBuilder saxBuilder = new SAXBuilder();
		File xmlFile = new File(Constants.XML_PATH);

		try {
			Document document = saxBuilder.build(xmlFile);
			Element rootElement = document.getRootElement();
			Element recipeElement = new Element("recipe");
			String newId = SearchTool.getNextId().toString();
			recipeElement.setAttribute(new Attribute("id", newId));
			Element titleElement = new Element("title");
			Element contentElement = new Element("content");
			Element ingredientsElement = new Element("ingredients");
			titleElement.addContent(obj.getTitle());
			contentElement.addContent(obj.getContent());
			
			
			List<String> ingredients = obj.getIngredientList();
			StringBuffer sb = new StringBuffer();
			Iterator<String> it = ingredients.iterator();
			while(it.hasNext()) {
				sb.append(it.next());
				if(it.hasNext())
					sb.append(";");
			}
			ingredientsElement.addContent(sb.toString());
			
			
			recipeElement.addContent(titleElement);
			recipeElement.addContent(contentElement);
			recipeElement.addContent(ingredientsElement);
			
			rootElement.addContent(recipeElement);
			
			XMLOutputter xmlOutput = new XMLOutputter();
			xmlOutput.setFormat(Format.getPrettyFormat());
			xmlOutput.output(document, new FileWriter(Constants.XML_PATH));
			LOG.info("A new recipe with ID: " + obj.getId() + "and title: " + obj.getTitle() + " saved to file");
		} catch (IOException | JDOMException ex) {
			success = false;
			LOG.error("Could not add recipe to save file 'recipes.xml'.\n" + ex.getMessage());
		} 
		
		return success;
	}
	
	/**
	 * Gets search string input from a popup dialog that is used to find a recipe
	 * @param parent is used for placing the dialog correctly
	 * @return single recipe or null if none is found
	 * @throws SearchFileException
	 */
	public static SearchResult searchForRecipe(Container parent) throws SearchFileException {
		String queryString = (String)JOptionPane.showInputDialog(
                parent,
                "The full title or parts of the full title",
                "Find recipe by title",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                ""
            );
		
			if (queryString == null) {
				return new SearchResult(null, ResultCode.CANCELLED_SEARCH);
			}
					
			if (queryString.length() == 0) {
				return new SearchResult(null, ResultCode.EMPTY_SEARCH_STRING);
				//throw new SearchFileException("Search string must be at least one letter long", new IllegalArgumentException());
			}
			
			NodeList recipeList = SearchTool.searchForNodesByTitle(queryString);
			if (recipeList == null || recipeList.getLength() == 0) {
				return new SearchResult(null, ResultCode.NO_MATCH, queryString);
			}
			
			String idAttribute = null;
			if (recipeList.getLength() == 1) {
				idAttribute = recipeList.item(0).getAttributes().item(0).getTextContent();
			} 
			else if (recipeList.getLength() > 1) { 
				ArrayList<String> recipes = new ArrayList<String>();
				for (int i = 0; i < recipeList.getLength(); i++) {
					Node item = recipeList.item(i);
					idAttribute = item.getAttributes().item(0).getTextContent();
					String title = item.getChildNodes().item(1).getTextContent();
					if (idAttribute != null)
						recipes.add(idAttribute + ": " + title);
				}
				//TODO: Hur hantera nullvärden för idAttribute? bara hoppa över eller kasta exception eller logga?
				//TODO: begränsa till max 10?
				
				String result = (String)JOptionPane.showInputDialog(
					parent,
					"There are more than one recipe, select one",
					"Multiple search hits",
					JOptionPane.PLAIN_MESSAGE,
					null,
					recipes.toArray(),
					""
	             );
				if (result == null) {
					return new SearchResult(null, ResultCode.CANCELLED_SELECTION);
				}
				idAttribute = result.split(": ")[0].trim();
			}
			Node recipe = SearchTool.searchForSingleNodeById(idAttribute);
			if (recipe == null) {
				return new SearchResult(null, ResultCode.UNEXPECTED_ERROR);
			}
			String t = recipe.getChildNodes().item(1).getTextContent();
			String content = recipe.getChildNodes().item(3).getTextContent();
			
			String ingredientString = recipe.getChildNodes().item(5).getTextContent();
			String[] ingredientArray = ingredientString.split(";");
			List<String> ingredients = new ArrayList<String>();
			for (String ingredient : ingredientArray) {
				ingredients.add(ingredient);
			}	
			
			return new SearchResult(new RecipeObject(new Integer(idAttribute), t, content, ingredients), ResultCode.SUCCESS);
	}
	
	public static boolean persist(RecipeObject obj) throws SaveFileException, SearchFileException {
		Integer id = obj.getId();
		if (id == null) {
			saveNewRecipeToFile(obj);
		} else {
			updateExistingRecipe(obj);
		}
		
		return false;
	}
	
	private static boolean updateExistingRecipe(RecipeObject obj) throws SaveFileException, SearchFileException {
		boolean success = true;
		
		Node recipe = SearchTool.searchForSingleNodeById(obj.getId().toString());
		if (recipe == null)
			return false;
		
		recipe.getChildNodes().item(1).setTextContent(obj.getTitle());
		recipe.getChildNodes().item(3).setTextContent(obj.getContent());
		
		List<String> ingredients = obj.getIngredientList();
		StringBuffer sb = new StringBuffer();
		Iterator<String> it = ingredients.iterator();
		while(it.hasNext()) {
			sb.append(it.next());
			if(it.hasNext())
				sb.append(";");
		}
		recipe.getChildNodes().item(5).setTextContent(sb.toString());
		try{
			Transformer xformer = TransformerFactory.newInstance().newTransformer();
				xformer.transform
					(new DOMSource(recipe.getOwnerDocument()), new StreamResult(new File(Constants.XML_PATH)));
		} catch (TransformerException e) {
			throw new SaveFileException("Exception when updating recipe: " + obj.getTitle(), e);
		}
		return success;
	}
	
	public static List<RecipeObject> getAllRecipes() throws SearchFileException {
		List<RecipeObject> roList = new ArrayList<RecipeObject>();
		NodeList nl = SearchTool.getAllRecipes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			RecipeObject obj = nodeObjectToRecipeObject(n);
			roList.add(obj);
		}
		
		return roList;
	}
	
	public static RecipeObject nodeObjectToRecipeObject(Node n) {
		if (n == null) {
			return null;
		}
		String t = n.getChildNodes().item(1).getTextContent();
		String content = n.getChildNodes().item(3).getTextContent();
		
		String ingredientString = n.getChildNodes().item(5).getTextContent();
		String[] ingredientArray = ingredientString.split(";");
		List<String> ingredients = new ArrayList<String>();
		for (String ingredient : ingredientArray) {
			ingredients.add(ingredient);
		}	
		String idAttribute = n.getAttributes().item(0).getTextContent();

		return new RecipeObject(new Integer(idAttribute), t, content, ingredients);
	}
	
	public static class SearchResult{
		private RecipeObject o;
		private ResultCode c;
		private String s;
		
		public SearchResult(RecipeObject o, ResultCode c) {
			this.o = o;
			this.c = c;
		}
		
		public SearchResult(RecipeObject o, ResultCode c, String searchString) {
			this(o, c);
			this.s = searchString;
		}
		
		public RecipeObject getRecipeObject() {
			return o;
		}

		public ResultCode getResultCode() {
			return c;
		}

		public String getSearchString() {
			return s;
		}

		public enum ResultCode {
			SUCCESS("Search result found"), EMPTY_SEARCH_STRING("Search string is empty"), 
				CANCELLED_SEARCH("Search was cancelled"), CANCELLED_SELECTION("recipe selection was cancelled"), 
					NO_MATCH("No recipe containing the search string found"),
						UNEXPECTED_ERROR("Un unexpected error has occurred");
			
	        private String value;

	        ResultCode(String value) {
	            this.value = value;
	        }
	        
	        public String getValue() {
	        	return value;
	        }
		}
	}
}
