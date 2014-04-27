package se.jacob.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NodeList;

import se.jacob.exception.SearchFileException;
import se.jacob.xml.FileHandler;

public class ListRecipes extends AbstractPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int LIST_WIDTH = 400; 
	private final int LIST_LENGTH = 7;
	private final PaginatedList leftList;
	private DefaultListModel<String> listModel;
	private JPanel buttonPanel;
	private JButton toFirstButton;
	private JButton nextButton;
	private JButton previousButton;
	private JButton toLastButton;
	
	private static final Logger LOG = LoggerFactory.getLogger(ListRecipes.class);
	
	public ListRecipes(JTabbedPane parent) throws FileNotFoundException, SearchFileException {
		super(parent);
				
		setBackground(Color.LIGHT_GRAY);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel mainPanel = new JPanel();
		listModel = new DefaultListModel<>();
		NodeList listData = FileHandler.getAllRecipes();
		leftList = new PaginatedList(listModel, listData, LIST_LENGTH);
		leftList.setPreferredSize(new Dimension(LIST_WIDTH, 450));
		
		buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		buttonPanel.setPreferredSize(new Dimension(LIST_WIDTH, 36));
		
		toFirstButton = new JButton(new AbstractAction("first") {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				leftList.firstPage();
			}
		});
		
		previousButton = new JButton(new AbstractAction("previous") {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				leftList.previousPage();
			}
		});
		
		nextButton = new JButton(new AbstractAction("next") {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				leftList.nextPage();
			}
		}); 
		
		toLastButton = new JButton(new AbstractAction("last") {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				leftList.lastPage();
			}
		});
		
		buttonPanel.add(toFirstButton);
		buttonPanel.add(previousButton);
		buttonPanel.add(nextButton);
		buttonPanel.add(toLastButton);
		
        //JScrollPane listScrollPane = new JScrollPane(leftList);
		//listScrollPane.setPreferredSize(new Dimension(400, 450));
		
		GroupLayout layout = new GroupLayout(mainPanel);
		mainPanel.setLayout(layout);
		mainPanel.setAlignmentX(LEFT_ALIGNMENT);
		layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING) 
                .addComponent(leftList, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)
                .addComponent(buttonPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)
             )                
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(leftList, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
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