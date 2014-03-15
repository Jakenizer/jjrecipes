package se.jacob.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.jacob.gui.GUI;
import se.jacob.panel.ExistingRecipe;
import se.jacob.panel.NewRecipe;
import se.jacob.xml.FileHandler;
import se.jacob.xml.RecipeObject;
//import se.jacob.panel.ListRecipes;

public class MainMenuListener implements ActionListener {
	
	private JFrame parent;
	private Logger log = LoggerFactory.getLogger(getClass());
	
	public MainMenuListener(JFrame parent) {
		this.parent = parent;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		switch (action) {
			case "Quit": {
				System.exit(0);
			}
			case "New Recipe": {
				JTabbedPane tabbedPane = (JTabbedPane) GUI.getComponent("TabbedPane");
				NewRecipe recipeView = new NewRecipe(tabbedPane);
				recipeView.addClosableTab();
				log.info("New Recipe tab opened");
				break;
			}
			case "Open Recipe": {
				RecipeObject obj = FileHandler.searchForRecipe(parent);
				if (obj == null) {
					JOptionPane.showMessageDialog(parent, "Recipe does not exist");
					break;
				}
				
				Integer idAttribute = obj.getId();
				if (idAttribute != null && (idAttribute > 0)) {
					JTabbedPane tabbedPane = (JTabbedPane) GUI.getComponent("TabbedPane");
					ExistingRecipe recipeView = new ExistingRecipe(tabbedPane, obj);
					recipeView.addClosableTab();
				}
				log.info("Recipe with ID: {} and name: {}", obj.getId(), obj.getTitle());
				break;
			}
		}
	}
}
