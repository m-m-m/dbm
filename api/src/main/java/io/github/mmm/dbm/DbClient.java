/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.dbm;

import org.reactivestreams.Publisher;

/**
 * Interface for mapping {@link io.github.mmm.entity.bean.EntityBean}s from/to a database.
 *
 * @since 1.0.0
 */
public interface DbClient extends AutoCloseable {

  /**
   * @return a {@link Publisher} of the {@link EntityBeanManager} to run a transaction.
   */
  Publisher<EntityBeanManager> tx();

  @Override
  void close();

}
