/*
  Andrew Darwin
  www.adarwin.com
  January 2013
*/

package com.adarwin.pcbc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SpringLayout;

public class MemberCard extends Card
{
  private Member member;
  private JLabel firstNameLabel, lastNameLabel, dateOfBirthLabel, emailLabel;
  private EditableJLabel firstNameField, lastNameField, dateOfBirthField, emailField;

  public MemberCard()
  {
    this(null);
  }
  public MemberCard(Member member)
  {
    super();
  }
  



  public void setMember(Member newMember)
  {
    member = newMember;
    firstNameField.setInitialText(member.getFirstName());
    lastNameField.setInitialText(member.getLastName());
    dateOfBirthField.setInitialText(member.getDateOfBirthString());
    emailField.setInitialText(member.getEmail());
    address1Field.setInitialText(member.getAddressLine1());
    address2Field.setInitialText(member.getAddressLine2());
    //repaint();
  }



  @Override
  protected void setupCardComponents()
  {
    this.member = member;
    super.setupCardComponents();
    firstNameLabel = new JLabel("First Name:");
    lastNameLabel = new JLabel("Last Name:");
    dateOfBirthLabel = new JLabel("Date Of Birth:");
    emailLabel = new JLabel("Email:");
    firstNameField = new EditableJLabel("FIRST NAME HERE");
    lastNameField = new EditableJLabel("LAST NAME HERE");
    dateOfBirthField = new EditableJLabel("DOB HERE");
    emailField = new EditableJLabel("EMAIL ADDRESS HERE");
    applyChangesButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        member.setFirstName(firstNameField.getText());
        member.setLastName(lastNameField.getText());
        member.setDateOfBirthString(dateOfBirthField.getText());
        member.setEmail(emailField.getText());
        ChurchMemberManager.dbAccess.updateMember(member);
        ChurchMemberManager.reloadMemberList();
      }
    });
    if (member != null)
    {
      firstNameField.setText(member.getFirstName());
      lastNameField.setText(member.getLastName());
      dateOfBirthField.setText(member.getDateOfBirth().toString());
      emailField.setText(member.getEmail());
    }
  }
  @Override
  protected void populateComponentList()
  {
    super.populateComponentList();
    componentList.add(firstNameLabel);
    componentList.add(lastNameLabel);
    componentList.add(dateOfBirthLabel);
    componentList.add(emailLabel);
    componentList.add(firstNameField);
    componentList.add(lastNameField);
    componentList.add(dateOfBirthField);
    componentList.add(emailField);
  }



  @Override
  public void configureLayout()
  {
    super.configureLayout();
    int m = 5; //m = margin
    layout.putConstraint(SpringLayout.NORTH, firstNameLabel, m, SpringLayout.NORTH, imagePanel);
    layout.putConstraint(SpringLayout.EAST, firstNameLabel, 0, SpringLayout.EAST, address1Label);

    layout.putConstraint(SpringLayout.NORTH, firstNameField, 0, SpringLayout.NORTH, firstNameLabel);
    layout.putConstraint(SpringLayout.SOUTH, firstNameField, 0, SpringLayout.SOUTH, firstNameLabel);
    layout.putConstraint(SpringLayout.WEST, firstNameField, m, SpringLayout.EAST, firstNameLabel);
    
    layout.putConstraint(SpringLayout.NORTH, lastNameLabel, m, SpringLayout.SOUTH, firstNameLabel);
    layout.putConstraint(SpringLayout.EAST, lastNameLabel, 0, SpringLayout.EAST, firstNameLabel);
    
    layout.putConstraint(SpringLayout.NORTH, lastNameField, 0, SpringLayout.NORTH, lastNameLabel);
    layout.putConstraint(SpringLayout.SOUTH, lastNameField, 0, SpringLayout.SOUTH, lastNameLabel);
    layout.putConstraint(SpringLayout.WEST, lastNameField, m, SpringLayout.EAST, lastNameLabel);

    layout.putConstraint(SpringLayout.NORTH, dateOfBirthLabel, m, SpringLayout.SOUTH, lastNameLabel);
    layout.putConstraint(SpringLayout.EAST, dateOfBirthLabel, 0, SpringLayout.EAST, firstNameLabel);

    layout.putConstraint(SpringLayout.NORTH, dateOfBirthField, 0, SpringLayout.NORTH, dateOfBirthLabel);
    layout.putConstraint(SpringLayout.SOUTH, dateOfBirthField, 0, SpringLayout.SOUTH, dateOfBirthLabel);
    layout.putConstraint(SpringLayout.WEST, dateOfBirthField, m, SpringLayout.EAST, dateOfBirthLabel);

    layout.putConstraint(SpringLayout.NORTH, emailLabel, m, SpringLayout.SOUTH, dateOfBirthLabel);
    layout.putConstraint(SpringLayout.EAST, emailLabel, 0, SpringLayout.EAST, firstNameLabel);

    layout.putConstraint(SpringLayout.NORTH, emailField, 0, SpringLayout.NORTH, emailLabel);
    layout.putConstraint(SpringLayout.SOUTH, emailField, 0, SpringLayout.SOUTH, emailLabel);
    layout.putConstraint(SpringLayout.WEST, emailField, m, SpringLayout.EAST, emailLabel);

    layout.putConstraint(SpringLayout.NORTH, address1Label, m, SpringLayout.SOUTH, emailLabel);
    //layout.putConstraint(SpringLayout.WEST, address1Label, 0, SpringLayout.WEST, lastNameLabel);
  }
}
