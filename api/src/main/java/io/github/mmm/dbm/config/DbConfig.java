/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.dbm.config;

/**
 * Configuration to connect with database.
 *
 * @since 1.0.0
 */
public interface DbConfig {

  /**
   * @return the connection URL.
   */
  String getUrl();

  /**
   * @return the login of the user to connect.
   */
  String getUsername();

  /**
   * @return the password of the user to connect.
   */
  String getPassword();

  /**
   * @return the name of the database inside DB server to connect (by default). Only used by DBs that support this.
   */
  String getDatabaseName();

}
