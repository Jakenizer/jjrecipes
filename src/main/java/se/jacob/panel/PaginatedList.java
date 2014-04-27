package se.jacob.panel;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PaginatedList extends JList<String> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private NodeList list;
	private int rows;
	private int startIndex = 0;
	private DefaultListModel<String> model;
	
	public PaginatedList(DefaultListModel<String> model, NodeList listData, int numRows) {
		this.model = model;
		this.list = listData;
		if (listData.getLength() < 1 || numRows < 1)
			throw new IllegalArgumentException("list size and number of view rows must be at least 1");
		
		this.rows = numRows;
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setSelectedIndex(0);
		setVisibleRowCount(rows);
		setModel(model);
		updatePage();	
	}
	
	public void nextPage() {
		if ((startIndex + rows) >= list.getLength()) {
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
		Double inflatedNumber = (double) (list.getLength() / rows);
		int deflatedNumber = inflatedNumber.intValue();
		startIndex = rows * (deflatedNumber-1);
		updatePage();
	}
	
	private void updatePage() {
		model.clear();
		System.out.println(startIndex);
		int maxLength = list.getLength();
		for (int i = 0; i < rows && maxLength > (startIndex + i); i++) {
			Node item = list.item(startIndex + i);
			String idAttribute = item.getAttributes().item(0).getTextContent();
			String title = item.getChildNodes().item(1).getTextContent();
			model.add(i, idAttribute + ": " + title);
		}
	}
}
