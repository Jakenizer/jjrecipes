package se.jacob.panel;

import java.io.FileNotFoundException;

import javax.swing.JTabbedPane;

public class ListRecipes extends AbstractPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ListRecipes(JTabbedPane parent) throws FileNotFoundException {
		super(parent);
	}

	@Override
	public String getTitle() {
		return "Recipe List";
	}

}
