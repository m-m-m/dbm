package io.github.mmm.dbm.r2dbc;

import io.github.mmm.dbm.base.AbstractEntityBeanManager;
import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.bean.sql.SqlDialect;
import io.github.mmm.entity.bean.sql.SqlFormatter;
import io.github.mmm.entity.bean.sql.select.SelectStatement;
import io.r2dbc.spi.Connection;
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
  public <E extends EntityBean> Mono<E> find(SelectStatement<E> select) {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <E extends EntityBean> Flux<E> findAll(SelectStatement<E> select) {

    if (select == null) {
      return Flux.empty();
    }
    SqlFormatter formatter = getDialect().createFormatter().onStatement(select);
    String sql = formatter.toString();
    Statement statement = this.connection.createStatement(sql);
    statement.bind(0, statement);
    Mono.from()
        .flatMapMany(connection -> connection.createStatement("SELECT firstname FROM PERSON WHERE age > $1")
            .bind("$1", 42).execute())
        .flatMap(result -> result.map((row, rowMetadata) -> row.get("firstname", String.class)));
    return null;
  }

}
