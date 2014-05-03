package se.jacob.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.jacob.exception.SaveFileException;
import se.jacob.exception.SearchFileException;
import se.jacob.xml.FileHandler;
import se.jacob.xml.RecipeObject;

public class ExistingRecipeView extends CommonRecipeView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(CommonRecipeView.class);
	private RecipeObject obj;
	
	public ExistingRecipeView(RecipeObject obj) throws FileNotFoundException {
		this.obj = obj;
		String contentString = obj.getContent();
		recipeText.setText(contentString);
		
		List<String> list = obj.getIngredientList();
		for (String ingredient : list) {
			listModel.addElement(ingredient);
		}	
	}

	@Override
	protected ActionListener getSaveActionListener() {
		return new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				obj.setTitle(nameField.getText().trim());
				obj.setContent(recipeText.getText().trim());
				
				Enumeration<String> allElements = listModel.elements();
				List<String> ingredients = new ArrayList<String>();
				while(allElements.hasMoreElements()) {
					ingredients.add(allElements.nextElement().trim());
				}
				
				obj.setIngredientList(ingredients);
				
				try {
					FileHandler.persist(obj);
					LOG.info("Recipe with ID: " + obj.getId() + " and title: " + obj.getTitle() + " updated in save file");
				} catch (SaveFileException | SearchFileException ex) {
					LOG.error(ex.getMessage());
					JOptionPane.showMessageDialog(parent, "Could not save file", "File saving error", ERROR);
				}
			}
		};
	}

	@Override
	public String getTitle() {
		return obj.getTitle();
	}
}
