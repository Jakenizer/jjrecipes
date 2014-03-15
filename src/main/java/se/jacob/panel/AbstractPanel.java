package se.jacob.panel;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jgoodies.looks.common.RGBGrayFilter;

public abstract class AbstractPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	protected JTabbedPane parent;
	private Icon CLOSE_TAB_ICON;
	private Logger log = LoggerFactory.getLogger(getClass());
	protected JLabel lblTitle;
	
//	protected String theTitle;
	
	public AbstractPanel(JTabbedPane parent) {
		this.parent = parent;
		setBackground(Color.LIGHT_GRAY);
		URL url = getClass().getResource("/closeTabButton.png");
		//CLOSE_TAB_ICON = new ImageIcon(getClass().getResource("/closeTabButton.png"));
		if (url == null) {
			log.error("closeTabButton.jpg not found in src/main/resources");
			throw new IllegalArgumentException("closeTabButton.png cannot be found");
		} 
			
		CLOSE_TAB_ICON = new ImageIcon(url);
		
//		this.theTitle = getTitle();
	}
	
	public void addClosableTab() {		
		// Add the tab to the pane without any label
		parent.addTab(null, this);
		int pos = parent.indexOfComponent(this);

		// Create a FlowLayout that will space things 5px apart
		FlowLayout f = new FlowLayout(FlowLayout.CENTER, 5, 0);

		// Make a small JPanel with the layout and make it non-opaque
		JPanel pnlTab = new JPanel(f);
		pnlTab.setOpaque(false);

		// Add a JLabel with title and the left-side tab icon
		lblTitle = new JLabel();
		setTitle();


		// Create a JButton for the close tab button
		JButton btnClose = new JButton();
		btnClose.setOpaque(false);

		// Configure icon and rollover icon for button
		btnClose.setRolloverIcon(CLOSE_TAB_ICON);
		btnClose.setRolloverEnabled(true);
		btnClose.setIcon(RGBGrayFilter.getDisabledIcon(btnClose, CLOSE_TAB_ICON));

		// Set border null so the button doesn't make the tab too big
		btnClose.setBorder(null);

		// Make sure the button can't get focus, otherwise it looks funny
		btnClose.setFocusable(false);

		// Put the panel together
		pnlTab.add(lblTitle);
		pnlTab.add(btnClose);

		// Add a thin border to keep the image below the top edge of the tab
		// when the tab is selected
		pnlTab.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));

		// Now assign the component for the tab
		parent.setTabComponentAt(pos, pnlTab);

		// Add the listener that removes the tab
		final JComponent that = this;
		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.remove(that);
				//FileHandler.saveToFile("leif", "det var en gång liten pojk som hette leif och han var smart.");
			}
		};
		btnClose.addActionListener(listener);

		// Optionally bring the new tab to the front
		parent.setSelectedComponent(this);
	}
	
	public String getUnSavedTitle() {
		return getTitle() + "*";
	}
	
	public abstract String getTitle();
	
	/**
	 * Override
	 */
	protected void setTitle() {
		String theTitle = getTitle();
		if (theTitle == null)
			theTitle = "[no title]";
		
		String output = theTitle;
		if (theTitle.length() > 15)
			output = theTitle.substring(0, 13) + "..";
			
		lblTitle.setText(output);
		lblTitle.setToolTipText(theTitle);
	}
	
	
	//TODO: set focus order traversal policy abstract method
}
