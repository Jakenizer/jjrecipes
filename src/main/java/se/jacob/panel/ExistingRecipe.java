package se.jacob.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.jacob.exception.SaveFileException;
import se.jacob.exception.SearchFileException;
import se.jacob.xml.FileHandler;
import se.jacob.xml.RecipeObject;

public class ExistingRecipe extends CommonRecipeView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(CommonRecipeView.class);
	private RecipeObject obj;
	
	public ExistingRecipe(JTabbedPane parent, RecipeObject obj) throws FileNotFoundException {
		super(parent);
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
