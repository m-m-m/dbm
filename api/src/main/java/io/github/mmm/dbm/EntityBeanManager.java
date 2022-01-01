/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.dbm;

import java.util.Collection;
import java.util.List;

import org.reactivestreams.Publisher;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.bean.sql.delete.DeleteStatement;
import io.github.mmm.entity.bean.sql.insert.InsertStatement;
import io.github.mmm.entity.bean.sql.select.SelectStatement;
import io.github.mmm.entity.bean.sql.update.UpdateStatement;
import io.github.mmm.entity.id.Id;
import io.github.mmm.entity.link.Link;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Interface for a generic manager mapping {@link EntityBean}s to a persistent store (database). It is the analogy to
 * the JPA {@code EntityManager} interface.
 *
 * @since 1.0.0
 */
public interface EntityBeanManager {

  /**
   * @param <E> type of the {@link EntityBean}.
   * @param id the {@link Id} (primary key) of the requested {@link EntityBean}.
   * @return the {@link EntityBean} for the given {@link Id} or {@link Mono#empty()} if no such entity exists.
   */
  <E extends EntityBean> Mono<E> findById(Id<E> id);

  /**
   * @param <E> type of the {@link EntityBean}.
   * @param link the {@link Link} pointing to the {@link EntityBean} to find.
   * @return the {@link EntityBean} for the given {@link Link} or {@link Mono#empty()} if no such entity exists.
   */
  default <E extends EntityBean> Mono<E> findById(Link<E> link) {

    if (link != null) {
      Id<E> id = link.getId();
      if (id != null) {
        return findById(id);
      }
    }
    return Mono.empty();
  }

  /**
   * @param <E> type of the {@link EntityBean}.
   * @param select the {@link SelectStatement} used as query. Has to match to a single result or nothing.
   * @return the {@link EntityBean} or {@link Mono#empty()} if not matched.
   */
  <E extends EntityBean> Mono<E> find(SelectStatement<E> select);

  /**
   * @param <E> type of the {@link EntityBean}.
   * @param select the {@link SelectStatement} used as query.
   * @return the {@link List} of matching {@link EntityBean}s.
   */
  <E extends EntityBean> Flux<E> findAll(SelectStatement<E> select);

  /**
   * @param <E> type of the {@link EntityBean}.
   * @param ids the {@link Iterable} with the {@link Id}s of the requested {@link EntityBean}s.
   * @return the {@link EntityBean}s for all given {@link Id}s.
   */
  default <E extends EntityBean> Flux<E> findAllById(Collection<Id<E>> ids) {

    return findAllById(Flux.fromIterable(ids));
  }

  /**
   * @param <E> type of the {@link EntityBean}.
   * @param ids the {@link Publisher} with the {@link Id}s of the requested {@link EntityBean}s.
   * @return the {@link EntityBean}s for all given {@link Id}s.
   */
  <E extends EntityBean> Flux<E> findAllById(Publisher<Id<E>> ids);

  /**
   * @param bean the {@link EntityBean} to save.
   * @return {@link Mono} of {@link Void} to distinguish success or error.
   */
  Mono<Void> save(EntityBean bean);

  /**
   * @param id the {@link Id} (primary key) of the {@link EntityBean} to delete.
   * @return {@link true} if deleted, {@code false} if no such {@link Id} exists.
   */
  Mono<Boolean> deleteById(Id<?> id);

  /**
   * @param bean the {@link EntityBean} to delete.
   * @return {@link true} if deleted, {@code false} otherwise.
   */
  default Mono<Boolean> delete(EntityBean bean) {

    if (bean != null) {
      Id<?> id = Id.from(bean);
      if (id != null) {
        return deleteById(id);
      }
    }
    return Mono.just(Boolean.FALSE);
  }

  /**
   * @param link the {@link Link} pointing to the {@link EntityBean} to delete.
   * @return {@link true} if deleted, {@code false} otherwise.
   */
  default Mono<Boolean> deleteById(Link<?> link) {

    if (link != null) {
      Id<?> id = link.getId();
      if (id != null) {
        return deleteById(id);
      }
    }
    return Mono.just(Boolean.FALSE);
  }

  /**
   * @param update the {@link UpdateStatement} to perform.
   * @return the number of records updated.
   */
  Mono<Long> save(UpdateStatement<?> update);

  /**
   * @param insert the {@link InsertStatement} to perform.
   * @return {@link Mono} of {@link Void} to distinguish success or error.
   */
  Mono<Void> save(InsertStatement<?> insert);

  /**
   * @param delete the {@link DeleteStatement} to perform.
   * @return the number of records deleted.
   */
  Mono<Long> delete(DeleteStatement<?> delete);

}
