package io.github.mmm.dbm.r2dbc;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.reactivestreams.Publisher;

import io.github.mmm.bean.ReadableBean;
import io.github.mmm.bean.WritableBean;
import io.github.mmm.dbm.base.AbstractEntityBeanManager;
import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.bean.sql.AbstractStatement;
import io.github.mmm.entity.bean.sql.SqlDialect;
import io.github.mmm.entity.bean.sql.SqlFormatter;
import io.github.mmm.entity.bean.sql.select.Select;
import io.github.mmm.entity.bean.sql.select.SelectStatement;
import io.github.mmm.property.criteria.CriteriaSqlParameters;
import io.github.mmm.property.criteria.CriteriaSqlParametersIndexed;
import io.github.mmm.property.criteria.CriteriaSqlParametersNamed;
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

  private final SqlDialect dialect;

  /**
   * The constructor.
   *
   * @param connection the R2DBC {@link Connection}.
   * @param dialect the {@link #getDialect() SQL dialect}.
   */
  public R2dbcEntityBeanManager(Connection connection, SqlDialect dialect) {

    super();
    this.connection = connection;
    this.dialect = dialect;
  }

  @Override
  public SqlDialect getDialect() {

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

  @SuppressWarnings("unchecked")
  @Override
  public <R> Flux<R> findAll(SelectStatement<R> selectStatement) {

    if (selectStatement == null) {
      return Flux.empty();
    }
    Select<R> select = selectStatement.getSelect();
    R result = select.getResultBean();
    if (result == null) {
      int size = select.getSelections().size();
      if (size == 0) {
        result = (R) selectStatement.getFrom().getEntity();
      }
    }
    Statement statement = createStatement(selectStatement);
    return Flux.from(statement.execute()).flatMap(r -> map(r, result));
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  protected <R> Publisher<R> mapToBean(Result result, WritableBean prototype, Select<R> select) {

    return result.map((row, rowMetadata) -> {
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

    SqlFormatter formatter = getDialect().createFormatter();
    String sql = formatter.onStatement(statement).toString();
    Statement r2statement = this.connection.createStatement(sql);
    CriteriaSqlParameters parameters = formatter.getCriteriaFormatter().getParameters();
    if (parameters instanceof CriteriaSqlParametersNamed) {
      Map<String, Object> params = ((CriteriaSqlParametersNamed) parameters).getParameters();
      for (Entry<String, Object> entry : params.entrySet()) {
        Object value = entry.getValue();
        r2statement.bind(entry.getKey(), value);
      }
    } else if (parameters instanceof CriteriaSqlParametersIndexed) {
      List<Object> params = ((CriteriaSqlParametersIndexed) parameters).getParameters();
      for (int i = 0; i < params.size(); i++) {
        r2statement.bind(i, params.get(i));
      }
    } else {
      throw new IllegalStateException();
    }
    return r2statement;
  }

}
