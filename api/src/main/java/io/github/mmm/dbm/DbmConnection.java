/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.dbm;

import java.util.List;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.bean.sql.delete.DeleteStatement;
import io.github.mmm.entity.bean.sql.select.SelectStatement;
import io.github.mmm.entity.bean.sql.update.UpdateStatement;
import io.github.mmm.entity.id.Id;
import io.github.mmm.entity.link.Link;

/**
 * Interface for the actual database bean mapping (DBM). It represents a connection to the database as well as a
 * transaction.
 *
 * @since 1.0.0
 */
public interface DbmConnection {

  /**
   * @param <E> type of the {@link EntityBean}.
   * @param id the {@link Id} (primary key) of the requested {@link EntityBean}.
   * @return the {@link EntityBean} for the given {@link Id} or {@code null} if no such entity exists.
   */
  <E extends EntityBean> E find(Id<E> id);

  /**
   * @param <E> type of the {@link EntityBean}.
   * @param select the {@link SelectStatement} used as query.
   * @return the {@link List} of matching {@link EntityBean}s.
   */
  <E extends EntityBean> List<E> find(SelectStatement<E> select);

  /**
   * @param bean the {@link EntityBean} to save.
   */
  void save(EntityBean bean);

  /**
   * @param update the {@link UpdateStatement} to perform.
   */
  void save(UpdateStatement<?> update);

  /**
   * @param id the {@link Id} (primary key) of the {@link EntityBean} to delete.
   */
  void delete(Id<?> id);

  /**
   * @param delete the {@link DeleteStatement} to perform.
   */
  void delete(DeleteStatement<?> delete);

  /**
   * @param bean the {@link EntityBean} to delete.
   */
  default void delete(EntityBean bean) {

    if (bean != null) {
      Id<?> id = bean.Id().get();
      if (id != null) {
        delete(id);
      }
    }
  }

  /**
   * @param link the {@link Link} pointing to the {@link EntityBean} to delete.
   */
  default void delete(Link<?> link) {

    if (link != null) {
      Id<?> id = link.getId();
      if (id != null) {
        delete(id);
      }
    }
  }

  /**
   * @param <E> type of the requested {@link EntityBean}.
   * @param query the {@link SelectStatement} used to search for the quested {@link EntityBean}s.
   * @return the {@link Iterable} of the matching {@link EntityBean}s. TODO: Consider reactive API!!!
   */
  <E extends EntityBean> Iterable<Link<E>> query(SelectStatement<E> query);

}
