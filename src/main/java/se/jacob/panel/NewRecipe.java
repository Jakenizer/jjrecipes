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
import se.jacob.xml.FileHandler;
import se.jacob.xml.RecipeObject;

public class NewRecipe extends CommonRecipeView {

	private static final long serialVersionUID = 1L;	
	private static final Logger LOG = LoggerFactory.getLogger(NewRecipe.class);

	public NewRecipe(JTabbedPane parent) throws FileNotFoundException {
		super(parent);
		
	}
	
	@Override
	public String getTitle() {
		return "New Recipe";
	}
	
	@Override
	protected ActionListener getSaveActionListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				String title = nameField.getText().trim();
				String content = recipeText.getText().trim();
				
				Enumeration<String> allElements = listModel.elements();
				List<String> ingredients = new ArrayList<String>();
				while(allElements.hasMoreElements()) {
					ingredients.add(allElements.nextElement().trim());
				}
				
				RecipeObject obj = new RecipeObject(null, title, content, ingredients);
				
				try {
					FileHandler.persist(obj);
				} catch (SaveFileException ex) {
					LOG.error(ex.getMessage());
					JOptionPane.showMessageDialog(parent, "Could not save file", "File saving error", ERROR);
				}
			}
		};
	}
}