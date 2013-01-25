/*
  Andrew Darwin
  www.adarwin.com
  January 2013
*/

package com.adarwin.pcbc;

import javax.swing.JLabel;

public class FamilyCard extends Card
{
  private JLabel nameLabel;
  private Family family;

  public FamilyCard()
  {
    this(null);
  }
  public FamilyCard(Family family)
  {
    super();
    nameLabel = new JLabel("Family Name");
    this.family = family;
  }
}
