package se.jacob.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;


public abstract class CommonRecipeView extends AbstractPanel{

	private static final long serialVersionUID = 1L;
	protected final JLabel nameLabel;
	protected final JTextField nameField;
	protected final JTextArea recipeText;
	protected final JLabel ingredientLabel;
	protected final JTextField ingredientField;
	protected final JLabel recipeLabel;
	protected final JButton saveButton;
	protected final JButton ingredientButton;
	protected final JButton removeIngredientButton;
	protected final DefaultListModel<String> listModel;
	protected final JList<String> ingredientList;

	public CommonRecipeView(JTabbedPane parent) throws FileNotFoundException {
		super(parent);
		setBackground(Color.LIGHT_GRAY);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel mainPanel = new JPanel();
		
		nameLabel = new JLabel("Recipe: ");
		nameField = new JTextField();
		nameField.setPreferredSize(new Dimension(500, 20));
		nameField.setDocument(new TextFieldLimiter(20, false));
		
		recipeLabel = new JLabel("Text: ");
		recipeText = new JTextArea();
		recipeText.setLineWrap( true );
		recipeText.setWrapStyleWord( true );
		recipeText.setPreferredSize(new Dimension(500, 300));
		recipeText.setDocument(new TextAreaLimiter(200, 10, false));
		
		ingredientLabel = new JLabel("New ingredient: ");
		ingredientField = new JTextField();
		ingredientField.setPreferredSize(new Dimension(250, 20));
		ingredientField.setDocument(new TextFieldLimiter(30, false));
		ingredientField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addIngredientToList();
			}
		});
		
		//ingredienslistan		
		listModel = new DefaultListModel<>();
		ingredientList = new JList<>(listModel);
		ingredientList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ingredientList.setSelectedIndex(0);
		ingredientList.setVisibleRowCount(10);
        JScrollPane listScrollPane = new JScrollPane(ingredientList);
		listScrollPane.setPreferredSize(new Dimension(255, 200));
		
		ingredientButton = new JButton("Add");
		ingredientButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addIngredientToList();
			}
		});
		
		removeIngredientButton = new JButton("Remove");
		removeIngredientButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedIndex = ingredientList.getSelectedIndex();
				if (selectedIndex >= 0) {
					listModel.remove(selectedIndex);
					//TODO: set the new selected index
				}
			}
		});
		
		saveButton = new JButton("Save");
		saveButton.addActionListener(getSaveActionListener());
		
		
		GroupLayout layout = new GroupLayout(mainPanel);
		mainPanel.setLayout(layout);
		mainPanel.setAlignmentX(LEFT_ALIGNMENT);
        
		layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(nameLabel)  
                .addComponent(recipeLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)
             )           
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(nameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)
                .addComponent(recipeText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)
                .addComponent(saveButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)
             )
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            	.addComponent(ingredientLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE) 
            )
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            		.addComponent(ingredientField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                            GroupLayout.PREFERRED_SIZE)
                    .addComponent(listScrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                            GroupLayout.PREFERRED_SIZE)
            )
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            		.addComponent(ingredientButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                            GroupLayout.PREFERRED_SIZE)
                    .addComponent(removeIngredientButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                            GroupLayout.PREFERRED_SIZE)
            		
            )
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(nameLabel)
                .addComponent(nameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)
                .addComponent(ingredientLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)
                .addComponent(ingredientField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                            GroupLayout.PREFERRED_SIZE)
                .addComponent(ingredientButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                            GroupLayout.PREFERRED_SIZE)
             )
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            	.addComponent(recipeLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)	
                .addComponent(recipeText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)
                .addComponent(listScrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)
                .addComponent(removeIngredientButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                            GroupLayout.PREFERRED_SIZE)
            	
             )
             .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            	.addComponent(saveButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)
             )
        );   
		
		mainPanel.setBackground(Color.LIGHT_GRAY);
		add(mainPanel);		
	}
	
	private void addIngredientToList() {
		String newIngredient = ingredientField.getText().trim();
		if (newIngredient == null || newIngredient.length() < 1)
			return;
		
		int maxIndex = listModel.getSize();
		listModel.add(maxIndex, newIngredient);
		ingredientList.setSelectedIndex(maxIndex);
		ingredientList.ensureIndexIsVisible(maxIndex);		
		ingredientField.setText("");	
	}

	protected abstract ActionListener getSaveActionListener();
	
	@Override
	protected void setTitle() {
		super.setTitle();
		String t = getTitle();
		if (t == null)
			t = "";
		nameField.setText(t);
	}
	
}
