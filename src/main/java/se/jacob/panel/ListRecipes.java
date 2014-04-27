package se.jacob.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NodeList;

import se.jacob.exception.SearchFileException;
import se.jacob.xml.FileHandler;
import se.jacob.xml.RecipeObject;

public class ListRecipes extends AbstractPanel {

	private static final long serialVersionUID = 1L;
	private final int LIST_WIDTH = 600; 
	private final int LIST_LENGTH = 7;
	private final PaginatedList contentList;
	private DefaultListModel<String> listModel;
	private JPanel buttonPanel;
	private JButton toFirstButton;
	private JButton nextButton;
	private JButton previousButton;
	private JButton toLastButton;
	private JButton selectButton;
	
	private static final Logger LOG = LoggerFactory.getLogger(ListRecipes.class);
	
	public ListRecipes(final JTabbedPane parent) throws FileNotFoundException, SearchFileException {
		super(parent);
		setBackground(Color.LIGHT_GRAY);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel mainPanel = new JPanel();
		listModel = new DefaultListModel<>();
		List<RecipeObject> listData = FileHandler.getAllRecipes();
		contentList = new PaginatedList(listModel, listData, LIST_LENGTH);
		contentList.setPreferredSize(new Dimension(LIST_WIDTH, 450));
		
		buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		buttonPanel.setBackground(Color.LIGHT_GRAY);
		buttonPanel.setPreferredSize(new Dimension(LIST_WIDTH, 36));
		
		toFirstButton = new JButton(new AbstractAction("First") {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				contentList.firstPage();
			}
		});
		
		previousButton = new JButton(new AbstractAction("Previous") {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				contentList.previousPage();
			}
		});
		
		nextButton = new JButton(new AbstractAction("Next") {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				contentList.nextPage();
			}
		}); 
		
		toLastButton = new JButton(new AbstractAction("Last") {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				contentList.lastPage();
			}
		});
		
		final JComponent that = this;
		selectButton = new JButton(new AbstractAction("Select Recipe") {

			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				RecipeObject selectedItem = contentList.getSelectedDataItem();
				//TODO: Öppna Existing Recipe
				if (selectedItem != null) {
					System.out.println(selectedItem.getTitle());
					
					
					
					parent.remove(that);
				}
			}
		}); 
		
		buttonPanel.add(toFirstButton);
		buttonPanel.add(previousButton);
		buttonPanel.add(nextButton);
		buttonPanel.add(toLastButton);
		buttonPanel.add(selectButton);
		
        //JScrollPane listScrollPane = new JScrollPane(leftList);
		//listScrollPane.setPreferredSize(new Dimension(400, 450));
		
		GroupLayout layout = new GroupLayout(mainPanel);
		mainPanel.setLayout(layout);
		mainPanel.setAlignmentX(LEFT_ALIGNMENT);
		layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING) 
                .addComponent(contentList, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)
                .addComponent(buttonPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)
             )                
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(contentList, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)
                
             )
             .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
        		 .addComponent(buttonPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)
             )
        );

		mainPanel.setBackground(Color.LIGHT_GRAY);
		add(mainPanel);	
	}

	@Override
	public String getTitle() {
		return "Recipe List";
	}
}