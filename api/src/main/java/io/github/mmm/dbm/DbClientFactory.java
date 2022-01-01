package io.github.mmm.dbm;

import io.github.mmm.dbm.api.impl.DbClientFactoryManager;
import io.github.mmm.dbm.config.DbConfig;

/**
 * Interface for a factory used to create a {@link DbClient}.
 */
public interface DbClientFactory {

  /**
   * @param config the {@link DbConfig configuration} to access the database.
   * @return the {@link DbClient} for the given configuration.
   */
  DbClient create(DbConfig config);

  /**
   * @return the instance of {@link DbClientFactory}.
   */
  public static DbClientFactory get() {

    return DbClientFactoryManager.INSTANCE;
  }

}
