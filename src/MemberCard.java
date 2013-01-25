/*
  Andrew Darwin
  www.adarwin.com
  January 2013
*/

package com.adarwin.pcbc;

import com.adarwin.logging.Logbook;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SpringLayout;

public class MemberCard extends Card
{
  private Member member;
  private JLabel firstNameLabel, middleNameLabel, lastNameLabel, dateOfBirthLabel, emailLabel;
  private EditableJLabel firstNameField, middleNameField, lastNameField,
                         dateOfBirthField, emailField;

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
    if (newMember != null)
    {
      member = newMember;

      String firstName = getSafeCardString(member.getFirstName());
      String middleName = getSafeCardString(member.getMiddleName());
      String lastName = getSafeCardString(member.getLastName());
      String dobString = getSafeCardString(member.getDateOfBirthString());
      String email = getSafeCardString(member.getEmail());
      String addressLine1 = getSafeCardString(member.getAddressLine1());
      String addressLine2 = getSafeCardString(member.getAddressLine2());

      firstNameField.setInitialText(firstName);
      middleNameField.setInitialText(middleName);
      lastNameField.setInitialText(lastName);
      dateOfBirthField.setInitialText(dobString);
      emailField.setInitialText(email);
      address1Field.setInitialText(addressLine1);
      address2Field.setInitialText(addressLine2);
    }
    else
    {
      ChurchMemberManager.logbook.log(Logbook.WARNING, "Received a null member in the " +
                                                       "setMember() method of MemberCard.");
    }
    //repaint();
  }



  @Override
  protected void setupCardComponents()
  {
    //this.member = member;
    super.setupCardComponents();
    firstNameLabel = new JLabel("First Name:");
    middleNameLabel = new JLabel("Middle Name:");
    lastNameLabel = new JLabel("Last Name:");
    dateOfBirthLabel = new JLabel("Date Of Birth:");
    emailLabel = new JLabel("Email:");

    firstNameField = new EditableJLabel("FIRST NAME HERE");
    middleNameField = new EditableJLabel("MIDDLE NAME HERE");
    lastNameField = new EditableJLabel("LAST NAME HERE");
    dateOfBirthField = new EditableJLabel("DOB HERE");
    emailField = new EditableJLabel("EMAIL ADDRESS HERE");

    applyChangesButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        updateChangedMemberFields();
        /*
        member.setFirstName(firstNameField.getText());
        member.setMiddleName(middleNameField.getText());
        member.setLastName(lastNameField.getText());
        member.setDateOfBirthString(dateOfBirthField.getText());
        member.setEmail(emailField.getText());
        */
        ChurchMemberManager.dbAccess.updateMember(member);
        ChurchMemberManager.reloadMemberList();
      }
    });
    if (member != null)
    {
      setMember(member);
    }
  }
  private void updateChangedMemberFields()
  {
    for (JComponent component : componentList)
    {
      if (component instanceof EditableJLabel)
      {
        EditableJLabel temp = (EditableJLabel)component;
        if (temp.isChanged())
        {
          if (component == firstNameField)
          {
            member.setFirstName(temp.getText());
          }
          else if (component == middleNameField)
          {
            member.setMiddleName(temp.getText());
          }
          else if (component == lastNameField)
          {
            member.setLastName(temp.getText());
          }
          else if (component == dateOfBirthField)
          {
            member.setDateOfBirthString(temp.getText());
          }
          else if (component == emailField)
          {
            member.setEmail(temp.getText());
          }
        }
      }
    }
  }
  @Override
  protected void populateComponentList()
  {
    super.populateComponentList();
    componentList.add(firstNameLabel);
    componentList.add(firstNameField);
    componentList.add(middleNameLabel);
    componentList.add(middleNameField);
    componentList.add(lastNameLabel);
    componentList.add(lastNameField);
    componentList.add(dateOfBirthLabel);
    componentList.add(dateOfBirthField);
    componentList.add(emailLabel);
    componentList.add(emailField);
  }



  @Override
  public void configureLayout()
  {
    super.configureLayout();
    int m = 5; //m = margin
    JLabel longestLabel = middleNameLabel;
    layout.putConstraint(SpringLayout.WEST, longestLabel, 10, SpringLayout.EAST, imagePanel);

    layout.putConstraint(SpringLayout.NORTH, firstNameLabel, m, SpringLayout.NORTH, imagePanel);
    layout.putConstraint(SpringLayout.EAST, firstNameLabel, 0, SpringLayout.EAST, longestLabel);

    layout.putConstraint(SpringLayout.NORTH, firstNameField, 0, SpringLayout.NORTH, firstNameLabel);
    layout.putConstraint(SpringLayout.SOUTH, firstNameField, 0, SpringLayout.SOUTH, firstNameLabel);
    layout.putConstraint(SpringLayout.WEST, firstNameField, m, SpringLayout.EAST, firstNameLabel);

    layout.putConstraint(SpringLayout.NORTH, middleNameLabel, m, SpringLayout.SOUTH, firstNameLabel);
    //layout.putConstraint(SpringLayout.EAST, middleNameLabel, 0, SpringLayout.EAST, firstNameLabel);

    layout.putConstraint(SpringLayout.NORTH, middleNameField, 0, SpringLayout.NORTH, middleNameLabel);
    layout.putConstraint(SpringLayout.SOUTH, middleNameField, 0, SpringLayout.SOUTH, middleNameLabel);
    layout.putConstraint(SpringLayout.WEST, middleNameField, m, SpringLayout.EAST, middleNameLabel);
    
    layout.putConstraint(SpringLayout.NORTH, lastNameLabel, m, SpringLayout.SOUTH, middleNameLabel);
    layout.putConstraint(SpringLayout.EAST, lastNameLabel, 0, SpringLayout.EAST, longestLabel);
    
    layout.putConstraint(SpringLayout.NORTH, lastNameField, 0, SpringLayout.NORTH, lastNameLabel);
    layout.putConstraint(SpringLayout.SOUTH, lastNameField, 0, SpringLayout.SOUTH, lastNameLabel);
    layout.putConstraint(SpringLayout.WEST, lastNameField, m, SpringLayout.EAST, lastNameLabel);

    layout.putConstraint(SpringLayout.NORTH, dateOfBirthLabel, m, SpringLayout.SOUTH, lastNameLabel);
    layout.putConstraint(SpringLayout.EAST, dateOfBirthLabel, 0, SpringLayout.EAST, longestLabel);

    layout.putConstraint(SpringLayout.NORTH, dateOfBirthField, 0, SpringLayout.NORTH, dateOfBirthLabel);
    layout.putConstraint(SpringLayout.SOUTH, dateOfBirthField, 0, SpringLayout.SOUTH, dateOfBirthLabel);
    layout.putConstraint(SpringLayout.WEST, dateOfBirthField, m, SpringLayout.EAST, dateOfBirthLabel);

    layout.putConstraint(SpringLayout.NORTH, emailLabel, m, SpringLayout.SOUTH, dateOfBirthLabel);
    layout.putConstraint(SpringLayout.EAST, emailLabel, 0, SpringLayout.EAST, longestLabel);

    layout.putConstraint(SpringLayout.NORTH, emailField, 0, SpringLayout.NORTH, emailLabel);
    layout.putConstraint(SpringLayout.SOUTH, emailField, 0, SpringLayout.SOUTH, emailLabel);
    layout.putConstraint(SpringLayout.WEST, emailField, m, SpringLayout.EAST, emailLabel);

    //layout.putConstraint(SpringLayout.NORTH, address1Label, m, SpringLayout.SOUTH, emailLabel);
    //layout.putConstraint(SpringLayout.WEST, address1Label, 0, SpringLayout.WEST, lastNameLabel);
  }
}
