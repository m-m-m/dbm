/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.dbm.config;

import io.github.mmm.bean.BeanFactory;
import io.github.mmm.bean.Mandatory;
import io.github.mmm.bean.ReadableBean;
import io.github.mmm.bean.WritableBean;
import io.github.mmm.property.string.PasswordProperty;
import io.github.mmm.property.string.StringProperty;
import io.github.mmm.property.temporal.duration.DurationProperty;

/**
 * {@link ReadableBean} with the configuration to connect with database.
 *
 * @since 1.0.0
 */
public interface DbConfig extends WritableBean {

  /**
   * @return the connection URL.
   */
  @Mandatory
  StringProperty Url();

  /**
   * @return the login of the user to connect.
   */
  StringProperty Username();

  /**
   * @return the password of the user to connect.
   */
  PasswordProperty Password();

  /**
   * @return the name of the database inside DB server to connect (by default). Only used by DBs that support this. May
   *         be {@code StringProperty#isEmpty() empty}.
   */
  StringProperty DatabaseName();

  /**
   * @return the optional connect timeout (if connection to DB cannot be established within this duration an exception
   *         is thrown).
   */
  DurationProperty ConnectTimeout();

  /**
   * @return the optional statement timeout (if a SQL statement cannot be completed within this duration an exception is
   *         thrown).
   */
  DurationProperty StatementTimeout();

  /**
   * @return the optional lock wait timeout (if a lock cannot be acquired within this duration an exception is thrown).
   */
  DurationProperty LockWaitTimeout();

  @Override
  default String doToString() {

    return Username().get() + "@" + Url().get();
  }

  /**
   * @return a new instance of {@link DbConfig}.
   */
  static DbConfig of() {

    return BeanFactory.get().create(DbConfig.class);
  }

  /**
   * @param url the {@link #Url()}.
   * @param username the {@link #Username()}.
   * @param password the {@link #Password()}.
   * @return a new {@link DbConfig#isReadOnly() read-only} instance of {@link DbConfig} with the given parameters.
   */
  static DbConfig ofReadOnly(String url, String username, String password) {

    DbConfig config = BeanFactory.get().create(DbConfig.class);
    config.Url().set(url);
    config.Username().set(username);
    config.Password().set(password);
    return config;
  }

}
