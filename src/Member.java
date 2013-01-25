/*
  Andrew Darwin
  www.adarwin.com
  January 2013
*/

package com.adarwin.pcbc;

import java.sql.Date;

public class Member
{
  private int id;
  private boolean currentMember;
  private String firstName, middleName, lastName, cellPhone, email, blurb;
  private String addressLine1, addressLine2;
  private Date dateOfBirth;


  public Member()
  {
    this(0);
  }
  public Member(int id)
  {
    this.id = id;
    currentMember = false;
    firstName = "";
    middleName = "";
    lastName = "";
    cellPhone = "";
    email = "";
    blurb = "";
    addressLine1 = "";
    addressLine2 = "";
  }

  public void setID(int number) { id = number; }
  public void setFirstName(String name) { firstName = name; }
  public void setMiddleName(String name) { middleName = name; }
  public void setLastName(String name) { lastName = name; }
  public void setCellPhone(String cellNumber) { cellPhone = cellNumber; }
  public void setEmail(String email) { this.email = email; }
  public void setBlurb(String blurb) { this.blurb = blurb; }
  public void setCurrent(boolean current) { currentMember = current; }
  public void setDateOfBirth(Date newDOB) { dateOfBirth = newDOB; }
  public void setAddressLine1(String value) { addressLine1 = value; }
  public void setAddressLine2(String value) { addressLine2 = value; }

  public String getFirstName() { return firstName; }
  public String getMiddleName() { return middleName; }
  public String getLastName() { return lastName; }
  public String getCellPhone() { return cellPhone; }
  public String getEmail() { return email; }
  public String getBlurb() { return blurb; }
  public boolean isCurrent() { return currentMember; }
  public Date getDateOfBirth() { return dateOfBirth; }
  public String getAddressLine1() { return addressLine1; }
  public String getAddressLine2() { return addressLine2; }

  @Override
  public String toString()
  {
    String newline = System.getProperty("line.separator");
    String output = newline;
    output += "Name: " + firstName + " ";
    if (middleName != null) output += middleName + " ";
    output += lastName + newline;
    output += "ID: " + id + newline;
    output += "Cell: " + cellPhone + newline;
    output += "Email: " + email + newline;
    output += "Blurb: " + blurb + newline;
    return output;
  }
}
