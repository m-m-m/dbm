package io.github.mmm.dbm.r2dbc;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.github.mmm.dbm.DbClient;
import io.github.mmm.dbm.EntityBeanManager;
import io.github.mmm.orm.dialect.DbDialect;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;

/**
 * Implementation of {@link DbClient} using R2DBC.
 */
public class R2dbcClient implements DbClient {

  private ConnectionFactory connectionFactory;

  /**
   * The constructor.
   *
   * @param connectionFactory the R2DBC {@link ConnectionFactory}.
   */
  @SuppressWarnings("exports")
  public R2dbcClient(ConnectionFactory connectionFactory) {

    super();
    this.connectionFactory = connectionFactory;
  }

  @Override
  public Publisher<EntityBeanManager> tx() {

    return s -> {
      Subscriber<Connection> subscriber = new Subscriber<>() {

        @Override
        public void onSubscribe(Subscription subscription) {

          s.onSubscribe(subscription);
        }

        @Override
        public void onNext(Connection c) {

          DbDialect dialect = null;
          s.onNext(new R2dbcEntityBeanManager(c, dialect));
        }

        @Override
        public void onError(Throwable t) {

          // TODO exception mapping
          s.onError(t);
        }

        @Override
        public void onComplete() {

          s.onComplete();
        }

      };
      this.connectionFactory.create().subscribe(subscriber);
    };
  }

  @Override
  public void close() {

    this.connectionFactory = null;
  }

}
