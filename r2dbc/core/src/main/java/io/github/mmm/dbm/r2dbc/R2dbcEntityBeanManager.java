package io.github.mmm.dbm.r2dbc;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Supplier;

import org.reactivestreams.Publisher;

import io.github.mmm.bean.ReadableBean;
import io.github.mmm.bean.WritableBean;
import io.github.mmm.dbm.base.AbstractEntityBeanManager;
import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.bean.db.dialect.DbDialect;
import io.github.mmm.entity.bean.db.statement.AbstractStatement;
import io.github.mmm.entity.bean.db.statement.DbStatementFormatter;
import io.github.mmm.entity.bean.db.statement.select.Select;
import io.github.mmm.entity.bean.db.statement.select.SelectStatement;
import io.github.mmm.entity.bean.db.statement.select.Result.ResultEntry;
import io.github.mmm.property.criteria.CriteriaParameters;
import io.github.mmm.property.criteria.CriteriaParametersIndexed;
import io.github.mmm.property.criteria.CriteriaParametersNamed;
import io.r2dbc.spi.ColumnMetadata;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.Result;
import io.r2dbc.spi.Statement;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementation of {@link AbstractEntityBeanManager} for R2DBC.
 *
 */
public class R2dbcEntityBeanManager extends AbstractEntityBeanManager {

  private final Connection connection;

  private final DbDialect dialect;

  /**
   * The constructor.
   *
   * @param connection the R2DBC {@link Connection}.
   * @param dialect the {@link #getDialect() SQL dialect}.
   */
  public R2dbcEntityBeanManager(Connection connection, DbDialect dialect) {

    super();
    this.connection = connection;
    this.dialect = dialect;
  }

  @Override
  public DbDialect getDialect() {

    return this.dialect;
  }

  @Override
  public <R extends EntityBean> Mono<R> find(SelectStatement<R> select) {

    if (select == null) {
      return Mono.empty();
    }
    EntityBean entity = select.getFrom().getEntity();
    Statement statement = createStatement(select);
    return null; // Mono.from(statement.execute()).map(r -> map(r, entity));
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  public <R> Flux<R> findAll(SelectStatement<R> selectStatement) {

    if (selectStatement == null) {
      return Flux.empty();
    }
    Statement statement = createStatement(selectStatement);
    Select<R> select = selectStatement.getSelect();
    if (select.isSelectSingle()) {
      return Flux.from(statement.execute()).flatMap(r -> mapToSingle(r, select));
    } else if (select.isSelectResult()) {
      return (Flux) Flux.from(statement.execute()).flatMap(r -> mapToResult(r, select));
    } else {
      WritableBean prototype;
      if (select.isSelectEntity()) {
        prototype = selectStatement.getFrom().getEntity();
      } else {
        prototype = (WritableBean) select.getResultBean();
      }
      Objects.requireNonNull(prototype, "bean");
      return Flux.from(statement.execute()).flatMap(r -> mapToBean(r, select, prototype));
    }
  }

  /**
   * @param <R> type of the result.
   * @param r2result the R2DBC {@link Result}.
   * @param select the {@link Select} clause.
   * @return the {@link Publisher} of the results.
   */
  @SuppressWarnings("unchecked")
  protected <R> Publisher<R> mapToSingle(Result r2result, Select<R> select) {

    return r2result.map((row, rowMetadata) -> {

      // TODO potential type conversion from DB type to property type.
      return (R) row.get(0);
    });
  }

  /**
   * @param r2result the R2DBC {@link Result}.
   * @param select the {@link Select} clause.
   * @return the {@link Publisher} of the results.
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  protected Publisher<io.github.mmm.entity.bean.db.statement.select.Result> mapToResult(Result r2result, Select<?> select) {

    return r2result.map((row, rowMetadata) -> {
      List<? extends ColumnMetadata> columnMetadatas = row.getMetadata().getColumnMetadatas();
      ResultEntry[] entries = new ResultEntry[columnMetadatas.size()];
      int i = 0;
      for (ColumnMetadata col : columnMetadatas) {
        String columnName = col.getName();
        Object value = row.get(columnName);
        Supplier<?> selection = select.getSelections().get(i);
        entries[i++] = new ResultEntry(selection, value);
      }
      return new io.github.mmm.entity.bean.db.statement.select.Result(entries);
    });
  }

  /**
   * @param <R> type of the result.
   * @param r2result the R2DBC {@link Result}.
   * @param select the {@link Select} clause.
   * @param prototype the {@link WritableBean} to use as prototype to create the actual result objects.
   * @return the {@link Publisher} of the results.
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  protected <R> Publisher<R> mapToBean(Result r2result, Select<R> select, WritableBean prototype) {

    return r2result.map((row, rowMetadata) -> {
      WritableBean bean = ReadableBean.newInstance(prototype);
      for (ColumnMetadata col : row.getMetadata().getColumnMetadatas()) {
        String columnName = col.getName();
        Object value = row.get(columnName);
        String propertyName = columnName;
        bean.set(propertyName, value, (Class) col.getJavaType());
      }
      return (R) bean;
    });
  }

  /**
   * @param statement the {@link AbstractStatement} to convert.
   * @return the converted R2DBC {@link Statement}.
   */
  protected Statement createStatement(AbstractStatement<?> statement) {

    DbStatementFormatter formatter = getDialect().createFormatter();
    String sql = formatter.onStatement(statement).toString();
    Statement r2statement = this.connection.createStatement(sql);
    CriteriaParameters parameters = formatter.getCriteriaFormatter().getParameters();
    if (parameters instanceof CriteriaParametersNamed) {
      Map<String, Object> params = ((CriteriaParametersNamed) parameters).getParameters();
      for (Entry<String, Object> entry : params.entrySet()) {
        Object value = entry.getValue();
        r2statement.bind(entry.getKey(), value);
      }
    } else if (parameters instanceof CriteriaParametersIndexed) {
      List<Object> params = ((CriteriaParametersIndexed) parameters).getParameters();
      for (int i = 0; i < params.size(); i++) {
        r2statement.bind(i, params.get(i));
      }
    } else {
      throw new IllegalStateException();
    }
    return r2statement;
  }

}
