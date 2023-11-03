package io.github.mmm.dbm.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.reactivestreams.Publisher;

import io.github.mmm.bean.BeanFactory;
import io.github.mmm.bean.WritableBean;
import io.github.mmm.bean.property.BeanProperty;
import io.github.mmm.dbm.EntityBeanManager;
import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.bean.db.dialect.DbDialect;
import io.github.mmm.entity.bean.db.statement.delete.Delete;
import io.github.mmm.entity.bean.db.statement.delete.DeleteStatement;
import io.github.mmm.entity.bean.db.statement.insert.InsertStatement;
import io.github.mmm.entity.bean.db.statement.select.SelectEntity;
import io.github.mmm.entity.bean.db.statement.select.SelectStatement;
import io.github.mmm.entity.bean.db.statement.update.UpdateStatement;
import io.github.mmm.entity.id.Id;
import io.github.mmm.property.WritableProperty;
import io.github.mmm.value.PropertyPath;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Abstract base implementation of {@link EntityBeanManager}.
 *
 * @since 1.0.0
 */
public abstract class AbstractEntityBeanManager implements EntityBeanManager {

  /**
   * @return the {@link DbDialect} of the database.
   */
  public abstract DbDialect getDialect();

  @Override
  public <E extends EntityBean> Flux<E> findAllById(Collection<Id<E>> ids) {

    if (ids == null) {
      return Flux.empty();
    }
    SelectStatement<E> select = selectById(ids);
    return findAll(select);
  }

  /**
   * @param <E> type of the {@link EntityBean}.
   * @param ids the {@link Publisher} with the {@link Id}s of the requested {@link EntityBean}s.
   * @return the {@link EntityBean}s for all given {@link Id}s.
   */
  @Override
  public <E extends EntityBean> Flux<E> findAllById(Publisher<Id<E>> ids) {

    return Flux.from(ids).buffer().filter(idList -> !idList.isEmpty()).concatMap(idList -> {
      if (idList.isEmpty()) {
        return Flux.empty();
      }
      SelectStatement<E> select = selectById(idList);
      return findAll(select);
    });
  }

  /**
   * @param <E> type of the {@link EntityBean}.
   * @param ids the {@link Collection} with the {@link Id}s of the {@link EntityBean}s to delete.
   * @return the {@link DeleteStatement} to delete by the given {@link Id}s.
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  protected <E extends EntityBean> DeleteStatement<E> deleteById(Collection<Id<E>> ids) {

    if ((ids == null) || ids.isEmpty()) {
      return null;
    }
    Id<E> id = ids.iterator().next();
    Class<E> entityType = id.getEntityClass();
    E entity = BeanFactory.get().create(entityType);
    return new Delete().from(entity).where(entity.Id().in((Collection) ids)).get();
  }

  /**
   * @param <E> type of the {@link EntityBean}.
   * @param ids the {@link Collection} with the {@link Id}s of the {@link EntityBean}s to select.
   * @return the {@link SelectStatement} to select from the given {@link Id}s.
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  protected <E extends EntityBean> SelectStatement<E> selectById(Collection<Id<E>> ids) {

    if ((ids == null) || ids.isEmpty()) {
      return null;
    }
    Id<E> id = ids.iterator().next();
    Class<E> entityType = id.getEntityClass();
    E entity = BeanFactory.get().create(entityType);
    SelectEntity<E> select = new SelectEntity<>(entity);
    // if (entity.isDynamic()) {
    // } else {
    // List<PropertyPath<?>> properties = getProperties(entity);
    // for (PropertyPath<?> property : properties) {
    // select.and(property);
    // }
    // }
    return select.from().where(entity.Id().in((Collection) ids)).get();
  }

  private List<PropertyPath<?>> getProperties(WritableBean entity) {

    List<PropertyPath<?>> properties = new ArrayList<>();
    collectProperties(entity, properties);
    return properties;
  }

  private List<PropertyPath<?>> collectProperties(WritableBean entity, List<PropertyPath<?>> properties) {

    for (WritableProperty<?> property : entity.getProperties()) {
      if (!property.isTransient() && !property.isReadOnly()) {
        if (property instanceof BeanProperty) {
          WritableBean embedded = ((BeanProperty<?>) property).getSafe();
          if (embedded == null) {
            // LOG...
          } else {
            collectProperties(embedded, properties);
          }
        } else {
          properties.add(property);
        }
      }
    }
    return properties;
  }

  @Override
  public <E extends EntityBean> Mono<E> findById(Id<E> id) {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mono<Void> save(EntityBean bean) {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mono<Boolean> deleteById(Id<?> id) {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mono<Long> save(UpdateStatement<?> update) {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mono<Void> save(InsertStatement<?> insert) {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mono<Long> delete(DeleteStatement<?> delete) {

    // TODO Auto-generated method stub
    return null;
  }

}
