package se.jacob.panel;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.jacob.exception.SearchFileException;
import se.jacob.panel.ViewFactory.Views;
import se.jacob.xml.FileHandler;
import se.jacob.xml.RecipeObject;

public class ListRecipes extends AbstractView {

	private static final long serialVersionUID = 1L;
	private final int LIST_WIDTH = 430; 
	private final int LIST_LENGTH = 20;
	private final PaginatedList contentList;
	private DefaultListModel<String> listModel;
	private JPanel buttonPanel;
	private JButton toFirstButton;
	private JButton nextButton;
	private JButton previousButton;
	private JButton toLastButton;
	private JButton selectButton;
	
	private static final Logger LOG = LoggerFactory.getLogger(ListRecipes.class);
	
	public ListRecipes() throws FileNotFoundException, SearchFileException {
		setBackground(Color.LIGHT_GRAY);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel mainPanel = new JPanel();
		
		PaginationLabel rangeDisplay = new PaginationLabel() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void setRangeText(String t) {
				this.setText("Recipes " + t);
			}
		};
		
		listModel = new DefaultListModel<>();
		List<RecipeObject> listData = FileHandler.getAllRecipes();
		contentList = new PaginatedList(listModel, listData, LIST_LENGTH);
		contentList.setPreferredSize(new Dimension(LIST_WIDTH, 340));
		contentList.addRangeSubscriber(rangeDisplay);
		
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
				//Öppna Existing Recipe
				if (selectedItem != null) {
					ExistingRecipe view = (ExistingRecipe) ViewFactory.getView(Views.OPEN_RECIPE, selectedItem);
					if (view != null) {
						view.addClosableTab();
						parent.remove(that);
						LOG.info("Recipe with ID: {} and title: {} opened", selectedItem.getId(), selectedItem.getTitle());				
					}
				}
			}
		}); 
		
		buttonPanel.add(toFirstButton);
		buttonPanel.add(previousButton);
		buttonPanel.add(nextButton);
		buttonPanel.add(toLastButton);
		buttonPanel.add(selectButton);
		
		//filler
		JPanel filler = new JPanel();
		filler.setPreferredSize(new Dimension(100, 340));
		filler.setBackground(Color.LIGHT_GRAY);
		
		//right panel
		JPanel recipePreview = setupPreviewPanel();
		contentList.setPreviewPanel(recipePreview);
		
		GroupLayout layout = new GroupLayout(mainPanel);
		mainPanel.setLayout(layout);
		mainPanel.setAlignmentX(LEFT_ALIGNMENT);
		layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING) 
        		.addComponent(rangeDisplay, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)	
                .addComponent(contentList, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)
                .addComponent(buttonPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)
             )
             .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING) 
            	.addComponent(filler, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                             GroupLayout.PREFERRED_SIZE)
             )
             .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING) 
            	 .addComponent(recipePreview, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                             GroupLayout.PREFERRED_SIZE)	
             )
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(rangeDisplay, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)
                
             )
             .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(contentList, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)
                .addComponent(filler, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                     GroupLayout.PREFERRED_SIZE)
                .addComponent(recipePreview, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE)	
                
             )
             .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
        		 .addComponent(buttonPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)
             )
        );
		contentList.addKeyListener(new ArrowKeyListener());
		mainPanel.setBackground(Color.LIGHT_GRAY);
		add(mainPanel);	
	}

	@Override
	public String getTitle() {
		return "Recipe List";
	}
	
	private JPanel setupPreviewPanel() {
		JPanel recipePreview = new JPanel();
		recipePreview.setPreferredSize(new Dimension(LIST_WIDTH, 340));
		recipePreview.setBackground(new Color(230, 230, 230));
		GroupLayout layout = new GroupLayout(recipePreview);
		recipePreview.setLayout(layout);
		recipePreview.setAlignmentX(LEFT_ALIGNMENT);
		
		
		JLabel titleLabel = new JLabel();
		titleLabel.setText("Title");
		titleLabel.setFont(new Font("Verdana", Font.PLAIN, 20)); 
		
		JLabel ingredients = new JLabel();
		ingredients.setHorizontalAlignment(SwingConstants.LEFT);
		ingredients.setVerticalAlignment(SwingConstants.TOP);
		ingredients.setFont(new Font("Verdana", Font.PLAIN, 12)); 
		ingredients.setOpaque(true);
		ingredients.setPreferredSize(new Dimension(100, 280));
		ingredients.setText("<html>socker<br/>kanel<br/>kakao<br/>kött<br/>bacon</html>");
		ingredients.setBackground(Color.WHITE);
		
		JLabel contentLabel = new JLabel();
		contentLabel.setFont(new Font("Verdana", Font.PLAIN, 12)); 
		contentLabel.setHorizontalAlignment(SwingConstants.LEFT);
		contentLabel.setVerticalAlignment(SwingConstants.TOP);
		contentLabel.setPreferredSize(new Dimension(300, 280));
		contentLabel.setOpaque(true);
		contentLabel.setBackground(Color.WHITE);
		contentLabel.setText("<html>There are two types of fonts: physical fonts and logical fonts. Physical fonts are the actual font libraries consisting of, for example, TrueType or PostScript Type 1 fonts. The physical fonts may be Time, Helvetica, Courier, or any number of other fonts, including international fonts.</html>");
		

		layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING) 
        		.addComponent(titleLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)
                .addComponent(ingredients, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)	
             )
             .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING) 
        		.addComponent(contentLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)	

             )
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(titleLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                            GroupLayout.PREFERRED_SIZE)  
                 )
                 .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            		 .addComponent(ingredients, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
            				 GroupLayout.PREFERRED_SIZE)
	                 .addComponent(contentLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
	                         GroupLayout.PREFERRED_SIZE)	
                 )
            );
				
		return recipePreview;
	}
	
	private class ArrowKeyListener extends KeyAdapter {
		
		@Override
		public void keyReleased(KeyEvent e) {
			int keyCode = e.getKeyCode();
		    switch( keyCode ) { 
		        case KeyEvent.VK_LEFT: {
		        	previousButton.doClick();		            
		        	break;
		        }
		        case KeyEvent.VK_RIGHT : {
		        	nextButton.doClick();
		        	break;
		        }
		     }
		}
	}
}