package se.jacob.panel;

import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

import se.jacob.xml.RecipeObject;

public class PaginatedList extends JList<String> {
	
	private static final long serialVersionUID = 1L;
	private List<RecipeObject> objectList;
	private int rows;
	private int startIndex = 0;
	private DefaultListModel<String> model;
		
	public PaginatedList(DefaultListModel<String> model, List<RecipeObject> listData, int numRows) {
		this.model = model;
		this.objectList = listData;
		if (listData.size() < 1 || numRows < 1)
			throw new IllegalArgumentException("list size and number of view rows must be at least 1");
		
		this.rows = numRows;
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setSelectedIndex(0);
		setVisibleRowCount(rows);
		setModel(model);
		updatePage();	
	}
	
	public void nextPage() {
		if ((startIndex + rows) >= objectList.size()) {
			return;
		}
		startIndex += rows;
		updatePage();
	}
	
	public void previousPage() {
		if ((startIndex - rows) <= 0) { 
			startIndex = 0;
		} else {
			startIndex = startIndex - rows;
		}
		updatePage();
	}
	
	public void firstPage() {
		startIndex = 0;
		updatePage();
	}
	
	public void lastPage() {
//		Double inflatedNumber = (double) (objectList.size() / rows);
//		int deflatedNumber = inflatedNumber.intValue();
//		startIndex = rows * (deflatedNumber-1);
		int mod = objectList.size() % rows;
		if (mod == 0) {
			startIndex = objectList.size() - rows;
		} else {
			startIndex = objectList.size() - mod;
		}
		updatePage();
	}
	
	/**
	 * Get the selected Recipe Object
	 * @return the RecipeObject corresponding to the selected value or null if nothing is selected
	 */
	public RecipeObject getSelectedDataItem() {
		if (isSelectionEmpty()) return null;
		
		int index = startIndex + getSelectedIndex();
		return objectList.get(index);
	}
	
	public int getStartIndex() {
		return startIndex;
	}
	
	private void updatePage() {
		model.clear();
		int maxLength = objectList.size();
		for (int i = 0; i < rows && maxLength > (startIndex + i); i++) {
			RecipeObject item = objectList.get(startIndex + i);
			String title = item.getTitle();
			model.add(i, title);
		}
	}
}