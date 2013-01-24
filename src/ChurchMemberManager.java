/*
  Andrew Darwin
  www.adarwin.com
  January 2013
*/

package com.adarwin.pcbc;

import com.adarwin.logging.Logbook;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.FlowLayout;
import java.util.ArrayList;
import javax.swing.border.EtchedBorder;
import javax.swing.DefaultListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class ChurchMemberManager
{
  private static JFrame mainFrame;
  private static JTextField searchBox;
  private static SpringLayout headerLayout, mainContentLayout;
  private static JPanel header, memberBrowsePanel, familyBrowsePanel;
  private static JTabbedPane mainTabbedPane;
  private static JScrollPane memberListScrollPane, familyListScrollPane;
  private static JList<String> memberJList, familyJList;
  private static DefaultListModel<String> memberListModel, familyListModel;
  private static JMenuBar menuBar;
  private static JMenu fileMenu, editMenu, databaseMenu, toolsMenu, helpMenu;
  private static JMenuItem openMenuItem, saveMenuItem, exitMenuItem;
  private static JMenuItem optionsMenuItem;

  private static MySQLAccess dbAccess;
  private static String dbName, username, password;

  private static final int startingWidth = 800;
  private static final int startingHeight = 600;
  private static final String applicationName = "Church Member Manager";

  private static void createAndShowGUI()
  {
    initializePeripherals();
    initializeLayouts();
    initializeComponents();
    initializeContainers();
    defineListeners();
    configureLayouts();
    assembleGUI();
    loadDatabase();
  }

  private static void initializeLayouts()
  {
    headerLayout = new SpringLayout();
  }

  private static void initializeContainers()
  {
    mainFrame = new JFrame(applicationName);
    mainFrame.setSize(new Dimension(startingWidth, startingHeight));
    mainFrame.setLocationRelativeTo(null);
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainFrame.setLayout(new BorderLayout());

    header = new JPanel();
    header.setLayout(headerLayout);
    header.setPreferredSize(new Dimension(startingWidth, startingHeight/15));

    memberBrowsePanel = new JPanel();
    memberBrowsePanel.setLayout(new BorderLayout());

    familyBrowsePanel = new JPanel();
    familyBrowsePanel.setLayout(new BorderLayout());

    mainTabbedPane = new JTabbedPane();

    memberListScrollPane = new JScrollPane(memberJList);
    familyListScrollPane = new JScrollPane(familyJList);
    memberListScrollPane.setPreferredSize(new Dimension(200, startingHeight));
    familyListScrollPane.setPreferredSize(new Dimension(200, startingHeight));

  }

  private static void initializeComponents()
  {
    menuBar = new JMenuBar();
    fileMenu = new JMenu("File");
    toolsMenu = new JMenu("Tools");
    exitMenuItem = new JMenuItem("Exit");
    optionsMenuItem = new JMenuItem("Options");

    searchBox = new JTextField("Search");
    searchBox.setForeground(Color.gray);
    searchBox.setPreferredSize(new Dimension(150, 20));

    memberJList = new JList<String>(memberListModel);

    familyJList = new JList<String>(familyListModel);
  }

  private static void showStartScreen()
  {
    final JFrame startFrame = new JFrame("Welcome");
    startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    startFrame.setMinimumSize(new Dimension(370, 160));
    JLabel dbNameLabel = new JLabel("Database Name:");
    JLabel userLabel = new JLabel("Username:");
    JLabel passwordLabel = new JLabel("Password:");
    dbNameLabel.setHorizontalAlignment(SwingConstants.LEFT);
    userLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    passwordLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    final JTextField dbNameField = new JTextField("PantonChurch");
    //dbNameField.setPreferredSize(new Dimension(100, 30));
    final JTextField userField = new JTextField("andrew");
    //userField.setPreferredSize(new Dimension(100, 30));
    final JPasswordField passwordField = new JPasswordField();
    //passwordField.setPreferredSize(new Dimension(100, 30));
    JButton connectButton = new JButton("Connect");
    JButton cancelButton = new JButton("Cancel");
    JCheckBox alwaysCheckBox = new JCheckBox("Always use these login credentials");

    connectButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        dbName = dbNameField.getText();
        username = userField.getText();
        password = new String(passwordField.getPassword());
        startFrame.dispose();
        createAndShowGUI();
      }
    });
    cancelButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        //startFrame.dispose();
        System.exit(0);
      }
    });

    Container contentPane = startFrame.getContentPane();

    int m = 5; //m = margin
    SpringLayout layout = new SpringLayout();
    layout.putConstraint(SpringLayout.NORTH, dbNameLabel, 2*m, SpringLayout.NORTH, contentPane);
    layout.putConstraint(SpringLayout.SOUTH, dbNameLabel, 0, SpringLayout.SOUTH, dbNameField);
    layout.putConstraint(SpringLayout.WEST, dbNameLabel, m, SpringLayout.WEST, contentPane);

    layout.putConstraint(SpringLayout.NORTH, dbNameField, 2*m, SpringLayout.NORTH, contentPane);
    layout.putConstraint(SpringLayout.EAST, dbNameField, -1*m, SpringLayout.EAST, contentPane);
    layout.putConstraint(SpringLayout.WEST, dbNameField, m, SpringLayout.EAST, dbNameLabel);

    layout.putConstraint(SpringLayout.NORTH, userLabel, 2*m, SpringLayout.SOUTH, dbNameLabel);
    layout.putConstraint(SpringLayout.SOUTH, userLabel, 0, SpringLayout.SOUTH, userField);
    layout.putConstraint(SpringLayout.EAST, userLabel, 0, SpringLayout.EAST, dbNameLabel);
    layout.putConstraint(SpringLayout.WEST, userLabel, m, SpringLayout.WEST, contentPane);

    layout.putConstraint(SpringLayout.NORTH, userField, 2*m, SpringLayout.SOUTH, dbNameField);
    layout.putConstraint(SpringLayout.EAST, userField, -1*m, SpringLayout.EAST, contentPane);
    layout.putConstraint(SpringLayout.WEST, userField, m, SpringLayout.EAST, userLabel);

    layout.putConstraint(SpringLayout.NORTH, passwordLabel, 2*m, SpringLayout.SOUTH, userLabel);
    layout.putConstraint(SpringLayout.SOUTH, passwordLabel, 0, SpringLayout.SOUTH, passwordField);
    layout.putConstraint(SpringLayout.EAST, passwordLabel, 0, SpringLayout.EAST, userLabel);
    layout.putConstraint(SpringLayout.WEST, passwordLabel, m, SpringLayout.WEST, contentPane);

    layout.putConstraint(SpringLayout.NORTH, passwordField, 2*m, SpringLayout.SOUTH, userField);
    layout.putConstraint(SpringLayout.EAST, passwordField, -1*m, SpringLayout.EAST, contentPane);
    layout.putConstraint(SpringLayout.WEST, passwordField, m, SpringLayout.EAST, passwordLabel);

    layout.putConstraint(SpringLayout.SOUTH, cancelButton, -1*m, SpringLayout.SOUTH, contentPane);
    layout.putConstraint(SpringLayout.EAST, cancelButton, -1*m, SpringLayout.EAST, contentPane);

    layout.putConstraint(SpringLayout.SOUTH, connectButton, -1*m, SpringLayout.SOUTH, contentPane);
    layout.putConstraint(SpringLayout.EAST, connectButton, -1*m, SpringLayout.WEST, cancelButton);

    layout.putConstraint(SpringLayout.SOUTH, alwaysCheckBox, -1*m, SpringLayout.SOUTH, contentPane);
    layout.putConstraint(SpringLayout.WEST, alwaysCheckBox, m, SpringLayout.WEST, contentPane);

    contentPane.setLayout(layout);
    contentPane.add(dbNameLabel);
    contentPane.add(dbNameField);
    contentPane.add(userLabel);
    contentPane.add(userField);
    contentPane.add(passwordLabel);
    contentPane.add(passwordField);
    contentPane.add(connectButton);
    contentPane.add(cancelButton);
    contentPane.add(alwaysCheckBox);

    startFrame.setLocationRelativeTo(null);
    startFrame.setVisible(true);
    startFrame.pack();
  }

  private static void initializePeripherals()
  {
    memberListModel = new DefaultListModel<String>();
    familyListModel = new DefaultListModel<String>();
  }

  private static void defineListeners()
  {
    exitMenuItem.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        //mainFrame.dispose();
        System.exit(0);
      }
    });
    searchBox.addFocusListener(new FocusListener()
    {
      public void focusGained(FocusEvent e)
      {
        searchBox.setText("");
        searchBox.setForeground(Color.black);
      }
      public void focusLost(FocusEvent e)
      {
        searchBox.setText("Search");
        searchBox.setForeground(Color.gray);
      }
    });
    memberJList.addListSelectionListener(new ListSelectionListener()
    {
      public void valueChanged(ListSelectionEvent e)
      {
        String selection = memberJList.getSelectedValue();
        String key = ", ";
        String firstName = selection.substring(0, selection.indexOf(key));
        String lastName = selection.substring(selection.indexOf(key) + key.length());
        Member selectedMember = dbAccess.getMember(lastName, firstName);
      }
    });
  }

  private static void configureLayouts()
  {
    int m = 5; //m = margin
    headerLayout.putConstraint(SpringLayout.NORTH, searchBox,  m, SpringLayout.NORTH, header);
    headerLayout.putConstraint(SpringLayout.EAST, searchBox,  -1*m, SpringLayout.EAST, header);
  }

  private static void assembleGUI()
  {
    Container contentPane = mainFrame.getContentPane();
    fileMenu.add(exitMenuItem);
    toolsMenu.add(optionsMenuItem);
    
    menuBar.add(fileMenu);
    menuBar.add(toolsMenu);

    mainFrame.setJMenuBar(menuBar);

    header.add(searchBox);

    memberBrowsePanel.add(memberListScrollPane, BorderLayout.WEST);
    familyBrowsePanel.add(familyListScrollPane, BorderLayout.WEST);

    mainTabbedPane.addTab("Members", memberBrowsePanel);
    mainTabbedPane.addTab("Families", familyBrowsePanel);

    contentPane.add(header, BorderLayout.NORTH);
    contentPane.add(mainTabbedPane, BorderLayout.CENTER);
    mainFrame.setVisible(true);
  }

  private static void loadDatabase()
  {
    dbAccess = new MySQLAccess(dbName, username, password);
    dbAccess.loadDatabase();
    ArrayList<String> members = dbAccess.getMembersList();
    for (String member : members)
    {
      memberListModel.addElement(member);
    }
    /*
    String lastFirst = members.get(5);
    dbAccess.getMember(lastFirst.substring(0, lastFirst.indexOf(",")),
                       lastFirst.substring(lastFirst.indexOf(", ") + 2));
    */

    ArrayList<String> families = dbAccess.getFamiliesList();
    for (String family : families)
    {
      System.out.println(family);
      familyListModel.addElement(family);
    }
  }
  public static void main(String[] args)
  {
    try
    {
      UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
    }
    catch (UnsupportedLookAndFeelException ex)
    {
    }
    catch (ClassNotFoundException ex)
    {
    }
    catch (InstantiationException ex)
    {
    }
    catch (IllegalAccessException ex)
    {
    }
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        showStartScreen();
        //createAndShowGUI();
      }
    });
    /*
    MySQLAccess dbAccess = new MySQLAccess();
    dbAccess.loadDatabase();
    */
  }
}
