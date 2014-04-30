package se.jacob.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.jacob.Constants;
import se.jacob.listener.MainMenuListener;

public class GUI extends JFrame{
	
	private static final long serialVersionUID = 1L;
	private static Map<String, JComponent> components = new HashMap<>();
	private static Logger log = LoggerFactory.getLogger(GUI.class);
	
	private static Container mainFrame;
	 
	public GUI() {
		DOMConfigurator.configure("src/main/resources/log4j.xml");
		
		mainFrame = this.getContentPane();
		
		setTitle("JJ Recipes " + Constants.version);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
		setLayout(new BorderLayout());
		
		//meny
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem newRecipeItem = new JMenuItem("New Recipe");
		JMenuItem listRecipesItem = new JMenuItem("List Recipes");
		//JMenuItem searchRecipesItem = new JMenuItem("Search Recipes");
		JMenuItem openRecipesItem = new JMenuItem("Search Recipe");
		JMenuItem quitItem = new JMenuItem("Quit");
		
		fileMenu.add(newRecipeItem);
		fileMenu.add(listRecipesItem);
		//menu1.add(searchRecipesItem);
		fileMenu.add(openRecipesItem);
		fileMenu.add(quitItem);
		
		JMenu advancedMenu = new JMenu("Advanced");
		JMenuItem validateXMLIds = new JMenuItem("Validate: Unique XML ids");
		advancedMenu.add(validateXMLIds);

		MainMenuListener mainListener = new MainMenuListener();
		newRecipeItem.addActionListener(mainListener);
		listRecipesItem.addActionListener(mainListener);
		openRecipesItem.addActionListener(mainListener);
		quitItem.addActionListener(mainListener);
		validateXMLIds.addActionListener(mainListener);
		
        //snabbknappar
		newRecipeItem.setAccelerator(KeyStroke.getKeyStroke('n'));
		listRecipesItem.setAccelerator(KeyStroke.getKeyStroke('l'));
		//searchRecipesItem.setAccelerator(KeyStroke.getKeyStroke('s'));
		openRecipesItem.setAccelerator(KeyStroke.getKeyStroke('o'));
		quitItem.setAccelerator(KeyStroke.getKeyStroke('q'));
		validateXMLIds.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.SHIFT_DOWN_MASK));
		
		menuBar.add(fileMenu);
		menuBar.add(advancedMenu);
		setJMenuBar(menuBar);
		
		//tabbar
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		
		add(tabbedPane, BorderLayout.CENTER);
		
		registerComponent("TabbedPane", tabbedPane);
		setVisible(true);
	}
	
	public static void registerComponent(String name, JComponent c) {
		components.put(name, c);
	}
	
	public static void unregisterComponent(String name) {
		components.remove(name);
	}
	
	public static JComponent getComponent(String name) {
		return components.get(name);
	}
	
	/**
	 * For logging
	 */
	protected void setupRollingfileAppender() {
		org.apache.log4j.Logger rootLogger = org.apache.log4j.Logger.getRootLogger();
		
		//Define log pattern layout
		PatternLayout layout = new PatternLayout("%d{ABSOLUTE} %-5p [%c{1}] %m%n"); 
		//("%d{ISO8601} [%t] %-5p %c %x - %m%n");
		
		try
		{
		//Define file appender with layout and output log file name
		String s = System.getProperty("user.dir") + "/jjrecipes.log";
		RollingFileAppender fileAppender = new RollingFileAppender(layout, s);
		fileAppender.setMaxFileSize("100KB");
		fileAppender.setThreshold(Level.DEBUG);
		fileAppender.setAppend(true);
		//Add the appender to root logger
		rootLogger.addAppender(fileAppender);
		}
		catch (IOException e)
		{
			
		}
		log.info("Rollingfileappender added");
	}
	
	public static Container getMainFrame() {
		return mainFrame;
	}
	
	public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
                          "javax.swing.plaf.metal.MetalLookAndFeel");
                        //  "com.sun.java.swing.plaf.motif.MotifLookAndFeel");
                        //UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
		GUI gui = new GUI();
		gui.setupRollingfileAppender();
	}
}