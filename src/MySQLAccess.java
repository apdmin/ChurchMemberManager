/*
  Andrew Darwin
  www.adarwin.com
  January 2013
*/

package com.adarwin.pcbc;

import com.adarwin.logging.Logbook;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MySQLAccess
{
  private Logbook logbook = new Logbook("MySQLAccess.log");
  private Connection connection = null;
  private Statement statement = null;
  private PreparedStatement preparedStatement = null;
  private ResultSet resultSet = null;
  private String dbName = "PantonChurch";
  private String username = "MemberManager";
  private String password = "pcbc";
  private boolean properlySetup = false;
  private String badSetupString = "Did not execute query because " +
                                  "database was not set up correctly.";

  public MySQLAccess(String dbName, String username, String password)
  {
    this.dbName = dbName;
    this.username = username;
    this.password = password;
  }


  public void backupDatabase()
  {
  }
  public void restoreDatabase()
  {
  }

  public void loadDatabase()
  {
    try
    {
      Class.forName("com.mysql.jdbc.Driver");
      String url = "jdbc:mysql://localhost/" + dbName;
      connection = DriverManager.getConnection(url, username, password);
      statement = connection.createStatement();
      properlySetup = true;
    }
    catch (ClassNotFoundException ex)
    {
      logbook.log(ex);
    }
    catch (SQLException ex)
    {
      logbook.log(ex);
    }
  }
  public ArrayList<String> getFamiliesList()
  {
    ArrayList<String> output = new ArrayList<String>();
    try
    {
      if (properlySetup)
      {
        resultSet = statement.executeQuery("select FName " +
                                           "from families " +
                                           "order by FName asc;");
        while(resultSet.next())
        {
          String name = resultSet.getString("FName");
          output.add(name);
        }
      }
      else
      {
        logbook.log(Logbook.WARNING, badSetupString);
      }
    }
    catch (SQLException ex)
    {
      logbook.log(ex);
    }
    return output;
  }


  public ArrayList<String> getMembersList()
  {
    ArrayList<String> output = new ArrayList<String>();
    try
    {
      if (properlySetup)
      {
        resultSet = statement.executeQuery("select MemFirstName, MemLastName " +
                                           "from members " +
                                           "order by MemLastName asc, MemFirstName asc;");
        while(resultSet.next())
        {
          String firstName = resultSet.getString("MemFirstName");
          String lastName = resultSet.getString("MemLastName");
          output.add(lastName + ", " + firstName);
        }
      }
      else
      {
        logbook.log(Logbook.WARNING, badSetupString);
      }
    }
    catch (SQLException ex)
    {
      logbook.log(ex);
    }
    return output;
  }




  public Member getMember(String lastName, String firstName)
  {
    Member member = new Member();
    try
    {
      if (properlySetup)
      {
        resultSet = statement.executeQuery("select * " +
                                           "from members natural join familymembers natural join families " +
                                           "where memFirstName like '" + firstName + "' " +
                                           "and memLastName like '" + lastName + "';");
        if (resultSet.next())
        {
          member.setID(resultSet.getInt("MemID"));
          member.setFirstName(resultSet.getString("MemFirstName"));
          member.setMiddleName(resultSet.getString("MemMIName"));
          member.setLastName(resultSet.getString("MemLastName"));
          member.setCellPhone(resultSet.getString("CellPhone"));
          member.setEmail(resultSet.getString("Email"));
          member.setBlurb(resultSet.getString("MemBlurb"));
          member.setCurrent(resultSet.getString("MemCurrent").equals("Yes") ? true : false);
          member.setDateOfBirth(resultSet.getDate("MemDOB"));
          member.setAddressLine1(resultSet.getString("FAddrLine1"));
          member.setAddressLine2(resultSet.getString("FAddrLine2"));
        }
      }
      else
      {
        logbook.log(Logbook.WARNING, badSetupString);
      }
    }
    catch (SQLException ex)
    {
      logbook.log(ex);
    }
    return member;
  }




  private String getStringForDBUpdate(String inputValue)
  {
    if (inputValue == null || inputValue.equalsIgnoreCase("null"))
    {
      return null;
    }
    else
    {
      return inputValue;
    }
  }




  public void updateMember(Member member)
  {
    try
    {
      if (properlySetup)
      {
        String firstName = getStringForDBUpdate(member.getFirstName());
        String middleName = getStringForDBUpdate(member.getMiddleName());
        String lastName = getStringForDBUpdate(member.getLastName());
        String dob = getStringForDBUpdate(member.getDateOfBirthString());
        String cellPhone = getStringForDBUpdate(member.getCellPhone());
        String email = getStringForDBUpdate(member.getEmail());
        String blurb = getStringForDBUpdate(member.getBlurb());
        int id = member.getID();
        int rows = statement.executeUpdate("UPDATE members SET " +
                                           "MemFirstName = '" + firstName + "', " +
                                           "MemMIName = '" + middleName + "', " +
                                           "MemLastName = '" + lastName + "', " +
                                           "MemDOB = '" + dob + "', " +
                                           "CellPhone = '" + cellPhone + "', " +
                                           "Email = '" + email + "', " +
                                           "MemBlurb = '" + blurb + "' " +
                                           "WHERE memID = " + id + " limit 1;");
        logbook.log(Logbook.INFO, "Updated " + rows + " rows");
      }
    }
    catch (SQLException ex)
    {
      logbook.log(ex);
    }
  }



  public void addNewUser(String username, String password)
  {
  }
  public void removeUser(String username)
  {
  }
}
