package se.jacob.panel;

import javax.swing.JTabbedPane;

public class ListRecipes extends AbstractPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ListRecipes(JTabbedPane parent) {
		super(parent);
	}

	@Override
	public String getTitle() {
		return "Recipe List";
	}

}
