/*
  Andrew Darwin
  www.adarwin.com
  January 2013
*/

package com.adarwin.pcbc;

public class Member
{
  private int id;
  private boolean currentMember;
  private String firstName, middleName, lastName, cellPhone, email, blurb;


  public Member()
  {
    this(0);
  }
  public Member(int id)
  {
    this.id = id;
    currentMember = false;
    firstName = "";
    lastName = "";
    cellPhone = "";
    email = "";
    blurb = "";
  }

  public void setFirstName(String name) { firstName = name; }
  public void setMiddleName(String name) { middleName = name; }
  public void setLastName(String name) { lastName = name; }
  public void setCellPhone(String cellNumber) { cellPhone = cellNumber; }
  public void setEmail(String email) { this.email = email; }
  public void setBlurb(String blurb) { this.blurb = blurb; }
  public void setCurrent(boolean current) { currentMember = current; }

  public String getFirstName() { return firstName; }
  public String getMiddleName() { return middleName; }
  public String getLastName() { return lastName; }
  public String getCellPhone() { return cellPhone; }
  public String getEmail() { return email; }
  public String getBlurb() { return blurb; }
  public boolean isCurrent() { return currentMember; }
}
