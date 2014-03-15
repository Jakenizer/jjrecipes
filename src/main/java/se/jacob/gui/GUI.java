package se.jacob.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
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

import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.jacob.Constants;
import se.jacob.listener.MainMenuListener;

public class GUI extends JFrame{
	
	private static final long serialVersionUID = 1L;
	private static Map<String, JComponent> components = new HashMap<>();
	private static Logger log = LoggerFactory.getLogger(GUI.class.getName());
	
	public GUI() {
		DOMConfigurator.configure("src/main/resources/log4j.xml");
		log.info("HEJ HEJ");
		
		setTitle("JJ Recipes " + Constants.version);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
		setLayout(new BorderLayout());
		
		//meny
		JMenuBar menuBar = new JMenuBar();
		JMenu menu1 = new JMenu("File");
		JMenuItem newRecipeItem = new JMenuItem("New Recipe");
		JMenuItem listRecipesItem = new JMenuItem("List Recipes");
		JMenuItem searchRecipesItem = new JMenuItem("Search Recipes");
		JMenuItem openRecipesItem = new JMenuItem("Open Recipe");
		JMenuItem quitItem = new JMenuItem("Quit");
		
        //snabbknappar
		newRecipeItem.setAccelerator(KeyStroke.getKeyStroke('n'));
		listRecipesItem.setAccelerator(KeyStroke.getKeyStroke('l'));
		searchRecipesItem.setAccelerator(KeyStroke.getKeyStroke('s'));
		openRecipesItem.setAccelerator(KeyStroke.getKeyStroke('o'));
		quitItem.setAccelerator(KeyStroke.getKeyStroke('q'));
		
		menu1.add(newRecipeItem);
		menu1.add(listRecipesItem);
		menu1.add(searchRecipesItem);
		menu1.add(openRecipesItem);
		menu1.add(quitItem);

		MainMenuListener mainListener = new MainMenuListener(this);
		newRecipeItem.addActionListener(mainListener);
		listRecipesItem.addActionListener(mainListener);
		openRecipesItem.addActionListener(mainListener);
		quitItem.addActionListener(mainListener);
		menuBar.add(menu1);
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
	
	public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
                          "javax.swing.plaf.metal.MetalLookAndFeel");
                        //  "com.sun.java.swing.plaf.motif.MotifLookAndFeel");
                        //UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
		new GUI();
	}
}

