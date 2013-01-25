/*
  Andrew Darwin
  www.adarwin.com
  January 2013
*/

package com.adarwin.pcbc;

import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

public class Card extends JPanel
{
  protected JLabel address1Label, address2Label;
  protected EditableJLabel address1Field, address2Field;
  protected ImagePanel imagePanel;
  protected SpringLayout layout;
  protected ArrayList<JComponent> componentList;
  protected JButton applyChangesButton;

  public Card()
  {
    setupCardComponents();
    populateComponentList();
    configureLayout();
    assembleCard();
  }
  protected String getSafeCardString(String inputString)
  {
    if (inputString == null || inputString.equalsIgnoreCase("null"))
    {
      return "";
    }
    else
    {
      return inputString;
    }
  }
  protected void setupCardComponents()
  {
    address1Label = new JLabel("Address Line 1:");
    address2Label = new JLabel("Address Line 2:");
    address1Field = new EditableJLabel("ADDRESS LINE 1");
    address2Field = new EditableJLabel("ADDRESS LINE 2");
    applyChangesButton = new JButton("Apply Changes");
    //applyChangesButton.setEnabled(false);
    imagePanel = new ImagePanel();
    layout = new SpringLayout();
    setLayout(layout);
  }
  protected void populateComponentList()
  {
    componentList = new ArrayList<JComponent>();
    componentList.add(address1Label);
    componentList.add(address2Label);
    componentList.add(address1Field);
    componentList.add(address2Field);
    componentList.add(imagePanel);
    componentList.add(applyChangesButton);
  }

  public void assembleCard()
  {
    for (JComponent component : componentList)
    {
      add(component);
    }
  }

  public void configureLayout()
  {
    layout.putConstraint(SpringLayout.NORTH, imagePanel, 20, SpringLayout.NORTH, this);
    layout.putConstraint(SpringLayout.WEST, imagePanel, 20, SpringLayout.WEST, this);

    layout.putConstraint(SpringLayout.NORTH, address1Label, 20, SpringLayout.SOUTH, imagePanel);
    layout.putConstraint(SpringLayout.WEST, address1Label, 0, SpringLayout.WEST, imagePanel);

    layout.putConstraint(SpringLayout.NORTH, address2Label, 5, SpringLayout.SOUTH, address1Label);
    layout.putConstraint(SpringLayout.EAST, address2Label, 0, SpringLayout.EAST, address1Label);
    layout.putConstraint(SpringLayout.WEST, address2Label, 0, SpringLayout.WEST, address1Label);

    layout.putConstraint(SpringLayout.NORTH, address1Field, 0, SpringLayout.NORTH, address1Label);
    layout.putConstraint(SpringLayout.SOUTH, address1Field, 0, SpringLayout.SOUTH, address1Label);
    layout.putConstraint(SpringLayout.WEST, address1Field, 5, SpringLayout.EAST, address1Label);

    layout.putConstraint(SpringLayout.NORTH, address2Field, 0, SpringLayout.NORTH, address2Label);
    layout.putConstraint(SpringLayout.SOUTH, address2Field, 0, SpringLayout.SOUTH, address2Label);
    layout.putConstraint(SpringLayout.WEST, address2Field, 5, SpringLayout.EAST, address2Label);

    layout.putConstraint(SpringLayout.SOUTH, applyChangesButton, -10, SpringLayout.SOUTH, this);
    layout.putConstraint(SpringLayout.EAST, applyChangesButton, -10, SpringLayout.EAST, this);
  }
}
