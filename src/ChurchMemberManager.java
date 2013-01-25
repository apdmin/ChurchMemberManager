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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
import javax.swing.JSplitPane;
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
  protected static Logbook logbook;
  private static JFrame mainFrame;
  private static JTextField searchBox;
  private static SpringLayout headerLayout, mainContentLayout;
  private static JPanel header, memberBrowsePanel, familyBrowsePanel;
  private static JTabbedPane mainTabbedPane;
  private static JScrollPane memberListScrollPane, familyListScrollPane, searchListScrollPane;
  private static JSplitPane memberSplitPane, familySplitPane, searchSplitPane;
  private static JList<String> memberJList, familyJList, searchResultsJList;
  private static DefaultListModel<String> memberListModel, familyListModel, searchListModel;
  private static JMenuBar menuBar;
  private static JMenu fileMenu, editMenu, databaseMenu, toolsMenu, helpMenu;
  private static JMenuItem openMenuItem, saveMenuItem, exitMenuItem;
  private static JMenuItem optionsMenuItem;
  private static MemberCard memberCard;
  private static FamilyCard familyCard;

  protected static MySQLAccess dbAccess;

  private static ConnectionInfo connectionInfo;

  private static final int startingWidth = 800;
  private static final int startingHeight = 600;
  private static final String applicationName = "Church Member Manager";






  public static void main(String[] args)
  {
    logbook = new Logbook("ChurchMemberManager.log");
    //Set the look and feel for this application.
    try
    {
      UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
    }
    //If we encounter an exception, the jvm should default to the native look and feel,
    //which is just fine.
    catch (UnsupportedLookAndFeelException ex) { logbook.log(ex); }
    catch (ClassNotFoundException ex) { logbook.log(ex); }
    catch (InstantiationException ex) { logbook.log(ex); }
    catch (IllegalAccessException ex) { logbook.log(ex); }

    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        //Starting the application inside this runnable object ensures that we are
        //running from the event queue. :-)
        //showStartScreen();
        startApplication();
      }
    });
  }






  private static void startApplication()
  {
    if (connectionInfo == null)
    {
      // Try to deserialize it
      deserializeConnectionInfo();
    }
    if (connectionInfo == null)
    {
      // If it's still null, provide a way to initialize it
      showStartScreen();
    }
    else
    {
      createAndShowGUI();
    }
  }
  



  private static void serializeConnectionInfo()
  {
    FileOutputStream fileOut = null;
    ObjectOutputStream objectOut = null;
    try
    {
      fileOut = new FileOutputStream("ConnectionInfo.ci");
      objectOut = new ObjectOutputStream(fileOut);
      objectOut.writeObject(connectionInfo);
    }
    catch (IOException ex)
    {
      logbook.log(ex);
    }
    finally
    {
      try
      {
        if (objectOut != null) objectOut.close();
        if (fileOut != null) fileOut.close();
      }
      catch (IOException ex)
      {
        logbook.log(ex);
        logbook.log(Logbook.ERROR, "Unable to close output streams for some weird reason.");
      }
    }
  }
  private static void deserializeConnectionInfo()
  {
    FileInputStream fileIn = null;
    ObjectInputStream objectIn = null;
    try
    {
      fileIn = new FileInputStream("ConnectionInfo.ci");
      objectIn = new ObjectInputStream(fileIn);
      connectionInfo = (ConnectionInfo) objectIn.readObject();
    }
    catch (IOException ex)
    {
      logbook.log(ex);
    }
    catch (ClassNotFoundException ex)
    {
      logbook.log(ex);
    }
    finally
    {
      try
      {
        if (objectIn != null) objectIn.close();
        if (fileIn != null) fileIn.close();
      }
      catch (IOException ex)
      {
        logbook.log(ex);
        logbook.log(Logbook.ERROR, "Unable to close input streams for some weird reason.");
      }
    }
  }




  private static void showStartScreen()
  {
    //Initialize containers and components for the start screen
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
    final JTextField userField = new JTextField("root");
    final JPasswordField passwordField = new JPasswordField();
    JButton connectButton = new JButton("Connect");
    JButton cancelButton = new JButton("Cancel");
    final JCheckBox alwaysCheckBox = new JCheckBox("Always use these login credentials");

    //Define any listeners the start screen components need
    connectButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        connectionInfo = new ConnectionInfo();
        connectionInfo.setDatabaseName(dbNameField.getText());
        connectionInfo.setUsername(userField.getText());
        connectionInfo.setPassword(new String(passwordField.getPassword()));
        startFrame.dispose();
        if (alwaysCheckBox.isSelected())
        {
          serializeConnectionInfo();
        }
        createAndShowGUI();
      }
    });

    cancelButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        System.exit(0);
      }
    });

    //Configure the layout for the start screen
    //Note that contentPane is used here to help define the layout and then is used later
    //as the container for all components in this window.
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

    //Add the components to the content pane
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

    //Position the startwindow in the center of the screen and make it visible
    startFrame.setLocationRelativeTo(null);
    startFrame.setVisible(true);
  }








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
    header.setPreferredSize(new Dimension(startingWidth, 25));

    //memberBrowsePanel = new JPanel();
    //memberBrowsePanel.setLayout(new BorderLayout());

    //familyBrowsePanel = new JPanel();
    //familyBrowsePanel.setLayout(new BorderLayout());

    mainTabbedPane = new JTabbedPane();

    memberSplitPane = new JSplitPane();
    familySplitPane = new JSplitPane();
    searchSplitPane = new JSplitPane();

    memberListScrollPane = new JScrollPane(memberJList);
    familyListScrollPane = new JScrollPane(familyJList);
    searchListScrollPane = new JScrollPane(searchResultsJList);
    /*
    memberListScrollPane.setPreferredSize(new Dimension(200, startingHeight));
    familyListScrollPane.setPreferredSize(new Dimension(200, startingHeight));
    */

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
    searchResultsJList = new JList<String>(searchListModel);

    memberCard = new MemberCard();
    familyCard = new FamilyCard();
  }









  private static void initializePeripherals()
  {
    memberListModel = new DefaultListModel<String>();
    familyListModel = new DefaultListModel<String>();
    searchListModel = new DefaultListModel<String>();
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
        if (!e.getValueIsAdjusting() && memberJList.getSelectedValue() != null)
        {
          String selection = memberJList.getSelectedValue();
          String key = ", ";
          String lastName = selection.substring(0, selection.indexOf(key));
          String firstName = selection.substring(selection.indexOf(key) + key.length());
          Member selectedMember = dbAccess.getMember(lastName, firstName);
          memberCard.setMember(selectedMember);
          System.out.println(selectedMember);
        }
      }
    });
  }






  private static void configureLayouts()
  {
    int m = 5; //m = margin
    headerLayout.putConstraint(SpringLayout.NORTH, searchBox,  m, SpringLayout.NORTH, header);
    headerLayout.putConstraint(SpringLayout.EAST, searchBox,  -1*m, SpringLayout.EAST, header);

    //memberCard.configureLayout();
    //familyCard.configureLayout();
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

    //memberBrowsePanel.add(memberListScrollPane, BorderLayout.WEST);
    //familyBrowsePanel.add(familyListScrollPane, BorderLayout.WEST);
    //memberCard.assembleCard();
    //familyCard.assembleCard();

    memberSplitPane.setLeftComponent(memberListScrollPane);
    memberSplitPane.setRightComponent(memberCard);
    familySplitPane.setLeftComponent(familyListScrollPane);
    familySplitPane.setRightComponent(familyCard);
    searchSplitPane.setLeftComponent(searchListScrollPane);

    mainTabbedPane.addTab("Members", memberSplitPane);
    mainTabbedPane.addTab("Families", familySplitPane);
    mainTabbedPane.addTab("Search Results", searchSplitPane);

    contentPane.add(header, BorderLayout.NORTH);
    contentPane.add(mainTabbedPane, BorderLayout.CENTER);
    mainFrame.setVisible(true);
  }






  private static void loadDatabase()
  {
    dbAccess = new MySQLAccess(connectionInfo.getDatabaseName(),
                               connectionInfo.getUsername(),
                               connectionInfo.getPassword());
    dbAccess.loadDatabase();
    reloadMemberList();
    reloadFamilyList();
  }

  protected static void reloadMemberList()
  {
    memberListModel.clear();
    ArrayList<String> members = dbAccess.getMembersList();
    for (String member : members)
    {
      memberListModel.addElement(member);
    }
  }
  protected static void reloadFamilyList()
  {
    familyListModel.clear();
    ArrayList<String> families = dbAccess.getFamiliesList();
    for (String family : families)
    {
      familyListModel.addElement(family);
    }
  }





}
