package se.jacob.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.io.FileNotFoundException;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ListRecipes extends AbstractPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JList<String> leftList;
	private DefaultListModel<String> listModel;
	private static final Logger LOG = LoggerFactory.getLogger(ListRecipes.class);
	
	public ListRecipes(JTabbedPane parent) throws FileNotFoundException {
		super(parent);
				
		setBackground(Color.LIGHT_GRAY);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel mainPanel = new JPanel();
		leftList = new JList<String>();
		listModel = new DefaultListModel<>();
		leftList = new JList<>(listModel);
		leftList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		leftList.setSelectedIndex(0);
		leftList.setVisibleRowCount(10);
        JScrollPane listScrollPane = new JScrollPane(leftList);
		listScrollPane.setPreferredSize(new Dimension(400, 450));
		
		GroupLayout layout = new GroupLayout(mainPanel);
		mainPanel.setLayout(layout);
		mainPanel.setAlignmentX(LEFT_ALIGNMENT);
		layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING) 
                .addComponent(listScrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)
             )                
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(listScrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)
             )
        );

		mainPanel.setBackground(Color.LIGHT_GRAY);
		add(mainPanel);	
		
		
		
		
		//createContent();
	}
	
	private void createContent() {
		setBackground(Color.LIGHT_GRAY);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel mainPanel = new JPanel();
		leftList = new JList<String>();
		listModel = new DefaultListModel<>();
		leftList = new JList<>(listModel);
		leftList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		leftList.setSelectedIndex(0);
		leftList.setVisibleRowCount(20);
        JScrollPane listScrollPane = new JScrollPane(leftList);
		listScrollPane.setPreferredSize(new Dimension(255, 400));
		
		GroupLayout layout = new GroupLayout(mainPanel);
		mainPanel.setLayout(layout);
		mainPanel.setAlignmentX(LEFT_ALIGNMENT);
		layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING) 
                .addComponent(leftList, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)
             )                
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(leftList)
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
