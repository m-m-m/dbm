/*
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

/**
 * Provides the DB mapping using R2DBC.<br>
 *
 * @provides io.github.mmm.dbm.DbClientFactory
 */
module io.github.mmm.dbm.r2dbc {

  requires transitive io.github.mmm.dbm.base;

  requires r2dbc.spi;

  provides io.github.mmm.dbm.DbClientFactory with io.github.mmm.dbm.r2dbc.R2dbcClientFactory;

  exports io.github.mmm.dbm.r2dbc;

}
