package se.jacob.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.jacob.panel.AbstractView;
import se.jacob.panel.ViewFactory;
import se.jacob.panel.ViewFactory.Views;

public class MainMenuListener implements ActionListener {
	
	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void actionPerformed(ActionEvent e) {
		AbstractView view = null;
		String action = e.getActionCommand();
		switch (action) {
			case "Quit": {
				//TODO: really exit? and save current view first
				System.exit(0);
			}
			case "New Recipe": { 
				view = ViewFactory.getView(Views.NEW_RECIPE);	
				break;
			}
			
			case "Search Recipe": {
				//log.info("Recipe with ID: {} and name: {} opened", obj.getId(), obj.getTitle());
				view = ViewFactory.getView(Views.SEARCH_AND_OPEN_RECIPE);
				break;
			}
			
			case "List Recipes": {
				view = ViewFactory.getView(Views.LIST_RECIPES);
				break;
			}
			
			case "Validate: Unique XML ids" : {
				log.warn("validation not implemented");
				/*try {
					boolean isOk = SearchTool.validateUniqueIds();
					if (!isOk) {
						log.error("Corrupt XML save file: " +
								"There are one or more duplicate recipe objects in recipes.xml");
					}
				} catch (Exception e1) {
					log.error(e1.getMessage());
				}*/
				break;
			}
		}
		if (view != null)
			view.addClosableTab();
	}
}