/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.dbm.config;

/**
 * Implementation of {@link DbConfig} as plain old Java bean.
 */
public class DbConfigBean extends AbstractDbConfig {

  private String url;

  private String username;

  private String password;

  private String databaseName;

  /**
   * The constructor.
   */
  public DbConfigBean() {

    super();
    this.databaseName = DEFAULT_DB_NAME;
  }

  @Override
  public String getUrl() {

    return this.url;
  }

  /**
   * @param url new value of {@link #getUrl()}.
   */
  public void setUrl(String url) {

    this.url = url;
  }

  @Override
  public String getUsername() {

    return this.username;
  }

  /**
   * @param username new value of {@link #getUsername()}.
   */
  public void setUsername(String username) {

    this.username = username;
  }

  @Override
  public String getPassword() {

    return this.password;
  }

  /**
   * @param password new value of {@link #getPassword()}.
   */
  public void setPassword(String password) {

    this.password = password;
  }

  @Override
  public String getDatabaseName() {

    return this.databaseName;
  }

  /**
   * @param databaseName new value of {@link #getDatabaseName()}.
   */
  public void setDatabaseName(String databaseName) {

    this.databaseName = databaseName;
  }

}
