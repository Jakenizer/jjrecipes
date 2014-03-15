package se.jacob.xml;

import java.util.List;

public class RecipeObject {

	private Integer id;
	private String title;
	private String content;
	private List<String> ingredientList;
	
	public RecipeObject(Integer id, String title, String content, List<String> ingredientList) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.ingredientList = ingredientList;
	}


	public Integer getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<String> getIngredientList() {
		return ingredientList;
	}

	public void setIngredientList(List<String> ingredientList) {
		this.ingredientList = ingredientList;
	}
}
