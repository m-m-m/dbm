package io.github.mmm.dbm.r2dbc;

import java.util.HashMap;
import java.util.Map;

import io.github.mmm.dbm.DbClient;
import io.github.mmm.dbm.DbClientFactory;
import io.github.mmm.dbm.config.DbConfig;
import io.github.mmm.property.ReadableProperty;
import io.github.mmm.property.WritableProperty;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import io.r2dbc.spi.Option;

/**
 * Implementation of {@link DbClientFactory} for R2DBC.
 */
public class R2dbcClientFactory implements DbClientFactory {

  @Override
  public DbClient create(DbConfig config) {

    config.validateOrThrow();
    String url = config.Url().get();
    if (!url.startsWith("r2dbc:")) {
      return null;
    }

    ConnectionFactoryOptions.Builder builder = ConnectionFactoryOptions.parse(url).mutate();
    Map<String, Option<?>> optionsMap = new HashMap<>();
    optionsMap.put(config.Username().getName(), ConnectionFactoryOptions.USER);
    optionsMap.put(config.Password().getName(), ConnectionFactoryOptions.PASSWORD);
    optionsMap.put(config.DatabaseName().getName(), ConnectionFactoryOptions.DATABASE);
    optionsMap.put(config.ConnectTimeout().getName(), ConnectionFactoryOptions.CONNECT_TIMEOUT);
    optionsMap.put(config.StatementTimeout().getName(), ConnectionFactoryOptions.STATEMENT_TIMEOUT);
    optionsMap.put(config.LockWaitTimeout().getName(), ConnectionFactoryOptions.LOCK_WAIT_TIMEOUT);
    for (WritableProperty<?> property : config.getProperties()) {
      if (property != config.Url()) {
        configure(builder, property, optionsMap);
      }
    }
    ConnectionFactoryOptions options = builder.build();
    ConnectionFactory factory = ConnectionFactories.get(options);
    return new R2dbcClient(factory);
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  private void configure(ConnectionFactoryOptions.Builder builder, ReadableProperty<?> property,
      Map<String, Option<?>> options) {

    String name = property.getName();
    Option option = options.get(name);
    if (option == null) {
      String key = Character.toLowerCase(name.charAt(0)) + name.substring(1);
      option = Option.valueOf(key);
    }
    configure(builder, option, property);
  }

  private <T> void configure(ConnectionFactoryOptions.Builder builder, Option<T> option,
      ReadableProperty<? extends T> property) {

    T value = property.get();
    if (value == null) {
      return;
    }
    if ((value instanceof String) && ((String) value).isEmpty()) {
      return;
    }
    builder.option(option, value);
  }

}
