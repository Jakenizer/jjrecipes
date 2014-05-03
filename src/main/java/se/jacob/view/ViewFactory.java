package se.jacob.view;

import java.io.FileNotFoundException;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.jacob.exception.SearchFileException;
import se.jacob.gui.GUI;
import se.jacob.xml.FileHandler;
import se.jacob.xml.FileHandler.SearchResult;
import se.jacob.xml.FileHandler.SearchResult.ResultCode;
import se.jacob.xml.RecipeObject;

public class ViewFactory {

	private static Logger LOG = LoggerFactory.getLogger(ViewFactory.class);
	
	private ViewFactory() {}
	
	public static AbstractView getView(Views v, Object... args) {
		AbstractView view = null;
		switch(v) {
			case NEW_RECIPE: {
				try {
					view = new NewRecipeView();
				} catch (FileNotFoundException e) {
					LOG.error(e.getMessage());
				}
				break;
			}
			case LIST_RECIPES: {
				try {
					view = new ListRecipesVIew();
				} catch (FileNotFoundException | SearchFileException e1) {
					LOG.error("Error in when populating list in  List Recipes" + e1.getMessage());
				}
				break;
			}
			case SEARCH_AND_OPEN_RECIPE: {
				SearchResult resultObj = null;
				try {
					resultObj = FileHandler.searchForRecipe(GUI.getMainFrame());
				} catch (SearchFileException e) {
					LOG.error(e.getMessage());
					break;
				}
				
				ResultCode rc = resultObj.getResultCode();
				
				switch(rc) {
					case SUCCESS: {
						RecipeObject obj = resultObj.getRecipeObject();
						Integer idAttribute = obj.getId();
						if (idAttribute != null && (idAttribute > 0)) {
							try {
								view = new ExistingRecipeView(obj);
								LOG.info(rc.getValue() + ". Recipe with ID: {} and title: {} opened", obj.getId(), obj.getTitle());
							} catch (FileNotFoundException e) {
								LOG.error(e.getMessage());
							}
						}
						break;
					}
					case CANCELLED_SEARCH: {
						LOG.info(rc.getValue());
						break;
					}
					case CANCELLED_SELECTION: {
						LOG.info(rc.getValue());
						break;
					}
					case EMPTY_SEARCH_STRING: {
						LOG.info("Empty search string: Search string must be at least one letter long");
						break;
					}
					case NO_MATCH: {
						LOG.info("No recipe with title containing '{}' found", resultObj.getSearchString());
						JOptionPane.showMessageDialog(GUI.getMainFrame(), "Recipe with title " 
								+ resultObj.getSearchString() + " does not exist");
						break;
					}
					case UNEXPECTED_ERROR: {
						LOG.warn(rc.getValue());
						break;
					}
				}
			}
			case OPEN_RECIPE: {
				if (args.length == 0) {
					LOG.error("A valid RecipeObject is needed to open recipe view");
					break;
				}
				RecipeObject obj = (RecipeObject) args[0];
				try {
					view = new ExistingRecipeView(obj);
				} catch (FileNotFoundException e) {
					LOG.error(e.getMessage());				
				}
				break;
			}

		}
		return view;
	}
	
	public enum Views {
		NEW_RECIPE,
		LIST_RECIPES,
		OPEN_RECIPE,
		SEARCH_AND_OPEN_RECIPE
	}
}
