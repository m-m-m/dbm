/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.dbm.config;

/**
 * Implementation of {@link DbConfig} as immutable value type.
 */
public class DbConfigType extends AbstractDbConfig {

  private final String url;

  private final String username;

  private final String password;

  private final String databaseName;

  /**
   * The constructor.
   *
   * @param url the {@link #getUrl() url}.
   * @param username the {@link #getUsername() username}.
   * @param password the {@link #getPassword() password}.
   */
  public DbConfigType(String url, String username, String password) {

    this(url, username, password, DEFAULT_DB_NAME);
  }

  /**
   * The constructor.
   *
   * @param url the {@link #getUrl() url}.
   * @param username the {@link #getUsername() username}.
   * @param password the {@link #getPassword() password}.
   * @param databaseName the {@link #getDatabaseName() database name}.
   */
  public DbConfigType(String url, String username, String password, String databaseName) {

    super();
    this.url = url;
    this.username = username;
    this.password = password;
    this.databaseName = databaseName;
  }

  /**
   * The constructor.
   *
   * @param config the {@link DbConfig} to copy.
   */
  public DbConfigType(DbConfig config) {

    this(config.getUrl(), config.getUsername(), config.getPassword(), config.getDatabaseName());
  }

  @Override
  public String getUrl() {

    return this.url;
  }

  @Override
  public String getUsername() {

    return this.username;
  }

  @Override
  public String getPassword() {

    return this.password;
  }

  @Override
  public String getDatabaseName() {

    return this.databaseName;
  }

}
