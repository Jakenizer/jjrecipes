package se.jacob.panel;

import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
		//addListener();
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
		Double inflatedNumber = (double) (objectList.size() / rows);
		int deflatedNumber = inflatedNumber.intValue();
		startIndex = rows * (deflatedNumber-1);
		updatePage();
	}
	
	/*private void addListener() {
		getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int itemIndex = getStartIndex() + getSelectedIndex();
				Node item = list.item(itemIndex);
				String idAttribute = item.getAttributes().item(0).getTextContent();
				String title = item.getChildNodes().item(1).getTextContent();
				System.out.println(idAttribute + ": " + title);
			}
		});
	}*/
	
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