/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.dbm;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.dao.Dao;

/**
 * Interface for the actual database bean mapping (DBM). It represents a connection to the database as well as a
 * transaction.
 *
 * @since 1.0.0
 */
public interface Tx {

  /**
   * @param <E> type of the {@link EntityBean}.
   * @param entityType the {@link Class} of the {@link EntityBean} to manage.
   * @return the {@link Dao} to manage the given {@link EntityBean}.
   */
  <E extends EntityBean> Dao<E> getDaoByEntity(Class<E> entityType);

  /**
   * @param <DAO> type of the {@link Dao}.
   * @param daoType the {@link Class} reflecting the requested {@link Dao}.
   * @return the requested {@link Dao} instance.
   */
  <DAO extends Dao<?>> DAO getDao(Class<DAO> daoType);

}
