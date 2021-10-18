/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.dbm;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Interface for mapping between {@link io.github.mmm.entity.bean.EntityBean}s and database.
 *
 * @since 1.0.0
 */
public interface Dbm extends AutoCloseable {

  /**
   * @param <T> type of the result.
   * @param lambda the (lambda) {@link Function} to be executed within a transaction. It will receive the
   *        {@link DbmConnection}.
   * @return the result of your {@link Function}. Will be {@code null} if your {@link Function} returned {@code null}.
   */
  <T> T doInTx(Function<DbmConnection, T> lambda);

  /**
   * @param databaseName the name of the database inside the OrientDB server to connect to. See
   *        {@link io.github.mmm.dbm.config.DbConfig#getDatabaseName()}.
   * @param lambda the (lambda) {@link Consumer} to be executed within a transaction. It will receive the
   *        {@link DbmConnection}.
   */
  default void doInTx(Consumer<DbmConnection> lambda) {

    doInTx(x -> {
      lambda.accept(x);
      return null;
    });
  }

  @Override
  void close();

}
