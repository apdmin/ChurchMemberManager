/*
  Andrew Darwin
  www.adarwin.com
  January 2013
*/

package com.adarwin.pcbc;

import java.io.Serializable;

public class ConnectionInfo implements Serializable
{
  private String databaseName;
  private String username;
  private String password;

  public ConnectionInfo()
  {
    databaseName = "";
    username = "";
    password = "";
  }
  public ConnectionInfo(String databaseName, String username, String password)
  {
    this.databaseName = databaseName;
    this.username = username;
    this.password = password;
  }

  protected void setDatabaseName(String newName) { databaseName = newName; }
  protected void setUsername(String newName) { username = newName; }
  protected void setPassword(String newPassword) { password = newPassword; }
  protected String getDatabaseName() { return databaseName; }
  protected String getUsername() { return username; }
  protected String getPassword() { return password; }
}
