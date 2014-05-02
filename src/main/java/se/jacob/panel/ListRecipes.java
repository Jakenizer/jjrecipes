package se.jacob.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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
	private final JPanel recipePreview;
	
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
		selectButton = new JButton(new AbstractAction("Edit Recipe") {
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
		contentList.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					selectButton.doClick();
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
		recipePreview = setupPreviewPanel();
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
		contentList.addListSelectionListener(new RecipelistListener());
		mainPanel.setBackground(Color.LIGHT_GRAY);
		add(mainPanel);	
	}

	@Override
	public String getTitle() {
		return "Recipe List";
	}
	
	private JPanel setupPreviewPanel() {
		JPanel recipePreview = new JPanel(new BorderLayout());
		recipePreview.setPreferredSize(new Dimension(LIST_WIDTH, 340));
		recipePreview.setBackground(new Color(230, 230, 230));
		
		JLabel titleLabel = new JLabel();
		titleLabel.setName("titlelabel");
		titleLabel.setFont(new Font("Verdana", Font.PLAIN, 20)); 
		
		JLabel ingredients = new JLabel();
		ingredients.setName("ingredients");
		ingredients.setHorizontalAlignment(SwingConstants.LEFT);
		ingredients.setVerticalAlignment(SwingConstants.TOP);
		ingredients.setFont(new Font("Verdana", Font.PLAIN, 12));
		ingredients.setMinimumSize(new Dimension(50, 280));
		
		JTextArea contentLabel = new JTextArea();
		contentLabel.setName("contentlabel");
		contentLabel.setLineWrap(true);
		contentLabel.setWrapStyleWord(true);
		contentLabel.setFont(new Font("Verdana", Font.PLAIN, 12));
		contentLabel.setMinimumSize(new Dimension(100, 280));
		contentLabel.setBackground(new Color(230, 230, 230));
		contentLabel.setEditable(false);

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, ingredients, contentLabel);
	    splitPane.setDividerLocation(100);
		
		recipePreview.add(titleLabel, BorderLayout.NORTH);
		recipePreview.add(splitPane, BorderLayout.CENTER);

		return recipePreview;
	}
	
	/**
	 * Fill the preview with info from the object in the left list
	 * @author jacobflarup
	 *
	 */
	private class RecipelistListener implements ListSelectionListener {
		@Override
		public void valueChanged(ListSelectionEvent e) {			
			RecipeObject selected = contentList.getSelectedDataItem();
			
			if (recipePreview == null) {
				LOG.error("recipeView is null");

			}
			if (selected == null) {
				LOG.error("selected object is null");
				return;
			}
			
			String title = selected.getTitle();
			String content = selected.getContent();
			List<String> ingredients = selected.getIngredientList();
			
			Component[] comps = recipePreview.getComponents();
			List<Component> compList = new ArrayList<Component>();
 			for (int i = 0; i < comps.length; i++) {
 				Component comp = comps[i];
				if (comp instanceof JSplitPane) {
					Component[] cp = ((JSplitPane) comp).getComponents();
					for (int j = 0; j < cp.length; j++) {
						if (cp[j] instanceof JLabel || cp[j] instanceof JTextArea)
							compList.add(cp[j]);
					}
					
				} else if (comp instanceof JLabel) {
					compList.add(comp);
				}
			}
			
			
			for (int i = 0; i < compList.size(); i++) {
				Component comp = compList.get(i);	
				switch (comp.getName()) {
				case "titlelabel": {
					JLabel label = (JLabel)comp;
					label.setText(title);
					break;
				}
				case "ingredients": {
					StringBuilder sb = new StringBuilder();
					sb.append("<html>");
					for (int j = 0; j < ingredients.size(); j++) {
						if (ingredients.size() > 17 && j == 16) {
							sb.append("...");
							break;
						} else {
							String ing = ingredients.get(j);
							if (ing.length() > 13) {
								ing = ing.substring(0, 13) + "..";
							}
							sb.append(ing);
							sb.append("<br>");
						}
					}
					sb.append("</html>");
					JLabel area = (JLabel)comp;
					area.setText(sb.toString());
					break;
				}
				case "contentlabel": {
					JTextArea label = (JTextArea)comp;
					label.setText(content);
					break;
				}	
				default:
					LOG.error("Unknown or unhandled component in previewPanel");
					break;
				}
			}
		}
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