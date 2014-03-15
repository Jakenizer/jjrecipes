package se.jacob.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JTabbedPane;

import se.jacob.xml.FileHandler;
import se.jacob.xml.RecipeObject;

public class ExistingRecipe extends CommonRecipeView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RecipeObject obj;
	
	public ExistingRecipe(JTabbedPane parent, RecipeObject obj) {
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
				
				FileHandler.persist(obj);
			}
		};
	}

	@Override
	public String getTitle() {
		return obj.getTitle();
	}

}
