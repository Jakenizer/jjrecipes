package se.jacob.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.jacob.exception.SaveFileException;
import se.jacob.exception.SearchFileException;
import se.jacob.xml.FileHandler;
import se.jacob.xml.RecipeObject;

public class NewRecipeView extends CommonRecipeView {

	private static final long serialVersionUID = 1L;	
	private static final Logger LOG = LoggerFactory.getLogger(NewRecipeView.class);

	public NewRecipeView() throws FileNotFoundException {
		super();
	}
	
	@Override
	public String getTitle() {
		return "New Recipe";
	}
	
	@Override
	protected ActionListener getSaveActionListener() {
		final JComponent that = this;
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
					parent.remove(that);
				} catch (SaveFileException | SearchFileException ex) {
					LOG.error(ex.getMessage());
					JOptionPane.showMessageDialog(parent, "Could not save file", "File saving error", ERROR);
				}
			}
		};
	}
}