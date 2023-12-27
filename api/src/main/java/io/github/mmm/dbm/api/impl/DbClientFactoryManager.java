/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.dbm.api.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;

import io.github.mmm.base.service.ServiceHelper;
import io.github.mmm.dbm.DbClient;
import io.github.mmm.dbm.DbClientFactory;
import io.github.mmm.dbm.config.DbConfig;

/**
 * Implementation of {@link DbClientFactory}.
 */
public final class DbClientFactoryManager implements DbClientFactory {

  /** The singleton instance. */
  public static final DbClientFactory INSTANCE = new DbClientFactoryManager();

  private final List<DbClientFactory> delegates;

  private DbClientFactoryManager() {

    super();
    this.delegates = new ArrayList<>();
    ServiceHelper.all(ServiceLoader.load(DbClientFactory.class), this.delegates);
  }

  @Override
  public DbClient create(DbConfig config) {

    Objects.requireNonNull(config, "config");
    try {
      for (DbClientFactory delegate : this.delegates) {
        DbClient dbClient = delegate.create(config);
        if (dbClient != null) {
          return dbClient;
        }
      }
      throw new IllegalStateException("No client could be created for database URL " + config.Url().get());
    } catch (Exception e) {
      throw new IllegalArgumentException("Failed to create client for database URL " + config.Url().get(), e);
    }
  }

}
