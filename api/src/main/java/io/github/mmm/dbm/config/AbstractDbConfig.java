/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.dbm.config;

/**
 * Abstract base implementation of {@link DbConfig}.
 *
 * @since 1.0.0
 */
public abstract class AbstractDbConfig implements DbConfig {

  /** Default value for {@link #getDatabaseName() database name}. */
  public static final String DEFAULT_DB_NAME = "db";

  @Override
  public String toString() {

    return getUsername() + '@' + getUrl();
  }

}
